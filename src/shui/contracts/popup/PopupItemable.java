package shui.contracts.popup;

import java.awt.Color;
import java.awt.Font;
import javax.swing.Icon;

/**
 * Contrato para items acoplables a ShPopupMenu.
 */
public interface PopupItemable {

    void setItemText(String text);

    String getItemText();

    void setItemIcon(Icon icon);

    Icon getItemIcon();

    void setOnClick(Runnable onClick);

    Runnable getOnClick();

    void addChild(ShPopupItemReference child);

    void removeChild(ShPopupItemReference child);

    void clearChildren();

    int getChildCount();

    boolean isParentItem();

    void setItemBackground(Color color);

    Color getItemBackground();

    void setItemForeground(Color color);

    Color getItemForeground();

    void setItemHoverColor(Color color);

    Color getItemHoverColor();

    void setItemFont(Font font);

    Font getItemFont();

    void setIconTextGap(int gap);

    int getIconTextGap();

    void setArrowVisible(boolean visible);

    boolean isArrowVisible();

    void doClick();

    /**
     * Referencia minima para evitar que el contrato dependa del paquete publico.
     */
    interface ShPopupItemReference {
    }
}
