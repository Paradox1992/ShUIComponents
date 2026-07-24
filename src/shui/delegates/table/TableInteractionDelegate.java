package shui.delegates.table;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.ShPopups.ShPopupMenu;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

/**
 * Maneja eventos de mouse, menu contextual y callback de click.
 */
public class TableInteractionDelegate {

    private JDialog menu;
    private ShPopupMenu popupMenu;
    private Runnable onTableClick;

    public void install(JTable table) {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (SwingUtilities.isRightMouseButton(event)) {
                    showContextMenu(table, event);
                }
                if (onTableClick != null) {
                    onTableClick.run();
                }
            }
        });
    }

    public void setMenu(JDialog menu) {
        this.menu = menu;
        this.popupMenu = menu instanceof ShPopupMenu shPopupMenu ? shPopupMenu : null;
    }

    public JDialog getMenu() {
        return menu;
    }

    public void setPopupMenu(ShPopupMenu popupMenu) {
        this.popupMenu = popupMenu;
        this.menu = popupMenu;
    }

    public ShPopupMenu getPopupMenu() {
        return popupMenu;
    }

    public void setOnTableClick(Runnable onTableClick) {
        this.onTableClick = onTableClick;
    }

    public Runnable getOnTableClick() {
        return onTableClick;
    }

    private void showContextMenu(JTable table, MouseEvent event) {
        if (menu == null || table.getRowCount() == 0) {
            return;
        }

        int row = table.rowAtPoint(event.getPoint());
        if (row >= 0) {
            table.setRowSelectionInterval(row, row);
        }

        menu.setLocation(event.getLocationOnScreen());
        menu.setVisible(true);
    }
}
