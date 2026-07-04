package shui.delegates.table;

import com.requestsupport.interfaces.RowMapper;
import shui.contracts.table.TableColumnSpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Modelo generico para ShTable.
 *
 * @param <T> tipo de item
 */
public class ShTableModel<T> extends AbstractTableModel {

    private static final long serialVersionUID = 6631589351068814042L;

    private final List<T> data = new ArrayList<>();
    private TableColumnSpec[] columnSpecs = new TableColumnSpec[0];
    private RowMapper<? super T> mapper;

    public void setColumnSpecs(TableColumnSpec[] columnSpecs) {
        this.columnSpecs = columnSpecs != null
                ? Arrays.copyOf(columnSpecs, columnSpecs.length)
                : new TableColumnSpec[0];
        fireTableStructureChanged();
    }

    public TableColumnSpec[] getColumnSpecs() {
        return Arrays.copyOf(columnSpecs, columnSpecs.length);
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnSpecs.length;
    }

    @Override
    public String getColumnName(int column) {
        return isColumnIndexValid(column) ? columnSpecs[column].getTitle() : "";
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= data.size() || columnIndex < 0) {
            return null;
        }
        Object[] row = mapper != null ? mapper.mapToRow(data.get(rowIndex)) : new Object[0];
        return row != null && columnIndex < row.length ? row[columnIndex] : null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return isColumnIndexValid(columnIndex) && columnSpecs[columnIndex].isEditable();
    }

    public void setData(List<T> data, RowMapper<? super T> mapper) {
        this.mapper = mapper;
        this.data.clear();
        if (data != null) {
            this.data.addAll(data);
        }
        fireTableDataChanged();
    }

    public void appendData(List<T> data, RowMapper<? super T> mapper) {
        if (mapper != null) {
            this.mapper = mapper;
        }
        if (data == null || data.isEmpty()) {
            return;
        }
        int start = this.data.size();
        this.data.addAll(data);
        fireTableRowsInserted(start, this.data.size() - 1);
    }

    public void addRow(T item) {
        int index = data.size();
        data.add(item);
        fireTableRowsInserted(index, index);
    }

    public void updateRow(int rowIndex, T item) {
        if (rowIndex < 0 || rowIndex >= data.size()) {
            return;
        }
        data.set(rowIndex, item);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void clear() {
        data.clear();
        fireTableDataChanged();
    }

    public T getRow(int rowIndex) {
        return rowIndex >= 0 && rowIndex < data.size() ? data.get(rowIndex) : null;
    }

    private boolean isColumnIndexValid(int columnIndex) {
        return columnIndex >= 0 && columnIndex < columnSpecs.length;
    }
}
