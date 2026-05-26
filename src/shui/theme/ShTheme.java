package shui.theme;

import shui.contracts.visual.Borderable.BorderStyle;
import shui.contracts.visual.Shadowable.ShadowElevation;
import shui.contracts.visual.VisualState;
import java.awt.Color;
import java.util.EnumMap;
import java.util.Map;

/**
 * Tema visual reutilizable para componentes Shui.
 */
public class ShTheme {

    private Color backgroundColor = new Color(255, 255, 255, 0);
    private Color borderColor = new Color(180, 180, 180);
    private boolean borderEnabled;
    private float borderWidth = 1f;
    private BorderStyle borderStyle = BorderStyle.SOLID;
    private Color shadowColor = new Color(0, 0, 0, 60);
    private boolean shadowEnabled;
    private ShadowElevation shadowElevation = ShadowElevation.MEDIUM;
    private Color hoverOverlayColor = new Color(255, 255, 255, 30);
    private Color focusRingColor = new Color(60, 130, 230, 170);
    private int cornerRadius = 10;

    private final Map<VisualState, Color> stateOverlayColors = new EnumMap<>(VisualState.class);
    private final Map<VisualState, Color> stateBackgroundColors = new EnumMap<>(VisualState.class);
    private final Map<VisualState, Color> stateBorderColors = new EnumMap<>(VisualState.class);

    public ShTheme() {
        stateOverlayColors.put(VisualState.HOVERED, new Color(0, 0, 0, 18));
        stateOverlayColors.put(VisualState.PRESSED, new Color(0, 0, 0, 24));
        stateOverlayColors.put(VisualState.DISABLED, new Color(245, 245, 245, 120));
        stateOverlayColors.put(VisualState.SELECTED, new Color(50, 120, 220, 32));
        stateOverlayColors.put(VisualState.ERROR, new Color(220, 45, 45, 26));
        stateOverlayColors.put(VisualState.WARNING, new Color(230, 170, 35, 30));
        stateOverlayColors.put(VisualState.SUCCESS, new Color(35, 160, 90, 28));

        stateBorderColors.put(VisualState.FOCUSED, focusRingColor);
        stateBorderColors.put(VisualState.ERROR, new Color(210, 55, 55));
        stateBorderColors.put(VisualState.WARNING, new Color(205, 145, 30));
        stateBorderColors.put(VisualState.SUCCESS, new Color(35, 145, 85));
        stateBorderColors.put(VisualState.SELECTED, new Color(55, 120, 220));
    }

    public static ShTheme light() {
        return new ShTheme();
    }

    public static ShTheme dark() {
        ShTheme theme = new ShTheme();
        theme.backgroundColor = new Color(35, 38, 44, 255);
        theme.borderColor = new Color(82, 88, 98);
        theme.hoverOverlayColor = new Color(255, 255, 255, 20);
        theme.focusRingColor = new Color(95, 155, 255, 180);
        theme.shadowColor = new Color(0, 0, 0, 120);
        theme.stateOverlayColors.put(VisualState.DISABLED, new Color(0, 0, 0, 90));
        theme.stateBorderColors.put(VisualState.FOCUSED, theme.focusRingColor);
        return theme;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public ShTheme setBackgroundColor(Color backgroundColor) {
        if (backgroundColor != null) {
            this.backgroundColor = backgroundColor;
        }
        return this;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public ShTheme setBorderColor(Color borderColor) {
        if (borderColor != null) {
            this.borderColor = borderColor;
        }
        return this;
    }

    public boolean isBorderEnabled() {
        return borderEnabled;
    }

    public ShTheme setBorderEnabled(boolean borderEnabled) {
        this.borderEnabled = borderEnabled;
        return this;
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public ShTheme setBorderWidth(float borderWidth) {
        this.borderWidth = Math.max(0f, borderWidth);
        return this;
    }

    public BorderStyle getBorderStyle() {
        return borderStyle;
    }

    public ShTheme setBorderStyle(BorderStyle borderStyle) {
        if (borderStyle != null) {
            this.borderStyle = borderStyle;
        }
        return this;
    }

    public Color getShadowColor() {
        return shadowColor;
    }

    public ShTheme setShadowColor(Color shadowColor) {
        if (shadowColor != null) {
            this.shadowColor = shadowColor;
        }
        return this;
    }

    public boolean isShadowEnabled() {
        return shadowEnabled;
    }

    public ShTheme setShadowEnabled(boolean shadowEnabled) {
        this.shadowEnabled = shadowEnabled;
        return this;
    }

    public ShadowElevation getShadowElevation() {
        return shadowElevation;
    }

    public ShTheme setShadowElevation(ShadowElevation shadowElevation) {
        if (shadowElevation != null) {
            this.shadowElevation = shadowElevation;
        }
        return this;
    }

    public Color getHoverOverlayColor() {
        return hoverOverlayColor;
    }

    public ShTheme setHoverOverlayColor(Color hoverOverlayColor) {
        if (hoverOverlayColor != null) {
            this.hoverOverlayColor = hoverOverlayColor;
        }
        return this;
    }

    public Color getFocusRingColor() {
        return focusRingColor;
    }

    public ShTheme setFocusRingColor(Color focusRingColor) {
        if (focusRingColor != null) {
            this.focusRingColor = focusRingColor;
            stateBorderColors.put(VisualState.FOCUSED, focusRingColor);
        }
        return this;
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public ShTheme setCornerRadius(int cornerRadius) {
        this.cornerRadius = Math.max(0, cornerRadius);
        return this;
    }

    public Color getStateOverlayColor(VisualState state) {
        return stateOverlayColors.get(state);
    }

    public ShTheme setStateOverlayColor(VisualState state, Color color) {
        putColor(stateOverlayColors, state, color);
        return this;
    }

    public Color getStateBackgroundColor(VisualState state) {
        return stateBackgroundColors.get(state);
    }

    public ShTheme setStateBackgroundColor(VisualState state, Color color) {
        putColor(stateBackgroundColors, state, color);
        return this;
    }

    public Color getStateBorderColor(VisualState state) {
        return stateBorderColors.get(state);
    }

    public ShTheme setStateBorderColor(VisualState state, Color color) {
        putColor(stateBorderColors, state, color);
        return this;
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
    }
}
