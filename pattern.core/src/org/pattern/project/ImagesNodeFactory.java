/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.project;

import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.support.NodeFactory;
import org.netbeans.spi.project.ui.support.NodeList;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.ChangeSupport;
import org.openide.util.Exceptions;

/**
 * Node of images folder.
 *
 * @author palas
 */
@NodeFactory.Registration(projectType = "org-pattern-project", position = 10)
public class ImagesNodeFactory implements NodeFactory {

    @Override
    public NodeList<?> createNodes(Project project) {
        PatternProject p = project.getLookup().lookup(PatternProject.class);
        return new ImagesNodeList(p);
    }
    
    /**
     * List with nodes displayed. 
     */
    private class ImagesNodeList implements NodeList<Node>, FileChangeListener {

        private final PatternProject project;
        private final ChangeSupport changeSupport = new ChangeSupport(this);

        public ImagesNodeList(PatternProject project) {
            this.project = project;
        }

        @Override
        public List<Node> keys() {
            FileObject dataFolder = project.getDataFolder();
            List<Node> result = new ArrayList<>();
            if (dataFolder != null) {
                for (FileObject imagesFolderFile : dataFolder.getChildren()) {
                    try {
                        result.add(DataObject.find(imagesFolderFile).getNodeDelegate());
                    } catch (DataObjectNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
            return result;
        }

        @Override
        public void addChangeListener(ChangeListener l) {
            changeSupport.addChangeListener(l);
        }

        @Override
        public void removeChangeListener(ChangeListener l) {
            changeSupport.removeChangeListener(l);
        }

        @Override
        public Node node(Node key) {
            return new FilterNode(key);
        }

        @Override
        public void addNotify() {
            project.getDataFolder().addFileChangeListener(this);
        }

        @Override
        public void removeNotify() {
            project.getDataFolder().removeFileChangeListener(this);
        }
        
        /**
         * Simplification for run {@link ChangeSupport#fireChange()}.
         */
        private void fireChange() {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    changeSupport.fireChange();
                }
            });
        }

        @Override
        public void fileFolderCreated(FileEvent fe) {
            fireChange();
        }

        @Override
        public void fileDataCreated(FileEvent fe) {
            fireChange();
        }

        @Override
        public void fileChanged(FileEvent fe) {
            fireChange();
        }

        @Override
        public void fileDeleted(FileEvent fe) {
            fireChange();
        }

        @Override
        public void fileRenamed(FileRenameEvent fe) {
            fireChange();
        }

        @Override
        public void fileAttributeChanged(FileAttributeEvent fe) {
            fireChange();
        }

    }

}
