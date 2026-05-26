package shui.components.base;

import shui.contracts.visual.Animatable;
import shui.contracts.visual.Borderable;
import shui.contracts.visual.Colorable;
import shui.contracts.visual.Gradientable;
import shui.contracts.visual.Hoverable;
import shui.contracts.visual.Imageable;
import shui.contracts.visual.Shadowable;
import shui.contracts.visual.Shapeable;
import shui.contracts.visual.Stateable;
import shui.contracts.visual.Themeable;
import shui.contracts.visual.VisualState;
import shui.delegates.visual.AnimationDelegate;
import shui.delegates.visual.BorderDelegate;
import shui.delegates.visual.ColorDelegate;
import shui.delegates.visual.GradientDelegate;
import shui.delegates.visual.HoverDelegate;
import shui.delegates.visual.ImageDelegate;
import shui.delegates.visual.ShadowDelegate;
import shui.delegates.visual.ShapeDelegate;
import shui.delegates.visual.StateDelegate;
import shui.delegates.visual.ThemeDelegate;
import shui.security.AbstractSecuredComponent;
import shui.theme.ShTheme;
import java.awt.*;
import javax.swing.Icon;
import javax.swing.JPanel;
import shui.security.Contexable;

/**
 * Componente base abstracto que unifica todas las capacidades visuales y de
 * seguridad mediante composición con delegates especializados.
 *
 * <h3>Correcciones aplicadas:</h3>
 * <ul>
 * <li>FIX: setCornerRadius sincroniza el radio al ShadowDelegate para que la
 * sombra siga la forma redondeada del componente.</li>
 * <li>FIX: Cualquier apertura de JColorChooser desde listeners de este
 * componente debe hacerse con SwingUtilities.invokeLater para no bloquear el
 * EDT y permitir que el botón "Aceptar" responda.</li>
 * </ul>
 *
 * <h3>Orden de pintado en {@code paintComponent}:</h3>
 * <ol>
 * <li>Alpha global (composite)</li>
 * <li>Sombra</li>
 * <li>Fondo (degradado si activo, color plano si no)</li>
 * <li>Hover overlay</li>
 * <li>Borde</li>
 * <li>Contenido Swing ({@code super.paintComponent})</li>
 * </ol>
 */
public abstract class BaseContainer extends JPanel
        implements Borderable, Colorable, Shadowable, Gradientable, Shapeable, Hoverable,
        Imageable, Stateable, Animatable, Themeable, Contexable {

    private final BorderDelegate borderDelegate;
    private final ColorDelegate colorDelegate;
    private final ShadowDelegate shadowDelegate;
    private final GradientDelegate gradientDelegate;
    private final ShapeDelegate shapeDelegate;
    private final HoverDelegate hoverDelegate;
    private final ImageDelegate imageDelegate;
    private final StateDelegate stateDelegate;
    private final AnimationDelegate animationDelegate;
    private final ThemeDelegate themeDelegate;
    private final AbstractSecuredComponent securityDelegate;

    protected BaseContainer(int cornerRadius, Color backgroundColor) {
        setOpaque(false);
        this.shapeDelegate = new ShapeDelegate(this, cornerRadius);
        this.colorDelegate = new ColorDelegate(this, backgroundColor);
        this.shadowDelegate = new ShadowDelegate(this);
        this.gradientDelegate = new GradientDelegate(this);
        this.borderDelegate = new BorderDelegate(this);
        this.hoverDelegate = new HoverDelegate(this);
        this.imageDelegate = new ImageDelegate(this);
        this.stateDelegate = new StateDelegate(this);
        this.animationDelegate = new AnimationDelegate(this);
        this.themeDelegate = new ThemeDelegate(this);
        this.securityDelegate = new AbstractSecuredComponent() {
        };
        // FIX: sincronizar radio inicial al ShadowDelegate
        this.shadowDelegate.setCornerRadius(cornerRadius);
        this.themeDelegate.register(this);
        applyTheme();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        applyRenderingHints(g2);

        int w = getWidth();
        int h = getHeight();

        Composite originalComposite = colorDelegate.applyAlpha(g2);

        shadowDelegate.paint(g2, 0, 0, w, h);

        Shape shape = shapeDelegate.getShape(w, h);

        Color stateBackground = stateDelegate.getResolvedBackgroundColor();
        if (stateBackground != null) {
            g2.setColor(stateBackground);
            g2.fill(shape);
        } else if (gradientDelegate.isGradientEnabled()) {
            gradientDelegate.paint(g2, shape, w, h);
        } else {
            colorDelegate.paint(g2, shape);
        }

        shadowDelegate.paintInner(g2, shape);

        // pintar imagen aquí, antes de dispose y antes de super
        imageDelegate.paint(g2, shape);

        onPaintBackground(g2, shape);

        stateDelegate.paintOverlay(g2, shape);

        hoverDelegate.paint(g2, shape);

        borderDelegate.paint(g2, shape, stateDelegate.getResolvedBorderColor());

        stateDelegate.paintFocusRing(g2, shape);

        g2.setComposite(originalComposite);
        g2.dispose();

        super.paintComponent(g);
    }

    private void applyRenderingHints(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    protected void onPaintBackground(Graphics2D g2, Shape shape) {
    }

    protected void onAccessGranted() {
    }

    protected void onAccessDenied() {
    }

    @Override
    public Insets getInsets() {
        int pad = shapeDelegate.getRecommendedPadding();
        return new Insets(pad, pad, pad, pad);
    }

    // ── Borderable ───────────────────────────────────────────────────────────
    @Override
    public void setBorder(float width, Color color) {
        borderDelegate.setBorder(width, color);
    }

    @Override
    public void setBorderEnabled(boolean enabled) {
        borderDelegate.setBorderEnabled(enabled);
    }

    @Override
    public boolean isBorderEnabled() {
        return borderDelegate.isBorderEnabled();
    }

    @Override
    public float getBorderWidth() {
        return borderDelegate.getBorderWidth();
    }

    @Override
    public void setBorderWidth(float width) {
        borderDelegate.setBorderWidth(width);
    }

    @Override
    public Color getBorderColor() {
        return borderDelegate.getBorderColor();
    }

    @Override
    public void setBorderColor(Color color) {
        borderDelegate.setBorderColor(color);
    }

    @Override
    public void setBorderStyle(BorderStyle style) {
        borderDelegate.setBorderStyle(style);
    }

    @Override
    public BorderStyle getBorderStyle() {
        return borderDelegate.getBorderStyle();
    }

    @Override
    public void setBorderSides(boolean top, boolean right, boolean bottom, boolean left) {
        borderDelegate.setBorderSides(top, right, bottom, left);
    }

    @Override
    public boolean isTopBorderVisible() {
        return borderDelegate.isTopBorderVisible();
    }

    @Override
    public boolean isRightBorderVisible() {
        return borderDelegate.isRightBorderVisible();
    }

    @Override
    public boolean isBottomBorderVisible() {
        return borderDelegate.isBottomBorderVisible();
    }

    @Override
    public boolean isLeftBorderVisible() {
        return borderDelegate.isLeftBorderVisible();
    }

    @Override
    public void setBorderGradient(Color start, Color end) {
        borderDelegate.setBorderGradient(start, end);
    }

    @Override
    public void setBorderGradientEnabled(boolean enabled) {
        borderDelegate.setBorderGradientEnabled(enabled);
    }

    @Override
    public boolean isBorderGradientEnabled() {
        return borderDelegate.isBorderGradientEnabled();
    }

    @Override
    public Color getBorderGradientStart() {
        return borderDelegate.getBorderGradientStart();
    }

    @Override
    public void setBorderGradientStart(Color color) {
        borderDelegate.setBorderGradientStart(color);
    }

    @Override
    public Color getBorderGradientEnd() {
        return borderDelegate.getBorderGradientEnd();
    }

    @Override
    public void setBorderGradientEnd(Color color) {
        borderDelegate.setBorderGradientEnd(color);
    }

    // ── Colorable ────────────────────────────────────────────────────────────
    @Override
    public void setBackgroundColor(Color color) {
        colorDelegate.setBackgroundColor(color);
    }

    @Override
    public Color getBackgroundColor() {
        return colorDelegate.getBackgroundColor();
    }

    @Override
    public void setAlpha(float alpha) {
        colorDelegate.setAlpha(alpha);
    }

    @Override
    public float getAlpha() {
        return colorDelegate.getAlpha();
    }

    // ── Shadowable ───────────────────────────────────────────────────────────
    @Override
    public void setShadow(int blur, Color color) {
        shadowDelegate.setShadow(blur, color);
    }

    @Override
    public void setShadow(int blur, Color color, int ox, int oy) {
        shadowDelegate.setShadow(blur, color, ox, oy);
    }

    @Override
    public void setShadowEnabled(boolean enabled) {
        shadowDelegate.setShadowEnabled(enabled);
    }

    @Override
    public boolean isShadowEnabled() {
        return shadowDelegate.isShadowEnabled();
    }

    @Override
    public int getShadowBlur() {
        return shadowDelegate.getShadowBlur();
    }

    @Override
    public Color getShadowColor() {
        return shadowDelegate.getShadowColor();
    }

    @Override
    public int getShadowOffsetX() {
        return shadowDelegate.getShadowOffsetX();
    }

    @Override
    public int getShadowOffsetY() {
        return shadowDelegate.getShadowOffsetY();
    }

    @Override
    public void setShadowBlur(int blur) {
        shadowDelegate.setShadowBlur(blur);
    }

    @Override
    public void setShadowColor(Color color) {
        shadowDelegate.setShadowColor(color);
    }

    @Override
    public void setShadowOffset(int offsetX, int offsetY) {
        shadowDelegate.setShadowOffset(offsetX, offsetY);
    }

    @Override
    public void setShadowElevation(ShadowElevation elevation) {
        shadowDelegate.setShadowElevation(elevation);
    }

    @Override
    public ShadowElevation getShadowElevation() {
        return shadowDelegate.getShadowElevation();
    }

    @Override
    public void setInnerShadowEnabled(boolean enabled) {
        shadowDelegate.setInnerShadowEnabled(enabled);
    }

    @Override
    public boolean isInnerShadowEnabled() {
        return shadowDelegate.isInnerShadowEnabled();
    }

    @Override
    public void setInnerShadow(Color color, int blur) {
        shadowDelegate.setInnerShadow(color, blur);
    }

    @Override
    public Color getInnerShadowColor() {
        return shadowDelegate.getInnerShadowColor();
    }

    @Override
    public int getInnerShadowBlur() {
        return shadowDelegate.getInnerShadowBlur();
    }

    // ── Gradientable ─────────────────────────────────────────────────────────
    @Override
    public void setGradient(Color s, Color e) {
        gradientDelegate.setGradient(s, e);
    }

    @Override
    public void setGradient(Color s, Color e, Gradientable.GradientDirection d) {
        gradientDelegate.setGradient(s, e, d);
    }

    @Override
    public void setGradientEnabled(boolean enabled) {
        gradientDelegate.setGradientEnabled(enabled);
    }

    @Override
    public boolean isGradientEnabled() {
        return gradientDelegate.isGradientEnabled();
    }

    @Override
    public Color getGradientStart() {
        return gradientDelegate.getGradientStart();
    }

    @Override
    public void setGradientStart(Color color) {
        gradientDelegate.setGradientStart(color);
    }

    @Override
    public Color getGradientEnd() {
        return gradientDelegate.getGradientEnd();
    }

    @Override
    public void setGradientEnd(Color color) {
        gradientDelegate.setGradientEnd(color);
    }

    @Override
    public Gradientable.GradientDirection getGradientDirection() {
        return gradientDelegate.getGradientDirection();
    }

    // ── Shapeable ────────────────────────────────────────────────────────────
    @Override
    public void setCornerRadius(int radius) {
        shapeDelegate.setCornerRadius(radius);
        // FIX: propagar radio al ShadowDelegate para sombra con forma correcta
        shadowDelegate.setCornerRadius(radius);
    }

    @Override
    public int getCornerRadius() {
        return shapeDelegate.getCornerRadius();
    }

    @Override
    public void setTopLeftRounded(boolean r) {
        shapeDelegate.setTopLeftRounded(r);
    }

    @Override
    public void setTopRightRounded(boolean r) {
        shapeDelegate.setTopRightRounded(r);
    }

    @Override
    public void setBottomLeftRounded(boolean r) {
        shapeDelegate.setBottomLeftRounded(r);
    }

    @Override
    public void setBottomRightRounded(boolean r) {
        shapeDelegate.setBottomRightRounded(r);
    }

    @Override
    public boolean isTopLeftRounded() {
        return shapeDelegate.isTopLeftRounded();
    }

    @Override
    public boolean isTopRightRounded() {
        return shapeDelegate.isTopRightRounded();
    }

    @Override
    public boolean isBottomLeftRounded() {
        return shapeDelegate.isBottomLeftRounded();
    }

    @Override
    public boolean isBottomRightRounded() {
        return shapeDelegate.isBottomRightRounded();
    }

    @Override
    public void setAllCornersRounded(boolean r) {
        shapeDelegate.setAllCornersRounded(r);
    }

    @Override
    public void setTopCornersRounded(boolean r) {
        shapeDelegate.setTopCornersRounded(r);
    }

    @Override
    public void setBottomCornersRounded(boolean r) {
        shapeDelegate.setBottomCornersRounded(r);
    }

    // ── Hoverable ────────────────────────────────────────────────────────────
    @Override
    public void setHoverEnabled(boolean enabled) {
        hoverDelegate.setHoverEnabled(enabled);
    }

    @Override
    public boolean isHoverEnabled() {
        return hoverDelegate.isHoverEnabled();
    }

    @Override
    public void setHoverColor(Color color) {
        hoverDelegate.setHoverColor(color);
    }

    @Override
    public Color getHoverColor() {
        return hoverDelegate.getHoverColor();
    }

    // ── Stateable ────────────────────────────────────────────────────────────
    @Override
    public void setVisualState(VisualState state) {
        stateDelegate.setVisualState(state);
    }

    @Override
    public VisualState getVisualState() {
        return stateDelegate.getVisualState();
    }

    @Override
    public void setSelected(boolean selected) {
        stateDelegate.setSelected(selected);
    }

    @Override
    public boolean isSelected() {
        return stateDelegate.isSelected();
    }

    @Override
    public void setStateOverlayColor(VisualState state, Color color) {
        stateDelegate.setStateOverlayColor(state, color);
    }

    @Override
    public Color getStateOverlayColor(VisualState state) {
        return stateDelegate.getStateOverlayColor(state);
    }

    @Override
    public void setStateBackgroundColor(VisualState state, Color color) {
        stateDelegate.setStateBackgroundColor(state, color);
    }

    @Override
    public Color getStateBackgroundColor(VisualState state) {
        return stateDelegate.getStateBackgroundColor(state);
    }

    @Override
    public void setStateBorderColor(VisualState state, Color color) {
        stateDelegate.setStateBorderColor(state, color);
    }

    @Override
    public Color getStateBorderColor(VisualState state) {
        return stateDelegate.getStateBorderColor(state);
    }

    @Override
    public void setFocusRingEnabled(boolean enabled) {
        stateDelegate.setFocusRingEnabled(enabled);
    }

    @Override
    public boolean isFocusRingEnabled() {
        return stateDelegate.isFocusRingEnabled();
    }

    @Override
    public void setFocusRingColor(Color color) {
        stateDelegate.setFocusRingColor(color);
    }

    @Override
    public Color getFocusRingColor() {
        return stateDelegate.getFocusRingColor();
    }

    // ── Animatable ───────────────────────────────────────────────────────────
    @Override
    public void setAnimationsEnabled(boolean enabled) {
        animationDelegate.setEnabled(enabled);
    }

    @Override
    public boolean isAnimationsEnabled() {
        return animationDelegate.isEnabled();
    }

    @Override
    public void setAnimationDuration(int duration) {
        animationDelegate.setDuration(duration);
    }

    @Override
    public int getAnimationDuration() {
        return animationDelegate.getDuration();
    }

    @Override
    public void animateBackgroundTo(Color color) {
        animationDelegate.animateColor("background",
                colorDelegate.getBackgroundColor(), color, colorDelegate::setBackgroundColor);
    }

    @Override
    public void animateBorderColorTo(Color color) {
        animationDelegate.animateColor("border",
                borderDelegate.getBorderColor(), color, borderDelegate::setBorderColor);
    }

    @Override
    public void animateAlphaTo(float alpha) {
        animationDelegate.animateFloat("alpha", colorDelegate.getAlpha(), alpha, colorDelegate::setAlpha);
    }

    @Override
    public void stopAnimations() {
        animationDelegate.stopAll();
    }

    // ── Themeable ────────────────────────────────────────────────────────────
    @Override
    public void setTheme(ShTheme theme) {
        themeDelegate.setTheme(theme);
        applyTheme();
    }

    @Override
    public ShTheme getTheme() {
        return themeDelegate.getTheme();
    }

    @Override
    public void applyTheme() {
        ShTheme theme = themeDelegate.getTheme();
        if (theme == null) {
            return;
        }

        setBackgroundColor(theme.getBackgroundColor());
        setHoverColor(theme.getHoverOverlayColor());
        setFocusRingColor(theme.getFocusRingColor());
        setCornerRadius(theme.getCornerRadius());

        setBorderColor(theme.getBorderColor());
        setBorderWidth(theme.getBorderWidth());
        setBorderStyle(theme.getBorderStyle());
        setBorderEnabled(theme.isBorderEnabled());

        setShadowColor(theme.getShadowColor());
        setShadowElevation(theme.getShadowElevation());
        setShadowEnabled(theme.isShadowEnabled());

        for (VisualState state : VisualState.values()) {
            Color overlay = theme.getStateOverlayColor(state);
            if (overlay != null) {
                setStateOverlayColor(state, overlay);
            }
            Color background = theme.getStateBackgroundColor(state);
            if (background != null) {
                setStateBackgroundColor(state, background);
            }
            Color border = theme.getStateBorderColor(state);
            if (border != null) {
                setStateBorderColor(state, border);
            }
        }
    }

    // ── Contexable ───────────────────────────────────────────────────────────
    @Override
    public void setContext(shui.security.ContextInfo contextInfo) {
        securityDelegate.setContext(contextInfo);
    }

    @Override
    @java.beans.Transient
    public shui.security.ContextInfo getContext() {
        return securityDelegate.getContext();
    }

    @Override
    public void setGradientDirection(GradientDirection direction) {
        gradientDelegate.setGradientDirection(direction);
    }

    @Override
    public void setImage(Icon image) {
        this.imageDelegate.setImage(image);
    }

    @Override
    public void setImageEnabled(boolean enabled) {
        this.imageDelegate.setImageEnabled(enabled);
    }

    @Override
    public void setImageScale(ImageScale scale) {
        this.imageDelegate.setImageScale(scale);
    }

    @Override
    public boolean isImageEnabled() {
        return this.imageDelegate.isImageEnabled();
    }

    @Override
    public Icon getImage() {
        return this.imageDelegate.getImage();
    }

    @Override
    public ImageScale getImageScale() {
        return this.imageDelegate.getImageScale();
    }

}
