package shui.contracts.visual;

import java.awt.Color;

/**
 * Contrato para componentes que pueden exponer estados visuales.
 */
public interface Stateable {

    void setVisualState(VisualState state);

    VisualState getVisualState();

    void setSelected(boolean selected);

    boolean isSelected();

    void setStateOverlayColor(VisualState state, Color color);

    Color getStateOverlayColor(VisualState state);

    void setStateBackgroundColor(VisualState state, Color color);

    Color getStateBackgroundColor(VisualState state);

    void setStateBorderColor(VisualState state, Color color);

    Color getStateBorderColor(VisualState state);

    void setFocusRingEnabled(boolean enabled);

    boolean isFocusRingEnabled();

    void setFocusRingColor(Color color);

    Color getFocusRingColor();
}
