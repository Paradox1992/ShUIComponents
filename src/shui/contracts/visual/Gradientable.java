package shui.contracts.visual;

import java.awt.Color;

/**
 * Interfaz que define el contrato para componentes con fondo degradado
 * configurable.
 *
 * <p>
 * Cuando el degradado está activo, reemplaza al color de fondo plano definido
 * por {@link Colorable}. Soporta tres direcciones: horizontal, vertical y
 * diagonal.</p>
 *
 * <h3>Ejemplo de uso:</h3>
 * <pre>
 *   Gradientable comp = new ShPanel();
 *   comp.setGradient(Color.WHITE, Color.LIGHT_GRAY);
 *   comp.setGradient(Color.BLUE, Color.CYAN, GradientDirection.HORIZONTAL);
 *   comp.setGradientEnabled(false);
 * </pre>
 */
public interface Gradientable {

    /**
     * Direcciones disponibles para el degradado de fondo.
     */
    enum GradientDirection {
        /**
         * De izquierda a derecha.
         */
        HORIZONTAL,
        /**
         * De arriba a abajo (por defecto).
         */
        VERTICAL,
        /**
         * De esquina superior-izquierda a esquina inferior-derecha.
         */
        DIAGONAL_RIGHT,
        DIAGONAL_LEFT
    }

    /**
     * Activa el degradado vertical con los colores de inicio y fin
     * especificados.
     *
     * @param start color de inicio del degradado
     * @param end color de fin del degradado
     */
    void setGradient(Color start, Color end);

 

    /**
     * Activa el degradado con los colores y dirección especificados.
     *
     * @param start color de inicio
     * @param end color de fin
     * @param direction dirección del degradado
     */
    void setGradient(Color start, Color end, GradientDirection direction);

    /**
     * Activa o desactiva el degradado sin perder su configuración.
     *
     * @param enabled {@code true} para usar el degradado en lugar del color
     * plano
     */
    void setGradientEnabled(boolean enabled);

    /**
     * Indica si el degradado está actualmente activo.
     *
     * @return {@code true} si el degradado está activo
     */
    boolean isGradientEnabled();

    /**
     * Devuelve el color de inicio del degradado.
     *
     * @return color de inicio, o {@code null} si no ha sido configurado
     */
    Color getGradientStart();

    /**
     * Establece el color de inicio del degradado de forma independiente.
     *
     * <p>
     * FIX: Par JavaBean necesario para que el IDE reconozca
     * {@code gradientStart} como propiedad editable (OK/Cancel en el
     * chooser).</p>
     *
     * @param color color de inicio; {@code null} es ignorado
     */
    void setGradientStart(Color color);

    /**
     * Devuelve el color de fin del degradado.
     *
     * @return color de fin, o {@code null} si no ha sido configurado
     */
    Color getGradientEnd();

    /**
     * Establece el color de fin del degradado de forma independiente.
     *
     * <p>
     * FIX: Par JavaBean necesario para que el IDE reconozca {@code gradientEnd}
     * como propiedad editable (OK/Cancel en el chooser).</p>
     *
     * @param color color de fin; {@code null} es ignorado
     */
    void setGradientEnd(Color color);

    /**
     * Devuelve la dirección actual del degradado.
     *
     * @return dirección del degradado
     */
    GradientDirection getGradientDirection();

    void setGradientDirection(GradientDirection direction);
}

