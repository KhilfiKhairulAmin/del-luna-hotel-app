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
                guest.points = Integer.parseInt(parts[5]);
                guest.level = parts[6];
                guest.tag = parts[7];
                guest.passwordHash = parts.length > 8 ? parts[8] : "";
                
                guests.add(guest);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("guestId,email,fullName,phone,gender,points,level,tags,passwordHash\n");
            for (Guest guest : guests) {
                writer.write(String.join(",",
                    guest.guestId,
                    guest.email,
                    guest.fullName,
                    guest.phone,
                    guest.gender,
                    String.valueOf(guest.points),
                    guest.level,
                    guest.tag,
                    guest.passwordHash
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

    // Guest operations
    public String createGuest(Guest guest) throws RuntimeException {
        if (getGuestByEmail(guest.email) != null) {
            throw new RuntimeException("Email already registered");
        }

        guest.level = "First-time User";
        guest.points = 0;
        guest.tag = "Newcomer";
        guest.passwordHash = Hash.hashString(guest.passwordHash);
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

    public boolean updateGuest(Guest updatedGuest) throws RuntimeException {
        Guest existingGuest = getGuestById(updatedGuest.guestId);
        if (existingGuest == null) return false;

        if (!existingGuest.email.equals(updatedGuest.email) && 
            getGuestByEmail(updatedGuest.email) != null) {
            throw new RuntimeException("Email already in use");
        }

        updatedGuest.points = existingGuest.points;
        updatedGuest.level = existingGuest.level;
        updatedGuest.passwordHash = existingGuest.passwordHash;

        guests.replaceAll(g -> g.guestId.equals(updatedGuest.guestId) ? updatedGuest : g);
        saveToFile();
        return true;
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
        if (guest == null || !guest.passwordHash.equals(Hash.hashString(password))) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = JwtUtil.generateToken(guest.guestId);
        saveAuthToken(guest.guestId, token);
        return token;
    }

    public boolean addPoints(String guestId, int points) {
        Guest guest = getGuestById(guestId);
        if (guest != null) {
            guest.points += points;
            saveToFile();
            return true;
        }
        return false;
    }
}