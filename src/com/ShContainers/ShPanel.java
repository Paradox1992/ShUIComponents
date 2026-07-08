package com.ShContainers;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Component;
import java.awt.Container;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.util.Arrays;
import shui.components.base.BaseContainer;
import static shui.config.colors.BaseContainerColors.EMPTY_BG;
import shui.contracts.text.ShInputFormable;
import shui.contracts.text.ShInputValidator;
import shui.contracts.visual.Borderable.BorderStyle;
import shui.contracts.visual.Gradientable.GradientDirection;
import shui.contracts.visual.Shadowable.ShadowElevation;
import shui.repositories.text.ShInputValidationRepositories;

/**
 * Panel Swing de propósito general con esquinas redondeadas, colores, sombra,
 * degradado, borde y control de acceso integrados.
 *
 * <p>
 * Extiende {@link BaseContainer}, por lo que hereda automáticamente todas las
 * capacidades visuales y de seguridad sin necesidad de código adicional
 * .</p>
 */
public class ShPanel extends BaseContainer implements ShInputFormable {

    public enum ShPanelStyle {
        DEFAULT,
        LIQUID_GLASS,
        BLURR,
        PLASMA
    }

    private static final ShInputValidator INPUT_VALIDATOR = ShInputValidationRepositories.defaultRepository();
    private static final int BLURR_BACKDROP_RADIUS = 4;

    private ShPanelStyle panelStyle = ShPanelStyle.DEFAULT;
    private boolean paintingBackdropSnapshot;

    // ── Constructores ────────────────────────────────────────────────────────
    /**
     * Crea un ShPanel con valores por defecto (radio 10, fondo transparente).
     */
    public ShPanel() {
        super(10, EMPTY_BG);
        setBackground(EMPTY_BG);
        applyPanelStyle();
    }

    public void setPanelStyle(ShPanelStyle style) {
        this.panelStyle = style != null ? style : ShPanelStyle.DEFAULT;
        applyPanelStyle();
    }

    public ShPanelStyle getPanelStyle() {
        return panelStyle;
    }

    @Override
    public void paint(Graphics g) {
        if (paintingBackdropSnapshot) {
            return;
        }
        super.paint(g);
    }

    @Override
    public void clearForm() {
        INPUT_VALIDATOR.clearForm(this);
    }

    @Override
    public boolean validForm(Component... exclude) {
        return INPUT_VALIDATOR.validForm(this, exclude);
    }

    @Override
    protected void onPaintBackground(Graphics2D g2, Shape shape) {
        if (shape == null) {
            return;
        }
        switch (panelStyle) {
            case LIQUID_GLASS ->
                paintLiquidGlass(g2, shape);
            case BLURR ->
                paintBlurr(g2, shape);
            case PLASMA ->
                paintPlasma(g2, shape);
            default -> {
            }
        }
    }

    private void applyPanelStyle() {
        resetPanelStyle();
        switch (panelStyle) {
            case LIQUID_GLASS ->
                applyLiquidGlassStyle();
            case BLURR ->
                applyBlurrStyle();
            case PLASMA ->
                applyPlasmaStyle();
            default -> {
            }
        }
        repaint();
    }

    private void resetPanelStyle() {
        setAlpha(1f);
        setAllCornersRounded(true);
        setCornerRadius(10);
        setBackground(EMPTY_BG);
        setBackgroundColor(EMPTY_BG);
        setGradientEnabled(false);
        setBorderGradientEnabled(false);
        setBorderEnabled(false);
        setBorderWidth(1f);
        setBorderStyle(BorderStyle.SOLID);
        setBorderSides(true, true, true, true);
        setShadowElevation(ShadowElevation.NONE);
        setInnerShadowEnabled(false);
        setHoverEnabled(false);
        setHoverColor(new Color(0, 0, 0, 0));
    }

    private void applyLiquidGlassStyle() {
        setCornerRadius(24);
        setBackgroundColor(new Color(255, 255, 255, 150));
        setGradient(new Color(255, 255, 255, 210), new Color(215, 235, 255, 120),
                GradientDirection.DIAGONAL_RIGHT);
        setBorderGradient(new Color(255, 255, 255, 230), new Color(126, 212, 255, 120));
        setBorderWidth(1.2f);
        setShadow(20, new Color(32, 80, 160, 55), 0, 8);
        setInnerShadow(new Color(255, 255, 255, 100), 8);
        setHoverEnabled(true);
        setHoverColor(new Color(255, 255, 255, 42));
    }

    private void applyBlurrStyle() {
        setCornerRadius(20);
        setBackgroundColor(new Color(245, 248, 255, 55));
        setGradient(new Color(250, 252, 255, 82), new Color(232, 238, 248, 54),
                GradientDirection.VERTICAL);
        setBorderGradient(new Color(255, 255, 255, 220), new Color(142, 166, 205, 130));
        setBorderWidth(1.2f);
        setShadow(34, new Color(20, 35, 55, 58), 0, 12);
        setInnerShadow(new Color(255, 255, 255, 85), 12);
        setHoverEnabled(true);
        setHoverColor(new Color(255, 255, 255, 45));
    }

    private void applyPlasmaStyle() {
        setCornerRadius(22);
        setBackgroundColor(new Color(20, 10, 48, 245));
        setGradient(new Color(34, 9, 77), new Color(0, 94, 154), GradientDirection.DIAGONAL_RIGHT);
        setBorderGradient(new Color(255, 79, 216, 210), new Color(0, 241, 255, 210));
        setBorderWidth(1.4f);
        setShadow(24, new Color(123, 50, 255, 85), 0, 8);
        setInnerShadow(new Color(255, 255, 255, 35), 6);
        setHoverEnabled(true);
        setHoverColor(new Color(255, 255, 255, 28));
    }

    private void paintLiquidGlass(Graphics2D g2, Shape shape) {
        Graphics2D copy = (Graphics2D) g2.create();
        copy.setClip(shape);
        int width = getWidth();
        int height = getHeight();
        copy.setPaint(new GradientPaint(0, 0, new Color(255, 255, 255, 105),
                0, Math.max(1, height / 2), new Color(255, 255, 255, 0)));
        copy.fill(shape);
        copy.setColor(new Color(255, 255, 255, 70));
        copy.setStroke(new BasicStroke(Math.max(10f, height * 0.08f), BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
        copy.drawLine(-width / 5, height / 4, width, -height / 8);
        copy.dispose();
    }

    private void paintBlurr(Graphics2D g2, Shape shape) {
        Graphics2D copy = (Graphics2D) g2.create();
        copy.setClip(shape);
        BufferedImage backdrop = createBlurredBackdrop(BLURR_BACKDROP_RADIUS);
        if (backdrop != null) {
            Composite originalComposite = copy.getComposite();
            copy.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.94f));
            copy.drawImage(backdrop, 0, 0, null);
            copy.setComposite(originalComposite);
        }
        paintGlow(copy, shape, 0.15f, 0.10f, 0.62f, new Color(255, 255, 255, 120));
        paintGlow(copy, shape, 0.90f, 0.20f, 0.68f, new Color(180, 215, 255, 70));
        copy.setPaint(new GradientPaint(0, 0, new Color(255, 255, 255, 88),
                getWidth(), getHeight(), new Color(255, 255, 255, 24)));
        copy.fill(shape);
        copy.dispose();
    }

    private void paintPlasma(Graphics2D g2, Shape shape) {
        Graphics2D copy = (Graphics2D) g2.create();
        copy.setClip(shape);
        paintGlow(copy, shape, 0.16f, 0.18f, 0.72f, new Color(255, 40, 220, 190));
        paintGlow(copy, shape, 0.86f, 0.24f, 0.66f, new Color(0, 220, 255, 170));
        paintGlow(copy, shape, 0.55f, 0.95f, 0.78f, new Color(115, 72, 255, 185));
        copy.setPaint(new GradientPaint(0, 0, new Color(255, 255, 255, 45),
                getWidth(), getHeight(), new Color(255, 255, 255, 0)));
        copy.fill(shape);
        copy.dispose();
    }

    private void paintGlow(Graphics2D g2, Shape shape, float x, float y, float radiusScale, Color color) {
        int width = getWidth();
        int height = getHeight();
        float radius = Math.max(width, height) * radiusScale;
        if (width <= 0 || height <= 0 || radius <= 0f || color == null) {
            return;
        }
        Paint oldPaint = g2.getPaint();
        Point2D center = new Point2D.Float(width * x, height * y);
        g2.setPaint(new RadialGradientPaint(center, radius,
                new float[]{0f, 1f},
                new Color[]{color, new Color(color.getRed(), color.getGreen(), color.getBlue(), 0)},
                CycleMethod.NO_CYCLE));
        g2.fill(shape);
        g2.setPaint(oldPaint);
    }

    private BufferedImage createBlurredBackdrop(int radius) {
        Container parent = getParent();
        int width = getWidth();
        int height = getHeight();
        if (parent == null || width <= 0 || height <= 0) {
            return null;
        }

        BufferedImage source = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D copy = source.createGraphics();
        copy.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        copy.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        copy.translate(-getX(), -getY());
        paintingBackdropSnapshot = true;
        try {
            parent.paint(copy);
        } finally {
            paintingBackdropSnapshot = false;
            copy.dispose();
        }
        return blurImage(source, radius);
    }

    private BufferedImage blurImage(BufferedImage image, int radius) {
        if (image == null || radius <= 0) {
            return image;
        }
        int size = radius * 2 + 1;
        float[] weights = new float[size];
        Arrays.fill(weights, 1f / size);

        BufferedImage horizontal = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        BufferedImage blurred = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        new ConvolveOp(new Kernel(size, 1, weights), ConvolveOp.EDGE_NO_OP, null).filter(image, horizontal);
        new ConvolveOp(new Kernel(1, size, weights), ConvolveOp.EDGE_NO_OP, null).filter(horizontal, blurred);
        return blurred;
    }
}
