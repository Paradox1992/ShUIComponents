package shui.delegates.table;

import shui.contracts.table.TableTheme;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

/**
 * Aplica estilos visuales y renderers a JTable/JScrollPane.
 */
public class TableStyleDelegate {

    private Color tableBackground = Color.WHITE;
    private Color cellForeground = new Color(80, 80, 80);
    private Color headerBackground = new Color(245, 245, 245);
    private Color headerForeground = new Color(40, 40, 40);
    private Color selectionBackground = new Color(43, 45, 66);
    private Color selectionForeground = new Color(237, 242, 244);
    private Color gridColor = new Color(230, 230, 230);
    private Color alternateRowBackground = new Color(248, 249, 250);
    private Font headerFont;
    private Font contentFont;
    private int rowHeight = 26;
    private boolean showHorizontalLines = true;
    private boolean showVerticalLines;
    private boolean stripedRows;
    private TableTheme tableTheme = TableTheme.SHUI;
    private int[] centerContentColumns = new int[0];
    private final TableCellRenderer headerRenderer = createHeaderRenderer();
    private final TableCellRenderer cellRenderer = createCellRenderer();

    public void apply(JTable table, JScrollPane scrollPane) {
        Objects.requireNonNull(table, "table");
        table.setOpaque(true);
        table.setBackground(tableBackground);
        table.setForeground(cellForeground);
        table.setSelectionBackground(selectionBackground);
        table.setSelectionForeground(selectionForeground);
        if (contentFont != null) {
            table.setFont(contentFont);
        }
        table.setGridColor(gridColor);
        table.setRowHeight(rowHeight);
        table.setShowHorizontalLines(showHorizontalLines);
        table.setShowVerticalLines(showVerticalLines);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
        if (headerFont != null) {
            table.getTableHeader().setFont(headerFont);
        }
        table.getTableHeader().setDefaultRenderer(headerRenderer);
        table.setDefaultRenderer(Object.class, cellRenderer);

        if (scrollPane != null) {
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(true);
            scrollPane.getViewport().setBackground(tableBackground);
            scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
            scrollPane.getHorizontalScrollBar().setUI(new ModernScrollBarUI());
            scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(6, 6));
            scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(6, 6));
        }
    }

    public void setRenderer(JTable table, TableCellRenderer renderer) {
        if (table != null && renderer != null) {
            table.setDefaultRenderer(Object.class, createAlignedRenderer(renderer));
        }
    }

    public void applyTheme(TableTheme theme) {
        tableTheme = theme != null ? theme : TableTheme.SHUI;
        switch (tableTheme) {
            case BOOTSTRAP -> applyBootstrap(false);
            case BOOTSTRAP_STRIPED -> applyBootstrap(true);
            default -> applyShui();
        }
    }

    public TableTheme getTableTheme() {
        return tableTheme;
    }

    private void applyShui() {
        tableBackground = Color.WHITE;
        cellForeground = new Color(80, 80, 80);
        headerBackground = new Color(245, 245, 245);
        headerForeground = new Color(40, 40, 40);
        selectionBackground = new Color(43, 45, 66);
        selectionForeground = new Color(237, 242, 244);
        gridColor = new Color(230, 230, 230);
        alternateRowBackground = new Color(248, 249, 250);
        rowHeight = 26;
        showHorizontalLines = true;
        showVerticalLines = false;
        stripedRows = false;
    }

    private void applyBootstrap(boolean striped) {
        tableBackground = Color.WHITE;
        cellForeground = new Color(33, 37, 41);
        headerBackground = new Color(248, 249, 250);
        headerForeground = new Color(33, 37, 41);
        selectionBackground = new Color(13, 110, 253);
        selectionForeground = Color.WHITE;
        gridColor = new Color(222, 226, 230);
        alternateRowBackground = new Color(242, 242, 242);
        rowHeight = 30;
        showHorizontalLines = true;
        showVerticalLines = false;
        stripedRows = striped;
    }

    public Color getTableBackground() {
        return tableBackground;
    }

    public void setTableBackground(Color tableBackground) {
        if (tableBackground != null) {
            this.tableBackground = tableBackground;
        }
    }

    public Color getCellForeground() {
        return cellForeground;
    }

    public void setCellForeground(Color cellForeground) {
        if (cellForeground != null) {
            this.cellForeground = cellForeground;
        }
    }

    public Color getHeaderBackground() {
        return headerBackground;
    }

    public void setHeaderBackground(Color headerBackground) {
        if (headerBackground != null) {
            this.headerBackground = headerBackground;
        }
    }

    public Color getHeaderForeground() {
        return headerForeground;
    }

    public void setHeaderForeground(Color headerForeground) {
        if (headerForeground != null) {
            this.headerForeground = headerForeground;
        }
    }

    public Color getSelectionBackground() {
        return selectionBackground;
    }

    public void setSelectionBackground(Color selectionBackground) {
        if (selectionBackground != null) {
            this.selectionBackground = selectionBackground;
        }
    }

    public Color getSelectionForeground() {
        return selectionForeground;
    }

    public void setSelectionForeground(Color selectionForeground) {
        if (selectionForeground != null) {
            this.selectionForeground = selectionForeground;
        }
    }

    public Color getGridColor() {
        return gridColor;
    }

    public void setGridColor(Color gridColor) {
        if (gridColor != null) {
            this.gridColor = gridColor;
        }
    }

    public Color getAlternateRowBackground() {
        return alternateRowBackground;
    }

    public void setAlternateRowBackground(Color alternateRowBackground) {
        if (alternateRowBackground != null) {
            this.alternateRowBackground = alternateRowBackground;
        }
    }

    public Font getHeaderFont() {
        return headerFont;
    }

    public void setHeaderFont(Font headerFont) {
        if (headerFont != null) {
            this.headerFont = headerFont;
        }
    }

    public Font getContentFont() {
        return contentFont;
    }

    public void setContentFont(Font contentFont) {
        if (contentFont != null) {
            this.contentFont = contentFont;
        }
    }

    public int getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(int rowHeight) {
        this.rowHeight = Math.max(12, rowHeight);
    }

    public boolean isShowHorizontalLines() {
        return showHorizontalLines;
    }

    public void setShowHorizontalLines(boolean showHorizontalLines) {
        this.showHorizontalLines = showHorizontalLines;
    }

    public boolean isShowVerticalLines() {
        return showVerticalLines;
    }

    public void setShowVerticalLines(boolean showVerticalLines) {
        this.showVerticalLines = showVerticalLines;
    }

    public boolean isStripedRows() {
        return stripedRows;
    }

    public void setStripedRows(boolean stripedRows) {
        this.stripedRows = stripedRows;
    }

    public void setCenterContentColumns(int... indexCols) {
        centerContentColumns = indexCols != null ? indexCols.clone() : new int[0];
    }

    public int[] getCenterContentColumns() {
        return centerContentColumns.clone();
    }

    private TableCellRenderer createHeaderRenderer() {
        return new DefaultTableCellRenderer() {
            private static final long serialVersionUID = -1175987752418532255L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                JTableHeader header = table.getTableHeader();
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setOpaque(true);
                label.setBackground(headerBackground);
                label.setForeground(headerForeground);
                label.setFont(headerFont != null ? headerFont : header.getFont());
                label.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
                return label;
            }
        };
    }

    private TableCellRenderer createCellRenderer() {
        return new DefaultTableCellRenderer() {
            private static final long serialVersionUID = -4873742078758642339L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean selected,
                    boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(
                        table, value, selected, hasFocus, row, column);
                if (selected) {
                    component.setBackground(selectionBackground);
                    component.setForeground(selectionForeground);
                } else {
                    component.setBackground(stripedRows && row % 2 == 1
                            ? alternateRowBackground
                            : tableBackground);
                    component.setForeground(cellForeground);
                }
                if (component instanceof JComponent jc) {
                    jc.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                }
                if (contentFont != null) {
                    component.setFont(contentFont);
                }
                applyContentAlignment(table, component, column);
                return component;
            }
        };
    }

    private TableCellRenderer createAlignedRenderer(TableCellRenderer renderer) {
        return (table, value, isSelected, hasFocus, row, column) -> {
            Component component = renderer.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            applyContentAlignment(table, component, column);
            return component;
        };
    }

    private void applyContentAlignment(JTable table, Component component, int viewColumn) {
        if (component instanceof JLabel label) {
            label.setHorizontalAlignment(isCenteredColumn(table, viewColumn)
                    ? SwingConstants.CENTER
                    : SwingConstants.LEFT);
        }
    }

    private boolean isCenteredColumn(JTable table, int viewColumn) {
        if (centerContentColumns.length == 0) {
            return false;
        }
        int modelColumn = table != null ? table.convertColumnIndexToModel(viewColumn) : viewColumn;
        return Arrays.stream(centerContentColumns)
                .anyMatch(index -> index == -1 || index == modelColumn);
    }

    private static class ModernScrollBarUI extends BasicScrollBarUI {

        @Override
        protected void configureScrollBarColors() {
            thumbColor = new Color(160, 160, 160);
            trackColor = new Color(245, 245, 245);
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(thumbColor);
            g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 6, 6);
            g2.dispose();
        }

        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }
    }
}
