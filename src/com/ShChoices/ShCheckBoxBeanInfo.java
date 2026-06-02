package com.ShChoices;

import shui.beans.ShBeanInfoSupport;

public class ShCheckBoxBeanInfo extends ShBeanInfoSupport {

    public ShCheckBoxBeanInfo() {
        super(ShCheckBox.class, "ShCheckBox", "CheckBox Shui con header configurable.",
                "/shui/assets/shcheckbox.png",
                new String[]{
                    "text", "selected", "indicatorSize", "indicatorColor", "indicatorBorderColor",
                    "textColor", "toggleFont", "textGap",
                    "headerText", "headerPosition", "headerVisible", "headerForeground", "headerFont"
                },
                "context", "onClick");
    }
}
