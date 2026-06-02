package com.ShInputs;

import static shui.config.colors.BaseContainerColors.EMPTY_BG;
import shui.components.base.BaseContainer;
import shui.contracts.text.Inputable;
import shui.contracts.visual.VisualState;
import shui.delegates.text.InputFilterDelegate;
import shui.delegates.text.InputValidationDelegate;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.time.LocalDate;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

/**
 * Componente de entrada con tipos de texto, password, descripcion y fecha.
 */
public class ShInput extends BaseContainer implements Inputable {

    private static final String CARD_TEXT = "TEXT";
    private static final String CARD_PASSWORD = "PASSWORD";
    private static final String CARD_DESCRIPTION = "DESCRIPTION";
    private static final String CARD_DATE = "DATE";

    private final CardLayout cardLayout = new CardLayout();
    private final JComponent editorHost = new JComponent() {
    };
    private final PlaceholderTextField textField = new PlaceholderTextField();
    private final PlaceholderPasswordField passwordField = new PlaceholderPasswordField();
    private final PlaceholderTextArea textArea = new PlaceholderTextArea();
    private final PlaceholderTextField dateField = new PlaceholderTextField();
    private final JScrollPane textAreaScroll = new JScrollPane(textArea);
    private final JLabel headerLabel = new JLabel();
    private final InputFilterDelegate filterDelegate = new InputFilterDelegate();
    private final InputValidationDelegate validationDelegate = new InputValidationDelegate();

    private InputType inputType = InputType.TEXT;
    private String placeholder = "";
    private String headerText = "";
    private HeaderPosition headerPosition = HeaderPosition.TOP_LEFT;
    private boolean headerVisible = false;
    private boolean autoValidate = true;
    private boolean showValidationState = false;
    private boolean inputBarVisible = true;
    private Color inputBarColor = new Color(153, 153, 153);
    private boolean updatingText;
    private Font textFont = new Font("Segoe UI", 0, 13);

    public ShInput() {
        super(10, EMPTY_BG);
        setLayout(new BorderLayout());
        setBackground(EMPTY_BG);
        setBorderEnabled(false);
        setVisualState(VisualState.NONE);
        buildHeader();
        buildEditors();
        updateInputBarStyle();
        updateHeaderLayout();
        setInputType(InputType.TEXT);
    }

    private void buildHeader() {
        headerLabel.setOpaque(false);
        headerLabel.setForeground(new Color(70, 70, 70));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
        headerLabel.setVisible(false);
    }

    private void buildEditors() {
        editorHost.setLayout(cardLayout);
        editorHost.setOpaque(false);

        configureTextComponent(textField);
        configureTextComponent(passwordField);
        configureTextComponent(textArea);
        configureTextComponent(dateField);

        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textAreaScroll.setOpaque(false);
        textAreaScroll.setBorder(BorderFactory.createEmptyBorder());
        textAreaScroll.getViewport().setOpaque(false);

        editorHost.add(textField, CARD_TEXT);
        editorHost.add(passwordField, CARD_PASSWORD);
        editorHost.add(textAreaScroll, CARD_DESCRIPTION);
        editorHost.add(dateField, CARD_DATE);
    }

    private void configureTextComponent(JTextComponent component) {
        component.setOpaque(false);
        component.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        component.setForeground(new Color(30, 30, 30));
        component.setCaretColor(new Color(30, 30, 30));
        component.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                textChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                textChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                textChanged();
            }
        });
        filterDelegate.install(component);
    }

    private void textChanged() {
        if (!updatingText && autoValidate) {
            isValidInput();
        }
    }

    @Override
    public void setInputType(InputType type) {
        InputType next = type != null ? type : InputType.TEXT;
        String current = getText();
        this.inputType = next;
        switch (next) {
            case PASSWORD ->
                cardLayout.show(editorHost, CARD_PASSWORD);
            case DESCRIPTION ->
                cardLayout.show(editorHost, CARD_DESCRIPTION);
            case DATE ->
                cardLayout.show(editorHost, CARD_DATE);
            default ->
                cardLayout.show(editorHost, CARD_TEXT);
        }
        setText(current);
        revalidate();
        repaint();
    }

    @Override
    public InputType getInputType() {
        return inputType;
    }

    @Override
    public void setInputDataType(InputDataType type) {
        filterDelegate.setDataType(type);
        setText(filterDelegate.sanitize(getText()));
    }

    @Override
    public InputDataType getInputDataType() {
        return filterDelegate.getDataType();
    }

    @Override
    public void setText(String text) {
        updatingText = true;
        try {
            activeEditor().setText(filterDelegate.sanitize(text));
        } finally {
            updatingText = false;
        }
        if (autoValidate) {
            isValidInput();
        }
    }

    @Override
    public String getText() {
        JTextComponent editor = activeEditor();
        if (editor instanceof JPasswordField field) {
            return new String(field.getPassword());
        }
        return editor.getText();
    }

    @Override
    public void clear() {
        setText("");
    }

    @Override
    public void selectAllText() {
        activeEditor().selectAll();
        activeEditor().requestFocusInWindow();
    }

    @Override
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder != null ? placeholder : "";
        textField.setPlaceholder(this.placeholder);
        passwordField.setPlaceholder(this.placeholder);
        textArea.setPlaceholder(this.placeholder);
        dateField.setPlaceholder(this.placeholder);
        repaint();
    }

    @Override
    public String getPlaceholder() {
        return placeholder;
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

    public void setHeaderForeground(Color color) {
        if (color == null) {
            return;
        }
        headerLabel.setForeground(color);
        repaint();
    }

    public Color getHeaderForeground() {
        return headerLabel.getForeground();
    }

    public void setHeaderFont(Font font) {
        if (font == null) {
            return;
        }
        headerLabel.setFont(font);
        revalidate();
        repaint();
    }

    public Font getHeaderFont() {
        return headerLabel.getFont();
    }

    @Override
    public void setMaxLength(int maxLength) {
        filterDelegate.setMaxLength(maxLength);
        validationDelegate.setMaxLength(maxLength);
        setText(getText());
    }

    @Override
    public int getMaxLength() {
        return filterDelegate.getMaxLength();
    }

    @Override
    public void setMinLength(int minLength) {
        validationDelegate.setMinLength(minLength);
        if (autoValidate) {
            isValidInput();
        }
    }

    @Override
    public int getMinLength() {
        return validationDelegate.getMinLength();
    }

    @Override
    public void setRequired(boolean required) {
        validationDelegate.setRequired(required);
        if (autoValidate) {
            isValidInput();
        }
    }

    @Override
    public boolean isRequired() {
        return validationDelegate.isRequired();
    }

    @Override
    public boolean isEmpty() {
        return validationDelegate.isEmpty(getText());
    }

    @Override
    public boolean isValidInput() {
        boolean valid = validationDelegate.validate(getText(), inputType);
        if (showValidationState) {
            setVisualState(valid ? VisualState.NONE : VisualState.ERROR);
        }
        setToolTipText(valid ? null : validationDelegate.getValidationMessage());
        return valid;
    }

    @Override
    public String getValidationMessage() {
        return validationDelegate.getValidationMessage();
    }

    @Override
    public void setRegex(String regex) {
        validationDelegate.setRegex(regex);
        if (autoValidate) {
            isValidInput();
        }
    }

    @Override
    public String getRegex() {
        return validationDelegate.getRegex();
    }

    @Override
    public void setAutoValidate(boolean autoValidate) {
        this.autoValidate = autoValidate;
    }

    @Override
    public boolean isAutoValidate() {
        return autoValidate;
    }

    @Override
    public void setShowValidationState(boolean showValidationState) {
        this.showValidationState = showValidationState;
    }

    @Override
    public boolean isShowValidationState() {
        return showValidationState;
    }

    @Override
    public void setAllowDecimal(boolean allowDecimal) {
        filterDelegate.setAllowDecimal(allowDecimal);
        setText(getText());
    }

    @Override
    public boolean isAllowDecimal() {
        return filterDelegate.isAllowDecimal();
    }

    @Override
    public void setAllowNegative(boolean allowNegative) {
        filterDelegate.setAllowNegative(allowNegative);
        setText(getText());
    }

    @Override
    public boolean isAllowNegative() {
        return filterDelegate.isAllowNegative();
    }

    @Override
    public void setDatePattern(String pattern) {
        validationDelegate.setDatePattern(pattern);
        if (inputType == InputType.DATE && placeholder.isEmpty()) {
            dateField.setPlaceholder(validationDelegate.getDatePattern());
        }
        if (autoValidate) {
            isValidInput();
        }
    }

    @Override
    public String getDatePattern() {
        return validationDelegate.getDatePattern();
    }

    @Override
    public LocalDate getDateValue() {
        return validationDelegate.parseDate(getText());
    }

    @Override
    public void setEditable(boolean editable) {
        textField.setEditable(editable);
        passwordField.setEditable(editable);
        textArea.setEditable(editable);
        dateField.setEditable(editable);
    }

    @Override
    public boolean isEditable() {
        return activeEditor().isEditable();
    }

    @Override
    public void setInputBarVisible(boolean visible) {
        this.inputBarVisible = visible;
        updateInputBarStyle();
    }

    @Override
    public boolean isInputBarVisible() {
        return inputBarVisible;
    }

    @Override
    public void setInputBarColor(Color color) {
        if (color == null) {
            return;
        }
        this.inputBarColor = color;
        updateInputBarStyle();
    }

    @Override
    public Color getInputBarColor() {
        return inputBarColor;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        textField.setEnabled(enabled);
        passwordField.setEnabled(enabled);
        textArea.setEnabled(enabled);
        dateField.setEnabled(enabled);
    }

    private JTextComponent activeEditor() {
        return switch (inputType) {
            case PASSWORD ->
                passwordField;
            case DESCRIPTION ->
                textArea;
            case DATE ->
                dateField;
            default ->
                textField;
        };
    }

    private void updateInputBarStyle() {
        Color visibleColor = inputBarVisible ? inputBarColor : new Color(0, 0, 0, 0);
        editorHost.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, visibleColor));
        revalidate();
        repaint();
    }

    private void updateHeaderLayout() {
        remove(headerLabel);
        remove(editorHost);

        boolean showHeader = headerVisible && !headerText.isBlank();
        headerLabel.setVisible(showHeader);
        if (showHeader) {
            add(headerLabel, headerConstraint());
        }
        add(editorHost, BorderLayout.CENTER);

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

    private static class PlaceholderTextField extends JTextField {

        private String placeholder = "";

        void setPlaceholder(String placeholder) {
            this.placeholder = placeholder != null ? placeholder : "";
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            paintPlaceholder(g, this, placeholder);
        }
    }

    private static class PlaceholderPasswordField extends JPasswordField {

        private String placeholder = "";

        void setPlaceholder(String placeholder) {
            this.placeholder = placeholder != null ? placeholder : "";
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            paintPlaceholder(g, this, placeholder);
        }
    }

    private static class PlaceholderTextArea extends JTextArea {

        private String placeholder = "";

        void setPlaceholder(String placeholder) {
            this.placeholder = placeholder != null ? placeholder : "";
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            paintPlaceholder(g, this, placeholder);
        }
    }

    private static void paintPlaceholder(Graphics g, JTextComponent component, String placeholder) {
        if (placeholder == null || placeholder.isBlank() || !component.getText().isEmpty()) {
            return;
        }
        FontMetrics metrics = g.getFontMetrics();
        g.setColor(new Color(130, 130, 130));
        g.drawString(placeholder, component.getInsets().left, component.getInsets().top + metrics.getAscent());
    }

    public void setTextFont(Font textFont) {
        this.textFont = textFont;
        if (activeEditor() != null) {
            activeEditor().setFont(this.textFont);
        }
    }

    public Font getTextFont() {
        return textFont;
    }

}
