/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.classification.ui.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.InstanceDataObject;
import org.openide.util.Exceptions;
import org.pattern.data.Classificator;

/**
 *
 * @author palas
 */
public class ImportClassificatorAction extends AbstractAction{

    public ImportClassificatorAction() {
        super("Import classificator");
    }    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        
        int retVal = fc.showOpenDialog(null);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            Classificator newClassificator = Classificator.load(file);
            System.out.println("Obtained classificator:" + newClassificator);
            
//            try {
//                InstanceDataObject.create(
//                        DataFolder.findFolder(FileUtil.getConfigFile("Classificator")),
//                        newClassificator.getName(),
//                        newClassificator,
//                        null,
//                        true
//                );
//            } catch (IOException ex) {
//                Exceptions.printStackTrace(ex);
//            }
            
        }
    }
    
}
