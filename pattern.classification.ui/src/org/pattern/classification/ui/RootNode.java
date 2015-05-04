/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.classification.ui;

import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.pattern.data.Classificator;
import org.pattern.project.PatternProject;

/**
 * Serves as root node for Classificator explorer.
 *
 * @author palas
 */
public class RootNode extends AbstractNode {

    private static final String NAME = "ClassificatorsRoot";

    public RootNode() {
        super(Children.create(new ClassificatorChildFactory(), true));
        setDisplayName(NAME);
    }

    private static class ClassificatorChildFactory extends ChildFactory.Detachable<PatternProject> implements PropertyChangeListener, ChangeListener {

        @Override
        protected boolean createKeys(List<PatternProject> toPopulate) {
            Project[] projects = OpenProjects.getDefault().getOpenProjects();
            for (Project project : projects) {
                PatternProject p = project.getLookup().lookup(PatternProject.class);
                if (p != null) {
                    p.addChangeListener(this);
                    if (p.getClassificator() != null) {
                        toPopulate.add(p);
                    }
                }
            }

            return true;
        }

        @Override
        protected Node createNodeForKey(PatternProject key) {
            ClassificatorNode node = null;
            try {
                node = new ClassificatorNode(key);
            } catch (IntrospectionException ex) {
                Exceptions.printStackTrace(ex);
            }
            return node;
        }

        @Override
        protected void addNotify() {
            // whenever new projects apears in a lookup
            OpenProjects.getDefault().addPropertyChangeListener(this);

        }

        @Override
        protected void removeNotify() {
            OpenProjects.getDefault().removePropertyChangeListener(this);
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            refresh(true);
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            refresh(true);
        }
    }
}
