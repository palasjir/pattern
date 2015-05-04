/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.image.editor.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;
import org.pattern.data.MultiImage;
import org.pattern.data.ParticleImage;

/**
 *
 * @author palas
 */
@ActionID(
        category = "Image",
        id = "org.pattern.editor.action.DeleteSelectionAction"
)
@ActionRegistration(
        iconBase = "org/pattern/image/editor/resources/ic_delete.png",
        displayName = "#CTL_DeleteSelectionAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/Image", position = 10000, separatorBefore = 9999),
})
@NbBundle.Messages("CTL_DeleteSelectionAction=Delete particles")
public class DeleteSelectionAction extends AbstractAction {
    
    private final MultiImage context;

    public DeleteSelectionAction(MultiImage context) {
        this.context = context;
    } 

    @Override
    public void actionPerformed(ActionEvent e) {
        ParticleImage image = context.getSelectedImage();
        int count = image.getSelectedParticles().size();

        if (count > 0) {
            NotifyDescriptor d = new NotifyDescriptor(
                    "Do you wish to delete " + count + " selected particles?", // message
                    "Delete particles", // title
                    NotifyDescriptor.OK_CANCEL_OPTION, // option type
                    NotifyDescriptor.WARNING_MESSAGE, // message type
                    null, // own buttons as Object[]
                    null); // initial value
            Object retval = DialogDisplayer.getDefault().notify(d);

            if (retval.equals(NotifyDescriptor.YES_OPTION)) {
                image.deleteSelected();
            }
        }

    }

}
