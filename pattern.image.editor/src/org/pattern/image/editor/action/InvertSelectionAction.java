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
 * Inverts selection such that selected become deselected and othewise.
 * 
 * @author palas
 */
@ActionID(
        category = "Image",
        id = "org.pattern.editor.action.InvertSelectionAction"
)
@ActionRegistration(
        displayName = "#CTL_InvertSelectionAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/Image", position = 200),
})
@NbBundle.Messages("CTL_InvertSelectionAction=Invert selection")
public class InvertSelectionAction extends AbstractAction{
    
    private final MultiImage context;

    public InvertSelectionAction(MultiImage context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        context.getSelectedImage().invertSelection();
    }
    
}
