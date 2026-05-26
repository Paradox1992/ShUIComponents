package shui.delegates.choice;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.EventListenerList;

/**
 * Delegate de estado, estilo y eventos para controles toggle.
 */
public class ToggleStateDelegate {

    private final Object source;
    private final EventListenerList listeners = new EventListenerList();

    private String text = "";
    private boolean selected;
    private int indicatorSize = 18;
    private int textGap = 8;
    private Color indicatorColor = new Color(13, 110, 253);
    private Color indicatorBorderColor = new Color(120, 120, 120);
    private Color textColor = new Color(35, 35, 35);
    private Font toggleFont = new Font("Segoe UI", Font.PLAIN, 13);
    private Runnable onClick;

    public ToggleStateDelegate(Object source) {
        this.source = source;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text != null ? text : "";
    }

    public boolean isSelected() {
        return selected;
    }

    public boolean setSelected(boolean selected) {
        boolean changed = this.selected != selected;
        this.selected = selected;
        return changed;
    }

    public int getIndicatorSize() {
        return indicatorSize;
    }

    public void setIndicatorSize(int indicatorSize) {
        this.indicatorSize = Math.max(8, indicatorSize);
    }

    public int getTextGap() {
        return textGap;
    }

    public void setTextGap(int textGap) {
        this.textGap = Math.max(0, textGap);
    }

    public Color getIndicatorColor() {
        return indicatorColor;
    }

    public void setIndicatorColor(Color indicatorColor) {
        if (indicatorColor != null) {
            this.indicatorColor = indicatorColor;
        }
    }

    public Color getIndicatorBorderColor() {
        return indicatorBorderColor;
    }

    public void setIndicatorBorderColor(Color indicatorBorderColor) {
        if (indicatorBorderColor != null) {
            this.indicatorBorderColor = indicatorBorderColor;
        }
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        if (textColor != null) {
            this.textColor = textColor;
        }
    }

    public Font getToggleFont() {
        return toggleFont;
    }

    public void setToggleFont(Font toggleFont) {
        if (toggleFont != null) {
            this.toggleFont = toggleFont;
        }
    }

    public void setOnClick(Runnable onClick) {
        this.onClick = onClick;
    }

    public Runnable getOnClick() {
        return onClick;
    }

    public void addActionListener(ActionListener listener) {
        listeners.add(ActionListener.class, listener);
    }

    public void removeActionListener(ActionListener listener) {
        listeners.remove(ActionListener.class, listener);
    }

    public ActionListener[] getActionListeners() {
        return listeners.getListeners(ActionListener.class);
    }

    public void fireAction(String command) {
        if (onClick != null) {
            onClick.run();
        }
        ActionEvent event = new ActionEvent(source, ActionEvent.ACTION_PERFORMED, command);
        for (ActionListener listener : getActionListeners()) {
            listener.actionPerformed(event);
        }
    }
}
