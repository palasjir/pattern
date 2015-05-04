/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.project;

import java.awt.Image;
import javax.swing.Action;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.openide.awt.Actions;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 * Root of the pattern project visual representation. All files are supposed
 * to be hidden creates only images are visible.
 * 
 * @author palas
 */
public final class PatternProjectNode extends FilterNode {

    final PatternProject project;

    @StaticResource
    public static final String PATTERN_ICON = "org/pattern/resources/ic_microscope.png";

    public PatternProjectNode(Node node, PatternProject project) throws DataObjectNotFoundException {
        super(node,
                NodeFactorySupport.createCompositeChildren(
                        project,
                        "Projects/org-pattern-project/Nodes"),
                new ProxyLookup(
                        new Lookup[]{
                            Lookups.singleton(project),
                            node.getLookup()
                        }));
        this.project = project;
        
    }

    @Override
    public Action[] getActions(boolean arg0) {
        
        return new Action[]{
            Actions.forID("Images", "org.pattern.project.action.ImportAction"),
            Actions.forID("Project", "org.pattern.project.action.SaveClassificatorAction"),
            CommonProjectActions.deleteProjectAction(),
            CommonProjectActions.closeProjectAction(),
        };
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage(PATTERN_ICON);
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }

    @Override
    public String getDisplayName() {
        return project.getProjectDirectory().getName();
    }
    
}
