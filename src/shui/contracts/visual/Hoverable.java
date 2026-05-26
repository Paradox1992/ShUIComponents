package shui.contracts.visual;

import java.awt.Color;

/**
 * Interfaz que define el contrato para componentes con efecto hover configurable.
 *
 * <p>Permite mostrar un color de superposición semitransparente cuando el cursor
 * del ratón entra en el área del componente, proporcionando retroalimentación visual
 * al usuario.</p>
 *
 * <h3>Ejemplo de uso:</h3>
 * <pre>
 *   Hoverable comp = new ShPanel();
 *   comp.setHoverEnabled(true);
 *   comp.setHoverColor(new Color(255, 255, 255, 40));
 * </pre>
 */
public interface Hoverable {

    /**
     * Activa o desactiva el efecto hover.
     *
     * <p>Al activarlo por primera vez se registran automáticamente los
     * {@code MouseListener} necesarios en el componente.</p>
     *
     * @param enabled {@code true} para activar el efecto hover
     */
    void setHoverEnabled(boolean enabled);

    /**
     * Indica si el efecto hover está activo.
     *
     * @return {@code true} si el hover está habilitado
     */
    boolean isHoverEnabled();

    /**
     * Establece el color de superposición que se muestra al hacer hover.
     *
     * <p>Se recomienda usar colores con canal alpha bajo (30-60) para
     * un efecto sutil.</p>
     *
     * @param color color de superposición; se recomienda color semitransparente
     */
    void setHoverColor(Color color);

    /**
     * Devuelve el color de superposición actual del hover.
     *
     * @return color de hover
     */
    Color getHoverColor();
}

