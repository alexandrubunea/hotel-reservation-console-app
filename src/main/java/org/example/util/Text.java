package org.example.util;

import java.util.ArrayList;
import org.example.model.Hotel;
import org.example.model.Room;

/**
 * This class contains useful text scripts.
*/
public class Text {
    public static final String COLOR_FG_BLUE = "\u001B[34m";
    public static final String COLOR_FG_RED = "\u001B[31m";
    public static final String COLOR_RESET = "\u001B[0m";

    /**
     * Print hotels in the console as a table.
     * @param hotels list of hotels
     */
    public static void printHotels(ArrayList<Hotel> hotels) {
        String leftAlignFormat = "| %-3d | %-30s | %-15d | %-10s |%n";

        System.out.format("+-----+--------------------------------+-----------------+------------+%n");
        System.out.format("| ID  | Hotel Name                     | No. rooms       | Distance   |%n");
        System.out.format("+-----+--------------------------------+-----------------+------------+%n");

        int id = 1;
        for(Hotel hotel : hotels) {
            System.out.format(leftAlignFormat, id, hotel.getName(), hotel.getRooms().size(),
                    String.format("%.1f", hotel.getClient_distance() / 1000) + " km");
            id++;
        }

        System.out.format("+-----+--------------------------------+-----------------+------------+%n");
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
    public static void printMenu() {
        System.out.println(
            COLOR_FG_BLUE +
            """
            ╔╗╔╗╔══╗╔════╗╔═══╗╔╗     ╔══╗ ╔══╗╔══╗╔╗╔══╗╔══╗╔╗ ╔╗╔═══╗
            ║║║║║╔╗║╚═╗╔═╝║╔══╝║║     ║╔╗║ ║╔╗║║╔╗║║║║╔═╝╚╗╔╝║╚═╝║║╔══╝
            ║╚╝║║║║║  ║║  ║╚══╗║║     ║╚╝╚╗║║║║║║║║║╚╝║   ║║ ║╔╗ ║║║╔═╗
            ║╔╗║║║║║  ║║  ║╔══╝║║     ║╔═╗║║║║║║║║║║╔╗║   ║║ ║║╚╗║║║╚╗║
            ║║║║║╚╝║  ║║  ║╚══╗║╚═╗   ║╚═╝║║╚╝║║╚╝║║║║╚═╗╔╝╚╗║║ ║║║╚═╝║
            ╚╝╚╝╚══╝  ╚╝  ╚═══╝╚══╝   ╚═══╝╚══╝╚══╝╚╝╚══╝╚══╝╚╝ ╚╝╚═══╝
            """
            + COLOR_FG_RED
            + "\t\t" +
            "▄▀▀ █   █ █   █   █▄ █ ▄▀▄ █▀▄ ▄▀▄ ▄▀▀ ▄▀▄"
            + "\n\t\t" +
            "▀▄▄ █▄▄ ▀▄█ ▀▄█   █ ▀█ █▀█ █▀  ▀▄▀ ▀▄▄ █▀█"
            + COLOR_RESET
            + "\n" +
            "———————————————————————————————————————————————————————————");
        System.out.println("\n");
        printInfoMessage("Type the number of the option that you want to use:");
        System.out.println(
            "\n\t" + COLOR_FG_BLUE + "[1]" + COLOR_RESET + " Book a hotel" +
            "\n\t" + COLOR_FG_BLUE + "[2]" + COLOR_RESET + " My reservations" +
            "\n\t" + COLOR_FG_BLUE + "[3]" + COLOR_RESET + " Exit");
    }

    /**
     * Print user's reservations.
     */
    public static void printReservations() {

    }

    /**
     * Prints an error message in a more "styled" way
     * @param msg the message of the error
     */
    public static void printErrorMessage(String msg) {
        System.out.println(COLOR_FG_RED + "(!)" + COLOR_RESET + " " + msg);
    }
    public static void printInfoMessage(String msg) {
        System.out.println(COLOR_FG_BLUE + "*" + COLOR_RESET + " " + msg);
    }
}
