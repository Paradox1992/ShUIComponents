package shui.contracts.visual;

import javax.swing.Icon;

/**
 * Interfaz que define el contrato para componentes con imagen de fondo
 * configurable.
 *
 * <p>
 * Usa {@link Icon} como tipo, compatible con {@code ImageIcon},
 * {@code SVGIcon}, y cualquier implementación personalizada.</p>
 *
 * <h3>Ejemplo de uso:</h3>
 * <pre>
 *   Imageable comp = new ShPanel();
 *   comp.setImage(new ImageIcon("/path/to/image.png"));
 *   comp.setImageEnabled(true);
 *   comp.setImageScale(ImageScale.FIT);
 * </pre>
 */
public interface Imageable {

    enum ImageScale {
        /**
         * Estira la imagen al tamaño exacto del componente.
         */
        FILL,
        /**
         * Mantiene proporción; puede dejar espacios vacíos.
         */
        FIT,
        /**
         * Centra la imagen sin escalar.
         */
        CENTER
    }

    void setImage(Icon image);

    Icon getImage();

    void setImageEnabled(boolean enabled);

    boolean isImageEnabled();

    void setImageScale(ImageScale scale);

    ImageScale getImageScale();
}

