package shui.icons.buttons;

import shui.contracts.button.Buttonable.ActionButton;
import shui.contracts.button.Buttonable.ButtonIconSize;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Iconos compartidos para ShButton. Se cargan una sola vez por clase.
 */
public final class ButtonIcons {

    public static final Icon CREATE_X16 = load("/shui/icons/buttons/create/create_black_x16.png");
    public static final Icon CREATE_X24 = load("/shui/icons/buttons/create/create_black_x24.png");
    public static final Icon CREATE_X32 = load("/shui/icons/buttons/create/create_black_x32.png");
    public static final Icon CREATE_X64 = load("/shui/icons/buttons/create/create_black_x64.png");

    public static final Icon EDIT_X16 = load("/shui/icons/buttons/edit/edit_black_x16.png");
    public static final Icon EDIT_X24 = load("/shui/icons/buttons/edit/edit_black_x24.png");
    public static final Icon EDIT_X32 = load("/shui/icons/buttons/edit/edit_black_x32.png");
    public static final Icon EDIT_X64 = load("/shui/icons/buttons/edit/edit_black_x64.png");

    public static final Icon DELETE_X16 = load("/shui/icons/buttons/delete/delete_black_x16.png");
    public static final Icon DELETE_X24 = load("/shui/icons/buttons/delete/delete_black_x24.png");
    public static final Icon DELETE_X32 = load("/shui/icons/buttons/delete/delete_black_x32.png");
    public static final Icon DELETE_X64 = load("/shui/icons/buttons/delete/delete_black_x64.png");

    private ButtonIcons() {
    }

    public static Icon getActionIcon(ActionButton action, ButtonIconSize size) {
        ActionButton resolvedAction = action != null ? action : ActionButton.CREATE;
        ButtonIconSize resolvedSize = size != null ? size : ButtonIconSize.X24;
        return switch (resolvedAction) {
            case EDIT -> getEditIcon(resolvedSize);
            case DELETE -> getDeleteIcon(resolvedSize);
            default -> getCreateIcon(resolvedSize);
        };
    }

    private static Icon getCreateIcon(ButtonIconSize size) {
        return switch (size) {
            case X16 -> CREATE_X16;
            case X32 -> CREATE_X32;
            case X64 -> CREATE_X64;
            default -> CREATE_X24;
        };
    }

    private static Icon getEditIcon(ButtonIconSize size) {
        return switch (size) {
            case X16 -> EDIT_X16;
            case X32 -> EDIT_X32;
            case X64 -> EDIT_X64;
            default -> EDIT_X24;
        };
    }

    private static Icon getDeleteIcon(ButtonIconSize size) {
        return switch (size) {
            case X16 -> DELETE_X16;
            case X32 -> DELETE_X32;
            case X64 -> DELETE_X64;
            default -> DELETE_X24;
        };
    }

    private static Icon load(String path) {
        java.net.URL resource = ButtonIcons.class.getResource(path);
        return resource != null ? new ImageIcon(resource) : null;
    }
}
