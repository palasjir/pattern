/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.image.editor.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;
import org.pattern.data.MultiImage;

/**
 * Deselects all selected particles in current image editor.
 * 
 * @author palas
 */
@ActionID(
        category = "Image",
        id = "org.pattern.editor.action.DeselectAction"
)
@ActionRegistration(
        displayName = "#CTL_DeselectAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/Image", position = 100),
})
@NbBundle.Messages("CTL_DeselectAction=Deselect particles")
public class DeselectAction extends AbstractAction{
    
    private final MultiImage context;

    public DeselectAction(MultiImage context) {
        this.context = context;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        context.getSelectedImage().deselectAll();
    }
    
}
