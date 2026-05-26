package shui.delegates.choice;

import shui.contracts.choice.Toggleable.HeaderPosition;
import java.awt.Color;
import java.awt.Font;

/**
 * Delegate para propiedades de header en controles toggle.
 */
public class ToggleHeaderDelegate {

    private String headerText = "";
    private HeaderPosition headerPosition = HeaderPosition.TOP_LEFT;
    private boolean headerVisible = true;
    private Color headerForeground = new Color(70, 70, 70);
    private Font headerFont = new Font("Segoe UI", Font.PLAIN, 12);

    public void setHeaderText(String headerText) {
        this.headerText = headerText != null ? headerText : "";
    }

    public String getHeaderText() {
        return headerText;
    }

    public void setHeaderPosition(HeaderPosition headerPosition) {
        this.headerPosition = headerPosition != null ? headerPosition : HeaderPosition.TOP_LEFT;
    }

    public HeaderPosition getHeaderPosition() {
        return headerPosition;
    }

    public void setHeaderVisible(boolean headerVisible) {
        this.headerVisible = headerVisible;
    }

    public boolean isHeaderVisible() {
        return headerVisible;
    }

    public void setHeaderForeground(Color headerForeground) {
        if (headerForeground != null) {
            this.headerForeground = headerForeground;
        }
    }

    public Color getHeaderForeground() {
        return headerForeground;
    }

    public void setHeaderFont(Font headerFont) {
        if (headerFont != null) {
            this.headerFont = headerFont;
        }
    }

    public Font getHeaderFont() {
        return headerFont;
    }

    public boolean hasHeader() {
        return headerVisible && !headerText.isBlank();
    }
}
