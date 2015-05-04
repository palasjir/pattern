/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.api.analysis;

import org.pattern.project.MultiImageDataObject;

/**
 *
 * @author palas
 */
public interface AnalysisProvider {
    
    /**
     * Opens new analysis window for image.
     * @param obj 
     */
    public void openAnalysis(MultiImageDataObject obj);
    
    /**
     * Provider display name. Used in chooser.
     * @return 
     */
    public String getDisplayName();
    
}
