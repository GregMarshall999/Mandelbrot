package com.isekario.util;

import com.isekario.Graph;
import com.isekario.Main;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Container class for program parameters
 */
public final class Util {

    public static Color[][] pixels;

    //region fields
    public static int iterationsMax = 1; //how deep we go in the mandelbrot set

    public static List<ComplexNumber> sequence = new ArrayList<>(); //The sequence of complex numbers

    //Screen parameters
    public static int dotSize = 10;
    public static int zoomValue = 100;
    public static final int intervalSize = 10; //Notch height for X and width for Y
    public static final int numberOffsetX = -20;
    public static final int numberOffsetY = 30;
    public static int xOffset = 0;
    public static int yOffset = 0;

    //The center point we are looking at on the screen
    public static int gridCenterFocusX = Main.getWIDTH()/2 + xOffset;
    public static int gridCenterFocusY = Main.getHEIGHT()/2 + yOffset;

    //red and green dot initial positions
    public static int zPosX = gridCenterFocusX;
    public static int zPosY = gridCenterFocusY;
    public static int cPosX = gridCenterFocusX - zoomValue; //at -1
    public static int cPosY = gridCenterFocusY;
    public static boolean isXSelected = false; //clicked the red dot
    public static boolean isCSelected = false; //clicked the green dot
    //endregion

    /**
     * Generates a string for Z position on the graph
     * @return - Z: X= x.x / Y= y.y
     */
    public static String convertXToCoordsString() {
        double x = fromScreenPosToCoords(zPosX, true);
        double y = fromScreenPosToCoords(zPosY, false);

        return "Z: X= " + x + " / Y= " + -y;
    }

    /**
     * Generates a string for C position on the graph
     * @return - C: X= x.x / Y= y.y
     */
    public static String convertCToCoordsString() {
        double x = fromScreenPosToCoords(cPosX, true);
        double y = fromScreenPosToCoords(cPosY, false);

        return "C= " + x + " / Y= " + -y;
    }

    /**
     * Takes the screen position of a point with the conversion axis and returns the axis position depending on the zoom level (to the 100th precision)
     * @param screenPos - point screen position coordinate
     * @param xAxis - axis in which to convert
     * @return - grid coordinate of the point
     */
    private static double fromScreenPosToCoords(int screenPos, boolean xAxis) {
        double coord = screenPos;

        if(xAxis)
        {
            coord = (coord - gridCenterFocusX) / zoomValue;
        }
        else
        {
            coord = (coord - gridCenterFocusY) / zoomValue;
        }

        return coord;
    }

    /**
     * Converts grid coordinates to screen position
     * @param coord - coordinate to convert
     * @param xAxis - axis in which to convert
     * @return - screen position
     */
    public static int fromCoordsToScreenPos(double coord, boolean xAxis) {
        if(xAxis)
        {
            return (int)(coord* zoomValue) + gridCenterFocusX;
        }
        else
        {
            return (int)(coord* zoomValue) + gridCenterFocusY;
        }
    }

    /**
     * Updates the sequence list with new values from the new position of C and Z
     */
    public static void reCalculateSequence() {
        sequence.clear();

        ComplexNumber start = new ComplexNumber(fromScreenPosToCoords(zPosX, true), fromScreenPosToCoords(zPosY, false));
        ComplexNumber constant = new ComplexNumber(fromScreenPosToCoords(cPosX, true), fromScreenPosToCoords(cPosY, false));

        sequence.add(start);

        for (int i = 1; i < iterationsMax; i++) {
            sequence.add(MandelUtil.mandelbrotSequence(sequence.get(i-1), constant));
        }
    }

    /**
     * Plots the pixel colors for the mandelbrot set
     * @param pixels - pixel colors to plot
     */
    public static void plotMandelbrot(Color[][] pixels) {
        ComplexNumber currentPosition;

        for(int x = 0; x < Main.getWIDTH(); x++)
            for(int y = 0; y < Main.getHEIGHT(); y++)
            {
                pixels[x][y] = Color.GRAY;

                currentPosition = new ComplexNumber(fromScreenPosToCoords(x, true), fromScreenPosToCoords(y, false));

                int colorValue = MandelUtil.escapeTimeAlgorithm(new ComplexNumber(fromScreenPosToCoords(zPosX, true), fromScreenPosToCoords(zPosY, false)), currentPosition, iterationsMax);

                pixels[x][y] = Color.getHSBColor((colorValue*1.0f)/iterationsMax, 1.0f, 1.0f);

                if (colorValue == iterationsMax)
                    pixels[x][y] = Color.BLACK;
            }
    }

    /**
     * Updates the iteration limit to the new value
     * @param changeValue - change for the iteration limit
     * @param graph - graphic context to update
     */
    public static void changeIterations(int changeValue, Graph graph) {
        iterationsMax += changeValue;

        if(iterationsMax <= 0)
            iterationsMax = 1;

        updateScreen(graph);
    }

    /**
     * Centers the screen display to the clicked position
     * @param graph - Graphic context to update
     */
    public static void shiftScreenToClick(Graph graph) {

        if(xOffset < 800)
        {
            gridCenterFocusX += Main.getWIDTH()/2 - xOffset;
            zPosX += Main.getWIDTH()/2 - xOffset;
            cPosX += Main.getWIDTH()/2 - xOffset;

        }
        else
        {
            gridCenterFocusX -= xOffset - Main.getWIDTH()/2;
            zPosX -= xOffset - Main.getWIDTH()/2;
            cPosX -= xOffset - Main.getWIDTH()/2;

        }
        if(yOffset < 800)
        {
            gridCenterFocusY += Main.getHEIGHT()/2 - yOffset;
            zPosY += Main.getHEIGHT()/2 - yOffset;
            cPosY += Main.getHEIGHT()/2 - yOffset;
        }
        else
        {
            gridCenterFocusY -= yOffset - Main.getHEIGHT()/2;
            zPosY -= yOffset - Main.getHEIGHT()/2;
            cPosY -= yOffset - Main.getHEIGHT()/2;
        }

        updateScreen(graph);
    }

    /**
     * Refreshes the display
     * @param graph - Graphic context to update
     */
    private static void updateScreen(Graph graph) {
        reCalculateSequence();
        plotMandelbrot(pixels);
        graph.repaint();
    }

    public static void zoomScreen(int zoom, Graph graph) {

    }
}
