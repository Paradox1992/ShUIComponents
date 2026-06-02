package com.ShPopups;

import shui.beans.ShBeanInfoSupport;

public class ShPopupMenuBeanInfo extends ShBeanInfoSupport {

    public ShPopupMenuBeanInfo() {
        super(ShPopupMenu.class, "ShPopupMenu", "Dialog popup Shui para menus contextuales.",
                "/shui/assets/shmenu.png",
                new String[]{
                    "size", "background", "undecorated", "visible"
                });
    }
}
