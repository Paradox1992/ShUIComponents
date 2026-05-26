package shui.contracts.visual;

import shui.theme.ShTheme;

/**
 * Contrato para componentes que reciben un tema visual.
 */
public interface Themeable {

    void setTheme(ShTheme theme);

    ShTheme getTheme();

    void applyTheme();
}
