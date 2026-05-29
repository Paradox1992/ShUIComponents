package shui.contracts.table;

/**
 * Informacion minima para pintar los controles de paginacion.
 */
public class PaginationInfo {

    private int currentPage = 1;
    private int lastPage = 1;
    private int total;

    public PaginationInfo() {
    }

    public PaginationInfo(int currentPage, int lastPage, int total) {
        this.currentPage = Math.max(1, currentPage);
        this.lastPage = Math.max(1, lastPage);
        this.total = Math.max(0, total);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = Math.max(1, currentPage);
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = Math.max(1, lastPage);
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = Math.max(0, total);
    }
}
