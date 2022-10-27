package com.gmarshall.mandelbrot;

import com.gmarshall.mandelbrot.util.Util;

import javax.swing.JFrame;

/**
 * This program attempts to show the incredible beauty of the mathematical world and our encounter with the mandelbrot set
 */
public class Main extends JFrame {

    public Main() {
        super("Mandelbrot");

        add(new Graph());

        setSize(Util.SCREEN_WIDTH, Util.SCREEN_HEIGHT);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new Main();
    }
}