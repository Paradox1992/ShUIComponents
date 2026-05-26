package shui.contracts.visual;

/**
 * Interfaz que define el contrato para componentes con forma de esquinas
 * redondeadas configurables de forma independiente.
 *
 * <p>
 * Permite redondear cada esquina por separado o en grupo, facilitando la
 * creación de formas como pestañas, tarjetas adosadas, etc.</p>
 *
 * <h3>Ejemplo de uso:</h3>
 * <pre>
 *   Shapeable comp = new ShPanel();
 *   comp.setCornerRadius(12);
 *   comp.setAllCornersRounded(true);
 *
 *   // Solo esquinas superiores redondeadas (efecto pestaña)
 *   comp.setTopCornersRounded(true);
 *   comp.setBottomCornersRounded(false);
 * </pre>
 */
public interface Shapeable {

    /**
     * Establece el radio de redondeo para las esquinas activas.
     *
     * @param radius radio en píxeles; se recorta al máximo posible según el
     * tamaño del componente
     */
    void setCornerRadius(int radius);

    /**
     * Devuelve el radio de redondeo actual.
     *
     * @return radio en píxeles
     */
    int getCornerRadius();

    // ── Control por esquina individual ───────────────────────────────────────
    void setTopLeftRounded(boolean rounded);

    void setTopRightRounded(boolean rounded);

    void setBottomLeftRounded(boolean rounded);

    void setBottomRightRounded(boolean rounded);

    boolean isTopLeftRounded();

    boolean isTopRightRounded();

    boolean isBottomLeftRounded();

    boolean isBottomRightRounded();

    // ── Control por grupos ───────────────────────────────────────────────────
    /**
     * Activa o desactiva el redondeo en las cuatro esquinas simultáneamente.
     *
     * @param rounded {@code true} para redondear todas las esquinas
     */
    void setAllCornersRounded(boolean rounded);

    /**
     * Activa o desactiva el redondeo en las esquinas superiores (izquierda y
     * derecha).
     *
     * @param rounded {@code true} para redondear las esquinas superiores
     */
    void setTopCornersRounded(boolean rounded);

    /**
     * Activa o desactiva el redondeo en las esquinas inferiores (izquierda y
     * derecha).
     *
     * @param rounded {@code true} para redondear las esquinas inferiores
     */
    void setBottomCornersRounded(boolean rounded);
}

