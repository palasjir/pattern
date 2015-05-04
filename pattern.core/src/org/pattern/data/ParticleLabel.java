/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.data;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import org.netbeans.api.settings.ConvertAsJavaBean;

/**
 * Represents the class of particles.
 *
 * @author palas
 */
@ConvertAsJavaBean()
public class ParticleLabel {
    
    protected int id;
    protected String name;
    protected Color color;
    
    private transient final PropertyChangeSupport propertyChangeSupport;

    public ParticleLabel() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }
    
    public ParticleLabel(int id, String name, Color color){
        this();
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }
    
    public int getId(){
        return id;
    }

    public void setName(String name) {
        String oldValue = this.name;
        this.name = name;
        propertyChangeSupport.firePropertyChange("name", oldValue, name);
    }
    
    public Color getColor() {
        return color;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public void setColor(Color color) {
        Color oldValue = this.color;
        this.color = color;
        propertyChangeSupport.firePropertyChange("color", oldValue, color);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ParticleLabel other = (ParticleLabel) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    public boolean isUnknown(){
        return id == UNKNOWN_ID;
    }
    
    public static final int UNKNOWN_ID = -1;
    private static final String UNKNOWN_NAME = "unknown";
    public static final ParticleLabel UNKNOWN = new ParticleLabel(UNKNOWN_ID,
            UNKNOWN_NAME, Color.BLACK);
    
}
