package com.delluna.dellunahotel.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import com.delluna.dellunahotel.models.Guest;
import com.delluna.dellunahotel.models.GuestManager;
import com.delluna.dellunahotel.utils.LoaderFX;

import javafx.event.ActionEvent;
import java.io.IOException;

public class AccountController {
    @FXML private Label guestIdLabel;
    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private Label genderLabel;
    @FXML private Label engagementLabel;
    
    // Sample data - in a real app, you would get this from your database
    private String guestId = "G001";
    private String email = "khilfika@gmail.com";
    private String name = "KFY";
    private String phone = "01156325494";
    private String gender = "Female";
    private String engagement = "Regular";
    
    @FXML
    public void initialize() {
    	
    	try {
			Guest guest = GuestManager.getGuestByEmail(email);
			guestId = guest.getGuestId();
			email = guest.getEmail();
			name = guest.getName();
			phone = guest.getPhone();
			gender = guest.getGender();
			engagement = guest.getPersonality();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
        // Set the labels with the guest data
        guestIdLabel.setText(guestId);
        nameLabel.setText(name);
        emailLabel.setText(email);
        phoneLabel.setText(phone);
        genderLabel.setText(gender);
        engagementLabel.setText(engagement);
        
        // Style the engagement label based on status
        switch(engagement.toLowerCase()) {
            case "newcomer":
                engagementLabel.setStyle("-fx-text-fill: #666666; -fx-font-weight: bold;");
                break;
            case "regular":
                engagementLabel.setStyle("-fx-text-fill: #d5acd1; -fx-font-weight: bold;");
                break;
            case "premium":
                engagementLabel.setStyle("-fx-text-fill: #9c27b0; -fx-font-weight: bold;");
                break;
            case "vip":
                engagementLabel.setStyle("-fx-text-fill: #673ab7; -fx-font-weight: bold;");
                break;
            default:
                engagementLabel.setStyle("-fx-text-fill: #666666; -fx-font-weight: bold;");
        }
    }
    
    @FXML
    private void handleEditProfile(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(LoaderFX.getFXML("account_edit.fxml"));
            Node editView = loader.load();
            
            AccountEditController editController = loader.getController();
            editController.setGuestData(name, phone, gender);
            
            // Get the MainController instance and update content
            MainController.getInstance().setContent(editView);
        } catch (IOException e) {
            e.printStackTrace();
            // Show error alert
        }
    }
}