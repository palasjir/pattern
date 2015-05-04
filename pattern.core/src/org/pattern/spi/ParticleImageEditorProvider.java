/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.spi;

import org.openide.windows.TopComponent;
import org.pattern.data.ParticleImage;

/**
 *
 * @author palas
 */
public interface ParticleImageEditorProvider {
    
    /**
     * Provides editor for single image.
     * @param image
     * @return 
     */
    public TopComponent getEditor(ParticleImage image);
    
}
