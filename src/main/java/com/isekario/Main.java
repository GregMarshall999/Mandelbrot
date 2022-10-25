package com.isekario;

import javax.swing.*;

/**
 * This program attempts to show the incredible beauty of the mathematical world and our encounter with the mandelbrot set
 */
public class Main extends JFrame {

    //Screen dimensions
    private static final int WIDTH = 1600;
    private static final int HEIGHT = 900;

    public Main() {
        super("Mandelbrot");

        add(new Graph());

        setSize(WIDTH, HEIGHT);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static void main(String[] args) {
        new Main();
    }
}