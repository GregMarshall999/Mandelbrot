package com.isekario.util;

import com.isekario.Main;

import java.util.ArrayList;
import java.util.List;

/**
 * Container class for program parameters
 */
public final class Util {

    //region fields
    public static int iterationsMax = 100; //how deep we go in the mandelbrot set

    public static List<ComplexNumber> sequence = new ArrayList<>(); //The sequence of complex numbers

    //Screen parameters
    public static int dotSize = 10;
    public static int zoomLevel = 100;
    public static final int intervalSize = 10; //Notch height for X and width for Y
    public static final int numberOffsetX = -20;
    public static final int numberOffsetY = 30;

    //The center point we are looking at on the screen
    public static int gridCenterFocusX = Main.getWIDTH()/2;
    public static int gridCenterFocusY = Main.getHEIGHT()/2;

    //red and green dot initial positions
    public static int zPosX = gridCenterFocusX;
    public static int zPosY = gridCenterFocusY;
    public static int cPosX = gridCenterFocusX - zoomLevel; //at -1
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
            coord = (coord - gridCenterFocusX) / zoomLevel;
        }
        else
        {
            coord = (coord - gridCenterFocusY) / zoomLevel;
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
            return (int)(coord*zoomLevel) + gridCenterFocusX;
        }
        else
        {
            return (int)(coord*zoomLevel) + gridCenterFocusY;
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
            sequence.add(MandelUtil.MandelbrotSequence(sequence.get(i-1), constant));
        }
    }
}
