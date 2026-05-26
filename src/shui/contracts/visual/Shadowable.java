package shui.contracts.visual;

import java.awt.Color;

/**
 * Interfaz que define el contrato para componentes con sombra configurable.
 *
 * <p>Permite configurar la sombra con distintos niveles de desenfoque,
 * color, y desplazamiento X/Y de forma independiente al resto de
 * propiedades visuales.</p>
 *
 * <h3>Ejemplo de uso:</h3>
 * <pre>
 *   Shadowable comp = new ShPanel();
 *   comp.setShadow(8, new Color(0, 0, 0, 60));
 *   comp.setShadow(10, new Color(0, 0, 0, 80), 2, 4);
 *   comp.setShadowEnabled(false);
 * </pre>
 */
public interface Shadowable {

    enum ShadowElevation {
        NONE,
        LOW,
        MEDIUM,
        HIGH,
        FLOATING
    }

    /**
     * Activa la sombra con el desenfoque y color especificados.
     * El desplazamiento queda en (0, 2) por defecto.
     *
     * @param blur  radio de desenfoque en píxeles
     * @param color color de la sombra (usar alpha para transparencia)
     */
    void setShadow(int blur, Color color);

    /**
     * Activa la sombra con desenfoque, color y desplazamiento personalizados.
     *
     * @param blur    radio de desenfoque en píxeles
     * @param color   color de la sombra
     * @param offsetX desplazamiento horizontal de la sombra
     * @param offsetY desplazamiento vertical de la sombra
     */
    void setShadow(int blur, Color color, int offsetX, int offsetY);

    /**
     * Activa o desactiva la sombra sin perder su configuración.
     *
     * @param enabled {@code true} para mostrar la sombra
     */
    void setShadowEnabled(boolean enabled);

    /**
     * Indica si la sombra está actualmente visible.
     *
     * @return {@code true} si la sombra está activa
     */
    boolean isShadowEnabled();

    /**
     * Devuelve el radio de desenfoque actual de la sombra.
     *
     * @return blur en píxeles
     */
    int getShadowBlur();

    /**
     * Devuelve el color actual de la sombra.
     *
     * @return color de la sombra
     */
    Color getShadowColor();

    /**
     * Devuelve el desplazamiento horizontal de la sombra.
     *
     * @return offset X en píxeles
     */
    int getShadowOffsetX();

    /**
     * Devuelve el desplazamiento vertical de la sombra.
     *
     * @return offset Y en píxeles
     */
    int getShadowOffsetY();

    void setShadowBlur(int blur);

    void setShadowColor(Color color);

    void setShadowOffset(int offsetX, int offsetY);

    void setShadowElevation(ShadowElevation elevation);

    ShadowElevation getShadowElevation();

    void setInnerShadowEnabled(boolean enabled);

    boolean isInnerShadowEnabled();

    void setInnerShadow(Color color, int blur);

    Color getInnerShadowColor();

    int getInnerShadowBlur();
}

