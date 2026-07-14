package com.ShDateSelectors;

import shui.beans.ShBeanInfoSupport;

public class ShDateSelectorBeanInfo extends ShBeanInfoSupport {

    public ShDateSelectorBeanInfo() {
        super(ShDateSelector.class, "ShDateSelector", "Selector Shui de fecha con popup estilo Material.", "/shui/assets/dateicon.png",
                new String[]{
                    "selectedDate", "text", "datePattern", "placeholder",
                    "minDate", "maxDate", "required", "editable", "allowCopy", "allowPaste", "weekStartsOnMonday",
                    "todayButtonVisible", "clearButtonVisible",
                    "headerText", "headerPosition", "headerVisible", "headerForeground", "headerFont",
                    "accentColor", "fieldBackgroundColor", "fieldForegroundColor", "placeholderColor",
                    "popupBackgroundColor", "selectedDayForeground", "dayHoverColor",
                    "backgroundColor", "cornerRadius", "contentPadding",
                    "borderEnabled", "borderColor", "borderWidth",
                    "hoverEnabled", "hoverColor", "shadowEnabled", "visualState"
                },
                "context", "actionListeners");
    }
}
