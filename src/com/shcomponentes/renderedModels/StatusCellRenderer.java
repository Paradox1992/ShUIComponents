package com.shcomponentes.renderedModels;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 * Renderer para pintar filas o celdas segun reglas de estado.
 */
public class StatusCellRenderer extends BasicRenderer {

    private static final long serialVersionUID = -5249880882303364340L;

    private final List<ModelRender> renders = new ArrayList<>();

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

        if (!isSelected && !renders.isEmpty()) {
            ModelRender rule = findRule(table, value, row, column);
            if (rule != null) {
                applyRule(cell, rule);
            }
        }

        return cell;
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

    private static void applyRule(Component cell, ModelRender rule) {
        Color background = rule.getBackground();
        cell.setBackground(background);
        cell.setForeground(rule.getForeground() != null
                ? rule.getForeground()
                : RenderColor.readableForeground(background));
    }
}
