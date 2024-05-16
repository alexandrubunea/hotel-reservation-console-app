package org.example.util;

import java.nio.file.Paths;

public class Config {
    public static final String ABSOLUTE_PATH = Paths.get("").toAbsolutePath().toString();
    public static final String HOTELS_API_PATH = ABSOLUTE_PATH + "/src/main/resources/hotels.json/";
    public static final String DATABASE_LOCATION = ABSOLUTE_PATH + "/src/main/resources/database/database";
}
