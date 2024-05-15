package model;
/**
    * This class represents a hotel's room as an object.
*/
public class Room {
    private final int roomNumber;
    private final String roomType;
    private final int price;

    public Room(int roomNumber, String roomType, int price) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.price = price;
    }

    public int getRoomNumber() {
        return roomNumber;
    }
    public String getRoomType() {
        return roomType;
    }
    public int getPrice() {
        return price;
    }
}
