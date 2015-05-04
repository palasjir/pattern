/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern;

import java.io.IOException;
import org.netbeans.api.project.ProjectManager;
import org.openide.LifecycleManager;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author palas
 */
@ServiceProvider(service=LifecycleManager.class, position=1)
public class PatternLifecycleManager extends LifecycleManager {

    @Override
    public void saveAll() {
        for (LifecycleManager manager
                : Lookup.getDefault().lookupAll(LifecycleManager.class)) {
            if (manager != this) {
                /* skip our own instance */
                manager.saveAll();
            }
        }
    }

    @Override
    public void exit() {
// do application specific shutdown tasks
        for (LifecycleManager manager: Lookup.getDefault().lookupAll(LifecycleManager.class)) {
            if (manager != this) { 
                try {
                    ProjectManager.getDefault().saveAllProjects();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
                manager.exit();
            }
        }
    }

}
