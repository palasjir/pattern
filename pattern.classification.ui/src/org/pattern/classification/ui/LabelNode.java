/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.classification.ui;

import java.awt.Image;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import javax.swing.Action;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.actions.DeleteAction;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;
import org.pattern.api.ColorIcon;
import org.pattern.data.ParticleLabel;

/**
 * Node displaying label.
 *
 * @author palas
 */
public class LabelNode extends BeanNode<ParticleLabel> implements PropertyChangeListener{
    
    private final ParticleLabel label;
    
    LabelNode(ParticleLabel bean) throws IntrospectionException {
        super(bean, Children.LEAF, Lookups.singleton(bean));
        this.label = bean;
        setDisplayName(bean.getName());
        label.addPropertyChangeListener(this);
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.icon2Image(new ColorIcon(label.getColor()));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("color")){
            fireIconChange();
        }else if(evt.getPropertyName().equals("name")){
            fireDisplayNameChange(getDisplayName(), (String) evt.getNewValue());
        }
    }

    @Override
    public Action[] getActions(boolean context) {
        return new Action[]{SystemAction.get(DeleteAction.class)};
    }

    @Override
    public boolean canDestroy() {
        return !label.isUnknown();       
    }

    @Override
    public void destroy() throws IOException {
        fireNodeDestroyed();
    }

}
