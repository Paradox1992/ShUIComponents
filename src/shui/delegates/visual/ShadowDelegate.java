package shui.delegates.visual;

import shui.contracts.visual.Shadowable;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;

/**
 * Delegate que encapsula sombra externa, elevacion e inner shadow.
 */
public class ShadowDelegate implements Shadowable {

    private final JComponent owner;

    private boolean enabled = false;
    private int blur = 8;
    private Color color = new Color(0, 0, 0, 60);
    private int offsetX = 0;
    private int offsetY = 2;
    private int cornerRadius = 10;
    private ShadowElevation elevation = ShadowElevation.NONE;
    private boolean innerShadowEnabled;
    private Color innerShadowColor = new Color(0, 0, 0, 45);
    private int innerShadowBlur = 6;

    public ShadowDelegate(JComponent owner) {
        this.owner = owner;
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = Math.max(0, radius);
    }

    @Override
    public void setShadow(int blur, Color color) {
        setShadow(blur, color, 0, 2);
    }

    @Override
    public void setShadow(int blur, Color color, int offsetX, int offsetY) {
        this.blur = Math.max(0, blur);
        if (color != null) {
            this.color = color;
        }
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.enabled = this.blur > 0;
        owner.repaint();
    }

    @Override
    public void setShadowEnabled(boolean enabled) {
        this.enabled = enabled;
        owner.repaint();
    }

    @Override
    public boolean isShadowEnabled() {
        return enabled;
    }

    @Override
    public int getShadowBlur() {
        return blur;
    }

    @Override
    public Color getShadowColor() {
        return color;
    }

    @Override
    public int getShadowOffsetX() {
        return offsetX;
    }

    @Override
    public int getShadowOffsetY() {
        return offsetY;
    }

    @Override
    public void setShadowBlur(int blur) {
        this.blur = Math.max(0, blur);
        owner.repaint();
    }

    @Override
    public void setShadowColor(Color color) {
        if (color == null) {
            return;
        }
        this.color = color;
        owner.repaint();
    }

    @Override
    public void setShadowOffset(int offsetX, int offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        owner.repaint();
    }

    @Override
    public void setShadowElevation(ShadowElevation elevation) {
        if (elevation == null) {
            return;
        }
        this.elevation = elevation;
        switch (elevation) {
            case NONE -> {
                enabled = false;
                blur = 0;
                offsetX = 0;
                offsetY = 0;
            }
            case LOW -> setShadow(4, new Color(0, 0, 0, 35), 0, 1);
            case MEDIUM -> setShadow(8, new Color(0, 0, 0, 55), 0, 2);
            case HIGH -> setShadow(14, new Color(0, 0, 0, 75), 0, 4);
            case FLOATING -> setShadow(24, new Color(0, 0, 0, 90), 0, 8);
        }
        owner.repaint();
    }

    @Override
    public ShadowElevation getShadowElevation() {
        return elevation;
    }

    @Override
    public void setInnerShadowEnabled(boolean enabled) {
        this.innerShadowEnabled = enabled;
        owner.repaint();
    }

    @Override
    public boolean isInnerShadowEnabled() {
        return innerShadowEnabled;
    }

    @Override
    public void setInnerShadow(Color color, int blur) {
        if (color != null) {
            this.innerShadowColor = color;
        }
        this.innerShadowBlur = Math.max(0, blur);
        this.innerShadowEnabled = this.innerShadowBlur > 0;
        owner.repaint();
    }

    @Override
    public Color getInnerShadowColor() {
        return innerShadowColor;
    }

    @Override
    public int getInnerShadowBlur() {
        return innerShadowBlur;
    }

    public void paint(Graphics2D g2, int x, int y, int width, int height) {
        if (!enabled || blur <= 0 || color == null) {
            return;
        }
        for (int i = blur; i > 0; i--) {
            float ratio = (float) i / blur;
            int opacity = (int) (color.getAlpha() * (1f - ratio) * 0.5f);
            g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity));
            int sx = x + i + offsetX;
            int sy = y + i + offsetY;
            int sw = width - i * 2;
            int sh = height - i * 2;
            if (sw > 0 && sh > 0) {
                g2.fill(new RoundRectangle2D.Double(sx, sy, sw, sh, cornerRadius, cornerRadius));
            }
        }
    }

    public void paintInner(Graphics2D g2, Shape shape) {
        if (!innerShadowEnabled || shape == null || innerShadowBlur <= 0 || innerShadowColor == null) {
            return;
        }
        Graphics2D copy = (Graphics2D) g2.create();
        copy.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        copy.clip(shape);
        for (int i = 1; i <= innerShadowBlur; i++) {
            float ratio = 1f - (i / (float) innerShadowBlur);
            int alpha = (int) (innerShadowColor.getAlpha() * ratio * 0.45f);
            copy.setColor(new Color(
                    innerShadowColor.getRed(),
                    innerShadowColor.getGreen(),
                    innerShadowColor.getBlue(),
                    alpha
            ));
            copy.setStroke(new BasicStroke(i * 2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            copy.draw(shape);
        }
        copy.dispose();
    }
}
