/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.project;

import javax.swing.Action;
import org.openide.loaders.DataNode;
import org.openide.nodes.FilterNode;
import org.openide.util.Lookup;

/**
 * Displays {@link Node} for {@link MultiImageDataObject}.
 * @author palas
 */
public class MultiImageNode extends FilterNode {

    MultiImageDataObject data;

    public MultiImageNode(MultiImageDataObject obj, Lookup lookup) {
        super(
                new DataNode(obj, FilterNode.Children.LEAF, lookup), FilterNode.Children.LEAF,
                lookup
        );
        this.data = obj;
        setDisplayName(obj.getName());
    }
    
    
    
}
