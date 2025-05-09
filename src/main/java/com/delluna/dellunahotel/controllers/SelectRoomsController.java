package com.delluna.dellunahotel.controllers;

import javax.swing.SwingUtilities;

import com.delluna.dellunahotel.models.Booking;
import com.delluna.dellunahotel.services.BookingListener;
import com.delluna.dellunahotel.services.BookingSingleton;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class SelectRoomsController implements BookingListener {
  @FXML private GridPane roomGrid;
  @FXML private Button continueButton; // Ensure it's in your FXML
  @FXML private Button cancelButton; // Ensure it's in your FXML
  private int roomCount = 0;
  private StackPane selectedCard = null;

  @FXML
  public void initialize() {
    BookingSingleton.getInstance().addListener(this);
  } 
  
  @Override
  public void onBookingChanged(Booking booking) {
      Platform.runLater(() -> {
        if (booking.serviceIds != null) {
            continueButton.setDisable(false);
            cancelButton.setDisable(false);
            MainController.getInstance().changeView("Payment.fxml", Sidebar.EXPLORE);
        }
      });
  }

  public void addRoomCard(String roomNumber, int floor, String type) {
      StackPane card = new StackPane();
      card.setPrefSize(200, 120);
      card.setStyle(defaultCardStyle());

      DropShadow shadow = new DropShadow();
      shadow.setColor(Color.GRAY);
      shadow.setRadius(10);
      card.setEffect(shadow);

      VBox vbox = new VBox(10);
      vbox.setAlignment(Pos.CENTER);

      Label roomLabel = new Label("Room " + roomNumber);
      roomLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
      Label floorLabel = new Label("Floor " + floor);
      Label typeLabel = new Label("Type: " + type);

      vbox.getChildren().addAll(roomLabel, floorLabel, typeLabel);
      card.getChildren().add(vbox);

      // Hover effect
      card.setOnMouseEntered(e -> card.setStyle(defaultCardStyle() + "-fx-border-color: #0078D7;"));
      card.setOnMouseExited(e -> {
          if (card != selectedCard) card.setStyle(defaultCardStyle());
      });

      // Click selection
      card.setOnMouseClicked((MouseEvent e) -> {
          if (selectedCard != null) {
              selectedCard.setStyle(defaultCardStyle());
          }

          selectedCard = card;
          card.setStyle(selectedCardStyle());
          continueButton.setDisable(false);
      });

      int column = roomCount % 3;
      int row = roomCount / 3;
      roomGrid.add(card, column, row);
      roomCount++;
  }

  private String defaultCardStyle() {
      return "-fx-background-color: white; -fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: #cccccc;";
  }

  private String selectedCardStyle() {
      return "-fx-background-color: #E3F2FD; -fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: #2196F3; -fx-border-width: 2;";
  }

  BookingSingleton bs = BookingSingleton.getInstance();

  @FXML
  private void handleContinue() {
        System.out.println("Continue clicked with selected card!");
        Label roomNumLabel = (Label) ((VBox) selectedCard.getChildren().get(0)).getChildren().getFirst();
        String roomNum = roomNumLabel.getText().split(" ")[1];
        bs.setRoom(roomNum);
        SwingUtilities.invokeLater(() -> {
            new ServicesInfo();    // create and show your Swing JFrame
        });
  }

  @FXML
  private void handleCancel() {
    MainController.getInstance().resetCache("SelectRoomss.fxml");
    MainController.getInstance().resetCache("checkingAvailability.fxml");
    MainController.getInstance().changeView("SelectingRooms2.fxml", Sidebar.EXPLORE);
  }
}
