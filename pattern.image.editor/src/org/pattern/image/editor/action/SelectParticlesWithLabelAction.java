/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.image.editor.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
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
        id = "org.pattern.editor.action.SelectParticlesWithLabelAction"
)
@ActionRegistration(
        lazy = false,
        displayName = "#CTL_SelectParticlesWithLabelAction"
)
@ActionReference(path = "Menu/Image", position = 300, separatorAfter = 301)
@Messages("CTL_SelectParticlesWithLabelAction=Select label")
public final class SelectParticlesWithLabelAction extends AbstractAction implements Presenter.Menu, Presenter.Popup {

    private ParticleImage image;

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public JMenuItem getMenuPresenter() {
        return new MySubMenu();
    }

    @Override
    public JMenuItem getPopupPresenter() {
        return new MySubMenu();
    }

    private class MySubMenu extends JMenu implements DynamicMenuContent {

        public MySubMenu() {
            super(Bundle.CTL_SelectParticlesWithLabelAction());
            initComponent();
        }

        @Override
        public JComponent[] getMenuPresenters() {
            return new JComponent[]{this};
        }

        @Override
        public JComponent[] synchMenuPresenters(JComponent[] items) {
            return new JComponent[]{new MySubMenu()};
        }

        private void initComponent() {
            MultiImageDataObject obj = Utilities.actionsGlobalContext().lookup(MultiImageDataObject.class);

            if (obj != null) {
                Classificator classificator = obj.getOwnerProject().getClassificator();
                if (classificator != null) {
                    image = obj.getImage().getSelectedImage();
                    List<ParticleLabel> labels = classificator.getLabels();
                    for (int i = 0; i < labels.size(); i++) {
                        final ParticleLabel label = labels.get(i);
                        this.add(new ParticleLabelMenuItem(image, label, new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                List<Particle> particles = image.findParticlesWithLabel(label);
                                image.setSelected(particles, true);
                            }
                        }));

                    }
                }
            }
        }

    }

}
