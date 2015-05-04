/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.classification.ui;

import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.nodes.NodeEvent;
import org.openide.nodes.NodeListener;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.NodeReorderEvent;
import org.openide.util.Exceptions;
import org.pattern.data.Classificator;
import org.pattern.data.ParticleLabel;

/**
 * Crates labels children for classificator node.
 * @author palas
 */
public class LabelsChildFactory extends ChildFactory.Detachable<ParticleLabel> implements ChangeListener, NodeListener {
    
    private final Classificator classificator;
    
    public LabelsChildFactory(Classificator classificator) {
        this.classificator = classificator;
        classificator.addChangeListener(this);
        
    }
    
    @Override
    protected boolean createKeys(List<ParticleLabel> toPopulate) {
        for (ParticleLabel label : classificator.getLabels()) {
            toPopulate.add(label);
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(ParticleLabel key) {
        LabelNode node = null;
        try {
            node = new LabelNode(key);
            node.addNodeListener(this);
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return node;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        refresh(true);
    }

    @Override
    public void childrenAdded(NodeMemberEvent ev) {
        
    }

    @Override
    public void childrenRemoved(NodeMemberEvent ev) {
    }

    @Override
    public void childrenReordered(NodeReorderEvent ev) {
        
    }

    @Override
    public void nodeDestroyed(NodeEvent ev) {
        ParticleLabel label = ev.getNode().getLookup().lookup(ParticleLabel.class);
        classificator.removeLabel(label); // calls classificator state changed
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
    
}
