package org.example.util;

import java.util.ArrayList;
import org.example.model.Hotel;
import org.example.model.Room;

public class TextTable {
    /**
     * Print hotels in the console as a table.
     * @param hotels list of hotels
     */
    public static void printHotels(ArrayList<Hotel> hotels) {
        String leftAlignFormat = "| %-30s | %-15d | %-10d |%n";

        System.out.format("+--------------------------------+-----------------+------------+%n");
        System.out.format("| Hotel Name                     | No. rooms       | Distance   |%n");
        System.out.format("+--------------------------------+-----------------+------------+%n");
        for(Hotel hotel : hotels) {
            System.out.format(leftAlignFormat, hotel.getName(), hotel.getRooms().size(), 1000);
        }
        System.out.format("+--------------------------------+-----------------+------------+%n");
    }

    /**
     * Print rooms of a hotel in the console as a table.
     * @param rooms list of rooms
     */
    public static void printRooms(ArrayList<Room> rooms) {
        String leftAlignFormat = "| %-11d | %-23s | %-10d |%n";

        System.out.format("+-------------+-------------------------+------------+%n");
        System.out.format("| Room number | Room Type               | Price      |%n");
        System.out.format("+-------------+-------------------------+------------+%n");
        for(Room room : rooms) {
            System.out.format(leftAlignFormat, room.getRoomNumber(), room.getRoomType(), room.getPrice());
        }
        System.out.format("+-------------+-------------------------+------------+%n");
    }
}
