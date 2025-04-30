package com.delluna.dellunahotel.models;

public class Booking {
  public String bookingId;
  public String guestId;
  public String roomNum;
  public String[] serviceIds = null;
  public String checkInDate;
  public String checkOutDate;
  public double totalRoomCost;
  public double totalServiceCost;
  public double taxAmount;
  public double depositAmount;
  public double grandTotal;
  public int pointsObtained;

  public String getBookingId() {
      return bookingId;
  }

  public String getCheckInDate() {
      return checkInDate;
  }
  public String getCheckOutDate() {
    return checkOutDate;
  }

}
