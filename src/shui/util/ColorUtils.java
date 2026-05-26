package shui.util;

public final class ColorUtils {

    private ColorUtils() {
        // Evita instanciación
    }

    /**
     * Convierte un String hexadecimal a un objeto Color.
     *
     * Soporta formatos: - "RRGGBB" - "#RRGGBB" - "0xRRGGBB" - "AARRGGBB" (con
     * alpha) - "#AARRGGBB"
     *
     * @param hex valor hexadecimal del color
     * @return instancia de Color
     * @throws IllegalArgumentException si el formato es inválido
     */
    public static java.awt.Color hexToColor(String hex) {
        if (hex == null) {
            throw new IllegalArgumentException("El valor hexadecimal no puede ser null");
        }

        String normalized = hex.trim()
                .replace("#", "")
                .replace("0x", "")
                .replace("0X", "");

        try {
            long value = Long.parseLong(normalized, 16);

            switch (normalized.length()) {
                case 6 -> {
                    // RRGGBB
                    return new java.awt.Color((int) value);
                }
                case 8 -> {
                    // AARRGGBB
                    return new java.awt.Color((int) value, true);
                }
                default ->
                    throw new IllegalArgumentException("Formato hexadecimal inválido: " + hex);
            }

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Error al parsear el color hexadecimal: " + hex, e);
        }
    }
}

