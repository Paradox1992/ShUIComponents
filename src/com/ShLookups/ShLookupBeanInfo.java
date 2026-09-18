package com.ShLookups;

import shui.beans.ShBeanInfoSupport;

public class ShLookupBeanInfo extends ShBeanInfoSupport {

    public ShLookupBeanInfo() {
        super(ShLookup.class, "ShLookup", "Campo de consulta de solo lectura con valor generico y boton de busqueda.",
                "/shui/assets/search.png",
                new String[]{
                    "text", "placeholder", "headerText", "headerPosition", "headerVisible",
                    "headerFont", "contentFont", "headerForeground", "contentForeground",
                    "placeholderColor", "inputBarVisible", "inputBarColor",
                    "required", "autoValidate", "showValidationState"
                },
                "context", "value", "displayText", "onSearch");
        // El valor T y las funciones se configuran desde codigo.
    }
}
