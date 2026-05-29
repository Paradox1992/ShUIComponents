package shui.contracts.table;

/**
 * Define titulo, editabilidad y ancho de una columna.
 */
public class TableColumnSpec {

    private final String title;
    private final boolean editable;
    private final int width;

    public TableColumnSpec(String title) {
        this(title, false, 0);
    }

    public TableColumnSpec(String title, boolean editable, int width) {
        this.title = title != null ? title.trim() : "";
        this.editable = editable;
        this.width = Math.max(0, width);
    }

    public static TableColumnSpec parse(String value) {
        if (value == null || value.isBlank()) {
            return new TableColumnSpec("");
        }

        String[] parts = value.split("\\|", -1);
        String title = parts.length > 0 ? parts[0].trim() : "";
        boolean editable = parts.length > 1 && Boolean.parseBoolean(parts[1].trim());
        int width = 0;

        if (parts.length > 2) {
            try {
                width = Integer.parseInt(parts[2].trim());
            } catch (NumberFormatException ex) {
                width = 0;
            }
        }

        return new TableColumnSpec(title, editable, width);
    }

    public String getTitle() {
        return title;
    }

    public boolean isEditable() {
        return editable;
    }

    public int getWidth() {
        return width;
    }

    public String toDeclaration() {
        return title + "|" + editable + "|" + width;
    }
}
