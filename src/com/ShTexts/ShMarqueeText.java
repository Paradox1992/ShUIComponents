package com.ShTexts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 * Etiqueta Swing que puede mostrar texto estatico o animado de derecha a izquierda.
 */
public class ShMarqueeText extends JLabel {

    private static final int DEFAULT_SPEED = 45;
    private static final int DEFAULT_ANIMATION_DELAY = 16;

    private final Timer animationTimer;

    private int speed = DEFAULT_SPEED;
    private int animationDelay = DEFAULT_ANIMATION_DELAY;
    private int gap = 48;
    private boolean running = true;
    private MarqueeTextMode mode = MarqueeTextMode.ANIMATED;
    private boolean pauseOnHover = true;
    private boolean hovered;
    private boolean resetPending = true;
    private float offset;
    private long lastTickNanos;

    public ShMarqueeText() {
        super();
        super.setText("ShMarqueeText");
        setOpaque(false);
        setForeground(new Color(33, 37, 41));
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setHorizontalAlignment(SwingConstants.LEFT);
        setVerticalAlignment(SwingConstants.CENTER);
        setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        setPreferredSize(new Dimension(220, 32));

        animationTimer = new Timer(animationDelay, event -> advanceAnimation());
        animationTimer.setCoalesce(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent event) {
                hovered = true;
            }

            @Override
            public void mouseExited(MouseEvent event) {
                hovered = false;
                lastTickNanos = System.nanoTime();
            }
        });
    }

    @Override
    public void addNotify() {
        super.addNotify();
        resetAnimation();
        updateTimerState();
    }

    @Override
    public void removeNotify() {
        animationTimer.stop();
        super.removeNotify();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        if (mode == MarqueeTextMode.STATIC || !running) {
            super.paintComponent(graphics);
            return;
        }

        Graphics2D g2 = (Graphics2D) graphics.create();
        try {
            paintLabelBackground(g2);
            applyTextRenderingHints(g2);

            String currentText = getText();
            Insets insets = getInsets();
            int x = insets.left;
            int y = insets.top;
            int width = Math.max(0, getWidth() - insets.left - insets.right);
            int height = Math.max(0, getHeight() - insets.top - insets.bottom);
            if (width <= 0 || height <= 0 || currentText == null || currentText.isEmpty()) {
                return;
            }

            g2.clipRect(x, y, width, height);
            g2.setColor(isEnabled() ? getForeground() : getDisabledTextColor());
            g2.setFont(getFont());

            FontMetrics metrics = g2.getFontMetrics();
            int textWidth = metrics.stringWidth(currentText);
            if (resetPending) {
                offset = Math.min(textWidth, width);
                resetPending = false;
            }

            int baseline = resolveBaseline(metrics, y, height);
            float drawX = x + width - offset;
            g2.drawString(currentText, Math.round(drawX), baseline);
        } finally {
            g2.dispose();
        }
    }

    @Override
    public void setText(String text) {
        super.setText(text != null ? text : "");
        resetAnimation();
        revalidate();
    }

    /**
     * Reinicia el texto en el extremo derecho del componente.
     */
    public void resetAnimation() {
        offset = 0f;
        resetPending = true;
        lastTickNanos = System.nanoTime();
        repaint();
    }

    /**
     * Define la velocidad horizontal en pixeles por segundo.
     */
    public void setSpeed(int speed) {
        this.speed = Math.max(1, speed);
        lastTickNanos = System.nanoTime();
    }

    public int getSpeed() {
        return speed;
    }

    /**
     * Define cada cuantos milisegundos se actualiza la animacion.
     */
    public void setAnimationDelay(int animationDelay) {
        this.animationDelay = Math.max(5, animationDelay);
        animationTimer.setDelay(this.animationDelay);
        animationTimer.setInitialDelay(this.animationDelay);
        lastTickNanos = System.nanoTime();
    }

    public int getAnimationDelay() {
        return animationDelay;
    }

    public void setGap(int gap) {
        this.gap = Math.max(0, gap);
        resetAnimation();
    }

    public int getGap() {
        return gap;
    }

    public void setMode(MarqueeTextMode mode) {
        this.mode = mode != null ? mode : MarqueeTextMode.ANIMATED;
        resetAnimation();
        updateTimerState();
    }

    public MarqueeTextMode getMode() {
        return mode;
    }

    public void setRunning(boolean running) {
        this.running = running;
        resetAnimation();
        updateTimerState();
    }

    public boolean isRunning() {
        return running;
    }

    public void start() {
        setRunning(true);
    }

    public void stop() {
        setRunning(false);
    }

    public void setPauseOnHover(boolean pauseOnHover) {
        this.pauseOnHover = pauseOnHover;
        if (!pauseOnHover) {
            hovered = false;
            lastTickNanos = System.nanoTime();
        }
    }

    public boolean isPauseOnHover() {
        return pauseOnHover;
    }

    private void advanceAnimation() {
        if (mode == MarqueeTextMode.STATIC || !running || (pauseOnHover && hovered)) {
            lastTickNanos = System.nanoTime();
            return;
        }

        long now = System.nanoTime();
        if (lastTickNanos == 0L) {
            lastTickNanos = now;
            return;
        }

        float elapsedSeconds = (now - lastTickNanos) / 1_000_000_000f;
        lastTickNanos = now;
        offset += speed * elapsedSeconds;

        FontMetrics metrics = getFontMetrics(getFont());
        int period = Math.max(1, getAvailableWidth() + metrics.stringWidth(getText()) + gap);
        if (offset >= period) {
            offset %= period;
        }
        repaint();
    }

    private void updateTimerState() {
        if (mode == MarqueeTextMode.ANIMATED && running && isDisplayable()) {
            lastTickNanos = System.nanoTime();
            animationTimer.start();
        } else {
            animationTimer.stop();
        }
    }

    private int getAvailableWidth() {
        Insets insets = getInsets();
        return Math.max(0, getWidth() - insets.left - insets.right);
    }

    private int resolveBaseline(FontMetrics metrics, int y, int height) {
        return switch (getVerticalAlignment()) {
            case SwingConstants.TOP -> y + metrics.getAscent();
            case SwingConstants.BOTTOM -> y + height - metrics.getDescent();
            default -> y + ((height - metrics.getHeight()) / 2) + metrics.getAscent();
        };
    }

    private Color getDisabledTextColor() {
        Color color = getForeground();
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), 120);
    }

    private void paintLabelBackground(Graphics2D g2) {
        if (isOpaque()) {
            g2.setColor(getBackground());
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private void applyTextRenderingHints(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }
}
