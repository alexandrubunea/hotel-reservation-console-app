package org.example.model;

/**
 * This class stores the Review object
 */
public class Review {
    private final int id;
    private final String text;
    private final Booking booking;

    /**
     * Creates a review object.
     * @param id the id of the review.
     * @param text the text of the review.
     * @param booking the booking related to the review.
     */
    public Review(int id, String text, Booking booking) {
        this.id = id;
        this.text = text;
        this.booking = booking;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Booking getBooking() {
        return booking;
    }
}
