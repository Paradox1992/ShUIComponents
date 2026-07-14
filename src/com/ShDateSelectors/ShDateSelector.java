package com.ShDateSelectors;

import static shui.config.colors.BaseContainerColors.EMPTY_BG;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.format.TextStyle;
import java.util.Locale;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import shui.components.base.BaseContainer;
import shui.contracts.visual.VisualState;

/**
 * Selector de fecha Shui con campo tipo Material y popup de calendario.
 */
public class ShDateSelector extends BaseContainer {

    public enum HeaderPosition {
        TOP_LEFT,
        TOP_CENTER,
        TOP_RIGHT,
        MIDDLE_LEFT,
        MIDDLE_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_CENTER,
        BOTTOM_RIGHT
    }

    private static final Color MATERIAL_BLUE = new Color(25, 118, 210);
    private static final Color TEXT_COLOR = new Color(33, 33, 33);
    private static final Color MUTED_TEXT = new Color(117, 117, 117);
    private static final Color FIELD_BG = new Color(250, 250, 250);
    private static final Color BORDER_COLOR = new Color(189, 189, 189);
    private static final Color ERROR_COLOR = new Color(211, 47, 47);

    private final EventListenerList listeners = new EventListenerList();
    private final JLabel headerLabel = new JLabel();
    private final JPanel fieldPanel = new JPanel(new BorderLayout());
    private final PlaceholderDateField textField = new PlaceholderDateField();
    private final CalendarIconButton calendarButton = new CalendarIconButton();
    private final JPopupMenu popup = new JPopupMenu();
    private final JPanel calendarPanel = new JPanel(new BorderLayout(0, 8));
    private final JLabel monthNameLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel yearLabel = new JLabel("", SwingConstants.CENTER);
    private final JPopupMenu monthPickerPopup = new JPopupMenu();
    private final JPopupMenu yearPickerPopup = new JPopupMenu();
    private final JPanel weekHeaderPanel = new JPanel(new GridLayout(1, 7));
    private final JPanel daysPanel = new JPanel(new GridLayout(6, 7, 2, 2));
    private final DayCell[] dayCells = new DayCell[42];
    private final JButton previousButton = createTextButton("<");
    private final JButton nextButton = createTextButton(">");
    private final JButton todayButton = createTextButton("Hoy");
    private final JButton clearButton = createTextButton("Limpiar");

    private LocalDate selectedDate;
    private LocalDate minDate;
    private LocalDate maxDate;
    private YearMonth displayedMonth = YearMonth.now();
    private String datePattern = "dd/MM/yyyy";
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
    private DateTimeFormatter parseFormatter = DateTimeFormatter
            .ofPattern(strictDatePattern(datePattern))
            .withResolverStyle(ResolverStyle.STRICT);
    private String placeholder = "Seleccione una fecha";
    private String headerText = "Fecha";
    private HeaderPosition headerPosition = HeaderPosition.TOP_LEFT;
    private boolean headerVisible = true;
    private boolean editable = true;
    private boolean required = false;
    private boolean allowCopy = false;
    private boolean allowPaste = false;
    private boolean weekStartsOnMonday = true;
    private boolean todayButtonVisible = true;
    private boolean clearButtonVisible = true;
    private Color accentColor = MATERIAL_BLUE;
    private Color fieldBackgroundColor = FIELD_BG;
    private Color fieldForegroundColor = TEXT_COLOR;
    private Color placeholderColor = MUTED_TEXT;
    private Color headerForeground = MUTED_TEXT;
    private Color popupBackgroundColor = Color.WHITE;
    private Color selectedDayForeground = Color.WHITE;
    private Color dayHoverColor = new Color(227, 242, 253);
    private boolean updatingText;
    private boolean dateTextValid = true;
    private String validationMessage = "";

    public ShDateSelector() {
        super(8, EMPTY_BG);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(EMPTY_BG);
        setBorderEnabled(false);
        setVisualState(VisualState.NONE);

        configureHeader();
        configureField();
        configurePopup();
        updateHeaderLayout();
        refreshField();
        refreshCalendar();
    }

    private void configureHeader() {
        headerLabel.setOpaque(false);
        headerLabel.setForeground(MUTED_TEXT);
        headerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 2, 10));
    }

    private void configureField() {
        fieldPanel.setOpaque(true);
        fieldPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                BorderFactory.createEmptyBorder(2, 8, 2, 2)));

        textField.setOpaque(false);
        textField.setBorder(BorderFactory.createEmptyBorder(8, 2, 8, 8));
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setForeground(fieldForegroundColor);
        textField.setCaretColor(fieldForegroundColor);
        textField.setPlaceholder(placeholder);
        textField.setPlaceholderColor(placeholderColor);
        installDateMask();
        installClipboardActions();
        textField.addActionListener(event -> commitEditorText());
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                refreshFieldBorder();
            }

            @Override
            public void focusLost(FocusEvent e) {
                commitEditorText(true, false);
                refreshFieldBorder();
            }
        });
        textField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled() && !editable) {
                    showPopup();
                }
            }
        });

        calendarButton.setFocusable(false);
        calendarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled()) {
                    SwingUtilities.invokeLater(() -> {
                        commitEditorText(false, false);
                        togglePopup();
                    });
                }
            }
        });

        fieldPanel.add(textField, BorderLayout.CENTER);
        fieldPanel.add(calendarButton, BorderLayout.EAST);
    }

    private void configurePopup() {
        popup.setBorder(BorderFactory.createLineBorder(new Color(224, 224, 224), 1));
        popup.add(calendarPanel);
        popup.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                displayedMonth = selectedDate != null ? YearMonth.from(selectedDate) : YearMonth.now();
                refreshCalendar();
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                monthPickerPopup.setVisible(false);
                yearPickerPopup.setVisible(false);
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });

        calendarPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 10, 12));
        calendarPanel.setBackground(popupBackgroundColor);

        JPanel monthHeader = new JPanel(new BorderLayout());
        monthHeader.setOpaque(false);
        monthHeader.add(previousButton, BorderLayout.WEST);
        monthHeader.add(createMonthYearHeader(), BorderLayout.CENTER);
        monthHeader.add(nextButton, BorderLayout.EAST);

        previousButton.addActionListener(event -> changeDisplayedMonth(-1));
        nextButton.addActionListener(event -> changeDisplayedMonth(1));

        JPanel center = new JPanel(new BorderLayout(0, 6));
        center.setOpaque(false);
        weekHeaderPanel.setOpaque(false);
        daysPanel.setOpaque(false);
        center.add(weekHeaderPanel, BorderLayout.NORTH);
        center.add(daysPanel, BorderLayout.CENTER);

        for (int i = 0; i < dayCells.length; i++) {
            DayCell cell = new DayCell();
            dayCells[i] = cell;
            daysPanel.add(cell);
        }

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        footer.setOpaque(false);
        todayButton.addActionListener(event -> setSelectedDate(LocalDate.now()));
        clearButton.addActionListener(event -> setSelectedDate(null));
        footer.add(clearButton);
        footer.add(todayButton);

        calendarPanel.add(monthHeader, BorderLayout.NORTH);
        calendarPanel.add(center, BorderLayout.CENTER);
        calendarPanel.add(footer, BorderLayout.SOUTH);
    }

    private JPanel createMonthYearHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        header.setOpaque(false);
        configureCalendarSelectorLabel(monthNameLabel);
        configureCalendarSelectorLabel(yearLabel);
        monthNameLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                showMonthPicker();
            }
        });
        yearLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                showYearPicker();
            }
        });
        header.add(monthNameLabel);
        header.add(yearLabel);
        return header;
    }

    private void configureCalendarSelectorLabel(JLabel label) {
        label.setOpaque(false);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    }

    public void setSelectedDate(LocalDate date) {
        setSelectedDate(date, true);
    }

    private void setSelectedDate(LocalDate date, boolean hidePopupOnChange) {
        LocalDate next = normalizeDate(date);
        LocalDate old = this.selectedDate;
        this.selectedDate = next;
        if (next != null) {
            displayedMonth = YearMonth.from(next);
        }
        markDateValid();
        refreshField();
        refreshCalendar();
        firePropertyChange("selectedDate", old, next);
        fireActionEvent();
        if (hidePopupOnChange) {
            hidePopup();
        }
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void setText(String text) {
        if (text == null || text.isBlank()) {
            setSelectedDate(null);
            return;
        }
        String maskedText = applyDateMask(text);
        try {
            setSelectedDate(LocalDate.parse(maskedText.trim(), parseFormatter));
        } catch (DateTimeParseException ex) {
            updatingText = true;
            try {
                textField.setText(maskedText);
            } finally {
                updatingText = false;
            }
            markDateInvalid("La fecha debe ser valida y tener formato " + datePattern + ".");
        }
    }

    public String getText() {
        return textField.getText();
    }

    public void clear() {
        setSelectedDate(null);
    }

    public void setDatePattern(String pattern) {
        String next = pattern != null && !pattern.isBlank() ? pattern : "dd/MM/yyyy";
        this.datePattern = next;
        this.formatter = DateTimeFormatter.ofPattern(next);
        this.parseFormatter = DateTimeFormatter.ofPattern(strictDatePattern(next))
                .withResolverStyle(ResolverStyle.STRICT);
        refreshField();
    }

    public String getDatePattern() {
        return datePattern;
    }

    public void setMinDate(LocalDate minDate) {
        this.minDate = minDate;
        if (selectedDate != null && isDateDisabled(selectedDate)) {
            setSelectedDate(normalizeDate(selectedDate));
        }
        refreshCalendar();
    }

    public LocalDate getMinDate() {
        return minDate;
    }

    public void setMaxDate(LocalDate maxDate) {
        this.maxDate = maxDate;
        if (selectedDate != null && isDateDisabled(selectedDate)) {
            setSelectedDate(normalizeDate(selectedDate));
        }
        refreshCalendar();
    }

    public LocalDate getMaxDate() {
        return maxDate;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder != null ? placeholder : "";
        textField.setPlaceholder(this.placeholder);
        repaint();
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
        this.headerPosition = position != null ? position : HeaderPosition.TOP_LEFT;
        updateHeaderLayout();
    }

    public HeaderPosition getHeaderPosition() {
        return headerPosition;
    }

    public void setHeaderVisible(boolean visible) {
        this.headerVisible = visible;
        updateHeaderLayout();
    }

    public boolean isHeaderVisible() {
        return headerVisible;
    }

    public void setHeaderForeground(Color color) {
        if (color == null) {
            return;
        }
        this.headerForeground = color;
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

    public void setEditable(boolean editable) {
        this.editable = editable;
        textField.setEditable(editable);
        textField.setCursor(editable ? Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR)
                : Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public boolean isEditable() {
        return editable;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isRequired() {
        return required;
    }

    public void setAllowCopy(boolean allowCopy) {
        this.allowCopy = allowCopy;
    }

    public boolean isAllowCopy() {
        return allowCopy;
    }

    public void setAllowPaste(boolean allowPaste) {
        this.allowPaste = allowPaste;
    }

    public boolean isAllowPaste() {
        return allowPaste;
    }

    public void setWeekStartsOnMonday(boolean weekStartsOnMonday) {
        this.weekStartsOnMonday = weekStartsOnMonday;
        refreshCalendar();
    }

    public boolean isWeekStartsOnMonday() {
        return weekStartsOnMonday;
    }

    public void setTodayButtonVisible(boolean visible) {
        this.todayButtonVisible = visible;
        todayButton.setVisible(visible);
        revalidate();
        repaint();
    }

    public boolean isTodayButtonVisible() {
        return todayButtonVisible;
    }

    public void setClearButtonVisible(boolean visible) {
        this.clearButtonVisible = visible;
        clearButton.setVisible(visible);
        revalidate();
        repaint();
    }

    public boolean isClearButtonVisible() {
        return clearButtonVisible;
    }

    public void setAccentColor(Color color) {
        if (color == null) {
            return;
        }
        this.accentColor = color;
        calendarButton.setAccentColor(color);
        previousButton.setForeground(color);
        nextButton.setForeground(color);
        todayButton.setForeground(color);
        clearButton.setForeground(color);
        refreshFieldBorder();
        refreshCalendar();
    }

    public Color getAccentColor() {
        return accentColor;
    }

    public void setFieldBackgroundColor(Color color) {
        if (color == null) {
            return;
        }
        this.fieldBackgroundColor = color;
        refreshField();
    }

    public Color getFieldBackgroundColor() {
        return fieldBackgroundColor;
    }

    public void setFieldForegroundColor(Color color) {
        if (color == null) {
            return;
        }
        this.fieldForegroundColor = color;
        refreshField();
    }

    public Color getFieldForegroundColor() {
        return fieldForegroundColor;
    }

    public void setPlaceholderColor(Color color) {
        if (color == null) {
            return;
        }
        this.placeholderColor = color;
        textField.setPlaceholderColor(color);
        repaint();
    }

    public Color getPlaceholderColor() {
        return placeholderColor;
    }

    public void setPopupBackgroundColor(Color color) {
        if (color == null) {
            return;
        }
        this.popupBackgroundColor = color;
        calendarPanel.setBackground(color);
        refreshCalendar();
    }

    public Color getPopupBackgroundColor() {
        return popupBackgroundColor;
    }

    public void setSelectedDayForeground(Color color) {
        if (color == null) {
            return;
        }
        this.selectedDayForeground = color;
        refreshCalendar();
    }

    public Color getSelectedDayForeground() {
        return selectedDayForeground;
    }

    public void setDayHoverColor(Color color) {
        if (color == null) {
            return;
        }
        this.dayHoverColor = color;
        refreshCalendar();
    }

    public Color getDayHoverColor() {
        return dayHoverColor;
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
        displayedMonth = selectedDate != null ? YearMonth.from(selectedDate) : YearMonth.now();
        refreshCalendar();
        popup.show(this, 0, getHeight());
    }

    public void hidePopup() {
        popup.setVisible(false);
    }

    public boolean isPopupVisible() {
        return popup.isVisible();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        textField.setEnabled(enabled);
        calendarButton.setEnabled(enabled);
        setAlpha(enabled ? 1f : 0.55f);
    }

    private void togglePopup() {
        if (popup.isVisible()) {
            hidePopup();
        } else {
            showPopup();
        }
    }

    private void changeDisplayedMonth(int amount) {
        displayedMonth = displayedMonth.plusMonths(amount);
        refreshCalendar();
    }

    private boolean commitEditorText() {
        return commitEditorText(true, true);
    }

    private boolean commitEditorText(boolean hidePopupOnChange) {
        return commitEditorText(hidePopupOnChange, true);
    }

    private boolean commitEditorText(boolean hidePopupOnChange, boolean validateRequired) {
        if (updatingText || !editable) {
            return true;
        }
        String text = applyDateMask(textField.getText());
        setEditorText(text);
        if (text == null || text.isBlank()) {
            if (selectedDate != null) {
                setSelectedDate(null, hidePopupOnChange);
            }
            if (required && validateRequired) {
                markDateInvalid("El campo es obligatorio.");
                return false;
            }
            markDateValid();
            return true;
        }
        try {
            LocalDate parsed = LocalDate.parse(text.trim(), parseFormatter);
            setSelectedDate(parsed, hidePopupOnChange);
            return true;
        } catch (DateTimeParseException ex) {
            markDateInvalid("La fecha debe ser valida y tener formato " + datePattern + ".");
            return false;
        }
    }

    private LocalDate normalizeDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        if (minDate != null && date.isBefore(minDate)) {
            return minDate;
        }
        if (maxDate != null && date.isAfter(maxDate)) {
            return maxDate;
        }
        return date;
    }

    private boolean isDateDisabled(LocalDate date) {
        return (minDate != null && date.isBefore(minDate))
                || (maxDate != null && date.isAfter(maxDate));
    }

    private void refreshField() {
        fieldPanel.setBackground(fieldBackgroundColor);
        textField.setForeground(fieldForegroundColor);
        textField.setPlaceholder(placeholder);
        textField.setPlaceholderColor(placeholderColor);
        textField.setEditable(editable);
        calendarButton.setAccentColor(accentColor);

        updatingText = true;
        try {
            textField.setText(selectedDate != null ? formatter.format(selectedDate) : "");
        } finally {
            updatingText = false;
        }
        refreshFieldBorder();
        revalidate();
        repaint();
    }

    private void refreshFieldBorder() {
        Color line = !dateTextValid ? ERROR_COLOR
                : textField.hasFocus() || popup.isVisible() ? accentColor : BORDER_COLOR;
        fieldPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, textField.hasFocus() ? 2 : 1, 0, line),
                BorderFactory.createEmptyBorder(2, 8, 2, 2)));
        headerLabel.setForeground(!dateTextValid ? ERROR_COLOR
                : textField.hasFocus() || popup.isVisible() ? accentColor : headerForeground);
        fieldPanel.repaint();
    }

    private void setEditorText(String text) {
        updatingText = true;
        try {
            textField.setText(text != null ? text : "");
        } finally {
            updatingText = false;
        }
    }

    private void markDateValid() {
        dateTextValid = true;
        validationMessage = "";
        setToolTipText(null);
        textField.setToolTipText(null);
        setVisualState(VisualState.NONE);
        refreshFieldBorder();
    }

    private void markDateInvalid(String message) {
        dateTextValid = false;
        validationMessage = message != null && !message.isBlank()
                ? message
                : "La fecha debe ser valida y tener formato " + datePattern + ".";
        setToolTipText(validationMessage);
        textField.setToolTipText(validationMessage);
        setVisualState(VisualState.ERROR);
        refreshFieldBorder();
    }

    public boolean validateDate() {
        return commitEditorText(false, true);
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    private void installDateMask() {
        Document document = textField.getDocument();
        if (document instanceof AbstractDocument abstractDocument) {
            abstractDocument.setDocumentFilter(new DateMaskFilter());
        }
    }

    private void installClipboardActions() {
        textField.getActionMap().put(DefaultEditorKit.copyAction, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copyFromEditor();
            }
        });
        textField.getActionMap().put(DefaultEditorKit.cutAction, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (allowCopy && editable && textField.isEditable()) {
                    copyFromEditor();
                    textField.replaceSelection("");
                }
            }
        });
        textField.getActionMap().put(DefaultEditorKit.pasteAction, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pasteIntoEditor();
            }
        });
    }

    private void copyFromEditor() {
        if (!allowCopy) {
            return;
        }
        String selected = textField.getSelectedText();
        if (selected != null && !selected.isEmpty()) {
            Toolkit.getDefaultToolkit().getSystemClipboard()
                    .setContents(new StringSelection(selected), null);
        }
    }

    private void pasteIntoEditor() {
        if (!allowPaste || !editable || !textField.isEditable() || !textField.isEnabled()) {
            return;
        }
        try {
            Object value = Toolkit.getDefaultToolkit().getSystemClipboard()
                    .getData(DataFlavor.stringFlavor);
            if (value instanceof String text) {
                textField.replaceSelection(text);
                commitEditorText(false, false);
            }
        } catch (Exception ex) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private String applyDateMask(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        String digits = value.replaceAll("\\D", "");
        StringBuilder result = new StringBuilder(datePattern.length());
        int digitIndex = 0;
        for (int i = 0; i < datePattern.length() && digitIndex < digits.length(); i++) {
            char patternChar = datePattern.charAt(i);
            if (isDatePatternFieldChar(patternChar)) {
                result.append(digits.charAt(digitIndex++));
            } else if (digitIndex > 0) {
                result.append(patternChar);
            }
        }
        return result.toString();
    }

    private static boolean isDatePatternFieldChar(char c) {
        return c == 'd' || c == 'D' || c == 'M' || c == 'm' || c == 'y' || c == 'Y' || c == 'u';
    }

    private static String strictDatePattern(String pattern) {
        return pattern != null ? pattern.replace('y', 'u').replace('Y', 'u') : "dd/MM/uuuu";
    }

    private void refreshCalendar() {
        if (displayedMonth == null) {
            displayedMonth = YearMonth.now();
        }
        calendarPanel.setBackground(popupBackgroundColor);
        monthNameLabel.setText(displayedMonth.getMonth()
                .getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("es-HN")));
        yearLabel.setText(Integer.toString(displayedMonth.getYear()));
        rebuildWeekHeader();
        populateDays();
        todayButton.setVisible(todayButtonVisible);
        clearButton.setVisible(clearButtonVisible);
        popup.pack();
        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    private void showMonthPicker() {
        if (!isEnabled() || displayedMonth == null) {
            return;
        }
        monthPickerPopup.removeAll();
        Locale locale = Locale.forLanguageTag("es-HN");
        for (Month month : Month.values()) {
            JMenuItem item = new JMenuItem(month.getDisplayName(TextStyle.FULL_STANDALONE, locale));
            item.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            item.setForeground(month == displayedMonth.getMonth() ? accentColor : TEXT_COLOR);
            item.setBackground(popupBackgroundColor);
            item.addActionListener(event -> selectDisplayedMonth(month));
            monthPickerPopup.add(item);
        }
        monthPickerPopup.show(monthNameLabel, 0, monthNameLabel.getHeight());
    }

    private void showYearPicker() {
        if (!isEnabled() || displayedMonth == null) {
            return;
        }
        int startYear = firstPickerYear();
        int endYear = lastPickerYear();
        if (endYear < startYear) {
            endYear = startYear;
        }
        Integer[] years = new Integer[endYear - startYear + 1];
        for (int i = 0; i < years.length; i++) {
            years[i] = startYear + i;
        }

        JList<Integer> yearList = new JList<>(years);
        yearList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        yearList.setFixedCellHeight(26);
        yearList.setVisibleRowCount(Math.min(8, years.length));
        yearList.setSelectedValue(displayedMonth.getYear(), true);
        yearList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        yearList.setForeground(TEXT_COLOR);
        yearList.setBackground(popupBackgroundColor);
        yearList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Integer year = yearList.getSelectedValue();
                if (year != null) {
                    selectDisplayedYear(year);
                }
            }
        });

        JScrollPane scroll = new JScrollPane(yearList);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(popupBackgroundColor);
        scroll.setPreferredSize(new Dimension(92, yearList.getVisibleRowCount() * yearList.getFixedCellHeight()));

        yearPickerPopup.removeAll();
        yearPickerPopup.add(scroll);
        yearPickerPopup.pack();
        yearPickerPopup.show(yearLabel, 0, yearLabel.getHeight());
    }

    private void selectDisplayedMonth(Month month) {
        displayedMonth = YearMonth.of(displayedMonth.getYear(), month);
        monthPickerPopup.setVisible(false);
        refreshCalendar();
    }

    private void selectDisplayedYear(int year) {
        displayedMonth = YearMonth.of(year, displayedMonth.getMonth());
        yearPickerPopup.setVisible(false);
        refreshCalendar();
    }

    private int firstPickerYear() {
        if (minDate != null) {
            return minDate.getYear();
        }
        return displayedMonth.getYear() - 50;
    }

    private int lastPickerYear() {
        if (maxDate != null) {
            return maxDate.getYear();
        }
        return displayedMonth.getYear() + 50;
    }

    private void rebuildWeekHeader() {
        weekHeaderPanel.removeAll();
        DayOfWeek first = firstDayOfWeek();
        for (int i = 0; i < 7; i++) {
            DayOfWeek day = first.plus(i);
            JLabel label = new JLabel(day.getDisplayName(TextStyle.SHORT_STANDALONE,
                    Locale.forLanguageTag("es-HN")).substring(0, 2).toUpperCase(Locale.ROOT),
                    SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 11));
            label.setForeground(MUTED_TEXT);
            weekHeaderPanel.add(label);
        }
    }

    private void populateDays() {
        LocalDate firstOfMonth = displayedMonth.atDay(1);
        int firstOffset = dayOffset(firstOfMonth.getDayOfWeek());
        LocalDate cursor = firstOfMonth.minusDays(firstOffset);

        for (DayCell cell : dayCells) {
            LocalDate cellDate = cursor;
            boolean currentMonth = YearMonth.from(cellDate).equals(displayedMonth);
            boolean selected = selectedDate != null && selectedDate.equals(cellDate);
            boolean today = LocalDate.now().equals(cellDate);
            boolean disabled = isDateDisabled(cellDate);
            cell.configure(cellDate, currentMonth, selected, today, disabled);
            cursor = cursor.plusDays(1);
        }
    }

    private DayOfWeek firstDayOfWeek() {
        return weekStartsOnMonday ? DayOfWeek.MONDAY : DayOfWeek.SUNDAY;
    }

    private int dayOffset(DayOfWeek day) {
        int first = firstDayOfWeek().getValue();
        int current = day.getValue();
        return (current - first + 7) % 7;
    }

    private void updateHeaderLayout() {
        remove(headerLabel);
        remove(fieldPanel);

        boolean showHeader = headerVisible && !headerText.isBlank();
        headerLabel.setText(headerText);
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

    private JButton createTextButton(String text) {
        JButton button = new JButton(text);
        button.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setForeground(accentColor);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void fireActionEvent() {
        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "dateChanged");
        for (ActionListener listener : getActionListeners()) {
            listener.actionPerformed(event);
        }
    }

    private class DateMaskFilter extends DocumentFilter {

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            replace(fb, offset, 0, string, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text == null) {
                return;
            }
            String current = fb.getDocument().getText(0, fb.getDocument().getLength());
            StringBuilder candidate = new StringBuilder(current);
            candidate.replace(offset, offset + length, text);
            fb.replace(0, fb.getDocument().getLength(), applyDateMask(candidate.toString()), attrs);
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            String current = fb.getDocument().getText(0, fb.getDocument().getLength());
            StringBuilder candidate = new StringBuilder(current);
            candidate.delete(offset, offset + length);
            fb.replace(0, fb.getDocument().getLength(), applyDateMask(candidate.toString()), null);
        }
    }

    private class DayCell extends JComponent {

        private LocalDate date;
        private boolean currentMonth;
        private boolean selected;
        private boolean today;
        private boolean disabled;
        private boolean hover;

        DayCell() {
            setPreferredSize(new Dimension(36, 32));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hover = false;
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    if (!disabled && date != null) {
                        setSelectedDate(date);
                    }
                }
            });
        }

        void configure(LocalDate date, boolean currentMonth, boolean selected, boolean today, boolean disabled) {
            this.date = date;
            this.currentMonth = currentMonth;
            this.selected = selected;
            this.today = today;
            this.disabled = disabled;
            setEnabled(!disabled);
            setToolTipText(date != null ? formatter.format(date) : null);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = Math.min(getWidth(), getHeight()) - 4;
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;

            if (selected) {
                g2.setColor(accentColor);
                g2.fillOval(x, y, size, size);
            } else if (hover && !disabled) {
                g2.setColor(dayHoverColor);
                g2.fillOval(x, y, size, size);
            }

            if (today && !selected) {
                g2.setColor(accentColor);
                g2.drawOval(x, y, size, size);
            }

            String text = date != null ? Integer.toString(date.getDayOfMonth()) : "";
            Font font = new Font("Segoe UI", selected ? Font.BOLD : Font.PLAIN, 12);
            g2.setFont(font);
            FontMetrics metrics = g2.getFontMetrics();
            int textX = (getWidth() - metrics.stringWidth(text)) / 2;
            int textY = (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();

            if (disabled) {
                g2.setColor(new Color(190, 190, 190));
            } else if (selected) {
                g2.setColor(selectedDayForeground);
            } else if (currentMonth) {
                g2.setColor(TEXT_COLOR);
            } else {
                g2.setColor(new Color(170, 170, 170));
            }
            g2.drawString(text, textX, textY);
            g2.dispose();
        }
    }

    private static class PlaceholderDateField extends JTextField {

        private String placeholder = "";
        private Color placeholderColor = MUTED_TEXT;

        void setPlaceholder(String placeholder) {
            this.placeholder = placeholder != null ? placeholder : "";
            repaint();
        }

        void setPlaceholderColor(Color placeholderColor) {
            this.placeholderColor = placeholderColor != null ? placeholderColor : MUTED_TEXT;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (placeholder.isBlank() || !getText().isEmpty()) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(placeholderColor);
            g2.setFont(getFont());
            FontMetrics metrics = g2.getFontMetrics();
            Insets insets = getInsets();
            g2.drawString(placeholder, insets.left,
                    (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent());
            g2.dispose();
        }
    }

    private static class CalendarIconButton extends JComponent {

        private Color accentColor = MATERIAL_BLUE;
        private boolean hover;

        CalendarIconButton() {
            setPreferredSize(new Dimension(36, 34));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hover = false;
                    repaint();
                }
            });
        }

        void setAccentColor(Color accentColor) {
            this.accentColor = accentColor != null ? accentColor : MATERIAL_BLUE;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (hover && isEnabled()) {
                g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 22));
                g2.fillOval(3, 2, getWidth() - 6, getHeight() - 4);
            }
            g2.setColor(isEnabled() ? accentColor : new Color(160, 160, 160));
            Rectangle r = new Rectangle((getWidth() - 18) / 2, (getHeight() - 18) / 2 + 1, 18, 16);
            g2.drawRoundRect(r.x, r.y + 2, r.width, r.height - 2, 4, 4);
            g2.fillRect(r.x, r.y + 5, r.width, 2);
            g2.drawLine(r.x + 5, r.y, r.x + 5, r.y + 4);
            g2.drawLine(r.x + r.width - 5, r.y, r.x + r.width - 5, r.y + 4);
            g2.fillOval(r.x + 5, r.y + 10, 2, 2);
            g2.fillOval(r.x + 11, r.y + 10, 2, 2);
            g2.dispose();
        }
    }
}
