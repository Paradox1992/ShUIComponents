package com.ShChoices;

import shui.components.base.BaseContainer;
import static shui.config.colors.BaseContainerColors.EMPTY_BG;
import shui.contracts.choice.Toggleable;
import shui.contracts.visual.VisualState;
import shui.delegates.choice.ToggleHeaderDelegate;
import shui.delegates.choice.ToggleStateDelegate;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * CheckBox Shui desacoplado de JCheckBox.
 */
public class ShCheckBox extends BaseContainer implements Toggleable {

    private final ToggleStateDelegate toggleDelegate = new ToggleStateDelegate(this);
    private final ToggleHeaderDelegate headerDelegate = new ToggleHeaderDelegate();

    public ShCheckBox() {
        super(4, EMPTY_BG);
        initUI();
    }

    private void initUI() {
        setOpaque(false);
        setFocusable(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorderEnabled(false);
        setVisualState(VisualState.DEFAULT);
        setText("CheckBox");
        installHandlers();
    }

    private void installHandlers() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (isEnabled() && contains(e.getPoint())) {
                    doClick();
                }
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (isEnabled() && (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER)) {
                    doClick();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int size = toggleDelegate.getIndicatorSize();
        HeaderMetrics header = buildHeaderMetrics(g2);
        int x = getInsets().left + 4 + header.leftWidth;
        int y = header.contentTop + ((header.contentHeight - size) / 2);

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(x, y, size, size, 4, 4);
        g2.setStroke(new BasicStroke(1.4f));
        g2.setColor(toggleDelegate.isSelected()
                ? toggleDelegate.getIndicatorColor()
                : toggleDelegate.getIndicatorBorderColor());
        g2.drawRoundRect(x, y, size, size, 4, 4);

        if (toggleDelegate.isSelected()) {
            g2.setStroke(new BasicStroke(Math.max(2f, size / 8f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int x1 = x + size / 4;
            int y1 = y + size / 2;
            int x2 = x + size / 2;
            int y2 = y + size - size / 4;
            int x3 = x + size - size / 5;
            int y3 = y + size / 4;
            g2.drawLine(x1, y1, x2, y2);
            g2.drawLine(x2, y2, x3, y3);
        }

        int textX = x + size + toggleDelegate.getTextGap();
        int textEnd = paintText(g2, textX, header);
        paintHeader(g2, header, textEnd + toggleDelegate.getTextGap());
        g2.dispose();
    }

    private int paintText(Graphics2D g2, int x, HeaderMetrics header) {
        g2.setFont(toggleDelegate.getToggleFont());
        g2.setColor(isEnabled() ? toggleDelegate.getTextColor() : new Color(140, 140, 140));
        FontMetrics metrics = g2.getFontMetrics();
        int y = header.contentTop + ((header.contentHeight - metrics.getHeight()) / 2) + metrics.getAscent();
        g2.drawString(toggleDelegate.getText(), x, y);
        return x + metrics.stringWidth(toggleDelegate.getText());
    }

    @Override
    public Dimension getPreferredSize() {
        FontMetrics metrics = getFontMetrics(toggleDelegate.getToggleFont());
        FontMetrics headerMetrics = getFontMetrics(headerDelegate.getHeaderFont());
        int headerWidth = headerDelegate.hasHeader() ? headerMetrics.stringWidth(headerDelegate.getHeaderText()) + 8 : 0;
        int headerHeight = headerDelegate.hasHeader() ? headerMetrics.getHeight() + 4 : 0;
        int width = toggleDelegate.getIndicatorSize() + toggleDelegate.getTextGap()
                + metrics.stringWidth(toggleDelegate.getText()) + 14;
        if (isMiddleHeader()) {
            width += headerWidth;
        } else {
            width = Math.max(width, headerWidth + 14);
        }
        int height = Math.max(toggleDelegate.getIndicatorSize(), metrics.getHeight()) + 8;
        if (isTopOrBottomHeader()) {
            height += headerHeight;
        }
        return new Dimension(width, height);
    }

    @Override
    public void setText(String text) {
        toggleDelegate.setText(text);
        revalidate();
        repaint();
    }

    @Override
    public String getText() {
        return toggleDelegate.getText();
    }

    @Override
    public void setSelected(boolean selected) {
        if (toggleDelegate.setSelected(selected)) {
            repaint();
        }
    }

    @Override
    public boolean isSelected() {
        return toggleDelegate.isSelected();
    }

    @Override
    public void toggle() {
        setSelected(!isSelected());
    }

    @Override
    public void doClick() {
        toggle();
        toggleDelegate.fireAction(getText());
    }

    @Override
    public void setIndicatorSize(int size) {
        toggleDelegate.setIndicatorSize(size);
        revalidate();
        repaint();
    }

    @Override
    public int getIndicatorSize() {
        return toggleDelegate.getIndicatorSize();
    }

    @Override
    public void setIndicatorColor(Color color) {
        toggleDelegate.setIndicatorColor(color);
        repaint();
    }

    @Override
    public Color getIndicatorColor() {
        return toggleDelegate.getIndicatorColor();
    }

    @Override
    public void setIndicatorBorderColor(Color color) {
        toggleDelegate.setIndicatorBorderColor(color);
        repaint();
    }

    @Override
    public Color getIndicatorBorderColor() {
        return toggleDelegate.getIndicatorBorderColor();
    }

    @Override
    public void setTextColor(Color color) {
        toggleDelegate.setTextColor(color);
        repaint();
    }

    @Override
    public Color getTextColor() {
        return toggleDelegate.getTextColor();
    }

    @Override
    public void setToggleFont(Font font) {
        toggleDelegate.setToggleFont(font);
        revalidate();
        repaint();
    }

    @Override
    public Font getToggleFont() {
        return toggleDelegate.getToggleFont();
    }

    @Override
    public void setTextGap(int gap) {
        toggleDelegate.setTextGap(gap);
        revalidate();
        repaint();
    }

    @Override
    public int getTextGap() {
        return toggleDelegate.getTextGap();
    }

    public void setOnClick(Runnable onClick) {
        toggleDelegate.setOnClick(onClick);
    }

    public Runnable getOnClick() {
        return toggleDelegate.getOnClick();
    }

    public void addActionListener(ActionListener listener) {
        toggleDelegate.addActionListener(listener);
    }

    public void removeActionListener(ActionListener listener) {
        toggleDelegate.removeActionListener(listener);
    }

    public ActionListener[] getActionListeners() {
        return toggleDelegate.getActionListeners();
    }

    @Override
    public void setHeaderText(String headerText) {
        headerDelegate.setHeaderText(headerText);
        revalidate();
        repaint();
    }

    @Override
    public String getHeaderText() {
        return headerDelegate.getHeaderText();
    }

    @Override
    public void setHeaderPosition(HeaderPosition position) {
        headerDelegate.setHeaderPosition(position);
        revalidate();
        repaint();
    }

    @Override
    public HeaderPosition getHeaderPosition() {
        return headerDelegate.getHeaderPosition();
    }

    @Override
    public void setHeaderVisible(boolean visible) {
        headerDelegate.setHeaderVisible(visible);
        revalidate();
        repaint();
    }

    @Override
    public boolean isHeaderVisible() {
        return headerDelegate.isHeaderVisible();
    }

    @Override
    public void setHeaderForeground(Color color) {
        headerDelegate.setHeaderForeground(color);
        repaint();
    }

    @Override
    public Color getHeaderForeground() {
        return headerDelegate.getHeaderForeground();
    }

    @Override
    public void setHeaderFont(Font font) {
        headerDelegate.setHeaderFont(font);
        revalidate();
        repaint();
    }

    @Override
    public Font getHeaderFont() {
        return headerDelegate.getHeaderFont();
    }

    private HeaderMetrics buildHeaderMetrics(Graphics2D g2) {
        HeaderMetrics metrics = new HeaderMetrics();
        metrics.contentTop = 0;
        metrics.contentHeight = getHeight();
        if (!headerDelegate.hasHeader()) {
            return metrics;
        }

        FontMetrics fm = g2.getFontMetrics(headerDelegate.getHeaderFont());
        int height = fm.getHeight() + 4;
        switch (headerDelegate.getHeaderPosition()) {
            case TOP_LEFT, TOP_CENTER, TOP_RIGHT -> {
                metrics.topHeight = height;
                metrics.contentTop = height;
                metrics.contentHeight = Math.max(0, getHeight() - height);
            }
            case BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT -> {
                metrics.bottomHeight = height;
                metrics.contentHeight = Math.max(0, getHeight() - height);
            }
            case MIDDLE_LEFT -> metrics.leftWidth = fm.stringWidth(headerDelegate.getHeaderText()) + 8;
            case MIDDLE_RIGHT -> metrics.rightWidth = fm.stringWidth(headerDelegate.getHeaderText()) + 8;
            default -> {
            }
        }
        return metrics;
    }

    private void paintHeader(Graphics2D g2, HeaderMetrics metrics, int middleRightX) {
        if (!headerDelegate.hasHeader()) {
            return;
        }
        g2.setFont(headerDelegate.getHeaderFont());
        g2.setColor(headerDelegate.getHeaderForeground());
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(headerDelegate.getHeaderText());
        int x;
        int y;
        switch (headerDelegate.getHeaderPosition()) {
            case TOP_CENTER -> {
                x = (getWidth() - textWidth) / 2;
                y = fm.getAscent() + 2;
            }
            case TOP_RIGHT -> {
                x = getWidth() - textWidth - 8;
                y = fm.getAscent() + 2;
            }
            case BOTTOM_CENTER -> {
                x = (getWidth() - textWidth) / 2;
                y = getHeight() - fm.getDescent() - 2;
            }
            case BOTTOM_RIGHT -> {
                x = getWidth() - textWidth - 8;
                y = getHeight() - fm.getDescent() - 2;
            }
            case MIDDLE_LEFT -> {
                x = 4;
                y = metrics.contentTop + ((metrics.contentHeight - fm.getHeight()) / 2) + fm.getAscent();
            }
            case MIDDLE_RIGHT -> {
                x = middleRightX;
                y = metrics.contentTop + ((metrics.contentHeight - fm.getHeight()) / 2) + fm.getAscent();
            }
            case BOTTOM_LEFT -> {
                x = 8;
                y = getHeight() - fm.getDescent() - 2;
            }
            default -> {
                x = 8;
                y = fm.getAscent() + 2;
            }
        }
        g2.drawString(headerDelegate.getHeaderText(), x, y);
    }

    private boolean isMiddleHeader() {
        return headerDelegate.hasHeader()
                && (headerDelegate.getHeaderPosition() == HeaderPosition.MIDDLE_LEFT
                || headerDelegate.getHeaderPosition() == HeaderPosition.MIDDLE_RIGHT);
    }

    private boolean isTopOrBottomHeader() {
        return headerDelegate.hasHeader() && !isMiddleHeader();
    }

    private static class HeaderMetrics {

        int contentTop;
        int contentHeight;
        int topHeight;
        int bottomHeight;
        int leftWidth;
        int rightWidth;
    }
}
