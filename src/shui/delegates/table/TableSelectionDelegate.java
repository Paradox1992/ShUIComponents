package shui.delegates.table;

import javax.swing.JTable;

/**
 * Lectura de seleccion y valores visibles de la tabla.
 *
 * @param <T> tipo de dato
 */
public class TableSelectionDelegate<T> {

    private final JTable table;
    private final ShTableModel<T> model;

    public TableSelectionDelegate(JTable table, ShTableModel<T> model) {
        this.table = table;
        this.model = model;
    }

    public String getValue(int columnIndex) {
        Object value = getRawValue(columnIndex);
        return value != null ? value.toString() : null;
    }

    public Object getRawValue(int columnIndex) {
        int modelRow = getSelectedModelRow();
        if (modelRow < 0 || columnIndex < 0 || columnIndex >= model.getColumnCount()) {
            return null;
        }
        return model.getValueAt(modelRow, columnIndex);
    }

    public Object[] buildRow(int... indexes) {
        int modelRow = getSelectedModelRow();
        if (modelRow < 0 || indexes == null) {
            return null;
        }

        Object[] row = new Object[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            int columnIndex = indexes[i];
            row[i] = columnIndex >= 0 && columnIndex < model.getColumnCount()
                    ? model.getValueAt(modelRow, columnIndex)
                    : null;
        }
        return row;
    }

    public T getSelectedItem() {
        int modelRow = getSelectedModelRow();
        return modelRow >= 0 ? model.getRow(modelRow) : null;
    }

    public int getSelectedModelRow() {
        int viewRow = table.getSelectedRow();
        return viewRow >= 0 ? table.convertRowIndexToModel(viewRow) : -1;
    }

    public boolean isRowSelected() {
        return table.getSelectedRow() >= 0;
    }

    public String getValue_FR(int conlIndex) {
        if (model == null) {
            return null;
        }

        return model.getValueAt(conlIndex, conlIndex).toString();
    }

    public String getValue_LR(int conlIndex) {
        if (model == null) {
            return null;
        }

        return model.getValueAt(model.getRowCount() - 1, conlIndex).toString();
    }
}
