package com.ShImageChoosers;

import com.ShButtons.ShButton;
import com.ShContainers.ShPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import shui.contracts.button.Buttonable;
import shui.contracts.visual.Imageable;

/**
 * Selector visual de imagen Shui con panel de vista previa y boton de busqueda.
 */
public class ShImageChooser extends ShPanel {

    private final ShPanel imagePanel = new ShPanel();
    private final ShButton searchButton = new ShButton();

    private Icon imagen;
    private File selectedFile;

    public ShImageChooser() {
        configureComponent();
    }

    private void configureComponent() {
        setLayout(new BorderLayout(0, 8));
        setBackgroundColor(Color.WHITE);
        setContentPadding(8);

        imagePanel.setPreferredSize(new Dimension(220, 150));
        imagePanel.setBackgroundColor(new Color(248, 249, 250));
        imagePanel.setBorderEnabled(true);
        imagePanel.setBorderColor(new Color(222, 226, 230));
        imagePanel.setImageEnabled(true);
        imagePanel.setImageScale(Imageable.ImageScale.FIT);

        searchButton.setText("Buscar imagen");
        searchButton.setButtonType(Buttonable.ButtonType.BOOTSTRAP);
        searchButton.setBootstrapButton(Buttonable.BootstrapButton.PRIMARY);
        searchButton.addActionListener(event -> chooseImage());

        add(imagePanel, BorderLayout.CENTER);
        add(searchButton, BorderLayout.SOUTH);
    }

    private void chooseImage() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter(
                "Imagenes (*.png, *.jpg, *.jpeg)",
                "png", "jpg", "jpeg"));

        if (selectedFile != null) {
            chooser.setSelectedFile(selectedFile);
        }

        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = chooser.getSelectedFile();
            setImagen(new ImageIcon(selectedFile.getAbsolutePath()));
        }
    }

    public void setImagen(Icon imagen) {
        this.imagen = imagen;
        imagePanel.setImage(imagen);
        imagePanel.setImageEnabled(imagen != null);
        repaint();
    }

    public Icon getImagen() {
        return imagen;
    }

    public File getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
        if (selectedFile != null) {
            setImagen(new ImageIcon(selectedFile.getAbsolutePath()));
        } else {
            setImagen(null);
        }
    }

    public void setImageScale(Imageable.ImageScale scale) {
        imagePanel.setImageScale(scale);
    }

    public Imageable.ImageScale getImageScale() {
        return imagePanel.getImageScale();
    }

    public void setPreviewBackground(Color color) {
        imagePanel.setBackgroundColor(color);
    }

    public Color getPreviewBackground() {
        return imagePanel.getBackgroundColor();
    }

    public void setPreviewBorderColor(Color color) {
        imagePanel.setBorderColor(color);
    }

    public Color getPreviewBorderColor() {
        return imagePanel.getBorderColor();
    }

    public void setButtonText(String text) {
        searchButton.setText(text);
    }

    public String getButtonText() {
        return searchButton.getText();
    }

    public void setButtonType(Buttonable.ButtonType type) {
        searchButton.setButtonType(type);
    }

    public Buttonable.ButtonType getButtonType() {
        return searchButton.getButtonType();
    }

    public void setActionButton(Buttonable.ActionButton action) {
        searchButton.setActionButton(action);
    }

    public Buttonable.ActionButton getActionButton() {
        return searchButton.getActionButton();
    }

    public void setBootstrapButton(Buttonable.BootstrapButton button) {
        searchButton.setBootstrapButton(button);
    }

    public Buttonable.BootstrapButton getBootstrapButton() {
        return searchButton.getBootstrapButton();
    }

    public void setIconSize(Buttonable.ButtonIconSize size) {
        searchButton.setIconSize(size);
    }

    public Buttonable.ButtonIconSize getIconSize() {
        return searchButton.getIconSize();
    }

    public void setCustomIcon(Icon icon) {
        searchButton.setCustomIcon(icon);
    }

    public Icon getCustomIcon() {
        return searchButton.getCustomIcon();
    }

    public void setCustomColor(Color color) {
        searchButton.setCustomColor(color);
    }

    public Color getCustomColor() {
        return searchButton.getCustomColor();
    }

    public void setCustomForeground(Color color) {
        searchButton.setCustomForeground(color);
    }

    public Color getCustomForeground() {
        return searchButton.getCustomForeground();
    }

    public void setButtonFont(Font font) {
        searchButton.setButtonFont(font);
    }

    public Font getButtonFont() {
        return searchButton.getButtonFont();
    }

    public void setIconTextGap(int gap) {
        searchButton.setIconTextGap(gap);
    }

    public int getIconTextGap() {
        return searchButton.getIconTextGap();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        imagePanel.setEnabled(enabled);
        searchButton.setEnabled(enabled);
    }

    public ShPanel getImagePanel() {
        return imagePanel;
    }

    public ShButton getSearchButton() {
        return searchButton;
    }
}
