package shui.delegates.table;

import shui.contracts.table.PageRequestHandler;
import shui.contracts.table.PaginationInfo;

/**
 * Estado y navegacion de paginacion para ShTable.
 */
public class TablePaginationDelegate {

    private PageRequestHandler handler;
    private PaginationInfo info = new PaginationInfo();
    private boolean paged;
    private boolean showItemsCount = true;

    public void reset() {
        info = new PaginationInfo(1, 1, 0);
    }

    public void setInfo(PaginationInfo info) {
        this.info = info != null ? info : new PaginationInfo(1, 1, 0);
    }

    public PaginationInfo getInfo() {
        return info;
    }

    public void requestFirst() {
        requestPage(1);
    }

    public void requestPrevious() {
        requestPage(Math.max(1, info.getCurrentPage() - 1));
    }

    public void requestNext() {
        requestPage(Math.min(info.getLastPage(), info.getCurrentPage() + 1));
    }

    public void requestLast() {
        requestPage(info.getLastPage());
    }

    public void requestPage(int page) {
        if (handler != null) {
            handler.onPageRequest(Math.max(1, page));
        }
    }

    public boolean canGoPrevious() {
        return info.getCurrentPage() > 1;
    }

    public boolean canGoNext() {
        return info.getCurrentPage() < info.getLastPage();
    }

    public void setHandler(PageRequestHandler handler) {
        this.handler = handler;
    }

    public PageRequestHandler getHandler() {
        return handler;
    }

    public boolean isPaged() {
        return paged;
    }

    public void setPaged(boolean paged) {
        this.paged = paged;
    }

    public boolean isShowItemsCount() {
        return showItemsCount;
    }

    public void setShowItemsCount(boolean showItemsCount) {
        this.showItemsCount = showItemsCount;
    }
}
