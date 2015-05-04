/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.image.viewer.renderer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import org.opencv.core.Point;
import org.pattern.data.Particle;

/**
 * Renders particles to image as selected and deselected.
 *
 * @author palas
 */
public class SelectedParticlesRenderer extends DefaultParticleRenderer{
    
    private Color deselected = Color.RED;
    private Color selected = Color.GREEN;

    public SelectedParticlesRenderer() {}

    public SelectedParticlesRenderer(Color deselected, Color selected) {
        this.deselected = deselected;
        this.selected = selected;
    }

    @Override
    public Color getColor(Particle p) {
        if (p.isSelected()) {
            return selected;
        } else {
            return deselected;
        }
    }

}
