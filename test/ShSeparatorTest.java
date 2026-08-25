import com.ShSeparators.SeparatorOrientation;
import com.ShSeparators.ShSeparator;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

public final class ShSeparatorTest {

    private ShSeparatorTest() {
    }

    public static void main(String[] args) {
        exposesSafeDefaults();
        adaptsDefaultSizeToOrientation();
        clampsInvalidMeasurements();
        paintsUsingInsetsAndThickness();
    }

    private static void exposesSafeDefaults() {
        ShSeparator separator = new ShSeparator();

        assertEquals(SeparatorOrientation.HORIZONTAL, separator.getOrientation(), "orientacion");
        assertEquals(new Color(224, 224, 224), separator.getSeparatorColor(), "color");
        assertEquals(1, separator.getThickness(), "grosor");
        assertEquals(true, separator.isRounded(), "extremos redondeados");
    }

    private static void adaptsDefaultSizeToOrientation() {
        ShSeparator separator = new ShSeparator();
        assertEquals(new Dimension(160, 8), separator.getPreferredSize(), "tamano horizontal");

        separator.setOrientation(SeparatorOrientation.VERTICAL);
        assertEquals(new Dimension(8, 160), separator.getPreferredSize(), "tamano vertical");
    }

    private static void clampsInvalidMeasurements() {
        ShSeparator separator = new ShSeparator();
        separator.setThickness(0);
        separator.setStartInset(-10);
        separator.setEndInset(-20);

        assertEquals(1, separator.getThickness(), "grosor minimo");
        assertEquals(0, separator.getStartInset(), "margen inicial minimo");
        assertEquals(0, separator.getEndInset(), "margen final minimo");
    }

    private static void paintsUsingInsetsAndThickness() {
        ShSeparator separator = new ShSeparator();
        separator.setSeparatorColor(Color.RED);
        separator.setThickness(2);
        separator.setStartInset(3);
        separator.setEndInset(4);
        separator.setRounded(false);
        separator.setSize(20, 8);

        BufferedImage image = new BufferedImage(20, 8, BufferedImage.TYPE_INT_ARGB);
        separator.paint(image.getGraphics());

        assertEquals(0, image.getRGB(2, 3), "pixel antes de la linea");
        assertEquals(Color.RED.getRGB(), image.getRGB(3, 3), "inicio de la linea");
        assertEquals(Color.RED.getRGB(), image.getRGB(15, 4), "final de la linea");
        assertEquals(0, image.getRGB(16, 4), "pixel despues de la linea");
    }

    private static void assertEquals(Object expected, Object actual, String property) {
        if (!expected.equals(actual)) {
            throw new AssertionError(property + ": esperado " + expected + ", obtenido " + actual);
        }
    }
}
