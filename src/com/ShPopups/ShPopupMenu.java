package com.ShPopups;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.MouseInfo;
import javax.swing.JComponent;
import javax.swing.JDialog;

import static shui.config.colors.BaseContainerColors.EMPTY_BG;

public class ShPopupMenu extends JDialog {

    public ShPopupMenu() {
        this.setUndecorated(true);
        applyTransparentBackground();
        this.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                dispose();
            }
        });

    }

    @Override
    public void setContentPane(Container contentPane) {
        super.setContentPane(contentPane);
        applyTransparentContent(contentPane);
    }

    public void showDialog() {
        applyTransparentHierarchy();
        this.setLocation(MouseInfo.getPointerInfo().getLocation());
        this.setVisible(true);
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            applyTransparentHierarchy();
        }
        super.setVisible(visible);
    }

    @Override
    public void setSize(Dimension d) {
        super.setSize(d);
        super.setPreferredSize(d);
        
    }

    private void applyTransparentBackground() {
        setBackground(EMPTY_BG);
        getRootPane().setOpaque(false);
        getLayeredPane().setOpaque(false);
        applyTransparentContent(getContentPane());
    }

    private void applyTransparentContent(Container contentPane) {
        if (contentPane == null) {
            return;
        }
        contentPane.setBackground(EMPTY_BG);
        if (contentPane instanceof JComponent component) {
            component.setOpaque(false);
        }
        applyTransparentChildren(contentPane);
    }

    private void applyTransparentHierarchy() {
        setBackground(EMPTY_BG);
        getRootPane().setOpaque(false);
        getLayeredPane().setOpaque(false);
        applyTransparentContent(getContentPane());
    }

    private void applyTransparentChildren(Container container) {
        for (Component child : container.getComponents()) {
            if (child instanceof JComponent component) {
                component.setOpaque(false);
            }
            if (child instanceof Container childContainer) {
                applyTransparentChildren(childContainer);
            }
        }
    }

}
