package com.ShPopups;

import shui.beans.ShBeanInfoSupport;

public class ShDialogBeanInfo extends ShBeanInfoSupport {

    public ShDialogBeanInfo() {
        super(ShDialog.class, "ShDialog", "Dialog Shui sin decoracion.",
                "/shui/assets/shdialog.png",
                new String[]{
                    "size", "background", "undecorated", "visible"
                });
    }
}
