package org.example.menu;

import org.apache.commons.lang3.StringUtils;
import org.example.model.Hotel;
import org.example.model.Room;
import org.example.util.Point;

import java.util.ArrayList;
import java.util.Comparator;

import static org.example.util.HotelsLoader.loadHotels;
import static org.example.util.Location.getUserLocation;
import static org.example.util.Text.*;

/**
 * This class shows the menus present in the application.
*/
public class Menu {
    /**
     * Prints the menu that shows up when the app is launched.
     */
    public static void MainMenu() {
        boolean is_running = true;
        printMenu();

        while (is_running) {
            String input = System.console().readLine();
            if(!StringUtils.isNumeric(input)) {
                continue;
            }

            int number = Integer.parseInt(input);

            switch (number) {
                case 1:
                    printHotelsMenu();
                    is_running = false;
                    break;
                case 2:
                    printReservations();
                    is_running = false;
                    break;
                case 3:
                    is_running = false;
                    break;
            }
        }
    }

    /**
     * Prints the menu to access the hotels nearby.
     */
    private static void printHotelsMenu() {
        ArrayList<Hotel> hotels = loadHotels("/src/main/resources/hotels.json/");
        double range = 0.0;
        Point client_position = getUserLocation();

        for(Hotel hotel : hotels) {
            hotel.calculateClientDistance(client_position);
        }
        hotels.sort(Comparator.comparingDouble(Hotel::getClient_distance));

        while (true) {
            printInfoMessage("Enter the radius you want the hotels to be (in kilometers): ");
            String input = System.console().readLine();

            try {
                Double.parseDouble(input);
            } catch (NumberFormatException e) {
                printErrorMessage("You must type an valid number.");
                continue;
            }

            range = Double.parseDouble(input) * 1000;

            if(range <= 0) {
                printErrorMessage("The radius must be greater than 0.");
                continue;
            }

            int last_position = 0;
            for(Hotel hotel : hotels) {
                if(hotel.getClient_distance() > range) {
                    break;
                }
                last_position ++;
            }

            ArrayList<Hotel> hotels_in_range = new ArrayList<Hotel>(hotels.subList(0, last_position));
            if(hotels_in_range.isEmpty()) {
                printErrorMessage("There are no more hotels in the range.");
                continue;
            }

            printHotels(hotels_in_range);
            printRoomsMenu(hotels_in_range);
            break;
        }
    }

    /**
     * Prints the menu to access the rooms of a hotel
     * @param hotels list of hotels
     */
    private static void printRoomsMenu(ArrayList<Hotel> hotels) {
        System.out.println("\n");
        printInfoMessage("Enter the ID of the hotel you want to see the rooms:");

        ArrayList<Room> rooms;

        while(true) {
            String input = System.console().readLine();
            if(!StringUtils.isNumeric(input)) {
                printErrorMessage("You must type an valid number.");
                continue;
            }

            int id = Integer.parseInt(input);
            if(id < 0 || id >= hotels.size()) {
                printErrorMessage("You must type a valid id.");
                continue;
            }

            rooms = hotels.get(id - 1).getRooms();
            printRooms(rooms);
            break;
        }

        System.out.println("\n");
        printInfoMessage("Select one of the options below: ");
        System.out.println(
            "\n\t" + COLOR_FG_BLUE + "[1]" + COLOR_RESET + " Book a room" +
            "\n\t" + COLOR_FG_BLUE + "[2]" + COLOR_RESET + " Go back to the start menu");

        while(true) {
            String input = System.console().readLine();

            if(!StringUtils.isNumeric(input)) {
                printErrorMessage("You must type an valid number.");
                continue;
            }

            int option = Integer.parseInt(input);
            if(option == 1) {
                printSelectRoomMenu(rooms);
            }
            else if(option == 2) {
                MainMenu();
            }
            else {
                printErrorMessage("This option doesn't exist.");
                continue;
            }

            break;
        }
    }

    /**
     * Prints the menu for selecting a room to book.
     * @param rooms the list of rooms.
     */
    private static void printSelectRoomMenu(ArrayList<Room> rooms) {
        System.out.println("\n");
        printInfoMessage("Type the number of the room you want to book: ");

        while(true) {
            String input = System.console().readLine();
            if(!StringUtils.isNumeric(input)) {
                printErrorMessage("You must type an valid number.");
                continue;
            }
            int number = Integer.parseInt(input);

            Room room_found = null;
            for(Room room : rooms) {
                if(room.getRoomNumber() == number) {
                    room_found = room;
                    break;
                }
            }

            if(room_found == null) {
                printErrorMessage("The room number you entered does not exist.");
                continue;
            }

            printBookRoomMenu(room_found);

            break;
        }
    }

    /**
     * Prints the menu to book a room.
     * @param room the room to book.
     */
    private static void printBookRoomMenu(Room room) {
        System.out.println("\n");
    }
}
