package shui.delegates.visual;

import static shui.config.colors.BaseContainerColors.DEFAULT_BORDER_COLOR;
import shui.contracts.visual.Borderable;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import javax.swing.JComponent;

/**
 * Delegate que encapsula estado y pintado de borde.
 */
public class BorderDelegate implements Borderable {

    private final JComponent owner;

    private boolean enabled = false;
    private float width = 1f;
    private Color borderColor = DEFAULT_BORDER_COLOR;
    private BorderStyle style = BorderStyle.SOLID;
    private boolean topVisible = true;
    private boolean rightVisible = true;
    private boolean bottomVisible = true;
    private boolean leftVisible = true;
    private boolean gradientEnabled;
    private Color gradientStart = DEFAULT_BORDER_COLOR;
    private Color gradientEnd = DEFAULT_BORDER_COLOR;

    public BorderDelegate(JComponent owner) {
        this.owner = owner;
    }

    @Override
    public void setBorder(float width, Color color) {
        this.width = Math.max(0f, width);
        if (color != null) {
            this.borderColor = color;
        }
        this.enabled = true;
        owner.repaint();
    }

    @Override
    public void setBorderEnabled(boolean enabled) {
        this.enabled = enabled;
        owner.repaint();
    }

    @Override
    public boolean isBorderEnabled() {
        return enabled;
    }

    @Override
    public float getBorderWidth() {
        return width;
    }

    @Override
    public void setBorderColor(Color color) {
        if (color == null) {
            return;
        }
        this.borderColor = color;
        owner.repaint();
    }

    @Override
    public void setBorderWidth(float width) {
        this.width = Math.max(0f, width);
        owner.repaint();
    }

    @Override
    public Color getBorderColor() {
        return borderColor;
    }

    @Override
    public void setBorderStyle(BorderStyle style) {
        if (style == null) {
            return;
        }
        this.style = style;
        owner.repaint();
    }

    @Override
    public BorderStyle getBorderStyle() {
        return style;
    }

    @Override
    public void setBorderSides(boolean top, boolean right, boolean bottom, boolean left) {
        this.topVisible = top;
        this.rightVisible = right;
        this.bottomVisible = bottom;
        this.leftVisible = left;
        owner.repaint();
    }

    @Override
    public boolean isTopBorderVisible() {
        return topVisible;
    }

    @Override
    public boolean isRightBorderVisible() {
        return rightVisible;
    }

    @Override
    public boolean isBottomBorderVisible() {
        return bottomVisible;
    }

    @Override
    public boolean isLeftBorderVisible() {
        return leftVisible;
    }

    @Override
    public void setBorderGradient(Color start, Color end) {
        if (start == null || end == null) {
            return;
        }
        this.gradientStart = start;
        this.gradientEnd = end;
        this.gradientEnabled = true;
        this.enabled = true;
        owner.repaint();
    }

    @Override
    public void setBorderGradientEnabled(boolean enabled) {
        this.gradientEnabled = enabled;
        owner.repaint();
    }

    @Override
    public boolean isBorderGradientEnabled() {
        return gradientEnabled;
    }

    @Override
    public Color getBorderGradientStart() {
        return gradientStart;
    }

    @Override
    public void setBorderGradientStart(Color color) {
        if (color == null) {
            return;
        }
        this.gradientStart = color;
        this.gradientEnabled = true;
        this.enabled = true;
        owner.repaint();
    }

    @Override
    public Color getBorderGradientEnd() {
        return gradientEnd;
    }

    @Override
    public void setBorderGradientEnd(Color color) {
        if (color == null) {
            return;
        }
        this.gradientEnd = color;
        this.gradientEnabled = true;
        this.enabled = true;
        owner.repaint();
    }

    public void paint(Graphics2D g2, Shape shape) {
        paint(g2, shape, null);
    }

    public void paint(Graphics2D g2, Shape shape, Color overrideColor) {
        if (!enabled || shape == null || width <= 0f) {
            return;
        }
        Graphics2D copy = (Graphics2D) g2.create();
        copy.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        copy.setPaint(buildPaint(overrideColor));
        copy.setStroke(buildStroke());
        copy.clip(shape);
        if (allSidesVisible()) {
            copy.draw(shape);
        } else {
            paintSides(copy, shape.getBounds());
        }
        copy.dispose();
    }

    private Paint buildPaint(Color overrideColor) {
        if (overrideColor != null) {
            return overrideColor;
        }
        if (gradientEnabled) {
            return new GradientPaint(0, 0, gradientStart, owner.getWidth(), owner.getHeight(), gradientEnd);
        }
        return borderColor;
    }

    private BasicStroke buildStroke() {
        float strokeWidth = Math.max(1f, width * 2f);
        return switch (style) {
            case DASHED ->
                new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        10f, new float[]{strokeWidth * 3f, strokeWidth * 2f}, 0f);
            case DOTTED ->
                new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        10f, new float[]{1f, strokeWidth * 2f}, 0f);
            default ->
                new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        };
    }

    private boolean allSidesVisible() {
        return topVisible && rightVisible && bottomVisible && leftVisible;
    }

    private void paintSides(Graphics2D copy, Rectangle bounds) {
        int x = bounds.x;
        int y = bounds.y;
        int maxX = bounds.x + bounds.width;
        int maxY = bounds.y + bounds.height;
        if (topVisible) {
            copy.drawLine(x, y, maxX, y);
        }
        if (rightVisible) {
            copy.drawLine(maxX, y, maxX, maxY);
        }
        if (bottomVisible) {
            copy.drawLine(maxX, maxY, x, maxY);
        }
        if (leftVisible) {
            copy.drawLine(x, maxY, x, y);
        }
    }
}
