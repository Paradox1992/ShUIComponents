package shui.delegates.text;

import shui.contracts.text.Inputable.InputDataType;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent;

/**
 * Delegate que limita longitud y tipo de caracteres aceptados.
 */
public class InputFilterDelegate {

    private int maxLength;
    private InputDataType dataType = InputDataType.ANY;
    private boolean allowDecimal;
    private boolean allowNegative;

    public void install(JTextComponent component) {
        if (component == null) {
            return;
        }
        Document document = component.getDocument();
        if (document instanceof AbstractDocument abstractDocument) {
            abstractDocument.setDocumentFilter(new Filter());
        }
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = Math.max(0, maxLength);
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setDataType(InputDataType dataType) {
        this.dataType = dataType != null ? dataType : InputDataType.ANY;
    }

    public InputDataType getDataType() {
        return dataType;
    }

    public void setAllowDecimal(boolean allowDecimal) {
        this.allowDecimal = allowDecimal;
    }

    public boolean isAllowDecimal() {
        return allowDecimal;
    }

    public void setAllowNegative(boolean allowNegative) {
        this.allowNegative = allowNegative;
    }

    public boolean isAllowNegative() {
        return allowNegative;
    }

    public String sanitize(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (acceptChar(c, result.toString(), i)) {
                result.append(c);
            }
        }
        if (maxLength > 0 && result.length() > maxLength) {
            return result.substring(0, maxLength);
        }
        return result.toString();
    }

    private boolean acceptChar(char c, String current, int offset) {
        return switch (dataType) {
            case NUMERIC -> acceptNumeric(c, current, offset);
            case TEXT -> Character.isLetter(c);
            case TEXT_WITH_SPACES -> Character.isLetter(c) || Character.isWhitespace(c);
            default -> true;
        };
    }

    private boolean acceptNumeric(char c, String current, int offset) {
        if (Character.isDigit(c)) {
            return true;
        }
        if (allowDecimal && c == '.' && !current.contains(".")) {
            return true;
        }
        return allowNegative && c == '-' && offset == 0 && !current.startsWith("-");
    }

    private class Filter extends DocumentFilter {

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
            String sanitized = sanitize(candidate.toString());
            fb.replace(0, fb.getDocument().getLength(), sanitized, attrs);
        }
    }
}
