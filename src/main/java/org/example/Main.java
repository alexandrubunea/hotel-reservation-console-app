package org.example;

import static org.example.menu.Menu.MainMenu;
import static org.example.util.DBUtils.test_connection;

public class Main {
    public static void main(String[] args) {
        if(test_connection()) {
            MainMenu();
        }
    }
}