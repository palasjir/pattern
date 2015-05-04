/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.detection.contour;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.openide.util.lookup.ServiceProvider;
import org.pattern.data.Particle;
import org.pattern.api.DetectionAlgorithm;
import org.pattern.data.ParticleImage;

/**
 *
 * @author palas
 */
@ServiceProvider(service = DetectionAlgorithm.class)
public class ContourDetectionAlgorithm implements DetectionAlgorithm{
    
    public static final int THRESHOLD = 150;
    public static final int MAX = 255;
    public static final Point ORIGIN = new Point(0, 0); 
    
    @Override
    public List<? extends Particle> detectAndAssign(ParticleImage image) {
        
        // take the copy of image that we dont modify the original
        Mat img = new Mat();
        image.getPixels().copyTo(img);
        // blur the image to denoise
        //Imgproc.blur(imagePixels, imagePixels, new Size(3, 3));
        
        // thresholds the image
        Mat thresholded = new Mat();
//        Imgproc.threshold(imagePixels, thresholded,
//                THRESHOLD, MAX, Imgproc.THRESH_TOZERO_INV);
        
        // thresholding
        Imgproc.adaptiveThreshold(
                img, thresholded, 
                255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, 
                Imgproc.THRESH_BINARY_INV, 155, 15);
         Highgui.imwrite("1_thresholded.jpg", thresholded);
        
        Mat edges = new Mat();
        Imgproc.Canny(img, edges, 100, 200);
         Highgui.imwrite("1_canny.jpg", edges);
       
        
        
        // remove small noises
//        Mat kernel = Mat.ones(new Size(3, 3), CvType.CV_8UC1);
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_CROSS, new Size(5,5));

        Imgproc.morphologyEx(thresholded, thresholded, Imgproc.MORPH_OPEN, kernel);
        Highgui.imwrite("2_opening.jpg", thresholded);
        
//        Imgproc.erode(thresholded, thresholded, kernel, ORIGIN, 3);
//        Highgui.imwrite("3_erode.jpg", thresholded);
        
        Mat distTransform = new Mat();
        Imgproc.distanceTransform(thresholded, distTransform, Imgproc.CV_DIST_C, 5);
        distTransform.convertTo(distTransform, CvType.CV_8UC1);
        Imgproc.equalizeHist(distTransform, distTransform);
        Highgui.imwrite("4_distance_transform.jpg", distTransform);
        
        Mat markerMask = Mat.zeros(img.size(), CvType.CV_8UC1);
        double max = Core.minMaxLoc(distTransform).maxVal;
        Imgproc.threshold(distTransform, markerMask, max * 0.9, 255, Imgproc.THRESH_BINARY);
        markerMask.convertTo(markerMask, CvType.CV_8UC1);
        Highgui.imwrite("5_thresholded_distance.jpg", markerMask);
        

        
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(
                markerMask,
                contours,
                new Mat(),
                Imgproc.RETR_EXTERNAL,
                Imgproc.CHAIN_APPROX_SIMPLE,
                ORIGIN);
        
        Mat markers = Mat.zeros(img.size(), CvType.CV_32S);
        //markers.setTo(Scalar.all(0));
        Random rand = new Random();
        for (int idx = 0; idx < contours.size(); idx++) {
            Scalar color = new Scalar(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
            Imgproc.drawContours(markers, contours, idx, color, -1);
        }
        Highgui.imwrite("6_markers.jpg", markers);
        
        
        Imgproc.cvtColor(img, img, Imgproc.COLOR_GRAY2RGB);
        img.convertTo(img, CvType.CV_8UC3);
        Imgproc.watershed(img, markers);
        Highgui.imwrite("7_wattershed.jpg", markers);

        
        // detect contours
//        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(
                thresholded,
                contours,
                new Mat(),
                Imgproc.RETR_EXTERNAL,
                Imgproc.CHAIN_APPROX_SIMPLE,
                ORIGIN);
        
        // create particle from each contour
        List<Particle> particles = new ArrayList<>();
        int i = 0;
        for (MatOfPoint contour : contours) {
            Point cog = calcCog(contour);
            if(!Double.isNaN(cog.x) && !Double.isNaN(cog.y)){
                System.out.println(cog);
                Particle p = new Particle(cog, contour);
                particles.add(p); // just for reorting reasons
                image.assign(p);
            }
        }
          
        return particles;
    }
    
    /** Calculates center of particle. */
    private Point calcCog(MatOfPoint contour){
        Moments moments = Imgproc.moments(contour);
        Point cog = new Point();
        cog.x = moments.get_m10() / moments.get_m00();
        cog.y = moments.get_m01() / moments.get_m00();
        return cog;
    }

    @Override
    public String toString() {
        return "Simple particle detector";
    }
    
}
