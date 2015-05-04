/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.image.loaders;

import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import loci.formats.FormatException;
import loci.formats.gui.BufferedImageReader;
import org.netbeans.api.progress.ProgressHandle;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;
import org.pattern.api.MultiImageImporter;
import org.pattern.data.MultiImage;
import org.pattern.data.ParticleImage;
import org.pattern.data.UniImage;

/**
 *
 * @author palasjiri
 */
@ServiceProvider(service = MultiImageImporter.class)
public class TIFFImporter implements MultiImageImporter {

    private static final String[] extensions = {"tif", "tiff"};
    private ProgressHandle p;

    @Override
    public MultiImage importData(File path) {

        //get image reader for tiff
        final BufferedImageReader reader = new BufferedImageReader();
        ParticleImage[] images = null;
        try {
            reader.setId(path.getAbsolutePath());

            int x = reader.getSizeX();
            int y = reader.getSizeY();
            int t = reader.getImageCount();
            images = new ParticleImage[t];

            if (p != null) {
                p.switchToDeterminate(t);
            }

            for (int i = 0; i < t; i++) {
                Mat mat = new Mat(y, x, CvType.CV_8UC1);
                mat.put(0, 0, reader.openBytes(i));
                images[i] = new ParticleImage(mat);
                updateProgress(i);
            }

            reader.close();
        } catch (FormatException | IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        if (images != null) {
            if (images.length == 1) {
                return new UniImage(images[0]);
            } else {
                return new MultiImage(Arrays.asList(images));
            }
        }
        return null;
    }

    /**
     * Updates progress of the task.
     */
    private void updateProgress(int i) {
        if (p != null) {
            p.progress(i);
        }
    }

    @Override
    public void setProgressHandle(ProgressHandle p) {
        this.p = p;
    }

    @Override
    public String[] getExtensions() {
        return extensions;
    }

    @Override
    public FileNameExtensionFilter getExtensionFilter() {
        return new FileNameExtensionFilter("TIFF (*.tif, *.tiff)", extensions);
    }

    @Override
    public boolean isSupporting(String extension) {
        for (String ex : extensions) {
            if (ex.equals(extension)) {
                return true;
            }
        }
        return false;
    }

}
