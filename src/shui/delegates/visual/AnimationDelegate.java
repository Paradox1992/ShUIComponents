package shui.delegates.visual;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.JComponent;
import javax.swing.Timer;

/**
 * Delegate para animaciones visuales pequenas y no bloqueantes.
 */
public class AnimationDelegate {

    private static final int FRAME_DELAY = 15;

    private final JComponent owner;
    private final Map<String, Timer> timers = new HashMap<>();
    private boolean enabled = true;
    private int duration = 180;

    public AnimationDelegate(JComponent owner) {
        this.owner = owner;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            stopAll();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setDuration(int duration) {
        this.duration = Math.max(1, duration);
    }

    public int getDuration() {
        return duration;
    }

    public void animateColor(String key, Color from, Color to, Consumer<Color> setter) {
        if (to == null || setter == null) {
            return;
        }
        if (!enabled || from == null) {
            setter.accept(to);
            return;
        }
        long start = System.currentTimeMillis();
        stop(key);
        Timer timer = new Timer(FRAME_DELAY, null);
        timer.addActionListener(e -> {
            float progress = progress(start);
            setter.accept(lerp(from, to, progress));
            owner.repaint();
            if (progress >= 1f) {
                stop(key);
            }
        });
        timers.put(key, timer);
        timer.start();
    }

    public void animateFloat(String key, float from, float to, Consumer<Float> setter) {
        if (setter == null) {
            return;
        }
        if (!enabled) {
            setter.accept(to);
            return;
        }
        long start = System.currentTimeMillis();
        stop(key);
        Timer timer = new Timer(FRAME_DELAY, null);
        timer.addActionListener(e -> {
            float progress = progress(start);
            setter.accept(from + ((to - from) * progress));
            owner.repaint();
            if (progress >= 1f) {
                stop(key);
            }
        });
        timers.put(key, timer);
        timer.start();
    }

    public void stopAll() {
        for (Timer timer : timers.values()) {
            timer.stop();
        }
        timers.clear();
    }

    private void stop(String key) {
        Timer timer = timers.remove(key);
        if (timer != null) {
            timer.stop();
        }
    }

    private float progress(long start) {
        long elapsed = System.currentTimeMillis() - start;
        return Math.min(1f, elapsed / (float) duration);
    }

    private Color lerp(Color from, Color to, float progress) {
        int r = Math.round(from.getRed() + ((to.getRed() - from.getRed()) * progress));
        int g = Math.round(from.getGreen() + ((to.getGreen() - from.getGreen()) * progress));
        int b = Math.round(from.getBlue() + ((to.getBlue() - from.getBlue()) * progress));
        int a = Math.round(from.getAlpha() + ((to.getAlpha() - from.getAlpha()) * progress));
        return new Color(r, g, b, a);
    }
}
