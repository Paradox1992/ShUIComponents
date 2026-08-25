package shui.contracts.select;

/**
 * Callback ejecutado cuando cambia el item seleccionado de un ShSelect.
 */
@FunctionalInterface
public interface SelectChangeHandler {

    void onChange();
}
