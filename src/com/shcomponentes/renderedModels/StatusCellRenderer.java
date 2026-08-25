package com.shcomponentes.renderedModels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;

/**
 * Renderer para mostrar estados como etiquetas dentro de las celdas.
 */
public class StatusCellRenderer extends BasicRenderer {

    private static final long serialVersionUID = -5249880882303364340L;
    private static final int BADGE_HORIZONTAL_PADDING = 8;
    private static final int BADGE_VERTICAL_PADDING = 2;
    private static final int BADGE_ARC = 8;

    private final List<ModelRender> renders = new ArrayList<>();
    private Color contentBackground;

    public StatusCellRenderer addRenders(ModelRender... rules) {
        if (rules != null) {
            Arrays.stream(rules)
                    .filter(rule -> rule != null)
                    .forEach(renders::add);
        }
        return this;
    }

    public StatusCellRenderer addRender(ModelRender rule) {
        if (rule != null) {
            renders.add(rule);
        }
        return this;
    }

    public StatusCellRenderer clearRenders() {
        renders.clear();
        return this;
    }

    public List<ModelRender> getRenders() {
        return Collections.unmodifiableList(renders);
    }

    public StatusCellRenderer centerCols(int... indexCols) {
        setCenteredColumns(indexCols);
        return this;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
        contentBackground = null;

        if (!isSelected && !renders.isEmpty()) {
            ModelRender rule = findRule(table, value, row, column);
            if (rule != null) {
                applyRule(cell, rule);
            }
        }

        return cell;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        if (contentBackground == null) {
            super.paintComponent(graphics);
            return;
        }

        Graphics2D graphics2D = (Graphics2D) graphics.create();
        try {
            graphics2D.setColor(getBackground());
            graphics2D.fillRect(0, 0, getWidth(), getHeight());

            Rectangle contentBounds = resolveContentBounds(graphics2D);
            if (contentBounds != null) {
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setColor(contentBackground);
                graphics2D.fillRoundRect(contentBounds.x, contentBounds.y,
                        contentBounds.width, contentBounds.height, BADGE_ARC, BADGE_ARC);
            }

            boolean opaque = isOpaque();
            setOpaque(false);
            try {
                super.paintComponent(graphics2D);
            } finally {
                setOpaque(opaque);
            }
        } finally {
            graphics2D.dispose();
        }
    }

    private ModelRender findRule(JTable table, Object currentValue, int viewRow, int viewColumn) {
        int modelColumn = table != null ? table.convertColumnIndexToModel(viewColumn) : viewColumn;
        for (ModelRender rule : renders) {
            if (rule.getTarget() == RenderTarget.CELL && rule.getColumn() != modelColumn) {
                continue;
            }
            if (matchesRule(table, currentValue, viewRow, modelColumn, rule)) {
                return rule;
            }
        }
        return null;
    }

    private boolean matchesRule(JTable table, Object currentValue, int viewRow,
            int currentModelColumn, ModelRender rule) {
        if (rule.getColumn() == ModelRender.ANY_COLUMN) {
            return matchesAnyColumn(table, currentValue, viewRow, rule);
        }
        Object value = valueAt(table, viewRow, currentModelColumn, rule.getColumn(), currentValue);
        return rule.matches(value);
    }

    private boolean matchesAnyColumn(JTable table, Object currentValue, int viewRow, ModelRender rule) {
        if (table == null || viewRow < 0) {
            return rule.matches(currentValue);
        }
        int modelRow = table.convertRowIndexToModel(viewRow);
        TableModel model = table.getModel();
        if (modelRow < 0 || modelRow >= model.getRowCount()) {
            return rule.matches(currentValue);
        }
        for (int col = 0; col < model.getColumnCount(); col++) {
            if (rule.matches(model.getValueAt(modelRow, col))) {
                return true;
            }
        }
        return false;
    }

    private Object valueAt(JTable table, int viewRow, int currentModelColumn,
            int ruleColumn, Object currentValue) {
        if (table == null || viewRow < 0 || ruleColumn == currentModelColumn) {
            return currentValue;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);
        TableModel model = table.getModel();
        if (modelRow < 0 || modelRow >= model.getRowCount()
                || ruleColumn < 0 || ruleColumn >= model.getColumnCount()) {
            return currentValue;
        }
        return model.getValueAt(modelRow, ruleColumn);
    }

    private Rectangle resolveContentBounds(Graphics2D graphics) {
        String text = getText();
        Icon icon = getIcon();
        boolean hasText = text != null && !text.isEmpty();
        if (!hasText && icon == null) {
            return null;
        }

        Insets insets = getInsets();
        Rectangle viewBounds = new Rectangle(
                insets.left,
                insets.top,
                Math.max(0, getWidth() - insets.left - insets.right),
                Math.max(0, getHeight() - insets.top - insets.bottom));
        Rectangle iconBounds = new Rectangle();
        Rectangle textBounds = new Rectangle();

        SwingUtilities.layoutCompoundLabel(this, graphics.getFontMetrics(getFont()),
                text, icon, getVerticalAlignment(), getHorizontalAlignment(),
                getVerticalTextPosition(), getHorizontalTextPosition(), viewBounds,
                iconBounds, textBounds, getIconTextGap());

        Rectangle contentBounds = icon != null ? new Rectangle(iconBounds) : null;
        if (hasText) {
            contentBounds = contentBounds == null
                    ? new Rectangle(textBounds)
                    : contentBounds.union(textBounds);
        }
        return addBadgePadding(contentBounds);
    }

    private Rectangle addBadgePadding(Rectangle contentBounds) {
        int left = Math.max(1, contentBounds.x - BADGE_HORIZONTAL_PADDING);
        int top = Math.max(1, contentBounds.y - BADGE_VERTICAL_PADDING);
        int right = Math.min(getWidth() - 1,
                contentBounds.x + contentBounds.width + BADGE_HORIZONTAL_PADDING);
        int bottom = Math.min(getHeight() - 1,
                contentBounds.y + contentBounds.height + BADGE_VERTICAL_PADDING);
        return new Rectangle(left, top, Math.max(0, right - left), Math.max(0, bottom - top));
    }

    private void applyRule(Component cell, ModelRender rule) {
        contentBackground = rule.getBackground();
        Color color = rule.getForeground() != null
                ? rule.getForeground()
                : RenderColor.statusForeground(rule.getBackground());
        cell.setForeground(color);
    }
}
