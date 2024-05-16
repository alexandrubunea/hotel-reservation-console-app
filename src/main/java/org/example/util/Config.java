package org.example.util;

import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

public class Config {
    public static final String ABSOLUTE_PATH = Paths.get("").toAbsolutePath().toString();
    public static final String HOTELS_API_PATH = ABSOLUTE_PATH + "/src/main/resources/hotels.json/";
    public static final String DATABASE_LOCATION = ABSOLUTE_PATH + "/src/main/resources/database/database";

    private static final String DATE_TIME_PATTERN = "dd/MM/yyyy'T'HH:mm";
    private static final String DATE_PATTERN = "dd/MM/yyyy";
    private static final String TIME_PATTERN = "HH:mm";
    private static final String DATE_TIME_NORMAL_PATTER = "dd/MM/yyyy HH:mm";

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);
    public static final DateTimeFormatter DATE_TIME_FORMATTER_NORMAL = DateTimeFormatter.ofPattern(DATE_TIME_NORMAL_PATTER);
}
