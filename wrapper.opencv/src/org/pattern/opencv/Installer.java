/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.opencv;

import org.opencv.core.Core;
import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {
    
    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    
    @Override
    public void restored() {
        
    }

}
