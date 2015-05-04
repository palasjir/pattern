/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.project;

import java.io.File;
import java.io.IOException;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.pattern.data.Classificator;

/**
 * Util class for {@link PatternProject}.
 * 
 * @author palas
 */
public final class PatternProjectSupport {
    
    /**
     * Imports image to project structure. Makes copy of image file and places
     * it withing the {@code images} folder. Then creates new file according to 
     * image name in {@code data} directory and initializes it with proper values.
     * 
     * @param project
     * @param image
     * @param newName
     * @throws IOException
     * 
     * @see PatternProject
     * @see PatternFileSupport
     */
    public static void importImage(PatternProject project, File image, String newName) throws IOException {
        if (image.isFile()) {
            FileObject imageFile = FileUtil.toFileObject(image);
            
            // if no new name use the old one from file
            if (newName == null) {
                newName = imageFile.getName();
            }
            
            // copy image to image files
            FileObject newImageFile = imageFile.copy(
                    project.getImagesFolder(), 
                    newName, 
                    imageFile.getExt()
            );
            PatternFileSupport.createPatternFile(
                    project, 
                    newName, 
                    newImageFile.getNameExt()
            );
        }
    }
    
    public static FileObject findProjectDirOfImage(FileObject image){
        return image.getParent().getParent();
    }
    
    public static Project findProjectOfImage(FileObject image){
        try {
            return ProjectManager.getDefault().findProject(findProjectDirOfImage(image));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalArgumentException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }
    
    public static Classificator findClassificatorForImage(FileObject image){
        return findProjectOfImage(image).getLookup().
                lookup(PatternProject.class).getClassificator();
    }
   
    
    // util class -> no instance
    private PatternProjectSupport(){}
    
}
