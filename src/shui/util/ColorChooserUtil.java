package shui.util;

import java.awt.Color;
import java.awt.Component;
import java.util.function.Consumer;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;

/**
 * Utilidad para abrir {@link JColorChooser} de forma segura desde el EDT.
 *
 * <h3>Problema que resuelve:</h3>
 * <p>Si se llama a {@code JColorChooser.showDialog()} directamente desde un
 * {@code ActionListener} que a su vez es invocado mientras el EDT está
 * procesando paint o eventos de ratón en cadena, el diálogo puede quedar
 * congelado sin mostrar los botones "Aceptar"/"Cancelar" correctamente.</p>
 *
 * <p>La solución es envolver la apertura del diálogo en
 * {@link SwingUtilities#invokeLater} para que se ejecute en el próximo
 * ciclo del EDT, con la pila de llamadas limpia.</p>
 *
 * <h3>Uso:</h3>
 * <pre>
 *   ColorChooserUtil.show(parentComponent, "Seleccionar color",
 *       Color.WHITE, selectedColor -&gt; {
 *           myComponent.setBackgroundColor(selectedColor);
 *       });
 * </pre>
 */
public final class ColorChooserUtil {

    private ColorChooserUtil() {}

    /**
     * Abre el diálogo de selección de color de forma segura.
     *
     * @param parent      componente padre para centrar el diálogo
     * @param title       título del diálogo
     * @param initial     color inicial seleccionado; puede ser {@code null}
     * @param onSelected  callback invocado con el color elegido (solo si el usuario acepta)
     */
    public static void show(Component parent, String title, Color initial, Consumer<Color> onSelected) {
        // FIX PRINCIPAL: invokeLater garantiza que el diálogo se abre con el
        // EDT libre, permitiendo que los botones "Aceptar"/"Cancelar" funcionen.
        SwingUtilities.invokeLater(() -> {
            Color chosen = JColorChooser.showDialog(
                    parent,
                    title,
                    initial != null ? initial : Color.WHITE
            );
            if (chosen != null && onSelected != null) {
                onSelected.accept(chosen);
            }
        });
    }

    /**
     * Abre el diálogo con soporte de vista previa en tiempo real.
     *
     * @param parent      componente padre
     * @param title       título del diálogo
     * @param initial     color inicial
     * @param onChange    callback invocado cada vez que cambia la selección
     * @param onSelected  callback invocado al confirmar con "Aceptar"
     */
    public static void showWithPreview(Component parent, String title, Color initial,
            Consumer<Color> onChange, Consumer<Color> onSelected) {
        SwingUtilities.invokeLater(() -> {
            JColorChooser chooser = new JColorChooser(initial != null ? initial : Color.WHITE);

            if (onChange != null) {
                chooser.getSelectionModel().addChangeListener(e -> {
                    onChange.accept(chooser.getColor());
                });
            }

            JDialog dialog = JColorChooser.createDialog(
                    parent,
                    title,
                    true,
                    chooser,
                    e -> { // OK
                        if (onSelected != null) {
                            onSelected.accept(chooser.getColor());
                        }
                    },
                    null  // Cancel
            );
            dialog.setVisible(true);
        });
    }
}

