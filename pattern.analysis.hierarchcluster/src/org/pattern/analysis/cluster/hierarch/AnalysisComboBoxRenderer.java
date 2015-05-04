/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.analysis.cluster.hierarch;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.pattern.api.ColorIcon;

/**
 *
 * @author palas
 */
public class AnalysisComboBoxRenderer implements ListCellRenderer<AnalysisComboBoxItem> {
    
    @Override
    public Component getListCellRendererComponent(
            JList<? extends AnalysisComboBoxItem> list, 
            final AnalysisComboBoxItem value, 
            int index, 
            boolean isSelected, 
            boolean cellHasFocus) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel();
        label.setOpaque(true);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setText(value.name);
        label.setIcon(new ColorIcon(value.color));
        
        final JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(value.checked);
        checkBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                value.checked = checkBox.isSelected();
            }
        });
        
        if(cellHasFocus){
            checkBox.setFocusable(true);
            checkBox.requestFocusInWindow();
        }
        
        if (isSelected) {
                panel.setBackground(list.getSelectionBackground());
                panel.setForeground(list.getSelectionForeground());
            } else {
                panel.setBackground(list.getBackground());
                panel.setForeground(list.getForeground());
        }
        
        
        
        
        panel.add(label);
        panel.add(checkBox);
        
        return panel;
    }
    
}
