/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.image.editor;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.util.ImageUtilities;
import org.pattern.image.viewer.Zoomer;

/**
 *
 * @author palas
 */
public class ImagePanelToolbar extends JToolBar implements ItemListener, Zoomer.Listener, ChangeListener{
    
    @StaticResource
    private static final String ZOOM_ICON = "org/pattern/image/editor/resources/ic_zoom.png";
    
    private Callback callback;
    private Zoomer zoomer;
    private SpinnerNumberModel zoomSpinnerModel;
    
    
    /**
     * Creates new form NewJPanel
     */
    public ImagePanelToolbar() {
        setFloatable(false);
        initComponents();
        conctourCheckBox.setSelected(true);
        originCheckBox.setSelected(true);
        
        conctourCheckBox.addItemListener(this);
        originCheckBox.addItemListener(this);
        
        zoomLabel.setIcon(ImageUtilities.loadImageIcon(ZOOM_ICON, true));
        
        zoomSpinnerModel = new SpinnerNumberModel(100.0d, 0.0d, 100.0d, 1.0d);
        zoomSpinner.setModel(zoomSpinnerModel);
        zoomSpinner.setEditor(new JSpinner.NumberEditor(zoomSpinner, "#"));
        zoomSpinner.addChangeListener(this);

    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
    
    public void setZoomer(Zoomer zoomer){
        this.zoomer = zoomer;
        zoomSpinnerModel.setMaximum(zoomer.getMax() * 100);
        zoomSpinnerModel.setMinimum(zoomer.getMin() * 100);
        zoomSpinnerModel.setValue(zoomer.getValue() * 100);
        zoomer.registerListener(this);
    }
    
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        displayLabel = new javax.swing.JLabel();
        originCheckBox = new javax.swing.JCheckBox();
        conctourCheckBox = new javax.swing.JCheckBox();
        zoomContainer = new javax.swing.JPanel();
        zoomLabel = new javax.swing.JLabel();
        zoomSpinner = new javax.swing.JSpinner();

        setFloatable(false);
        setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N

        displayLabel.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        displayLabel.setText("Display:");
        add(displayLabel);

        originCheckBox.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        originCheckBox.setText("origin");
        originCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        add(originCheckBox);

        conctourCheckBox.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        conctourCheckBox.setText("contour");
        conctourCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 25));
        conctourCheckBox.setMaximumSize(new java.awt.Dimension(100, 22));
        conctourCheckBox.setMinimumSize(new java.awt.Dimension(100, 22));
        add(conctourCheckBox);

        zoomContainer.setMaximumSize(new java.awt.Dimension(20000, 25));
        zoomContainer.setMinimumSize(new java.awt.Dimension(5, 25));
        zoomContainer.setPreferredSize(new java.awt.Dimension(300, 25));
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 0);
        flowLayout1.setAlignOnBaseline(true);
        zoomContainer.setLayout(flowLayout1);

        zoomLabel.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        zoomLabel.setText("Zoom:");
        zoomLabel.setAlignmentX(0.5F);
        zoomLabel.setMaximumSize(new java.awt.Dimension(34, 25));
        zoomLabel.setMinimumSize(new java.awt.Dimension(34, 25));
        zoomLabel.setPreferredSize(new java.awt.Dimension(34, 25));
        zoomContainer.add(zoomLabel);

        zoomSpinner.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        zoomSpinner.setAlignmentY(0.0F);
        zoomSpinner.setMaximumSize(new java.awt.Dimension(75, 25));
        zoomSpinner.setMinimumSize(new java.awt.Dimension(35, 25));
        zoomSpinner.setPreferredSize(new java.awt.Dimension(75, 25));
        zoomContainer.add(zoomSpinner);

        add(zoomContainer);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox conctourCheckBox;
    private javax.swing.JLabel displayLabel;
    private javax.swing.JCheckBox originCheckBox;
    private javax.swing.JPanel zoomContainer;
    private javax.swing.JLabel zoomLabel;
    private javax.swing.JSpinner zoomSpinner;
    // End of variables declaration//GEN-END:variables


    @Override
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getSource();
        if(source.equals(originCheckBox)){
            callback.onOriginDisplayChanged(originCheckBox.isSelected());
        }else if(source.equals(conctourCheckBox)){
            callback.onContourDisplayChanged(conctourCheckBox.isSelected());
        }
    }

    @Override
    public void zoomChanged() {
        zoomSpinnerModel.setValue(zoomer.getValue() * 100);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Object source = e.getSource();
        if(source.equals(zoomSpinner)){
            zoomer.setRatio(zoomSpinnerModel.getNumber().doubleValue() / 100.0);
        }
    }

    public interface Callback {
        public void onContourDisplayChanged(boolean state);
        public void onOriginDisplayChanged(boolean state);
    }

}
