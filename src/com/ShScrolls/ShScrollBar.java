package com.ShScrolls;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * Barra de desplazamiento Shui compacta y configurable.
 *
 * <p>Oculta los botones por defecto y dibuja una pista y un pulgar
 * redondeados tanto en orientacion vertical como horizontal.</p>
 */
public class ShScrollBar extends JScrollBar {

    private static final long serialVersionUID = 1L;
    private static final Color DEFAULT_THUMB_COLOR = new Color(120, 144, 156);
    private static final Color DEFAULT_TRACK_COLOR = new Color(245, 245, 245);

    private Color thumbColor = DEFAULT_THUMB_COLOR;
    private Color trackColor = DEFAULT_TRACK_COLOR;
    private int scrollBarSize = 8;
    private int minimumThumbLength = 24;
    private int thumbArc = 8;
    private int trackArc;
    private int thumbInset = 1;
    private boolean buttonsVisible;

    public ShScrollBar() {
        this(VERTICAL);
    }

    public ShScrollBar(int orientation) {
        super(orientation);
        setOpaque(false);
        setUnitIncrement(16);
        setBlockIncrement(64);
        updateUI();
    }

    @Override
    public void updateUI() {
        setUI(new ShScrollBarUI());
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension preferred = super.getPreferredSize();
        return getOrientation() == VERTICAL
                ? new Dimension(scrollBarSize, Math.max(48, preferred.height))
                : new Dimension(Math.max(48, preferred.width), scrollBarSize);
    }

    public Color getThumbColor() {
        return thumbColor;
    }

    public void setThumbColor(Color thumbColor) {
        Color nextColor = thumbColor != null ? thumbColor : DEFAULT_THUMB_COLOR;
        Color oldColor = this.thumbColor;
        this.thumbColor = nextColor;
        firePropertyChange("thumbColor", oldColor, nextColor);
        repaint();
    }

    public Color getTrackColor() {
        return trackColor;
    }

    public void setTrackColor(Color trackColor) {
        Color nextColor = trackColor != null ? trackColor : DEFAULT_TRACK_COLOR;
        Color oldColor = this.trackColor;
        this.trackColor = nextColor;
        firePropertyChange("trackColor", oldColor, nextColor);
        repaint();
    }

    public int getScrollBarSize() {
        return scrollBarSize;
    }

    public void setScrollBarSize(int scrollBarSize) {
        int nextSize = Math.max(2, scrollBarSize);
        int oldSize = this.scrollBarSize;
        this.scrollBarSize = nextSize;
        firePropertyChange("scrollBarSize", oldSize, nextSize);
        revalidate();
        repaint();
    }

    public int getMinimumThumbLength() {
        return minimumThumbLength;
    }

    public void setMinimumThumbLength(int minimumThumbLength) {
        int nextLength = Math.max(4, minimumThumbLength);
        int oldLength = this.minimumThumbLength;
        this.minimumThumbLength = nextLength;
        firePropertyChange("minimumThumbLength", oldLength, nextLength);
        revalidate();
        repaint();
    }

    public int getThumbArc() {
        return thumbArc;
    }

    public void setThumbArc(int thumbArc) {
        int nextArc = Math.max(0, thumbArc);
        int oldArc = this.thumbArc;
        this.thumbArc = nextArc;
        firePropertyChange("thumbArc", oldArc, nextArc);
        repaint();
    }

    public int getTrackArc() {
        return trackArc;
    }

    public void setTrackArc(int trackArc) {
        int nextArc = Math.max(0, trackArc);
        int oldArc = this.trackArc;
        this.trackArc = nextArc;
        firePropertyChange("trackArc", oldArc, nextArc);
        repaint();
    }

    public int getThumbInset() {
        return thumbInset;
    }

    public void setThumbInset(int thumbInset) {
        int nextInset = Math.max(0, thumbInset);
        int oldInset = this.thumbInset;
        this.thumbInset = nextInset;
        firePropertyChange("thumbInset", oldInset, nextInset);
        revalidate();
        repaint();
    }

    public boolean isButtonsVisible() {
        return buttonsVisible;
    }

    public void setButtonsVisible(boolean buttonsVisible) {
        boolean oldVisible = this.buttonsVisible;
        if (oldVisible == buttonsVisible) {
            return;
        }
        this.buttonsVisible = buttonsVisible;
        firePropertyChange("buttonsVisible", oldVisible, buttonsVisible);
        updateUI();
        revalidate();
        repaint();
    }

    private static final class ShScrollBarUI extends BasicScrollBarUI {

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return shScrollBar().isButtonsVisible()
                    ? super.createDecreaseButton(orientation) : createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return shScrollBar().isButtonsVisible()
                    ? super.createIncreaseButton(orientation) : createZeroButton();
        }

        @Override
        protected void paintTrack(Graphics graphics, JComponent component, Rectangle bounds) {
            ShScrollBar bar = (ShScrollBar) component;
            if (bounds.isEmpty() || bar.getTrackColor().getAlpha() == 0) {
                return;
            }
            paintRoundedRectangle(graphics, bounds, bar.getTrackColor(), bar.getTrackArc());
        }

        @Override
        protected void paintThumb(Graphics graphics, JComponent component, Rectangle bounds) {
            ShScrollBar bar = (ShScrollBar) component;
            if (bounds.isEmpty() || !bar.isEnabled() || bar.getThumbColor().getAlpha() == 0) {
                return;
            }

            int inset = bar.getThumbInset();
            Rectangle paintedBounds = new Rectangle(
                    bounds.x + inset,
                    bounds.y + inset,
                    Math.max(0, bounds.width - (inset * 2)),
                    Math.max(0, bounds.height - (inset * 2))
            );
            if (!paintedBounds.isEmpty()) {
                paintRoundedRectangle(graphics, paintedBounds,
                        bar.getThumbColor(), bar.getThumbArc());
            }
        }

        @Override
        protected Dimension getMinimumThumbSize() {
            ShScrollBar bar = shScrollBar();
            int crossAxis = Math.max(2, bar.getScrollBarSize() - (bar.getThumbInset() * 2));
            return bar.getOrientation() == JScrollBar.VERTICAL
                    ? new Dimension(crossAxis, bar.getMinimumThumbLength())
                    : new Dimension(bar.getMinimumThumbLength(), crossAxis);
        }

        private ShScrollBar shScrollBar() {
            return (ShScrollBar) scrollbar;
        }

        private JButton createZeroButton() {
            JButton button = new JButton();
            Dimension zero = new Dimension(0, 0);
            button.setPreferredSize(zero);
            button.setMinimumSize(zero);
            button.setMaximumSize(zero);
            return button;
        }

        private void paintRoundedRectangle(
                Graphics graphics,
                Rectangle bounds,
                Color color,
                int arc
        ) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            try {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                if (arc > 0) {
                    g2.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, arc, arc);
                } else {
                    g2.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
                }
            } finally {
                g2.dispose();
            }
        }
    }
}
