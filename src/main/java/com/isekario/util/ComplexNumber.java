package com.isekario.util;

/**
 * Complex number with the form (x+iy)
 * X is the real number represented by the x value of the screen
 * Y is the imaginary number represented by the y value of the screen
 */
public class ComplexNumber
{
    private final double real;
    private final double imaginary;

    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }
}