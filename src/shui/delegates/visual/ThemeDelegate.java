package shui.delegates.visual;

import shui.contracts.visual.Themeable;
import shui.theme.ShTheme;
import shui.theme.ShThemeManager;
import javax.swing.JComponent;

/**
 * Delegate que conserva el tema asignado al componente.
 */
public class ThemeDelegate {

    private final JComponent owner;
    private ShTheme theme = ShThemeManager.getCurrentTheme();

    public ThemeDelegate(JComponent owner) {
        this.owner = owner;
    }

    public void register(Themeable component) {
        ShThemeManager.register(component);
    }

    public void unregister(Themeable component) {
        ShThemeManager.unregister(component);
    }

    public void setTheme(ShTheme theme) {
        if (theme == null) {
            return;
        }
        this.theme = theme;
        owner.repaint();
    }

    public ShTheme getTheme() {
        return theme;
    }
}
