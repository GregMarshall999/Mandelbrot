package com.isekario;

import com.isekario.util.ComplexNumber;
import com.isekario.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static com.isekario.util.Util.*;

/**
 * Screen element to display all operations for the mandelbrot set
 */
public class Graph extends JPanel implements MouseListener, MouseMotionListener {


    private Button increaseIterations, decreaseIterations, zoomIn, zoomOut;

    public Graph() {
        setLayout(null);

        pixels = new Color[Main.getWIDTH()][Main.getHEIGHT()];
        plotMandelbrot(pixels);

        increaseIterations = new Button("Increase Iterations");
        decreaseIterations = new Button("Decrease Iterations");
        zoomIn = new Button("Zoom in");
        zoomOut = new Button("Zoom out");

        increaseIterations.setBounds(Main.getWIDTH() - 300, 40, 150, 30);
        decreaseIterations.setBounds(Main.getWIDTH() - 300, 70, 150, 30);
        zoomIn.setBounds(Main.getWIDTH()/2 - 100, Main.getHEIGHT() - 70, 100, 20);
        zoomOut.setBounds(Main.getWIDTH()/2, Main.getHEIGHT() - 70, 100, 20);

        increaseIterations.addActionListener(e -> changeIterations(1, Graph.this));
        decreaseIterations.addActionListener(e -> changeIterations(-1, Graph.this));
        zoomIn.addActionListener(e -> zoomScreen(zoomValue, Graph.this));
        zoomOut.addActionListener(e -> zoomScreen(-zoomValue /2, Graph.this));

        reCalculateSequence();

        add(increaseIterations);
        add(decreaseIterations);
        add(zoomIn);
        add(zoomOut);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public void paint(Graphics g) {
        g.clearRect(0, 0, Main.getWIDTH(), Main.getHEIGHT());
        g.setFont(new Font("Arial", Font.PLAIN, 20));

        drawMandelbrot(g);
        drawGraph(g);
        //drawStableCircle(g);
        drawSequence(g);

        //UI
        drawZPoint(g);
        drawCPoint(g);
        drawIterationCount(g);
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
            g.drawLine(gridCenterFocusX + i* zoomValue, gridCenterFocusY - intervalSize,
                    gridCenterFocusX + i* zoomValue, gridCenterFocusY + intervalSize); //X
            g.drawLine(gridCenterFocusX - intervalSize, gridCenterFocusY + i* zoomValue,
                    gridCenterFocusX + intervalSize, gridCenterFocusY + i* zoomValue); //Y

            g.drawString(""+i, gridCenterFocusX + i* zoomValue + numberOffsetX, gridCenterFocusY + numberOffsetY); //X
            g.drawString(""+-i, gridCenterFocusX + numberOffsetX, gridCenterFocusY + i* zoomValue + numberOffsetY); //Y
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
     * Draw stability boundary for C = 0
     * @param g -
     */
    private void drawStableCircle(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawOval(gridCenterFocusX - zoomValue, gridCenterFocusY - zoomValue, zoomValue *2, zoomValue *2);
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

    private void drawIterationCount(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString(iterationsMax + " iterations", Main.getWIDTH() - 300, 30);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (isXSelected)
        {
            zPosX = e.getX();
            zPosY = e.getY();

            reCalculateSequence();
            Util.plotMandelbrot(pixels);

            //paintImmediately(0, 0, Main.getWIDTH(), Main.getHEIGHT());
            repaint();
        }
        if(isCSelected)
        {
            cPosX = e.getX();
            cPosY = e.getY();

            reCalculateSequence();

            //paintImmediately(0, 0, Main.getWIDTH(), Main.getHEIGHT());
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

        xOffset = e.getX();
        yOffset = e.getY();

        if(!isCSelected && !isXSelected) {
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
}
