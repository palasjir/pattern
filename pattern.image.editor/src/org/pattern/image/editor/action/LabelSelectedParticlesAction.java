/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.image.editor.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.awt.DynamicMenuContent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.util.actions.Presenter;
import org.pattern.data.Classificator;
import org.pattern.data.Particle;
import org.pattern.data.ParticleImage;
import org.pattern.data.ParticleLabel;
import org.pattern.project.MultiImageDataObject;

@ActionID(
        category = "Image",
        id = "org.pattern.editor.action.LabelSelectedParticlesAction"
)
@ActionRegistration(
        lazy = false,
        displayName = "#CTL_LabelSelectedParticlesAction"
)
@ActionReference(path = "Menu/Image", position = 1000, separatorBefore = 999)
@Messages("CTL_LabelSelectedParticlesAction=Label selected as")
public final class LabelSelectedParticlesAction extends AbstractAction implements Presenter.Menu, Presenter.Popup {

    private ParticleImage image;

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO implement action body
    }

    @Override
    public JMenuItem getMenuPresenter() {
        return new SubMenu();
    }

    @Override
    public JMenuItem getPopupPresenter() {
        return new SubMenu();
    }

    private class SubMenu extends JMenu implements DynamicMenuContent {

        public SubMenu() {
            super(Bundle.CTL_LabelSelectedParticlesAction());
            initComponent();
        }

        private void initComponent() {
            MultiImageDataObject obj = Utilities.actionsGlobalContext().lookup(MultiImageDataObject.class);
            if (obj != null) {
                Classificator classificator = obj.getOwnerProject().getClassificator();
                if (classificator != null) {
                    image = obj.getImage().getSelectedImage();
                    for (final ParticleLabel label : classificator.getLabels()) {

                        this.add(new ParticleLabelMenuItem(image, label, new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                Set<Particle> particles = image.getSelectedParticles();
                                for (Particle particle : particles) {
                                    image.labelHardly(particle, label);
                                }
                            }
                        }));
                    }
                }
            }
        }

        @Override
        public JComponent[] getMenuPresenters() {
            return new JComponent[]{this};
        }

        @Override
        public JComponent[] synchMenuPresenters(JComponent[] items) {
            return new JComponent[]{new SubMenu()};
        }

    }

}
