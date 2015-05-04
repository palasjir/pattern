/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.analysis.cluster.hierarch;

import java.awt.Color;
import org.pattern.data.ParticleLabel;

/**
 *
 * @author palas
 */
public class AnalysisComboBoxItem {
    
    public Color color;
    public String name;
    public boolean checked = true;

    public AnalysisComboBoxItem(ParticleLabel label) {
        color = label.getColor();
        name = label.getName();
    }
    
}
