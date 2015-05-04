/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.project;


import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

/**
 * Creates visualization of pattern project structure. All files are supposed
 * to be hidden creates only nodes for images.
 * 
 * @author palas
 */
public class PatternProjectLogicalView implements LogicalViewProvider {
    
    private static final String TAG = PatternProjectLogicalView.class.getSimpleName();
    private final PatternProject project;

    public PatternProjectLogicalView(PatternProject project) {
        this.project = project;
    }

    @Override
    public Node createLogicalView() {
        try {
            //Obtain the project directory's node
            FileObject projectDirectory = project.getProjectDirectory();
            DataFolder projectFolder = DataFolder.findFolder(projectDirectory);
            Node nodeOfProjectFolder = projectFolder.getNodeDelegate();
            CommonProjectActions.newFileAction();
            //Decorate the project directory's node
            return new PatternProjectNode(nodeOfProjectFolder, project);
        } catch (DataObjectNotFoundException donfe) {
            Exceptions.printStackTrace(donfe);
            Logger.getLogger(TAG).log(Level.WARNING, "Project data object not found");
            return new AbstractNode(Children.LEAF);
        }
    }

    @Override
    public Node findPath(Node arg0, Object arg1) {
        return null; // currently dont implemented - minds ?
    }
}
