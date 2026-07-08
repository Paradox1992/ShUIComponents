package shui.contracts.text;

import java.awt.Component;

/**
 * Capability for containers that manage ShInput form operations.
 */
public interface ShInputFormable {

    void clearForm();

    boolean validForm(Component... exclude);
}
