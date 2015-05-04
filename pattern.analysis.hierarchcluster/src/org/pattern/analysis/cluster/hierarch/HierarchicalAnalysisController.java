/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.analysis.cluster.hierarch;

import java.util.List;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.netbeans.core.spi.multiview.MultiViewFactory;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.TopComponent;
import org.patern.clustering.hierarchical.Cluster;
import org.patern.clustering.hierarchical.algorithm.ClusteringAlgorithm;
import org.patern.clustering.hierarchical.algorithm.DefaultClusteringAlgorithm;
import org.patern.clustering.hierarchical.strategy.AverageLinkageStrategy;
import org.pattern.api.analysis.AnalysisProvider;
import org.pattern.api.analysis.DistanceUtil;
import org.pattern.data.Particle;
import org.pattern.data.ParticleImage;
import org.pattern.project.MultiImageDataObject;

/**
 *
 * @author palas
 */
@ServiceProvider(service = AnalysisProvider.class)
public class HierarchicalAnalysisController implements AnalysisProvider {

    @Override
    public void openAnalysis(MultiImageDataObject obj) {
        ParticleImage image = obj.getImage().getSelectedImage();
        List<Particle> particles = image.getParticles();

        if (!particles.isEmpty()) {
            Particle[] arr = particles.toArray(new Particle[particles.size()]);

            Object[] ids = new Object[arr.length];
            for (int i = 0; i < arr.length; i++) {
                ids[i] = arr[i].getId();
            }
            
            double[][] distances = DistanceUtil.allDistances(arr);

            ClusteringAlgorithm alg = new DefaultClusteringAlgorithm();
            Cluster cluster = alg.performClustering(distances, ids, new AverageLinkageStrategy());

            DistanceAnalysis analysis = new DistanceAnalysis(image, cluster);
            analysis.setClassificator(obj.getOwnerProject().getClassificator());

            MultiViewDescription[] descriptions = new MultiViewDescription[]{
                new DendrogramDescription(analysis),
                new ParticleImageClusterViewDescription(analysis)
            };
            TopComponent tc = MultiViewFactory.createMultiView(
                    descriptions,
                    descriptions[0]
            );
            tc.open();
            tc.requestActive();
        }
    }

    @Override
    public String getDisplayName() {
        return "Hierarchical clustering";
    }

}
