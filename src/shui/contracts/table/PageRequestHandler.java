package shui.contracts.table;

/**
 * Callback usado por la paginacion para solicitar una nueva pagina.
 */
@FunctionalInterface
public interface PageRequestHandler {

    void onPageRequest(int page);
}
