package com.delluna.dellunahotel.controllers;

import javax.swing.SwingUtilities;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class SelectRoomsController {
  @FXML private GridPane roomGrid;
  @FXML private Button continueButton; // Ensure it's in your FXML
  private int roomCount = 0;

  private StackPane selectedCard = null;

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

  @FXML
  private void handleContinue() {
      System.out.println("Continue clicked with selected card!");
      Label roomNumLabel = (Label) ((VBox) selectedCard.getChildren().get(0)).getChildren().getFirst();
              SwingUtilities.invokeLater(() -> {
            new HotelBooking2(roomNumLabel.getText().split(" ")[1]);    // create and show your Swing JFrame
        });

        // 2) Close the JavaFX window (optional)
        // Stage fxStage = (Stage) continueButton.getScene().getWindow();
        // fxStage.close();
  }

}
