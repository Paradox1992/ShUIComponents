package shui.contracts.popup;

import java.awt.Color;

/**
 * Contrato para menus popup Shui.
 */
public interface PopupMenuable {

    void addItem(PopupItemable.ShPopupItemReference item);

    void removeItem(PopupItemable.ShPopupItemReference item);

    void clearItems();

    int getItemCount();

    void setMenuBackground(Color color);

    Color getMenuBackground();

    void setMenuBorderColor(Color color);

    Color getMenuBorderColor();

    void setMenuBorderVisible(boolean visible);

    boolean isMenuBorderVisible();
}
