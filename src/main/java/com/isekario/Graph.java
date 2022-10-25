package com.isekario;

import com.isekario.util.ComplexNumber;
import com.isekario.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import static com.isekario.util.Util.*;

/**
 * Screen element to display all operations for the mandelbrot set
 */
public class Graph extends JPanel implements MouseListener, MouseMotionListener {

    private Color[][] pixels;

    public Graph() {
        pixels = new Color[Main.getWIDTH()][Main.getHEIGHT()];
        Util.plotMandelbrot(pixels);

        reCalculateSequence();

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public void paint(Graphics g) {
        g.clearRect(0, 0, Main.getWIDTH(), Main.getHEIGHT());
        g.setFont(new Font("Arial", Font.PLAIN, 20));

        drawMandelbrot(g);
        drawGraph(g);
        drawStableCircle(g);
        drawSequence(g);
        drawXPoint(g);
        drawCPoint(g);
    }

    /**
     * Displays the 2D graph
     * @param g -
     */
    private void drawGraph(Graphics g) {
        g.setColor(Color.DARK_GRAY);

        //X,Y axes
        g.drawLine(0, gridCenterFocusY, Main.getWIDTH(), gridCenterFocusY);
        g.drawLine(gridCenterFocusX, 0, gridCenterFocusX, Main.getHEIGHT());

        //X and Y intervals with numbers
        for (int i = -8; i < 9; i++) {
            g.drawLine(gridCenterFocusX + i*zoomLevel, gridCenterFocusY - intervalSize,
                    gridCenterFocusX + i*zoomLevel, gridCenterFocusY + intervalSize); //X
            g.drawLine(gridCenterFocusX - intervalSize, gridCenterFocusY + i*zoomLevel,
                    gridCenterFocusX + intervalSize, gridCenterFocusY + i*zoomLevel); //Y

            g.drawString(""+i, gridCenterFocusX + i*zoomLevel + numberOffsetX, gridCenterFocusY + numberOffsetY); //X
            g.drawString(""+-i, gridCenterFocusX + numberOffsetX, gridCenterFocusY + i*zoomLevel + numberOffsetY); //Y
        }
    }

    /**
     * Draw the red point
     * @param g -
     */
    private void drawXPoint(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(zPosX - dotSize/2, zPosY - dotSize/2, dotSize, dotSize);
        g.drawString(convertXToCoordsString(), 10, 30);
    }

    /**
     * Draw the green point
     * @param g -
     */
    private void drawCPoint(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillOval(cPosX - dotSize/2, cPosY - dotSize/2, dotSize, dotSize);
        g.drawString(convertCToCoordsString(), 10, 60);
    }

    /**
     * Draw stability boundary for C = 0
     * @param g -
     */
    private void drawStableCircle(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawOval(gridCenterFocusX - zoomLevel, gridCenterFocusY - zoomLevel, zoomLevel*2, zoomLevel*2);
    }

    /**
     * Draw all points in the sequence
     * @param g -
     */
    private void drawSequence(Graphics g) {
        g.setColor(Color.ORANGE);

        for (ComplexNumber complex : sequence) {
            int x = fromCoordsToScreenPos(complex.getReal() ,true);
            int y = fromCoordsToScreenPos(complex.getImaginary(), false);

            g.fillOval(x-dotSize/2, y-dotSize/2, dotSize, dotSize);
        }
    }

    private void drawMandelbrot(Graphics g) {
        for(int x = 0; x < pixels.length; x++)
            for(int y = 0; y < pixels[x].length; y++)
            {
                g.setColor(pixels[x][y]);
                g.drawRect(x, y, 1, 1);
            }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (isXSelected)
        {
            zPosX = e.getX();
            zPosY = e.getY();

            reCalculateSequence();

            repaint();
        }

        if(isCSelected)
        {
            cPosX = e.getX();
            cPosY = e.getY();

            reCalculateSequence();

            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        //did we click the red dot
        isXSelected =   e.getX() >= zPosX - dotSize/2 &&
                        e.getX() <= zPosX + dotSize/2 &&
                        e.getY() >= zPosY - dotSize/2 &&
                        e.getY() <= zPosY + dotSize/2;

        isCSelected =   e.getX() >= cPosX - dotSize/2 &&
                        e.getX() <= cPosX + dotSize/2 &&
                        e.getY() >= cPosY - dotSize/2 &&
                        e.getY() <= cPosY + dotSize/2;
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
