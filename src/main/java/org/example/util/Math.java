package org.example.util;

import org.apache.commons.lang3.tuple.Pair;

import static java.lang.Math.*;

/**
 * Contains helpful math formulas.
*/
public class Math {
    /**
     * Calculates the distance between two points.
     * @param a point A.
     * @param b point B.
     * @return the distance between point A and point B.
     */
    public static double distance(Point a, Point b) {
        double delta_longitude = abs(a.getLongitude() - b.getLongitude());
        double delta_latitude = abs(a.getLatitude() - b.getLatitude());

        Pair<Double, Double> conversion = convertLatLon(a.getLatitude(), a.getLongitude());
        double m_per_longitude = conversion.getLeft();
        double m_per_latitude = conversion.getRight();

        return sqrt(pow(delta_longitude * m_per_longitude, 2) + pow(delta_latitude * m_per_latitude, 2));
    }

    /**
     * Converts latitude / longitude into meters per latitude / longitude.
     * @param latitude the latitude.
     * @param longitude the longitude.
     * @return a list that contains the meters per latitude and meters per longitude.
     */
    private static Pair<Double, Double> convertLatLon(double latitude, double longitude) {
        double longitude_radians = toRadians(longitude);
        double latitude_radians = toRadians(latitude);

        double m_per_longitude = 111412.84 * cos(longitude_radians) - 93.5 * cos(3 * longitude_radians) +
                0.118 * cos(5 * longitude_radians);
        double m_per_latitude = 111132.92 - 559.82 * cos(2 * latitude_radians) + 1.175 * cos(4 * latitude_radians) -
                0.0023 * cos(6 * latitude_radians);

        return Pair.of(m_per_longitude, m_per_latitude);
    }
}
