import com.ShLookups.ShLookup;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

public final class ShLookupTest {

    private record Cliente(int id, String nombre) {
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            storesTypedValueAndRefreshesText();
            delegatesSearchAndHonorsDisabledState();
            laysOutReadOnlyContentAndHeader();
            exposesDesignerPropertiesAndEvents();
        });
        System.out.println("ShLookupTest: OK");
    }

    private static void storesTypedValueAndRefreshesText() {
        ShLookup<Cliente> lookup = new ShLookup<>();
        Cliente cliente = new Cliente(7, "Ana");
        AtomicInteger changes = new AtomicInteger();
        lookup.addPropertyChangeListener("value", event -> {
            equal(cliente, event.getNewValue(), "valor del evento");
            equal("Ana", lookup.getText(), "texto actualizado antes del evento");
            changes.incrementAndGet();
        });
        lookup.setDisplayText(Cliente::nombre);
        lookup.setValue(cliente);
        if (lookup.getValue() != cliente) {
            throw new AssertionError("Debe conservar el objeto original");
        }
        equal(1, changes.get(), "evento de cambio");
        lookup.setDisplayText(value -> value.id() + " - " + value.nombre());
        equal("7 - Ana", lookup.getText(), "cambio de formateador");
        lookup.setText("Cliente seleccionado");
        equal(cliente, lookup.getValue(), "texto manual conserva T");
        lookup.setDisplayText(null);
        equal(cliente.toString(), lookup.getText(), "toString por defecto");
        lookup.setDisplayText(value -> null);
        equal("", lookup.getText(), "resultado null");

        ShLookup<Cliente> empty = new ShLookup<>();
        empty.setPlaceholder("Seleccione un cliente");
        empty.setDisplayText(Cliente::nombre);
        empty.setValue(cliente);
        empty.clear();
        equal(null, empty.getValue(), "limpiar valor");
        equal("", empty.getText(), "limpiar texto");
        JLabel label = (JLabel) field(empty).getComponent(0);
        equal("Seleccione un cliente", label.getText(), "placeholder visible");
        empty.setValue(null); // No debe enviar null a Cliente::nombre.
    }

    private static void delegatesSearchAndHonorsDisabledState() {
        ShLookup<Cliente> lookup = new ShLookup<>();
        Cliente cliente = new Cliente(2, "Luis");
        AtomicInteger searches = new AtomicInteger();
        AtomicInteger events = new AtomicInteger();
        lookup.setDisplayText(Cliente::nombre);
        lookup.setOnSearch(() -> {
            searches.incrementAndGet();
            lookup.setValue(cliente);
        });
        ActionListener listener = event -> {
            equal(lookup, event.getSource(), "origen del evento");
            equal("search", event.getActionCommand(), "comando");
            events.incrementAndGet();
        };
        lookup.addActionListener(listener);
        JButton button = (JButton) field(lookup).getComponent(1);
        button.doClick(0);
        equal("Luis", lookup.getText(), "resultado de busqueda");
        // Acciones usadas por Swing para activar el boton con el teclado.
        button.getActionMap().get("pressed").actionPerformed(new ActionEvent(button, 0, ""));
        button.getActionMap().get("released").actionPerformed(new ActionEvent(button, 0, ""));
        equal(2, searches.get(), "clic y teclado");
        equal(2, events.get(), "eventos");
        lookup.setEnabled(false);
        button.doClick(0);
        lookup.doSearch();
        equal(2, searches.get(), "busqueda deshabilitada");
        equal(false, button.isEnabled(), "boton deshabilitado");
        lookup.setEnabled(true);
        lookup.removeActionListener(listener);
        lookup.setOnSearch(null);
        lookup.doSearch();
        equal(2, events.get(), "listener removido");
        equal(cliente, lookup.getValue(), "busqueda sin callback conserva valor");
    }

    private static void laysOutReadOnlyContentAndHeader() {
        ShLookup<String> lookup = new ShLookup<>();
        lookup.setHeaderText("Cliente");
        lookup.setValue("Ana");
        Font headerFont = new Font("Dialog", Font.BOLD, 18);
        Font contentFont = new Font("Dialog", Font.PLAIN, 14);
        lookup.setHeaderFont(headerFont);
        lookup.setContentFont(contentFont);
        BorderLayout layout = (BorderLayout) lookup.getLayout();
        for (ShLookup.HeaderPosition position : ShLookup.HeaderPosition.values()) {
            lookup.setHeaderPosition(position);
            String constraint = switch (position) {
                case TOP_LEFT, TOP_CENTER, TOP_RIGHT -> BorderLayout.NORTH;
                case MIDDLE_LEFT -> BorderLayout.WEST;
                case MIDDLE_RIGHT -> BorderLayout.EAST;
                default -> BorderLayout.SOUTH;
            };
            JLabel header = (JLabel) layout.getLayoutComponent(constraint);
            equal("Cliente", header.getText(), "header " + position);
            equal(headerFont, header.getFont(), "fuente de header");
            lookup.setSize(300, 90);
            layoutTree(lookup);
            BufferedImage image = new BufferedImage(300, 90, BufferedImage.TYPE_INT_ARGB);
            var graphics = image.createGraphics();
            try {
                lookup.paint(graphics);
            } finally {
                graphics.dispose();
            }
        }
        equal(contentFont, field(lookup).getComponent(0).getFont(), "fuente de contenido");
        assertReadOnly(lookup);
        lookup.setHeaderVisible(false);
        equal(1, lookup.getComponentCount(), "header oculto");
        lookup.setHeaderPosition(null);
        equal(ShLookup.HeaderPosition.TOP_LEFT, lookup.getHeaderPosition(), "posicion null");
        lookup.setContentFont(null);
        equal(contentFont, lookup.getContentFont(), "fuente null conserva valor");
    }

    private static void exposesDesignerPropertiesAndEvents() {
        try {
            var info = Introspector.getBeanInfo(ShLookup.class);
            for (String name : new String[]{"text", "headerFont", "contentFont", "inputBarColor",
                "required", "autoValidate", "showValidationState"}) {
                PropertyDescriptor descriptor = Arrays.stream(info.getPropertyDescriptors())
                        .filter(property -> property.getName().equals(name)).findFirst().orElseThrow();
                equal(true, descriptor.isPreferred(), "propiedad preferida " + name);
                if (descriptor.getWriteMethod() == null) {
                    throw new AssertionError("Propiedad sin setter: " + name);
                }
            }
            equal(true, Arrays.stream(info.getEventSetDescriptors())
                    .anyMatch(event -> event.getName().equals("action")), "evento de paleta");
        } catch (java.beans.IntrospectionException ex) {
            throw new AssertionError(ex);
        }
    }

    private static Container field(ShLookup<?> lookup) {
        return (Container) ((BorderLayout) lookup.getLayout()).getLayoutComponent(BorderLayout.CENTER);
    }

    private static void assertReadOnly(Container container) {
        for (Component child : container.getComponents()) {
            if (child instanceof JTextComponent) {
                throw new AssertionError("No debe contener editores de texto");
            }
            if (child instanceof Container nested) {
                assertReadOnly(nested);
            }
        }
    }

    private static void layoutTree(Container container) {
        container.doLayout();
        for (Component child : container.getComponents()) {
            if (child instanceof Container nested) {
                layoutTree(nested);
            }
        }
    }

    private static void equal(Object expected, Object actual, String description) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError(description + ": esperado " + expected + ", obtenido " + actual);
        }
    }
}
