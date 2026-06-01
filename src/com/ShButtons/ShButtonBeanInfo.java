package com.ShButtons;

import shui.beans.ShBeanInfoSupport;

public class ShButtonBeanInfo extends ShBeanInfoSupport {

    public ShButtonBeanInfo() {
        super(ShButton.class, "ShButton", "Boton Shui con tipos de accion, Bootstrap y custom.",
                new String[]{
                    "text", "buttonText", "buttonType", "actionButton", "bootstrapButton",
                    "iconSize", "customColor", "customForeground", "buttonFont",
                    "iconTextGap", "enabled"
                },
                "context", "onClick");
    }
}
