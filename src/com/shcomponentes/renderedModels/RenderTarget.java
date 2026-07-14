package com.shcomponentes.renderedModels;

/**
 * Alcance visual de una regla de renderizado.
 */
public enum RenderTarget {

    /**
     * Aplica el estilo a toda la fila cuando la regla coincide.
     */
    ROW,

    /**
     * Aplica el estilo solo a la celda de la columna configurada.
     */
    CELL
}
