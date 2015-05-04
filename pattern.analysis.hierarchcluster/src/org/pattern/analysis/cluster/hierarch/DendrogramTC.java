/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.analysis.cluster.hierarch;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import org.japura.gui.CheckComboBox;
import org.japura.gui.event.ListCheckListener;
import org.japura.gui.event.ListEvent;
import org.japura.gui.model.ListCheckModel;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.patern.clustering.hierarchical.Cluster;
import org.patern.clustering.hierarchical.algorithm.ClusteringAlgorithm;
import org.patern.clustering.hierarchical.algorithm.DefaultClusteringAlgorithm;
import org.patern.clustering.hierarchical.visualization.DendrogramPanel;
import org.pattern.analysis.cluster.hierarch.strategy.LinkageStrategyProvider;
import org.pattern.api.analysis.DistanceUtil;
import org.pattern.data.Particle;
import org.pattern.data.ParticleLabel;

/**
 * Top component with dendrogram.
 */
public final class DendrogramTC extends TopComponent implements MultiViewElement, ActionListener {

    private final DistanceAnalysis analysis;
    private final DendrogramPanel dendrogram;
    private final List<LinkageStrategyProvider> linkages;
    
    private MultiViewElementCallback callback;
    private int selectedLinkage;
    private boolean[] selectedClasses;
    private AnalysisComboBoxItem[] items;
    private DefaultComboBoxModel<String> linkageComboBoxModel;

    // Toolbar
    private JToolBar toolbar;
    private JComboBox linkageComboBox;
    private CheckComboBox checkComboBox;
    private JButton recalculateButton;

    public DendrogramTC(Lookup lkp) {
        analysis = lkp.lookup(DistanceAnalysis.class);
        linkages = new ArrayList<>();

        Collection<? extends LinkageStrategyProvider> linkageCollection
                = Lookup.getDefault().lookupAll(LinkageStrategyProvider.class);
        for (LinkageStrategyProvider linkage : linkageCollection) {
            linkages.add(linkage);
        }
        selectedLinkage = 0;

        initComponents();
        initToolbar();

        dendrogram = new DendrogramPanel();
        add(dendrogram, BorderLayout.CENTER);
    }

    private void initToolbar() {
        // choose linkage startegy
        String[] linkageNames = createLinkageNames();
        linkageComboBoxModel = new DefaultComboBoxModel<>(linkageNames);
        linkageComboBox = new JComboBox();
        linkageComboBox.setModel(linkageComboBoxModel);
        linkageComboBox.addActionListener(this);

        // which classes to include
        checkComboBox = new CheckComboBox();
        checkComboBox.setTextFor(CheckComboBox.NONE, "select classes");
        checkComboBox.setTextFor(CheckComboBox.MULTIPLE, "select classes");
        checkComboBox.setTextFor(CheckComboBox.CheckState.MULTIPLE, "select classes");
        checkComboBox.setMaximumSize(new Dimension(200, 40));
        initCheckComboBoxModel();

        // change dendrogram
        recalculateButton = new JButton("Recalculate");
        recalculateButton.addActionListener(this);

        toolbar = new JToolBar();
        toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolbar.add(linkageComboBox);
        toolbar.add(checkComboBox);
        toolbar.add(recalculateButton);
    }

    private AnalysisComboBoxItem[] createItems() {
        if (analysis.classificator != null) {
            int size = analysis.getClassificator().labelCount();
            AnalysisComboBoxItem[] items = new AnalysisComboBoxItem[size];
            int i = 0;
            for (ParticleLabel label : analysis.getClassificator().getLabels()) {
                items[i] = new AnalysisComboBoxItem(label);
                i++;
            }
            return items;
        }
        return new AnalysisComboBoxItem[0];
    }

    private String[] createLinkageNames() {
        String[] linkageNames = new String[linkages.size()];
        for (int j = 0; j < linkages.size(); j++) {
            linkageNames[j] = linkages.get(j).getDisplayName();
        }
        return linkageNames;
    }

    private void initCheckComboBoxModel() {
        items = createItems();
        final ListCheckModel model = checkComboBox.getModel();
        for (AnalysisComboBoxItem item : items) {
            model.addElement(item.name);
        }
        selectedClasses = new boolean[items.length];
        Arrays.fill(selectedClasses, true);
        model.checkAll();
        model.addListCheckListener(new ListCheckListener() {

            @Override
            public void removeCheck(ListEvent e) {
                for (int i = 0; i < items.length; i++) {
                    selectedClasses[i] = model.isChecked(items[i].name);
                }
            }

            @Override
            public void addCheck(ListEvent e) {
                for (int i = 0; i < items.length; i++) {
                    selectedClasses[i] = model.isChecked(items[i].name);
                }
            }
        });
    }

    private void recalculateDendrogram() {
        // Get all involved particles
        List<Particle> particles = new ArrayList<>();
        for (int i = 0; i < selectedClasses.length; i++) {
            if (selectedClasses[i]) {
                ParticleLabel label = analysis.classificator.getLabels().get(i);
                particles.addAll(analysis.image
                        .findParticlesWithLabelId(label.getId()));
            }
        }
        Particle[] arr = particles.toArray(new Particle[particles.size()]);
        Cluster cluster = null;

        if (arr.length > 0) {
            Object[] ids = new Object[arr.length];
            for (int i = 0; i < arr.length; i++) {
                ids[i] = arr[i].getId();
            }
            // We dont need to calculate it again ?? Or just a matlab malformation
            double[][] distances = DistanceUtil.allDistances(arr);
            ClusteringAlgorithm alg = new DefaultClusteringAlgorithm();
            cluster = alg.performClustering(
                    distances, 
                    ids, 
                    linkages.get(selectedLinkage).getStrategy());
        }
        
        analysis.setCluster(cluster);
        dendrogram.setModel(cluster);
        dendrogram.repaint();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    //<editor-fold defaultstate="collapsed" desc="TC Methods">
    @Override
    public void componentOpened() {
        callback.updateTitle("Analysis");
        dendrogram.setModel(analysis.cluster);
    }

    @Override
    public void componentClosed() {
    }

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
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
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
    //</editor-fold>

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == recalculateButton) {
            recalculateDendrogram();
        } else if(source == linkageComboBox){
            selectedLinkage = linkageComboBox.getSelectedIndex();
        }
    }
}
