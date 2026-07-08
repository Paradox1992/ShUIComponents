package com.ShHeaders;

import shui.beans.ShBeanInfoSupport;

public class ShDialogHeaderBeanInfo extends ShBeanInfoSupport {

    public ShDialogHeaderBeanInfo() {
        super(ShDialogHeader.class, "ShDialogHeader", "Header para dialogos Shui con boton de cierre.",
                "/shui/assets/shdialogHeader.png",
                new String[]{
                    "title", "dialog", "titleFont", "titleColor", "buttonColor", "backgroundColor", "cornerRadius"
                },
                "context");
    }
}
