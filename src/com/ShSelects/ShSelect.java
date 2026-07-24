package com.ShSelects;

import shui.components.base.BaseContainer;
import static shui.config.colors.BaseContainerColors.EMPTY_BG;
import shui.contracts.select.SelectChangeHandler;
import shui.contracts.select.Selectable;
import shui.contracts.visual.VisualState;
import shui.delegates.select.SelectUIDelegate;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.MutableComboBoxModel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * Select Shui desacoplado de JComboBox, con popup propio y header configurable.
 *
 * @param <E> tipo de item
 */
public final class ShSelect<E> extends BaseContainer implements Selectable<E> {

    private final SelectUIDelegate uiDelegate = new SelectUIDelegate();
    private final EventListenerList listeners = new EventListenerList();
    private final JLabel valueLabel = new JLabel();
    private final JLabel arrowLabel = new JLabel("▼");
    private final JLabel headerLabel = new JLabel();
    private final JPanel fieldPanel = new JPanel(new BorderLayout());
    private final JPopupMenu popup = new JPopupMenu();
    private ComboBoxModel<E> model = new DefaultComboBoxModel<>();
    private final JList<E> list = new JList<>(model);
    private final JScrollPane scrollPane = new JScrollPane(list);
    private final ListDataListener modelListener = new ListDataListener() {
        @Override
        public void intervalAdded(ListDataEvent e) {
            modelChanged();
        }

        @Override
        public void intervalRemoved(ListDataEvent e) {
            modelChanged();
        }

        @Override
        public void contentsChanged(ListDataEvent e) {
            modelChanged();
        }
    };

    private String headerText = "";
    private HeaderPosition headerPosition = HeaderPosition.TOP_LEFT;
    private boolean headerVisible = true;
    private int maximumRowCount = 8;
    private Font contentFont = new Font("Segoe UI", Font.PLAIN, 13);
    private Runnable onChange;
    private SelectChangeHandler<E> onchangeHandler;

    public ShSelect() {
        super(8, EMPTY_BG);
        initUI();
    }

    public ShSelect(E[] items) {
        super(8, EMPTY_BG);
        initUI();
        setData(items != null ? Arrays.asList(items) : null);
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setFocusable(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorderEnabled(false);
        setVisualState(VisualState.NONE);

        configureHeader();
        configureFieldPanel();
        configureValueLabel();
        configureArrowLabel();
        configureList();
        configurePopup();
        model.addListDataListener(modelListener);
        installMouseHandler();
        updateHeaderLayout();
        refreshDisplay();
    }

    private void configureHeader() {
        headerLabel.setOpaque(false);
        headerLabel.setForeground(new Color(70, 70, 70));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
        headerLabel.setVisible(false);
    }

    private void configureFieldPanel() {
        fieldPanel.setOpaque(false);
        fieldPanel.add(valueLabel, BorderLayout.CENTER);
        fieldPanel.add(arrowLabel, BorderLayout.EAST);
    }

    private void configureValueLabel() {
        valueLabel.setOpaque(false);
        valueLabel.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 8));
        valueLabel.setHorizontalAlignment(SwingConstants.LEFT);
        valueLabel.setFont(contentFont);
    }

    private void configureArrowLabel() {
        arrowLabel.setOpaque(false);
        arrowLabel.setHorizontalAlignment(SwingConstants.CENTER);
        arrowLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 10));
    }

    private void configureList() {
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFixedCellHeight(28);
        list.setBorder(BorderFactory.createEmptyBorder());
        list.setFont(contentFont);
        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                if (isSelected) {
                    setBackground(uiDelegate.getSelectionBackgroundColor());
                    setForeground(uiDelegate.getSelectionForegroundColor());
                } else {
                    setBackground(uiDelegate.getPopupBackgroundColor());
                    setForeground(uiDelegate.getForegroundColor());
                }
                return this;
            }
        });
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int index = list.locationToIndex(e.getPoint());
                if (index >= 0) {
                    setSelectedIndex(index);
                    hidePopup();
                }
            }
        });
    }

    private void configurePopup() {
        popup.setBorder(BorderFactory.createEmptyBorder());
        popup.setLightWeightPopupEnabled(false);
        popup.setFocusable(false);
        popup.add(scrollPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        JScrollBar bar = scrollPane.getVerticalScrollBar();
        bar.setUI(new ModernScrollBarUI());
        bar.setPreferredSize(new Dimension(5, 5));
    }

    private void installMouseHandler() {
        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (isEnabled() && isClickInsideSelect(e)) {
                    requestFocusInWindow();
                    togglePopup();
                }
            }
        };
        addMouseListener(adapter);
        fieldPanel.addMouseListener(adapter);
        valueLabel.addMouseListener(adapter);
        arrowLabel.addMouseListener(adapter);
        headerLabel.addMouseListener(adapter);
    }

    @Override
    public void setData(List<E> items) {
        DefaultComboBoxModel<E> nextModel = new DefaultComboBoxModel<>();
        if (items != null) {
            for (E item : items) {
                nextModel.addElement(item);
            }
        }
        if (nextModel.getSize() > 0) {
            nextModel.setSelectedItem(nextModel.getElementAt(0));
        } else {
            nextModel.setSelectedItem(null);
        }
        setModel(nextModel);
    }

    @Override
    public List<E> getData() {
        List<E> items = new ArrayList<>();
        for (int i = 0; i < model.getSize(); i++) {
            items.add(model.getElementAt(i));
        }
        return List.copyOf(items);
    }

    public void setModel(ComboBoxModel<E> model) {
        ComboBoxModel<E> nextModel = model != null ? model : new DefaultComboBoxModel<>();
        this.model.removeListDataListener(modelListener);
        this.model = nextModel;
        this.model.addListDataListener(modelListener);
        list.setModel(this.model);
        list.setSelectedIndex(getSelectedIndex());
        refreshDisplay();
    }

    public ComboBoxModel<E> getModel() {
        return model;
    }

    @SuppressWarnings("unchecked")
    public void setItems(String items) {
        if (items == null || items.isBlank()) {
            setData(null);
            return;
        }

        DefaultComboBoxModel<E> model = new DefaultComboBoxModel<>();
        for (String item : items.split(",")) {
            String value = item.trim();
            if (!value.isEmpty()) {
                model.addElement((E) value);
            }
        }
        setModel(model);
    }

    public String getItems() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < model.getSize(); i++) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(String.valueOf(model.getElementAt(i)));
        }
        return builder.toString();
    }

    @Override
    public void setSelectedValue(E value) {
        Object old = model.getSelectedItem();
        model.setSelectedItem(value);
        list.setSelectedIndex(getSelectedIndex());
        refreshDisplay();
        if (!Objects.equals(old, value)) {
            fireActionEvent();
        }
    }

    @Override
    public E getSelectedValue() {
        @SuppressWarnings("unchecked")
        E selected = (E) model.getSelectedItem();
        return selected;
    }

    public E getSelectedItem() {
        return getSelectedValue();
    }

    public void setSelectedIndex(int index) {
        E value = index >= 0 && index < model.getSize() ? model.getElementAt(index) : null;
        setSelectedValue(value);
    }

    public int getSelectedIndex() {
        return indexOf(model.getSelectedItem());
    }

    @Override
    public void clearSelection() {
        setSelectedValue(null);
    }

    @Override
    public void setButtonColor(Color color) {
        uiDelegate.setButtonColor(color);
        refreshDisplay();
    }

    @Override
    public Color getButtonColor() {
        return uiDelegate.getButtonColor();
    }

    @Override
    public void setSelectBackgroundColor(Color color) {
        uiDelegate.setBackgroundColor(color);
        refreshDisplay();
    }

    @Override
    public Color getSelectBackgroundColor() {
        return uiDelegate.getBackgroundColor();
    }

    @Override
    public void setSelectForegroundColor(Color color) {
        uiDelegate.setForegroundColor(color);
        refreshDisplay();
    }

    @Override
    public Color getSelectForegroundColor() {
        return uiDelegate.getForegroundColor();
    }

    @Override
    public void setSelectionBackgroundColor(Color color) {
        uiDelegate.setSelectionBackgroundColor(color);
        list.repaint();
    }

    @Override
    public Color getSelectionBackgroundColor() {
        return uiDelegate.getSelectionBackgroundColor();
    }

    @Override
    public void setSelectionForegroundColor(Color color) {
        uiDelegate.setSelectionForegroundColor(color);
        list.repaint();
    }

    @Override
    public Color getSelectionForegroundColor() {
        return uiDelegate.getSelectionForegroundColor();
    }

    @Override
    public void setPopupBackgroundColor(Color color) {
        uiDelegate.setPopupBackgroundColor(color);
        refreshPopupStyle();
    }

    @Override
    public Color getPopupBackgroundColor() {
        return uiDelegate.getPopupBackgroundColor();
    }

    @Override
    public void setIconSize(int iconSize) {
        uiDelegate.setIconSize(iconSize);
        arrowLabel.setFont(arrowLabel.getFont().deriveFont((float) uiDelegate.getIconSize()));
        revalidate();
        repaint();
    }

    @Override
    public int getIconSize() {
        return uiDelegate.getIconSize();
    }

    @Override
    public void setPlaceholder(String placeholder) {
        uiDelegate.setPlaceholder(placeholder);
        refreshDisplay();
    }

    @Override
    public String getPlaceholder() {
        return uiDelegate.getPlaceholder();
    }

    @Override
    public void setPopupBorderVisible(boolean visible) {
        uiDelegate.setPopupBorderVisible(visible);
        refreshPopupStyle();
    }

    @Override
    public boolean isPopupBorderVisible() {
        return uiDelegate.isPopupBorderVisible();
    }

    public void setMaximumRowCount(int maximumRowCount) {
        this.maximumRowCount = Math.max(1, maximumRowCount);
        updatePopupSize();
    }

    public int getMaximumRowCount() {
        return maximumRowCount;
    }

    @Override
    public void setHeaderText(String headerText) {
        this.headerText = headerText != null ? headerText : "";
        headerLabel.setText(this.headerText);
        updateHeaderLayout();
    }

    @Override
    public String getHeaderText() {
        return headerText;
    }

    @Override
    public void setHeaderPosition(HeaderPosition position) {
        this.headerPosition = position != null ? position : HeaderPosition.TOP_LEFT;
        updateHeaderLayout();
    }

    @Override
    public HeaderPosition getHeaderPosition() {
        return headerPosition;
    }

    @Override
    public void setHeaderVisible(boolean visible) {
        this.headerVisible = visible;
        updateHeaderLayout();
    }

    @Override
    public boolean isHeaderVisible() {
        return headerVisible;
    }

    @Override
    public void setHeaderForeground(Color color) {
        if (color == null) {
            return;
        }
        headerLabel.setForeground(color);
        repaint();
    }

    @Override
    public Color getHeaderForeground() {
        return headerLabel.getForeground();
    }

    @Override
    public void setHeaderFont(Font font) {
        if (font == null) {
            return;
        }
        headerLabel.setFont(font);
        revalidate();
        repaint();
    }

    @Override
    public Font getHeaderFont() {
        return headerLabel.getFont();
    }

    @Override
    public void setContentFont(Font font) {
        if (font == null) {
            return;
        }
        this.contentFont = font;
        valueLabel.setFont(this.contentFont);
        list.setFont(this.contentFont);
        revalidate();
        repaint();
    }

    @Override
    public Font getContentFont() {
        return contentFont;
    }

    @Override
    public void setOnChange(Runnable onChange) {
        this.onChange = onChange;
    }

    @Override
    public Runnable getOnChange() {
        return onChange;
    }

    @Override
    public void setOnchangeHandler(SelectChangeHandler<E> onchangeHandler) {
        this.onchangeHandler = onchangeHandler;
    }

    @Override
    public SelectChangeHandler<E> getOnchangeHandler() {
        return onchangeHandler;
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

    public void showPopup() {
        if (!isEnabled() || popup.isVisible()) {
            return;
        }
        updatePopupSize();
        refreshPopupStyle();
        popup.revalidate();
        popup.pack();
        popup.show(this, 0, getHeight());
    }

    public void hidePopup() {
        popup.setVisible(false);
    }

    public boolean isPopupVisible() {
        return popup.isVisible();
    }

    private void togglePopup() {
        if (popup.isVisible()) {
            hidePopup();
        } else {
            showPopup();
        }
    }

    private void refreshDisplay() {
        setBackgroundColor(uiDelegate.getBackgroundColor());
        valueLabel.setForeground(model.getSelectedItem() == null
                ? new Color(130, 130, 130)
                : uiDelegate.getForegroundColor());
        arrowLabel.setForeground(uiDelegate.getButtonColor());
        arrowLabel.setFont(arrowLabel.getFont().deriveFont((float) uiDelegate.getIconSize()));

        Object selected = model.getSelectedItem();
        if (selected != null) {
            valueLabel.setText(String.valueOf(selected));
        } else {
            valueLabel.setText(uiDelegate.getPlaceholder());
        }
        refreshPopupStyle();
        revalidate();
        repaint();
    }

    private void refreshPopupStyle() {
        popup.setBackground(uiDelegate.getPopupBackgroundColor());
        popup.setBorder(uiDelegate.isPopupBorderVisible()
                ? new LineBorder(uiDelegate.getSelectionBackgroundColor(), 1)
                : BorderFactory.createEmptyBorder());
        list.setBackground(uiDelegate.getPopupBackgroundColor());
        scrollPane.getViewport().setBackground(uiDelegate.getPopupBackgroundColor());
        list.repaint();
    }

    private void updatePopupSize() {
        int rows = Math.min(Math.max(1, model.getSize()), maximumRowCount);
        int height = rows * list.getFixedCellHeight();
        int width = Math.max(Math.max(getWidth(), getPreferredSize().width), 120);
        list.setVisibleRowCount(rows);
        scrollPane.setPreferredSize(new Dimension(width, height));
    }

    private void modelChanged() {
        list.setSelectedIndex(getSelectedIndex());
        refreshDisplay();
        updatePopupSize();
    }

    private int indexOf(Object value) {
        for (int i = 0; i < model.getSize(); i++) {
            if (Objects.equals(model.getElementAt(i), value)) {
                return i;
            }
        }
        return -1;
    }

    public void addItem(E item) {
        mutableModel().addElement(item);
    }

    public void insertItemAt(E item, int index) {
        mutableModel().insertElementAt(item, Math.max(0, Math.min(index, model.getSize())));
    }

    public void removeItem(Object item) {
        mutableModel().removeElement(item);
    }

    public void removeItemAt(int index) {
        if (index >= 0 && index < model.getSize()) {
            mutableModel().removeElementAt(index);
        }
    }

    public void removeAllItems() {
        MutableComboBoxModel<E> mutable = mutableModel();
        while (model.getSize() > 0) {
            mutable.removeElementAt(0);
        }
    }

    public int getItemCount() {
        return model.getSize();
    }

    public E getItemAt(int index) {
        return index >= 0 && index < model.getSize() ? model.getElementAt(index) : null;
    }

    @SuppressWarnings("unchecked")
    private MutableComboBoxModel<E> mutableModel() {
        if (model instanceof MutableComboBoxModel<?> mutable) {
            return (MutableComboBoxModel<E>) mutable;
        }

        DefaultComboBoxModel<E> mutable = new DefaultComboBoxModel<>();
        for (int i = 0; i < model.getSize(); i++) {
            mutable.addElement(model.getElementAt(i));
        }
        mutable.setSelectedItem(model.getSelectedItem());
        setModel(mutable);
        return mutable;
    }

    private boolean isClickInsideSelect(MouseEvent event) {
        Component source = event.getComponent();
        if (source == this) {
            return contains(event.getPoint());
        }
        Point point = SwingUtilities.convertPoint(source, event.getPoint(), this);
        return contains(point);
    }

    private void updateHeaderLayout() {
        remove(headerLabel);
        remove(fieldPanel);

        boolean showHeader = headerVisible && !headerText.isBlank();
        headerLabel.setVisible(showHeader);
        if (showHeader) {
            add(headerLabel, headerConstraint());
        }
        add(fieldPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private String headerConstraint() {
        return switch (headerPosition) {
            case TOP_LEFT -> {
                headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
                yield BorderLayout.NORTH;
            }
            case TOP_CENTER -> {
                headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
                yield BorderLayout.NORTH;
            }
            case TOP_RIGHT -> {
                headerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
                yield BorderLayout.NORTH;
            }
            case MIDDLE_LEFT -> {
                headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
                yield BorderLayout.WEST;
            }
            case MIDDLE_RIGHT -> {
                headerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
                yield BorderLayout.EAST;
            }
            case BOTTOM_CENTER -> {
                headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
                yield BorderLayout.SOUTH;
            }
            case BOTTOM_RIGHT -> {
                headerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
                yield BorderLayout.SOUTH;
            }
            default -> {
                headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
                yield BorderLayout.SOUTH;
            }
        };
    }

    private void fireActionEvent() {
        if (onchangeHandler != null) {
            onchangeHandler.onChange(getSelectedValue());
        }
        if (onChange != null) {
            onChange.run();
        }
        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "selectionChanged");
        for (ActionListener listener : getActionListeners()) {
            listener.actionPerformed(event);
        }
    }

    private static class ModernScrollBarUI extends BasicScrollBarUI {

        @Override
        protected void configureScrollBarColors() {
            thumbColor = new Color(160, 160, 160);
            trackColor = new Color(245, 245, 245);
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected void paintThumb(Graphics g, javax.swing.JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(thumbColor);
            g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 6, 6);
            g2.dispose();
        }

        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }
    }
}
