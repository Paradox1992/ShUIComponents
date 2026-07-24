package shui.delegates.table;

import com.requestsupport.responses.PaginationMeta;
import shui.contracts.table.PageRequestHandler;
import shui.contracts.table.PagedMode;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Estado y navegacion de paginacion para ShTable.
 */
public class TablePaginationDelegate {

    private PageRequestHandler handler;
    private PaginationMeta info = defaultInfo();
    private final List<PageRequestHandler> pageRequestHandlers = new ArrayList<>();
    private final JPanel component = createComponent();
    private final JLabel summaryLabel = new JLabel();
    private final JButton firstButton = createButton("<<");
    private final JButton previousButton = createButton("<");
    private final JButton nextButton = createButton(">");
    private final JButton lastButton = createButton(">>");
    private boolean paged;
    private boolean showItemsCount = true;
    private int visibleRowCount;
    private int requestedPage = 1;
    private PagedMode pagedMode = PagedMode.FULL;

    public TablePaginationDelegate() {
        firstButton.addActionListener(event -> requestFirst());
        previousButton.addActionListener(event -> requestPrevious());
        nextButton.addActionListener(event -> requestNext());
        lastButton.addActionListener(event -> requestLast());
        component.add(summaryLabel);
        component.add(firstButton);
        component.add(previousButton);
        component.add(nextButton);
        component.add(lastButton);
        refreshComponent();
    }

    public void reset() {
        info = defaultInfo();
        requestedPage = info.getCurrentPage();
        refreshComponent();
    }

    public void setInfo(PaginationMeta info) {
        this.info = info != null ? info : defaultInfo();
        requestedPage = this.info.getCurrentPage();
        refreshComponent();
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
        int targetPage = normalizePage(page);
        if (targetPage == requestedPage && targetPage == info.getCurrentPage()) {
            return;
        }
        requestedPage = targetPage;
        info = new PaginationMeta(info.getTotal(), targetPage, Math.max(1, info.getLastPage()), info.getPath());
        refreshComponent();
        firePageRequest(targetPage);
    }

    public boolean canGoPrevious() {
        return requestedPage > 1;
    }

    public boolean canGoNext() {
        return requestedPage < Math.max(1, info.getLastPage());
    }

    public void setHandler(PageRequestHandler handler) {
        this.handler = handler;
    }

    public PageRequestHandler getHandler() {
        return handler;
    }

    public void addPageRequestHandler(PageRequestHandler handler) {
        if (handler != null) {
            pageRequestHandlers.add(handler);
        }
    }

    public void removePageRequestHandler(PageRequestHandler handler) {
        pageRequestHandlers.remove(handler);
    }

    public int getRequestedPage() {
        return requestedPage;
    }

    public boolean isPaged() {
        return paged;
    }

    public void setPaged(boolean paged) {
        this.paged = paged;
        refreshComponent();
    }

    public boolean isShowItemsCount() {
        return showItemsCount;
    }

    public void setShowItemsCount(boolean showItemsCount) {
        this.showItemsCount = showItemsCount;
        setPagedMode(showItemsCount ? PagedMode.FULL : PagedMode.SINGLE);
    }

    public PagedMode getPagedMode() {
        return pagedMode;
    }

    public void setPagedMode(PagedMode pagedMode) {
        this.pagedMode = pagedMode != null ? pagedMode : PagedMode.FULL;
        this.showItemsCount = this.pagedMode == PagedMode.FULL;
        refreshComponent();
    }

    public JComponent getComponent() {
        return component;
    }

    public void setVisibleRowCount(int visibleRowCount) {
        this.visibleRowCount = Math.max(0, visibleRowCount);
        refreshComponent();
    }

    private static PaginationMeta defaultInfo() {
        return new PaginationMeta(0, 1, 1, "");
    }

    private JPanel createComponent() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 4));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
        return panel;
    }

    private static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFocusable(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        return button;
    }

    private void refreshComponent() {
        component.setVisible(paged);
        summaryLabel.setVisible(pagedMode == PagedMode.FULL);
        summaryLabel.setText(createSummaryText());
        summaryLabel.setForeground(new Color(80, 80, 80));
        summaryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        firstButton.setEnabled(canGoPrevious());
        previousButton.setEnabled(canGoPrevious());
        nextButton.setEnabled(canGoNext());
        lastButton.setEnabled(canGoNext());
        component.revalidate();
        component.repaint();
    }

    private String createSummaryText() {
        int currentPage = Math.max(1, requestedPage);
        int lastPage = Math.max(1, info.getLastPage());
        return "Filas: " + resolveTotalRows() + " | Pagina " + currentPage + " de " + lastPage;
    }

    private long resolveTotalRows() {
        return Math.max(visibleRowCount, info.getTotal());
    }

    private int normalizePage(int page) {
        int lastPage = Math.max(1, info.getLastPage());
        return Math.max(1, Math.min(lastPage, page));
    }

    private void firePageRequest(int page) {
        if (handler != null) {
            handler.onPageRequest(page);
        }
        for (PageRequestHandler listener : List.copyOf(pageRequestHandlers)) {
            listener.onPageRequest(page);
        }
    }
}
