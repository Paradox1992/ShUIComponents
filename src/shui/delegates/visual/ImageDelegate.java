package shui.delegates.visual;

import shui.contracts.visual.Imageable;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.JComponent;

/**
 * Delegate que encapsula toda la lógica de imagen de fondo para componentes que
 * implementan {@link Imageable}.
 *
 * <p>
 * Usa {@link Icon} como tipo de imagen, compatible con ImageIcon, SVGIcon, etc.
 * La imagen se dibuja recortada al {@link Shape} del componente.</p>
 *
 * <p>
 * FIX: Se reemplazó {@code drawImage()} por {@code Icon.paintIcon()} que es el
 * método correcto para renderizar un Icon. Además se corrigió
 * {@code getIconHeight} que se usaba para obtener el ancho (debía ser
 * {@code getIconWidth}).</p>
 */
public class ImageDelegate implements Imageable {

    private final JComponent owner;

    private Icon image = null;
    private boolean enabled = false;
    private ImageScale scale = ImageScale.FILL;

    public ImageDelegate(JComponent owner) {
        this.owner = owner;
    }

    // ── Imageable ────────────────────────────────────────────────────────────
    @Override
    public void setImage(Icon image) {
        this.image = image;
        this.enabled = (image != null);
        owner.repaint();
    }

    @Override
    public Icon getImage() {
        return image;
    }

    @Override
    public void setImageEnabled(boolean enabled) {
        this.enabled = enabled && (image != null);
        owner.repaint();
    }

    @Override
    public boolean isImageEnabled() {
        return enabled;
    }

    @Override
    public void setImageScale(ImageScale scale) {
        if (scale == null) {
            return;
        }
        this.scale = scale;
        owner.repaint();
    }

    @Override
    public ImageScale getImageScale() {
        return scale;
    }

    // ── Pintado ──────────────────────────────────────────────────────────────
    public void paint(Graphics2D g2, Shape shape) {
        if (!enabled || image == null || shape == null) {
            return;
        }

        // FIX: getIconWidth() para ancho, getIconHeight() para alto
        int iw = image.getIconWidth();
        int ih = image.getIconHeight();

        if (iw <= 0 || ih <= 0) {
            return;
        }

        int pw = owner.getWidth();
        int ph = owner.getHeight();

        int x = 0, y = 0, w = pw, h = ph;

        switch (scale) {
            case FIT -> {
                double ratio = Math.min((double) pw / iw, (double) ph / ih);
                w = (int) (iw * ratio);
                h = (int) (ih * ratio);
                x = (pw - w) / 2;
                y = (ph - h) / 2;
            }
            case CENTER -> {
                w = iw;
                h = ih;
                x = (pw - w) / 2;
                y = (ph - h) / 2;
            }
            case FILL -> {
                /* x=0,y=0,w=pw,h=ph ya asignados */ }
        }

        Graphics2D copy = (Graphics2D) g2.create();
        copy.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        copy.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        copy.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Recortar al shape para respetar esquinas redondeadas
        copy.clip(shape);

        if (scale == ImageScale.CENTER || w == iw && h == ih) {
            // FIX: Icon se renderiza con paintIcon(), no con drawImage()
            image.paintIcon(owner, copy, x, y);
        } else {
            // Para FILL y FIT hay que escalar: convertir Icon a BufferedImage
            // y dibujar escalado con Graphics2D
            BufferedImage buf = iconToBufferedImage(iw, ih);
            copy.drawImage(buf, x, y, w, h, null);
        }

        copy.dispose();
    }

    /**
     * Convierte el {@link Icon} a {@link BufferedImage} para poder escalarlo.
     * Necesario porque Icon.paintIcon() no acepta dimensiones de destino.
     */
    private BufferedImage iconToBufferedImage(int iw, int ih) {
        BufferedImage buf = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_ARGB);
        Graphics2D bg = buf.createGraphics();
        bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        image.paintIcon(owner, bg, 0, 0);
        bg.dispose();
        return buf;
    }
}

