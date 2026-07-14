package com.shcomponentes.renderedModels;

import java.awt.Color;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Regla publica para pintar filas o celdas segun el valor de una columna.
 */
public final class ModelRender {

    public static final int ANY_COLUMN = -1;

    private final Object validator;
    private final Predicate<Object> matcher;
    private final Color background;
    private final Color foreground;
    private final int column;
    private final RenderTarget target;

    public ModelRender(Object validator, Color background, int column) {
        this(validator, null, background, null, column, RenderTarget.ROW);
    }

    public ModelRender(Object validator, Color background, Color foreground, int column) {
        this(validator, null, background, foreground, column, RenderTarget.ROW);
    }

    public ModelRender(Object validator, Color background, int column, RenderTarget target) {
        this(validator, null, background, null, column, target);
    }

    public ModelRender(Object validator, Color background, Color foreground, int column, RenderTarget target) {
        this(validator, null, background, foreground, column, target);
    }

    private ModelRender(Object validator, Predicate<Object> matcher, Color background,
            Color foreground, int column, RenderTarget target) {
        this.validator = validator;
        this.matcher = matcher;
        this.background = background != null ? background : Color.WHITE;
        this.foreground = foreground;
        this.column = column;
        this.target = target != null ? target : RenderTarget.ROW;
    }

    public static ModelRender row(Object validator, Color background, int column) {
        return new ModelRender(validator, background, column, RenderTarget.ROW);
    }

    public static ModelRender cell(Object validator, Color background, int column) {
        return new ModelRender(validator, background, column, RenderTarget.CELL);
    }

    public static ModelRender when(int column, Predicate<Object> matcher, Color background) {
        return when(column, matcher, background, null, RenderTarget.ROW);
    }

    public static ModelRender when(int column, Predicate<Object> matcher, Color background,
            Color foreground, RenderTarget target) {
        return new ModelRender(null, Objects.requireNonNull(matcher, "matcher"),
                background, foreground, column, target);
    }

    public ModelRender withForeground(Color foreground) {
        return new ModelRender(validator, matcher, background, foreground, column, target);
    }

    public ModelRender asRow() {
        return new ModelRender(validator, matcher, background, foreground, column, RenderTarget.ROW);
    }

    public ModelRender asCell() {
        return new ModelRender(validator, matcher, background, foreground, column, RenderTarget.CELL);
    }

    public boolean matches(Object value) {
        if (matcher != null) {
            return matcher.test(value);
        }
        if (Objects.equals(validator, value)) {
            return true;
        }
        return matchesText(validator, value);
    }

    public Object getValidator() {
        return validator;
    }

    public Color getColor() {
        return background;
    }

    public Color getBackground() {
        return background;
    }

    public Color getForeground() {
        return foreground;
    }

    public int getCol() {
        return column;
    }

    public int getColumn() {
        return column;
    }

    public RenderTarget getTarget() {
        return target;
    }

    private static boolean matchesText(Object expected, Object actual) {
        if (expected == null || actual == null) {
            return false;
        }
        String expectedText = normalize(expected);
        String actualText = normalize(actual);
        return expectedText.equalsIgnoreCase(actualText);
    }

    private static String normalize(Object value) {
        if (value instanceof Enum<?> enumValue) {
            return enumValue.name().trim();
        }
        return String.valueOf(value).trim();
    }
}
