package com.shcomponentes.renderedModels;

import java.awt.Color;

/**
 * Paleta publica para renderizadores de estado.
 */
public final class RenderColor {

    public static final Color GREEN_COLOR = new Color(52428);
    public static final Color RED_COLOR = new Color(16737894);
    public static final Color YELLOW_COLOR = new Color(255, 243, 205);
    public static final Color BLUE_COLOR = new Color(207, 226, 255);
    public static final Color ORANGE_COLOR = new Color(255, 229, 180);
    public static final Color GRAY_COLOR = new Color(233, 236, 239);

    public static final Color GREEN_TEXT = new Color(21, 87, 36);
    public static final Color RED_TEXT = new Color(114, 28, 36);
    public static final Color YELLOW_TEXT = new Color(102, 77, 3);
    public static final Color BLUE_TEXT = new Color(8, 66, 152);
    public static final Color ORANGE_TEXT = new Color(127, 68, 0);
    public static final Color GRAY_TEXT = new Color(73, 80, 87);

    private RenderColor() {
    }

    public static Color readableForeground(Color background) {
        if (background == null) {
            return Color.BLACK;
        }
        double luminance = (0.299 * background.getRed()
                + 0.587 * background.getGreen()
                + 0.114 * background.getBlue()) / 255;
        return luminance < 0.55 ? Color.WHITE : Color.BLACK;
    }

    public static Color statusForeground(Color color) {
        if (GREEN_COLOR.equals(color)) {
            return GREEN_TEXT;
        }
        if (RED_COLOR.equals(color)) {
            return RED_TEXT;
        }
        if (YELLOW_COLOR.equals(color)) {
            return YELLOW_TEXT;
        }
        if (BLUE_COLOR.equals(color)) {
            return BLUE_TEXT;
        }
        if (ORANGE_COLOR.equals(color)) {
            return ORANGE_TEXT;
        }
        if (GRAY_COLOR.equals(color)) {
            return GRAY_TEXT;
        }
        return readableForeground(color);
    }
}
