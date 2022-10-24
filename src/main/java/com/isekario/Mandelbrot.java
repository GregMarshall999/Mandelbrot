package com.isekario;

public class Mandelbrot {

    public static ComplexNumber squareComplex(ComplexNumber complex) {
        double real = complex.getReal()*complex.getReal() - complex.getImaginary()*complex.getImaginary();
        double imaginary = 2*(complex.getReal()*complex.getImaginary());

        ComplexNumber squared = new ComplexNumber(real, imaginary);

        return squared;
    }

    public static ComplexNumber MandelbrotSequence(ComplexNumber xComplex, ComplexNumber cComplex) {
        ComplexNumber nextIterationValue = squareComplex(xComplex);
        nextIterationValue.addComplex(cComplex);

        return nextIterationValue;
    }
}
