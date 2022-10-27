package com.gmarshall.mandelbrot;

import com.gmarshall.mandelbrot.util.ComplexNumber;
import com.gmarshall.mandelbrot.util.Util;

import javax.swing.JPanel;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import static com.gmarshall.mandelbrot.util.Util.*;

/**
 * Screen element to display all operations for the mandelbrot set
 */
public class Graph extends JPanel implements MouseListener, MouseMotionListener {

    public Graph() {

        //region Buttons
        Button increaseIterations = new Button("Increase Iterations");
        Button decreaseIterations = new Button("Decrease Iterations");
        Button zoomIn = new Button("Zoom in");
        Button zoomOut = new Button("Zoom out");

        increaseIterations.setBounds(Util.SCREEN_WIDTH - 300, 40, 150, 30);
        decreaseIterations.setBounds(Util.SCREEN_WIDTH - 300, 70, 150, 30);
        zoomIn.setBounds(Util.SCREEN_WIDTH /2 - 100, Util.SCREEN_HEIGHT - 70, 100, 20);
        zoomOut.setBounds(Util.SCREEN_WIDTH /2, Util.SCREEN_HEIGHT - 70, 100, 20);

        increaseIterations.addActionListener(e -> changeIterations(1, Graph.this));
        decreaseIterations.addActionListener(e -> changeIterations(-1, Graph.this));
        zoomIn.addActionListener(e -> zoomScreen(zoomValue, Graph.this));
        zoomOut.addActionListener(e -> zoomScreen(-zoomValue /2, Graph.this));
        //endregion

        pixels = new Color[Util.SCREEN_WIDTH][Util.SCREEN_WIDTH];

        plotMandelbrot(pixels);
        reCalculateSequence();

        setLayout(null);
        add(increaseIterations);
        add(decreaseIterations);
        add(zoomIn);
        add(zoomOut);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public void paint(Graphics g) {
        g.clearRect(0, 0, Util.SCREEN_WIDTH, Util.SCREEN_HEIGHT);
        g.setFont(new Font("Arial", Font.PLAIN, 20));

        drawMandelbrot(g);
        drawGraph(g);
        drawSequence(g);

        //UI
        drawZPoint(g);
        drawCPoint(g);
        drawIterationCount(g);
    }

    //region Display

    /**
     * Displays the 2D graph
     * @param g -
     */
    private void drawGraph(Graphics g) {
        g.setColor(Color.DARK_GRAY);

        //X,Y axes
        g.drawLine(0, gridCenterY, Util.SCREEN_WIDTH, gridCenterY);
        g.drawLine(gridCenterX, 0, gridCenterX, Util.SCREEN_HEIGHT);

        //X and Y intervals with numbers
        for (int i = -8; i < 9; i++) {
            g.drawLine(gridCenterX + i* zoomValue, gridCenterY - intervalSize,
                    gridCenterX + i* zoomValue, gridCenterY + intervalSize); //X
            g.drawLine(gridCenterX - intervalSize, gridCenterY + i* zoomValue,
                    gridCenterX + intervalSize, gridCenterY + i* zoomValue); //Y

            g.drawString(""+i, gridCenterX + i* zoomValue + numberOffsetX, gridCenterY + numberOffsetY); //X
            g.drawString(""+-i, gridCenterX + numberOffsetX, gridCenterY + i* zoomValue + numberOffsetY); //Y
        }
    }

    /**
     * Draw the red point
     * @param g -
     */
    private void drawZPoint(Graphics g) {
        g.setColor(Color.BLUE);
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
     * Shows the current iterative depth of the sequence
     * @param g -
     */
    private void drawIterationCount(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString(iterationsMax + " iterations", Util.SCREEN_WIDTH - 300, 30);
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

    /**
     * Draws the mandelbrot set
     * All dark points  (representing the initial C point for the sequence) on the set result in stable orbits of the sequence
     * @param g -
     */
    private void drawMandelbrot(Graphics g) {
        for(int x = 0; x < pixels.length; x++)
            for(int y = 0; y < pixels[x].length; y++)
            {
                g.setColor(pixels[x][y]);
                g.drawRect(x, y, 1, 1);
            }
    }

    //endregion

    //region Mouse events

    @Override
    public void mouseDragged(MouseEvent e) {
        if (isXSelected)
        {
            zPosX = e.getX();
            zPosY = e.getY();

            reCalculateSequence();
            Util.plotMandelbrot(pixels);

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
        //did we click the red dot or the green?
        isXSelected =   e.getX() >= zPosX - dotSize/2 &&
                        e.getX() <= zPosX + dotSize/2 &&
                        e.getY() >= zPosY - dotSize/2 &&
                        e.getY() <= zPosY + dotSize/2;

        isCSelected =   e.getX() >= cPosX - dotSize/2 &&
                        e.getX() <= cPosX + dotSize/2 &&
                        e.getY() >= cPosY - dotSize/2 &&
                        e.getY() <= cPosY + dotSize/2;

        xOffset = e.getX();
        yOffset = e.getY();

        if(!isCSelected && !isXSelected) {
            focusedPoint = new ComplexNumber(fromScreenPosToCoords(e.getX(), true), -fromScreenPosToCoords(e.getY(), false));
            shiftScreenToClick(this);
        }
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

    //endregion
}
