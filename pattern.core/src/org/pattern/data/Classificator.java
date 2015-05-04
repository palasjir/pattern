package org.pattern.data;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.apache.commons.io.FileUtils;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.ChangeSupport;
import org.openide.util.Exceptions;
import org.pattern.serialization.PatternGson;

/**
 * Represennts classificator for particles. Listens to property changes of its
 * labels;
 *
 * @author palas
 */
public class Classificator implements PropertyChangeListener{
    
    private static final int nColors = 10;
    
    private ClassificatorInfo info;
    private final List<ParticleLabel> labels = new ArrayList<>();
    private int selectedLabel = 0;

    private final List<Particle> examples = new ArrayList<>();
    private transient final ChangeSupport changeSupport = new ChangeSupport(this);

    public Classificator() {
        info = new ClassificatorInfo();
        labels.add(ParticleLabel.UNKNOWN);
    }

    public Classificator(String name) {
        this();
        info.setId(name);
    }

    public ClassificatorInfo getInfo() {
        return info;
    }
    
    public int labelCount(){
        return labels.size();
    }

    public List<ParticleLabel> getLabels() {
        return labels;
    }

    public void addExamples(List<Particle> examples) {
        for (Particle particle : examples) {
            addExample(particle);
        }
    }

    public void addExample(Particle particle) {
        if (particle.getLabelId() != ParticleLabel.UNKNOWN_ID) {
            examples.add(particle);
            changeSupport.fireChange();
        }
    }
    
    public void moveToNextLabel(){
        if (!labels.isEmpty()) {
            selectedLabel = ++selectedLabel % labels.size();
            changeSupport.fireChange();
        }
        
    }
    
    public void moveToPrevLabel(){
        if(!labels.isEmpty()){
            selectedLabel = --selectedLabel <= -1 ? labels.size() - 1 : selectedLabel;
            changeSupport.fireChange();
        }
    }
    
    public void setSelectedLabel(int index){
        if(index < 0 || index >= labels.size()){
            this.selectedLabel = 0;
        }else{
            selectedLabel = index;
        }  
        changeSupport.fireChange();
    }
    
    public ParticleLabel getSelectedLabel(){
        return labels.get(selectedLabel);
    }
    
    public boolean hasLabels(){
        return !labels.isEmpty();
    }

    /**
     * Only examples from data are removed.
     *
     * @param exmples
     */
    public void updateExamples(List<Particle> exmples) {
        examples.removeAll(exmples);
        for (Particle particle : exmples) {
            if (particle.isHardConstrained()) {
                addExample(particle);
            }
        }
    }

    public void addChangeListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    /**
     * Clasifies all particles in the image according to classification model
     * specified by this classifier.
     *
     * @param image image with particles to classify
     */
    public void classify(ParticleImage image) {
        for (Particle particle : image.getParticles()) {
            ParticleLabel label = clasifyKNN(5, particle);
            if(!particle.isHardConstrained()){
                image.label(particle, label);
            }
        }
    }

    /**
     * Nearest neigbour clasificator.
     *
     * @param particle
     * @return class id
     */
    private int clasifyNN(Particle particle) {
        double min = Double.MAX_VALUE;
        int indexOfNearest = 0, i = 0;
        for (Particle trainingParticle : examples) {
            double diff = particle.distanceTo(trainingParticle);
            if (diff < min) {
                min = diff;
                indexOfNearest = i;
            }
            i++;
        }
        Integer threshold = null;

        if (threshold == null || min > threshold) {
            return examples.get(indexOfNearest).getLabelId();
        } else {
            return ParticleLabel.UNKNOWN_ID;
        }
    }

    private ParticleLabel clasifyKNN(int k, Particle particle) {

        Distance[] distances = new Distance[examples.size()];
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < examples.size(); i++) {
            Particle train = examples.get(i);
            builder.append(train.id).append(";").append(train.label).append(";");
            distances[i] = new Distance();
            distances[i].index = i;
            double[] parts = particle.distanceParts(train);
            distances[i].value = particle.distanceTo(train);
            builder.append(parts[0]).append(";").append(parts[1]);
            builder.append("\n");
        }
        File file = new File("reports/"+ particle.id +".txt");
        try {
            FileUtils.writeStringToFile(file, builder.toString(), "UTF-8");
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        Arrays.sort(distances);
        
        int[] counters = new int[labels.size()];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < labels.size(); j++) {
                ParticleLabel label = labels.get(j);
                int otherId = examples.get(distances[i].index).getLabelId();
                
                if(label.getId() == otherId){ // have same id
                    counters[j]++;
                }
            }
        }
        
        int max = counters[0];
        int index = 0;
        for (int i = 1; i < counters.length; i++) {
            if(max < counters[i]){
                max = counters[i];
                index = i;
            }
        }
        
        return labels.get(index);
    }

    public void createLabel(String name) {
        int newId = labels.get(labels.size() - 1).getId() + 1;
        ParticleLabel label = new ParticleLabel(newId, name, generateColor(newId));
        label.addPropertyChangeListener(this);
        labels.add(label);
        changeSupport.fireChange();
    }
    
    private static Color generateColor(int i){
        return Color.getHSBColor((float) i / (float) nColors, 0.85f, 1.0f);
    }

    public void removeLabel(ParticleLabel label) {
        labels.remove(label);
        changeSupport.fireChange();
    }

    public ParticleLabel getLabel(int label) {
        for (ParticleLabel pl : labels) {
            if(pl.getId() == label)
                return pl;
        }
        return null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getSource().getClass().equals(ParticleLabel.class)){
            changeSupport.fireChange();
        }
    }

    private class Distance implements Comparable<Distance> {

        int index;
        double value;

        @Override
        public int compareTo(Distance o) {
            if(this.value == o.value)
                return 0;
            else if(this.value > o.value)
                return 1;
            else
                return -1;
        }

    }
    
    //<editor-fold defaultstate="collapsed" desc="Save and Load">
    public void save(FileObject file){
        save(FileUtil.toFile(file));
    }
    
    /**
     * Saves classdificator to file.
     *
     * @param file
     */
    public void save(File file) {
        //FileOutputStream fos = null;
        try {
            //gson.toJson(this, null, new JsonWriter(new FileWriterWithEncoding(file, "UTF-8")));
            String str = PatternGson.gson().toJson(this);
            FileUtils.writeStringToFile(file, str);
            info.setPath(file.getAbsolutePath());
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    public static Classificator load(FileObject file){
        return load(FileUtil.toFile(file));
    }
    
    public static Classificator load(File file) {
        try {
            if (file.exists() && file.isFile()) {
                
                String string = FileUtils.readFileToString(file);
                return PatternGson.gson().fromJson(string, Classificator.class);
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }
//</editor-fold>

}
