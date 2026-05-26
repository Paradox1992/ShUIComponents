package shui.theme;

import shui.contracts.visual.Themeable;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Registro global de tema para componentes Shui.
 */
public final class ShThemeManager {

    private static final Set<Themeable> COMPONENTS =
            Collections.newSetFromMap(new WeakHashMap<>());
    private static ShTheme currentTheme = ShTheme.light();

    private ShThemeManager() {
    }

    public static ShTheme getCurrentTheme() {
        return currentTheme;
    }

    public static void setCurrentTheme(ShTheme theme) {
        if (theme == null) {
            return;
        }
        currentTheme = theme;
        for (Themeable component : COMPONENTS) {
            component.setTheme(currentTheme);
        }
    }

    public static void register(Themeable component) {
        if (component != null) {
            COMPONENTS.add(component);
        }
    }

    public static void unregister(Themeable component) {
        COMPONENTS.remove(component);
    }
}
