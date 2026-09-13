import com.ShScrolls.ShScrollBar;
import com.ShScrolls.ShScrollBarBeanInfo;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.JScrollBar;

public final class ShScrollBarTest {

    private ShScrollBarTest() {
    }

    public static void main(String[] args) {
        exposesCompactDefaults();
        adaptsPreferredSizeToOrientation();
        clampsInvalidMeasurements();
        publishesVisualPropertiesInBeanInfo();
    }

    private static void exposesCompactDefaults() {
        ShScrollBar bar = new ShScrollBar();

        assertEquals(JScrollBar.VERTICAL, bar.getOrientation(), "orientacion");
        assertEquals(new Color(120, 144, 156), bar.getThumbColor(), "color del pulgar");
        assertEquals(new Color(245, 245, 245), bar.getTrackColor(), "color de la pista");
        assertEquals(8, bar.getScrollBarSize(), "tamano");
        assertEquals(false, bar.isButtonsVisible(), "botones ocultos");
    }

    private static void adaptsPreferredSizeToOrientation() {
        ShScrollBar vertical = new ShScrollBar(JScrollBar.VERTICAL);
        ShScrollBar horizontal = new ShScrollBar(JScrollBar.HORIZONTAL);

        assertEquals(8, vertical.getPreferredSize().width, "ancho vertical");
        assertEquals(8, horizontal.getPreferredSize().height, "alto horizontal");

        vertical.setScrollBarSize(12);
        horizontal.setScrollBarSize(12);
        assertEquals(new Dimension(12, vertical.getPreferredSize().height),
                vertical.getPreferredSize(), "tamano vertical configurado");
        assertEquals(new Dimension(horizontal.getPreferredSize().width, 12),
                horizontal.getPreferredSize(), "tamano horizontal configurado");
    }

    private static void clampsInvalidMeasurements() {
        ShScrollBar bar = new ShScrollBar();
        bar.setScrollBarSize(0);
        bar.setMinimumThumbLength(0);
        bar.setThumbArc(-1);
        bar.setTrackArc(-1);
        bar.setThumbInset(-1);

        assertEquals(2, bar.getScrollBarSize(), "tamano minimo");
        assertEquals(4, bar.getMinimumThumbLength(), "longitud minima");
        assertEquals(0, bar.getThumbArc(), "arco del pulgar");
        assertEquals(0, bar.getTrackArc(), "arco de la pista");
        assertEquals(0, bar.getThumbInset(), "margen del pulgar");
    }

    private static void publishesVisualPropertiesInBeanInfo() {
        Set<String> preferred = Arrays.stream(
                new ShScrollBarBeanInfo().getPropertyDescriptors())
                .filter(PropertyDescriptor::isPreferred)
                .map(PropertyDescriptor::getName)
                .collect(Collectors.toSet());

        assertEquals(true, preferred.contains("thumbColor"), "thumbColor preferida");
        assertEquals(true, preferred.contains("trackColor"), "trackColor preferida");
        assertEquals(true, preferred.contains("scrollBarSize"), "scrollBarSize preferida");
        assertEquals(true, preferred.contains("buttonsVisible"), "buttonsVisible preferida");
    }

    private static void assertEquals(Object expected, Object actual, String property) {
        if (!expected.equals(actual)) {
            throw new AssertionError(property + ": esperado " + expected + ", obtenido " + actual);
        }
    }
}
