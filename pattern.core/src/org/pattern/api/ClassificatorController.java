/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.InstanceDataObject;
import org.openide.util.Exceptions;
import org.openide.util.lookup.Lookups;
import org.pattern.data.Classificator;
import org.pattern.data.CentralLookup;
import org.pattern.data.ClassificatorInfo;

/**
 * Project controller, manage projects and workspaces states.
 *
 * @author palasjiri
 */
public class ClassificatorController {

    private static final String CLASSIFICATORS_FOLDER = "Classificators";

    private static ClassificatorController pattern = null;

    private Classificator selected;

    private transient final List<Listener> listeners = new ArrayList<>();

    public static ClassificatorController getInstance() {
        if (pattern == null) {
            pattern = new ClassificatorController();
        }
        return pattern;
    }

    public void restore() {

        // create repository for classificators if not exist
        if (FileUtil.getConfigFile(CLASSIFICATORS_FOLDER) == null) {
            try {
                FileUtil.getConfigRoot().createFolder(CLASSIFICATORS_FOLDER);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        CentralLookup lookup = CentralLookup.getDefault();
        // loads all classificators and registers them in the central lookup
        Collection<? extends ClassificatorInfo> infos
                = Lookups.forPath(CLASSIFICATORS_FOLDER).lookupAll(ClassificatorInfo.class);

        for (ClassificatorInfo info : infos) {
            if (info.getPath() != null) {
                File file = new File(info.getPath());
                if (file.exists() && file.isFile()) {
                    lookup.add(Classificator.load(file));
                }
            } else {
                InstanceDataObject.remove(
                        DataFolder.findFolder(FileUtil.getConfigFile(CLASSIFICATORS_FOLDER)),
                        info.getId(), ClassificatorInfo.class
                );
            }
        }
    }

    public void createClassificator(String name) {
        Classificator classificator = new Classificator(name);
        CentralLookup.getDefault().add(classificator);

        try {
            // register it
            InstanceDataObject.create(
                    DataFolder.findFolder(FileUtil.getConfigFile(CLASSIFICATORS_FOLDER)),
                    classificator.getInfo().getId(),
                    classificator.getInfo(),
                    null,
                    true
            );

            selected = classificator;
            for (Listener listener : listeners) {
                listener.onClassificatorCreated(selected);
            }

        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private Collection<? extends Classificator> getClassificators() {
        return CentralLookup.getDefault().lookupAll(Classificator.class);
    }

    public Classificator getMainClassificator() {
        return selected;
    }

    public void setAsMainClassificator(Classificator selected) {
        if (getClassificators().contains(selected)) {
            Classificator old = this.selected;
            this.selected = selected;
            for (Listener listener : listeners) {
                listener.onMainClassificatorChanged(old, selected);
            }
        }
    }

    public interface Listener {

        public void onClassificatorCreated(Classificator classificator);

        public void onMainClassificatorChanged(Classificator oldMain, Classificator newMain);
    }

    public void addListener(Listener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    public void removeListener(Listener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    private ClassificatorController() {
    }
}
