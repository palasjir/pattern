/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.image.viewer.renderer;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.pattern.data.Particle;

/**
 * Contains 
 * @author palas
 */
public final class RenderUtil {
    
    /**
     * Creates countour path from {@link MatOfPoint}.
     * @param contour 
     * @return 
     */
    public static Path2D createContour(MatOfPoint contour) {
        List<Point> points = contour.toList();
        Path2D.Double path = new Path2D.Double();

        Iterator<Point> it = points.iterator();
        if (it.hasNext()) {
            Point point = it.next();
            path.moveTo(point.x, point.y);
        }

        while (it.hasNext()) {
            Point point = it.next();
            path.lineTo(point.x, point.y);
        }

        path.closePath();
        return path;
    }
    
    public static void drawSimpleCross(Graphics2D g, double x, double y, double l) {
        g.draw(new Line2D.Double(x, y - l, x, y + l));
        g.draw(new Line2D.Double(x - l, y, x + l, y));
    }
    

    /**
     * Draws collection of particles with specified renderer.
     *
     * @param g
     * @param particles
     */
    public static void draw(Graphics2D g, ParticleRenderer renderer, Collection<? extends Particle> particles) {
        for (Particle particle : particles) {
            renderer.draw(g, particle);
        }
    }
    
    // no instance -> util class
    private RenderUtil(){}

}
