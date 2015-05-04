/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.api;

import java.io.File;
import javax.activation.UnsupportedDataTypeException;
import org.apache.commons.io.FilenameUtils;
import org.openide.util.Lookup;

/**
 *
 * @author palas
 */
public class MultiImageImporterFinder {
    
    /**
     * Finds importer to import data from images.
     * @param file file with images.
     * @return data importer
     * @throws javax.activation.UnsupportedDataTypeException when no importer found
     */
    public static MultiImageImporter findImporter(File file) throws UnsupportedDataTypeException{
        if(file.isDirectory())
            throw new IllegalStateException("Can't find importer for a directory");
        String extension = FilenameUtils.getExtension(file.getPath());
        
        for (MultiImageImporter importer : Lookup.getDefault().lookupAll(MultiImageImporter.class)) {
            if(importer.isSupporting(extension)){
                return importer;
            }
        }
        
        throw new UnsupportedDataTypeException("No suitable data importer found!");
    }
    
}
