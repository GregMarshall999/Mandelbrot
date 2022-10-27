package com.gmarshall.mandelbrot.util;

import com.gmarshall.mandelbrot.Graph;

import java.awt.Color;

/**
 * Container class for program parameters
 */
public final class Util {

    //region fields

    public static Color[][] pixels; //Mandelbrot pixels

    public static int iterationsMax = 1; //how deep we go in the mandelbrot set
    public static ComplexNumber[] sequence = new ComplexNumber[iterationsMax]; //The sequence of complex numbers

    //Screen parameters
    public static final int SCREEN_WIDTH = 1600;
    public static final int SCREEN_HEIGHT = 900;
    public static final int intervalSize = 10; //Notch height for X and width for Y
    public static final int numberOffsetX = -20;
    public static final int numberOffsetY = 30;
    public static int dotSize = 10;
    public static int zoomValue = 100; //ie how many pixels between grid units
    public static int xOffset = 0;
    public static int yOffset = 0;
    public static int gridCenterX = SCREEN_WIDTH /2 + xOffset;
    public static int gridCenterY = SCREEN_HEIGHT /2 + yOffset;
    public static ComplexNumber focusedPoint = new ComplexNumber(0, 0); //The center point we are looking at on the screen

    //red and green dot initial positions
    public static int zPosX = gridCenterX;
    public static int zPosY = gridCenterY;
    public static int cPosX = gridCenterX - zoomValue; //at -1
    public static int cPosY = gridCenterY;
    public static boolean isXSelected = false; //clicked the red dot
    public static boolean isCSelected = false; //clicked the green dot
    //endregion

    //region position conversion

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
    public static double fromScreenPosToCoords(int screenPos, boolean xAxis) {
        double coord = screenPos;

        if(xAxis)
        {
            coord = (coord - gridCenterX) / zoomValue;
        }
        else
        {
            coord = (coord - gridCenterY) / zoomValue;
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
            return (int)(coord* zoomValue) + gridCenterX;
        }
        else
        {
            return (int)(coord* zoomValue) + gridCenterY;
        }
    }

    //endregion

    /**
     * Updates the sequence list with new values from the new position of C and Z
     */
    public static void reCalculateSequence() {
        sequence = new ComplexNumber[iterationsMax];

        ComplexNumber start = new ComplexNumber(fromScreenPosToCoords(zPosX, true), -fromScreenPosToCoords(zPosY, false));
        ComplexNumber constant = new ComplexNumber(fromScreenPosToCoords(cPosX, true), -fromScreenPosToCoords(cPosY, false));

        sequence[0] = start;

        for (int i = 1; i < iterationsMax; i++) {
            sequence[i] = MandelUtil.mandelbrotSequence(sequence[i-1], constant);
        }
    }

    /**
     * Plots the pixel colors for the mandelbrot set
     * @param pixels - pixel colors to plot
     */
    public static void plotMandelbrot(Color[][] pixels) {
        ComplexNumber currentPosition;

        for(int x = 0; x < SCREEN_WIDTH; x++)
            for(int y = 0; y < SCREEN_HEIGHT; y++)
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

        if(xOffset < SCREEN_WIDTH /2)
        {
            gridCenterX += SCREEN_WIDTH /2 - xOffset;
            zPosX += SCREEN_WIDTH /2 - xOffset;
            cPosX += SCREEN_WIDTH /2 - xOffset;
        }
        else
        {
            gridCenterX -= xOffset - SCREEN_WIDTH /2;
            zPosX -= xOffset - SCREEN_WIDTH /2;
            cPosX -= xOffset - SCREEN_WIDTH /2;
        }
        if(yOffset < SCREEN_HEIGHT /2)
        {
            gridCenterY += SCREEN_HEIGHT /2 - yOffset;
            zPosY += SCREEN_HEIGHT /2 - yOffset;
            cPosY += SCREEN_HEIGHT /2 - yOffset;
        }
        else
        {
            gridCenterY -= yOffset - SCREEN_HEIGHT /2;
            zPosY -= yOffset - SCREEN_HEIGHT /2;
            cPosY -= yOffset - SCREEN_HEIGHT /2;
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

    /**
     * Zooms into the set
     * @param zoom - zoom amount
     * @param graph - graphic context to update
     */
    public static void zoomScreen(int zoom, Graph graph) {
        zoomValue += zoom;

        xOffset = fromCoordsToScreenPos(focusedPoint.getReal(), true);
        yOffset = fromCoordsToScreenPos(-focusedPoint.getImaginary(), false);

        shiftScreenToClick(graph);
    }
}
