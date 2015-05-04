/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.image.inspector;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.swing.table.AbstractTableModel;
import org.pattern.data.Classificator;
import org.pattern.data.Particle;
import org.pattern.data.ParticleLabel;

/**
 *
 * @author palas
 */
public class SelectedParticlesTableModel extends AbstractTableModel{
    
    Classificator classificator;
    List<Particle> particles = new ArrayList<Particle>();

    public SelectedParticlesTableModel(Classificator classifciator, Set<Particle> particles) {
        this.particles.addAll(particles);
        Collections.sort(this.particles);
        this.classificator = classifciator;
    }
    
    @Override
    public int getRowCount() {
        return particles.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Particle p = particles.get(row);
        switch (col) {
            case 0:
                return p.getId();
            case 1:
                return p.cog().x;
            case 2:
                return p.cog().y;
            case 3:
                if(classificator != null){
                    return classificator.getLabel(p.getLabelId()).getColor();
                }else{
                    return ParticleLabel.UNKNOWN.getColor();
                }
            default:
                throw new AssertionError();
        }
    }

    @Override
    public Class<?> getColumnClass(int col) {
        switch (col) {
            case 0:
                return int.class;
            case 1:
            case 2:
                return double.class;
            case 3:
                return Color.class;
            default:
                throw new AssertionError();
        }
    }

    @Override
    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return "id";
            case 1:
                return "x";
            case 2:
                return "y";
            case 3:
                return "label";
            default:
                throw new AssertionError();
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
}
