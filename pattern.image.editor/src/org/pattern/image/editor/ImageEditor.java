/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.image.editor;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import java.util.List;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.awt.Actions;
import org.openide.awt.UndoRedo;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.util.actions.Presenter;
import org.pattern.data.Classificator;
import org.pattern.data.MultiImage;
import org.pattern.data.Particle;
import org.pattern.data.ParticleImage;
import org.pattern.data.ParticleLabel;
import org.pattern.image.viewer.ParticleImageView;
import org.pattern.image.viewer.PopupProvider;
import org.pattern.image.viewer.renderer.ClassifiedParticlesRenderer;
import org.pattern.image.viewer.renderer.SelectedParticlesRenderer;
import org.pattern.project.MultiImageDataObject;
import org.pattern.project.PatternProject;

/**
 * Editor to edit {@link ParticleImage} within {@link MultiImage}. Contains
 * logic for selecting and labeling particles.
 *
 * @author palas
 */
@MultiViewElement.Registration(
        displayName = "#LBL_Dummy_VISUAL",
        /*iconBase = "org/pattern/project/resu/tag31.png", */
        mimeType = "text/x-pattern",
        persistenceType = TopComponent.PERSISTENCE_NEVER,
        preferredID = "ImageVisual",
        position = 1000
)
@Messages("LBL_Dummy_VISUAL=Image")
public final class ImageEditor extends TopComponent implements
        ChangeListener, MultiViewElement, PropertyChangeListener {

    protected MultiImageDataObject obj;
    protected MultiImage image;
    protected ParticleImageView imagePanel;
    protected Classificator classificator;
    protected ClassifiedParticlesRenderer classificationRenderer;
    protected SelectedParticlesRenderer selectionRenderer;

    private Mode mode = Mode.SELECTION;
    private ImageEditorToolbar editorToolbar;
    private ImagePanelToolbar imagePanelToobar;
    private MultiViewElementCallback tcCallback;

    public ImageEditor(Lookup lkp) throws IOException {
        obj = lkp.lookup(MultiImageDataObject.class);
        assert obj != null;
        image = obj.getImage();

        // add listener to listen if classificator is assigned to the project
        obj.getOwnerProject().addChangeListener(this);
        classificator = obj.getOwnerProject().getClassificator();
        classificationRenderer = new ClassifiedParticlesRenderer(classificator);
        selectionRenderer = new SelectedParticlesRenderer();

        initComponents();

        imagePanel.setCallback(imageCallback);
        imagePanel.setParticleRenderer(selectionRenderer);

        editorToolbar.setClassificator(classificator);
        editorToolbar.setCallback(toolbarCallback);

        imagePanelToobar.setCallback(imageToolbarCallback);
        imagePanelToobar.setZoomer(imagePanel.getZoomer());
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        imagePanel = new ParticleImageView();
        imagePanel.setPopupProvider(popupProvider);

        imagePanelToobar = new ImagePanelToolbar();
        add(imagePanelToobar, BorderLayout.NORTH);
        add(imagePanel, BorderLayout.CENTER);
        editorToolbar = new ImageEditorToolbar();
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        switch (mode) {
            case LABELING:
                imagePanel.setParticleRenderer(classificationRenderer);
                break;
            case SELECTION:
                imagePanel.setParticleRenderer(selectionRenderer);
            case CREATION:
                imagePanel.setParticleRenderer(selectionRenderer);
        }
        imagePanel.refresh();
    }

    //<editor-fold defaultstate="collapsed" desc="Popup Menu">
    private final PopupProvider popupProvider = new PopupProvider() {
        
        @Override
        public JPopupMenu getMenu() {
            JPopupMenu popupMenu = new JPopupMenu();
            popupMenu.add(Actions.forID("Image", "org.pattern.editor.action.DeselectAction"));
            popupMenu.add(Actions.forID("Image", "org.pattern.editor.action.InvertSelectionAction"));
            Presenter.Popup foo = (Presenter.Popup) Actions.forID("Image", "org.pattern.editor.action.SelectParticlesWithLabelAction");
            popupMenu.add(foo.getPopupPresenter());
            popupMenu.addSeparator();
            foo = (Presenter.Popup) Actions.forID("Image", "org.pattern.editor.action.LabelSelectedParticlesAction");
            popupMenu.add(foo.getPopupPresenter());
            popupMenu.addSeparator();
            popupMenu.add(Actions.forID("Image", "org.pattern.editor.action.DeleteSelectionAction"));
            return popupMenu;
        }
    };
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ParticleView.Callback">
    private final ParticleImageView.Callback imageCallback = new ParticleImageView.Callback() {
        @Override
        public void onParticlesSelected(List<Particle> list, Point point, Point2D trans) {
            switch (mode) {
                case SELECTION:
                    if (point != null) {
                        selectSingleParticle(list);
                    } else {
                        selectParticles(list);
                    }
                    break;
                case LABELING:
                    labelParticles(list);
                    break;
                case CREATION:
                    createParticle(trans);
                    break;
            }
        }
    };
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="EditorToolBar.Callback">
    private final ImageEditorToolbar.Callback toolbarCallback = new ImageEditorToolbar.Callback() {

        @Override
        public void onSelectionModeChanged(Mode mode) {
            setMode(mode);
        }

        @Override
        public void onAddAllPressed() {
            if (classificator != null) {
                for (Particle particle : image.getSelectedImage().getLabeledParticles()) {
                    classificator.addExample(particle);
                }
            }
        }

        @Override
        public void onUpdatePressed() {
            if (classificator != null) {
                classificator.updateExamples(image
                        .getSelectedImage().getLabeledParticles());
            }
        }

        @Override
        public void onApllyPressed() {
            if (classificator != null) {
                classificator.classify(image.getSelectedImage());
                imagePanel.refresh();
            }
        }
    };
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ImagePanelToolbar.Callback">
    private final ImagePanelToolbar.Callback imageToolbarCallback = new ImagePanelToolbar.Callback() {
        
        @Override
        public void onContourDisplayChanged(boolean state) {
            classificationRenderer.setDrawContour(state);
            selectionRenderer.setDrawContour(state);
            imagePanel.refresh();
        }
        
        @Override
        public void onOriginDisplayChanged(boolean state) {
            classificationRenderer.setDrawOrigin(state);
            selectionRenderer.setDrawOrigin(state);
            imagePanel.refresh();
        }
    };
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TC Setup">
    @Override
    public void componentOpened() {
        tcCallback.updateTitle(obj.getName());
        image.getSelectedImage().registerListener(this);
        imagePanel.setImage(image.getSelectedImage());
    }

    @Override
    public void componentClosed() {
        image.getSelectedImage().unregisterListener(this);
    }

    @Override
    protected String preferredID() {
        return super.preferredID() + obj.getImage();
    }

    @Override
    public Action[] getActions() {
        List<? extends Action> actionList = Utilities.actionsForPath("Actions/Editor");
        return actionList.toArray(new Action[actionList.size()]);
    }

    @Override
    public JComponent getVisualRepresentation() {
        return this;
    }

    @Override
    public JComponent getToolbarRepresentation() {
        return editorToolbar;
    }

    @Override
    public Lookup getLookup() {
        return obj.getLookup();
    }

    @Override
    public UndoRedo getUndoRedo() {
        return UndoRedo.NONE;
    }

    @Override
    public void setMultiViewCallback(MultiViewElementCallback callback) {
        this.tcCallback = callback;
    }

    @Override
    public CloseOperationState canCloseElement() {
        return CloseOperationState.STATE_OK;
    }

    @Override
    public void componentShowing() {
    }

    @Override
    public void componentHidden() {
    }

    @Override
    public void componentActivated() {
    }

    @Override
    public void componentDeactivated() {
    }
    //</editor-fold>

    /**
     * Assignes currently selected labelHardly to particles.
     *
     * @param point
     * @return
     */
    private void labelParticles(List<Particle> list) {
        if (list != null && !list.isEmpty() && classificator != null && classificator.getSelectedLabel() != null) {
            ParticleLabel label = classificator.getSelectedLabel();
            for (Particle p : list) {
                image.getSelectedImage().labelHardly(p, label);
            }
            imagePanel.refresh();
        }
    }
    /**
     * Selects particles.
     * 
     * @param list praticles to select
     */
    private void selectParticles(List<Particle> list) {
        if (list != null && !list.isEmpty()) {
            for (Particle p : list) {
                image.getSelectedImage().markSelected(p);
            }
            imagePanel.refresh();
        }
    }

    /**
     * Picks single particles under point.
     * @param list is list because partiles can be stacked above each other
     */
    private void selectSingleParticle(List<Particle> list) {
        for (Particle particle : list) {
            image.getSelectedImage().setSelected(particle, !particle.isSelected());
        }
    }
    
    /**
     * Creates new particle at specified location in the image.
     * 
     * @param trans point in orginal image coordinates
     */
    private void createParticle(Point2D trans) {
        if (trans != null && image.getSelectedImage().contains(trans.getX(), trans.getY())) {
            Particle particle = new Particle(
                    new org.opencv.core.Point(trans.getX(), trans.getY()), null);
            image.getSelectedImage().assign(particle);
            imagePanel.refresh();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource().getClass().equals(PatternProject.class)) {
            if (evt.getPropertyName().equals(PatternProject.PROP_CLASSIFICATOR)) {
                classificator = (Classificator) evt.getNewValue();
                editorToolbar.setClassificator(classificator);
                classificationRenderer.setClassificator(classificator);
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        // listens particle image
        imagePanel.refresh();
    }

}
