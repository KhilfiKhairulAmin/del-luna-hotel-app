package com.delluna.dellunahotel.services;

import com.delluna.dellunahotel.models.Booking;

import java.util.ArrayList;
import java.util.List;

public class BookingSingleton {

    private Booking booking = new Booking();
    private static volatile BookingSingleton instance = null;

    private final List<BookingListener> listeners = new ArrayList<>();

    private BookingSingleton() {}

    public static BookingSingleton getInstance() {
        if (instance == null) {
            instance = new BookingSingleton();
        }
        return instance;
    }

    public void addListener(BookingListener listener) {
        listeners.add(listener);
    }

    public void removeListener(BookingListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (BookingListener listener : listeners) {
            listener.onBookingChanged(booking);
        }
    }

    public void setGuestId(String id) {
        booking.guestId = id;
        notifyListeners();
    }

    public void setDate(String checkInDate, String checkOutDate) {
        booking.checkInDate = checkInDate;
        booking.checkOutDate = checkOutDate;
        notifyListeners();
    }

    public void setRoom(String roomNum) {
        booking.roomNum = roomNum;
        notifyListeners();
    }

    public void setServiceIds(String[] serviceIds) {
        booking.serviceIds = serviceIds;
        notifyListeners();
    }

    public Booking getBooking() {
        return booking;
    }

    public void resetBooking() {
        booking = new Booking();
        notifyListeners();
    }
}
