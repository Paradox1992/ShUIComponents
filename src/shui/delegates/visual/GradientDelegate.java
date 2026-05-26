package shui.delegates.visual;

import shui.contracts.visual.Gradientable;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import javax.swing.JComponent;

/**
 * Delegate que encapsula toda la lógica del degradado de fondo.
 *
 * <p>
 * FIX: setGradient ahora valida que los colores no sean null antes de habilitar
 * el degradado, evitando NPE en paint().</p>
 */
public class GradientDelegate implements Gradientable {

    private final JComponent owner;

    private boolean enabled = false;
    private Color start = Color.WHITE;
    private Color end = Color.LIGHT_GRAY;
    private GradientDirection direction = GradientDirection.VERTICAL;

    public GradientDelegate(JComponent owner) {
        this.owner = owner;
    }

    @Override
    public void setGradient(Color start, Color end) {
        // FIX: no habilitar si algún color es null
        if (start == null || end == null) {
            return;
        }
        this.start = start;
        this.end = end;
        this.enabled = true;
        owner.repaint();
    }

    @Override
    public void setGradient(Color start, Color end, GradientDirection direction) {
        if (direction != null) {
            this.direction = direction;
        }
        setGradient(start, end);
    }

    @Override
    public void setGradientEnabled(boolean enabled) {
        // FIX: solo habilitar si los colores ya están configurados
        if (enabled && (start == null || end == null)) {
            return;
        }
        this.enabled = enabled;
        owner.repaint();
    }

    @Override
    public boolean isGradientEnabled() {
        return enabled;
    }

    @Override
    public Color getGradientStart() {
        return start;
    }

    @Override
    public void setGradientStart(Color color) {
        if (color == null) {
            return;
        }
        this.start = color;
        owner.repaint();
    }

    @Override
    public Color getGradientEnd() {
        return end;
    }

    @Override
    public void setGradientEnd(Color color) {
        if (color == null) {
            return;
        }
        this.end = color;
        owner.repaint();
    }

    @Override
    public GradientDirection getGradientDirection() {
        return direction;
    }

    public void paint(Graphics2D g2, Shape shape, int width, int height) {
        if (!enabled || start == null || end == null || shape == null) {
            return;
        }
        g2.setPaint(buildPaint(width, height));
        g2.fill(shape);
    }

    private Paint buildPaint(int w, int h) {
        return switch (direction) {
            case HORIZONTAL ->
                new GradientPaint(0, 0, start, w, 0, end);
            case DIAGONAL_RIGHT ->
                new GradientPaint(0, 0, start, w, h, end);
            case DIAGONAL_LEFT ->
                new GradientPaint(w, 0, start, 0, h, end);
            default ->
                new GradientPaint(0, 0, start, 0, h, end);
        };
    }

    @Override
    public void setGradientDirection(GradientDirection direction) {
        if (direction == null) {
            return;
        }
        this.direction = direction;
        owner.repaint();
    }

  
}

