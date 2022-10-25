package com.isekario.util;

/*
 *
 * The mandelbrot set is defined by the set of complex numbers for which
 * the complex numbers of the sequence Zn remain bounded in absolute value.
 *
 * The sequence Zn is defined by:
 * Z0 = 0
 * Zn+1 = Zn² + c
 *
 * As a reminder, the modulus of a complex number is its distance to 0. We assume that
 * the sequence Zn is not bounded if the modulus of one of its terms is greater than 2.
 *
 * A complex number (x + iy) can be represented on a complex plane. The real part of the complex number is
 * represented by a displacement along the x-axis and the imaginary part by a displacement along the y-axis.
 *
 * The visual representation of the mandelbrot set may be created by determining, for each point of a part of the complex plane,
 * whether Zn is bounded. The number of iterations to reach a modulus greater than 2 can be used to determine the color to use.
 *
 */

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
    public static ComplexNumber mandelbrotSequence(ComplexNumber xComplex, ComplexNumber cComplex) {
        return ComplexUtil.addComplex(ComplexUtil.squareComplex(xComplex), cComplex);
    }

    /*
     *
     *  Plotting the mandelbrot set is relatively simple:
     *
     * - Iterate over all the pixels of the image
     * - Convert the coordinate of the pixel into a complex number of the complex plane
     * - If mandelbrot reaches the maximum iteration, plot a black pixel,
     *   otherwise plot a pixel in a color that depends on the number of iterations returned by mandelbrot
     *
     * This is called the "Escape time algorithm".
     *
     * */

    /**
     * Checks if the value of C is part of the mandelbrot set
     * Depending on how deep we go, we may reach different certainties as to being part of the set or not
     * @param constant - Point we wish to test
     * @param iterationMax - How deep we go down the rabit hole
     * @return - How certain we are that the sequence is stable
     */
    public static int escapeTimeAlgorithm(ComplexNumber constant, int iterationMax)
    {
        ComplexNumber nextIteration = mandelbrotSequence(new ComplexNumber(0.0, 0.0), constant);
        int i = 0;

        while (i < iterationMax){
            if(ComplexUtil.absoluteComplex(nextIteration) > 2 //|Zn| > 2
                    || nextIteration.getReal()*nextIteration.getReal() + nextIteration.getImaginary()*nextIteration.getImaginary() > 4) //x² + y² > 4
                return i;

            nextIteration = mandelbrotSequence(nextIteration, constant);
            i++;
        }

        return i;
    }
}
