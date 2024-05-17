package org.example;

import static org.example.menu.Menu.MainMenu;
import static org.example.util.Config.*;
import static org.example.util.DBUtils.test_connection;
import static org.example.util.ResourceLoader.loadResource;
import static org.example.util.Text.printErrorMessage;

public class Main {
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            printErrorMessage("SQLite driver not found | " + e.getMessage());
            return;
        }

        DATABASE_LOCATION = loadResource("/database/database", "database", "");
        HOTELS_API_PATH = loadResource("/api/hotels.json", "hotels", "json");

        if(test_connection()) {
            MainMenu();
        }
    }
}