package shui.contracts.visual;

import java.awt.Color;

/**
 * Interfaz que define el contrato para componentes con borde configurable.
 *
 * <p>Permite activar/desactivar el borde, configurar su grosor y color
 * de forma independiente al resto de propiedades visuales del componente.</p>
 *
 * <h3>Ejemplo de uso:</h3>
 * <pre>
 *   Borderable comp = new ShPanel();
 *   comp.setBorder(1.5f, Color.GRAY);
 *   comp.setBorderEnabled(false);
 * </pre>
 */
public interface Borderable {

    enum BorderStyle {
        SOLID,
        DASHED,
        DOTTED
    }

    /**
     * Activa el borde con el grosor y color especificados.
     *
     * @param width grosor del borde en píxeles
     * @param color color del borde
     */
    void setBorder(float width, Color color);

    /**
     * Activa o desactiva la visibilidad del borde sin perder su configuración.
     *
     * @param enabled {@code true} para mostrar el borde
     */
    void setBorderEnabled(boolean enabled);

    /**
     * Indica si el borde está actualmente visible.
     *
     * @return {@code true} si el borde está activo
     */
    boolean isBorderEnabled();

    /**
     * Devuelve el grosor actual del borde.
     *
     * @return grosor en píxeles
     */
    float getBorderWidth();

    /**
     * Establece el grosor del borde de forma independiente.
     *
     * <p>FIX: Necesario para que el IDE reconozca {@code borderWidth}
     * como propiedad editable (getter + setter del mismo tipo).</p>
     *
     * @param width grosor en píxeles; valores negativos se tratan como 0
     */
    void setBorderWidth(float width);

    /**
     * Devuelve el color actual del borde.
     *
     * @return color del borde
     */
    Color getBorderColor();

    /**
     * Establece el color del borde de forma independiente.
     *
     * <p>FIX: Necesario para que el IDE reconozca {@code borderColor}
     * como propiedad editable y muestre el color chooser con OK/Cancel
     * en lugar del modo informativo (solo Close).</p>
     *
     * @param color color del borde; {@code null} es ignorado
     */
    void setBorderColor(Color color);

    void setBorderStyle(BorderStyle style);

    BorderStyle getBorderStyle();

    void setBorderSides(boolean top, boolean right, boolean bottom, boolean left);

    boolean isTopBorderVisible();

    boolean isRightBorderVisible();

    boolean isBottomBorderVisible();

    boolean isLeftBorderVisible();

    void setBorderGradient(Color start, Color end);

    void setBorderGradientEnabled(boolean enabled);

    boolean isBorderGradientEnabled();

    Color getBorderGradientStart();

    void setBorderGradientStart(Color color);

    Color getBorderGradientEnd();

    void setBorderGradientEnd(Color color);
}

