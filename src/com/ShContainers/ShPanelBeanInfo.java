package com.ShContainers;

import java.beans.PropertyDescriptor;
import shui.beans.ShBeanInfoSupport;

public class ShPanelBeanInfo extends ShBeanInfoSupport {

    public ShPanelBeanInfo() {
        super(ShPanel.class, "ShPanel", "Panel visual Shui con bordes, sombras y estados.",
                "/shui/assets/shpanel.png",
                new String[]{
                    "panelStyle", "styleBackgroundOpaque", "formMode", "backgroundColor",
                    "cornerRadius", "contentPadding",
                    "borderEnabled", "borderColor", "borderWidth",
                    "hoverEnabled", "hoverColor", "shadowEnabled", "visualState"
                },
                "context");
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor[] descriptors = super.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : descriptors) {
            if ("styleBackgroundOpaque".equals(descriptor.getName())) {
                descriptor.setShortDescription(
                        "Mantiene visible el estilo cuando el panel es la raiz de una ventana transparente."
                );
            }
        }
        return descriptors;
    }
}
