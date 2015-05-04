package org.pattern.utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

/**
 * Provides multiple converting operations for images.
 *
 *
 * @author palasjiri
 */
public class ImageConverter {

    /**
     * Converts image in {@link BufferedImage} format to {@link Mat} format.
     *
     * Mat format is used by OpenCV to perform detecting operations.
     *
     * @param image
     * @return grayscale image in {@link Mat}
     */
    public static Mat toMat(BufferedImage image) {
        Mat mat = new MatOfByte(toByteArray(image));
        mat.create(image.getHeight(), image.getWidth(), Imgproc.COLOR_BGR2GRAY);
        return mat;
    }

    /**
     * Converts image from {@link Mat} to {
     *
     * @BufferedImage} format.
     *
     * @param image
     * @return image in {@link BufferedImage}
     */
    public static BufferedImage toBufferedImage(Mat image) {
        MatOfByte bytemat = new MatOfByte();
        Highgui.imencode(".jpg", image, bytemat);
        byte[] bytes = bytemat.toArray();
        InputStream in = new BufferedInputStream(new ByteArrayInputStream(bytes));

        try {
            return ImageIO.read(in);
        } catch (IOException ex) {
            Logger.getLogger(ImageConverter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * Converts to gray BufferedImage to byte array.
     *
     * @param image
     * @return image in byte array
     */
    public static byte[] toByteArray(BufferedImage image) {
        return ((DataBufferByte) image.getData().getDataBuffer()).getData();
    }
}
