package com.shcomponentes.renderedModels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Renderizador base para JTable con estilos consistentes.
 */
public abstract class BasicRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 5717637803473758954L;

    protected static final Font DEFAULT_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    protected static final Color DEFAULT_SELECTION_BACKGROUND = new Color(13, 110, 253);
    protected static final Color DEFAULT_SELECTION_FOREGROUND = Color.WHITE;
    protected static final Color DEFAULT_FOREGROUND = new Color(33, 37, 41);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        label.setOpaque(true);
        label.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        label.setFont(resolveFont(table));
        label.setHorizontalAlignment(SwingConstants.LEFT);

        if (isSelected) {
            label.setBackground(resolveSelectionBackground(table));
            label.setForeground(resolveSelectionForeground(table));
        } else {
            label.setBackground(resolveBackground(table));
            label.setForeground(resolveForeground(table));
        }

        return label;
    }

    private static Font resolveFont(JTable table) {
        return table != null && table.getFont() != null ? table.getFont() : DEFAULT_FONT;
    }

    private static Color resolveBackground(JTable table) {
        return table != null && table.getBackground() != null ? table.getBackground() : Color.WHITE;
    }

    private static Color resolveForeground(JTable table) {
        return table != null && table.getForeground() != null ? table.getForeground() : DEFAULT_FOREGROUND;
    }

    private static Color resolveSelectionBackground(JTable table) {
        return table != null && table.getSelectionBackground() != null
                ? table.getSelectionBackground()
                : DEFAULT_SELECTION_BACKGROUND;
    }

    private static Color resolveSelectionForeground(JTable table) {
        return table != null && table.getSelectionForeground() != null
                ? table.getSelectionForeground()
                : DEFAULT_SELECTION_FOREGROUND;
    }
}
