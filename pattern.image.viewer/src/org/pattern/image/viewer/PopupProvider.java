/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.image.viewer;

import javax.swing.JPopupMenu;

/**
 * Provides popup menu.
 * 
 * @author palas
 */
public interface PopupProvider {
    
    /**
     * Provides popup menu.
     * @return 
     */
    public JPopupMenu getMenu();
}
