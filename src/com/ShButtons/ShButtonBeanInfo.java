package com.ShButtons;

import java.beans.PropertyDescriptor;
import shui.beans.ShBeanInfoSupport;

public class ShButtonBeanInfo extends ShBeanInfoSupport {

    public ShButtonBeanInfo() {
        super(ShButton.class, "ShButton", "Boton Shui con tipos de accion, Bootstrap y custom.",
                "/shui/assets/shbutton.png",
                new String[]{
                    "text", "buttonText", "buttonType", "actionButton", "bootstrapButton",
                    "iconSize", "customColor", "customForeground", "buttonFont",
                    "iconTextGap", "horizontalAlignment", "verticalAlignment",
                    "horizontalTextPosition", "verticalTextPosition", "enabled"
                },
                "context", "onClick");
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor[] descriptors = super.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : descriptors) {
            switch (descriptor.getName()) {
                case "horizontalAlignment" ->
                    descriptor.setShortDescription("Alineacion horizontal del texto y el icono dentro del boton.");
                case "verticalAlignment" ->
                    descriptor.setShortDescription("Alineacion vertical del texto y el icono dentro del boton.");
                case "horizontalTextPosition" ->
                    descriptor.setShortDescription("Posicion horizontal del texto con respecto al icono.");
                case "verticalTextPosition" ->
                    descriptor.setShortDescription("Posicion vertical del texto con respecto al icono.");
                default -> {
                }
            }
        }
        return descriptors;
    }
}
