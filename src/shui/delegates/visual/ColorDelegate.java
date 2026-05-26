package shui.delegates.visual;

import shui.contracts.visual.Colorable;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Shape;
import javax.swing.JComponent;

/**
 * Delegate que encapsula el color de fondo y la opacidad (alpha) global para
 * componentes que implementan {@link Colorable}.
 *
 * <p>
 * FIX: backgroundColor nunca puede quedar null; se inicializa con un fallback
 * transparente si se pasa null al constructor.</p>
 */
public class ColorDelegate implements Colorable {

    private final JComponent owner;

    private Color backgroundColor;
    private float alpha = 1f;

    public ColorDelegate(JComponent owner, Color backgroundColor) {
        this.owner = owner;
        // FIX: evitar NPE si se pasa null
        this.backgroundColor = (backgroundColor != null) ? backgroundColor : new Color(0, 0, 0, 0);
    }

    @Override
    public void setBackgroundColor(Color color) {
        if (color == null) {
            return;
        }
        this.backgroundColor = color;
        owner.repaint();
    }

    @Override
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public void setAlpha(float alpha) {
        this.alpha = Math.max(0f, Math.min(1f, alpha));
        owner.repaint();
    }

    @Override
    public float getAlpha() {
        return alpha;
    }

    public Composite applyAlpha(Graphics2D g2) {
        Composite original = g2.getComposite();
        if (alpha < 1f) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        }
        return original;
    }

    public void paint(Graphics2D g2, Shape shape) {
        if (shape == null) {
            return;
        }
        g2.setColor(backgroundColor);
        g2.fill(shape);
    }
}

