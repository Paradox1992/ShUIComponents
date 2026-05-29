package shui.delegates.table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.net.URL;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

/**
 * UI y logica de busqueda/filtro para ShTable.
 */
public class TableSearchDelegate<T> {

    private static final String SEARCH_ICON = "/shui/assets/lupa_color_x24.png";

    private final ShTableModel<T> model;
    private final TableRowSorter<ShTableModel<T>> rowSorter;
    private final JPanel searchPanel = new JPanel(new BorderLayout(6, 0));
    private final JPanel searchBoxPanel = new JPanel(new BorderLayout(4, 0));
    private final JLabel searchIconLabel = new JLabel();
    private final PlaceholderTextField searchField = new PlaceholderTextField();

    private int filterColumn = -1;
    private int lastAppliedColumn = Integer.MIN_VALUE;
    private String lastAppliedText;

    public TableSearchDelegate(ShTableModel<T> model, TableRowSorter<ShTableModel<T>> rowSorter) {
        this.model = model;
        this.rowSorter = rowSorter;
        configure();
    }

    public JComponent getComponent() {
        return searchPanel;
    }

    public int getFilteredRowCount() {
        return rowSorter.getViewRowCount();
    }

    public void setSearchBoxVisible(boolean visible) {
        searchPanel.setVisible(visible);
    }

    public boolean isSearchBoxVisible() {
        return searchPanel.isVisible();
    }

    public void setSearchPlaceholder(String placeholder) {
        searchField.setPlaceholder(placeholder);
    }

    public String getSearchPlaceholder() {
        return searchField.getPlaceholder();
    }

    public void setSearchText(String text) {
        searchField.setText(text != null ? text : "");
    }

    public String getSearchText() {
        return searchField.getText();
    }

    public void setFilterColumn(int columnIndex) {
        filterColumn = columnIndex;
        refreshFilter();
    }

    public int getFilterColumn() {
        return filterColumn;
    }

    public void clearSearch() {
        setSearchText("");
    }

    public void setSearchFieldFont(Font font) {
        if (font != null) {
            searchField.setFont(font);
        }
    }

    public Font getSearchFieldFont() {
        return searchField.getFont();
    }

    public void refreshFilter() {
        lastAppliedText = null;
        applyFilter();
    }

    private void configure() {
        searchPanel.setOpaque(false);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        searchBoxPanel.setOpaque(false);
        searchBoxPanel.setPreferredSize(new Dimension(460, 34));

        URL iconUrl = TableSearchDelegate.class.getResource(SEARCH_ICON);
        if (iconUrl != null) {
            searchIconLabel.setIcon(new ImageIcon(iconUrl));
        }
        searchIconLabel.setBorder(BorderFactory.createEmptyBorder(3, 15, 0, 2));

        searchField.setPlaceholder("Buscar...");
        searchField.setOpaque(false);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(150, 150, 150)),
                BorderFactory.createEmptyBorder(4, 2, 4, 2)));
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                applyFilter();
            }
        });

        searchBoxPanel.add(searchIconLabel, BorderLayout.WEST);
        searchBoxPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchBoxPanel, BorderLayout.WEST);
    }

    private void applyFilter() {
        String text = normalizeText(searchField.getText());
        int column = normalizeColumn(filterColumn);
        if (Objects.equals(text, lastAppliedText) && column == lastAppliedColumn) {
            return;
        }

        lastAppliedText = text;
        lastAppliedColumn = column;

        if (text.isBlank()) {
            rowSorter.setRowFilter(null);
            return;
        }

        String expression = "(?i)" + Pattern.quote(text);
        rowSorter.setRowFilter(column >= 0
                ? RowFilter.regexFilter(expression, column)
                : RowFilter.regexFilter(expression));
    }

    private String normalizeText(String text) {
        return text != null ? text.trim() : "";
    }

    private int normalizeColumn(int columnIndex) {
        return columnIndex >= 0 && columnIndex < model.getColumnCount() ? columnIndex : -1;
    }

    private static class PlaceholderTextField extends JTextField {

        private String placeholder = "";

        public void setPlaceholder(String placeholder) {
            this.placeholder = placeholder != null ? placeholder : "";
            repaint();
        }

        public String getPlaceholder() {
            return placeholder;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (!getText().isEmpty() || placeholder.isBlank()) {
                return;
            }

            Graphics copy = g.create();
            copy.setColor(new Color(145, 145, 145));
            FontMetrics metrics = copy.getFontMetrics();
            Insets insets = getInsets();
            int y = (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
            copy.drawString(placeholder, insets.left, y);
            copy.dispose();
        }
    }
}
