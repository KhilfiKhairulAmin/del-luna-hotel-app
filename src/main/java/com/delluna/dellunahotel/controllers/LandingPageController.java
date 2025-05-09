package com.delluna.dellunahotel.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class LandingPageController {

    @FXML private BorderPane root;
    @FXML private ImageView heroImage;
    @FXML private Button bookNow;
    @FXML private Label mainTitle;

    @FXML
    public void initialize() {
        // Load default hero image

    }

    @FXML
    private void onHeroClick(MouseEvent event) {
        // optional: add interaction when hero image is clicked
        System.out.println("Hero image clicked!");
    }

    @FXML
    private void onBookNow() {
        MainController.getInstance().changeView("SelectingRooms2.fxml", Sidebar.EXPLORE);
    }
}