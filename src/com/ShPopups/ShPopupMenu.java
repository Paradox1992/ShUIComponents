package com.ShPopups;

import java.awt.Dimension;
import java.awt.MouseInfo;
import javax.swing.JDialog;

import static shui.config.colors.BaseContainerColors.EMPTY_BG;

public class ShPopupMenu extends JDialog {

    public ShPopupMenu() {
        this.setUndecorated(true);
        this.setBackground(EMPTY_BG);
        this.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                dispose();
            }
        });

    }

    public void showDialog() {
        this.setLocation(MouseInfo.getPointerInfo().getLocation());
        this.setVisible(true);
    }

    @Override
    public void setSize(Dimension d) {
        super.setSize(d);
        super.setPreferredSize(d);
        
    }

}
