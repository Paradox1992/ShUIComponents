package com.ShPopups;

import com.ShContainers.ShPanel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;
import static shui.config.colors.BaseContainerColors.EMPTY_BG;

public class ShPopupItem extends ShPanel {

    private static final Cursor HAND_CURSOR = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

    private final EventListenerList actionListeners = new EventListenerList();
    private boolean root;
    private JDialog subMenu;
    private Runnable task;
    private Icon icon;
    private String title;
    private Font titleFont;
    private Color enteredColor = new Color(235, 235, 235);
    private Color exitedColor = EMPTY_BG;

    public ShPopupItem() {
        initComponents();
        setBackground(EMPTY_BG);
        setCursor(HAND_CURSOR);
        root = false;
        title = lbl_description.getText();
        titleFont = lbl_description.getFont();
        installClickSupport();
        setBottomLeftRounded(false);
        setBottomRightRounded(false);
        setTopLeftRounded(false);
        setTopRightRounded(false);
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
                .addContainerGap()
                .addComponent(lbl_icon, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_description, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
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
        handleMouseClick(evt);
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

    private void installClickSupport() {
        MouseAdapter clickListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                handleMouseClick(event);
            }
        };
        addMouseListener(clickListener);
        lbl_icon.addMouseListener(clickListener);
        lbl_iconSub.addMouseListener(clickListener);
        lbl_icon.setCursor(HAND_CURSOR);
        lbl_iconSub.setCursor(HAND_CURSOR);
    }

    private void handleMouseClick(MouseEvent event) {
        if (event != null && SwingUtilities.isLeftMouseButton(event)) {
            doClick();
        }
    }

    /**
     * Activa el item igual que un clic principal del usuario.
     */
    public void doClick() {
        if (!isEnabled()) {
            return;
        }
        if (isRoot()) {
            showSubMenu();
            return;
        }
        fireActionPerformed();
    }

    private void showSubMenu() {
        JDialog menu = getSubMenu();
        if (menu == null) {
            return;
        }
        if (isShowing()) {
            Point location = getLocationOnScreen();
            menu.setLocation(location.x + getWidth(), location.y);
        } else {
            PointerInfo pointerInfo = MouseInfo.getPointerInfo();
            if (pointerInfo != null) {
                menu.setLocation(pointerInfo.getLocation());
            }
        }
        menu.setVisible(true);
    }

    private void fireActionPerformed() {
        Runnable currentTask = task;
        if (currentTask != null) {
            currentTask.run();
        }
        ActionEvent event = new ActionEvent(
                this,
                ActionEvent.ACTION_PERFORMED,
                title != null ? title : ""
        );
        for (ActionListener listener : getActionListeners()) {
            listener.actionPerformed(event);
        }
    }

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
        setOnClick(task);
    }

    public Runnable getTask() {
        return getOnClick();
    }

    public void setOnClick(Runnable onClick) {
        this.task = onClick;
    }

    public Runnable getOnClick() {
        return task;
    }

    public void addActionListener(ActionListener listener) {
        if (listener != null) {
            actionListeners.add(ActionListener.class, listener);
        }
    }

    public void removeActionListener(ActionListener listener) {
        if (listener != null) {
            actionListeners.remove(ActionListener.class, listener);
        }
    }

    public ActionListener[] getActionListeners() {
        return actionListeners.getListeners(ActionListener.class);
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
        lbl_icon.setIcon(this.icon);
    }

    public Icon getIcon() {
        return icon;
    }

    public void setTitle(String title) {
        this.title = title != null ? title : "";
        lbl_description.setText(this.title);
    }

    public String getTitle() {
        return title;
    }

    public void setTitleFont(Font titleFont) {
        if (titleFont == null) {
            return;
        }
        this.titleFont = titleFont;
        lbl_description.setFont(this.titleFont);
    }

    public Font getTitleFont() {
        return titleFont;
    }

    public void setEnteredColor(Color enteredColor) {
        if (enteredColor != null) {
            this.enteredColor = enteredColor;
        }
    }

    public Color getEnteredColor() {
        return enteredColor;
    }

    public void setExitedColor(Color exitedColor) {
        if (exitedColor != null) {
            this.exitedColor = exitedColor;
        }

    }

    public Color getExitedColor() {
        return exitedColor;
    }

}
