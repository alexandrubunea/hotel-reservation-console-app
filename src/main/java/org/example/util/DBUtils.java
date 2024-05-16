package org.example.util;

import org.apache.commons.lang3.tuple.Pair;
import org.example.model.Booking;
import org.example.model.Hotel;
import org.example.model.Review;
import org.example.model.Room;

import java.sql.*;
import java.util.ArrayList;

import static org.example.util.Config.*;
import static org.example.util.HotelsLoader.loadHotelRoom;
import static org.example.util.Text.printErrorMessage;

public class DBUtils {
    private static final String CONNECTION_STRING = "jdbc:sqlite:" + DATABASE_LOCATION;

    /**
     * Tests the connection to the database
     * @return True - if the connection is valid / False - if the connection is faulty.
     */
    public static boolean test_connection() {
        try(Connection conn = DriverManager.getConnection(CONNECTION_STRING)) {
            return true;
        } catch (SQLException e) {
            printErrorMessage("There was a problem connecting to the database | " + e.getMessage());
            return false;
        }
    }

    /**
     * Add a booking to the database.
     * @param booking the booking to add to the database.
     */
    public static void addBooking(Booking booking) {
        String query = "INSERT INTO Bookings('hotel_id','room_number','check_in','check_out') VALUES(?, ?, ?, ?)";

        try(PreparedStatement preparedStatement =
            DriverManager.getConnection(CONNECTION_STRING).prepareStatement(query)) {
            preparedStatement.setInt(1, booking.getHotel().getId());
            preparedStatement.setInt(2, booking.getRoom().getRoomNumber());
            preparedStatement.setString(3, booking.getCheck_in().toString());
            preparedStatement.setString(4, booking.getCheck_out().toString());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printErrorMessage("There was a problem trying to add a booking | " + e.getMessage());
        }
    }

    /**
     * Delete a booking from the database.
     * @param booking the booking to delete from the database.
     */
    public static void deleteBooking(Booking booking) {
        String query = "UPDATE Bookings SET 'active' = 0 WHERE id = ?";

        try(PreparedStatement preparedStatement =
            DriverManager.getConnection(CONNECTION_STRING).prepareStatement(query)) {
            preparedStatement.setInt(1, booking.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printErrorMessage("There was a problem trying to delete user bookings | " + e.getMessage());
        }
    }

    /**
     * Add a review to the database.
     * @param review the review to add to the database.
     */
    public static void addReview(Review review) {
        String query = "INSERT INTO Reviews('review', 'booking') VALUES(?, ?)";

        try (PreparedStatement preparedStatement =
            DriverManager.getConnection(CONNECTION_STRING).prepareStatement(query)) {
            preparedStatement.setString(1, review.getBooking().getHotel().getName());
            preparedStatement.setInt(2, review.getBooking().getId());

            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            printErrorMessage("There was a problem trying to add a user review | " + e.getMessage());
        }
    }

    /**
     * Extract all user's bookings from the database.
     * @return the list containing the bookings.
     */
    public static ArrayList<Booking> getUserBookings() {
        ArrayList<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM Bookings WHERE active = '1'";

        try(Connection conn = DriverManager.getConnection(CONNECTION_STRING)) {
            ResultSet res = conn.createStatement().executeQuery(query);

            while(res.next()) {
                Pair<Hotel, Room> hotel_and_room = loadHotelRoom(res.getInt("hotel_id"),
                    res.getInt("room_number"));

                if(hotel_and_room == null) {
                    printErrorMessage("Something went wrong trying to extract user bookings from the database.");
                    return null;
                }

                bookings.add(new Booking(
                    res.getInt("id"),
                    hotel_and_room.getLeft(),
                    hotel_and_room.getRight(),
                    res.getDate("check_in"),
                    res.getDate("check_out")
                ));
            }

        } catch (SQLException e) {
            printErrorMessage("There was a problem trying to get the user bookings | " + e.getMessage());
        }

        return bookings;
    }

    /**
     * Extract all user's reviews from the database.
     * @return the list containing the reviews.
     */
    public static ArrayList<Review> getUserReviews() {
        ArrayList<Review> reviews = new ArrayList<>();
        String query = "SELECT * FROM Reviews";
        try(Connection conn = DriverManager.getConnection(CONNECTION_STRING)) {
            ResultSet res = conn.createStatement().executeQuery(query);

            Booking booking = getBooking(res.getInt("booking"));

            if (booking == null) {
                printErrorMessage("There was an error trying to link a review to a booking.");
                return null;
            }

            while(res.next()) {
                reviews.add(new Review(
                        res.getInt("id"),
                        res.getString("review"),
                        booking
                ));
            }
        } catch (SQLException e) {
            printErrorMessage("There was a problem trying to get the user reviews | " + e.getMessage());
        }
        return reviews;
    }

    private static Booking getBooking(int id) {
        String query = "SELECT * FROM Bookings WHERE id = ?";

        try(PreparedStatement preparedStatement =
            DriverManager.getConnection(CONNECTION_STRING).prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet res = preparedStatement.executeQuery();

            if(res.next()) {
                Pair<Hotel, Room> hotel_and_room = loadHotelRoom(res.getInt("hotel_id"),
                    res.getInt("room_number"));

                if(hotel_and_room == null) {
                    printErrorMessage("Something went wrong trying to extract user booking from the database.");
                    return null;
                }

                return new Booking(
                        res.getInt("id"),
                        hotel_and_room.getLeft(),
                        hotel_and_room.getRight(),
                        res.getDate("check_in"),
                        res.getDate("check_out")
                );
            }
        } catch (SQLException e) {
            printErrorMessage("There was a problem trying to get the user booking " + id + " | " + e.getMessage());
        }

        return null;
    }
}
