/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.analysis.cluster.hierarch;

import org.patern.clustering.hierarchical.Cluster;
import org.pattern.data.Classificator;
import org.pattern.data.ParticleImage;

/**
 *
 * @author palas
 */
public class DistanceAnalysis {
    
    protected ParticleImage image;
    protected Cluster cluster;
    protected Classificator classificator;

    public DistanceAnalysis(ParticleImage image, Cluster cluster) {
        this.image = image;
        this.cluster = cluster;
    }

    public Classificator getClassificator() {
        return classificator;
    }

    public void setClassificator(Classificator classificator) {
        this.classificator = classificator;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }
    
    public Cluster getCluster(){
        return cluster;
    }
    
}
