package org.pattern.data;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.event.ChangeListener;
import org.opencv.core.Mat;
import org.openide.util.ChangeSupport;
import org.pattern.utils.ImageConverter;

/**
 * Data structure holding single image slice which is suspicious fromconatining
 * the particles. This is the decorator of OpenCV Mat.
 *
 * @author palas.jiri
 */
public class ParticleImage {

    /**
     * Image data. Using OpenCV data structure.
     */
    private final Mat pixels;

    /**
     * Detected particles in image.
     */
    private final List<Particle> particles = new ArrayList<>();

    private final transient Set<Particle> selectedParticles = new HashSet<>();
    private final transient ChangeSupport changeSupport = new ChangeSupport(this);

    /**
     * Constructs Pattern image decorator class.
     *
     * @param data image data (pixels) in OpenCV Mat data structure
     */
    public ParticleImage(Mat data) {
        this.pixels = data;
    }

    public BufferedImage createBufferedImage() {
        return ImageConverter.toBufferedImage(pixels);
    }

    /**
     * Adds detection to this image. This changes the particle id.
     *
     * @param particle detection to add
     */
    public void assign(Particle particle) {
        particle.id = particles.size();
        particles.add(particle);
        changeSupport.fireChange();
    }

    public void remove(Particle particle) {
        for (int i = particle.id + 1; i < particles.size(); i++) {
            particles.get(i).id--;
        }
        particles.remove(particle.id);
        changeSupport.fireChange();
    }

    /**
     * Assigns multiple detections to this image. This changes the particle id.
     *
     * @param particles
     */
    public void assign(Collection<? extends Particle> particles) {
        for (Particle particle : particles) {
            ParticleImage.this.assign(particle);
        }
    }
    
    public void label(Particle p, ParticleLabel label){
        p.setLabel(label.id);
        changeSupport.fireChange();
    }

    public void labelHardly(Particle p, ParticleLabel label) {
        p.setLabel(label.id);
        if (label.isUnknown()) {
            p.setHardConstrained(false);
        }
        p.setHardConstrained(true);
        changeSupport.fireChange();
    }

    public boolean contains(double x, double y) {
        return x > 0 && x < getWidth() && y > 0 && y < getHeight();
    }

    /**
     * Getter for destection count.
     *
     * @return number of detected objects in this class
     */
    public int getDetectionCount() {
        return particles.size();
    }

    /**
     * Getter for detected objects.
     *
     * @return list of detected objects
     */
    public List<Particle> getParticles() {
        return particles;
    }

    /**
     * Getter for image depth.
     *
     * @return image width
     */
    public int getWidth() {
        return pixels.cols();
    }

    /**
     * Getter for image height.
     *
     * @return image height
     */
    public int getHeight() {
        return pixels.rows();
    }

    /**
     * Getter for list of picked particles. Picked particle is one which was
     * selected with user interaction.
     *
     * @return list of picked particles
     */
    public Set<Particle> getSelectedParticles() {
        return selectedParticles;
    }

    /**
     * Deletes picked particles from list. It also adds them to history list to
     * keep track of changes an possibility to revert deleted particles. Sets
     * picked particle to false.
     */
    public void deleteSelected() {
        for (Particle particle : selectedParticles) {
            particle.setSelected(false);
            particles.remove(particle);
        }
        selectedParticles.clear();
        for (int i = 0; i < particles.size(); i++) {
            particles.get(i).id = i;
        }
        changeSupport.fireChange();
    }

    /**
     * Inverts current selection. Such that slected particles become deselected
     * and deselected becomes selected.
     */
    public void invertSelection() {
        List<Particle> selection = new ArrayList<>();
        for (Particle particle : particles) {
            if (particle.isSelected) {
                particle.setSelected(false);
            } else {
                particle.setSelected(true);
                selection.add(particle);
            }
        }
        selectedParticles.clear();
        selectedParticles.addAll(selection);
        changeSupport.fireChange();
    }

    /**
     * Deslects all particles. Selected state of all particles is set to false.
     */
    public void deselectAll() {
        for (Particle particle : selectedParticles) {
            particle.setSelected(false);
        }
        selectedParticles.clear();
        changeSupport.fireChange();
    }

    public void setSelected(Particle p, boolean selected) {
        if (selected) {
            markSelected(p);
        } else {
            deselectParticle(p);
        }
    }
    
    public void setSelected(List<Particle> particles, boolean selected){
        for (Particle particle : particles) {
            setSelected(particle, selected);
        }
    }

    public void markSelected(Particle particle) {
        if (particles.contains(particle)) {
            selectedParticles.add(particle);
            particle.setSelected(true);
            changeSupport.fireChange();
        }
    }

    public void deselectParticle(Particle particle) {
        if (particles.contains(particle)) {
            selectedParticles.remove(particle);
            particle.setSelected(false);
            changeSupport.fireChange();
        }
    }

    public Particle findParticleById(int id) {
        for (Particle particle : particles) {
            if (particle.id == id) {
                return particle;
            }
        }
        return null;
    }

    public List<Particle> findParticlesWithLabelId(int id) {
        List<Particle> found = new ArrayList<>();
        for (Particle particle : particles) {
            if (particle.label == id) {
                found.add(particle);
            }
        }
        return found;
    }
    
    public List<Particle> findParticlesWithLabel(ParticleLabel label){
        return findParticlesWithLabelId(label.id);
    }

    public int countParticlesWithLabelId(int id) {
        int count = 0;
        for (Particle particle : particles) {
            if (particle.label == id) {
                count++;
            }
        }
        return count;
    }
    
    public int countSelectedParticlesWithLabelId(int id){
        int count = 0;
        for (Particle particle : selectedParticles) {
            if (particle.label == id) {
                count++;
            }
        }
        return count;
    }

    public Mat getPixels() {
        return pixels;
    }

    public List<Particle> getLabeledParticles() {
        List<Particle> labeled = new ArrayList<>();
        for (Particle particle : particles) {
            if (particle.hasKnownLabel()) {
                labeled.add(particle);
            }
        }
        return labeled;
    }

    public void registerListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    public void unregisterListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }

    public boolean hasParticles() {
        return !particles.isEmpty();
    }

}
