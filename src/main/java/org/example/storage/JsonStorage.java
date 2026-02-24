package org.example.storage;

import java.io.*;
import java.nio.file.*;

public class JsonStorage {

    public static String read(String filePath) throws IOException {
        return Files.readString(Path.of(filePath));
    }

    public static void write(String filePath, String json) throws IOException {
        Files.writeString(Path.of(filePath), json);
    }

    public static boolean exists(String filePath) {
        return Files.exists(Path.of(filePath));
    }
}