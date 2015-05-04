/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.image.inspector;

import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.pattern.api.ColorIcon;


/**
 *
 * @author palas
 */
public class ParticlesTable extends JTable {

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        Class<?> clazz = getModel().getColumnClass(column);
        if(clazz == double.class || clazz == Double.class){
            return new DoubleRenderer();
        }else if(clazz == Color.class){
            return new ColorRenderer();
        }else{
            return super.getCellRenderer(row, column);
        }
    }
    
    private class DoubleRenderer extends DefaultTableCellRenderer{
        DecimalFormat formatter;

        public DoubleRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
            getTableHeader().setDefaultRenderer(new HeaderRenderer());
        }
        
        @Override
        public void setValue(Object value) {
            if (formatter == null) {
                formatter = new DecimalFormat("0.00");
            }
            setText((value == null) ? "" : formatter.format(value));
        }
    }
    
    private class ColorRenderer extends JLabel implements TableCellRenderer{

        
        public ColorRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Color color = (Color) value;
            this.setIcon(new ColorIcon(color));
            return this;
        }
    }
    
    private class HeaderRenderer implements TableCellRenderer {

    DefaultTableCellRenderer renderer;

    public HeaderRenderer() {
        renderer = (DefaultTableCellRenderer)
            new JTable().getTableHeader().getDefaultRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(
        JTable table, Object value, boolean isSelected,
        boolean hasFocus, int row, int col) {
        return renderer.getTableCellRendererComponent(
            table, value, isSelected, hasFocus, row, col);
    }
}
    
    
    
}
