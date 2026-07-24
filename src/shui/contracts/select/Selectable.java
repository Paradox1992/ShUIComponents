package shui.contracts.select;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

/**
 * Contrato para componentes de seleccion tipo combo.
 *
 * @param <E> tipo de item seleccionado
 */
public interface Selectable<E> {

    enum HeaderPosition {
        TOP_LEFT,
        TOP_CENTER,
        TOP_RIGHT,
        MIDDLE_LEFT,
        MIDDLE_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_CENTER,
        BOTTOM_RIGHT
    }

    void setData(List<E> items);

    List<E> getData();

    void setSelectedValue(E value);

    E getSelectedValue();

    void clearSelection();

    void setButtonColor(Color color);

    Color getButtonColor();

    void setSelectBackgroundColor(Color color);

    Color getSelectBackgroundColor();

    void setSelectForegroundColor(Color color);

    Color getSelectForegroundColor();

    void setSelectionBackgroundColor(Color color);

    Color getSelectionBackgroundColor();

    void setSelectionForegroundColor(Color color);

    Color getSelectionForegroundColor();

    void setPopupBackgroundColor(Color color);

    Color getPopupBackgroundColor();

    void setIconSize(int iconSize);

    int getIconSize();

    void setPlaceholder(String placeholder);

    String getPlaceholder();

    void setPopupBorderVisible(boolean visible);

    boolean isPopupBorderVisible();

    void setHeaderText(String headerText);

    String getHeaderText();

    void setHeaderPosition(HeaderPosition position);

    HeaderPosition getHeaderPosition();

    void setHeaderVisible(boolean visible);

    boolean isHeaderVisible();

    void setHeaderForeground(Color color);

    Color getHeaderForeground();

    void setHeaderFont(Font font);

    Font getHeaderFont();

    void setContentFont(Font font);

    Font getContentFont();

    void setOnChange(Runnable onChange);

    Runnable getOnChange();

    void setOnchangeHandler(SelectChangeHandler<E> onchangeHandler);

    SelectChangeHandler<E> getOnchangeHandler();
}
