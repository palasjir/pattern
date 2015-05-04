/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.image.viewer.renderer;

import java.awt.Color;
import org.pattern.data.Classificator;
import org.pattern.data.Particle;

/**
 * Renders clasified particles.
 * 
 * @author palas
 */
public class ClassifiedParticlesRenderer extends DefaultParticleRenderer{
    
    private Classificator classificator;

    public ClassifiedParticlesRenderer(Classificator classificator) {
        this.classificator = classificator;
    }

    @Override
    public Color getColor(Particle p) {
        if(classificator != null){
            return classificator.getLabel(p.label).getColor();
        }
        return super.getColor(p);
    }
    
    public void setClassificator(Classificator classificator) {
        this.classificator = classificator;
    }
}
