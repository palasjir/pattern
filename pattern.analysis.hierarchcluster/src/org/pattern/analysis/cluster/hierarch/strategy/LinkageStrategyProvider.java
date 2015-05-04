/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.analysis.cluster.hierarch.strategy;

import org.patern.clustering.hierarchical.strategy.LinkageStrategy;

/**
 *
 * @author palas
 */
public interface LinkageStrategyProvider {
    
    public String getDisplayName();
    public LinkageStrategy getStrategy();
    
}
