package org.example;

import org.example.model.Hotel;

import java.util.ArrayList;

import static org.example.util.HotelsLoader.loadHotels;
import static org.example.util.TextTable.*;

public class Main {
    public static void main(String[] args) {
        ArrayList<Hotel> hotels = loadHotels("/src/main/resources/hotels.json");
        printHotels(hotels);

        for (Hotel hotel : hotels) {
            printRooms(hotel.getRooms());
        }
    }
}