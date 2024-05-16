package org.example.model;

import java.util.Date;

/**
 * This class contains the booking object.
*/
public class Booking {
    private final int id;
    private final Hotel hotel;
    private final Room room;
    private final Date check_in;
    private final Date check_out;

    /**
     * Creates a booking object
     * @param id the booking id.
     * @param hotel the {@link org.example.model.Hotel} booked.
     * @param room the {@link org.example.model.Room}  booked in the hotel.
     * @param check_in the check-in date-hour.
     * @param check_out the check-out date-hour.
     */
    public Booking(int id, Hotel hotel, Room room, Date check_in, Date check_out) {
        this.id = id;
        this.hotel = hotel;
        this.room = room;
        this.check_in = check_in;
        this.check_out = check_out;
    }

    public int getId() {
        return id;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public Room getRoom() {
        return room;
    }

    public Date getCheck_in() {
        return check_in;
    }

    public Date getCheck_out() {
        return check_out;
    }
}
