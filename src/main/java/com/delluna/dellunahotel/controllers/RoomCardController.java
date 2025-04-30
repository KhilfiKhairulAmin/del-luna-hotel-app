package com.delluna.dellunahotel.controllers;

import java.io.IOException;

import com.delluna.dellunahotel.models.RoomType;
import com.delluna.dellunahotel.utils.LoaderFX;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RoomCardController {

    @FXML
    private Label roomLabel;

    @FXML
    private ImageView roomImage;


    private RoomType roomType;

    public void setData(RoomType roomType) {
        this.roomType = roomType;
        String name = this.roomType.typeName.toLowerCase();
        String[] words = name.split(" ");
        int i = 0;
        for (String word: words) {
          words[i++] = Character.toUpperCase(word.charAt(0)) + word.substring(1);
        }
        String out = String.join(" ", words);
        roomLabel.setText(out);
        roomImage.setImage(LoaderFX.getImage("rooms/" + name + ".png"));

    }

    @FXML
    private void handleClick() {
        try
    	{
            FXMLLoader loader = new FXMLLoader(LoaderFX.getFXML("RoomInformation.fxml"));
			VBox roomCard = loader.load();

            RoomInformationController controller = loader.getController();
            controller.setData(roomType);

            Stage stage = new Stage();
            stage.setTitle(roomType.typeName + " Description");
            stage.setScene(new Scene(roomCard));
            stage.show();
        	
    	}catch (IOException e)
    	{
    		e.printStackTrace();
    	}
    }
}
