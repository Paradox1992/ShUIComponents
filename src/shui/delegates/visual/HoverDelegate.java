package shui.delegates.visual;

import shui.contracts.visual.Hoverable;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;

/**
 * Delegate que encapsula el efecto hover.
 *
 * <p>FIX: setHoverColor ignora valores null para evitar NPE en paint().</p>
 */
public class HoverDelegate implements Hoverable {

    private final JComponent owner;

    private boolean enabled = false;
    private boolean hovered = false;
    private Color hoverColor = new Color(255, 255, 255, 30);
    private boolean listenerRegistered = false;

    public HoverDelegate(JComponent owner) {
        this.owner = owner;
    }

    @Override
    public void setHoverEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled && !listenerRegistered) {
            registerMouseListener();
        }
        owner.repaint();
    }

    @Override
    public boolean isHoverEnabled() {
        return enabled;
    }

    @Override
    public void setHoverColor(Color color) {
        // FIX: ignorar null
        if (color == null) return;
        this.hoverColor = color;
        owner.repaint();
    }

    @Override
    public Color getHoverColor() {
        return hoverColor;
    }

    public boolean isHovered() {
        return hovered;
    }

    public void paint(Graphics2D g2, Shape shape) {
        if (!enabled || !hovered || shape == null) {
            return;
        }
        g2.setColor(hoverColor);
        g2.fill(shape);
    }

    private void registerMouseListener() {
        owner.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                owner.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                owner.repaint();
            }
        });
        listenerRegistered = true;
    }
}

