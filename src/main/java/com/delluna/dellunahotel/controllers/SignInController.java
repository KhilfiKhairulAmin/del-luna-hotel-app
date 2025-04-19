package com.delluna.dellunahotel.controllers;

import javafx.event.ActionEvent;  // Event handling package

import javafx.fxml.FXML; 		  // FXML handling packages
import javafx.fxml.FXMLLoader;

import javafx.scene.control.*;	  // UI components package

import javafx.scene.Node;		  // Scene management packages
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.delluna.dellunahotel.models.GuestManager;
import com.delluna.dellunahotel.utils.LoaderFX;

import java.io.IOException;		  // Error handling package

import java.util.regex.Pattern;   // Input validation package


public class SignInController {

	// Input fields
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox rememberMeCheckbox;
    
    // Error message fields
    @FXML private Label emailError;
    @FXML private Label passwordError;
    
    // Input validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    /**
     * Handle Sign In button click event
     * @param event
     */
    @FXML private void handleSignIn(ActionEvent event) {
    	
    	clearErrors();
    	
    	boolean isValid = true;
    	
    	if (emailField.getText().isEmpty()) {
    		emailError.setText("Email is required");
    		isValid = false;
    	}
    	else if (!EMAIL_PATTERN.matcher(emailField.getText()).matches()) {
            emailError.setText("Invalid email format");
            isValid = false;
    	}
    	
    	if (passwordField.getText().isEmpty()) {
    		passwordError.setText("Password is required");
    		isValid = false;
    	}
    	
        if (isValid) {
            try {
                boolean isValidGuest = GuestManager.verifyLogin(emailField.getText(), passwordField.getText());
                
                if (isValidGuest) {
                	showAlert(Alert.AlertType.INFORMATION, "Success", "Logged in successfully!");
                	loadHomePage(event);
                }
                else showAlert(Alert.AlertType.ERROR, "Error", "Invalid credentials");
                
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to create account: " + e.getMessage());
            }
        }
    }
    
    /**
     * Reset error fields to default text
     */
    private void clearErrors() {
        emailError.setText("");
        passwordError.setText("");
    }

    /**
     * Handle Sign Up link click event
     * @param event
     */
    @FXML private void switchToSignUp(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(LoaderFX.getFXML("SignUp.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1366, 720));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Handle Forgot Password link click event
     * @param event
     */
    @FXML private void switchToForgotPassword(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(LoaderFX.getFXML("ForgotPassword.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1366, 720));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Transition to Homepage
     * @param event
     * @throws IOException
     */
    private void loadHomePage(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(LoaderFX.getFXML("Main.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1366, 720));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Show a pop up box to alert the user
     * @param alertType
     * @param title Title of the pop up window
     * @param message Message of the pop up window
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
