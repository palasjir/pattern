/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.data;

import java.util.Collections;

/**
 * Represents group of images that contains nly one group.
 * @author palas
 */
public class UniImage extends MultiImage{

    public UniImage(ParticleImage image) {
        super(Collections.singletonList(image));
    }

    @Override
    public ParticleImage getSelectedImage() {
        return group.get(0);
    }

    @Override
    public void setSelectedImageIndex(int index) {
        // do nothing
    }  
}
