package shui.contracts.visual;

import java.awt.Color;

/**
 * Contrato para transiciones visuales simples.
 */
public interface Animatable {

    void setAnimationsEnabled(boolean enabled);

    boolean isAnimationsEnabled();

    void setAnimationDuration(int duration);

    int getAnimationDuration();

    void animateBackgroundTo(Color color);

    void animateBorderColorTo(Color color);

    void animateAlphaTo(float alpha);

    void stopAnimations();
}
