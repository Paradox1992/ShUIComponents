package com.shcomponents.utilities;

import com.ShContainers.ShPanel;
import java.awt.Component;

/**
 * Utility access point for validating ShInput components inside ShPanel.
 */
public final class ShInputValidationUtil {

    private ShInputValidationUtil() {
    }

    public static void clearForm(ShPanel panel) {
        if (panel != null) {
            panel.clearForm();
        }
    }

    public static boolean validForm(ShPanel panel, Component... exclude) {
        return panel == null || panel.validForm(exclude);
    }
}
