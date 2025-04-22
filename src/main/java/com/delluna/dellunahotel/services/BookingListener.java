package com.delluna.dellunahotel.services;

import com.delluna.dellunahotel.models.Booking;

public interface BookingListener {
  void onBookingChanged(Booking booking);
}