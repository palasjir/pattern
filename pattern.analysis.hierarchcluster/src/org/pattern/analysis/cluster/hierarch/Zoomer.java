/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.analysis.cluster.hierarch;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author palasjiri
 */
public class Zoomer {

    private static final double DEFAULT_ZOOM = 1;
    private static final double MAX_ZOOM = 5;   // 500%
    private static final double MIN_ZOOM = 0.08; // 8%
    private static final double ratioUnitInc = 0.05;

    private Point zoomedAt;

    /**
     * Zoom ratio. It can be read as percentage zoom
     */
    private double ratio;

    private final List<Listener> listeners = new ArrayList<>();

    public Zoomer() {
        ratio = DEFAULT_ZOOM;
    }

    /**
     * Performes zoom in operation. Increases zoom ration with one ratio unit
     * step. Zoom ratio can't get over max zoom.
     *
     * @param point point under muse while zooming
     */
    public void in(Point point) {
        if (ratio <= MAX_ZOOM) {
            ratio += ratioUnitInc;
        }
        zoomedAt = point;
        fireZoomChanged();
    }

    /**
     * Performes zoom out operation. Decreases zoom ration with one ratio unit
     * step. Zoom ratio can't get under min zoom.
     *
     * @param point point under muse while zooming
     */
    public void out(Point point) {
        if (ratio > MIN_ZOOM) {
            ratio -= ratioUnitInc;
        }
        zoomedAt = point;
        fireZoomChanged();
    }

    public Point getZoomedAt() {
        return zoomedAt;
    }

    public void setZoomedAt(Point zoomedAt) {
        this.zoomedAt = zoomedAt;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    /**
     * Notifies all listeners about change in zoom.
     */
    private void fireZoomChanged() {
        for (Listener listener : listeners) {
            listener.zoomChanged();
        }
    }

    public void registerListener(Listener listener) {
        listeners.add(listener);
    }

    public void unregisterListener(Listener listener) {
        listeners.remove(listener);
    }

    public interface Listener {

        /**
         * Action performed when zoom is changed.
         */
        public void zoomChanged();

    }

}
