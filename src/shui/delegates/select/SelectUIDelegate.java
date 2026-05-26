package shui.delegates.select;

import java.awt.Color;

/**
 * Delegate de propiedades visuales para ShSelect.
 */
public class SelectUIDelegate {

    private Color buttonColor = Color.BLACK;
    private Color backgroundColor = Color.WHITE;
    private Color foregroundColor = Color.BLACK;
    private Color selectionBackgroundColor = new Color(204, 204, 204);
    private Color selectionForegroundColor = Color.BLACK;
    private Color popupBackgroundColor = Color.WHITE;
    private boolean popupBorderVisible;
    private int iconSize = 14;
    private String placeholder = "";

    public Color getButtonColor() {
        return buttonColor;
    }

    public void setButtonColor(Color buttonColor) {
        if (buttonColor != null) {
            this.buttonColor = buttonColor;
        }
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        if (backgroundColor != null) {
            this.backgroundColor = backgroundColor;
        }
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(Color foregroundColor) {
        if (foregroundColor != null) {
            this.foregroundColor = foregroundColor;
        }
    }

    public Color getSelectionBackgroundColor() {
        return selectionBackgroundColor;
    }

    public void setSelectionBackgroundColor(Color selectionBackgroundColor) {
        if (selectionBackgroundColor != null) {
            this.selectionBackgroundColor = selectionBackgroundColor;
        }
    }

    public Color getSelectionForegroundColor() {
        return selectionForegroundColor;
    }

    public void setSelectionForegroundColor(Color selectionForegroundColor) {
        if (selectionForegroundColor != null) {
            this.selectionForegroundColor = selectionForegroundColor;
        }
    }

    public Color getPopupBackgroundColor() {
        return popupBackgroundColor;
    }

    public void setPopupBackgroundColor(Color popupBackgroundColor) {
        if (popupBackgroundColor != null) {
            this.popupBackgroundColor = popupBackgroundColor;
        }
    }

    public boolean isPopupBorderVisible() {
        return popupBorderVisible;
    }

    public void setPopupBorderVisible(boolean popupBorderVisible) {
        this.popupBorderVisible = popupBorderVisible;
    }

    public int getIconSize() {
        return iconSize;
    }

    public void setIconSize(int iconSize) {
        this.iconSize = Math.max(8, iconSize);
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder != null ? placeholder : "";
    }
}
