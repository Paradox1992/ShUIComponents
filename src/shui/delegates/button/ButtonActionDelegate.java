package shui.delegates.button;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.event.EventListenerList;

/**
 * Delegate que ejecuta acciones de click sin depender de JButton.
 */
public class ButtonActionDelegate {

    private final JComponent owner;
    private final EventListenerList listeners = new EventListenerList();
    private Runnable onClick;

    public ButtonActionDelegate(JComponent owner) {
        this.owner = owner;
        installMouseHandler();
    }

    public void setOnClick(Runnable onClick) {
        this.onClick = onClick;
    }

    public Runnable getOnClick() {
        return onClick;
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

    public void fireAction(String command) {
        if (!owner.isEnabled()) {
            return;
        }
        if (onClick != null) {
            onClick.run();
        }
        ActionEvent event = new ActionEvent(owner, ActionEvent.ACTION_PERFORMED, command);
        for (ActionListener listener : getActionListeners()) {
            listener.actionPerformed(event);
        }
    }

    private void installMouseHandler() {
        owner.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (owner.contains(e.getPoint())) {
                    fireAction("click");
                }
            }
        });
    }
}
