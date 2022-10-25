package com.isekario.util;

/**
 * Contains the mandelbrot sequence operation
 */
public class MandelUtil {

    /**
     * Generates the next complex number in the sequence following this rule:
     * Zn <- complex number (x1+iy1)
     * C <- constant complex number (x2+iy2)
     * Zn = (Zn-1)² + C
     * @param xComplex - Zn-1 complex number
     * @param cComplex - constant complex number
     * @return - Zn
     */
    public static ComplexNumber MandelbrotSequence(ComplexNumber xComplex, ComplexNumber cComplex) {
        return ComplexUtil.addComplex(ComplexUtil.squareComplex(xComplex), cComplex);
    }

    //TODO: verify if C generates a stable sequence
}
