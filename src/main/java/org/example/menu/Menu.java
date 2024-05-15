package org.example.menu;

import org.apache.commons.lang3.StringUtils;
import org.example.model.Hotel;
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
    public static void printHotelsMenu() {
        ArrayList<Hotel> hotels = loadHotels("/src/main/resources/hotels.json/");
        double range = 0.0;
        Point client_position = getUserLocation();

        for(Hotel hotel : hotels) {
            hotel.calculateClientDistance(client_position);
        }
        hotels.sort(Comparator.comparingDouble(Hotel::getClient_distance));

        while (true) {
            System.out.println(COLOR_FG_BLUE + "*" + COLOR_RESET + " Enter the radius you want the hotels to be in kilometers:");
            String input = System.console().readLine();

            try {
                Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println(COLOR_FG_RED + "(!)" + COLOR_RESET + " You must type an valid number.");
                continue;
            }

            range = Double.parseDouble(input) * 1000;

            if(range <= 0) {
                System.out.println(COLOR_FG_RED + "(!)" + COLOR_RESET + " The radius must be greater than 0.");
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
                System.out.println(COLOR_FG_RED + "(!)" + COLOR_RESET + "There are no hotels in this range.");
                continue;
            }

            printHotels(hotels_in_range);
            break;
        }
    }
}
