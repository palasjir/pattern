/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.image.info;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import org.pattern.data.ParticleLabel;

/**
 *
 * @author palas
 */
public class ParticleClassesTableModel extends AbstractTableModel {

    List<ParticleClassesInfo> infos;

    public ParticleClassesTableModel(List<ParticleClassesInfo> info) {
        this.infos = info;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public int getRowCount() {
        return infos.size();
    }

    @Override
    public String getColumnName(int column) {
        return "id";
    }

    @Override
    public Object getValueAt(int row, int column) {
        ParticleClassesInfo info = infos.get(row);
        switch (column) {
            case 0:
                return info.label;
            case 1:
                return info.count;
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int col) {
        switch (col) {
            case 0:
                return ParticleLabel.class;
            default:
                return super.getColumnClass(col);
        }
    }
    
    
}
