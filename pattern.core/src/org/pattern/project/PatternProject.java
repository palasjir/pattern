/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.project;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.pattern.data.Classificator;

/**
 * Represents pattern project. Pattern project is recognized when folder with
 * proect contains {@code pattern.proj} file. Project is also asumed to have
 * following folder structure:
 * <pre>
 * +-data
 *      +- image1.pattern
 * +-images
 *      +- image1.tif
 * +-analysis
 * +-pattern.proj
 * </pre>
 *
 * Files with {@code .pattern} extension are vital for image display. They form
 * {@link MultImageDataObject} which contains information about image such as
 * source image, name, detected particles.
 *
 * @author palas
 * @see MultiImageDataObject
 */
public class PatternProject implements Project, ChangeListener {

    public static final String PROP_CLASSIFICATOR = "classificator";
    public static final String IMAGES_FOLDER = "images";
    public static final String DATA_FOLDER = "data";
    public static final String ANALYSIS_FOLDER = "analysis";

    private final FileObject projectDir;
    private Classificator classificator;
    private Lookup lkp;
    private final ProjectState state;

    private transient final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    public PatternProject(FileObject dir, ProjectState state) {
        this.projectDir = dir;
        this.classificator = Classificator.load(
                projectDir.getFileObject(PatternProjectFactory.PROJECT_FILE));
        if (classificator != null) {
            classificator.addChangeListener(this);
        }
        this.state = state;
    }

    @Override
    public FileObject getProjectDirectory() {
        return projectDir;
    }

    @Override
    public Lookup getLookup() {
        if (lkp == null) {
            lkp = Lookups.fixed(new Object[]{
                this,
                new Info(),
                new PatternProjectLogicalView(this),
                state
            });
        }
        return lkp;
    }

    public FileObject getImagesFolder() {
        return projectDir.getFileObject(IMAGES_FOLDER);
    }

    public FileObject getDataFolder() {
        return projectDir.getFileObject(DATA_FOLDER);
    }

    public FileObject getAnalysisFolder() {
        return projectDir.getFileObject(ANALYSIS_FOLDER);
    }

    public Classificator getClassificator() {
        return classificator;
    }

    public void setClassificator(Classificator classificator) {
        this.classificator = classificator;
        if (classificator != null) {
            this.classificator.addChangeListener(this);
        }
        changeSupport.firePropertyChange(PROP_CLASSIFICATOR, null, classificator);
        state.markModified();
    }

    public void addChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removeChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        state.markModified();
    }

    private final class Info implements ProjectInformation {

        @StaticResource
        public static final String PROJECT_ICON = "org/pattern/resources/ic_microscope.png";

        @Override
        public Icon getIcon() {
            return new ImageIcon(ImageUtilities.loadImage(PROJECT_ICON));
        }

        @Override
        public String getName() {
            return getProjectDirectory().getName();
        }

        @Override
        public String getDisplayName() {
            return getName();
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener pcl) {
            //do nothing, won't change
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener pcl) {
            //do nothing, won't change
        }

        @Override
        public Project getProject() {
            return PatternProject.this;
        }
    }
}
