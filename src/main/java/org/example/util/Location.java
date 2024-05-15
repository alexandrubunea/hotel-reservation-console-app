package org.example.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Contains useful functions related to geolocation.
 */
public class Location {
    /**
     * The function get user's location based on their IP address using an API.
     * @return user's longitude and latitude.
     */
    public static Point getUserLocation() {
        String API_LINK = "http://ip-api.com/json?fields=lat,lon";
        try {
            URL url = new URL(API_LINK);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder res = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                res.append(inputLine);
            }
            in.close();

            JsonObject jsonResponse = JsonParser.parseString(res.toString()).getAsJsonObject();
            double lat = jsonResponse.get("lat").getAsDouble();
            double lon = jsonResponse.get("lon").getAsDouble();

            return new Point(lon, lat);
        } catch (IOException e) {
            System.out.println("Error when trying to connect to " + API_LINK + " | " + e.toString());
        }
        return new Point(0, 0);
    }
}
