package com.ShLookups;

import static shui.config.colors.BaseContainerColors.EMPTY_BG;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.function.Function;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import shui.components.base.BaseContainer;
import shui.contracts.visual.VisualState;

/**
 * Campo de consulta de solo lectura con el estilo de ShInput.
 * La busqueda se delega a la aplicacion mediante {@link #setOnSearch(Runnable)}.
 * Usar en el EDT, como los demas componentes Swing.
 *
 * <pre>{@code
 * ShLookup<Cliente> cliente = new ShLookup<>();
 * cliente.setHeaderText("Cliente");
 * cliente.setDisplayText(Cliente::getNombre);
 * cliente.setOnSearch(() -> cliente.setValue(buscarCliente()));
 * }</pre>
 *
 * @param <T> tipo del objeto seleccionado
 */
public class ShLookup<T> extends BaseContainer {

    public enum HeaderPosition {
        TOP_LEFT, TOP_CENTER, TOP_RIGHT, MIDDLE_LEFT, MIDDLE_RIGHT,
        BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT
    }

    private final JLabel headerLabel = new JLabel();
    private final JLabel valueLabel = new JLabel();
    private final JPanel fieldPanel = new JPanel(new BorderLayout());
    private final JButton searchButton = new SearchButton();
    private T value;
    private Function<? super T, String> displayText = item -> Objects.toString(item, "");
    private Runnable onSearch;
    private String text = "";
    private String placeholder = "";
    private String headerText = "";
    private HeaderPosition headerPosition = HeaderPosition.TOP_LEFT;
    private boolean headerVisible = true;
    private boolean required;
    private boolean autoValidate = true;
    private boolean showValidationState;
    private String validationMessage = "";
    private boolean inputBarVisible = true;
    private Color inputBarColor = new Color(153, 153, 153);
    private Color contentForeground = new Color(30, 30, 30);
    private Color placeholderColor = new Color(130, 130, 130);

    public ShLookup() {
        super(10, EMPTY_BG);
        setLayout(new BorderLayout());
        setBackground(EMPTY_BG);
        setBorderEnabled(false);
        setVisualState(VisualState.NONE);

        headerLabel.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
        headerLabel.setForeground(new Color(70, 70, 70));
        headerLabel.setLabelFor(searchButton);
        // Mostrar texto literal, incluso cuando el valor comienza con <html>.
        valueLabel.putClientProperty("html.disable", Boolean.TRUE);
        valueLabel.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        valueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        fieldPanel.setOpaque(false);
        fieldPanel.add(valueLabel, BorderLayout.CENTER);
        fieldPanel.add(searchButton, BorderLayout.EAST);

        searchButton.setPreferredSize(new Dimension(36, 32));
        searchButton.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        searchButton.setContentAreaFilled(false);
        searchButton.setOpaque(false);
        searchButton.setForeground(contentForeground);
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setToolTipText("Buscar");
        searchButton.getAccessibleContext().setAccessibleName("Buscar");
        searchButton.addActionListener(event -> doSearch());
        updateInputBarStyle();
        updateHeaderLayout();
        refreshDisplay();
    }

    public T getValue() {
        return value;
    }

    /** Asigna el objeto y actualiza su texto. Un valor null limpia el campo. */
    public void setValue(T value) {
        String nextText = value == null ? "" : displayText.apply(value);
        T previous = this.value;
        this.value = value;
        setText(nextText);
        if (autoValidate) {
            isValidInput();
        }
        firePropertyChange("value", previous, value);
    }

    /** Define como mostrar T; null restaura toString(). */
    public void setDisplayText(Function<? super T, String> displayText) {
        Function<? super T, String> next = displayText != null
                ? displayText : item -> Objects.toString(item, "");
        String nextText = value == null ? "" : next.apply(value);
        this.displayText = next;
        setText(nextText);
    }

    public Function<? super T, String> getDisplayText() {
        return displayText;
    }

    /** Cambia solo el texto visible, conservando el objeto seleccionado. */
    public void setText(String text) {
        String previous = this.text;
        this.text = text != null ? text : "";
        refreshDisplay();
        firePropertyChange("text", previous, this.text);
    }

    public String getText() {
        return text;
    }

    public void clear() {
        setValue(null);
    }

    public void setRequired(boolean required) {
        this.required = required;
        if (autoValidate) {
            isValidInput();
        }
    }

    public boolean isRequired() {
        return required;
    }

    /** Un lookup esta vacio cuando no tiene objeto seleccionado, sin importar su texto. */
    public boolean isEmpty() {
        return value == null;
    }

    /** Valida la seleccion y, si esta habilitado, actualiza su estado visual. */
    public boolean isValidInput() {
        boolean valid = !required || !isEmpty();
        validationMessage = valid ? "" : "El campo es obligatorio.";
        if (showValidationState) {
            setVisualState(valid ? VisualState.NONE : VisualState.ERROR);
        }
        setToolTipText(valid ? null : validationMessage);
        return valid;
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    public void setAutoValidate(boolean autoValidate) {
        this.autoValidate = autoValidate;
    }

    public boolean isAutoValidate() {
        return autoValidate;
    }

    public void setShowValidationState(boolean showValidationState) {
        this.showValidationState = showValidationState;
    }

    public boolean isShowValidationState() {
        return showValidationState;
    }

    public void setOnSearch(Runnable onSearch) {
        this.onSearch = onSearch;
    }

    public Runnable getOnSearch() {
        return onSearch;
    }

    /** Ejecuta la busqueda si el componente esta habilitado; no modifica el valor. */
    public void doSearch() {
        if (!isEnabled()) {
            return;
        }
        if (onSearch != null) {
            onSearch.run();
        }
        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "search");
        for (ActionListener listener : getActionListeners()) {
            listener.actionPerformed(event);
        }
    }

    public void addActionListener(ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
    }

    public void removeActionListener(ActionListener listener) {
        listenerList.remove(ActionListener.class, listener);
    }

    public ActionListener[] getActionListeners() {
        return listenerList.getListeners(ActionListener.class);
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder != null ? placeholder : "";
        refreshDisplay();
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText != null ? headerText : "";
        headerLabel.setText(this.headerText);
        updateHeaderLayout();
    }

    public String getHeaderText() {
        return headerText;
    }

    public void setHeaderPosition(HeaderPosition position) {
        headerPosition = position != null ? position : HeaderPosition.TOP_LEFT;
        updateHeaderLayout();
    }

    public HeaderPosition getHeaderPosition() {
        return headerPosition;
    }

    public void setHeaderVisible(boolean visible) {
        headerVisible = visible;
        updateHeaderLayout();
    }

    public boolean isHeaderVisible() {
        return headerVisible;
    }

    public void setHeaderFont(Font font) {
        if (font != null) {
            headerLabel.setFont(font);
            revalidate();
            repaint();
        }
    }

    public Font getHeaderFont() {
        return headerLabel.getFont();
    }

    public void setContentFont(Font font) {
        if (font != null) {
            valueLabel.setFont(font);
            revalidate();
            repaint();
        }
    }

    public Font getContentFont() {
        return valueLabel.getFont();
    }

    public void setHeaderForeground(Color color) {
        if (color != null) {
            headerLabel.setForeground(color);
        }
    }

    public Color getHeaderForeground() {
        return headerLabel.getForeground();
    }

    public void setContentForeground(Color color) {
        if (color != null) {
            contentForeground = color;
            searchButton.setForeground(color);
            refreshDisplay();
        }
    }

    public Color getContentForeground() {
        return contentForeground;
    }

    public void setPlaceholderColor(Color color) {
        if (color != null) {
            placeholderColor = color;
            refreshDisplay();
        }
    }

    public Color getPlaceholderColor() {
        return placeholderColor;
    }

    public void setInputBarVisible(boolean visible) {
        inputBarVisible = visible;
        updateInputBarStyle();
    }

    public boolean isInputBarVisible() {
        return inputBarVisible;
    }

    public void setInputBarColor(Color color) {
        if (color != null) {
            inputBarColor = color;
            updateInputBarStyle();
        }
    }

    public Color getInputBarColor() {
        return inputBarColor;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (searchButton != null) {
            searchButton.setEnabled(enabled);
            valueLabel.setEnabled(enabled);
            headerLabel.setEnabled(enabled);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet()) {
            return super.getPreferredSize();
        }
        Dimension size = super.getPreferredSize();
        return new Dimension(Math.max(180, size.width), size.height);
    }

    private void refreshDisplay() {
        valueLabel.setText(text.isEmpty() ? placeholder : text);
        valueLabel.setForeground(text.isEmpty() ? placeholderColor : contentForeground);
        revalidate();
        repaint();
    }

    private void updateInputBarStyle() {
        Color color = inputBarVisible ? inputBarColor : new Color(0, 0, 0, 0);
        fieldPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, color));
        revalidate();
        repaint();
    }

    private void updateHeaderLayout() {
        remove(headerLabel);
        boolean visible = headerVisible && !headerText.isBlank();
        headerLabel.setVisible(visible);
        if (visible) {
            headerLabel.setHorizontalAlignment(switch (headerPosition) {
                case TOP_CENTER, BOTTOM_CENTER -> SwingConstants.CENTER;
                case TOP_RIGHT, MIDDLE_RIGHT, BOTTOM_RIGHT -> SwingConstants.RIGHT;
                default -> SwingConstants.LEFT;
            });
            add(headerLabel, switch (headerPosition) {
                case TOP_LEFT, TOP_CENTER, TOP_RIGHT -> BorderLayout.NORTH;
                case MIDDLE_LEFT -> BorderLayout.WEST;
                case MIDDLE_RIGHT -> BorderLayout.EAST;
                default -> BorderLayout.SOUTH;
            });
        }
        add(fieldPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private static final class SearchButton extends JButton {

        private SearchButton() {
            super(new SearchIcon());
            setRolloverEnabled(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (isEnabled() && getModel().isRollover()) {
                Graphics2D g2 = (Graphics2D) g.create();
                try {
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    Color color = getForeground();
                    g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
                    g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 8, 8);
                } finally {
                    g2.dispose();
                }
            }
            super.paintComponent(g);
        }
    }

    private static final class SearchIcon implements Icon {

        @Override
        public int getIconWidth() {
            return 18;
        }

        @Override
        public int getIconHeight() {
            return 18;
        }

        @Override
        public void paintIcon(Component component, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            try {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(component.isEnabled() ? component.getForeground() : Color.GRAY);
                g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawOval(x + 2, y + 2, 9, 9);
                g2.drawLine(x + 10, y + 10, x + 16, y + 16);
            } finally {
                g2.dispose();
            }
        }
    }
}
