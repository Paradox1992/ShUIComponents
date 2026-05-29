package shui.contracts.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Resultado paginado desacoplado de librerias externas.
 *
 * @param <T> tipo de item
 */
public class PagedTableData<T> {

    private final List<T> data;
    private final PaginationInfo paginationInfo;

    public PagedTableData(List<T> data, PaginationInfo paginationInfo) {
        this.data = data != null ? new ArrayList<>(data) : new ArrayList<>();
        this.paginationInfo = paginationInfo != null ? paginationInfo : new PaginationInfo();
    }

    public List<T> getData() {
        return Collections.unmodifiableList(data);
    }

    public PaginationInfo getPaginationInfo() {
        return paginationInfo;
    }
}
