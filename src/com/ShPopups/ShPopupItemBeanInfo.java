package com.ShPopups;

import shui.beans.ShBeanInfoSupport;

public class ShPopupItemBeanInfo extends ShBeanInfoSupport {

    public ShPopupItemBeanInfo() {
        super(ShPopupItem.class, "ShPopupItem", "Item de menu popup Shui.",
                "/shui/assets/shmenuItem.png",
                new String[]{
                    "title", "titleFont", "icon", "root", "enteredColor", "exitedColor"
                },
                "context", "task", "subMenu");
    }
}
