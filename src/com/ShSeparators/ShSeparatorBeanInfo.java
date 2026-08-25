package com.ShSeparators;

import java.beans.PropertyDescriptor;
import shui.beans.ShBeanInfoSupport;

public class ShSeparatorBeanInfo extends ShBeanInfoSupport {

    public ShSeparatorBeanInfo() {
        super(ShSeparator.class, "ShSeparator", "Separador visual horizontal o vertical.",
                "/shui/assets/separacion.png",
                new String[]{
                    "orientation", "separatorColor", "thickness", "startInset", "endInset",
                    "rounded", "enabled", "visible"
                },
                "background", "border", "components", "foreground", "layout", "opaque");
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor[] descriptors = super.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : descriptors) {
            switch (descriptor.getName()) {
                case "orientation" ->
                    descriptor.setShortDescription("Direccion del separador: HORIZONTAL o VERTICAL.");
                case "separatorColor" ->
                    descriptor.setShortDescription("Color con el que se dibuja la linea.");
                case "thickness" ->
                    descriptor.setShortDescription("Grosor de la linea en pixeles.");
                case "startInset" ->
                    descriptor.setShortDescription("Margen inicial de la linea en pixeles.");
                case "endInset" ->
                    descriptor.setShortDescription("Margen final de la linea en pixeles.");
                case "rounded" ->
                    descriptor.setShortDescription("Redondea los extremos cuando el grosor lo permite.");
                default -> {
                }
            }
        }
        return descriptors;
    }
}
