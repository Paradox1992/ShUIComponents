import com.ShContainers.ShPanel;
import com.ShDateSelectors.ShDateSelector;
import com.ShImageChoosers.ShImageChooser;
import java.io.File;
import java.time.LocalDate;
import javax.swing.JPanel;

public final class ShPanelClearFormTest {

    private ShPanelClearFormTest() {
    }

    public static void main(String[] args) {
        clearsDateSelectorAndImageChooser();
    }

    private static void clearsDateSelectorAndImageChooser() {
        ShPanel form = new ShPanel();
        JPanel nestedPanel = new JPanel();
        ShDateSelector dateSelector = new ShDateSelector();
        ShImageChooser imageChooser = new ShImageChooser();

        dateSelector.setSelectedDate(LocalDate.of(2026, 9, 9));
        imageChooser.setSelectedFile(new File("imagen.png"));
        nestedPanel.add(dateSelector);
        nestedPanel.add(imageChooser);
        form.add(nestedPanel);

        form.clearForm();

        assertEquals(null, dateSelector.getSelectedDate(), "fecha seleccionada");
        assertEquals(null, imageChooser.getImagen(), "imagen seleccionada");
        assertEquals(null, imageChooser.getSelectedFile(), "archivo seleccionado");
    }

    private static void assertEquals(Object expected, Object actual, String property) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(property + ": esperado " + expected + ", obtenido " + actual);
        }
    }
}
