package shui.repositories.text;

import com.ShDateSelectors.ShDateSelector;
import com.ShContainers.ShPanel;
import com.ShInputs.ShInput;
import java.awt.Component;
import java.awt.Container;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;
import shui.contracts.text.ShInputValidator;
import shui.contracts.visual.VisualState;

final class ShInputValidationRepository implements ShInputValidator {
    
    @Override
    public boolean validForm(ShPanel panel, Component... exclude) {
        if (panel == null) {
            return true;
        }
        
        boolean[] validForm = {true};
        Set<Component> excluded = excludedSet(exclude);
        visitFields(panel, excluded, field -> {
            if (field instanceof ShInput input && !validateAndApplyState(input)) {
                validForm[0] = false;
            } else if (field instanceof ShDateSelector dateSelector
                    && !validateAndApplyState(dateSelector)) {
                validForm[0] = false;
            }
        });
        return validForm[0];
    }
    
    @Override
    public void clearForm(ShPanel panel) {
        if (panel == null) {
            return;
        }
        
        visitFields(panel, Collections.emptySet(), field -> {
            if (field instanceof ShInput input) {
                clearInput(input);
            }
        });
    }
    
    private void visitFields(Container container, Set<Component> excluded, FormFieldVisitor visitor) {
        for (Component component : container.getComponents()) {
            if ((component instanceof ShInput || component instanceof ShDateSelector)
                    && !excluded.contains(component)) {
                visitor.visit(component);
            }
            
            if (component instanceof Container child) {
                visitFields(child, excluded, visitor);
            }
        }
    }
    
    private void clearInput(ShInput input) {
        input.clear();
        input.setToolTipText(null);
        input.setVisualState(VisualState.NONE);
    }

    private boolean validateAndApplyState(ShInput input) {
        boolean valid = input.isValidInput();
        input.setVisualState(valid ? VisualState.NONE : VisualState.ERROR);
        return valid;
    }

    private boolean validateAndApplyState(ShDateSelector dateSelector) {
        boolean valid = dateSelector.validateDate();
        dateSelector.setVisualState(valid ? VisualState.NONE : VisualState.ERROR);
        return valid;
    }
    
    private Set<Component> excludedSet(Component... excludedComponents) {
        Set<Component> excluded = Collections.newSetFromMap(new IdentityHashMap<>());
        if (excludedComponents == null) {
            return excluded;
        }
        
        for (Component component : excludedComponents) {
            if (component != null) {
                excluded.add(component);
            }
        }
        return excluded;
    }
    
    private interface FormFieldVisitor {
        
        void visit(Component component);
    }
}
