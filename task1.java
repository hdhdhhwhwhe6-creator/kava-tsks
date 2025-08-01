// FileOperations.java
// A Java program to demonstrate file reading, writing, and modifying text files

import java.io.*;
import java.util.*;

public class FileOperations {

    // Method to write text to a file
    public static void writeToFile(String filename, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(content);
            System.out.println("✅ File written successfully.");
        } catch (IOException e) {
            System.out.println("❌ Error writing to file: " + e.getMessage());
        }
    }

    // Method to read contents from a file
    public static void readFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            System.out.println("📄 File contents:");
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("  " + line);
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading file: " + e.getMessage());
        }
    }

    // Method to modify file (replaces a word with another)
    public static void modifyFile(String filename, String target, String replacement) {
        try {
            File file = new File(filename);
            List<String> lines = new ArrayList<>();

            // Read all lines and replace target with replacement
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line.replaceAll(target, replacement));
                }
            }

            // Write modified content back to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }

            System.out.println("✏️ File modified successfully.");
        } catch (IOException e) {
            System.out.println("❌ Error modifying file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String filename = "example.txt";

        // Step 1: Write to file
        writeToFile(filename, "Hello world!\nWelcome to Java file operations.");

        // Step 2: Read file
        readFromFile(filename);

        // Step 3: Modify file (replace "Java" with "Advanced Java")
        modifyFile(filename, "Java", "Advanced Java");

        // Step 4: Read file again to see changes
        readFromFile(filename);
    }
}
