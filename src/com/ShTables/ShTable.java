package com.ShTables;

import com.ShContainers.ShPanel;
import shui.contracts.table.PageRequestHandler;
import shui.contracts.table.PagedTableData;
import shui.contracts.table.PaginationInfo;
import shui.contracts.table.RowMapper;
import shui.contracts.table.TableColumnSpec;
import shui.contracts.table.TableTheme;
import shui.contracts.table.Tableable;
import shui.delegates.table.ShTableModel;
import shui.delegates.table.TableColumnDelegate;
import shui.delegates.table.TableDataDelegate;
import shui.delegates.table.TableInteractionDelegate;
import shui.delegates.table.TablePaginationDelegate;
import shui.delegates.table.TableSearchDelegate;
import shui.delegates.table.TableSelectionDelegate;
import shui.delegates.table.TableStyleDelegate;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

/**
 * Tabla Shui basada en ShPanel, con JTable interno y busqueda integrada.
 *
 * @param <T> tipo de item
 */
public class ShTable<T> extends ShPanel implements Tableable<T> {

    private final JTable table = new JTable();
    private final JScrollPane scrollPane = new JScrollPane(table);
    private final ShTableModel<T> model = new ShTableModel<>();
    private final TableDataDelegate dataDelegate = new TableDataDelegate();
    private final TablePaginationDelegate paginationDelegate = new TablePaginationDelegate();
    private final TableStyleDelegate styleDelegate = new TableStyleDelegate();
    private final TableRowSorter<ShTableModel<T>> rowSorter = new TableRowSorter<>(model);
    private final TableColumnDelegate<T> columnDelegate = new TableColumnDelegate<>(table, model, dataDelegate);
    private final TableInteractionDelegate interactionDelegate = new TableInteractionDelegate();
    private final TableSearchDelegate<T> searchDelegate = new TableSearchDelegate<>(model, rowSorter);
    private final TableSelectionDelegate<T> selectionDelegate = new TableSelectionDelegate<>(table, model);

    private TableCellRenderer customRenderer;

    public ShTable() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 6));
        setContentPadding(0);
        setBorderEnabled(false);
        setOpaque(false);

        table.setModel(model);
        table.setRowSorter(rowSorter);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        interactionDelegate.install(table);

        add(searchDelegate.getComponent(), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        styleDelegate.applyTheme(TableTheme.SHUI);
        refreshStyle();
        updatePaginationState(null);
    }

    @Override
    public void setTableHeaders(String... headers) {
        columnDelegate.setTableHeaders(headers);
        searchDelegate.refreshFilter();
        refreshStyle();
    }

    @Override
    public String[] getTableHeaders() {
        return columnDelegate.getTableHeaders();
    }

    @Override
    public void setColumnSpecs(TableColumnSpec... specs) {
        columnDelegate.setColumnSpecs(specs);
        searchDelegate.refreshFilter();
        refreshStyle();
    }

    @Override
    public TableColumnSpec[] getColumnSpecs() {
        return columnDelegate.getColumnSpecs();
    }

    @Override
    public void setData(List<T> data, RowMapper<? super T> mapper) {
        model.setData(data, mapper);
    }

    @Override
    public void setData(PagedTableData<T> pagedData, RowMapper<? super T> mapper) {
        if (pagedData == null) {
            clearTable();
            paginationDelegate.reset();
            return;
        }
        updatePaginationState(pagedData.getPaginationInfo());
        model.setData(pagedData.getData(), mapper);
    }

    @Override
    public void setTableData(List<T> data, RowMapper<? super T> mapper, PaginationInfo paginationInfo) {
        updatePaginationState(paginationInfo);
        model.setData(data, mapper);
    }

    @Override
    public void appendTableData(List<T> data, RowMapper<? super T> mapper) {
        model.appendData(data, mapper);
    }

    @Override
    public void addRow(T item) {
        model.addRow(item);
    }

    @Override
    public void updateRow(int rowIndex, T item) {
        model.updateRow(rowIndex, item);
    }

    @Override
    public void clearTable() {
        model.clear();
    }

    @Override
    public int getRowCount() {
        return model.getRowCount();
    }

    public int getFilteredRowCount() {
        return searchDelegate.getFilteredRowCount();
    }

    @Override
    public String getValue(int columnIndex) {
        return selectionDelegate.getValue(columnIndex);
    }

    public Object getRawValue(int columnIndex) {
        return selectionDelegate.getRawValue(columnIndex);
    }

    @Override
    public Object[] buildRow(int... indexes) {
        return selectionDelegate.buildRow(indexes);
    }

    public T getSelectedItem() {
        return selectionDelegate.getSelectedItem();
    }

    public int getSelectedModelRow() {
        return selectionDelegate.getSelectedModelRow();
    }

    @Override
    public boolean isRowSelected() {
        return selectionDelegate.isRowSelected();
    }

    @Override
    public void setRenderer(TableCellRenderer renderer) {
        customRenderer = renderer;
        if (renderer != null) {
            styleDelegate.setRenderer(table, renderer);
        } else {
            refreshStyle();
        }
    }

    public TableCellRenderer getRenderer() {
        return customRenderer;
    }

    @Override
    public void setMenu(JDialog menu) {
        interactionDelegate.setMenu(menu);
    }

    @Override
    public JDialog getMenu() {
        return interactionDelegate.getMenu();
    }

    @Override
    public JScrollPane getScroll() {
        return scrollPane;
    }

    @Override
    public JTable getTable() {
        return table;
    }

    @Override
    public void setPaged(boolean paged) {
        paginationDelegate.setPaged(paged);
    }

    @Override
    public boolean isPaged() {
        return paginationDelegate.isPaged();
    }

    @Override
    public void setPageHandler(PageRequestHandler handler) {
        paginationDelegate.setHandler(handler);
    }

    public void nextPage() {
        paginationDelegate.requestNext();
    }

    public void previousPage() {
        paginationDelegate.requestPrevious();
    }

    public void firstPage() {
        paginationDelegate.requestFirst();
    }

    public void lastPage() {
        paginationDelegate.requestLast();
    }

    public void setShowItemsCount(boolean showItemsCount) {
        paginationDelegate.setShowItemsCount(showItemsCount);
    }

    public boolean isShowItemsCount() {
        return paginationDelegate.isShowItemsCount();
    }

    public PaginationInfo getPaginationInfo() {
        return paginationDelegate.getInfo();
    }

    public void setOnTableClick(Runnable onTableClick) {
        interactionDelegate.setOnTableClick(onTableClick);
    }

    public Runnable getOnTableClick() {
        return interactionDelegate.getOnTableClick();
    }

    @Override
    public void setTableTheme(TableTheme theme) {
        styleDelegate.applyTheme(theme);
        refreshStyle();
    }

    @Override
    public TableTheme getTableTheme() {
        return styleDelegate.getTableTheme();
    }

    public void setBootstrapTheme(boolean enabled) {
        setTableTheme(enabled ? TableTheme.BOOTSTRAP : TableTheme.SHUI);
    }

    public boolean isBootstrapTheme() {
        return getTableTheme() == TableTheme.BOOTSTRAP || getTableTheme() == TableTheme.BOOTSTRAP_STRIPED;
    }

    public void setStripedRows(boolean stripedRows) {
        styleDelegate.setStripedRows(stripedRows);
        refreshStyle();
    }

    public boolean isStripedRows() {
        return styleDelegate.isStripedRows();
    }

    @Override
    public void setSearchVisible(boolean visible) {
        setSearchBoxVisible(visible);
    }

    @Override
    public boolean isSearchVisible() {
        return isSearchBoxVisible();
    }

    @Override
    public void setSearchBoxVisible(boolean visible) {
        searchDelegate.setSearchBoxVisible(visible);
        revalidate();
        repaint();
    }

    @Override
    public boolean isSearchBoxVisible() {
        return searchDelegate.isSearchBoxVisible();
    }

    @Override
    public void setSearchPlaceholder(String placeholder) {
        searchDelegate.setSearchPlaceholder(placeholder);
    }

    @Override
    public String getSearchPlaceholder() {
        return searchDelegate.getSearchPlaceholder();
    }

    @Override
    public void setSearchText(String text) {
        searchDelegate.setSearchText(text);
    }

    @Override
    public String getSearchText() {
        return searchDelegate.getSearchText();
    }

    @Override
    public void setFilterColumn(int columnIndex) {
        searchDelegate.setFilterColumn(columnIndex);
    }

    @Override
    public int getFilterColumn() {
        return searchDelegate.getFilterColumn();
    }

    public void clearSearch() {
        searchDelegate.clearSearch();
    }

    public void setSearchFieldFont(Font font) {
        searchDelegate.setSearchFieldFont(font);
    }

    public Font getSearchFieldFont() {
        return searchDelegate.getSearchFieldFont();
    }

    public void setTableBackground(Color color) {
        styleDelegate.setTableBackground(color);
        refreshStyle();
    }

    public Color getTableBackground() {
        return styleDelegate.getTableBackground();
    }

    public void setCellForeground(Color color) {
        styleDelegate.setCellForeground(color);
        refreshStyle();
    }

    public Color getCellForeground() {
        return styleDelegate.getCellForeground();
    }

    public void setHeaderBackground(Color color) {
        styleDelegate.setHeaderBackground(color);
        refreshStyle();
    }

    public Color getHeaderBackground() {
        return styleDelegate.getHeaderBackground();
    }

    public void setHeaderForeground(Color color) {
        styleDelegate.setHeaderForeground(color);
        refreshStyle();
    }

    public Color getHeaderForeground() {
        return styleDelegate.getHeaderForeground();
    }

    public void setHeaderFont(Font font) {
        styleDelegate.setHeaderFont(font);
        refreshStyle();
    }

    public Font getHeaderFont() {
        return styleDelegate.getHeaderFont();
    }

    public void setContentFont(Font font) {
        styleDelegate.setContentFont(font);
        refreshStyle();
    }

    public Font getContentFont() {
        return styleDelegate.getContentFont();
    }

    public void setSelectionBackground(Color color) {
        styleDelegate.setSelectionBackground(color);
        refreshStyle();
    }

    public Color getSelectionBackgroundColor() {
        return styleDelegate.getSelectionBackground();
    }

    public void setSelectionForeground(Color color) {
        styleDelegate.setSelectionForeground(color);
        refreshStyle();
    }

    public Color getSelectionForegroundColor() {
        return styleDelegate.getSelectionForeground();
    }

    public void setGridColor(Color color) {
        styleDelegate.setGridColor(color);
        refreshStyle();
    }

    public Color getGridColor() {
        return styleDelegate.getGridColor();
    }

    public void setAlternateRowBackground(Color color) {
        styleDelegate.setAlternateRowBackground(color);
        refreshStyle();
    }

    public Color getAlternateRowBackground() {
        return styleDelegate.getAlternateRowBackground();
    }

    public void setTableRowHeight(int rowHeight) {
        styleDelegate.setRowHeight(rowHeight);
        refreshStyle();
    }

    public int getTableRowHeight() {
        return styleDelegate.getRowHeight();
    }

    public void setShowHorizontalLines(boolean showHorizontalLines) {
        styleDelegate.setShowHorizontalLines(showHorizontalLines);
        refreshStyle();
    }

    public boolean isShowHorizontalLines() {
        return styleDelegate.isShowHorizontalLines();
    }

    public void setShowVerticalLines(boolean showVerticalLines) {
        styleDelegate.setShowVerticalLines(showVerticalLines);
        refreshStyle();
    }

    public boolean isShowVerticalLines() {
        return styleDelegate.isShowVerticalLines();
    }

    private void refreshStyle() {
        styleDelegate.apply(table, scrollPane);
        if (customRenderer != null) {
            styleDelegate.setRenderer(table, customRenderer);
        }
        revalidate();
        repaint();
    }

    private void updatePaginationState(PaginationInfo info) {
        if (info != null) {
            paginationDelegate.setInfo(info);
        } else {
            paginationDelegate.reset();
        }
    }
}
