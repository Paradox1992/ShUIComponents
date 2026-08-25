package com.ShSeparators;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;

/**
 * Separador visual ligero, configurable y compatible con el editor de
 * propiedades de NetBeans.
 */
public class ShSeparator extends JComponent {

    private static final Color DEFAULT_SEPARATOR_COLOR = new Color(224, 224, 224);
    private static final Dimension DEFAULT_HORIZONTAL_SIZE = new Dimension(160, 8);
    private static final Dimension DEFAULT_VERTICAL_SIZE = new Dimension(8, 160);

    private SeparatorOrientation orientation = SeparatorOrientation.HORIZONTAL;
    private Color separatorColor = DEFAULT_SEPARATOR_COLOR;
    private int thickness = 1;
    private int startInset;
    private int endInset;
    private boolean rounded = true;

    public ShSeparator() {
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g2 = (Graphics2D) graphics.create();
        try {
            if (rounded) {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
            }
            g2.setColor(separatorColor);

            if (orientation == SeparatorOrientation.VERTICAL) {
                paintVerticalSeparator(g2);
            } else {
                paintHorizontalSeparator(g2);
            }
        } finally {
            g2.dispose();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet()) {
            return super.getPreferredSize();
        }
        Dimension defaultSize = orientation == SeparatorOrientation.VERTICAL
                ? DEFAULT_VERTICAL_SIZE : DEFAULT_HORIZONTAL_SIZE;
        return new Dimension(defaultSize);
    }

    @Override
    public Dimension getMinimumSize() {
        if (isMinimumSizeSet()) {
            return super.getMinimumSize();
        }
        int crossAxisSize = Math.max(1, thickness);
        return orientation == SeparatorOrientation.VERTICAL
                ? new Dimension(crossAxisSize, 1)
                : new Dimension(1, crossAxisSize);
    }

    public SeparatorOrientation getOrientation() {
        return orientation;
    }

    public void setOrientation(SeparatorOrientation orientation) {
        SeparatorOrientation newOrientation = orientation != null
                ? orientation : SeparatorOrientation.HORIZONTAL;
        SeparatorOrientation oldOrientation = this.orientation;
        if (oldOrientation == newOrientation) {
            return;
        }
        this.orientation = newOrientation;
        firePropertyChange("orientation", oldOrientation, newOrientation);
        revalidate();
        repaint();
    }

    public Color getSeparatorColor() {
        return separatorColor;
    }

    public void setSeparatorColor(Color separatorColor) {
        Color newColor = separatorColor != null ? separatorColor : DEFAULT_SEPARATOR_COLOR;
        Color oldColor = this.separatorColor;
        if (oldColor.equals(newColor)) {
            return;
        }
        this.separatorColor = newColor;
        firePropertyChange("separatorColor", oldColor, newColor);
        repaint();
    }

    public int getThickness() {
        return thickness;
    }

    public void setThickness(int thickness) {
        int newThickness = Math.max(1, thickness);
        int oldThickness = this.thickness;
        if (oldThickness == newThickness) {
            return;
        }
        this.thickness = newThickness;
        firePropertyChange("thickness", oldThickness, newThickness);
        revalidate();
        repaint();
    }

    public int getStartInset() {
        return startInset;
    }

    public void setStartInset(int startInset) {
        int newInset = Math.max(0, startInset);
        int oldInset = this.startInset;
        if (oldInset == newInset) {
            return;
        }
        this.startInset = newInset;
        firePropertyChange("startInset", oldInset, newInset);
        repaint();
    }

    public int getEndInset() {
        return endInset;
    }

    public void setEndInset(int endInset) {
        int newInset = Math.max(0, endInset);
        int oldInset = this.endInset;
        if (oldInset == newInset) {
            return;
        }
        this.endInset = newInset;
        firePropertyChange("endInset", oldInset, newInset);
        repaint();
    }

    public boolean isRounded() {
        return rounded;
    }

    public void setRounded(boolean rounded) {
        boolean oldRounded = this.rounded;
        if (oldRounded == rounded) {
            return;
        }
        this.rounded = rounded;
        firePropertyChange("rounded", oldRounded, rounded);
        repaint();
    }

    private void paintHorizontalSeparator(Graphics2D g2) {
        int lineWidth = Math.max(0, getWidth() - startInset - endInset);
        int lineThickness = Math.min(thickness, getHeight());
        if (lineWidth <= 0 || lineThickness <= 0) {
            return;
        }

        int y = (getHeight() - lineThickness) / 2;
        paintLine(g2, startInset, y, lineWidth, lineThickness);
    }

    private void paintVerticalSeparator(Graphics2D g2) {
        int lineHeight = Math.max(0, getHeight() - startInset - endInset);
        int lineThickness = Math.min(thickness, getWidth());
        if (lineHeight <= 0 || lineThickness <= 0) {
            return;
        }

        int x = (getWidth() - lineThickness) / 2;
        paintLine(g2, x, startInset, lineThickness, lineHeight);
    }

    private void paintLine(Graphics2D g2, int x, int y, int width, int height) {
        if (rounded && thickness > 1) {
            int arc = Math.min(thickness, Math.min(width, height));
            g2.fillRoundRect(x, y, width, height, arc, arc);
        } else {
            g2.fillRect(x, y, width, height);
        }
    }
}
