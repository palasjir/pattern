/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.image.editor;

import org.pattern.api.ColorIcon;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.japura.gui.ArrowButton;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.util.ImageUtilities;
import org.pattern.data.Classificator;
import org.pattern.data.ParticleLabel;

/**
 * Toolbar with controls for {@Link ImageEditor}. Listens to selected
 * classificator.
 *
 * @author palas
 */
public class ImageEditorToolbar extends JToolBar implements ActionListener, ChangeListener {

    private static final String NO_CLASIFICATOR = "<No classificator>";
    private static final String NO_LABELS = "<No labels>";
    
    @StaticResource
    private static final String SELECTION_ICON = "org/pattern/image/editor/resources/ic_cursor.png";
    
    @StaticResource
    private static final String LABELING_ICON = "org/pattern/image/editor/resources/ic_label.png";
    
    @StaticResource
    private static final String CREATION_ICON = "org/pattern/image/editor/resources/ic_add.png";

    private JPanel labelingControls;
    private JToggleButton labelingModeButton;
    private JToggleButton selectionModeButton;
    private JToggleButton creationModeButton;
    private ArrowButton nextLabelButton;
    private ArrowButton prevLabelButton;
    private JButton addAllButton;
    private JButton updateButton;
    private JButton applyButton;
    private JLabel selectedParticleLabelNotification;

    private Classificator classificator;
    private ParticleLabel selectedLabel;
    private Callback callback;

    public ImageEditorToolbar() {
        initComponents();
    }

    private void initComponents() {
        setFloatable(false);

        selectionModeButton = new JToggleButton("Selection");
        selectionModeButton.setIcon(ImageUtilities.loadImageIcon(SELECTION_ICON, true));
        selectionModeButton.addActionListener(this);
        selectionModeButton.setSelected(true);

        labelingModeButton = new JToggleButton("Labeling");
        labelingModeButton.setIcon(ImageUtilities.loadImageIcon(LABELING_ICON, true));
        labelingModeButton.addActionListener(this);

        creationModeButton = new JToggleButton("Creation");
        creationModeButton.setIcon(ImageUtilities.loadImageIcon(CREATION_ICON, true));
        creationModeButton.addActionListener(this);

        // Group toglle buttons
        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(selectionModeButton);
        modeGroup.add(labelingModeButton);
        modeGroup.add(creationModeButton);

        add(selectionModeButton);
        add(labelingModeButton);
        add(creationModeButton);

        prevLabelButton = new ArrowButton(ArrowButton.LEFT, 14);
        prevLabelButton.addActionListener(this);

        selectedParticleLabelNotification = new JLabel();

        nextLabelButton = new ArrowButton(ArrowButton.RIGHT, 14);
        nextLabelButton.addActionListener(this);

        addAllButton = new JButton("Add all");
        addAllButton.addActionListener(this);

        updateButton = new JButton("Update");
        updateButton.addActionListener(this);

        applyButton = new JButton("Apply");
        applyButton.addActionListener(this);

        labelingControls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        labelingControls.setAlignmentY(Component.CENTER_ALIGNMENT);
        labelingControls.add(prevLabelButton);
        labelingControls.add(selectedParticleLabelNotification);
        labelingControls.add(nextLabelButton);
        labelingControls.add(addAllButton);
        labelingControls.add(updateButton);
        labelingControls.add(applyButton);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setClassificator(Classificator classificator) {
        if (classificator != null) {
            if(this.classificator != null)
                this.classificator.removeChangeListener(this);
            this.classificator = classificator;
            this.classificator.addChangeListener(this);
            updateLabelIndicator();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(labelingModeButton)) {
            setEditorMode(Mode.LABELING);
        } else if (e.getSource().equals(selectionModeButton)) {
            setEditorMode(Mode.SELECTION);
        } else if (e.getSource().equals(creationModeButton)) {
            setEditorMode(Mode.CREATION);
        } else if (e.getSource().equals(prevLabelButton)) {
            if (classificator != null) {
                classificator.moveToPrevLabel();
            }
        } else if (e.getSource().equals(nextLabelButton)) {
            if (classificator != null) {
                classificator.moveToNextLabel();
            }
        } else if (e.getSource().equals(addAllButton)) {
            callback.onAddAllPressed();
        } else if (e.getSource().equals(updateButton)) {
            callback.onUpdatePressed();
        } else if (e.getSource().equals(applyButton)) {
            callback.onApllyPressed();
        }
    }

    public void setEditorMode(Mode mode) {
        switch (mode) {
            case SELECTION:
                remove(labelingControls);
                break;
            case LABELING:
                remove(labelingControls);
                add(labelingControls);
                break;
            case CREATION:
                remove(labelingControls);
                break;

        }
        if (callback != null) {
            callback.onSelectionModeChanged(mode);
        }
        revalidate();
        repaint();
    }

    public void updateLabelIndicator() {
        if (classificator != null) {
            if (classificator.hasLabels()) {
                changeLabel();
            } else {
                selectedParticleLabelNotification.setText(NO_LABELS);
            }
        } else {
            selectedParticleLabelNotification.setText(NO_CLASIFICATOR);
        }
    }

    private void changeLabel() {
        if (selectedLabel != null) {
            selectedLabel.removePropertyChangeListener(labelChangeListener);
        }
        selectedLabel = classificator.getSelectedLabel();
        selectedLabel.addPropertyChangeListener(labelChangeListener);
        selectedParticleLabelNotification.setIcon(new ColorIcon(selectedLabel.getColor()));
        selectedParticleLabelNotification.revalidate();
        selectedParticleLabelNotification.repaint();
    }

    private final PropertyChangeListener labelChangeListener = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            switch (evt.getPropertyName()) {
                case "color":
                    selectedParticleLabelNotification.setIcon(new ColorIcon(selectedLabel.getColor()));
                    selectedParticleLabelNotification.revalidate();
                    selectedParticleLabelNotification.repaint();
                    break;
            }
        }
    };

    @Override
    public void stateChanged(ChangeEvent e) {
        updateLabelIndicator();
    }

    public interface Callback {

        public void onSelectionModeChanged(Mode mode);

        public void onAddAllPressed();

        public void onUpdatePressed();

        public void onApllyPressed();
    }

}
