package shui.contracts.text;

import com.ShContainers.ShPanel;
import java.awt.Component;

/**
 * Contract for form operations over ShInput components inside a ShPanel.
 */
public interface ShInputValidator {

    /**
     * Validates all ShInput components contained in the panel.
     *
     * @param panel panel to inspect
     * @param exclude components that should not be evaluated
     * @return true when every evaluated input is valid
     */
    boolean validForm(ShPanel panel, Component... exclude);

    /**
     * Clears ShInput components contained in the panel.
     *
     * @param panel panel to clear
     */
    void clearForm(ShPanel panel);
}
