package com.delluna.dellunahotel.services;

import com.delluna.dellunahotel.models.Booking;
import com.delluna.dellunahotel.models.Room;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class BookingService {
    private static final String FILE_PATH = "src/main/resources/com/delluna/dellunahotel/database/bookings.json";
    private List<Booking> bookings = new ArrayList<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Next booking ID counter (numeric increment)
    private int nextBookingId = 1;

    private RoomService roomService = new RoomService();

    public BookingService() {
        loadFromFile();
    }

    /**
     * Load bookings from JSON file and initialize nextBookingId
     */
    public void loadFromFile() {
        try (Reader reader = new FileReader(FILE_PATH)) {
            Type bookingListType = new TypeToken<ArrayList<Booking>>() {}.getType();
            bookings = gson.fromJson(reader, bookingListType);
            if (bookings == null) {
                bookings = new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
            bookings = new ArrayList<>();
        }
        // Determine highest existing ID and set nextBookingId
        int maxId = bookings.stream()
            .map(b -> {
                try { return Integer.parseInt(b.bookingId); }
                catch (Exception ex) { return 0; }
            })
            .max(Integer::compareTo)
            .orElse(0);
        nextBookingId = maxId + 1;
    }

    /**
     * Save current bookings list back to JSON file
     */
    public void saveToFile() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(bookings, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a new booking, auto-assigning an incremental ID if not set.
     * Returns the booking with ID, or null if ID conflict.
     */
    public Booking createBooking(Booking booking) {
        // Auto-assign bookingId if missing or blank
        if (booking.bookingId == null || booking.bookingId.isEmpty()) {
            booking.bookingId = String.valueOf(nextBookingId++);
        } else {
            // If provided ID already exists, fail
            if (getBooking(booking.bookingId) != null) {
                return null;
            }
        }
        bookings.add(booking);
        saveToFile();
        return booking;
    }

    public Booking getBooking(String bookingId) {
        return bookings.stream()
            .filter(b -> b.bookingId.equals(bookingId))
            .findFirst().orElse(null);
    }

    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings);
    }

    public void deleteBooking(String bookingId) {
        boolean removed = bookings.removeIf(b -> b.bookingId.equals(bookingId));
        if (removed) saveToFile();
    }

    public Booking updateBooking(Booking updatedBooking) {
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).bookingId.equals(updatedBooking.bookingId)) {
                bookings.set(i, updatedBooking);
                saveToFile();
                return updatedBooking;
            }
        }
        return null;
    }

    public List<Booking> getBookingsByGuest(String guestId) {
        return bookings.stream()
            .filter(b -> b.guestId.equals(guestId))
            .collect(Collectors.toList());
    }

    public List<Booking> getBookingsByRoom(String roomNum) {
        return bookings.stream()
            .filter(b -> b.roomNum.equals(roomNum))
            .collect(Collectors.toList());
    }

    public List<Booking> getActiveBookings(String currentDate) {
        return bookings.stream()
            .filter(b -> b.checkInDate.compareTo(currentDate) <= 0
                      && b.checkOutDate.compareTo(currentDate) >= 0)
            .collect(Collectors.toList());
    }

    public boolean addServiceToBooking(String bookingId, String serviceId) {
        Booking booking = getBooking(bookingId);
        if (booking == null) return false;
        if (booking.serviceIds == null) {
            booking.serviceIds = new String[0];
        }
        for (String id : booking.serviceIds) {
            if (id.equals(serviceId)) return false;
        }
        String[] newIds = Arrays.copyOf(booking.serviceIds, booking.serviceIds.length + 1);
        newIds[newIds.length - 1] = serviceId;
        booking.serviceIds = newIds;
        saveToFile();
        return true;
    }

    public boolean removeServiceFromBooking(String bookingId, String serviceId) {
        Booking booking = getBooking(bookingId);
        if (booking == null || booking.serviceIds == null) return false;
        List<String> list = new ArrayList<>(Arrays.asList(booking.serviceIds));
        boolean removed = list.remove(serviceId);
        if (removed) {
            booking.serviceIds = list.toArray(new String[0]);
            saveToFile();
        }
        return removed;
    }

    public List<Room> getAvailableRoomsByTypeAndDate(String roomTypeId, String dateStart, String dateEnd) {
        List<Room> available = new ArrayList<>();
        for (Room room : roomService.getRoomsByType(roomTypeId)) {
            if (isRoomAvailable(room.roomNum, dateStart, dateEnd)) {
                available.add(room);
            }
        }
        return available;
    }

    public List<String> getAvailableRoomTypes(String dateStart, String dateEnd) {
        List<String> types = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            String typeId = String.valueOf(i);
            for (Room r : roomService.getRoomsByType(typeId)) {
                if (isRoomAvailable(r.roomNum, dateStart, dateEnd)) {
                    types.add(typeId);
                    break;
                }
            }
        }
        return types;
    }

    private boolean isRoomAvailable(String roomNum, String dateStart, String dateEnd) {
        for (Booking b : bookings) {
            if (b.roomNum.equals(roomNum)) {
                if (!(b.checkOutDate.compareTo(dateStart) < 0
                   || b.checkInDate.compareTo(dateEnd) > 0)) {
                    return false;
                }
            }
        }
        return true;
    }
}
