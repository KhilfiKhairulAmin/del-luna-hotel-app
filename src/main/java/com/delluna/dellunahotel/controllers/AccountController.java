package com.delluna.dellunahotel.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import com.delluna.dellunahotel.models.Guest;
import com.delluna.dellunahotel.services.GuestService;
import com.delluna.dellunahotel.models.ResponseBody;
import com.delluna.dellunahotel.utils.ApiClient;
import com.delluna.dellunahotel.utils.LoaderFX;
import com.delluna.dellunahotel.utils.UI;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import java.io.IOException;

public class AccountController {
    @FXML private Label guestIdLabel;
    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private Label genderLabel;
    @FXML private Label pointsLabel;
    @FXML private Label levelLabel;
    @FXML private Label tagLabel;

    Guest guest;
    GuestService guestService = new GuestService();
    
    @FXML
    public void initialize() {
        guest = guestService.getCurrentGuest();
        guestIdLabel.setText(guest.guestId);
        nameLabel.setText(guest.fullName);
        emailLabel.setText(guest.email);
        phoneLabel.setText(guest.phone);
        genderLabel.setText(guest.gender);
        pointsLabel.setText(String.valueOf(guest.points));
        levelLabel.setText(guest.level);
        tagLabel.setText(guest.tag);
    }

    @FXML
    private void handleEditProfile(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(LoaderFX.getFXML("account_edit.fxml"));
            Node editView = loader.load();
            
            AccountEditController editController = loader.getController();
            editController.setGuestData(guest.fullName, guest.email, guest.phone, guest.gender, guest.tag, guest);
            
            // Get the MainController instance and update content
            MainController.getInstance().setContent(editView);
        } catch (IOException e) {
            e.printStackTrace();
            // Show error alert
        }
    }
}