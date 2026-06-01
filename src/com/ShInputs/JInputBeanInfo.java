package com.ShInputs;

import shui.beans.ShBeanInfoSupport;

public class JInputBeanInfo extends ShBeanInfoSupport {

    public JInputBeanInfo() {
        super(JInput.class, "JInput", "Campo de entrada Shui con tipos, header y validaciones.",
                new String[]{
                    "inputType", "inputDataType", "text", "placeholder",
                    "headerText", "headerPosition", "headerVisible", "headerForeground", "headerFont",
                    "inputBarVisible", "inputBarColor",
                    "maxLength", "minLength", "required", "regex", "autoValidate",
                    "showValidationState", "allowDecimal", "allowNegative", "datePattern", "editable"
                },
                "context");
    }
}
