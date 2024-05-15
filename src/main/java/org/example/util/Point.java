package org.example.util;

/**
 *  This class represents a point on a 2D plan.
*/
public class Point {
    private final double longitude;
    private final double latitude;

    /**
     * Creates a point in a 2D plan.
     * @param longitude position on x-axis.
     * @param latitude position on latitude-axis.
     */
    public Point(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    public double getLatitude() {
        return latitude;
    }
}
