package shui.contracts.choice;

import java.awt.Color;
import java.awt.Font;

/**
 * Contrato comun para controles seleccionables como checkbox y radio button.
 */
public interface Toggleable {

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

    void setText(String text);

    String getText();

    void setSelected(boolean selected);

    boolean isSelected();

    void toggle();

    void doClick();

    void setIndicatorSize(int size);

    int getIndicatorSize();

    void setIndicatorColor(Color color);

    Color getIndicatorColor();

    void setIndicatorBorderColor(Color color);

    Color getIndicatorBorderColor();

    void setTextColor(Color color);

    Color getTextColor();

    void setToggleFont(Font font);

    Font getToggleFont();

    void setTextGap(int gap);

    int getTextGap();

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
}
