package shui.delegates.text;

import shui.contracts.text.Inputable.InputType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Delegate que centraliza las reglas de validacion de texto.
 */
public class InputValidationDelegate {

    private boolean required;
    private int minLength;
    private int maxLength;
    private String regex;
    private String datePattern = "yyyy-MM-dd";
    private String validationMessage = "";

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isRequired() {
        return required;
    }

    public void setMinLength(int minLength) {
        this.minLength = Math.max(0, minLength);
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = Math.max(0, maxLength);
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setRegex(String regex) {
        this.regex = (regex == null || regex.isBlank()) ? null : regex;
    }

    public String getRegex() {
        return regex;
    }

    public void setDatePattern(String datePattern) {
        if (datePattern == null || datePattern.isBlank()) {
            return;
        }
        DateTimeFormatter.ofPattern(datePattern);
        this.datePattern = datePattern;
    }

    public String getDatePattern() {
        return datePattern;
    }

    public boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    public boolean validate(String text, InputType inputType) {
        String value = text != null ? text : "";
        validationMessage = "";

        if (required && isEmpty(value)) {
            validationMessage = "El campo es obligatorio.";
            return false;
        }
        if (!required && isEmpty(value)) {
            return true;
        }
        if (minLength > 0 && value.length() < minLength) {
            validationMessage = "Debe tener al menos " + minLength + " caracteres.";
            return false;
        }
        if (maxLength > 0 && value.length() > maxLength) {
            validationMessage = "Debe tener como maximo " + maxLength + " caracteres.";
            return false;
        }
        if (regex != null && !matchesRegex(value)) {
            validationMessage = "El formato no es valido.";
            return false;
        }
        if (inputType == InputType.DATE && parseDate(value) == null) {
            validationMessage = "La fecha debe tener formato " + datePattern + ".";
            return false;
        }
        return true;
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    public LocalDate parseDate(String text) {
        if (isEmpty(text)) {
            return null;
        }
        try {
            return LocalDate.parse(text.trim(), DateTimeFormatter.ofPattern(datePattern));
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    private boolean matchesRegex(String value) {
        try {
            return Pattern.matches(regex, value);
        } catch (PatternSyntaxException ex) {
            validationMessage = "La expresion regular no es valida.";
            return false;
        }
    }
}
