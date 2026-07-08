package com.ShContainers;

import shui.beans.ShBeanInfoSupport;

public class ShPanelBeanInfo extends ShBeanInfoSupport {

    public ShPanelBeanInfo() {
        super(ShPanel.class, "ShPanel", "Panel visual Shui con bordes, sombras y estados.",
                "/shui/assets/shpanel.png",
                new String[]{
                    "panelStyle", "backgroundColor", "cornerRadius", "contentPadding",
                    "borderEnabled", "borderColor", "borderWidth",
                    "hoverEnabled", "hoverColor", "shadowEnabled", "visualState"
                },
                "context");
    }
}
