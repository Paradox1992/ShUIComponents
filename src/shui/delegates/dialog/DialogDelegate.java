package shui.delegates.dialog;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;

/**
 * Delegate para estado adicional de ShDialog.
 */
public class DialogDelegate {

    private final JDialog owner;
    private final JPanel contentPanel = new JPanel(new BorderLayout());

    private Component content;
    private Color background = Color.WHITE;
    private int contentPadding;
    private boolean closeOnEscape = true;
    private Runnable onOpen;
    private Runnable onClose;

    public DialogDelegate(JDialog owner) {
        this.owner = owner;
        contentPanel.setOpaque(true);
        contentPanel.setBackground(background);
        owner.setContentPane(contentPanel);
        installEscapeBinding();
    }

    public void open() {
        if (onOpen != null) {
            onOpen.run();
        }
        owner.setVisible(true);
    }

    public void openCentered() {
        owner.setLocationRelativeTo(owner.getOwner());
        open();
    }

    public void close() {
        if (onClose != null) {
            onClose.run();
        }
        owner.setVisible(false);
    }

    public void setContent(Component component) {
        if (content != null) {
            contentPanel.remove(content);
        }
        this.content = component;
        if (component != null) {
            contentPanel.add(component, BorderLayout.CENTER);
        }
        contentPanel.revalidate();
        contentPanel.repaint();
        owner.pack();
    }

    public Component getContent() {
        return content;
    }

    public void setBackground(Color background) {
        if (background == null) {
            return;
        }
        this.background = background;
        contentPanel.setBackground(background);
        owner.repaint();
    }

    public Color getBackground() {
        return background;
    }

    public void setContentPadding(int padding) {
        this.contentPadding = Math.max(0, padding);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(
                contentPadding,
                contentPadding,
                contentPadding,
                contentPadding
        ));
        contentPanel.revalidate();
        owner.pack();
    }

    public int getContentPadding() {
        return contentPadding;
    }

    public void setCloseOnEscape(boolean closeOnEscape) {
        this.closeOnEscape = closeOnEscape;
    }

    public boolean isCloseOnEscape() {
        return closeOnEscape;
    }

    public void setOnOpen(Runnable onOpen) {
        this.onOpen = onOpen;
    }

    public Runnable getOnOpen() {
        return onOpen;
    }

    public void setOnClose(Runnable onClose) {
        this.onClose = onClose;
    }

    public Runnable getOnClose() {
        return onClose;
    }

    private void installEscapeBinding() {
        contentPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "shDialog.close");
        contentPanel.getActionMap().put("shDialog.close", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (closeOnEscape) {
                    close();
                }
            }
        });
    }
}
