package shui.delegates.table;

import shui.contracts.table.TableColumnSpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Mantiene la configuracion declarativa de columnas.
 */
public class TableDataDelegate {

    private TableColumnSpec[] columnSpecs = new TableColumnSpec[0];

    public void setTableHeaders(String... headers) {
        if (headers == null) {
            columnSpecs = new TableColumnSpec[0];
            return;
        }

        List<TableColumnSpec> specs = new ArrayList<>();
        for (String header : headers) {
            if (header == null) {
                continue;
            }
            TableColumnSpec spec = TableColumnSpec.parse(header);
            if (!spec.getTitle().isEmpty() || header.contains("|") || headers.length == 1) {
                specs.add(spec);
            }
        }
        columnSpecs = specs.toArray(TableColumnSpec[]::new);
    }

    public String[] getTableHeaders() {
        return Arrays.stream(columnSpecs)
                .map(TableColumnSpec::toDeclaration)
                .toArray(String[]::new);
    }

    public void setColumnSpecs(TableColumnSpec... specs) {
        columnSpecs = specs != null ? Arrays.copyOf(specs, specs.length) : new TableColumnSpec[0];
    }

    public TableColumnSpec[] getColumnSpecs() {
        return Arrays.copyOf(columnSpecs, columnSpecs.length);
    }
}
