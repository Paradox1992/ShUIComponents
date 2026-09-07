import com.ShButtons.ShButton;
import com.ShButtons.ShButtonBeanInfo;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import shui.contracts.button.Buttonable.HorizontalAlignment;
import shui.contracts.button.Buttonable.HorizontalTextPosition;
import shui.contracts.button.Buttonable.VerticalAlignment;
import shui.contracts.button.Buttonable.VerticalTextPosition;

public final class ShButtonTextLayoutTest {

    private ShButtonTextLayoutTest() {
    }

    public static void main(String[] args) {
        exposesDefaultTextLayout();
        changesTextAlignmentAndPosition();
        alignsMultilineText();
        publishesLayoutPropertiesInBeanInfo();
    }

    private static void exposesDefaultTextLayout() {
        ShButton button = new ShButton();

        assertEquals(HorizontalAlignment.CENTER, button.getHorizontalAlignment(), "alineacion horizontal");
        assertEquals(VerticalAlignment.CENTER, button.getVerticalAlignment(), "alineacion vertical");
        assertEquals(HorizontalTextPosition.RIGHT, button.getHorizontalTextPosition(), "posicion horizontal");
        assertEquals(VerticalTextPosition.CENTER, button.getVerticalTextPosition(), "posicion vertical");
    }

    private static void changesTextAlignmentAndPosition() {
        ShButton button = new ShButton();

        button.setHorizontalAlignment(HorizontalAlignment.LEFT);
        button.setVerticalAlignment(VerticalAlignment.TOP);
        button.setHorizontalTextPosition(HorizontalTextPosition.CENTER);
        button.setVerticalTextPosition(VerticalTextPosition.BOTTOM);

        assertEquals(HorizontalAlignment.LEFT, button.getHorizontalAlignment(), "alineacion horizontal");
        assertEquals(VerticalAlignment.TOP, button.getVerticalAlignment(), "alineacion vertical");
        assertEquals(HorizontalTextPosition.CENTER, button.getHorizontalTextPosition(), "posicion horizontal");
        assertEquals(VerticalTextPosition.BOTTOM, button.getVerticalTextPosition(), "posicion vertical");

        JLabel label = (JLabel) button.getComponent(0);
        assertEquals(SwingConstants.LEFT, label.getHorizontalAlignment(), "valor Swing horizontal");
        assertEquals(SwingConstants.TOP, label.getVerticalAlignment(), "valor Swing vertical");
        assertEquals(SwingConstants.CENTER, label.getHorizontalTextPosition(), "valor Swing del texto horizontal");
        assertEquals(SwingConstants.BOTTOM, label.getVerticalTextPosition(), "valor Swing del texto vertical");
    }

    private static void alignsMultilineText() {
        ShButton button = new ShButton();
        button.setText("Primera\nSegunda");
        button.setHorizontalAlignment(HorizontalAlignment.LEFT);

        JLabel label = (JLabel) button.getComponent(0);
        assertTrue(label.getText().contains("text-align:left"),
                "el texto multilinea no respeto la alineacion izquierda");

        button.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        assertTrue(label.getText().contains("text-align:right"),
                "el texto multilinea no respeto la alineacion derecha");
    }

    private static void publishesLayoutPropertiesInBeanInfo() {
        Map<String, PropertyDescriptor> descriptors = Arrays.stream(
                new ShButtonBeanInfo().getPropertyDescriptors())
                .collect(Collectors.toMap(PropertyDescriptor::getName, Function.identity()));
        Set<String> preferred = descriptors.values().stream()
                .filter(PropertyDescriptor::isPreferred)
                .map(PropertyDescriptor::getName)
                .collect(Collectors.toSet());

        assertTrue(preferred.contains("horizontalAlignment"), "horizontalAlignment no es preferida");
        assertTrue(preferred.contains("verticalAlignment"), "verticalAlignment no es preferida");
        assertTrue(preferred.contains("horizontalTextPosition"), "horizontalTextPosition no es preferida");
        assertTrue(preferred.contains("verticalTextPosition"), "verticalTextPosition no es preferida");
        assertTrue(descriptors.get("horizontalAlignment").getPropertyType().isEnum(),
                "horizontalAlignment no usa una enumeracion");
        assertTrue(descriptors.get("verticalAlignment").getPropertyType().isEnum(),
                "verticalAlignment no usa una enumeracion");
        assertTrue(descriptors.get("horizontalTextPosition").getPropertyType().isEnum(),
                "horizontalTextPosition no usa una enumeracion");
        assertTrue(descriptors.get("verticalTextPosition").getPropertyType().isEnum(),
                "verticalTextPosition no usa una enumeracion");
    }

    private static void assertEquals(Object expected, Object actual, String property) {
        if (!expected.equals(actual)) {
            throw new AssertionError(property + ": esperado " + expected + ", obtenido " + actual);
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
