package shui.contracts.visual;

import java.awt.Color;

/**
 * Interfaz que define el contrato para componentes con color de fondo y
 * transparencia configurables.
 *
 * <p>
 * Gestiona el color de fondo principal y el nivel de opacidad (alpha) del
 * componente como capacidades independientes.</p>
 *
 * <h3>Ejemplo de uso:</h3>
 * <pre>
 *   Colorable comp = new ShPanel();
 *   comp.setBackgroundColor(Color.WHITE);
 *   comp.setAlpha(0.85f);
 * </pre>
 */
public interface Colorable {

    /**
     * Establece el color de fondo del componente.
     *
     * @param color color de fondo; no debe ser {@code null}
     */
    void setBackgroundColor(Color color);



    /**
     * Devuelve el color de fondo actual del componente.
     *
     * @return color de fondo
     */
    Color getBackgroundColor();

    /**
     * Establece la opacidad global del componente.
     *
     * <p>
     * El valor se recorta automáticamente al rango [0.0, 1.0].</p>
     *
     * @param alpha nivel de opacidad; 0.0 = totalmente transparente, 1.0 =
     * opaco
     */
    void setAlpha(float alpha);

    /**
     * Devuelve el nivel de opacidad actual.
     *
     * @return valor entre 0.0 y 1.0
     */
    float getAlpha();
}

