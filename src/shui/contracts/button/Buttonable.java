package shui.contracts.button;

import java.awt.Color;
import javax.swing.Icon;

/**
 * Contrato para botones Shui que no dependen de JButton.
 */
public interface Buttonable {

    enum ButtonType {
        ACTION,
        BOOTSTRAP,
        CUSTOM
    }

    enum ActionButton {
        CREATE,
        EDIT,
        DELETE
    }

    enum BootstrapButton {
        PRIMARY,
        SECONDARY,
        SUCCESS,
        DANGER,
        WARNING,
        INFO,
        LIGHT,
        DARK
    }

    enum ButtonIconSize {
        X16(16),
        X24(24),
        X32(32),
        X64(64);

        private final int pixels;

        ButtonIconSize(int pixels) {
            this.pixels = pixels;
        }

        public int getPixels() {
            return pixels;
        }
    }

    void setButtonText(String text);

    String getButtonText();

    void setButtonType(ButtonType type);

    ButtonType getButtonType();

    void setActionButton(ActionButton action);

    ActionButton getActionButton();

    void setBootstrapButton(BootstrapButton button);

    BootstrapButton getBootstrapButton();

    void setIconSize(ButtonIconSize size);

    ButtonIconSize getIconSize();

    void setCustomIcon(Icon icon);

    Icon getCustomIcon();

    void setCustomColor(Color color);

    Color getCustomColor();

    void setCustomForeground(Color color);

    Color getCustomForeground();

    void doClick();
}
