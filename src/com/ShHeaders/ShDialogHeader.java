package com.ShHeaders;

import com.ShContainers.ShPanel;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JDialog;

public class ShDialogHeader extends ShPanel {
    
    private String title;
    private JDialog dialog;
    private Color titleColor;
    private Font titleFont;
    private Color buttonColor;
    
    public ShDialogHeader() {
        initComponents();
        shButton1.setCustomColor(new Color(255, 255, 255, 0));
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbl_shdilog_title = new javax.swing.JLabel();
        shButton1 = new com.ShButtons.ShButton();

        setBackgroundColor(new java.awt.Color(255, 255, 255));
        setBottomLeftRounded(false);
        setBottomRightRounded(false);

        lbl_shdilog_title.setBackground(new java.awt.Color(255, 255, 255));
        lbl_shdilog_title.setText("Title");

        shButton1.setCustomColor(new java.awt.Color(255, 255, 255));
        shButton1.setIconSize(shui.contracts.button.Buttonable.ButtonIconSize.X32);
        shButton1.setText("");
        shButton1.setImage(new javax.swing.ImageIcon(getClass().getResource("/shui/assets/cerrar_32.png"))); // NOI18N
        shButton1.setImageScale(shui.contracts.visual.Imageable.ImageScale.CENTER);
        shButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_shdilog_title, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(shButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_shdilog_title, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(shButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void shButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shButton1ActionPerformed
        if (this.dialog != null) {
            this.dialog.dispose();
        }
    }//GEN-LAST:event_shButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lbl_shdilog_title;
    private com.ShButtons.ShButton shButton1;
    // End of variables declaration//GEN-END:variables

    public void setTitle(String title) {
        this.title = title;
        lbl_shdilog_title.setText(this.title);
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setDialog(JDialog dialog) {
        this.dialog = dialog;
    }
    
    public JDialog getDialog() {
        return dialog;
    }
    
    @Override
    public void setBackgroundColor(Color color) {
        super.setBackgroundColor(color);
        
    }
    
    public void setTitleColor(Color titleColor) {
        this.titleColor = titleColor;
        this.lbl_shdilog_title.setForeground(this.titleColor);
    }
    
    public Color getTitleColor() {
        return titleColor;
    }
    
    public void setTitleFont(Font titleFont) {
        this.titleFont = titleFont;
        this.lbl_shdilog_title.setFont(this.titleFont);
    }
    
    public Font getTitleFont() {
        return titleFont;
    }
    
    public void setButtonColor(Color buttonColor) {
        this.buttonColor = buttonColor;
        this.shButton1.setBackgroundColor(this.buttonColor);
    }
    
    public Color getButtonColor() {
        return buttonColor;
    }
    
}
