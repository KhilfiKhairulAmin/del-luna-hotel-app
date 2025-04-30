package com.delluna.dellunahotel.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.List;

import com.delluna.dellunahotel.models.RoomType;
import com.delluna.dellunahotel.services.RoomService;

public class RoomSelectionController {
	@FXML
	private ImageView singleImage;
	@FXML
	private ImageView doubleImage;
	@FXML
	private ImageView tripleImage;
	@FXML
	private ImageView quadImage;
	@FXML
	private ImageView executiveImage;
	@FXML
	private ImageView suiteImage;
	@FXML
	private ImageView familyImage;
	@FXML
	private ImageView honeymoonImage;
	
	@FXML
	private Label singleLabel;
	@FXML
	private Label doubleLabel;
	@FXML
	private Label tripleLabel;
	@FXML
	private Label quadLabel;
	@FXML
	private Label executiveLabel;
	@FXML
	private Label suiteLabel;
	@FXML
	private Label familyLabel;
	@FXML
	private Label honeymoonLabel;

	@FXML
	private VBox roomContainer; // The VBox in your ScrollPane

	RoomService roomService = new RoomService();

public void initialize() {

	List<RoomType> roomTypes = roomService.getAllRoomTypes();

	for (RoomType room : roomTypes) {
			try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/delluna/dellunahotel/views/RoomCard.fxml"));
					HBox roomCard = loader.load();

					RoomCardController controller = loader.getController();
					controller.setData(room);

					roomContainer.getChildren().add(roomCard);
			} catch (IOException e) {
					e.printStackTrace();
			}
	}
}
}
