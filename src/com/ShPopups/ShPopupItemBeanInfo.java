package com.ShPopups;

import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import shui.beans.ShBeanInfoSupport;

public class ShPopupItemBeanInfo extends ShBeanInfoSupport {

    public ShPopupItemBeanInfo() {
        super(ShPopupItem.class, "ShPopupItem", "Item de menu popup Shui.",
                "/shui/assets/shmenuItem.png",
                new String[]{
                    "title", "titleFont", "icon", "root", "enteredColor", "exitedColor"
                },
                "context", "task", "onClick", "subMenu");
    }

    @Override
    public EventSetDescriptor[] getEventSetDescriptors() {
        try {
            EventSetDescriptor[] descriptors = Introspector
                    .getBeanInfo(ShPopupItem.class, Introspector.IGNORE_ALL_BEANINFO)
                    .getEventSetDescriptors();
            for (EventSetDescriptor descriptor : descriptors) {
                if ("action".equals(descriptor.getName())) {
                    descriptor.setPreferred(true);
                    descriptor.setDisplayName("action");
                    descriptor.setShortDescription("Accion ejecutada al hacer clic en el item.");
                }
            }
            return descriptors;
        } catch (IntrospectionException ex) {
            return new EventSetDescriptor[0];
        }
    }

    @Override
    public int getDefaultEventIndex() {
        EventSetDescriptor[] descriptors = getEventSetDescriptors();
        for (int index = 0; index < descriptors.length; index++) {
            if ("action".equals(descriptors[index].getName())) {
                return index;
            }
        }
        return -1;
    }
}
