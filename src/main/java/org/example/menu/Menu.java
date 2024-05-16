package org.example.menu;

import org.apache.commons.lang3.StringUtils;
import org.example.model.Booking;
import org.example.model.Hotel;
import org.example.model.Review;
import org.example.model.Room;
import org.example.util.Point;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;

import static java.lang.Math.abs;
import static org.example.util.Config.*;
import static org.example.util.DBUtils.*;
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
        int number;
        printMenu();

        while (true) {
            String input = System.console().readLine();
            if(!StringUtils.isNumeric(input)) {
                continue;
            }

            number = Integer.parseInt(input);
            if(number < 1 || number > 4) {
                continue;
            }

            break;
        }

        switch (number) {
            case 1:
                printHotelsMenu();
                break;
            case 2:
                printUserBookings();
                break;
            case 3:
                printUserReviews();
                break;
        }
    }

    /**
     * Prints the menu to access the hotels nearby.
     */
    private static void printHotelsMenu() {
        ArrayList<Hotel> hotels = loadHotels();
        double range;
        Point client_position = getUserLocation();

        if(hotels == null) {
            return;
        }

        for(Hotel hotel : hotels) {
            hotel.calculateClientDistance(client_position);
        }
        hotels.sort(Comparator.comparingDouble(Hotel::getClient_distance));

        ArrayList<Hotel> hotels_in_range;
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

            hotels_in_range = new ArrayList<>(hotels.subList(0, last_position));
            if(hotels_in_range.isEmpty()) {
                printErrorMessage("There are no more hotels in the range.");
                continue;
            }

            break;
        }
        printHotels(hotels_in_range);
        printRoomsMenu(hotels_in_range);
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
            if(id < 0 || id > hotels.size()) {
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

        int option;
        while(true) {
            String input = System.console().readLine();

            if(!StringUtils.isNumeric(input)) {
                printErrorMessage("You must type an valid number.");
                continue;
            }

            option = Integer.parseInt(input);

            if(option < 1 || option > 2) {
                printErrorMessage("This option doesn't exist.");
                continue;
            }

            break;
        }

        if(option == 1) {
            printSelectRoomMenu(hotel, rooms);
        }
        else {
            MainMenu();
        }
    }

    /**
     * Prints the menu for selecting a room to book.
     * @param rooms the list of rooms.
     */
    private static void printSelectRoomMenu(Hotel hotel, ArrayList<Room> rooms) {
        Room room_found = null;

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

            break;
        }

        printBookRoomMenu(hotel, room_found);
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
        int option;

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
     * Show user's past, running and future bookings.
     */
    private static void printUserBookings() {
        ArrayList<Booking> user_bookings = getUserBookings();

        if(user_bookings == null || user_bookings.isEmpty()) {
            try {
                printInfoMessage("You don't have any bookings");
                Thread.sleep(3000);
                MainMenu();
            } catch(InterruptedException e) {
                printErrorMessage("Something went wrong trying to sleep a thread | " + e.getMessage());
            }
            return;
        }

        ArrayList<Booking> past_bookings = new ArrayList<>();
        ArrayList<Booking> running_bookings = new ArrayList<>();
        ArrayList<Booking> future_bookings = new ArrayList<>();

        LocalDateTime current_date_time = LocalDateTime.now();

        for(Booking booking : user_bookings) {
            if(booking.getCheck_out().isBefore(current_date_time)) {
                past_bookings.add(booking);
            }
            else if (booking.getCheck_out().isAfter(current_date_time)) {
                if(booking.getCheck_in().isBefore(current_date_time)) {
                    running_bookings.add(booking);
                }
                else {
                    future_bookings.add(booking);
                }
            }
        }

        if(!past_bookings.isEmpty()) {
            System.out.println("\n");
            printInfoMessage("Your past bookings:\n");
            printBookings(past_bookings);
        }
        if(!running_bookings.isEmpty()) {
            System.out.println("\n");
            printInfoMessage("Your bookings that are currently running:\n");
            printBookings(running_bookings);
        }
        if(!future_bookings.isEmpty()) {
            System.out.println("\n");
            printInfoMessage("Your future bookings:\n");
            printBookings(future_bookings);
        }

        System.out.println("\n");

        int option;
        while(true) {
            printInfoMessage("Type the number of the option that you want to use:");
            System.out.println(
                "\n\t" + COLOR_FG_BLUE + "[1]" + COLOR_RESET + " Write a review for a past booking" +
                "\n\t" + COLOR_FG_BLUE + "[2]" + COLOR_RESET + " Cancel a future booking" +
                "\n\t" + COLOR_FG_BLUE + "[3]" + COLOR_RESET + " Return to previous menu");

            String input = System.console().readLine();
            if(!StringUtils.isNumeric(input)) {
                printErrorMessage("You must type an valid number.");
                continue;
            }
            option = Integer.parseInt(input);

            if(option < 1 || option > 3) {
                printErrorMessage("You need to type a valid option.");
                continue;
            }

            if(option == 1) {
                if(past_bookings.isEmpty()) {
                    printErrorMessage("You don't have any past bookings to leave a review!");
                    continue;
                }
            }
            else if(option == 2)
            {
                if(future_bookings.isEmpty()) {
                    printErrorMessage("You don't have any future bookings to cancel!");
                    continue;
                }
            }

            break;
        }

        if(option == 1) {
            addReviewMenu(past_bookings);
        }
        else if(option == 2)
        {
            cancelFutureBooking(future_bookings);
        }
        else {
            MainMenu();
        }

    }

    /**
     * Show the menu to cancel a future booking
     * @param bookings the list with future bookings.
     */
    private static void cancelFutureBooking(ArrayList<Booking> bookings) {
        printBookings(bookings);
        Booking booking;
        LocalDateTime current_date_time = LocalDateTime.now();

        while(true) {
            System.out.println("\n");
            printInfoMessage("Type the ID of the booking that you want to cancel:");

            String input = System.console().readLine();
            if(!StringUtils.isNumeric(input)) {
                printErrorMessage("You must type an valid number.");
                continue;
            }
            int option = Integer.parseInt(input);

            if (option < 1 || option > bookings.size()) {
                printErrorMessage("The ID you have typed is invalid.");
                continue;
            }

            booking = bookings.get(option - 1);

            int hours = abs((int) ChronoUnit.HOURS.between(booking.getCheck_in(), current_date_time));
            if(hours <= 2) {
                printErrorMessage("You can't cancel a booking if the check in it's in less than two hours.");
                continue;
            }

            break;
        }

        deleteBooking(booking);
        try {
            System.out.println("Your booking has been canceled.");
            Thread.sleep(3000);
            MainMenu();
        } catch(InterruptedException e) {
            printErrorMessage("Something went wrong trying to sleep a thread | " + e.getMessage());
        }
    }
    /**
     * Show the menu to add a review for a past booking
     * @param bookings the list with past bookings.
     */
    private static void addReviewMenu(ArrayList<Booking> bookings) {
        printBookings(bookings);
        Booking booking;

        while(true) {
            System.out.println("\n");
            printInfoMessage("Type the ID of the booking that you want to leave a review:");

            String input = System.console().readLine();
            if(!StringUtils.isNumeric(input)) {
                printErrorMessage("You must type an valid number.");
                continue;
            }
            int option = Integer.parseInt(input);
            if (option < 1 || option > bookings.size()) {
                printErrorMessage("The ID you have typed is invalid.");
                continue;
            }

            booking = bookings.get(option - 1);

            if(doesReviewAlreadyExist(booking)) {
                printErrorMessage("You already left a review for this booking!");
                continue;
            }

            break;
        }

        String review;
        printInfoMessage("Type your review (if your review exceeds 256 characters it will be sliced):");
        review = System.console().readLine();

        if(review.length() > 256) {
            review = review.substring(0, 256);
        }

        addReview(new Review(
                -1,
                review,
                booking
        ));

        try {
            System.out.println("Your review has been added.");
            Thread.sleep(3000);
            MainMenu();
        } catch(InterruptedException e) {
            printErrorMessage("Something went wrong trying to sleep a thread | " + e.getMessage());
        }
    }

    /**
     * Prints user's reviews on past bookings.
     */
    private static void printUserReviews() {
        ArrayList<Review> reviews = getUserReviews();

        if(reviews == null || reviews.isEmpty()) {
            try {
                printInfoMessage("You didn't left any reviews.");
                Thread.sleep(3000);
                MainMenu();
            } catch(InterruptedException e) {
                printErrorMessage("Something went wrong trying to sleep a thread | " + e.getMessage());
            }
            return;
        }

        for(Review review : reviews) {
            System.out.println("------------------------------------------------------------");
            System.out.println(COLOR_FG_BLUE + review.getBooking().getHotel().getName() + COLOR_RESET);
            System.out.println(COLOR_FG_RED + "Your review:" + COLOR_RESET);
            System.out.println(review.getText());
            System.out.println(review.getBooking().getCheck_in().format(DATE_TIME_FORMATTER_NORMAL) + " - " +
                    review.getBooking().getCheck_out().format(DATE_TIME_FORMATTER_NORMAL));
            System.out.println("------------------------------------------------------------");
        }

        while(true) {
            printInfoMessage("Type 1 to return to the previous menu:");
            String input = System.console().readLine();
            if(input.equals("1")) {
                break;
            }
        }
        MainMenu();
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
