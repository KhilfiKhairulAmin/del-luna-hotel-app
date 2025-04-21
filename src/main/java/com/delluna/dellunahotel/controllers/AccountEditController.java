package com.delluna.dellunahotel.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.delluna.dellunahotel.models.Guest;
import com.delluna.dellunahotel.models.GuestManager;
import com.delluna.dellunahotel.models.ResponseBody;
import com.delluna.dellunahotel.services.GuestService;
import com.delluna.dellunahotel.utils.ApiClient;
import com.delluna.dellunahotel.utils.LoaderFX;
import com.delluna.dellunahotel.utils.UI;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;

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
    public void setGuestData(String fullName, String email, String phone, String gender, String tag, Guest guest) {
        fullNameField.setText(fullName);
        emailField.setText(email);
        phoneField.setText(phone);
        displayGenderField.setText(gender);
        tagField.setText(tag);
        this.guest = guest;
    }
    
    @FXML
    private void handleCancel(ActionEvent event) {
        try {
            Node accountView = FXMLLoader.load(LoaderFX.getFXML("account.fxml"));
            MainController.getInstance().setContent(accountView);
        } catch (IOException e) {
            e.printStackTrace();
        }
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