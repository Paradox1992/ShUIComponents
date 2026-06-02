package com.ShHeaders;

import shui.beans.ShBeanInfoSupport;

public class ShPanelHeaderBeanInfo extends ShBeanInfoSupport {

    public ShPanelHeaderBeanInfo() {
        super(ShPanelHeader.class, "ShPanelHeader", "Header simple para paneles Shui.",
                "/shui/assets/shpanelHeader.png",
                new String[]{
                    "titleText", "titleFont", "titleColor", "backgroundColor", "cornerRadius"
                },
                "context");
    }
}
