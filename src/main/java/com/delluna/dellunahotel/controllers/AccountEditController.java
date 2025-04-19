package com.delluna.dellunahotel.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import com.delluna.dellunahotel.models.Guest;
import com.delluna.dellunahotel.models.GuestManager;
import com.delluna.dellunahotel.utils.LoaderFX;

import javafx.event.ActionEvent;
import java.io.IOException;

public class AccountEditController {
    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private TextField genderField;
    
    // This method is called from AccountController to set the initial values
    public void setGuestData(String name, String phone, String gender) {
        nameField.setText(name);
        phoneField.setText(phone);
        genderField.setText(gender);
    }
    
    @FXML
    private void handleCancel(ActionEvent event) {
        // Return to account view using MainController
        try {
            Node accountView = FXMLLoader.load(LoaderFX.getFXML("account.fxml"));
            MainController.getInstance().setContent(accountView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleSaveChanges(ActionEvent event) {
        // Here you would typically save the changes to your database
        String newName = nameField.getText();
        String newPhone = phoneField.getText();
        String newGender = genderField.getText();
        String email = "khilfika@gmail.com";
        
        
        try {
        	Guest guest = GuestManager.getGuestByEmail(email);
        	guest.setName(newName);
        	guest.setPhone(newPhone);
        	guest.setGender(newGender);
        	GuestManager.updateGuest(guest, null);
        	
        } catch(IOException e) {
        	e.printStackTrace();
        }
        	
        
        // After saving, return to the account info page
        handleCancel(event);
    }
}