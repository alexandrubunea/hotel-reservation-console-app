package org.example.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.tuple.Pair;
import org.example.model.Hotel;
import org.example.model.Room;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static org.example.util.Config.HOTELS_API_PATH;
import static org.example.util.Text.printErrorMessage;

public class HotelsLoader {
    /**
     * Loads hotels from a JSON file.
     *
     * @return a list that contains all the hotels in the JSON file.
     */
    public static ArrayList<Hotel> loadHotels() {
        ArrayList<Hotel> res = new ArrayList<>();
        JsonArray json_array = readJsonFile(HOTELS_API_PATH);

        if(json_array == null) {
            return null;
        }

        for(JsonElement jsonElement : json_array) {
            res.add(extractHotelFromJson(jsonElement));
        }

        return res;
    }

    /**
     * Loads a pair that contains a hotel and a room in that hotel from JSON
     *
     * @param hotel_id    the hotel's id
     * @param room_number the room number that is in the hotel
     * @return the pair storing the result.
     */
    public static Pair<Hotel, Room> loadHotelRoom(int hotel_id, int room_number) {
        Pair<Hotel, Room> result;
        JsonArray json_array = readJsonFile(HOTELS_API_PATH);

        if(json_array == null) {
            return null;
        }

        Hotel hotel = null;
        Room room = null;

        for(JsonElement jsonElement : json_array) {
            int idx = jsonElement.getAsJsonObject().get("id").getAsInt();
            if(idx == hotel_id) {
                hotel = extractHotelFromJson(jsonElement);
                break;
            }
        }

        if(hotel == null) {
            return null;
        }

        for(Room room_it : hotel.getRooms()) {
            if(room_it.getRoomNumber() == room_number) {
                room = room_it;
                break;
            }
        }

        if(room == null) {
            return null;
        }

        result = Pair.of(hotel, room);

        return result;
    }

    /**
     * Extracts json file content from the specified path.
     * @param path the path of the file.
     * @return file's content.
     */
    private static JsonArray readJsonFile(String path) {
        JsonArray json_array = null;
        try {
            BufferedReader in = new BufferedReader(new FileReader(path));
            String inputLine;
            StringBuilder sb = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            in.close();
            json_array = JsonParser.parseString(sb.toString()).getAsJsonArray();
        } catch (IOException e) {
            printErrorMessage("File not found at path: " + path + " | " + e.getMessage());
        }
        return json_array;
    }

    /**
     * Extracts {@link org.example.model.Hotel}'s data from {@link JsonElement}
     * @param json_element the JsonElement to extract data from
     * @return the hotel as a {@link org.example.model.Hotel} object.
     */
    private static Hotel extractHotelFromJson(JsonElement json_element) {
        int hotel_id = json_element.getAsJsonObject().get("id").getAsInt();
        String hotel_name = json_element.getAsJsonObject().get("name").getAsString();
        double latitude = json_element.getAsJsonObject().get("latitude").getAsDouble();
        double longitude = json_element.getAsJsonObject().get("longitude").getAsDouble();
        JsonArray rooms_json_array = json_element.getAsJsonObject().get("rooms").getAsJsonArray();

        ArrayList<Room> rooms = new ArrayList<>();
        for(JsonElement room_json_element : rooms_json_array) {
            int room_number = room_json_element.getAsJsonObject().get("roomNumber").getAsInt();
            int room_type = room_json_element.getAsJsonObject().get("type").getAsInt();
            int room_price = room_json_element.getAsJsonObject().get("price").getAsInt();

            String room_name_type = switch (room_type) {
                case 1 -> "single";
                case 2 -> "double";
                case 3 -> "matrimonial";
                default -> "undefined";
            };

            rooms.add(new Room(
                    room_number,
                    room_name_type,
                    room_price
            ));
        }
        return new Hotel(
            hotel_id,
            hotel_name,
            rooms,
            new Point(longitude, latitude)
        );
    }
}
