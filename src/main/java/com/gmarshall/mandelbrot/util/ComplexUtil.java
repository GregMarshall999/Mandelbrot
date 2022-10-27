package com.gmarshall.mandelbrot.util;

/**
 * Static methods for Complex number manipulation
 */
public final class ComplexUtil {

    /**
     * Adds the complex number to this one.
     * (x1 + x2) (iy1 + iy2)
     * @param firstComplex -
     * @param secondComplex -
     * @return - sum of both complex numbers
     */
    public static ComplexNumber addComplex(ComplexNumber firstComplex, ComplexNumber secondComplex) {
        return new ComplexNumber(firstComplex.getReal() + secondComplex.getReal(), firstComplex.getImaginary() + secondComplex.getImaginary());
    }

    /**
     * Takes a complex number and returns it's squared value
     * @param complex - complex number (x+iy)
     * @return - (x+iy)(x+iy)
     */
    public static ComplexNumber squareComplex(ComplexNumber complex) {
        double real = complex.getReal()*complex.getReal() - complex.getImaginary()*complex.getImaginary();
        double imaginary = 2*(complex.getReal()*complex.getImaginary());

        return new ComplexNumber(real, imaginary);
    }

    /**
     * Returns the absolute value of a complex number
     * IE the distance of the representative point on the grid to the grids origin
     * @param toAbs - complex number to absolute
     * @return - Distance from the grid origin and the complex coordinates
     */
    public static double absoluteComplex(ComplexNumber toAbs) {
        return Math.sqrt(toAbs.getReal()*toAbs.getReal() + toAbs.getImaginary()*toAbs.getImaginary());
    }
}
