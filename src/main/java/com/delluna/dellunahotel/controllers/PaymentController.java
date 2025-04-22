package com.delluna.dellunahotel.controllers;

import com.delluna.dellunahotel.models.Booking;
import com.delluna.dellunahotel.models.Room;
import com.delluna.dellunahotel.models.RoomType;
import com.delluna.dellunahotel.models.Service;
import com.delluna.dellunahotel.services.BookingService;
import com.delluna.dellunahotel.services.BookingSingleton;
import com.delluna.dellunahotel.services.GuestService;
import com.delluna.dellunahotel.services.RoomService;
import com.delluna.dellunahotel.services.ServiceService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PaymentController implements Initializable {

    @FXML private Label bookingIdLabel;
    @FXML private Label guestIdLabel;
    @FXML private Label roomNumLabel;
    @FXML private Label checkInLabel;
    @FXML private Label checkOutLabel;

    @FXML private TableView<Service> servicesTable;
    @FXML private TableColumn<Service, String> serviceNameCol;
    @FXML private TableColumn<Service, Double> servicePriceCol;

    @FXML private Label roomCostLabel;
    @FXML private Label serviceCostLabel;
    @FXML private Label taxLabel;
    @FXML private Label discountLabel;
    @FXML private Label grandTotalLabel;
    @FXML private Label pointsLabel;

    @FXML private Button payButton;
    @FXML private Button cancelButton;

    private Booking booking;

    private final BookingSingleton bookingSingleton = BookingSingleton.getInstance();
    private final ServiceService serviceService = new ServiceService();
    private final RoomService roomService = new RoomService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1) Fetch booking
        Booking booking = bookingSingleton.getBooking();
        booking.guestId = new GuestService().getCurrentGuest().guestId;

        // 2) Populate header labels
        guestIdLabel.setText(booking.guestId);
        roomNumLabel.setText(booking.roomNum);
        checkInLabel.setText(booking.checkInDate);
        checkOutLabel.setText(booking.checkOutDate);

        // 3) Configure service table columns
        serviceNameCol.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        servicePriceCol.setCellValueFactory(new PropertyValueFactory<>("pricePerStay"));

        // 4) Lookup services and populate table
        List<Service> bookedServices = new ArrayList<>();
        double totalServiceCost = 0;
        if (booking.serviceIds != null) {
            for (String sid : booking.serviceIds) {
                Service svc = serviceService.getServiceById(sid);
                if (svc != null) {
                    bookedServices.add(svc);
                    totalServiceCost += svc.pricePerStay;
                }
            }
        }
        servicesTable.setItems(FXCollections.observableArrayList(bookedServices));

        // 5) Calculate room cost
        Room room = roomService.getRoomByNumber(booking.roomNum);
        RoomType roomType = roomService.getRoomTypeById(room.roomTypeId);
        double nightlyRate = (room != null ? roomType.pricePerNight : 0);
        long nights = computeNights(booking.checkInDate, booking.checkOutDate);
        double totalRoomCost = nightlyRate * nights;

        // 6) (Optional) tax & discount from booking model or compute here
        double tax        = booking.taxAmount;
        double discount   = booking.discountAmount;
        double grandTotal = totalRoomCost + totalServiceCost + tax - discount;
        int    points     = booking.pointsObtained;

        booking.totalRoomCost = totalRoomCost;
        booking.totalServiceCost = totalServiceCost;
        booking.taxAmount = 0;
        booking.discountAmount = 0;
        booking.grandTotal = grandTotal;
        booking.pointsObtained = 0;

        // 7) Populate summary labels
        roomCostLabel.setText(roomCostLabel.getText() + " RM" + String.format("$%.2f", totalRoomCost));
        serviceCostLabel.setText(serviceCostLabel.getText() + " RM" + String.format("$%.2f", totalServiceCost));
        taxLabel.setText(taxLabel.getText() + " RM" + String.format("$%.2f", tax));
        discountLabel.setText(discountLabel.getText() + " RM" + String.format("-$%.2f", discount));
        grandTotalLabel.setText(grandTotalLabel.getText() + " RM" + String.format("$%.2f", grandTotal));
        pointsLabel.setText(pointsLabel.getText() + " " + String.valueOf(points));

        Booking newBooking = new BookingService().createBooking(booking);
        bookingIdLabel.setText("Booking ID: " + newBooking.bookingId);
        this.booking = booking;

        // 8) Wire up buttons
        payButton.setOnAction(e -> handlePay());
        cancelButton.setOnAction(e -> handleCancel());
    }

    /** Parses dates “yyyy-MM-dd” and returns days between (inclusive check-in, exclusive check-out). */
    private long computeNights(String checkIn, String checkOut) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate in = LocalDate.parse(checkIn, fmt);
        LocalDate out = LocalDate.parse(checkOut, fmt);
        return ChronoUnit.DAYS.between(in, out);
    }

    /** Dummy handler for Pay Now – implement your payment logic here */
    private void handlePay() {
        System.out.println("Pay Now clicked! Implement payment flow.");
        // e.g. bookingSingleton.resetBooking(); navigate away…
    }

    /** Dummy handler for Cancel – implement cancellation/reset logic here */
    private void handleCancel() {
        System.out.println("Cancel clicked! Resetting booking.");
        bookingSingleton.resetBooking();
        BookingService bs = new BookingService();
        bs.deleteBooking(booking.bookingId);
        MainController.getInstance().changeView("SelectingRooms2.fxml", Sidebar.EXPLORE);
    }
}
