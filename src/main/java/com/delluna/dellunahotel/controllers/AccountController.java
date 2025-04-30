package com.delluna.dellunahotel.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import com.delluna.dellunahotel.models.Guest;
import com.delluna.dellunahotel.services.GuestService;
import com.delluna.dellunahotel.utils.LoaderFX;
import javafx.event.ActionEvent;
import java.io.IOException;

public class AccountController {
    @FXML private Label guestIdLabel;
    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private Label genderLabel;

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

    }

    @FXML
    private void handleEditProfile(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(LoaderFX.getFXML("account_edit.fxml"));
            Node editView = loader.load();
            
            AccountEditController editController = loader.getController();
            editController.setGuestData(guest);
            
            // Get the MainController instance and update content
            MainController.getInstance().changeView("account_edit.fxml", Sidebar.ACCOUNT, editView);
        } catch (IOException e) {
            e.printStackTrace();
            // Show error alert
        }
    }
}