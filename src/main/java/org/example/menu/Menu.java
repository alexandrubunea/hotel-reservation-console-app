package org.example.menu;

import org.apache.commons.lang3.StringUtils;
import org.example.model.Booking;
import org.example.model.Hotel;
import org.example.model.Room;
import org.example.util.Point;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;

import static org.example.util.Config.*;
import static org.example.util.DBUtils.addBooking;
import static org.example.util.DBUtils.getBookedDays;
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
                    printBookings();
                    is_running = false;
                    break;
                case 3:
                    printUserReviews();
                    is_running = false;
                    break;
                case 4:
                    is_running = false;
                    break;
            }
        }
    }

    /**
     * Prints the menu to access the hotels nearby.
     */
    private static void printHotelsMenu() {
        ArrayList<Hotel> hotels = loadHotels();
        double range = 0.0;
        Point client_position = getUserLocation();

        if(hotels == null) {
            return;
        }

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
        ArrayList<Room> rooms;
        Hotel hotel;

        while(true) {
            System.out.println("\n");
            printInfoMessage("Enter the ID of the hotel you want to see the rooms:");

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

            hotel = hotels.get(id - 1);
            rooms = hotel.getRooms();
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
                printSelectRoomMenu(hotel, rooms);
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
    private static void printSelectRoomMenu(Hotel hotel, ArrayList<Room> rooms) {
        while(true) {
            printRooms(rooms);
            System.out.println("\n");
            printInfoMessage("Type the number of the room you want to book: ");

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

            printBookRoomMenu(hotel, room_found);

            break;
        }
    }

    /**
     * Prints the menu to book a room.
     * @param room the room to book.
     */
    private static void printBookRoomMenu(Hotel hotel, Room room) {
        System.out.println("\n");

        ArrayList<Booking> booked_time_frames = getBookedDays(hotel, room);
        printInfoMessage("The dates in the table below are already booked, please choose a date outside this " +
                "time frames");

        printHotelRoomBookings(booked_time_frames);

        LocalDateTime check_in = inputCheckIn(booked_time_frames);
        LocalDateTime check_out = inputCheckOut(booked_time_frames);

        addBooking(new Booking(
                -1,
                hotel,
                room,
                check_in,
                check_out
        ));

        int days = (int) ChronoUnit.DAYS.between(check_in, check_out);
        int price = room.getPrice() * days;
        int option = -1;

        while(true) {
            printInfoMessage("The total price for your staying will be " + price + " RON. Do you want to proceed?");
            System.out.println(
                "\n\t" + COLOR_FG_BLUE + "[1]" + COLOR_RESET + " Yes" +
                "\n\t" + COLOR_FG_BLUE + "[2]" + COLOR_RESET + " No");

            String input = System.console().readLine();
            if(!StringUtils.isNumeric(input)) {
                printErrorMessage("You must type an valid number.");
                continue;
            }
            option = Integer.parseInt(input);

            if(option < 1 || option > 2) {
                printErrorMessage("You must type an valid option.");
                continue;
            }

            break;
        }

        if(option == 2) {
            MainMenu();
            return;
        }

        try {
            System.out.println("Your booking has been registered.");
            Thread.sleep(3000);
            MainMenu();
        } catch(InterruptedException e) {
            printErrorMessage("Something went wrong trying to sleep a thread | " + e.getMessage());
        }
    }

    /**
     * Asks the user to input a check-in date and time
     * @param bookings booking list
     * @return the LocalDateTime that was given by the user
     */
    private static LocalDateTime inputCheckIn(ArrayList<Booking> bookings) {
        LocalDateTime input = LocalDateTime.now();
        while(true) {
            try {
                input = LocalDateTime.parse(inputDateTime("in"), DATE_TIME_FORMATTER);
            } catch (DateTimeParseException e) {
                printErrorMessage("Something went wrong while trying to parse user's input.");
            }

            if(isTimeFrameOverlapping(input, bookings)) {
                printErrorMessage("This date is already booked.");
                continue;
            }

            break;
        }
        return input;
    }

    /**
     * Asks the user to input a check-out date and time
     * @param bookings booking list
     * @return the LocalDateTime that was given by the user
     */
    private static LocalDateTime inputCheckOut(ArrayList<Booking> bookings) {
        LocalDateTime input = LocalDateTime.now();
        while(true) {
            try {
                input = LocalDateTime.parse(inputDateTime("out"), DATE_TIME_FORMATTER);
            } catch (DateTimeParseException e) {
                printErrorMessage("Something went wrong while trying to parse user's input.");
            }

            if(isTimeFrameOverlapping(input, bookings)) {
                printErrorMessage("This date is already booked.");
                continue;
            }

            break;
        }
        return input;
    }

    /**
     * Check if a give {@link LocalDateTime} is overlapping with a time frame in a {@link org.example.model.Booking} list
     * @param date_time the date time to check
     * @param bookings the booking list
     * @return true if it's overlapping, false if it's not
     */
    private static boolean isTimeFrameOverlapping(LocalDateTime date_time, ArrayList<Booking> bookings) {
        for(Booking booking : bookings) {
            if(booking.getCheck_out().isAfter(date_time)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Reads a date and a time from the keyboard and validates them
     * @param operation the operation will be "out" or "in", because the user can only check "in" or check "out"
     * @return the date-time read from the keyboard
     */
    private static String inputDateTime(String operation) {
        StringBuilder res;
        String input;
        LocalDateTime current_date_time = LocalDateTime.now();

        while(true) {
            res = new StringBuilder();

            printInfoMessage("Enter the date you want to check " + operation + " [dd/mm/yyyy]:");
            input = System.console().readLine();

            if(!isValidDate(input)) {
                printErrorMessage("You must enter a valid date.");
                continue;
            }
            if(LocalDate.parse(input, DATE_FORMATTER).isBefore(current_date_time.toLocalDate())) {
                printErrorMessage("You cannot enter a date in the past.");
                continue;
            }

            printInfoMessage("Enter the hour you want to check " + operation + " [hh:mm]:");
            res.append(input);
            input = System.console().readLine();

            if(!isValidTime(input)) {
                printErrorMessage("You must enter a valid time.");
                continue;
            }

            LocalDate date_time_input = LocalDate.parse(res.toString(), DATE_FORMATTER);

            if(date_time_input.isEqual(current_date_time.toLocalDate()) &&
                LocalTime.parse(input, TIME_FORMATTER).isBefore(current_date_time.toLocalTime())) {
                printErrorMessage("You cannot enter a time in the past.");
                continue;
            }

            res.append("T").append(input);
            break;
        }
        return res.toString();
    }

    /**
     * Prints user's reviews on past bookings.
     */
    private static void printUserReviews() {

    }

    private static boolean isValidDate(String text) {
        try {
            LocalDate.parse(text, DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    private static boolean isValidTime(String text) {
        try {
            LocalTime.parse(text, TIME_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
