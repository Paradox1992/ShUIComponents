package shui.delegates.popup;

import shui.contracts.popup.PopupItemable.ShPopupItemReference;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.Icon;

/**
 * Delegate para estado, estilo e hijos de un ShPopupItem.
 */
public class PopupItemDelegate {

    private final List<ShPopupItemReference> children = new ArrayList<>();

    private String text = "Item";
    private Icon icon;
    private Runnable onClick;
    private Color background = Color.WHITE;
    private Color foreground = new Color(35, 35, 35);
    private Color hoverColor = new Color(235, 235, 235);
    private Font font = new Font("Segoe UI", Font.PLAIN, 13);
    private int iconTextGap = 8;
    private boolean arrowVisible = true;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text != null ? text : "";
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public Runnable getOnClick() {
        return onClick;
    }

    public void setOnClick(Runnable onClick) {
        this.onClick = onClick;
    }

    public void addChild(ShPopupItemReference child) {
        if (child != null && !children.contains(child)) {
            children.add(child);
        }
    }

    public void removeChild(ShPopupItemReference child) {
        children.remove(child);
    }

    public void clearChildren() {
        children.clear();
    }

    public List<ShPopupItemReference> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public int getChildCount() {
        return children.size();
    }

    public boolean isParentItem() {
        return !children.isEmpty();
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        if (background != null) {
            this.background = background;
        }
    }

    public Color getForeground() {
        return foreground;
    }

    public void setForeground(Color foreground) {
        if (foreground != null) {
            this.foreground = foreground;
        }
    }

    public Color getHoverColor() {
        return hoverColor;
    }

    public void setHoverColor(Color hoverColor) {
        if (hoverColor != null) {
            this.hoverColor = hoverColor;
        }
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        if (font != null) {
            this.font = font;
        }
    }

    public int getIconTextGap() {
        return iconTextGap;
    }

    public void setIconTextGap(int iconTextGap) {
        this.iconTextGap = Math.max(0, iconTextGap);
    }

    public boolean isArrowVisible() {
        return arrowVisible;
    }

    public void setArrowVisible(boolean arrowVisible) {
        this.arrowVisible = arrowVisible;
    }
}
