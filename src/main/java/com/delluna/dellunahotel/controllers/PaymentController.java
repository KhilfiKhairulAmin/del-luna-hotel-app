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
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.SwingUtilities;

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
    @FXML private Label pricePerNightLabel;

    @FXML private Button payButton;
    @FXML private Button cancelButton;

    private Booking booking;
    

    private final BookingSingleton bookingSingleton = BookingSingleton.getInstance();
    private final ServiceService serviceService = new ServiceService();
    private final RoomService roomService = new RoomService();

    @FXML private Rectangle forgotPasswordImageContainer;

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
        double tax        = 0.06 * (totalRoomCost + totalServiceCost);
        double deposit   = 100;
        double grandTotal = totalRoomCost + totalServiceCost + tax + deposit;

        booking.totalRoomCost = totalRoomCost;
        booking.totalServiceCost = totalServiceCost;
        booking.taxAmount = tax;
        booking.depositAmount = deposit;
        booking.grandTotal = grandTotal;

        // 7) Populate summary labels
        roomCostLabel.setText(roomCostLabel.getText() + String.format(" RM%.2f", totalRoomCost));
        serviceCostLabel.setText(serviceCostLabel.getText() + String.format(" RM%.2f", totalServiceCost));
        taxLabel.setText(taxLabel.getText() + String.format(" RM%.2f", tax));
        discountLabel.setText(discountLabel.getText() + String.format(" RM%.2f", deposit));
        grandTotalLabel.setText(grandTotalLabel.getText() + String.format(" RM%.2f", grandTotal));
        pricePerNightLabel.setText(String.format("RM%.2f", roomType.pricePerNight));

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
        MainController.getInstance().resetCache("Payment.fxml");
        MainController.getInstance().resetCache("SelectRooms.fxml");
        MainController.getInstance().resetCache("checkingAvailability.fxml");

        SwingUtilities.invokeLater(() -> {
            new PaymentOptionsGUI(() -> {
                javafx.application.Platform.runLater(() -> {
                    MainController.getInstance().resetCache("BookingList.fxml");
                    MainController.getInstance().changeView("BookingList.fxml", Sidebar.BOOKINGS);
                });
            });
        });
        
    }

    /** Dummy handler for Cancel – implement cancellation/reset logic here */
    private void handleCancel() {
        System.out.println("Cancel clicked! Resetting booking.");
        bookingSingleton.resetBooking();
        BookingService bs = new BookingService();
        bs.deleteBooking(booking.bookingId);
        MainController.getInstance().resetCache("Payment.fxml");
        MainController.getInstance().resetCache("SelectRoomss.fxml");
        MainController.getInstance().resetCache("checkingAvailability.fxml");
        MainController.getInstance().changeView("SelectingRooms2.fxml", Sidebar.EXPLORE);
    }
}
