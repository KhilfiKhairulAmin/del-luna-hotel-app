package com.delluna.dellunahotel.controllers;

import java.io.IOException;
import java.util.List;

import com.delluna.dellunahotel.models.Booking;
import com.delluna.dellunahotel.services.BookingService;
import com.delluna.dellunahotel.services.GuestService;
import com.delluna.dellunahotel.utils.LoaderFX;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class BookingListController {

    @FXML private TableView<Booking> bookingTable;
    @FXML private TableColumn<Booking, String> colBookingId;
    @FXML private TableColumn<Booking, String> colCheckIn;
    @FXML private TableColumn<Booking, String> colCheckOut;

    @FXML
    public void initialize() {
        // 1. Set up cell value factories
        colBookingId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        colCheckIn.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
        colCheckOut.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));

        String guestId = new GuestService().getCurrentGuest().guestId;
        List<Booking> bookingList = new BookingService().getBookingsByGuest(guestId);
        ObservableList<Booking> bookings = FXCollections.observableArrayList(
          bookingList
        );
        bookingTable.setItems(bookings);

        // 3. Row click listener to navigate to detail view
        bookingTable.setRowFactory(tv -> {
            TableRow<Booking> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 1) {
                    Booking clickedBooking = row.getItem();
                    openBookingDetail(clickedBooking);
                }
            });
            return row;
        });  // mouse-click event on a table row :contentReference[oaicite:2]{index=2}
    }

    private void openBookingDetail(Booking booking) {
      FXMLLoader loader = new FXMLLoader(LoaderFX.getFXML("BookingDetail.fxml"));
      
      try {
        Node editView = loader.load();
        BookingDetailController editController = loader.getController();
        editController.setBooking(booking);
        MainController.getInstance().changeView("BookingDetail.fxml", Sidebar.BOOKINGS, editView);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
}
