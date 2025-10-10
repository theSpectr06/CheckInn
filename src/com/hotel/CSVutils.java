package com.hotel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVutils {

    // Reads a CSV f  ile and returns a list of string arrays (each row)
    public static List<String[]> readCSV(String filePath) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                // Skip header row
                if (firstLine) { firstLine = false; continue; }
                String[] fields = line.split(",");
                data.add(fields);
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV: " + e.getMessage());
        }
        return data;
    }

    // Writes a list of string arrays to CSV (overwrites file)
    public static void writeCSV(String filePath, List<String[]> data, boolean includeHeader, String[] header) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            if (includeHeader && header != null) {
                bw.write(String.join(",", header));
                bw.newLine();
            }
            for (String[] row : data) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing CSV: " + e.getMessage());
        }
    }

    // Appends a single row to CSV
    public static void appendCSV(String filePath, String[] row) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(String.join(",", row));
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error appending CSV: " + e.getMessage());
        }
    }
}
