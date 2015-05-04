/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.image.editor.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.pattern.api.DetectionAlgorithm;
import org.pattern.data.ParticleImage;
import org.pattern.project.MultiImageDataObject;

@ActionID(
        category = "Image",
        id = "org.pattern.editor.action.DetectAction"
)
@ActionRegistration(
        iconBase = "org/pattern/image/editor/resources/ic_detect.png",
        displayName = "Detect"
)
@ActionReferences({
    @ActionReference(path = "Menu/Image", position = 400),
    @ActionReference(path = "Toolbars/Image", position = 100)
})
@Messages("CTL_SomeAction=Detect")
public final class DetectAction implements ActionListener {

    private final MultiImageDataObject context;

    public DetectAction(MultiImageDataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final DetectionAlgorithm detector = Lookup.getDefault().lookup(DetectionAlgorithm.class);
        final ParticleImage image = context.getImage().getSelectedImage();
        
        if(image.hasParticles()){
            NotifyDescriptor.Confirmation n = new NotifyDescriptor.Confirmation(
                    "There are already some particles in "
                            + "the image. Do you realy wan't "
                            + "to delete them and replace with new detection?", 
                    "Detect particles");
            DialogDisplayer.getDefault().notify(n);
            
            if(n.getValue() != NotifyDescriptor.OK_OPTION){
                return;
            }
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                detector.detectAndAssign(image);
            }
        });
    }
}
