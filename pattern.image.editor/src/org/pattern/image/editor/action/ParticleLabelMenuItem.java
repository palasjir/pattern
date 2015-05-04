/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.image.editor.action;

import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import org.pattern.api.ColorIcon;
import org.pattern.data.ParticleImage;
import org.pattern.data.ParticleLabel;

/**
 *
 * @author palas
 */
public class ParticleLabelMenuItem extends JMenuItem {

    private final ParticleImage image;
    private final ParticleLabel label;

    public ParticleLabelMenuItem(ParticleImage image, ParticleLabel label, ActionListener listener) {
        super(label.getName(), new ColorIcon(label.getColor()));
        this.label = label;
        this.image = image;
        addActionListener(listener);
    }

}
