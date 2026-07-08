package shui.repositories.text;

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
        visitInputs(panel, excluded, input -> {
            if (!validateAndApplyState(input)) {
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
        
        visitInputs(panel, Collections.emptySet(), this::clearInput);
    }
    
    private void visitInputs(Container container, Set<Component> excluded, ShInputVisitor visitor) {
        for (Component component : container.getComponents()) {
            if (component instanceof ShInput input && !excluded.contains(input)) {
                visitor.visit(input);
            }
            
            if (component instanceof Container child) {
                visitInputs(child, excluded, visitor);
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
    
    private interface ShInputVisitor {
        
        void visit(ShInput input);
    }
}
