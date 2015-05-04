/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.project;

import java.awt.Image;
import java.util.List;
import javax.swing.Action;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.FilterNode;
import org.openide.util.ImageUtilities;
import org.openide.util.Utilities;

/**
 * Node for images folder. Displays nodes for all data objects within "data" folder.
 * 
 * @author palas
 * @see PatternProject
 */
public class ImagesNode extends FilterNode{
    
    private final FileObject dir;
    
    @StaticResource
    private static final String IMAGES_FOLDER_ICON = "org/pattern/resources/ic_image_collection.png";

    public ImagesNode(Project project) throws DataObjectNotFoundException {
        this(project.getProjectDirectory().getFileObject("data"));
    }

    public ImagesNode(FileObject dir) throws DataObjectNotFoundException {
        super(DataObject.find(dir).getNodeDelegate());
        this.dir = dir;
        setDisplayName("Images");
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage(IMAGES_FOLDER_ICON);
    }

    @Override
    public Image getOpenedIcon(int type) {
        return ImageUtilities.loadImage(IMAGES_FOLDER_ICON);
    }

    @Override
    public Action[] getActions(boolean context) {
        List<? extends Action> actions = Utilities.actionsForPath("Actions/Images");
        return actions.toArray(new Action[actions.size()]);
    }
    
    public FileObject getFileObject(){
        return dir;
    }
    


}
