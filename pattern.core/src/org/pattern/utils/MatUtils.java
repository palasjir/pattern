/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pattern.utils;

import org.opencv.core.Core;
import org.opencv.core.Mat;

/**
 *
 * @author palasjiri
 */
public class MatUtils {
    
    /**
     * Compares if two image matrices contains similar data.
     * 
     * @param mat1
     * @param mat2
     * @return 
     */
    public static boolean similar(Mat mat1, Mat mat2){
        
        if(mat1.cols() != mat2.cols() || mat1.rows() != mat2.rows()){
            return false;
        }
        
        Mat mat = new Mat();
        Core.compare(mat1, mat2, mat, Core.CMP_EQ);
        return Core.countNonZero(mat) != 0; 
    }
}
