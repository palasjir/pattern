/**
 * *****************************************************************************
 * Copyright 2013 Lars Behnke
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *****************************************************************************
 */
package org.patern.clustering.hierarchical.algorithm;

import org.patern.clustering.hierarchical.strategy.LinkageStrategy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.patern.clustering.hierarchical.Cluster;
import org.patern.clustering.hierarchical.ClusterPair;
import org.patern.clustering.hierarchical.Distance;
import org.patern.clustering.hierarchical.DistanceMap;
import org.patern.clustering.hierarchical.HierarchyBuilder;

public class DefaultClusteringAlgorithm implements ClusteringAlgorithm {

    @Override
    public Cluster performClustering(double[][] distances,
            Object[] obj, LinkageStrategy linkageStrategy) {

        checkArguments(distances, obj, linkageStrategy);
        /* Setup model */
        List<Cluster> clusters = createClusters(obj);
        DistanceMap linkages = createLinkages(distances, clusters);

        /* Process */
        HierarchyBuilder builder = new HierarchyBuilder(clusters, linkages);
        while (!builder.isTreeComplete()) {
            builder.agglomerate(linkageStrategy);
        }

        return builder.getRootCluster();
    }

    private void checkArguments(double[][] distances, Object[] objs,
            LinkageStrategy linkageStrategy) {
        if (distances == null || distances.length == 0
                || distances[0].length != distances.length) {
            throw new IllegalArgumentException("Invalid distance matrix");
        }
        if (distances.length != objs.length) {
            throw new IllegalArgumentException("Invalid cluster name array");
        }
        if (linkageStrategy == null) {
            throw new IllegalArgumentException("Undefined linkage strategy");
        }
        int uniqueCount = new HashSet<>(Arrays.asList(objs)).size();
        if (uniqueCount != objs.length) {
            throw new IllegalArgumentException("Duplicate names");
        }
    }

    @Override
    public Cluster performWeightedClustering(double[][] distances, Object[] objs,
            double[] weights, LinkageStrategy linkageStrategy) {

        checkArguments(distances, objs, linkageStrategy);

        if (weights.length != objs.length) {
            throw new IllegalArgumentException("Invalid weights array");
        }

        /* Setup model */
        List<Cluster> clusters = createClusters(objs, weights);
        DistanceMap linkages = createLinkages(distances, clusters);

        /* Process */
        HierarchyBuilder builder = new HierarchyBuilder(clusters, linkages);
        while (!builder.isTreeComplete()) {
            builder.agglomerate(linkageStrategy);
        }

        return builder.getRootCluster();
    }

    private DistanceMap createLinkages(double[][] distances,
            List<Cluster> clusters) {
        DistanceMap linkages = new DistanceMap();
        for (int col = 0; col < clusters.size(); col++) {
            for (int row = col + 1; row < clusters.size(); row++) {
                ClusterPair link = new ClusterPair();
                Cluster lCluster = clusters.get(col);
                Cluster rCluster = clusters.get(row);
                link.setLinkageDistance(distances[col][row]);
                link.setlCluster(lCluster);
                link.setrCluster(rCluster);
                linkages.add(link);
            }
        }
        return linkages;
    }
    
    
    /**
     * Changed to provide clusters to carry objects.
     * @param objs
     * @return
     * 
     * @author palas 
     */
    private List<Cluster> createClusters(Object[] objs) {
        List<Cluster> clusters = new ArrayList<>();
        for (int i = 0; i < objs.length; i++) {
            Cluster cluster = new Cluster(objs[i]);
            clusters.add(cluster);
        }
        return clusters;
    }

    private List<Cluster> createClusters(Object[] objs, double[] weights) {
        List<Cluster> clusters = new ArrayList<>();
        for (int i = 0; i < weights.length; i++) {
            Cluster cluster = new Cluster(objs[i]);
            cluster.setDistance(new Distance(0.0, weights[i]));
            clusters.add(cluster);
        }
        return clusters;
    }

}
