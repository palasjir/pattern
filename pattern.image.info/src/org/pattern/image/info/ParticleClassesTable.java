/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.image.info;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.pattern.api.ColorIcon;
import org.pattern.data.ParticleLabel;

/**
 *
 * @author palas
 */
public class ParticleClassesTable extends JTable{

    public ParticleClassesTable() {
        setTableHeader(null);
    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        Class<?> clazz = getModel().getColumnClass(column);
        if(clazz == ParticleLabel.class){
            return new LabelRenderer();
        }else{
            return super.getCellRenderer(row, column);
        }
    }
    
    private static class LabelRenderer extends JLabel implements TableCellRenderer{

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            ParticleLabel label = (ParticleLabel) value;
            this.setIcon(new ColorIcon(label.getColor()));
            this.setText(label.getName());
            return this;
        }
        
    }
    
}
