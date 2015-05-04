/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern;

import org.openide.modules.ModuleInstall;
import org.pattern.api.ClassificatorController;



public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        // Saved classifiers
        ClassificatorController.getInstance().restore();
    }

}