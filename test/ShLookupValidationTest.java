import com.ShContainers.ShPanel;
import com.ShDateSelectors.ShDateSelector;
import com.ShInputs.ShInput;
import com.ShLookups.ShLookup;
import java.time.LocalDate;
import java.util.Objects;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import shui.contracts.visual.VisualState;

public final class ShLookupValidationTest {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            validatesSelectionInsteadOfDisplayText();
            followsInputValidationFlags();
            validatesNestedMixedFormsAndExclusions();
            clearsSelectionAndVisualState();
        });
        System.out.println("ShLookupValidationTest: OK");
    }

    private static void validatesSelectionInsteadOfDisplayText() {
        ShLookup<Object> lookup = new ShLookup<>();
        equal(false, lookup.isRequired(), "opcional por defecto");
        equal(true, lookup.isEmpty(), "sin seleccion");
        equal(true, lookup.isValidInput(), "opcional vacio valido");
        lookup.setRequired(true);
        lookup.setPlaceholder("Seleccione un cliente");
        lookup.setText("Texto sin objeto");
        equal(false, lookup.isValidInput(), "texto no sustituye seleccion");
        equal("El campo es obligatorio.", lookup.getValidationMessage(), "mensaje requerido");
        equal(lookup.getValidationMessage(), lookup.getToolTipText(), "tooltip invalido");
        lookup.setDisplayText(value -> "");
        lookup.setValue(new Object());
        equal(false, lookup.isEmpty(), "objeto con texto vacio");
        equal(true, lookup.isValidInput(), "seleccion valida sin texto");
        equal("", lookup.getValidationMessage(), "mensaje limpio");
        equal(null, lookup.getToolTipText(), "tooltip limpio");
    }

    private static void followsInputValidationFlags() {
        ShLookup<String> lookup = new ShLookup<>();
        equal(true, lookup.isAutoValidate(), "autovalidacion por defecto");
        equal(false, lookup.isShowValidationState(), "estado opcional por defecto");
        lookup.setRequired(true);
        equal(VisualState.NONE, lookup.getVisualState(), "validar sin pintar");
        lookup.setShowValidationState(true);
        lookup.isValidInput();
        equal(VisualState.ERROR, lookup.getVisualState(), "error requerido");
        lookup.setValue("Cliente");
        equal(VisualState.NONE, lookup.getVisualState(), "seleccion elimina error");
        lookup.clear();
        equal(VisualState.ERROR, lookup.getVisualState(), "limpiar vuelve a validar");
        lookup.setRequired(false);
        equal(VisualState.NONE, lookup.getVisualState(), "opcional elimina error");
        lookup.setAutoValidate(false);
        lookup.setRequired(true);
        equal(VisualState.NONE, lookup.getVisualState(), "autovalidacion desactivada");
        equal(false, lookup.isValidInput(), "validacion explicita");
        lookup.setValue("Cliente");
        equal(VisualState.ERROR, lookup.getVisualState(), "setValue no valida automaticamente");
        equal(true, lookup.isValidInput(), "seleccion valida explicitamente");
        equal(VisualState.NONE, lookup.getVisualState(), "estado recuperado");
    }

    private static void validatesNestedMixedFormsAndExclusions() {
        ShPanel form = new ShPanel();
        JPanel nested = new JPanel();
        ShLookup<String> lookup = new ShLookup<>();
        ShInput input = new ShInput();
        ShDateSelector date = new ShDateSelector();
        lookup.setRequired(true);
        lookup.setAutoValidate(false);
        input.setRequired(true);
        date.setRequired(true);
        nested.add(lookup);
        nested.add(date);
        form.add(input);
        form.add(nested);
        equal(false, form.validForm(), "formulario mixto invalido");
        equal(VisualState.ERROR, lookup.getVisualState(), "error forzado aun sin showValidationState");
        equal(VisualState.ERROR, input.getVisualState(), "input tambien evaluado");
        equal(VisualState.ERROR, date.getVisualState(), "fecha tambien evaluada");
        input.setText("Texto");
        date.setSelectedDate(LocalDate.of(2026, 9, 14));
        equal(true, form.validForm(lookup, null, lookup), "excluir lookup por identidad");
        equal(VisualState.ERROR, lookup.getVisualState(), "exclusion conserva estado");
        equal(false, form.validForm(), "lookup sigue siendo requerido");
        lookup.setValue("Cliente");
        equal(true, form.validForm((java.awt.Component[]) null), "formulario recuperado");
        equal(VisualState.NONE, lookup.getVisualState(), "formulario limpia error lookup");
        equal(VisualState.NONE, input.getVisualState(), "formulario limpia error input");
        equal(VisualState.NONE, date.getVisualState(), "formulario limpia error fecha");
    }

    private static void clearsSelectionAndVisualState() {
        ShPanel form = new ShPanel();
        JPanel nested = new JPanel();
        ShLookup<String> lookup = new ShLookup<>();
        lookup.setRequired(true);
        lookup.setShowValidationState(true);
        lookup.setValue("Cliente");
        nested.add(lookup);
        form.add(nested);
        form.clearForm();
        equal(null, lookup.getValue(), "clearForm limpia valor");
        equal("", lookup.getText(), "clearForm limpia texto");
        equal(null, lookup.getToolTipText(), "clearForm limpia tooltip");
        equal(VisualState.NONE, lookup.getVisualState(), "clearForm restablece estado");
        equal(false, form.validForm(), "clearForm conserva required");
        equal(VisualState.ERROR, lookup.getVisualState(), "revalidar formulario vacio");
    }

    private static void equal(Object expected, Object actual, String description) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError(description + ": esperado " + expected + ", obtenido " + actual);
        }
    }
}
