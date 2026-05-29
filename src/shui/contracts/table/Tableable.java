package shui.contracts.table;

import java.util.List;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Contrato funcional para tablas Shui.
 *
 * @param <T> tipo de item que alimenta la tabla
 */
public interface Tableable<T> {

    void setTableHeaders(String... headers);

    String[] getTableHeaders();

    void setColumnSpecs(TableColumnSpec... specs);

    TableColumnSpec[] getColumnSpecs();

    void setData(List<T> data, RowMapper<? super T> mapper);

    void setData(PagedTableData<T> pagedData, RowMapper<? super T> mapper);

    void setTableData(List<T> data, RowMapper<? super T> mapper, PaginationInfo paginationInfo);

    void appendTableData(List<T> data, RowMapper<? super T> mapper);

    void addRow(T item);

    void updateRow(int rowIndex, T item);

    void clearTable();

    int getRowCount();

    String getValue(int columnIndex);

    Object[] buildRow(int... indexes);

    boolean isRowSelected();

    void setRenderer(TableCellRenderer renderer);

    void setMenu(JDialog menu);

    JDialog getMenu();

    JScrollPane getScroll();

    JTable getTable();

    void setPaged(boolean paged);

    boolean isPaged();

    void setPageHandler(PageRequestHandler handler);

    void setTableTheme(TableTheme theme);

    TableTheme getTableTheme();

    void setSearchVisible(boolean visible);

    boolean isSearchVisible();

    void setSearchBoxVisible(boolean visible);

    boolean isSearchBoxVisible();

    void setSearchPlaceholder(String placeholder);

    String getSearchPlaceholder();

    void setSearchText(String text);

    String getSearchText();

    void setFilterColumn(int columnIndex);

    int getFilterColumn();
}
