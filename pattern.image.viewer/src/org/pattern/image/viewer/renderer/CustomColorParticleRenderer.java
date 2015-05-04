/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.image.viewer.renderer;

import java.awt.Color;
import java.util.Map;
import org.pattern.data.Particle;

/**
 *
 * @author palas
 */
public class CustomColorParticleRenderer extends DefaultParticleRenderer{
    
    private Map<Integer, Color> colors;

    public CustomColorParticleRenderer() {
    }

    public CustomColorParticleRenderer(Color[] colors) {
        for (int i = 0; i < colors.length; i++) {
            this.colors.put(i, colors[i]);
        }
    }

    @Override
    public Color getColor(Particle p) {
        if(p.getId() < colors.size()){
            return colors.get(p.getId());
        }
        return super.getColor(p);
    }

    public void setColors(Color[] colors) {
        for (int i = 0; i < colors.length; i++) {
            this.colors.put(i, colors[i]);
        }
    }
    
    public void setColors(Map<Integer, Color> colors){
        this.colors = colors;
    }
    
}
