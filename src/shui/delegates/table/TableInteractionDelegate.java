package shui.delegates.table;

import java.awt.MouseInfo;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

/**
 * Maneja eventos de mouse, menu contextual y callback de click.
 */
public class TableInteractionDelegate {

    private JDialog menu;
    private Runnable onTableClick;

    public void install(JTable table) {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (menu != null && SwingUtilities.isRightMouseButton(event)) {
                    showMenu();
                }
                if (onTableClick != null) {
                    onTableClick.run();
                }
            }
        });
    }

    public void setMenu(JDialog menu) {
        this.menu = menu;
    }

    public JDialog getMenu() {
        return menu;
    }

    public void setOnTableClick(Runnable onTableClick) {
        this.onTableClick = onTableClick;
    }

    public Runnable getOnTableClick() {
        return onTableClick;
    }

    private void showMenu() {
        menu.setLocation(MouseInfo.getPointerInfo().getLocation());
        menu.setVisible(true);
    }
}
