/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.project.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.spi.project.ProjectState;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Project",
        id = "org.pattern.project.action.SaveClassificatorAction"
)
@ActionRegistration(
        displayName = "#CTL_SaveClassificatorAction"
)
@Messages("CTL_SaveClassificatorAction=Save classificator")
public final class SaveClassificatorAction implements ActionListener {

    private final Project context;

    public SaveClassificatorAction(Project context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        try {
            context.getLookup().lookup(ProjectState.class).markModified();
            ProjectManager.getDefault().saveProject(context);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
