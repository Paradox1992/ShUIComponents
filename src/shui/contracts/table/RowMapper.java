package shui.contracts.table;

/**
 * Convierte un objeto de dominio en los valores visibles de una fila.
 *
 * @param <T> tipo del item que alimenta la tabla
 */
@FunctionalInterface
public interface RowMapper<T> {

    Object[] mapToRow(T item);
}
