package com.ShHeaders;

import com.ShContainers.ShPanel;
import java.awt.Color;
import java.awt.Font;

public class ShPanelHeader extends ShPanel {
    
    private Font titleFont;
    private Color titleColor;
    private String titleText;
    
    public ShPanelHeader() {
        initComponents();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        setBackgroundColor(new java.awt.Color(255, 255, 255));
        setBottomLeftRounded(false);
        setBottomRightRounded(false);

        jLabel1.setText("title");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the titleFont
     */
    public Font getTitleFont() {
        return titleFont;
    }

    /**
     * @param titleFont the titleFont to set
     */
    public void setTitleFont(Font titleFont) {
        this.titleFont = titleFont;
        jLabel1.setFont(this.titleFont);
    }

    /**
     * @return the titleColor
     */
    public Color getTitleColor() {
        return titleColor;
    }

    /**
     * @param titleColor the titleColor to set
     */
    public void setTitleColor(Color titleColor) {
        this.titleColor = titleColor;
        this.jLabel1.setForeground(this.titleColor);
    }

    /**
     * @return the titleText
     */
    public String getTitleText() {
        return titleText;
    }

    /**
     * @param titleText the titleText to set
     */
    public void setTitleText(String titleText) {
        this.titleText = titleText;
        this.jLabel1.setText(this.titleText);
    }
    
}
