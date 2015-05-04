/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.classification.ui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.pattern.data.Classificator;
import org.pattern.project.PatternProject;

/**
 *
 * @author palas
 */
public class CreateClassificatorAction extends AbstractAction {
    
    private final DialogDescriptor d;
    private final NewClassificatorPanel panel = new NewClassificatorPanel();
    
    public CreateClassificatorAction() {
        super("New classsificator ...");
        d = new DialogDescriptor(panel, "New classificator", true, null);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        
//        NotifyDescriptor.InputLine n = 
//                new NotifyDescriptor.InputLine("Name", "Classificator");
        DialogDisplayer.getDefault().notify(d);
        Object value = d.getValue();
        if(value == NotifyDescriptor.OK_OPTION){
            PatternProject project = panel.getSelectedProject().getLookup().lookup(PatternProject.class);
            project.setClassificator(new Classificator(panel.getName()));
        }
        
    }

}
