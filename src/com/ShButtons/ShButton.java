package com.ShButtons;

import shui.components.base.BaseContainer;
import static shui.config.colors.BaseContainerColors.EMPTY_BG;
import shui.contracts.button.Buttonable;
import shui.contracts.visual.VisualState;
import shui.delegates.button.ButtonActionDelegate;
import shui.delegates.button.ButtonStyleDelegate;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * Boton Shui basado en BaseContainer, no en JButton.
 */
public final class ShButton extends BaseContainer implements Buttonable {

    private final JLabel contentLabel = new JLabel();
    private final ButtonStyleDelegate styleDelegate = new ButtonStyleDelegate();
    private final ButtonActionDelegate actionDelegate = new ButtonActionDelegate(this);

    private String buttonText = "";

    public ShButton() {
        super(8, EMPTY_BG);
        setLayout(new BorderLayout());
        setOpaque(false);
        setFocusable(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorderEnabled(false);
        setVisualState(VisualState.DEFAULT);
        configureContentLabel();
        add(contentLabel, BorderLayout.CENTER);
        setButtonType(ButtonType.CUSTOM);
    }

    private void configureContentLabel() {
        contentLabel.setOpaque(false);
        contentLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentLabel.setVerticalAlignment(SwingConstants.CENTER);
        contentLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        contentLabel.setVerticalTextPosition(SwingConstants.CENTER);
        contentLabel.setIconTextGap(8);
        contentLabel.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        contentLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }

    @Override
    public void setButtonText(String text) {
        this.buttonText = text != null ? text : "";
        refreshStyle(false);
    }

    @Override
    public String getButtonText() {
        return buttonText;
    }

    @Override
    public void setButtonType(ButtonType type) {
        styleDelegate.setButtonType(type);
        refreshStyle(true);
    }

    @Override
    public ButtonType getButtonType() {
        return styleDelegate.getButtonType();
    }

    @Override
    public void setActionButton(ActionButton action) {
        styleDelegate.setActionButton(action);
        refreshStyle(true);
    }

    @Override
    public ActionButton getActionButton() {
        return styleDelegate.getActionButton();
    }

    @Override
    public void setBootstrapButton(BootstrapButton button) {
        styleDelegate.setBootstrapButton(button);
        refreshStyle(true);
    }

    @Override
    public BootstrapButton getBootstrapButton() {
        return styleDelegate.getBootstrapButton();
    }

    @Override
    public void setIconSize(ButtonIconSize size) {
        styleDelegate.setIconSize(size);
        refreshStyle(false);
    }

    @Override
    public ButtonIconSize getIconSize() {
        return styleDelegate.getIconSize();
    }

    @Override
    public void setCustomIcon(Icon icon) {
        styleDelegate.setCustomIcon(icon);
        refreshStyle(false);
    }

    @Override
    public Icon getCustomIcon() {
        return styleDelegate.getCustomIcon();
    }

    @Override
    public void setCustomColor(Color color) {
        styleDelegate.setCustomColor(color);
        refreshStyle(false);
    }

    @Override
    public Color getCustomColor() {
        return styleDelegate.getCustomColor();
    }

    @Override
    public void setCustomForeground(Color color) {
        styleDelegate.setCustomForeground(color);
        refreshStyle(false);
    }

    @Override
    public Color getCustomForeground() {
        return styleDelegate.getCustomForeground();
    }

    public void setText(String text) {
        setButtonText(text);
    }

    public String getText() {
        return getButtonText();
    }

    public void setButtonFont(Font font) {
        if (font == null) {
            return;
        }
        contentLabel.setFont(font);
        revalidate();
        repaint();
    }

    public Font getButtonFont() {
        return contentLabel.getFont();
    }

    public void setIconTextGap(int gap) {
        contentLabel.setIconTextGap(Math.max(0, gap));
        revalidate();
        repaint();
    }

    public int getIconTextGap() {
        return contentLabel.getIconTextGap();
    }

    public void setOnClick(Runnable onClick) {
        actionDelegate.setOnClick(onClick);
    }

    public Runnable getOnClick() {
        return actionDelegate.getOnClick();
    }

    public void addActionListener(ActionListener listener) {
        actionDelegate.addActionListener(listener);
    }

    public void removeActionListener(ActionListener listener) {
        actionDelegate.removeActionListener(listener);
    }

    public ActionListener[] getActionListeners() {
        return actionDelegate.getActionListeners();
    }

    @Override
    public void doClick() {
        actionDelegate.fireAction(buttonText);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        contentLabel.setEnabled(enabled);
        setAlpha(enabled ? 1f : 0.55f);
        setCursor(enabled
                ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
                : Cursor.getDefaultCursor());
    }

    private void refreshStyle(boolean replaceEmptyText) {
        Color background = styleDelegate.resolveBackground();
        setBackgroundColor(background);
        setHoverColor(adjust(background, 1.12f));
        setStateOverlayColor(VisualState.PRESSED, new Color(0, 0, 0, 30));

        contentLabel.setForeground(styleDelegate.resolveForeground());
        contentLabel.setIcon(styleDelegate.resolveIcon());
        if (replaceEmptyText && buttonText.isBlank()) {
            buttonText = styleDelegate.defaultText();
        }
        contentLabel.setText(toDisplayText(buttonText));
        revalidate();
        repaint();
    }

    private String toDisplayText(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        String normalized = text.replace("\\r\\n", "\n")
                .replace("\\n", "\n")
                .replace("\r\n", "\n")
                .replace('\r', '\n');
        if (!normalized.contains("\n")) {
            return text;
        }
        return "<html><div style='text-align:center;'>"
                + escapeHtml(normalized).replace("\n", "<br>")
                + "</div></html>";
    }

    private String escapeHtml(String text) {
        StringBuilder escaped = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            switch (ch) {
                case '&' ->
                    escaped.append("&amp;");
                case '<' ->
                    escaped.append("&lt;");
                case '>' ->
                    escaped.append("&gt;");
                case '"' ->
                    escaped.append("&quot;");
                default ->
                    escaped.append(ch);
            }
        }
        return escaped.toString();
    }

    private Color adjust(Color color, float factor) {
        int r = Math.min(255, Math.max(0, Math.round(color.getRed() * factor)));
        int g = Math.min(255, Math.max(0, Math.round(color.getGreen() * factor)));
        int b = Math.min(255, Math.max(0, Math.round(color.getBlue() * factor)));
        return new Color(r, g, b, color.getAlpha());
    }
}
