package com.ShTexts;

import java.beans.PropertyDescriptor;
import shui.beans.ShBeanInfoSupport;

public class ShMarqueeTextBeanInfo extends ShBeanInfoSupport {

    public ShMarqueeTextBeanInfo() {
        super(ShMarqueeText.class, "ShMarqueeText", "Etiqueta con texto estatico o animado.",
                "/shui/assets/moverse.png",
                new String[]{
                    "text", "foreground", "font", "mode", "speed", "animationDelay", "gap",
                    "running", "pauseOnHover", "horizontalAlignment", "verticalAlignment",
                    "enabled", "opaque", "background", "border"
                },
                "components", "layout", "labelFor");
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor[] descriptors = super.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : descriptors) {
            switch (descriptor.getName()) {
                case "mode" ->
                    descriptor.setShortDescription("Modo de visualizacion: STATIC o ANIMATED.");
                case "speed" ->
                    descriptor.setShortDescription("Velocidad del texto en pixeles por segundo.");
                case "animationDelay" ->
                    descriptor.setShortDescription("Intervalo de actualizacion en milisegundos.");
                case "gap" ->
                    descriptor.setShortDescription("Espacio antes de repetir la animacion.");
                case "running" ->
                    descriptor.setShortDescription("Inicia o detiene el movimiento cuando el modo es ANIMATED.");
                case "pauseOnHover" ->
                    descriptor.setShortDescription("Pausa la animacion mientras el puntero esta encima.");
                default -> {
                }
            }
        }
        return descriptors;
    }
}
