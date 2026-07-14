package com.ShInputs;

import shui.beans.ShBeanInfoSupport;

public class ShInputBeanInfo extends ShBeanInfoSupport {

    public ShInputBeanInfo() {
        super(ShInput.class, "ShInput", "Campo de entrada Shui con tipos, header y validaciones.",
                "/shui/assets/shinput.png",
                new String[]{
                    "inputType", "inputDataType", "text", "placeholder",
                    "headerText", "headerPosition", "headerVisible", "headerForeground", "headerFont",
                    "inputBarVisible", "inputBarColor",
                    "maxLength", "minLength", "required", "regex", "autoValidate",
                    "showValidationState", "allowDecimal", "decimalPlaces", "allowNegative", "allowCopy", "allowPaste",
                    "datePattern", "editable"
                },
                "context");
    }
}
