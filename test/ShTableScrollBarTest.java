import com.ShScrolls.ShScrollBar;
import com.ShTables.ShTable;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import shui.contracts.table.TableTheme;

public final class ShTableScrollBarTest {

    private ShTableScrollBarTest() {
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            installsBothOrientationsWithDefaults();
            preservesConfigurationAcrossStyleAndUIChanges();
            scrollsBothAxesAndHonorsPolicies();
            updatesRangesWhenDataChanges();
            try {
                exposesWorkingBeanPropertiesAndNormalizedEvents();
            } catch (Exception ex) {
                throw new AssertionError("Propiedades JavaBeans", ex);
            }
        });
    }

    private static void installsBothOrientationsWithDefaults() {
        ShTable<Object[]> table = new ShTable<>();
        ShScrollBar vertical = bar(table, true);
        ShScrollBar horizontal = bar(table, false);
        assertEquals(JScrollBar.VERTICAL, vertical.getOrientation(), "orientacion vertical");
        assertEquals(JScrollBar.HORIZONTAL, horizontal.getOrientation(), "orientacion horizontal");
        assertEquals(table.getTable(), table.getScroll().getViewport().getView(), "vista interna");
        for (ShScrollBar bar : List.of(vertical, horizontal)) {
            assertEquals(new Color(120, 144, 156), bar.getThumbColor(), "color predeterminado");
            assertEquals(new Color(245, 245, 245), bar.getTrackColor(), "pista predeterminada");
            assertEquals(8, bar.getScrollBarSize(), "grosor predeterminado");
            assertEquals(24, bar.getMinimumThumbLength(), "longitud predeterminada");
            assertEquals(8, bar.getThumbArc(), "arco predeterminado");
            assertEquals(0, bar.getTrackArc(), "arco de pista predeterminado");
            assertEquals(1, bar.getThumbInset(), "margen predeterminado");
            assertEquals(false, bar.isButtonsVisible(), "botones ocultos");
        }
        assertEquals(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                table.getVerticalScrollBarPolicy(), "politica vertical inicial");
        assertEquals(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED,
                table.getHorizontalScrollBarPolicy(), "politica horizontal inicial");
        assertEquals(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS,
                table.getTableAutoResizeMode(), "ajuste inicial conservado");
    }

    private static void preservesConfigurationAcrossStyleAndUIChanges() {
        ShTable<Object[]> table = populatedTable();
        ShScrollBar vertical = bar(table, true);
        ShScrollBar horizontal = bar(table, false);
        table.setScrollBarThumbColor(Color.MAGENTA);
        table.setScrollBarTrackColor(Color.CYAN);
        table.setScrollBarSize(14);
        table.setScrollBarMinimumThumbLength(32);
        table.setScrollBarThumbArc(0);
        table.setScrollBarTrackArc(0);
        table.setScrollBarThumbInset(2);
        table.setScrollBarButtonsVisible(true);
        Object verticalUI = vertical.getUI();
        Object horizontalUI = horizontal.getUI();
        for (TableTheme theme : TableTheme.values()) {
            table.setTableTheme(theme);
            table.setTableHeaders("Nombre", "Detalle");
            table.setTableBackground(Color.LIGHT_GRAY);
            table.setTableRowHeight(35);
            assertTrue(verticalUI == vertical.getUI(), "el estilo conserva la UI vertical");
            assertTrue(horizontalUI == horizontal.getUI(), "el estilo conserva la UI horizontal");
        }
        SwingUtilities.updateComponentTreeUI(table);
        assertTrue(vertical == table.getScroll().getVerticalScrollBar(), "misma barra vertical");
        assertTrue(horizontal == table.getScroll().getHorizontalScrollBar(), "misma barra horizontal");
        for (ShScrollBar bar : List.of(vertical, horizontal)) {
            assertEquals(Color.MAGENTA, bar.getThumbColor(), "conserva pulgar");
            assertEquals(Color.CYAN, bar.getTrackColor(), "conserva pista");
            assertEquals(14, bar.getScrollBarSize(), "conserva grosor");
            assertEquals(32, bar.getMinimumThumbLength(), "conserva longitud");
            assertEquals(0, bar.getThumbArc(), "conserva arco del pulgar");
            assertEquals(0, bar.getTrackArc(), "conserva arco de pista");
            assertEquals(2, bar.getThumbInset(), "conserva margen");
            assertEquals(true, bar.isButtonsVisible(), "conserva botones");
            bar.setButtonsVisible(false);
            bar.setSize(bar.getOrientation() == JScrollBar.VERTICAL ? 14 : 180,
                    bar.getOrientation() == JScrollBar.VERTICAL ? 180 : 14);
            bar.setValues(30, 20, 0, 100);
            bar.doLayout();
            BufferedImage image = new BufferedImage(bar.getWidth(), bar.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            var graphics = image.createGraphics();
            try {
                bar.paint(graphics);
            } finally {
                graphics.dispose();
            }
            assertTrue(containsColor(image, Color.MAGENTA), "UI pinta el pulgar configurado");
            assertTrue(containsColor(image, Color.CYAN), "UI pinta la pista configurada");
        }
        assertEquals(14, vertical.getPreferredSize().width, "ancho vertical actualizado");
        assertEquals(14, horizontal.getPreferredSize().height, "alto horizontal actualizado");
    }

    private static void scrollsBothAxesAndHonorsPolicies() {
        ShTable<Object[]> table = populatedTable();
        layout(table);
        JScrollPane scroll = table.getScroll();
        ShScrollBar vertical = bar(table, true);
        ShScrollBar horizontal = bar(table, false);
        assertTrue(vertical.isVisible() && horizontal.isVisible(), "ambas barras necesarias");
        vertical.setValue(90);
        horizontal.setValue(120);
        assertEquals(new Point(120, 90), scroll.getViewport().getViewPosition(),
                "las barras desplazan la vista");
        scroll.getViewport().setViewPosition(new Point(40, 60));
        assertEquals(40, horizontal.getValue(), "vista sincroniza barra horizontal");
        assertEquals(60, vertical.getValue(), "vista sincroniza barra vertical");
        scroll.dispatchEvent(new MouseWheelEvent(scroll, MouseWheelEvent.MOUSE_WHEEL,
                System.currentTimeMillis(), 0, 20, 20, 0, false,
                MouseWheelEvent.WHEEL_UNIT_SCROLL, 3, 1));
        assertTrue(scroll.getViewport().getViewPosition().y > 60, "rueda desplaza verticalmente");

        table.setScrollBarSize(18);
        layout(table);
        assertEquals(18, vertical.getWidth(), "layout respeta ancho");
        assertEquals(18, horizontal.getHeight(), "layout respeta alto");
        table.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        table.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        layout(table);
        assertTrue(!vertical.isVisible() && !horizontal.isVisible(), "politicas NEVER");
        table.clearTable();
        table.setTableAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        table.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        layout(table);
        assertTrue(vertical.isVisible() && horizontal.isVisible(), "politicas ALWAYS sin datos");
        table.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        table.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        layout(table);
        assertTrue(!vertical.isVisible() && !horizontal.isVisible(), "AS_NEEDED sin desbordamiento");
    }

    private static void updatesRangesWhenDataChanges() {
        ShTable<Object[]> table = populatedTable();
        layout(table);
        ShScrollBar vertical = bar(table, true);
        vertical.setValue(vertical.getMaximum());
        assertTrue(vertical.getValue() > 0, "hay rango vertical");
        table.setSearchText("fila 99");
        layout(table);
        assertEquals(1, table.getFilteredRowCount(), "filtro conserva funcionamiento");
        assertEquals(0, vertical.getValue(), "filtro ajusta rango y posicion");
        table.clearSearch();
        layout(table);
        assertTrue(vertical.getMaximum() > vertical.getVisibleAmount(), "datos restauran rango");
        table.clearTable();
        layout(table);
        assertEquals(0, vertical.getValue(), "limpieza ajusta posicion");
        assertEquals(0, table.getScroll().getViewport().getViewPosition().y, "vista reiniciada");
    }

    private static void exposesWorkingBeanPropertiesAndNormalizedEvents() throws Exception {
        ShTable<Object[]> table = new ShTable<>();
        Map<String, PropertyDescriptor> descriptors = Arrays.stream(
                Introspector.getBeanInfo(ShTable.class).getPropertyDescriptors())
                .collect(Collectors.toMap(PropertyDescriptor::getName, Function.identity()));
        Object[][] cases = {
            {"scrollBarThumbColor", "thumbColor", Color.RED, null, new Color(120, 144, 156)},
            {"scrollBarTrackColor", "trackColor", Color.BLUE, null, new Color(245, 245, 245)},
            {"scrollBarSize", "scrollBarSize", 12, -1, 2},
            {"scrollBarMinimumThumbLength", "minimumThumbLength", 40, -1, 4},
            {"scrollBarThumbArc", "thumbArc", 12, -1, 0},
            {"scrollBarTrackArc", "trackArc", 12, -1, 0},
            {"scrollBarThumbInset", "thumbInset", 3, -1, 0},
            {"scrollBarButtonsVisible", "buttonsVisible", true, false, false},
            {"verticalScrollBarPolicy", null, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED},
            {"horizontalScrollBarPolicy", null, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED},
            {"tableAutoResizeMode", null, JTable.AUTO_RESIZE_OFF,
                JTable.AUTO_RESIZE_ALL_COLUMNS, JTable.AUTO_RESIZE_ALL_COLUMNS}
        };
        for (Object[] values : cases) {
            String name = (String) values[0];
            PropertyDescriptor descriptor = descriptors.get(name);
            assertTrue(descriptor != null && descriptor.isPreferred() && !descriptor.isHidden()
                    && descriptor.getReadMethod() != null && descriptor.getWriteMethod() != null,
                    name + " disponible en el disenador");
            List<PropertyChangeEvent> events = new ArrayList<>();
            table.addPropertyChangeListener(name, events::add);
            Object initial = descriptor.getReadMethod().invoke(table);
            descriptor.getWriteMethod().invoke(table, values[2]);
            assertEquals(values[2], descriptor.getReadMethod().invoke(table), name + " configurable");
            assertEquals(initial, events.get(0).getOldValue(), name + " evento anterior");
            assertEquals(values[2], events.get(0).getNewValue(), name + " evento nuevo");
            assertBarProperty(table, values[1], values[2]);
            descriptor.getWriteMethod().invoke(table, values[3]);
            assertEquals(values[4], descriptor.getReadMethod().invoke(table), name + " normalizado");
            assertEquals(values[4], events.get(1).getNewValue(), name + " evento normalizado");
            assertBarProperty(table, values[1], values[4]);
            descriptor.getWriteMethod().invoke(table, values[3]);
            assertEquals(2, events.size(), name + " sin eventos duplicados");
        }
    }

    private static void assertBarProperty(ShTable<?> table, Object name, Object value) throws Exception {
        if (name != null) {
            PropertyDescriptor descriptor = new PropertyDescriptor((String) name, ShScrollBar.class);
            for (ShScrollBar bar : List.of(bar(table, true), bar(table, false))) {
                assertEquals(value, descriptor.getReadMethod().invoke(bar), name + " en ambas barras");
            }
        }
    }

    private static ShTable<Object[]> populatedTable() {
        ShTable<Object[]> table = new ShTable<>();
        table.setSearchBoxVisible(false);
        table.setTableHeaders("Nombre", "Detalle");
        table.setData(IntStream.range(0, 100)
                .mapToObj(i -> new Object[]{"fila " + i, "detalle " + i}).toList(), row -> row);
        table.setTableAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int index = 0; index < 2; index++) {
            table.getTable().getColumnModel().getColumn(index).setPreferredWidth(400);
        }
        return table;
    }

    private static void layout(ShTable<?> table) {
        table.setSize(320, 200);
        table.doLayout();
        table.getScroll().doLayout();
        table.getScroll().getViewport().doLayout();
        table.getTable().doLayout();
        table.getScroll().doLayout();
    }

    private static ShScrollBar bar(ShTable<?> table, boolean vertical) {
        JScrollBar bar = vertical ? table.getScroll().getVerticalScrollBar()
                : table.getScroll().getHorizontalScrollBar();
        assertTrue(bar instanceof ShScrollBar, "barra integrada ShScrollBar");
        return (ShScrollBar) bar;
    }

    private static boolean containsColor(BufferedImage image, Color color) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (image.getRGB(x, y) == color.getRGB()) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError(message + ": esperado " + expected + ", obtenido " + actual);
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
