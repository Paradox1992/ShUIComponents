package com.ShImageChoosers;

import shui.beans.ShBeanInfoSupport;

public class ShImageChooserBeanInfo extends ShBeanInfoSupport {

    public ShImageChooserBeanInfo() {
        super(ShImageChooser.class, "ShImageChooser", "Selector Shui para buscar y mostrar imagenes.",
                "/shui/assets/imagen.png",
                new String[]{
                    "imagen", "selectedFile", "imageScale",
                    "buttonText", "buttonType", "actionButton", "bootstrapButton",
                    "iconSize", "customIcon", "customColor", "customForeground",
                    "buttonFont", "iconTextGap",
                    "previewBackground", "previewBorderColor",
                    "backgroundColor", "cornerRadius", "contentPadding",
                    "borderEnabled", "borderColor", "borderWidth",
                    "hoverEnabled", "hoverColor", "shadowEnabled", "visualState"
                },
                "context", "imagePanel", "searchButton");
    }
}
