package shui.delegates.visual;

import shui.contracts.visual.Stateable;
import shui.contracts.visual.VisualState;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EnumMap;
import java.util.Map;
import javax.swing.JComponent;

/**
 * Delegate para estado visual automatico y estados semanticos.
 */
public class StateDelegate implements Stateable {

    private final JComponent owner;
    private final Map<VisualState, Color> overlayColors = new EnumMap<>(VisualState.class);
    private final Map<VisualState, Color> backgroundColors = new EnumMap<>(VisualState.class);
    private final Map<VisualState, Color> borderColors = new EnumMap<>(VisualState.class);

    private VisualState baseState = VisualState.NONE;
    private boolean selected = false;
    private boolean hovered = false;
    private boolean pressed = false;
    private boolean focused = false;
    private boolean focusRingEnabled = false;
    private Color focusRingColor = new Color(255, 255, 255, 255);

    public StateDelegate(JComponent owner) {
        this.owner = owner;
        installDefaults();
        installListeners();
    }

    private void installDefaults() {
        overlayColors.put(VisualState.HOVERED, new Color(0, 0, 0, 18));
        overlayColors.put(VisualState.PRESSED, new Color(0, 0, 0, 24));
        overlayColors.put(VisualState.DISABLED, new Color(245, 245, 245, 120));
        overlayColors.put(VisualState.SELECTED, new Color(50, 120, 220, 32));
        overlayColors.put(VisualState.ERROR, new Color(220, 45, 45, 26));
        overlayColors.put(VisualState.WARNING, new Color(230, 170, 35, 30));
        overlayColors.put(VisualState.SUCCESS, new Color(35, 160, 90, 28));

        borderColors.put(VisualState.FOCUSED, focusRingColor);
        borderColors.put(VisualState.SELECTED, new Color(55, 120, 220));
        borderColors.put(VisualState.ERROR, new Color(210, 55, 55));
        borderColors.put(VisualState.WARNING, new Color(205, 145, 30));
        borderColors.put(VisualState.SUCCESS, new Color(35, 145, 85));
    }

    private void installListeners() {
        owner.addMouseListener(new MouseAdapter() {
          /*  @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                owner.repaint();
            }*/

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                pressed = false;
                owner.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                pressed = true;
                owner.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pressed = false;
                owner.repaint();
            }
        });
        owner.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                focused = true;
                owner.repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                focused = false;
                owner.repaint();
            }
        });
        owner.addPropertyChangeListener("enabled", evt -> owner.repaint());
    }

    @Override
    public void setVisualState(VisualState state) {
        this.baseState = state != null ? state : VisualState.NONE;
        owner.repaint();
    }

    @Override
    public VisualState getVisualState() {
        return resolveState();
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
        owner.repaint();
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setStateOverlayColor(VisualState state, Color color) {
        putColor(overlayColors, state, color);
    }

    @Override
    public Color getStateOverlayColor(VisualState state) {
        return overlayColors.get(state);
    }

    @Override
    public void setStateBackgroundColor(VisualState state, Color color) {
        putColor(backgroundColors, state, color);
    }

    @Override
    public Color getStateBackgroundColor(VisualState state) {
        return backgroundColors.get(state);
    }

    @Override
    public void setStateBorderColor(VisualState state, Color color) {
        putColor(borderColors, state, color);
    }

    @Override
    public Color getStateBorderColor(VisualState state) {
        return borderColors.get(state);
    }

    @Override
    public void setFocusRingEnabled(boolean enabled) {
        this.focusRingEnabled = enabled;
        owner.repaint();
    }

    @Override
    public boolean isFocusRingEnabled() {
        return focusRingEnabled;
    }

    @Override
    public void setFocusRingColor(Color color) {
        if (color == null) {
            return;
        }
        this.focusRingColor = color;
        borderColors.put(VisualState.FOCUSED, color);
        owner.repaint();
    }

    @Override
    public Color getFocusRingColor() {
        return focusRingColor;
    }

    public Color getResolvedBackgroundColor() {
        return backgroundColors.get(resolveState());
    }

    public Color getResolvedBorderColor() {
        if (baseState == VisualState.NONE) {
            return null;
        }
        VisualState state = resolveState();
        Color color = borderColors.get(state);
        if (color != null) {
            return color;
        }
        if (focused) {
            return borderColors.get(VisualState.FOCUSED);
        }
        return null;
    }

    public void paintOverlay(Graphics2D g2, Shape shape) {
        if (shape == null) {
            return;
        }
        Color color = overlayColors.get(resolveState());
        if (color == null) {
            return;
        }
        Graphics2D copy = (Graphics2D) g2.create();
        copy.setColor(color);
        copy.fill(shape);
        copy.dispose();
    }

    public void paintFocusRing(Graphics2D g2, Shape shape) {
        if (baseState == VisualState.NONE || !focusRingEnabled || !focused || shape == null || !owner.isEnabled()) {
            return;
        }
        Graphics2D copy = (Graphics2D) g2.create();
        copy.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        copy.setColor(focusRingColor);
        copy.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        copy.draw(shape);
        copy.dispose();
    }

    private VisualState resolveState() {
        if (baseState == VisualState.NONE) {
            return VisualState.NONE;
        }
        if (!owner.isEnabled()) {
            return VisualState.DISABLED;
        }
        if (baseState == VisualState.HOVERED) {
            return hovered ? VisualState.HOVERED : VisualState.NONE;
        }
        if (baseState == VisualState.PRESSED) {
            return pressed ? VisualState.PRESSED : VisualState.NONE;
        }
        if (baseState == VisualState.FOCUSED) {
            return focused ? VisualState.FOCUSED : VisualState.NONE;
        }
        if (baseState == VisualState.ERROR
                || baseState == VisualState.WARNING
                || baseState == VisualState.SUCCESS) {
            return baseState;
        }
        if (pressed) {
            return VisualState.PRESSED;
        }
        if (selected || baseState == VisualState.SELECTED) {
            return VisualState.SELECTED;
        }
        if (hovered) {
            return VisualState.HOVERED;
        }
        if (focused) {
            return VisualState.FOCUSED;
        }
        return baseState != null ? baseState : VisualState.NONE;
    }

    private void putColor(Map<VisualState, Color> colors, VisualState state, Color color) {
        if (state == null) {
            return;
        }
        if (color == null) {
            colors.remove(state);
        } else {
            colors.put(state, color);
        }
        owner.repaint();
    }
}
