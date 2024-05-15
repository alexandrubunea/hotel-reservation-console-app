package org.example.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.model.Hotel;
import org.example.model.Room;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class HotelsLoader {
    /**
     * Loads hotels from a JSON file.
     * @param path the path to the JSON file.
     * @return a list that contains all the hotels in the JSON file.
     */
    public static ArrayList<Hotel> loadHotels(String path) {
        ArrayList<Hotel> res = new ArrayList<>();
        String absolutePath = Paths.get("").toAbsolutePath().toString() + path;

        try {
            BufferedReader in = new BufferedReader(new FileReader(absolutePath));
            String inputLine;
            StringBuilder sb = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            in.close();

            JsonArray json_array = JsonParser.parseString(sb.toString()).getAsJsonArray();

            for(JsonElement jsonElement : json_array) {
                int hotel_id = jsonElement.getAsJsonObject().get("id").getAsInt();
                String hotel_name = jsonElement.getAsJsonObject().get("name").getAsString();
                double latitude = jsonElement.getAsJsonObject().get("latitude").getAsDouble();
                double longitude = jsonElement.getAsJsonObject().get("longitude").getAsDouble();
                JsonArray rooms_json_array = jsonElement.getAsJsonObject().get("rooms").getAsJsonArray();

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

                res.add(new Hotel(
                    hotel_id,
                    hotel_name,
                    rooms,
                    new Point(latitude, longitude)
                ));
            }

        } catch (IOException e) {
            System.out.println("File not found at path: " + path + " | " + e.getMessage());
        }

        return res;
    }
}
