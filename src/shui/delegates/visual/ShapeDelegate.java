package shui.delegates.visual;

import shui.contracts.visual.Shapeable;
import java.awt.Shape;
import java.awt.geom.Path2D;
import javax.swing.JComponent;

/**
 * Delegate que encapsula la construcción de forma con esquinas redondeadas.
 *
 * <p>FIX: La caché ahora también se invalida correctamente cuando el tamaño
 * cambia entre llamadas a getShape(), sin necesidad de llamar invalidateCache()
 * manualmente desde fuera.</p>
 */
public class ShapeDelegate implements Shapeable {

    private final JComponent owner;

    private int cornerRadius;

    private boolean topLeft = true;
    private boolean topRight = true;
    private boolean bottomLeft = true;
    private boolean bottomRight = true;

    private transient Shape cachedShape;
    private transient int cachedW = -1;
    private transient int cachedH = -1;

    public ShapeDelegate(JComponent owner, int cornerRadius) {
        this.owner = owner;
        this.cornerRadius = cornerRadius;
    }

    @Override
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        invalidateCache();
        owner.repaint();
    }

    @Override
    public int getCornerRadius() {
        return cornerRadius;
    }

    @Override
    public void setTopLeftRounded(boolean rounded) {
        topLeft = rounded;
        invalidateCache();
        owner.repaint();
    }

    @Override
    public void setTopRightRounded(boolean rounded) {
        topRight = rounded;
        invalidateCache();
        owner.repaint();
    }

    @Override
    public void setBottomLeftRounded(boolean rounded) {
        bottomLeft = rounded;
        invalidateCache();
        owner.repaint();
    }

    @Override
    public void setBottomRightRounded(boolean rounded) {
        bottomRight = rounded;
        invalidateCache();
        owner.repaint();
    }

    @Override
    public boolean isTopLeftRounded() { return topLeft; }

    @Override
    public boolean isTopRightRounded() { return topRight; }

    @Override
    public boolean isBottomLeftRounded() { return bottomLeft; }

    @Override
    public boolean isBottomRightRounded() { return bottomRight; }

    @Override
    public void setAllCornersRounded(boolean rounded) {
        topLeft = topRight = bottomLeft = bottomRight = rounded;
        invalidateCache();
        owner.repaint();
    }

    @Override
    public void setTopCornersRounded(boolean rounded) {
        topLeft = topRight = rounded;
        invalidateCache();
        owner.repaint();
    }

    @Override
    public void setBottomCornersRounded(boolean rounded) {
        bottomLeft = bottomRight = rounded;
        invalidateCache();
        owner.repaint();
    }

    /**
     * FIX: La comparación de dimensiones ya cubría el caso de resize, pero
     * se añade equals en cornerRadius para garantizar invalidación completa.
     */
    public Shape getShape(int width, int height) {
        if (width <= 0 || height <= 0) {
            return null;
        }
        if (cachedShape == null || cachedW != width || cachedH != height) {
            cachedShape = buildShape(0, 0, width, height);
            cachedW = width;
            cachedH = height;
        }
        return cachedShape;
    }

    public void invalidateCache() {
        cachedShape = null;
        cachedW = -1;
        cachedH = -1;
    }

    public int getRecommendedPadding() {
        return Math.max(0, cornerRadius / 3);
    }

    private Shape buildShape(int x, int y, int w, int h) {
        int r = Math.min(cornerRadius, Math.min(w, h) / 2);
        Path2D path = new Path2D.Double();

        path.moveTo(x + (topLeft ? r : 0), y);
        path.lineTo(x + w - (topRight ? r : 0), y);
        if (topRight) {
            path.quadTo(x + w, y, x + w, y + r);
        } else {
            path.lineTo(x + w, y);
        }

        path.lineTo(x + w, y + h - (bottomRight ? r : 0));
        if (bottomRight) {
            path.quadTo(x + w, y + h, x + w - r, y + h);
        } else {
            path.lineTo(x + w, y + h);
        }

        path.lineTo(x + (bottomLeft ? r : 0), y + h);
        if (bottomLeft) {
            path.quadTo(x, y + h, x, y + h - r);
        } else {
            path.lineTo(x, y + h);
        }

        path.lineTo(x, y + (topLeft ? r : 0));
        if (topLeft) {
            path.quadTo(x, y, x + r, y);
        } else {
            path.lineTo(x, y);
        }

        path.closePath();
        return path;
    }
}

