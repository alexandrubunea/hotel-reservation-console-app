package org.example.util;

import org.example.Main;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.example.util.Text.printErrorMessage;

/**
 * After packaging the file using Maven the files in the resources folder will end up in a different place in the
 * JAR file, this class ensure that the resources are loaded correctly even after packaging.
 */
public class ResourceLoader {
    /**
     * Loads a resource from the "resources" folder.
     * @param path the path to the file in the "resources" folder
     * @param file_name name of the file
     * @param file_suffix extension of the file
     * @return the path to the file
     */
    public static String loadResource(String path, String file_name, String file_suffix) {
        try (InputStream inputStream = Main.class.getResourceAsStream(path)) {
            if (inputStream == null) {
                printErrorMessage("Resource not found: " + path);
                return null;
            }

            Path tempFile = Files.createTempFile(file_name, file_suffix);
            tempFile.toFile().deleteOnExit();
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

            return tempFile.toAbsolutePath().toString();
        } catch (IOException e) {
            printErrorMessage("Error trying to get the resource " + file_name + " | " + e.getMessage());
            return null;
        }
    }
}
