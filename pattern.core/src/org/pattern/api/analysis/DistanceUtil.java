/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.api.analysis;

import java.util.List;
import org.pattern.data.Particle;

/**
 * Utilitty class for cumputing distance between particles.
 * 
 * @author palas
 */
public class DistanceUtil {
    
    /**
     * Computes distance betwen particle centers. Also preserves the dx and dy
     * values.
     * 
     * @param a
     * @param b
     * @return array where 0->euclidean distance, 1->dx, 2->dy
     */
    public static double[] euclidean3(Particle a, Particle b){
        double[] d = new double[3];
        d[1] = Math.abs(a.cog().x - b.cog().x);
        d[2] = Math.abs(a.cog().y - b.cog().y);
        d[0] = Math.sqrt( d[1] * d[1] + d[2] * d[2] );
        return d;
    }
    
    public static double euclidean(Particle a, Particle b){
        double[] d = new double[3];
        double dx = a.cog().x - b.cog().x;
        double dy  = a.cog().y - b.cog().y;
        return Math.sqrt( dx * dx + dy * dy );
    }
    
    /**
     * Computes distances between particle all centers.
     * @param arr array of particles
     * @return distance matrix
     */
    public static double[][] allDistances(Particle[] arr){
        double[][] matrix = new double[arr.length][arr.length];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                if(i==j){
                    matrix[i][j] = 0;
                }else{
                    double dist = euclidean(arr[i], arr[j]);
                    matrix[i][j] = dist;
                    matrix[j][i] = dist;
                }
            }
        }
        return matrix;
    }
    
    /**
     * Computes distances between particle all centers.
     * @param list list of particles
     * @return distance matrix
     */
    public static double[][] allDistances(List<Particle> list){
        double[][] matrix = new double[list.size()][list.size()];
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if(i==j){
                    matrix[i][j] = 0;
                }else{
                    double[] dist = euclidean3(list.get(i), list.get(j));
                    matrix[i][j] = dist[0];
                    matrix[j][i] = dist[0];
                }
            }
        }
        return matrix;
    }
    
}
