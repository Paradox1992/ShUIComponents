package shui.contracts.text;

import com.ShContainers.ShPanel;
import java.awt.Component;

/**
 * Contract for form operations over Shui fields inside a ShPanel.
 */
public interface ShInputValidator {

    /**
     * Validates ShInput, ShDateSelector and ShLookup components, including nested fields.
     *
     * @param panel panel to inspect
     * @param exclude components that should not be evaluated
     * @return true when every evaluated input is valid
     */
    boolean validForm(ShPanel panel, Component... exclude);

    /**
     * Clears ShInput, ShDateSelector, ShLookup and ShImageChooser components in the panel.
     *
     * @param panel panel to clear
     */
    void clearForm(ShPanel panel);
}
