/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.analysis.cluster.hierarch;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.patern.clustering.hierarchical.Cluster;
import org.pattern.image.viewer.ParticleImageView;
import org.pattern.image.viewer.renderer.CustomColorParticleRenderer;

/**
 *
 * @author palas
 */
public class ParticleImageClusterTC extends TopComponent implements MultiViewElement {

    private final JToolBar toolbar = new JToolBar();
    private final DistanceAnalysis analysis;
    private final CustomColorParticleRenderer particleRenderer;
    
    private MultiViewElementCallback callback;
    private ParticleImageView panel;
    
    public ParticleImageClusterTC(Lookup lkp) {
        this.analysis = lkp.lookup(DistanceAnalysis.class);
        assert analysis != null;
        associateLookup(lkp);
        
        initComponents();
        
        particleRenderer = new CustomColorParticleRenderer();
        panel.setParticleRenderer(particleRenderer);     
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        panel = new ParticleImageView();
        add(panel, BorderLayout.CENTER);
    }

    @Override
    public JComponent getVisualRepresentation() {
        return this;
    }

    @Override
    public JComponent getToolbarRepresentation() {
        return toolbar;
    }

    @Override
    public void setMultiViewCallback(MultiViewElementCallback callback) {
        this.callback = callback;
    }

    @Override
    public CloseOperationState canCloseElement() {
        return CloseOperationState.STATE_OK;
    }

    //<editor-fold defaultstate="collapsed" desc="TC actions">
    @Override
    public void componentDeactivated() {
    }

    @Override
    public void componentActivated() {
    }

    @Override
    public void componentHidden() {
    }

    @Override
    public void componentShowing() {
        List<Cluster> leafs = new ArrayList<>();
        analysis.getCluster().getLeafs(leafs);
        Map<Integer, Color> colorTable = new HashMap<>();
        for (int i = 0; i < leafs.size(); i++) {
            Cluster leaf = leafs.get(i);
            int id = (int) leaf.obj;
            colorTable.put(id, leaf.getColor());
        }
        particleRenderer.setColors(colorTable);
        panel.refresh();
    }

    @Override
    public void componentClosed() {}

    @Override
    public void componentOpened() {
        panel.setImage(analysis.image);
    }
//</editor-fold>

}
