package model;
/**
    * This class represents a hotel's room as an object.
*/
public class Room {
    private final int room_number;
    private final String room_type;
    private final int price;

    public Room(int room_number, String room_type, int price) {
        this.room_number = room_number;
        this.room_type = room_type;
        this.price = price;
    }

    public int getRoomNumber() {
        return room_number;
    }
    public String getRoomType() {
        return room_type;
    }
    public int getPrice() {
        return price;
    }
}
