/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.image.loaders;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.filechooser.FileNameExtensionFilter;
import loci.formats.FormatException;
import loci.formats.gui.BufferedImageReader;
import org.netbeans.api.progress.ProgressHandle;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.openide.util.lookup.ServiceProvider;
import org.pattern.api.MultiImageImporter;
import org.pattern.data.MultiImage;
import org.pattern.data.ParticleImage;
import org.pattern.data.UniImage;

/**
 * Concrete implementation of PDataImporter to implement loading of MRC Data
 * files.
 *
 * @author palasjiri
 */
@ServiceProvider(service = MultiImageImporter.class)
public class MRCImporter implements MultiImageImporter {

    private static final String[] extensions = {"mrc","rec"};
    private ProgressHandle p;

    @Override
    public MultiImage importData(File path) {
        return importMRC(path);
    }

    /**
     * Provides import from MRC.
     *
     * @param file file to import from
     * @return imported data
     */
    private MultiImage importMRC(File path) {
        BufferedImageReader r = new BufferedImageReader();

        try {
            r.setId(path.getAbsolutePath());            
            int imageWidth = r.getSizeX();
            int imageHeight = r.getSizeY();
            int nImages = r.getImageCount();
            
            List<ParticleImage> images = new ArrayList<>(nImages);
            
            if(p != null){
                p.switchToDeterminate(nImages);
            }
            
            for (int i = 0; i < nImages; i++) {
                Mat mat = new Mat(imageHeight, imageWidth, CvType.CV_8UC1);
                mat.put(0, 0, r.openBytes(i));
                images.add(new ParticleImage(mat));
                updateProgress(i);
            }
            
            r.close();
            
            if(images.size()==1){
                return new UniImage(images.get(0));
            }
            return new MultiImage(images);

        } catch (FormatException | IOException ex) {
//            Logger.getLogger(PDataImporterFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private void updateProgress(int i){
        if(p != null){
            p.progress(i);
        }
    }

    @Override
    public String[] getExtensions() {
       return extensions;
    }

    @Override
    public FileNameExtensionFilter getExtensionFilter() {
        return new FileNameExtensionFilter("MRC Files (*.mrc, *.rec)", extensions);
    }

    @Override
    public boolean isSupporting(String extension) {
        for (String ex : extensions) {
            if(ex.equals(extension))
                return true;
        }
        return false;
    }

    @Override
    public void setProgressHandle(ProgressHandle p) {
        this.p = p;
    }

}
