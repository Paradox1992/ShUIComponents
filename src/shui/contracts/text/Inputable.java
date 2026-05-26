package shui.contracts.text;

import java.time.LocalDate;

/**
 * Contrato base para componentes de entrada de texto.
 */
public interface Inputable {

    enum InputType {
        TEXT,
        PASSWORD,
        DESCRIPTION,
        DATE
    }

    enum InputDataType {
        ANY,
        NUMERIC,
        TEXT,
        TEXT_WITH_SPACES
    }

    enum HeaderPosition {
        TOP_LEFT,
        TOP_CENTER,
        TOP_RIGHT,
        MIDDLE_LEFT,
        MIDDLE_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_CENTER,
        BOTTOM_RIGHT
    }

    void setInputType(InputType type);

    InputType getInputType();

    void setInputDataType(InputDataType type);

    InputDataType getInputDataType();

    void setText(String text);

    String getText();

    void clear();

    void selectAllText();

    void setPlaceholder(String placeholder);

    String getPlaceholder();

    void setHeaderText(String headerText);

    String getHeaderText();

    void setHeaderPosition(HeaderPosition position);

    HeaderPosition getHeaderPosition();

    void setHeaderVisible(boolean visible);

    boolean isHeaderVisible();

    void setMaxLength(int maxLength);

    int getMaxLength();

    void setMinLength(int minLength);

    int getMinLength();

    void setRequired(boolean required);

    boolean isRequired();

    boolean isEmpty();

    boolean isValidInput();

    String getValidationMessage();

    void setRegex(String regex);

    String getRegex();

    void setAutoValidate(boolean autoValidate);

    boolean isAutoValidate();

    void setShowValidationState(boolean showValidationState);

    boolean isShowValidationState();

    void setAllowDecimal(boolean allowDecimal);

    boolean isAllowDecimal();

    void setAllowNegative(boolean allowNegative);

    boolean isAllowNegative();

    void setDatePattern(String pattern);

    String getDatePattern();

    LocalDate getDateValue();

    void setEditable(boolean editable);

    boolean isEditable();
}
