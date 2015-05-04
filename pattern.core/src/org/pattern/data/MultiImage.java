/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents group of images. This can come handy if we are dealing for example
 * with multi tiffs.
 *
 * @author palas
 */
public class MultiImage {
    
    /** Image name. */
    public String name;
    
    /** Path to image. */
    public String image;
    
    /**
     * Stack of images in this dataset.
     */
    protected List<ParticleImage> group;

    /**
     * Index of currently selected data. By defaut is set to zero, wcich
     * corresponds to first image in stack if there is any.
     */
    protected Integer selected = 0;

    private transient final List<Listener> observers = new ArrayList<>();

    /**
     * Constructs pattern data.
     *
     * @param images stack of images
     */
    public MultiImage(List<ParticleImage> images) {
        if (images.isEmpty()) {
            throw new IllegalArgumentException("Pointles to create group of images with data");
        } else {
            group = Collections.unmodifiableList(images);
        }
    }

    /**
     * Getter of image which is currently selected in data.
     *
     * @return selected PatternImage
     */
    public ParticleImage getSelectedImage() {
        return group.get(selected);
    }

    /**
     * @return index of selected image (from 0 to numImages -1)
     */
    public int getSelectedImageIndex() {
        return selected;
    }

    /**
     * Sets selected image by index. Also fires {@link Listener#onSelectedChanged()}.
     *
     * @param index
     * @throws IndexOutOfBoundsException if index is out of bounds
     */
    public void setSelectedImageIndex(int index) {
            if (index < 0 || index > group.size() - 1) {
                throw new IndexOutOfBoundsException("Selected image index not possible. Index:" + index);
            } else {
                selected = index;
                fireChange();
            }
    }

    /**
     * @return number of images in stack
     */
    public int size() {
        return group.size();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": [" + group.size() + "]";
    }

    /**
     * @return unmodifiable list of images
     */
    public List<ParticleImage> getImages() {
        return group;
    }

    public void registerListener(Listener observer) {
        observers.add(observer);
    }

    public void unregisterListener(Listener observer) {
        observers.remove(observer);
    }

    public void fireChange() {
        for (Listener listener : observers) {
            listener.onSelectedChanged();
        }
    }
    
    /**
     * Implement this listener to observe internal changes.
     */
    public interface Listener {
        
        /**
         * Notifies that selected image property changed.
         */
        public void onSelectedChanged();

    }
}
