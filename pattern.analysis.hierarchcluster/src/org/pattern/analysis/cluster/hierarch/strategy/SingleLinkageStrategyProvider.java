/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.analysis.cluster.hierarch.strategy;

import org.openide.util.lookup.ServiceProvider;
import org.patern.clustering.hierarchical.strategy.LinkageStrategy;
import org.patern.clustering.hierarchical.strategy.SingleLinkageStrategy;

/**
 *
 * @author palas
 */
@ServiceProvider(service = LinkageStrategyProvider.class)
public class SingleLinkageStrategyProvider implements LinkageStrategyProvider{

    @Override
    public String getDisplayName() {
        return "Single";
    }

    @Override
    public LinkageStrategy getStrategy() {
        return new SingleLinkageStrategy();
    }
    
}
