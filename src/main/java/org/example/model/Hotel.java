package org.example.model;

import org.example.util.Point;

import java.util.ArrayList;

import static org.example.util.Math.distance;

/**
    * This class represents a hotel as an object.
*/
public class Hotel {
    private final int id;
    private final String name;
    private final ArrayList<Room> rooms;
    private final Point location;
    private double client_distance;

    /**
     * Creates a hotel project.
     * @param id the id of the hotel.
     * @param name the name of the hotel.
     * @param rooms the list that contains hotel's rooms
     * @param location the location of the hotel as a {@link org.example.util.Point}
     */
    public Hotel(int id, String name, ArrayList<Room> rooms, Point location) {
        this.id = id;
        this.name = name;
        this.rooms = rooms;
        this.location = location;
        this.client_distance = 0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public Point getLocation() {
        return location;
    }

    public double getClient_distance() {
        return client_distance;
    }

    public void calculateClientDistance(Point client_position) {
        client_distance = (int) distance(location, client_position);
    }
}
