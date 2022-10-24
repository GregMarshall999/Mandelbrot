package com.isekario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

public class Graph extends JPanel implements MouseListener, MouseMotionListener {

    private static int iterationsMax = 100;

    private static List<ComplexNumber> iterations = new ArrayList<>();

    private static boolean isXSelected = false; //clicked red dot
    private static boolean isCSelected = false; //clicked green dot

    private static int resolution = 10; //dot sizes

    private static int interval = 100; //grid spacing
    private static int intervalSize = 10;

    private static int gridCenterX = Main.getWIDTH()/2;
    private static int gridCenterY = Main.getHEIGHT()/2;

    private static int numberOffsetX = -20; //number display X interval offset
    private static int numberOffsetY = 30; //number display Y interval offset

    private static int xPosX = gridCenterX;
    private static int xPosY = gridCenterY;
    private static int cPosX = gridCenterX-interval;
    private static int cPosY = gridCenterY;

    public Graph() {
        reCalculateSequence();

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public void paint(Graphics g) {
        g.clearRect(0, 0, Main.getWIDTH(), Main.getHEIGHT());
        g.setFont(new Font("Arial", Font.PLAIN, 20));

        g.setColor(Color.GRAY);
        g.fillRect(0, 0, Main.getWIDTH(), Main.getHEIGHT());

        drawGraph(g);
        drawStableCircle(g);
        drawSequence(g);
        drawXPoint(g);
        drawCPoint(g);
    }

    private void drawGraph(Graphics g) {
        g.setColor(Color.DARK_GRAY);

        //X,Y axes
        g.drawLine(0, gridCenterY, Main.getWIDTH(), gridCenterY);
        g.drawLine(gridCenterX, 0, gridCenterX, Main.getHEIGHT());

        //X and Y intervals with numbers
        for (int i = -8; i < 9; i++) {
            g.drawLine(gridCenterX + i*interval, gridCenterY - intervalSize, gridCenterX + i*interval, gridCenterY + intervalSize); //X
            g.drawLine(gridCenterX - intervalSize, gridCenterY + i*interval, gridCenterX + intervalSize, gridCenterY + i*interval); //Y

            g.drawString(""+i, gridCenterX + i*interval + numberOffsetX, gridCenterY + numberOffsetY); //X
            g.drawString(""+-i, gridCenterX + numberOffsetX, gridCenterY + i*interval + numberOffsetY); //Y
        }
    }

    private void drawXPoint(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(xPosX - resolution/2, xPosY - resolution/2, resolution, resolution);
        g.drawString(convertXToCoordsString(), 10, 30);
    }

    private void drawCPoint(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillOval(cPosX - resolution/2, cPosY - resolution/2, resolution, resolution);
        g.drawString(convertCToCoordsString(), 10, 60);
    }

    private void drawStableCircle(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawOval(gridCenterX - interval, gridCenterY - interval, interval*2, interval*2);
    }

    private void drawSequence(Graphics g) {
        g.setColor(Color.ORANGE);

        for (ComplexNumber complex : iterations) {
            int x = fromCoordsToScreenPos(complex.getReal() ,true);
            int y = fromCoordsToScreenPos(complex.getImaginary(), false);

            g.fillOval(x-resolution/2, y-resolution/2, resolution, resolution);
        }
    }

    private String convertXToCoordsString() {
        double x = fromScreenPosToCoords(xPosX, true);
        double y = fromScreenPosToCoords(xPosY, false);

        return "X= " + x + " / Y= " + y;
    }

    private String convertCToCoordsString() {
        double x = fromScreenPosToCoords(cPosX, true);
        double y = fromScreenPosToCoords(cPosY, false);

        return "X= " + x + " / Y= " + y;
    }

    private double fromScreenPosToCoords(int screenPos, boolean xAxis) {
        int unit, firstDec, secondDec;

        double coord = 0;

        if(xAxis)
        {
            unit = (screenPos - gridCenterX)/interval;
            firstDec = ((screenPos - gridCenterX)/10)%10;
            secondDec = ((screenPos - gridCenterX)/100)%100;
        }
        else
        {
            unit = (screenPos - gridCenterY)/interval;
            firstDec = ((screenPos - gridCenterY)/10)%10;
            secondDec = ((screenPos - gridCenterY)/100)%100;
        }

        coord+=unit;
        coord+=((double)firstDec)/10;
        coord+=((double)secondDec)/100;

        return coord;
    }

    private int fromCoordsToScreenPos(double coord, boolean xAxis) {
        if(xAxis)
        {
            return (int)(coord*100.0) + gridCenterX;
        }
        else
        {
            return (int)(coord*100.0) + gridCenterY;
        }
    }

    private void reCalculateSequence() {
        iterations.clear();

        ComplexNumber start = new ComplexNumber(fromScreenPosToCoords(xPosX, true), fromScreenPosToCoords(xPosY, false));
        ComplexNumber constant = new ComplexNumber(fromScreenPosToCoords(cPosX, true), fromScreenPosToCoords(cPosY, false));

        iterations.add(start);

        for (int i = 1; i < iterationsMax; i++) {
            iterations.add(Mandelbrot.MandelbrotSequence(iterations.get(i-1), constant));
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (isXSelected)
        {
            xPosX = e.getX();
            xPosY = e.getY();

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
        isXSelected =   e.getX() >= xPosX - resolution/2 &&
                        e.getX() <= xPosX + resolution/2 &&
                        e.getY() >= xPosY - resolution/2 &&
                        e.getY() <= xPosY + resolution/2;

        isCSelected =   e.getX() >= cPosX - resolution/2 &&
                        e.getX() <= cPosX + resolution/2 &&
                        e.getY() >= cPosY - resolution/2 &&
                        e.getY() <= cPosY + resolution/2;
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
