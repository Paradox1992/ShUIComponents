package shui.contracts.dialog;

import java.awt.Color;
import java.awt.Component;

/**
 * Contrato de utilidades para ShDialog sobre la funcionalidad nativa de JDialog.
 */
public interface Dialogable {

    void open();

    void openCentered();

    void close();

    void setContent(Component component);

    Component getContent();

    void setDialogBackground(Color color);

    Color getDialogBackground();

    void setContentPadding(int padding);

    int getContentPadding();

    void setCloseOnEscape(boolean closeOnEscape);

    boolean isCloseOnEscape();

    void setOnOpen(Runnable onOpen);

    Runnable getOnOpen();

    void setOnClose(Runnable onClose);

    Runnable getOnClose();
}
