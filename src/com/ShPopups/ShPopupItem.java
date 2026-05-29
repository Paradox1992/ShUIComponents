package com.ShPopups;

import com.ShContainers.ShPanel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.MouseInfo;
import javax.swing.Icon;
import javax.swing.JDialog;
import static shui.config.colors.BaseContainerColors.EMPTY_BG;

public class ShPopupItem extends ShPanel {

    private final Cursor DEFAULT_CURSOR = new Cursor(Cursor.HAND_CURSOR);

    private boolean root;
    private JDialog subMenu;
    private Runnable task;
    private Icon icon;
    private String title;
    private Font titleFont;
    private Color enteredColor;
    private Color exitedColor;

    public ShPopupItem() {
        initComponents();
        setBackground(EMPTY_BG);
        setCursor(DEFAULT_CURSOR);
        root = false;

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbl_icon = new javax.swing.JLabel();
        lbl_description = new javax.swing.JLabel();
        lbl_iconSub = new javax.swing.JLabel();

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }
        });

        lbl_description.setText("description");
        lbl_description.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_description.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_descriptionMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lbl_descriptionMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lbl_descriptionMouseExited(evt);
            }
        });

        lbl_iconSub.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbl_iconSub.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lbl_icon, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_description, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_iconSub, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_iconSub, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_icon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_description, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    private void shPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_shPanel1MouseClicked

    }//GEN-LAST:event_shPanel1MouseClicked

    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered

    }//GEN-LAST:event_formMouseEntered

    private void shPanel1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_shPanel1MouseExited

    }//GEN-LAST:event_shPanel1MouseExited

    private void shPanel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_shPanel1MouseEntered

    }//GEN-LAST:event_shPanel1MouseEntered

    private void lbl_descriptionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_descriptionMouseClicked
        if (isRoot()) {
            if (getSubMenu() != null) {
                getSubMenu().setLocation(MouseInfo.getPointerInfo().getLocation());
                getSubMenu().setVisible(true);
            }
        } else {
            if (task != null) {
                task.run();
            }
        }
    }//GEN-LAST:event_lbl_descriptionMouseClicked

    private void lbl_descriptionMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_descriptionMouseEntered
        this.setBackgroundColor(this.enteredColor);
    }//GEN-LAST:event_lbl_descriptionMouseEntered

    private void lbl_descriptionMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_descriptionMouseExited
        this.setBackgroundColor(this.exitedColor);

    }//GEN-LAST:event_lbl_descriptionMouseExited


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lbl_description;
    private javax.swing.JLabel lbl_icon;
    private javax.swing.JLabel lbl_iconSub;
    // End of variables declaration//GEN-END:variables

    public void setRoot(boolean root) {
        this.root = root;
        if (isRoot()) {
            lbl_iconSub.setText(">");
        } else {
            lbl_iconSub.setText("");
        }

    }

    public boolean isRoot() {
        return this.root;
    }

    public void setSubMenu(JDialog subMenu) {
        this.subMenu = subMenu;
    }

    public JDialog getSubMenu() {
        return subMenu;
    }

    public void setTask(Runnable task) {
        this.task = task;
    }

    public Runnable getTask() {
        return task;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
        lbl_icon.setIcon(this.icon);
    }

    public Icon getIcon() {
        return icon;
    }

    public void setTitle(String title) {
        this.title = title;
        lbl_description.setText(this.title);
    }

    public String getTitle() {
        return title;
    }

    public void setTitleFont(Font titleFont) {
        this.titleFont = titleFont;
        lbl_description.setFont(this.titleFont);
    }

    public Font getTitleFont() {
        return titleFont;
    }

    public void setEnteredColor(Color enteredColor) {
        this.enteredColor = enteredColor;
    }

    public Color getEnteredColor() {
        return enteredColor;
    }

    public void setExitedColor(Color exitedColor) {
        this.exitedColor = exitedColor;

    }

    public Color getExitedColor() {
        return exitedColor;
    }

}
