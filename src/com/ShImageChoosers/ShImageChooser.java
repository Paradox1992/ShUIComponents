package com.ShImageChoosers;

import com.ShButtons.ShButton;
import com.ShContainers.ShPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Iterator;
import java.util.Locale;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
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

    private static final int DEFAULT_MAX_IMAGE_SIZE = 1920;
    private static final float DEFAULT_JPEG_QUALITY = 0.85f;

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

    /**
     * Decodifica y muestra una imagen recibida como texto Base64.
     * Acepta tanto el contenido Base64 puro como una URL de datos, por ejemplo
     * {@code data:image/png;base64,...}. Un valor nulo o vacio limpia la imagen.
     *
     * @param imagenBase64 imagen codificada en Base64
     * @throws IllegalArgumentException si el texto no contiene una imagen valida
     */
    public void setImagenBase64(String imagenBase64) {
        if (imagenBase64 == null || imagenBase64.isBlank()) {
            selectedFile = null;
            setImagen(null);
            return;
        }

        String contenidoBase64 = extraerContenidoBase64(imagenBase64.trim());
        byte[] imageBytes;

        try {
            imageBytes = Base64.getDecoder().decode(contenidoBase64.replaceAll("\\s", ""));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("El texto no tiene un formato Base64 valido.", exception);
        }

        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));

            if (image == null) {
                throw new IllegalArgumentException("El texto Base64 no contiene una imagen valida.");
            }

            selectedFile = null;
            setImagen(new ImageIcon(image));
        } catch (IOException exception) {
            throw new IllegalArgumentException("No se pudo leer la imagen codificada en Base64.", exception);
        }
    }

    private static String extraerContenidoBase64(String imagenBase64) {
        if (!imagenBase64.regionMatches(true, 0, "data:", 0, 5)) {
            return imagenBase64;
        }

        int separatorIndex = imagenBase64.indexOf(',');
        if (separatorIndex < 0
                || !imagenBase64.substring(0, separatorIndex)
                        .toLowerCase(Locale.ROOT).contains(";base64")) {
            throw new IllegalArgumentException("La URL de datos no contiene una imagen codificada en Base64.");
        }

        return imagenBase64.substring(separatorIndex + 1);
    }

    /**
     * Obtiene la imagen actual como Base64 puro, sin metadatos y optimizada.
     * Las imagenes mayores de 1920 pixeles se redimensionan conservando su
     * proporcion. Se usa PNG si existe transparencia y JPEG en caso contrario.
     *
     * @return imagen saneada en Base64, o {@code null} si no hay imagen
     */
    public String getImagenBase64() {
        return getImagenBase64(
                DEFAULT_MAX_IMAGE_SIZE,
                DEFAULT_MAX_IMAGE_SIZE,
                DEFAULT_JPEG_QUALITY);
    }

    /**
     * Obtiene la imagen actual como Base64 puro, permitiendo configurar la
     * resolucion maxima y la calidad usada al generar JPEG.
     *
     * @param anchoMaximo ancho maximo en pixeles
     * @param altoMaximo alto maximo en pixeles
     * @param calidadJpeg calidad JPEG entre 0 y 1
     * @return imagen saneada en Base64, o {@code null} si no hay imagen
     * @throws IllegalArgumentException si las dimensiones o la calidad no son validas
     * @throws IllegalStateException si la imagen actual no se puede procesar
     */
    public String getImagenBase64(int anchoMaximo, int altoMaximo, float calidadJpeg) {
        validarOpcionesDeOptimizacion(anchoMaximo, altoMaximo, calidadJpeg);

        if (imagen == null) {
            return null;
        }

        BufferedImage image = renderizarIcono(imagen);
        BufferedImage optimizedImage = redimensionar(image, anchoMaximo, altoMaximo);
        boolean hasTransparency = tieneTransparencia(optimizedImage);
        String format = hasTransparency ? "png" : "jpeg";
        BufferedImage imageToEncode = hasTransparency
                ? optimizedImage
                : convertirTipoImagen(optimizedImage, BufferedImage.TYPE_INT_RGB);
        float compressionQuality = hasTransparency ? 0.0f : calidadJpeg;

        return Base64.getEncoder().encodeToString(
                codificarImagen(imageToEncode, format, compressionQuality));
    }

    private static void validarOpcionesDeOptimizacion(
            int anchoMaximo, int altoMaximo, float calidadJpeg) {
        if (anchoMaximo <= 0 || altoMaximo <= 0) {
            throw new IllegalArgumentException("Las dimensiones maximas deben ser mayores que cero.");
        }
        if (Float.isNaN(calidadJpeg) || calidadJpeg < 0.0f || calidadJpeg > 1.0f) {
            throw new IllegalArgumentException("La calidad JPEG debe estar entre 0 y 1.");
        }
    }

    private BufferedImage renderizarIcono(Icon icon) {
        int width = icon.getIconWidth();
        int height = icon.getIconHeight();
        if (width <= 0 || height <= 0) {
            throw new IllegalStateException("La imagen actual no tiene dimensiones validas.");
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        try {
            icon.paintIcon(this, graphics, 0, 0);
        } finally {
            graphics.dispose();
        }
        return image;
    }

    private static BufferedImage redimensionar(
            BufferedImage image, int anchoMaximo, int altoMaximo) {
        double scale = Math.min(
                1.0,
                Math.min(
                        (double) anchoMaximo / image.getWidth(),
                        (double) altoMaximo / image.getHeight()));
        if (scale == 1.0) {
            return image;
        }

        int width = Math.max(1, (int) Math.round(image.getWidth() * scale));
        int height = Math.max(1, (int) Math.round(image.getHeight() * scale));
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = resizedImage.createGraphics();
        try {
            graphics.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            graphics.setRenderingHint(
                    RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
            graphics.drawImage(image, 0, 0, width, height, null);
        } finally {
            graphics.dispose();
        }
        return resizedImage;
    }

    private static boolean tieneTransparencia(BufferedImage image) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if ((image.getRGB(x, y) >>> 24) != 0xFF) {
                    return true;
                }
            }
        }
        return false;
    }

    private static BufferedImage convertirTipoImagen(BufferedImage image, int imageType) {
        BufferedImage convertedImage = new BufferedImage(
                image.getWidth(), image.getHeight(), imageType);
        Graphics2D graphics = convertedImage.createGraphics();
        try {
            graphics.drawImage(image, 0, 0, null);
        } finally {
            graphics.dispose();
        }
        return convertedImage;
    }

    private static byte[] codificarImagen(
            BufferedImage image, String format, float compressionQuality) {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(format);
        if (!writers.hasNext()) {
            throw new IllegalStateException("No existe un codificador para el formato " + format + ".");
        }

        ImageWriter writer = writers.next();
        try (ByteArrayOutputStream output = new ByteArrayOutputStream();
                ImageOutputStream imageOutput = ImageIO.createImageOutputStream(output)) {
            writer.setOutput(imageOutput);
            ImageWriteParam parameters = writer.getDefaultWriteParam();
            if (parameters.canWriteCompressed()) {
                parameters.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                parameters.setCompressionQuality(compressionQuality);
            }
            writer.write(null, new IIOImage(image, null, null), parameters);
            imageOutput.flush();
            return output.toByteArray();
        } catch (IOException exception) {
            throw new IllegalStateException("No se pudo optimizar la imagen actual.", exception);
        } finally {
            writer.dispose();
        }
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
