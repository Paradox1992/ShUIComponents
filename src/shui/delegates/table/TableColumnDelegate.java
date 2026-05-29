package shui.delegates.table;

import shui.contracts.table.TableColumnSpec;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

/**
 * Coordina la declaracion de columnas con el modelo y el JTable.
 */
public class TableColumnDelegate<T> {

    private final JTable table;
    private final ShTableModel<T> model;
    private final TableDataDelegate dataDelegate;

    public TableColumnDelegate(JTable table, ShTableModel<T> model, TableDataDelegate dataDelegate) {
        this.table = table;
        this.model = model;
        this.dataDelegate = dataDelegate;
    }

    public void setTableHeaders(String... headers) {
        dataDelegate.setTableHeaders(headers);
        applyToModel();
    }

    public String[] getTableHeaders() {
        return dataDelegate.getTableHeaders();
    }

    public void setColumnSpecs(TableColumnSpec... specs) {
        dataDelegate.setColumnSpecs(specs);
        applyToModel();
    }

    public TableColumnSpec[] getColumnSpecs() {
        return dataDelegate.getColumnSpecs();
    }

    public void applyToModel() {
        model.setColumnSpecs(dataDelegate.getColumnSpecs());
        applyWidths();
    }

    private void applyWidths() {
        TableColumnSpec[] specs = dataDelegate.getColumnSpecs();
        int count = Math.min(specs.length, table.getColumnModel().getColumnCount());
        for (int i = 0; i < count; i++) {
            int width = specs[i].getWidth();
            if (width <= 0) {
                continue;
            }
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setMinWidth(width);
            column.setPreferredWidth(width);
            column.setMaxWidth(width);
            column.setResizable(true);
        }
    }
}
