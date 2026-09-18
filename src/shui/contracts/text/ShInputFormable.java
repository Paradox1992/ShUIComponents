package shui.contracts.text;

import java.awt.Component;

/**
 * Capability for containers that validate and clear Shui form fields.
 */
public interface ShInputFormable {

    void clearForm();

    boolean validForm(Component... exclude);
}
