package com.ShContainers;

/**
 * Define el modo de trabajo de un formulario visual.
 *
 * <p>
 * Permite distinguir de forma explicita si un formulario esta creando un nuevo
 * registro o actualizando uno existente, sin depender del texto de botones u
 * otros elementos visuales.
 * </p>
 */
public interface FormMode {

    /**
     * Modos disponibles para un formulario.
     */
    enum Mode {
        /**
         * El formulario esta creando informacion nueva.
         */
        CREATE,
        /**
         * El formulario esta actualizando informacion existente.
         */
        UPDATE
    }

    /**
     * Establece el modo actual del formulario.
     *
     * @param mode modo que debe usar el formulario
     */
    void setFormMode(Mode mode);

    /**
     * Obtiene el modo actual del formulario.
     *
     * @return modo actual del formulario
     */
    Mode getFormMode();

    /**
     * Indica si el formulario esta en modo creacion.
     *
     * @return {@code true} si el modo actual es {@link Mode#CREATE}
     */
    default boolean isCreateMode() {
        return getFormMode() == Mode.CREATE;
    }

    /**
     * Indica si el formulario esta en modo actualizacion.
     *
     * @return {@code true} si el modo actual es {@link Mode#UPDATE}
     */
    default boolean isUpdateMode() {
        return getFormMode() == Mode.UPDATE;
    }
}
