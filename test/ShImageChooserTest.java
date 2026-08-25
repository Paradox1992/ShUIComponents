import com.ShImageChoosers.ShImageChooser;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public final class ShImageChooserTest {

    private ShImageChooserTest() {
    }

    public static void main(String[] args) throws IOException {
        displaysPlainBase64Image();
        displaysDataUrlImage();
        clearsImageForEmptyContent();
        rejectsInvalidImageContent();
        rejectsMalformedBase64();
        returnsSanitizedOptimizedBase64();
        preservesTransparencyAsPng();
        returnsNullWithoutImage();
        validatesOptimizationOptions();
    }

    private static void displaysPlainBase64Image() throws IOException {
        ShImageChooser chooser = new ShImageChooser();
        chooser.setImagenBase64(createPngBase64());

        ImageIcon icon = (ImageIcon) chooser.getImagen();
        assertEquals(2, icon.getIconWidth(), "ancho");
        assertEquals(1, icon.getIconHeight(), "alto");
        assertEquals(null, chooser.getSelectedFile(), "archivo seleccionado");
    }

    private static void displaysDataUrlImage() throws IOException {
        ShImageChooser chooser = new ShImageChooser();
        chooser.setImagenBase64("data:image/png;base64," + createPngBase64());

        ImageIcon icon = (ImageIcon) chooser.getImagen();
        assertEquals(2, icon.getIconWidth(), "ancho de URL de datos");
        assertEquals(1, icon.getIconHeight(), "alto de URL de datos");
    }

    private static void clearsImageForEmptyContent() throws IOException {
        ShImageChooser chooser = new ShImageChooser();
        chooser.setImagenBase64(createPngBase64());
        chooser.setImagenBase64("  ");

        assertEquals(null, chooser.getImagen(), "imagen vacia");
    }

    private static void rejectsInvalidImageContent() {
        ShImageChooser chooser = new ShImageChooser();

        try {
            chooser.setImagenBase64(Base64.getEncoder().encodeToString("no es una imagen".getBytes()));
            throw new AssertionError("Se esperaba IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Resultado esperado.
        }
    }

    private static void rejectsMalformedBase64() {
        ShImageChooser chooser = new ShImageChooser();

        try {
            chooser.setImagenBase64("contenido-no-base64");
            throw new AssertionError("Se esperaba IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Resultado esperado.
        }
    }

    private static void returnsSanitizedOptimizedBase64() throws IOException {
        ShImageChooser chooser = new ShImageChooser();
        BufferedImage source = new BufferedImage(8, 4, BufferedImage.TYPE_INT_RGB);
        chooser.setImagen(new ImageIcon(source));

        String base64 = chooser.getImagenBase64(4, 4, 0.85f);
        BufferedImage optimizedImage = ImageIO.read(new ByteArrayInputStream(
                Base64.getDecoder().decode(base64)));

        assertEquals(false, base64.startsWith("data:"), "Base64 sin prefijo");
        assertEquals(4, optimizedImage.getWidth(), "ancho optimizado");
        assertEquals(2, optimizedImage.getHeight(), "alto proporcional");
    }

    private static void preservesTransparencyAsPng() throws IOException {
        ShImageChooser chooser = new ShImageChooser();
        BufferedImage source = new BufferedImage(2, 1, BufferedImage.TYPE_INT_ARGB);
        source.setRGB(0, 0, 0x00FFFFFF);
        source.setRGB(1, 0, Color.BLUE.getRGB());
        chooser.setImagen(new ImageIcon(source));

        byte[] optimizedBytes = Base64.getDecoder().decode(chooser.getImagenBase64());
        BufferedImage optimizedImage = ImageIO.read(new ByteArrayInputStream(optimizedBytes));

        assertEquals((byte) 0x89, optimizedBytes[0], "firma PNG");
        assertEquals(0, optimizedImage.getRGB(0, 0) >>> 24, "transparencia");
    }

    private static void returnsNullWithoutImage() {
        ShImageChooser chooser = new ShImageChooser();

        assertEquals(null, chooser.getImagenBase64(), "imagen Base64 ausente");
    }

    private static void validatesOptimizationOptions() {
        ShImageChooser chooser = new ShImageChooser();

        try {
            chooser.getImagenBase64(0, 100, 0.85f);
            throw new AssertionError("Se esperaba IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Resultado esperado.
        }

        try {
            chooser.getImagenBase64(100, 100, 1.1f);
            throw new AssertionError("Se esperaba IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Resultado esperado.
        }
    }

    private static String createPngBase64() throws IOException {
        BufferedImage image = new BufferedImage(2, 1, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, Color.RED.getRGB());
        image.setRGB(1, 0, Color.BLUE.getRGB());

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(image, "png", output);
        return Base64.getEncoder().encodeToString(output.toByteArray());
    }

    private static void assertEquals(Object expected, Object actual, String property) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(property + ": esperado " + expected + ", obtenido " + actual);
        }
    }
}
