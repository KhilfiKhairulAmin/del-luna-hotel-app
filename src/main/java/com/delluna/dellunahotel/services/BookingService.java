package com.delluna.dellunahotel.services;

import com.delluna.dellunahotel.models.Booking;
import com.delluna.dellunahotel.models.Room;
import com.delluna.dellunahotel.models.Service;
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

    private RoomService roomService = new RoomService();

    public void loadFromFile() {
        try (Reader reader = new FileReader(FILE_PATH)) {
            Type bookingListType = new TypeToken<ArrayList<Booking>>(){}.getType();
            bookings = gson.fromJson(reader, bookingListType);
            if (bookings == null) {
                bookings = new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
            bookings = new ArrayList<>();
        }
    }

    public void saveToFile() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(bookings, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Booking createBooking(Booking booking) {
        if (getBooking(booking.bookingId) != null) {
            return null;
        }
        bookings.add(booking);
        return booking;
    }

    public Booking getBooking(String bookingId) {
        return bookings.stream()
                .filter(b -> b.bookingId.equals(bookingId))
                .findFirst()
                .orElse(null);
    }

    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings);
    }

    public void deleteBooking(String bookingId) {
        bookings.removeIf(b -> b.bookingId.equals(bookingId));
    }

    public Booking updateBooking(Booking updatedBooking) {
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).bookingId.equals(updatedBooking.bookingId)) {
                bookings.set(i, updatedBooking);
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
                .filter(b -> b.checkInDate.compareTo(currentDate) <= 0 && 
                           b.checkOutDate.compareTo(currentDate) >= 0)
                .collect(Collectors.toList());
    }

    public boolean addServiceToBooking(String bookingId, String serviceId) {
        Booking booking = getBooking(bookingId);
        if (booking == null) return false;

        if (booking.serviceIds == null) {
            booking.serviceIds = new String[0];
        }

        for (String id : booking.serviceIds) {
            if (id.equals(serviceId)) {
                return false;
            }
        }

        String[] newServiceIds = new String[booking.serviceIds.length + 1];
        System.arraycopy(booking.serviceIds, 0, newServiceIds, 0, booking.serviceIds.length);
        newServiceIds[booking.serviceIds.length] = serviceId;
        booking.serviceIds = newServiceIds;
        return true;
    }

    public boolean removeServiceFromBooking(String bookingId, String serviceId) {
        Booking booking = getBooking(bookingId);
        if (booking == null || booking.serviceIds == null) return false;

        List<String> services = new ArrayList<>(List.of(booking.serviceIds));
        boolean removed = services.remove(serviceId);
        if (removed) {
            booking.serviceIds = services.toArray(new String[0]);
        }
        return removed;
    }

    public List<String> getAvailableRoomTypes(String dateStart, String dateEnd) {
        List<String> availableRoomTypes = new ArrayList<>();
    
        // Room types go from "1" to "8"
        for (int i = 1; i <= 8; i++) {
            String typeId = String.valueOf(i);
            List<Room> roomsOfType = roomService.getRoomsByType(typeId);
    
            for (Room room : roomsOfType) {
                if (isRoomAvailable(room.roomNum, dateStart, dateEnd)) {
                    availableRoomTypes.add(typeId);
                    break; // No need to check more rooms of this type if one is available
                }
            }
        }
    
        return availableRoomTypes;
    }
    

    private boolean isRoomAvailable(String roomNum, String dateStart, String dateEnd) {
        for (Booking b : bookings) {
            if (b.roomNum.equals(roomNum)) {
                if (!(b.checkOutDate.compareTo(dateStart) < 0 || b.checkInDate.compareTo(dateEnd) > 0)) {
                    return false;
                }
            }
        }
        return true;
    }
}
