package shui.delegates.button;

import shui.contracts.button.Buttonable.ActionButton;
import shui.contracts.button.Buttonable.BootstrapButton;
import shui.contracts.button.Buttonable.ButtonIconSize;
import shui.contracts.button.Buttonable.ButtonType;
import shui.icons.buttons.ButtonIcons;
import java.awt.Color;
import javax.swing.Icon;

/**
 * Delegate que resuelve color, texto e icono visual del boton.
 */
public class ButtonStyleDelegate {

    private ButtonType buttonType = ButtonType.CUSTOM;
    private ActionButton actionButton = ActionButton.CREATE;
    private BootstrapButton bootstrapButton = BootstrapButton.PRIMARY;
    private ButtonIconSize iconSize = ButtonIconSize.X24;
    private Icon customIcon;
    private Color customColor = new Color(135, 135, 135);
    private Color customForeground = Color.WHITE;

    public ButtonType getButtonType() {
        return buttonType;
    }

    public void setButtonType(ButtonType buttonType) {
        this.buttonType = buttonType != null ? buttonType : ButtonType.CUSTOM;
    }

    public ActionButton getActionButton() {
        return actionButton;
    }

    public void setActionButton(ActionButton actionButton) {
        this.actionButton = actionButton != null ? actionButton : ActionButton.CREATE;
        this.buttonType = ButtonType.ACTION;
    }

    public BootstrapButton getBootstrapButton() {
        return bootstrapButton;
    }

    public void setBootstrapButton(BootstrapButton bootstrapButton) {
        this.bootstrapButton = bootstrapButton != null ? bootstrapButton : BootstrapButton.PRIMARY;
        this.buttonType = ButtonType.BOOTSTRAP;
    }

    public ButtonIconSize getIconSize() {
        return iconSize;
    }

    public void setIconSize(ButtonIconSize iconSize) {
        this.iconSize = iconSize != null ? iconSize : ButtonIconSize.X24;
    }

    public Icon getCustomIcon() {
        return customIcon;
    }

    public void setCustomIcon(Icon customIcon) {
        if (buttonType == ButtonType.CUSTOM) {
            this.customIcon = customIcon;
        }
    }

    public Color getCustomColor() {
        return customColor;
    }

    public void setCustomColor(Color customColor) {
        if (customColor != null) {
            this.customColor = customColor;
        }
    }

    public Color getCustomForeground() {
        return customForeground;
    }

    public void setCustomForeground(Color customForeground) {
        if (customForeground != null) {
            this.customForeground = customForeground;
        }
    }

    public Icon resolveIcon() {
        return switch (buttonType) {
            case ACTION -> ButtonIcons.getActionIcon(actionButton, iconSize);
            case CUSTOM -> customIcon;
            default -> null;
        };
    }

    public Color resolveBackground() {
        return switch (buttonType) {
            case ACTION -> actionBackground();
            case BOOTSTRAP -> bootstrapBackground();
            default -> customColor;
        };
    }

    public Color resolveForeground() {
        if (buttonType == ButtonType.CUSTOM) {
            return customForeground;
        }
        if (buttonType == ButtonType.BOOTSTRAP
                && (bootstrapButton == BootstrapButton.LIGHT || bootstrapButton == BootstrapButton.WARNING)) {
            return new Color(30, 30, 30);
        }
        return Color.WHITE;
    }

    public String defaultText() {
        return switch (buttonType) {
            case ACTION -> switch (actionButton) {
                case EDIT -> "Editar";
                case DELETE -> "Eliminar";
                default -> "Crear";
            };
            case BOOTSTRAP -> switch (bootstrapButton) {
                case SECONDARY -> "Secondary";
                case SUCCESS -> "Success";
                case DANGER -> "Danger";
                case WARNING -> "Warning";
                case INFO -> "Info";
                case LIGHT -> "Light";
                case DARK -> "Dark";
                default -> "Primary";
            };
            default -> "Custom";
        };
    }

    private Color actionBackground() {
        return switch (actionButton) {
            case EDIT -> new Color(13, 110, 253);
            case DELETE -> new Color(220, 53, 69);
            default -> new Color(25, 135, 84);
        };
    }

    private Color bootstrapBackground() {
        return switch (bootstrapButton) {
            case SECONDARY -> new Color(108, 117, 125);
            case SUCCESS -> new Color(25, 135, 84);
            case DANGER -> new Color(220, 53, 69);
            case WARNING -> new Color(255, 193, 7);
            case INFO -> new Color(13, 202, 240);
            case LIGHT -> new Color(248, 249, 250);
            case DARK -> new Color(33, 37, 41);
            default -> new Color(13, 110, 253);
        };
    }
}
