/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.project.action;

import java.awt.BorderLayout;
import org.pattern.api.MultiImageImporter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.Collection;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.*;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.pattern.project.PatternProject;
import org.pattern.project.PatternProjectSupport;

@ActionID(
        category = "Images",
        id = "org.pattern.project.action.ImportAction"
)
@ActionRegistration(
        iconBase = "org/pattern/resources/ic_import.png",
        displayName = "#CTL_ImportAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1450),
    @ActionReference(path = "Toolbars/File", position = 300),
    @ActionReference(path = "Shortcuts", name = "D-I")
})
@Messages("CTL_ImportAction=Import image ...")
/**
 * Imports image file to project. Import finds the propper data loader and also
 * loads reference to data. Data aren't automatically opened.
 */
public final class ImportAction implements ActionListener {

    private ProjectSelectorPanel panel;
    private DialogDescriptor d;

    @Override
    public void actionPerformed(ActionEvent e) {
        PatternProject project = Utilities.actionsGlobalContext().lookup(PatternProject.class);
        if (project != null) {
            createAndShowDialog(project);
            return;
        }
        Project[] projects = OpenProjects.getDefault().getOpenProjects();
        if (projects != null && projects.length != 0) {
            panel = new ProjectSelectorPanel(projects);
            d = new DialogDescriptor(panel, "Import into", true, null);
            DialogDisplayer.getDefault().notify(d);
            Object value = d.getValue();
            if (value == NotifyDescriptor.OK_OPTION) {
                project = panel.getSelectedProject().getLookup().lookup(PatternProject.class);
                createAndShowDialog(project);
            }
        }else{
            NotifyDescriptor d = new NotifyDescriptor.Message("No project where to import the image. First create new one.");
            DialogDisplayer.getDefault().notify(d);
        }

    }

    /**
     * Creates and shows import dialog. Also handles user interaction with the
     * dialog.
     */
    public void createAndShowDialog(PatternProject project) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Import image");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setAcceptAllFileFilterUsed(true);

        Collection<? extends MultiImageImporter> importers = Lookup.getDefault().lookupAll(MultiImageImporter.class);

        // find all importers and set the filters for open dialog
        for (MultiImageImporter importer : importers) {
            chooser.addChoosableFileFilter(importer.getExtensionFilter());
        }

        // open and wait for result
        int returnVal = chooser.showOpenDialog(null);

        // process the result from open dialog
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            final File file = chooser.getSelectedFile();

            if (file.exists()) {
                executeImport(project, file);
            }
        }
    }

    /**
     * Process the import with selected importer and with selected file. Moves
     * file to project folder and creates .particles file for it.
     *
     * @param importer
     * @param file
     */
    private void executeImport(PatternProject project, final File file) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FileObject obj = FileUtil.toFileObject(file);
                    String name = FileUtil.findFreeFileName(
                            project.getImagesFolder(),
                            obj.getName(),
                            obj.getExt()
                    );
                    PatternProjectSupport.importImage(project, file, name);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }

            }
        }).start();
    }

    private class ProjectSelectorPanel extends JPanel implements ItemListener {

        Project[] projects;
        JComboBox<String> projectCB;
        int selected;

        public ProjectSelectorPanel(Project[] projects) {
            this.projects = projects;
            setLayout(new BorderLayout());

            String[] names = new String[projects.length];
            for (int i = 0; i < projects.length; i++) {
                names[i] = projects[i].getProjectDirectory().getName();
            }

            projectCB = new JComboBox<>(names);
            projectCB.addItemListener(this);
            add(projectCB, BorderLayout.CENTER);
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            selected = projectCB.getSelectedIndex();
        }

        public Project getSelectedProject() {
            return projects[selected];
        }

    }

}
