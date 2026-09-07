package shui.contracts.button;

import java.awt.Color;
import javax.swing.Icon;
import javax.swing.SwingConstants;

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

    enum HorizontalAlignment {
        LEFT(SwingConstants.LEFT),
        CENTER(SwingConstants.CENTER),
        RIGHT(SwingConstants.RIGHT),
        LEADING(SwingConstants.LEADING),
        TRAILING(SwingConstants.TRAILING);

        private final int swingConstant;

        HorizontalAlignment(int swingConstant) {
            this.swingConstant = swingConstant;
        }

        public int getSwingConstant() {
            return swingConstant;
        }
    }

    enum VerticalAlignment {
        TOP(SwingConstants.TOP),
        CENTER(SwingConstants.CENTER),
        BOTTOM(SwingConstants.BOTTOM);

        private final int swingConstant;

        VerticalAlignment(int swingConstant) {
            this.swingConstant = swingConstant;
        }

        public int getSwingConstant() {
            return swingConstant;
        }
    }

    enum HorizontalTextPosition {
        LEFT(SwingConstants.LEFT),
        CENTER(SwingConstants.CENTER),
        RIGHT(SwingConstants.RIGHT),
        LEADING(SwingConstants.LEADING),
        TRAILING(SwingConstants.TRAILING);

        private final int swingConstant;

        HorizontalTextPosition(int swingConstant) {
            this.swingConstant = swingConstant;
        }

        public int getSwingConstant() {
            return swingConstant;
        }
    }

    enum VerticalTextPosition {
        TOP(SwingConstants.TOP),
        CENTER(SwingConstants.CENTER),
        BOTTOM(SwingConstants.BOTTOM);

        private final int swingConstant;

        VerticalTextPosition(int swingConstant) {
            this.swingConstant = swingConstant;
        }

        public int getSwingConstant() {
            return swingConstant;
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

    void setHorizontalAlignment(HorizontalAlignment alignment);

    HorizontalAlignment getHorizontalAlignment();

    void setVerticalAlignment(VerticalAlignment alignment);

    VerticalAlignment getVerticalAlignment();

    void setHorizontalTextPosition(HorizontalTextPosition position);

    HorizontalTextPosition getHorizontalTextPosition();

    void setVerticalTextPosition(VerticalTextPosition position);

    VerticalTextPosition getVerticalTextPosition();

    void doClick();
}
