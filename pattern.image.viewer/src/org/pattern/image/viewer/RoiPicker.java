package org.pattern.image.viewer;

import java.awt.AlphaComposite;
import static java.awt.AlphaComposite.getInstance;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import org.pattern.data.Particle;

/**
 * Region of interest selected with mouse. It's purpose is to find particles.
 *
 * @author palasjiri
 */
public class RoiPicker {

    private static final Dimension INIT_DIM = new Dimension(0, 0);
    private static final AlphaComposite ALPHA_COMPOSITE = getInstance(AlphaComposite.SRC_OVER, 0.25f);
    private static final BasicStroke STROKE = new BasicStroke(1.0f);
    private static final Color COLOR = Color.BLUE;

    /**
     * Position of start point in window. This is the point where user clicked
     * for the first time he started to pick with ROI.
     */
    private final Point start;

    /**
     * Region of ROI in window. This is the full area the roi takes.
     */
    private final Rectangle roi;

    /**
     * Current plane transformation.
     */
    private final AffineTransform at;
    private final AffineTransform atInverse;

    /**
     * Constructor of region of interest.
     *
     * @param point clicked point in window
     * @param at
     * @throws java.awt.geom.NoninvertibleTransformException
     */
    public RoiPicker(Point point, AffineTransform at) throws NoninvertibleTransformException {
        start = point;
        roi = new Rectangle(start, INIT_DIM);
        this.at = at;
        atInverse = at.createInverse();
    }

    /**
     * Updates ROI with current mose position.
     *
     * @param point current mouse position
     */
    public void update(Point point) {
        int x, y, w, h;

        if (point.x < start.x) {
            w = Math.abs(point.x - start.x);
            x = start.x - w;
        } else {
            x = start.x;
            w = Math.abs(point.x - start.x);
        }

        if (point.y < start.y) {
            h = Math.abs(start.y - point.y);
            y = start.y - h;
        } else {
            y = start.y;
            h = Math.abs(point.y - start.y);
        }

        roi.setLocation(x, y);
        roi.setSize(w, h);
    }

    /**
     * @return top left corner point
     */
    public Point getTL() {
        return roi.getLocation();
    }

    /**
     * @return bottom right corner point
     */
    public Point getBR() {
        return new Point(roi.x + roi.width, roi.y + roi.height);
    }
    
    /**
     * Checks if the given point is inside region.
     * @param p
     * @return true is inside, false otherwise
     */
    public boolean contains(Point p) {
        return roi.contains(p);
    }

    /**
     *
     * @return roi in window space as Restangle2D
     */
    public Rectangle2D getBounds2D() {
        return roi.getBounds2D();
    }

    /**
     * Determines which particles lays inside this roi. First converts ROI to
     * image space. And then determines which particles lays inside.
     *
     * @param particles particles to be determined
     * @return list of particles which lays inside this roi
     */
    public List<Particle> find(List<Particle> particles) {
        //TODO: Possible future speedup with quad tree
        List<Particle> particlesInRoi = new ArrayList<>();
        Rectangle2D roiInImageSpace = toImageSpace(atInverse);

        for (Particle object : particles) {
            org.opencv.core.Point pos = object.cog();
            if (roiInImageSpace.contains(pos.x, pos.y)) {
                particlesInRoi.add(object);
            }
        }

        return particlesInRoi;
    }

    /**
     * Transformes this ROI from window space to image space.
     *
     * @param at transformation which transfers window point to image space
     * @return transformed roi to image space
     */
    public Rectangle2D toImageSpace(AffineTransform at) {
        Point2D lt = at.transform(new Point2D.Double(getTL().x, getTL().y), new Point2D.Double());
        Point2D br = at.transform(new Point2D.Double(getBR().x, getBR().y), new Point2D.Double());

        double w = Math.abs(br.getX() - lt.getX());
        double h = Math.abs(br.getY() - lt.getY());

        return new Rectangle2D.Double(lt.getX(), lt.getY(), w, h);
    }
    
    /**
     * Draws region.
     * @param g2 
     */
    public void draw(Graphics2D g2) {
        g2.setPaint(COLOR);
        g2.draw(roi);
        g2.setComposite(ALPHA_COMPOSITE);
        g2.setColor(COLOR);
        g2.fill(roi);
        g2.setComposite(AlphaComposite.SrcOver);
    }
}
