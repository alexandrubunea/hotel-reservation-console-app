package org.example.model;

import java.util.Date;

/**
    * This class represents a reservation made to a hotel by the user.
*/
public class Reservation {
    private final Date check_in;
    private final Date check_out;
    private final Hotel hotel;
    private final Room room;

    /**
        * Creates a reservation.
        * @param hotel The hotel were the user booked.
        * @param room The room user booked from the hotel.
        * @param check_in The date & time the user will check in.
        * @param check_out The date & time the user will check out.
    */
    public Reservation(Hotel hotel, Room room, Date check_in, Date check_out) {
        this.hotel = hotel;
        this.room = room;
        this.check_in = check_in;
        this.check_out = check_out;
    }

    public Date getCheck_in() {
        return check_in;
    }
    public Date getCheck_out() {
        return check_out;
    }
    public Hotel getHotel() {
        return hotel;
    }
    public Room getRoom() {
        return room;
    }
}
