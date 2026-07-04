package shui.delegates.table;

import com.requestsupport.responses.PaginationMeta;
import shui.contracts.table.PageRequestHandler;

/**
 * Estado y navegacion de paginacion para ShTable.
 */
public class TablePaginationDelegate {

    private PageRequestHandler handler;
    private PaginationMeta info = defaultInfo();
    private boolean paged;
    private boolean showItemsCount = true;

    public void reset() {
        info = defaultInfo();
    }

    public void setInfo(PaginationMeta info) {
        this.info = info != null ? info : defaultInfo();
    }

    public PaginationMeta getInfo() {
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

    private static PaginationMeta defaultInfo() {
        return new PaginationMeta(0, 1, 1, "");
    }
}
