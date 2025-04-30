package com.delluna.dellunahotel.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import com.delluna.dellunahotel.models.Booking;
import com.delluna.dellunahotel.models.Room;
import com.delluna.dellunahotel.services.RoomService;
import com.delluna.dellunahotel.utils.LoaderFX;

import javafx.event.ActionEvent;

public class BookingDetailController {

    @FXML private Label labelBookingId;
    @FXML private Label labelCheckInDate;
    @FXML private Label labelCheckOutDate;
    @FXML private Label labelGuestName;
    @FXML private Label labelRoomNumber;
    @FXML private Label labelTotalPrice;
    @FXML private ImageView bookingImage;

    private Booking booking;

    public void setBooking(Booking booking) {
        this.booking = booking;
        updateUI();
    }

    private void updateUI() {
        labelBookingId.setText(booking.getBookingId());
        labelCheckInDate.setText(booking.getCheckInDate().toString());
        labelCheckOutDate.setText(booking.getCheckOutDate().toString());
        labelGuestName.setText(booking.guestId);
        labelRoomNumber.setText(booking.roomNum);
        labelTotalPrice.setText("RM " + booking.grandTotal);
        
        Room room = new RoomService().getRoomByNumber(booking.roomNum);
        String name = new RoomService().getRoomTypeById(room.roomTypeId).typeName;

        // Optionally load an image if you have one
        bookingImage.setImage(LoaderFX.getImage("rooms/" + name + ".png"));
    }

    @FXML
    private void handleBack(ActionEvent event) {
        MainController.getInstance().changeView("BookingList.fxml", Sidebar.BOOKINGS);
        MainController.getInstance().resetCache("BookingDetail.fxml");
    }
}
