package org.pattern.data;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Objects;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

/**
 * Abstract class of detected particle. In sense of pattern project particle is that
 * black thing we want to detect in images. Particles can vary in position,
 * size, type. Particles supports user interaction and can be marked as picked
 * (selected).
 *
 * @author palasjiri
 */
public class Particle implements Comparable<Particle>{
    
    private static final double DEAFAULT_SIZE = 10; 
    
    protected int id = -1;
    protected Point cog;
    protected MatOfPoint contour;
    protected Rect boundingRect;
    
    public int label = ParticleLabel.UNKNOWN_ID;
    public boolean hardConstrained = false;
    
    protected transient boolean isSelected = false;

    public Particle(Point cog, MatOfPoint contour){
        this.contour = contour;
        this.cog = cog;
    }

    public int getId() {
        return id;
    }
    
    /**
     * Center of gravity for this particle.
     * @return position of particle in space (center of gravity)
     */
    public Point cog(){
        return cog;
    }
    
    /**
     * Defines contour around particle.
     * @return 
     */
    public MatOfPoint getContour(){
        return contour;
    }

    /**
     * Getter for picked state.
     * @return true if picked, false otherwise
     */
    public boolean isSelected(){
        return isSelected;
    }
    
    /**
     * Sets particle picked state
     * @param picked particle picked state, true == picked, false == not picked
     */
    protected void setSelected(boolean picked){
        isSelected = picked;
    }

    public boolean contains(double x, double y) {
        return getBoundingRect().contains(new Point(x, y));
    }
    
    public boolean contains(java.awt.Point point){
        return getBoundingRect().contains(new Point(point.getX(), point.getY()));
    }
    
    public boolean contains(Point2D point){
        return getBoundingRect().contains(new Point(point.getX(), point.getY()));
    }

    protected void setLabel(int label) {
        this.label = label;
    }
    
    public int getLabelId(){
        return label;
    }

    public boolean isHardConstrained() {
        return hardConstrained;
    }

    public void setHardConstrained(boolean hardConstrained) {
        this.hardConstrained = hardConstrained;
    }
    
    public double[] distanceParts(Particle other){
        double[] parts = new double[2];
        parts[0] = Imgproc.matchShapes(
                contour, other.contour, Imgproc.CV_CONTOURS_MATCH_I3, 0);
        double areaDistance = calcArea() - other.calcArea();
        parts[1] = areaDistance;
        return parts;
    }
    
    public double distanceTo(Particle other) {
        double shapesDistance = Imgproc.matchShapes(
                contour, other.contour, Imgproc.CV_CONTOURS_MATCH_I3, 0);
        double areaDistance = calcArea() - other.calcArea();
        return Math.sqrt(10*shapesDistance + areaDistance * areaDistance);
    }
    
    /**
     * Calculates contour area of the particle.
     * @return 
     */
    private double calcArea(){
        if(contour != null && !contour.empty()){
            return Imgproc.contourArea(contour);
        }
        return 0;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.cog);
        hash = 79 * hash + Objects.hashCode(this.contour);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Particle other = (Particle) obj;
        if (!Objects.equals(this.cog, other.cog)) {
            return false;
        }
        if (contour != null && other.contour != null && 
                !Arrays.deepEquals(this.contour.toArray(), other.contour.toArray())) {
            return false;
        }
        if(contour == null && other.contour != null){
            return false;
        }
        if(contour != null && other.contour == null){
            return false;
        }
        return true;
    }
    
    
    
    /**
     * Gets the particle contour bounding box. For the firts time it is called
     * its calculated.
     * @return 
     */
    public Rect getBoundingRect() {
        if(boundingRect == null){
            boundingRect = calcBoundingRect();
        }
        return boundingRect;
    }
    
    /**
     * Calculates bounding box of this particle.
     * @return 
     * @see Imgproc#boundingRect() 
     */
    protected Rect calcBoundingRect(){
        if(contour != null && !contour.empty()){
            MatOfPoint2f curve = new MatOfPoint2f(contour.toArray());
            MatOfPoint2f curveApprox = new MatOfPoint2f();
            Imgproc.approxPolyDP(curve, curveApprox, 3, true);
            return Imgproc.boundingRect(new MatOfPoint(curveApprox.toArray()));
        }
        return new Rect(
                new Point(cog.x - DEAFAULT_SIZE, cog.y - DEAFAULT_SIZE), 
                new Point(cog.x + DEAFAULT_SIZE, cog.y + DEAFAULT_SIZE));
    }
    
    /**
     * Determines if the point is inside bounding box of the contour.
     * @param point
     * @return 
     */
    public boolean contains(Point point){
        return getBoundingRect().contains(point);
    }
    
    boolean hasKnownLabel() {
        return label != ParticleLabel.UNKNOWN_ID;
    }

    @Override
    public int compareTo(Particle other) {
        if(id == other.id)
            return 0;
        else if(id < other.id)
            return -1;
        else
            return 1;
    }
    
    // private contructor used by Gson for deserilization
    private Particle(){}
    
}
