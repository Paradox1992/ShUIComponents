import com.ShContainers.ShPanel;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.beans.PropertyDescriptor;
import java.util.Arrays;

public final class ShPanelStylePaintingTest {

    private static final int WIDTH = 240;
    private static final int HEIGHT = 140;

    private ShPanelStylePaintingTest() {
    }

    public static void main(String[] args) {
        styledSurfacesAreOpaqueByDefault();
        translucencyCanBeEnabledExplicitly();
        styledPanelKeepsChildrenVisible();
        publishesOpaqueBackgroundProperty();
    }

    private static void styledSurfacesAreOpaqueByDefault() {
        for (ShPanel.ShPanelStyle style : ShPanel.ShPanelStyle.values()) {
            if (style == ShPanel.ShPanelStyle.DEFAULT) {
                continue;
            }

            ShPanel panel = createPanel(style);
            Color center = pixelAt(panel, WIDTH / 2, HEIGHT / 2);
            assertEquals(255, center.getAlpha(), style + " debe pintar una base opaca");
        }
    }

    private static void translucencyCanBeEnabledExplicitly() {
        ShPanel panel = createPanel(ShPanel.ShPanelStyle.LIQUID_GLASS);
        panel.setStyleBackgroundOpaque(false);

        Color center = pixelAt(panel, WIDTH / 2, HEIGHT / 2);
        assertTrue(center.getAlpha() < 255,
                "LIQUID_GLASS debe permitir translucidez explicita");
    }

    private static void styledPanelKeepsChildrenVisible() {
        ShPanel panel = createPanel(ShPanel.ShPanelStyle.LIQUID_GLASS);
        panel.setLayout(null);

        javax.swing.JPanel child = new javax.swing.JPanel();
        child.setBackground(Color.RED);
        child.setBounds(80, 40, 80, 60);
        panel.add(child);

        Color childPixel = pixelAt(panel, 120, 70);
        assertEquals(Color.RED, childPixel, "el estilo no debe cubrir los componentes hijos");
    }

    private static void publishesOpaqueBackgroundProperty() {
        boolean published = Arrays.stream(new com.ShContainers.ShPanelBeanInfo()
                .getPropertyDescriptors())
                .filter(PropertyDescriptor::isPreferred)
                .anyMatch(descriptor -> "styleBackgroundOpaque".equals(descriptor.getName()));

        assertTrue(published, "styleBackgroundOpaque debe estar disponible en NetBeans");
    }

    private static ShPanel createPanel(ShPanel.ShPanelStyle style) {
        ShPanel panel = new ShPanel();
        panel.setSize(WIDTH, HEIGHT);
        panel.setPanelStyle(style);
        return panel;
    }

    private static Color pixelAt(ShPanel panel, int x, int y) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D graphics = image.createGraphics();
        try {
            panel.paint(graphics);
        } finally {
            graphics.dispose();
        }
        return new Color(image.getRGB(x, y), true);
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + ": esperado " + expected + ", obtenido " + actual);
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
