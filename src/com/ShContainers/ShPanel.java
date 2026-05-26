package com.ShContainers;

import shui.components.base.BaseContainer;
import static shui.config.colors.BaseContainerColors.EMPTY_BG;
/**
 * Panel Swing de propósito general con esquinas redondeadas, colores, sombra,
 * degradado, borde y control de acceso integrados.
 *
 * <p>
 * Extiende {@link BaseContainer}, por lo que hereda automáticamente todas las
 * capacidades visuales y de seguridad sin necesidad de código adicional
 * .</p>
 */
public class ShPanel extends BaseContainer {

    // ── Constructores ────────────────────────────────────────────────────────
    /**
     * Crea un ShPanel con valores por defecto (radio 10, fondo blanco).
     */
    public ShPanel() {
        super(10, EMPTY_BG);
        setBackground(EMPTY_BG);
    }

}

