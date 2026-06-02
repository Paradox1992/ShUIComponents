package com.ShSelects;

import shui.beans.ShBeanInfoSupport;

public class ShSelectBeanInfo extends ShBeanInfoSupport {

    public ShSelectBeanInfo() {
        super(ShSelect.class, "ShSelect", "Selector Shui desacoplado de JComboBox con header.",
                "/shui/assets/shselect.png",
                new String[]{
                    "placeholder", "selectedIndex", "buttonColor", "selectBackgroundColor",
                    "selectForegroundColor", "selectionBackgroundColor", "selectionForegroundColor",
                    "popupBackgroundColor", "popupBorderVisible", "iconSize", "maximumRowCount",
                    "headerText", "headerPosition", "headerVisible", "headerForeground", "headerFont"
                },
                "context", "data", "selectedValue");
    }
}
