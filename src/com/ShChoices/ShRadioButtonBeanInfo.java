package com.ShChoices;

import shui.beans.ShBeanInfoSupport;

public class ShRadioButtonBeanInfo extends ShBeanInfoSupport {

    public ShRadioButtonBeanInfo() {
        super(ShRadioButton.class, "ShRadioButton", "RadioButton Shui con grupos y header configurable.",
                "/shui/assets/shradioButton.png",
                new String[]{
                    "text", "selected", "groupName", "indicatorSize", "indicatorColor",
                    "indicatorBorderColor", "textColor", "toggleFont", "textGap",
                    "headerText", "headerPosition", "headerVisible", "headerForeground", "headerFont"
                },
                "context", "onClick");
    }
}
