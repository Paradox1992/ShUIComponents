package shui.delegates.select;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Delegate desacoplado para datos y seleccion.
 *
 * @param <E> tipo de item
 */
public class SelectDataDelegate<E> {

    private final List<E> data = new ArrayList<>();
    private E selectedValue;

    public void setData(List<E> items) {
        data.clear();
        selectedValue = null;
        if (items != null) {
            data.addAll(items);
        }
    }

    public List<E> getData() {
        return Collections.unmodifiableList(data);
    }

    public void setSelectedValue(E value) {
        selectedValue = value;
    }

    public E getSelectedValue() {
        return selectedValue;
    }

    public int getSelectedIndex() {
        return data.indexOf(selectedValue);
    }

    public void setSelectedIndex(int index) {
        selectedValue = (index >= 0 && index < data.size()) ? data.get(index) : null;
    }

    public void clearSelection() {
        selectedValue = null;
    }
}
