package shui.contracts.select;

/**
 * Callback ejecutado cuando cambia el item seleccionado de un ShSelect.
 *
 * @param <E> tipo de item seleccionado
 */
@FunctionalInterface
public interface SelectChangeHandler<E> {

    void onChange(E selectedItem);
}
