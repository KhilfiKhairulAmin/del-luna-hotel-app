package com.delluna.dellunahotel.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.delluna.dellunahotel.models.Guest;
import com.delluna.dellunahotel.services.GuestService;
import com.delluna.dellunahotel.utils.LoaderFX;
import javafx.event.ActionEvent;
import java.io.IOException;

public class AccountEditController {
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField displayGenderField;
    @FXML private TextField tagField;
    @FXML private Label errorMsg;

    Guest guest;
    GuestService guestService = new GuestService();
    
    // Set initial values from account info
    public void setGuestData(Guest guest) {
        fullNameField.setText(guest.fullName);
        emailField.setText(guest.email);
        phoneField.setText(guest.phone);
        displayGenderField.setText(guest.gender);
        tagField.setText(guest.tag);
        this.guest = guest;
    }
    
    @FXML
    private void handleCancel(ActionEvent event) {
        MainController.getInstance().changeView("account.fxml", Sidebar.ACCOUNT);
    }
    
    @FXML
    private void handleSaveChanges(ActionEvent event) {
        errorMsg.setText("");

        String newEmail = emailField.getText();
        String newPhone = phoneField.getText();
        String newTag = tagField.getText();

        guest.email = newEmail;
        guest.phone = newPhone;
        guest.tag = newTag;

        guestService.updateGuest(guest);
        handleCancel(event);
    }
}