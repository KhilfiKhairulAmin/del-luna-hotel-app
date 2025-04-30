package com.delluna.dellunahotel.services;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.delluna.dellunahotel.models.Guest;
import com.delluna.dellunahotel.utils.Hash;
import com.delluna.dellunahotel.utils.JwtUtil;

public class GuestService {
    private static final String FILE_PATH = "src/main/resources/com/delluna/dellunahotel/database/guests.txt";
    private static final String AUTH_FILE_PATH = "src/main/resources/com/delluna/dellunahotel/database/auth.txt";
    private List<Guest> guests = new ArrayList<>();

    public GuestService() {
        loadFromFile();
    }

    // File operations
private void loadFromFile() {
    guests.clear();
    try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
        reader.readLine(); // Skip header
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",", -1);
            if (parts.length < 8) continue;

            Guest guest = new Guest();
            guest.guestId = parts[0];
            guest.email = parts[1];
            guest.fullName = parts[2];
            guest.phone = parts[3];
            guest.gender = parts[4];
            guest.securityQuestion = parts[5];
            guest.hashPassword = parts[6];
            guest.hashSecurityAnswer = parts.length > 7 ? parts[7] : "";
            
            guests.add(guest);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("guestId,email,fullName,phone,gender,securityQuestion,passwordHash,securityAnswerHash\n");
            for (Guest guest : guests) {
                writer.write(String.join(",",
                    guest.guestId,
                    guest.email,
                    guest.fullName,
                    guest.phone,
                    guest.gender,
                    guest.securityQuestion,
                    guest.hashPassword,
                    guest.hashSecurityAnswer
                ));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Auth token operations
    private Optional<String> getLatestToken() {
        try (BufferedReader reader = new BufferedReader(new FileReader(AUTH_FILE_PATH))) {
            String lastLine = "", line;
            while ((line = reader.readLine()) != null) {
                lastLine = line;
            }
            if (!lastLine.isEmpty()) {
                String[] parts = lastLine.split(",");
                if (parts.length == 2) {
                    return Optional.of(parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private void saveAuthToken(String guestId, String token) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(AUTH_FILE_PATH, true))) {
            writer.write(guestId + "," + token);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(AUTH_FILE_PATH))) {
            // Opening the file like this with no content clears it
            writer.write(""); // Optional: can omit this line
        } catch (IOException e) {
            e.printStackTrace();
        }        
    }

    // Guest operations
    public String createGuest(Guest guest) throws RuntimeException {
        if (getGuestByEmail(guest.email) != null) {
            throw new RuntimeException("Email already registered");
        }

        guest.hashPassword = Hash.hashString(guest.hashPassword);
        guest.hashSecurityAnswer = Hash.hashString(guest.hashSecurityAnswer);
        guest.guestId = guests.isEmpty() ? "1" : 
            String.valueOf(Integer.parseInt(guests.get(guests.size()-1).guestId) + 1);

        guests.add(guest);
        saveToFile();

        String token = JwtUtil.generateToken(guest.guestId);
        saveAuthToken(guest.guestId, token);
        return token;
    }

    public Guest getCurrentGuest() throws RuntimeException {
        Optional<String> tokenOptional = getLatestToken();
        if (!tokenOptional.isPresent()) {
            throw new RuntimeException("No active session found");
        }

        String token = tokenOptional.get();
        try {
            String guestId = JwtUtil.validateToken(token);
            return getGuestById(guestId);
        } catch (RuntimeException e) {
            throw new RuntimeException("Invalid or expired session");
        }
    }

    public boolean verifyLogin(String email, String password) throws RuntimeException {
        Guest guest = getGuestByEmail(email);
        System.out.println(guest.email);
        if (guest == null || !guest.hashPassword.equals(Hash.hashString(password))) {
            return false;
        }
        String token = JwtUtil.generateToken(guest.guestId);
        saveAuthToken(guest.guestId, token);
        return true;
    }

    public boolean updateGuest(Guest updatedGuest) throws RuntimeException {
        Guest existingGuest = getGuestById(updatedGuest.guestId);
        if (existingGuest == null) return false;

        if (!existingGuest.email.equals(updatedGuest.email) && 
            getGuestByEmail(updatedGuest.email) != null) {
            throw new RuntimeException("Email already in use");
        }
        
        // updatedGuest.hashPassword = existingGuest.hashPassword;
        // updatedGuest.hashSecurityAnswer = existingGuest.hashSecurityAnswer;

        guests.replaceAll(g -> g.guestId.equals(updatedGuest.guestId) ? updatedGuest : g);
        saveToFile();
        return true;
    }

    public void updatePassword(String email, String newPassword) throws RuntimeException {
        Guest guest = getGuestByEmail(email);
        if (guest == null) throw new RuntimeException("Guest not found");

        guest.hashPassword = Hash.hashString(newPassword);
        updateGuest(guest);
    }

    public Guest getGuestById(String guestId) {
        return guests.stream()
                .filter(g -> g.guestId.equals(guestId))
                .findFirst()
                .orElse(null);
    }

    public Guest getGuestByEmail(String email) {
        return guests.stream()
                .filter(g -> g.email.equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    public List<Guest> getAllGuests() {
        return new ArrayList<>(guests);
    }

    public void deleteGuest(String guestId) {
        guests.removeIf(g -> g.guestId.equals(guestId));
        saveToFile();
    }

    public String authenticate(String email, String password) {
        Guest guest = getGuestByEmail(email);
        if (guest == null || !guest.hashPassword.equals(Hash.hashString(password))) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = JwtUtil.generateToken(guest.guestId);
        saveAuthToken(guest.guestId, token);
        return token;
    }

    public boolean verifySecurityAnswer(String email, String answer) {
        Guest guest = getGuestByEmail(email);
        return guest != null && guest.hashSecurityAnswer.equals(Hash.hashString(answer));
    }

    public boolean resetPassword(String email, String newPassword, String securityAnswer) {
        Guest guest = getGuestByEmail(email);
        if (guest == null || !guest.hashSecurityAnswer.equals(Hash.hashString(securityAnswer))) {
            return false;
        }
        
        guest.hashPassword = Hash.hashString(newPassword);
        saveToFile();
        return true;
    }
}