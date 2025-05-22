package com.tallerwebi.dominio.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class LogHelper {
    public static void logToFile(String message) {
        String path = "D:/Ivo/Taller1/SublimApp_fork/SublimApp/user-log.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            writer.write(LocalDateTime.now() + " - " + message);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
