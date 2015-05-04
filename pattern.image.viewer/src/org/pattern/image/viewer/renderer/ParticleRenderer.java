/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.image.viewer.renderer;

import java.awt.Graphics2D;
import org.pattern.data.Particle;

/**
 * Renders particles to image.
 * @author palas
 */
public interface ParticleRenderer {
    
    /** 
     * Draw particle as java awt graphics.
     * @param g
     * @param p particle to be drawn
     */
    public void draw(Graphics2D g, Particle p);
}
