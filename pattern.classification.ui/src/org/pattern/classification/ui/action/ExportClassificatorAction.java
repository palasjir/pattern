/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.classification.ui.action;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import org.pattern.data.Classificator;

/**
 *
 * @author palas
 */
public class ExportClassificatorAction extends AbstractAction{
    
    private final Classificator context;
    
    public ExportClassificatorAction(Classificator classificator) {
        context = classificator;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        
        int retVal = fc.showSaveDialog(null);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            context.save(file);
        }
    }
    
}
