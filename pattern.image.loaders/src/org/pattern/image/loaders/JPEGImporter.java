/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.image.loaders;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.netbeans.api.progress.ProgressHandle;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
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
public class JPEGImporter implements MultiImageImporter {

    private static final String[] extensions = {"jpg", "jpeg"};

    @Override
    public MultiImage importData(File path) {
        return new UniImage(importJPEG(path));
    }

    private static ParticleImage importJPEG(File file) {

        BufferedImage tempImage;
        WritableRaster tempRaster;
        byte[] values;

        try {
            tempImage = ImageIO.read(file);
            tempRaster = tempImage.getRaster();
            values = new byte[tempImage.getWidth() * tempImage.getHeight()];
            int c = 0;
            for (int y = 0; y < tempImage.getHeight(); y++) {
                for (int x = 0; x < tempImage.getWidth(); x++) {
                    values[c++] = (byte) tempRaster.getSample(x, y, 0);
                }
            }

            Mat mat = new Mat(tempImage.getHeight(), tempImage.getWidth(), CvType.CV_8UC1);
            mat.put(0, 0, values);

            return new ParticleImage(mat);

        } catch (IOException ex) {
            Logger.getLogger("Loading").log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public String[] getExtensions() {
        return extensions;
    }

    @Override
    public FileNameExtensionFilter getExtensionFilter() {
        return new FileNameExtensionFilter("JPEG", extensions);
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

    @Override
    public void setProgressHandle(ProgressHandle p) {
    }

}
