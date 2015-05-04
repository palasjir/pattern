/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.image.viewer.renderer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import org.pattern.data.Particle;

/**
 *
 * @author palas
 */
public class DefaultParticleRenderer implements ParticleRenderer{
    
    private static final double crossSize = 2.5;
    protected boolean drawOrigin = true;
    protected boolean drawContour = true;
    
    /**
     * Draws single particle.
     * @param g awt grahics
     * @param p particle
     */
    @Override
    public void draw(Graphics2D g, Particle p) {
        Color c = g.getColor();
        g.setColor(getColor(p));
        drawContour(g, p);
        drawOrigin(g, p);
        g.setColor(c); // restore color to original after draw     
    }
    
    private void drawContour(Graphics2D g, Particle p){
        if(p.getContour() != null && drawContour){
           Path2D path = RenderUtil.createContour(p.getContour());
           g.draw(path); 
        }
    }
    
    private void drawOrigin(Graphics2D g, Particle p) {
        if (drawOrigin) {
            RenderUtil.drawSimpleCross(g, p.cog().x, p.cog().y, crossSize);
        }
    }
    
    public Color getColor(Particle p){
        return Color.BLACK;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public boolean isDrawingOrigin() {
        return drawOrigin;
    }

    public void setDrawOrigin(boolean state) {
        this.drawOrigin = state;
    }
    
    public boolean isDrawingContour(){
        return drawContour;
    }
    
    public void setDrawContour(boolean state) {
        drawContour = state;
    }
    //</editor-fold>
    
}
