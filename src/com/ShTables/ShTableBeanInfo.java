package com.ShTables;

import shui.beans.ShBeanInfoSupport;

public class ShTableBeanInfo extends ShBeanInfoSupport {

    public ShTableBeanInfo() {
        super(ShTable.class, "ShTable", "Tabla Shui con busqueda, temas y renderizado configurable.",
                "/shui/assets/shtable.png",
                new String[]{
                    "tableTheme", "bootstrapTheme", "stripedRows", "tableHeaders",
                    "paged", "pagedMode", "pageHandler",
                    "searchBoxVisible", "searchPlaceholder", "filterColumn",
                    "searchButtonVisible", "searchButtonText", "onSearchButtonClick",
                    "headerBackground", "headerForeground", "headerFont",
                    "contentFont", "tableBackground", "cellForeground",
                    "selectionBackground", "selectionForeground", "gridColor",
                    "alternateRowBackground", "tableRowHeight",
                    "showHorizontalLines", "showVerticalLines", "centerContentColumns",
                    "scrollBarThumbColor", "scrollBarTrackColor", "scrollBarSize",
                    "scrollBarMinimumThumbLength", "scrollBarThumbArc", "scrollBarTrackArc",
                    "scrollBarThumbInset", "scrollBarButtonsVisible",
                    "verticalScrollBarPolicy", "horizontalScrollBarPolicy", "tableAutoResizeMode",
                    "popupMenu"
                },
                "context", "menu", "scroll", "table", "onTableClick");
    }
}
