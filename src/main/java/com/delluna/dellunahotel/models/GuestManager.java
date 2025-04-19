package com.delluna.dellunahotel.models;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GuestManager {
    private static final String FILE_PATH = "src/data/guests.txt";
    private static final String ID_PREFIX = "G";
    private static int nextId = 1;

    static {
        initializeFile();
        loadLastId();
    }

    private static void initializeFile() {
        try {
            Path path = Paths.get(FILE_PATH);
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            }
        } catch (IOException e) {
            System.err.println("Error initializing guests file: " + e.getMessage());
        }
    }

    private static void loadLastId() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
            if (!lines.isEmpty()) {
                String lastLine = lines.get(lines.size() - 1);
                if (!lastLine.trim().isEmpty()) {
                    String lastId = lastLine.split(",")[0];
                    nextId = Integer.parseInt(lastId.substring(ID_PREFIX.length())) + 1;
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading last ID: " + e.getMessage());
        }
    }

    public static String generateNextId() {
        return ID_PREFIX + String.format("%03d", nextId++);
    }

    // Hash utility method
    private static String hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not available", e);
        }
    }

    // Add new guest with all fields
    public static void addGuest(Guest guest, String password, String securityAnswer) throws IOException {
        if (guest.getGuestId() == null || guest.getGuestId().isEmpty()) {
            guest.setGuestId(generateNextId());
        }
        
        guest.setHashPassword(hashString(password));
        guest.setHashSecurityAnswer(hashString(securityAnswer));
        
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(FILE_PATH), 
                StandardOpenOption.APPEND, 
                StandardOpenOption.CREATE)) {
            writer.write(guestToFileString(guest));
            writer.newLine();
        }
    }

    // Convert Guest to CSV string
    private static String guestToFileString(Guest guest) {
        return String.join(",",
                guest.getGuestId(),
                guest.getEmail(),
                guest.getName(),
                guest.getPhone(),
                guest.getGender(),
                guest.getPersonality(),
                guest.getSecurityQuestion(),
                guest.getHashPassword(),
                guest.getHashSecurityAnswer());
    }

    // Parse CSV line to Guest
    private static Guest parseGuestFromLine(String line) {
        String[] parts = line.split(",");
        if (parts.length != 9) {
            throw new IllegalArgumentException("Invalid guest data format");
        }
        return new Guest(
                parts[0].trim(), // guestId
                parts[1].trim(), // email
                parts[2].trim(), // name
                parts[3].trim(), // phone
                parts[4].trim(), // gender
                parts[5].trim(), // personality
                parts[6].trim(), // securityQuestion
                parts[7].trim(), // hashPassword
                parts[8].trim()  // hashSecurityAnswer
        );
    }

    // Get all guests
    public static List<Guest> getAllGuests() throws IOException {
        List<Guest> guests = new ArrayList<>();
        if (Files.notExists(Paths.get(FILE_PATH)) || Files.size(Paths.get(FILE_PATH)) == 0) {
            return guests;
        }

        for (String line : Files.readAllLines(Paths.get(FILE_PATH))) {
            if (!line.trim().isEmpty()) {
                guests.add(parseGuestFromLine(line));
            }
        }
        return guests;
    }

    // Find guest by email
    public static Guest getGuestByEmail(String email) throws IOException {
        return getAllGuests().stream()
                .filter(guest -> guest.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    // Verify login credentials
    public static boolean verifyLogin(String email, String password) throws IOException {
        Guest guest = getGuestByEmail(email);
        if (guest == null) return false;
        return guest.getHashPassword().equals(hashString(password));
    }

    // Verify security answer
    public static boolean verifySecurityAnswer(String email, String answer) throws IOException {
        Guest guest = getGuestByEmail(email);
        if (guest == null) return false;
        return guest.getHashSecurityAnswer().equals(hashString(answer));
    }

    // Update guest information
    public static void updateGuest(Guest updatedGuest, String newPassword) throws IOException {
    	
        List<Guest> guests = getAllGuests();
        boolean found = false;
        
        for (int i = 0; i < guests.size(); i++) {
            if (guests.get(i).getGuestId().equals(updatedGuest.getGuestId())) {
            	if (newPassword != null) {
            		updatedGuest.setHashPassword(hashString(newPassword));            		
            	}
            	guests.set(i, updatedGuest);
                found = true;
                break;
            }
        }
        
        if (found) {
            saveAllGuests(guests);
        } else {
            throw new IOException("Guest not found");
        }
    }

    // Save all guests to file
    private static void saveAllGuests(List<Guest> guests) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_PATH))) {
            for (Guest guest : guests) {
                writer.write(guestToFileString(guest));
                writer.newLine();
            }
        }
    }
}