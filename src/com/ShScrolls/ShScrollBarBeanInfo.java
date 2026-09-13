package com.ShScrolls;

import java.beans.PropertyDescriptor;
import shui.beans.ShBeanInfoSupport;

public class ShScrollBarBeanInfo extends ShBeanInfoSupport {

    public ShScrollBarBeanInfo() {
        super(ShScrollBar.class, "ShScrollBar",
                "Barra de desplazamiento Shui compacta y configurable.",
                new String[]{
                    "orientation", "thumbColor", "trackColor", "scrollBarSize",
                    "minimumThumbLength", "thumbArc", "trackArc", "thumbInset",
                    "buttonsVisible", "unitIncrement", "blockIncrement",
                    "enabled", "visible"
                },
                "background", "border", "componentPopupMenu", "foreground", "opaque",
                "UI");
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor[] descriptors = super.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : descriptors) {
            switch (descriptor.getName()) {
                case "thumbColor" ->
                    descriptor.setShortDescription("Color del pulgar que indica la posicion.");
                case "trackColor" ->
                    descriptor.setShortDescription("Color de la pista de desplazamiento.");
                case "scrollBarSize" ->
                    descriptor.setShortDescription("Ancho vertical o alto horizontal en pixeles.");
                case "minimumThumbLength" ->
                    descriptor.setShortDescription("Longitud minima visible del pulgar.");
                case "thumbArc" ->
                    descriptor.setShortDescription("Redondeo del pulgar en pixeles.");
                case "trackArc" ->
                    descriptor.setShortDescription("Redondeo de la pista en pixeles.");
                case "thumbInset" ->
                    descriptor.setShortDescription("Margen interior aplicado al pulgar.");
                case "buttonsVisible" ->
                    descriptor.setShortDescription("Muestra los botones de incremento y decremento.");
                default -> {
                }
            }
        }
        return descriptors;
    }
}
