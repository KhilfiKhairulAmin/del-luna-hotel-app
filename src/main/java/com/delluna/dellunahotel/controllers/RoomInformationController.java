package com.delluna.dellunahotel.controllers;

import java.io.IOException;

import com.delluna.dellunahotel.models.RoomType;
import com.delluna.dellunahotel.utils.LoaderFX;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RoomInformationController {

    @FXML
    private Label roomLabel;

    @FXML
    private ImageView roomImage;

    @FXML
    private VBox utilitiesContainer;

    @FXML
    private Button button;

    private RoomType roomType;

    public void setData(RoomType roomType) {
      this.roomType = roomType;
        String name = roomType.typeName.toLowerCase();
        String[] words = name.split(" ");
        int i = 0;
        for (String word: words) {
          words[i++] = Character.toUpperCase(word.charAt(0)) + word.substring(1);
        }

        roomLabel.setText(String.join(" ", words));
        roomImage.setImage(LoaderFX.getImage("rooms/" + name + ".png"));

        Label l = new Label();
        l.setText("Amenities:");
        utilitiesContainer.getChildren().add(l);
        for (String util: roomType.amenities) {
          l = new Label();
          l.setText("• " + util);
          utilitiesContainer.getChildren().add(l);
        }
        l = new Label();
        l.setText(String.format("Price: RM %.2f", roomType.pricePerNight));
        utilitiesContainer.getChildren().add(l);
    }

    @FXML
    private void handleClick() {
      try {
        FXMLLoader loader = new FXMLLoader(LoaderFX.getFXML("checkingAvailability.fxml"));
        VBox roomCard = loader.load();
  
        CheckAvailabilityController controller = loader.getController();
        controller.setData(roomType);

        MainController.getInstance().changeView("checkingAvailability.fxml", Sidebar.EXPLORE, roomCard);
        Stage stage = (Stage) utilitiesContainer.getScene().getWindow();
        stage.close();
      } catch (IOException e)
    	{
    		e.printStackTrace();
    	}
    }
}
