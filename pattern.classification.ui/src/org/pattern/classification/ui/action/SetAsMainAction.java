/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.classification.ui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.pattern.api.ClassificatorController;
import org.pattern.data.Classificator;

/**
 *
 * @author palas
 */
public class SetAsMainAction extends AbstractAction{
    
    Classificator context;

    public SetAsMainAction(Classificator context) {
        this.context = context;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        ClassificatorController.getInstance().setAsMainClassificator(context);
    }
    
}
