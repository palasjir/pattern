/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.image.info;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.pattern.api.ColorIcon;
import org.pattern.data.Classificator;
import org.pattern.data.ParticleImage;
import org.pattern.data.ParticleLabel;
import org.pattern.project.MultiImageDataObject;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//org.pattern.info//ImageInfo//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "ImageInfoTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "inspector", openAtStartup = true)
@ActionID(category = "Window", id = "org.pattern.info.ImageInfoTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_ImageInfoAction",
        preferredID = "ImageInfoTopComponent"
)
@Messages({
    "CTL_ImageInfoAction=Image Info",
    "CTL_ImageInfoTopComponent=Image Info",
    "HINT_ImageInfoTopComponent=Shows basic informations about image."
})
public final class ImageInfoTC extends TopComponent implements LookupListener, ChangeListener {

    Classificator classificator;
    ParticleImage image;
    Lookup.Result<MultiImageDataObject> result;

    public ImageInfoTC() {
        initComponents();
        setName(Bundle.CTL_ImageInfoTopComponent());
        setToolTipText(Bundle.HINT_ImageInfoTopComponent());
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        infoContainer = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        resolutionLabel = new javax.swing.JLabel();
        particlesLabel = new javax.swing.JLabel();
        resolutionText = new javax.swing.JLabel();
        particlesText = new javax.swing.JLabel();
        nameText = new javax.swing.JLabel();
        categoriesLabel = new javax.swing.JLabel();
        categoriesContainer = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(nameLabel, org.openide.util.NbBundle.getMessage(ImageInfoTC.class, "ImageInfoTC.nameLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(resolutionLabel, org.openide.util.NbBundle.getMessage(ImageInfoTC.class, "ImageInfoTC.resolutionLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(particlesLabel, org.openide.util.NbBundle.getMessage(ImageInfoTC.class, "ImageInfoTC.particlesLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(resolutionText, org.openide.util.NbBundle.getMessage(ImageInfoTC.class, "ImageInfoTC.resolutionText.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(particlesText, org.openide.util.NbBundle.getMessage(ImageInfoTC.class, "ImageInfoTC.particlesText.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(nameText, org.openide.util.NbBundle.getMessage(ImageInfoTC.class, "ImageInfoTC.nameText.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(categoriesLabel, org.openide.util.NbBundle.getMessage(ImageInfoTC.class, "ImageInfoTC.categoriesLabel.text")); // NOI18N

        javax.swing.GroupLayout infoContainerLayout = new javax.swing.GroupLayout(infoContainer);
        infoContainer.setLayout(infoContainerLayout);
        infoContainerLayout.setHorizontalGroup(
            infoContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(infoContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(infoContainerLayout.createSequentialGroup()
                        .addGroup(infoContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(infoContainerLayout.createSequentialGroup()
                                .addComponent(resolutionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18))
                            .addGroup(infoContainerLayout.createSequentialGroup()
                                .addComponent(particlesLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(32, 32, 32))
                            .addGroup(infoContainerLayout.createSequentialGroup()
                                .addComponent(nameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(31, 31, 31)))
                        .addGroup(infoContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameText)
                            .addComponent(particlesText)
                            .addComponent(resolutionText)))
                    .addComponent(categoriesLabel))
                .addGap(31, 31, 31))
        );
        infoContainerLayout.setVerticalGroup(
            infoContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(infoContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(nameText))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(resolutionLabel)
                    .addComponent(resolutionText))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(particlesLabel)
                    .addComponent(particlesText))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(categoriesLabel)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        add(infoContainer, java.awt.BorderLayout.NORTH);

        categoriesContainer.setLayout(new javax.swing.BoxLayout(categoriesContainer, javax.swing.BoxLayout.PAGE_AXIS));
        add(categoriesContainer, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel categoriesContainer;
    private javax.swing.JLabel categoriesLabel;
    private javax.swing.JPanel infoContainer;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel nameText;
    private javax.swing.JLabel particlesLabel;
    private javax.swing.JLabel particlesText;
    private javax.swing.JLabel resolutionLabel;
    private javax.swing.JLabel resolutionText;
    // End of variables declaration//GEN-END:variables

    @Override
    public void componentOpened() {
        result = Utilities.actionsGlobalContext().lookupResult(MultiImageDataObject.class);
        result.addLookupListener(this);
    }

    @Override
    public void componentClosed() {
        result.addLookupListener(this);
    }

    void writeProperties(java.util.Properties p) {
        p.setProperty("version", "1.0");
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        Collection<? extends MultiImageDataObject> objs = result.allInstances();
        if (!objs.isEmpty()) {
            MultiImageDataObject obj = objs.iterator().next();
            if (image != null) {
                image.unregisterListener(this);
            }
            image = obj.getImage().getSelectedImage();
            image.registerListener(this);
            classificator = obj.getOwnerProject().getClassificator();
            setResolutionText();
            setParticleCountText();
            setClassesTable();
        }
    }

    private void setResolutionText() {
        String text = image.getWidth() + " x " + image.getHeight();
        resolutionText.setText(text);
    }

    private void setParticleCountText() {
        String text = String.valueOf(image.getParticles().size());
        particlesText.setText(text);
    }

    private void setClassesTable() {
        List<ParticleClassesInfo> infos = new ArrayList<>();
        if (classificator != null) {
            categoriesContainer.removeAll();
            for (ParticleLabel label : classificator.getLabels()) {
                categoriesContainer.add(
                        new CategoryCountPanel(
                                label, 
                                image.countParticlesWithLabelId(label.getId())
                        )
                );
            } 
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == image) {
            setParticleCountText();
            setClassesTable();
        }
    }
    
    private class CategoryCountPanel extends JPanel{

        public CategoryCountPanel(ParticleLabel pLabel, int count) {
            setLayout(new FlowLayout(FlowLayout.LEFT));
            setMaximumSize(new Dimension(4000, 25));
            JLabel label = new JLabel();
            label.setText(pLabel.getName());
            label.setIcon(new ColorIcon(pLabel.getColor()));
            label.setMinimumSize(new Dimension(300, 5));
            add(label);
            
            JPanel container = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JLabel countLabel = new JLabel(String.valueOf(count));
            countLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
            container.add(countLabel);
            add(container);
        }
        
        
        
    }
    

}
