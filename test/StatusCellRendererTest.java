import com.ShTables.ShTable;
import com.shcomponentes.renderedModels.ModelRender;
import com.shcomponentes.renderedModels.RenderColor;
import com.shcomponentes.renderedModels.StatusCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;

public final class StatusCellRendererTest {

    private StatusCellRendererTest() {
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            paintsCellRuleAsBadge();
            paintsRowRuleAcrossTheRow();
            preservesSelectionColors();
            usesModelColumnAfterReordering();
            delegatesAlignmentToShTable();
        });
    }

    private static void paintsCellRuleAsBadge() {
        JTable table = table();
        StatusCellRenderer renderer = renderer();
        Component cell = renderer.getTableCellRendererComponent(
                table, "ACTIVO", false, false, 0, 1);
        cell.setBounds(0, 0, 200, 30);

        BufferedImage image = new BufferedImage(200, 30, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        try {
            cell.paint(graphics);
        } finally {
            graphics.dispose();
        }

        assertNotEquals(RenderColor.GREEN_COLOR, new Color(image.getRGB(198, 15), true),
                "La regla de celda no debe pintar toda la celda");
        assertContainsColor(image, RenderColor.GREEN_COLOR,
                "La etiqueta del estado no se pinto");
    }

    private static void paintsRowRuleAcrossTheRow() {
        JTable table = table();
        StatusCellRenderer renderer = new StatusCellRenderer()
                .addRender(ModelRender.row("ACTIVO", RenderColor.GREEN_COLOR, 1));

        Component firstColumn = renderer.getTableCellRendererComponent(
                table, "Caja 1", false, false, 0, 0);

        assertEquals(RenderColor.GREEN_COLOR, firstColumn.getBackground(),
                "La regla de fila debe aplicarse usando la columna de estado");
    }

    private static void preservesSelectionColors() {
        JTable table = table();
        table.setSelectionBackground(Color.MAGENTA);
        table.setSelectionForeground(Color.WHITE);

        Component selected = renderer().getTableCellRendererComponent(
                table, "ACTIVO", true, false, 0, 1);

        assertEquals(Color.MAGENTA, selected.getBackground(),
                "El estado no debe sobrescribir el fondo de seleccion");
        assertEquals(Color.WHITE, selected.getForeground(),
                "El estado no debe sobrescribir el texto de seleccion");
    }

    private static void usesModelColumnAfterReordering() {
        JTable table = table();
        table.moveColumn(1, 0);

        Component status = renderer().getTableCellRendererComponent(
                table, "ACTIVO", false, false, 0, 0);

        assertEquals(RenderColor.GREEN_TEXT, status.getForeground(),
                "La regla debe usar indices del modelo despues de reordenar columnas");
    }

    private static void delegatesAlignmentToShTable() {
        ShTable<Object[]> shTable = new ShTable<>();
        shTable.setTableHeaders("Nombre", "Estado");
        shTable.setCenterContentColumns(1);
        shTable.setRenderer(renderer());

        JTable table = shTable.getTable();
        TableCellRenderer installed = table.getDefaultRenderer(Object.class);
        JLabel component = (JLabel) installed.getTableCellRendererComponent(
                table, "Caja 1", false, false, 0, 0);
        int nameAlignment = component.getHorizontalAlignment();
        component = (JLabel) installed.getTableCellRendererComponent(
                table, "ACTIVO", false, false, 0, 1);
        int statusAlignment = component.getHorizontalAlignment();

        assertEquals(SwingConstants.LEFT, nameAlignment,
                "ShTable debe mantener alineadas a la izquierda las demas columnas");
        assertEquals(SwingConstants.CENTER, statusAlignment,
                "ShTable debe centrar la columna configurada aun con renderer personalizado");
    }

    private static StatusCellRenderer renderer() {
        return new StatusCellRenderer()
                .addRender(ModelRender.cell("ACTIVO", RenderColor.GREEN_COLOR, 1));
    }

    private static JTable table() {
        return new JTable(
                new Object[][]{{"Caja 1", "ACTIVO"}},
                new Object[]{"Nombre", "Estado"});
    }

    private static void assertContainsColor(BufferedImage image, Color expected, String message) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (image.getRGB(x, y) == expected.getRGB()) {
                    return;
                }
            }
        }
        throw new AssertionError(message);
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + ": esperado=" + expected + ", actual=" + actual);
        }
    }

    private static void assertNotEquals(Object unexpected, Object actual, String message) {
        if (unexpected.equals(actual)) {
            throw new AssertionError(message + ": valor=" + actual);
        }
    }
}
