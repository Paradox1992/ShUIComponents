import com.ShSelects.ShSelect;
import java.util.List;
import java.util.Objects;

public final class ShSelectSelectionTest {

    private ShSelectSelectionTest() {
    }

    public static void main(String[] args) {
        selectsCanonicalModelItem();
        preservesSelectionMadeBeforeDataArrives();
    }

    private static void selectsCanonicalModelItem() {
        Option first = new Option(1, "USUARIO");
        Option complete = new Option(7, "ADMINISTRADOR");
        Option partial = new Option(7, null);
        ShSelect<Option> select = new ShSelect<>();

        select.setData(List.of(first, complete));
        select.setSelectedValue(partial);

        if (select.getSelectedItem() != complete) {
            throw new AssertionError("ShSelect no reutilizó el item completo del modelo");
        }
        if (select.getSelectedIndex() != 1) {
            throw new AssertionError("ShSelect no seleccionó el índice del item resuelto");
        }
        if (!Objects.equals(7, select.getSelectedItem().id())) {
            throw new AssertionError("ShSelect perdió el id del item seleccionado");
        }
    }

    private static void preservesSelectionMadeBeforeDataArrives() {
        Option inactive = new Option(2, "INACTIVO");
        Option active = new Option(1, "ACTIVO");
        Option pendingActive = new Option(1, "ACTIVO");
        ShSelect<Option> select = new ShSelect<>();

        select.setSelectedValue(pendingActive);
        if (select.getSelectedIndex() != -1) {
            throw new AssertionError("La selección pendiente no debería tener índice antes de cargar datos");
        }

        select.setData(List.of(inactive, active));

        if (select.getSelectedItem() != active) {
            throw new AssertionError("ShSelect no conservó la selección pendiente al cargar los datos");
        }
        if (select.getSelectedIndex() != 1) {
            throw new AssertionError("ShSelect no resolvió el índice de la selección pendiente");
        }
    }

    private record Option(Integer id, String description) {

        @Override
        public boolean equals(Object object) {
            return object instanceof Option other && Objects.equals(id, other.id);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(id);
        }

        @Override
        public String toString() {
            return description;
        }
    }
}
