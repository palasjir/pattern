/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pattern.api;

import java.io.File;
import org.pattern.data.MultiImage;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.netbeans.api.progress.ProgressHandle;

/**
 * This interface allows retrieve data from file to patern data structure.
 * 
 * @author palasjiri
 */
public interface MultiImageImporter {
    
    /**
     * Imports data from file.
     * @param path File with data to be imported
     * @return imported PatternData stucture
     */
    public MultiImage importData(File path);
    
    /**
     * Extensions this importer can  load.
     * @return list of supported extensions
     */
    public String[] getExtensions();
    
    /**
     * Provides extension filter for this importer.
     * @return filename extension filter
     */
    public FileNameExtensionFilter getExtensionFilter();
    
    /**
     * Checks 
     * @param extension smallcase letters defining the file extension, without dot eg. txt, png, jpg
     * @return true if supports, false otherwise 
     */
    public boolean isSupporting(String extension);
    
    /**
     * Setter for ProgressHandle to document the progress.
     * @param p 
     */
    public void setProgressHandle(ProgressHandle p);
     
}
