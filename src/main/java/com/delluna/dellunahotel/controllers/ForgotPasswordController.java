package com.delluna.dellunahotel.controllers;

import javafx.event.ActionEvent;  // Event handling package

import javafx.fxml.FXML;          // FXML packages
import javafx.fxml.FXMLLoader;

import javafx.scene.control.*;    // UI components package
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.Parent;       // Scene management packages
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import com.delluna.dellunahotel.models.Guest;
import com.delluna.dellunahotel.services.GuestService;
import com.delluna.dellunahotel.utils.LoaderFX;

import java.io.IOException;       // Error handling package
import java.util.regex.Pattern;   // Input validation package


public class ForgotPasswordController {

	/* @FXML annotation is used to link UI components with attributes of the class to handle UI updates & input retrieval */
	
	// These are fields entered by the user
    @FXML private TextField emailField;
    @FXML private TextField securityQuestionField;
    @FXML private TextField securityAnswerField;
    @FXML private PasswordField passwordField;
    
    // These are fields to display error messages
    @FXML private Label emailError;
    @FXML private Label securityAnswerError;
    @FXML private Label passwordError;
    
    @FXML private Button submitButton;

    @FXML private Rectangle forgotPasswordContainer;

    GuestService GuestManager = new GuestService();

    // These are regular expressions for input validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}$");
    
    private String previousEmail = "";
    private Guest guest = null;

    @FXML public void initialize() {
        Image image = LoaderFX.getImage("pattern.jpg");
        forgotPasswordContainer.setFill(new ImagePattern(image));
    }
    
    /**
     * Handle Sign Up button click event
     * @param event
     */
    @FXML private void handleResetPassword(ActionEvent event) {
        // Reset error messages
        clearErrors();
        
        // Validate inputs
        boolean isValid = true;
        
        // Security answer validation
        if (securityAnswerField.getText().isEmpty()) {
        	securityAnswerError.setText("Security answer is required");
            isValid = false;
        }
        
        // Password validation
        if (passwordField.getText().isEmpty()) {
            passwordError.setText("Password is required");
            isValid = false;
        } else if (!PASSWORD_PATTERN.matcher(passwordField.getText()).matches()) {
            passwordError.setText("Password must be 8+ chars with uppercase, lowercase, number and special char");
            isValid = false;
        }
        
        // If all valid, proceed to sign up
        if (isValid) {
            guest = GuestManager.getGuestByEmail(emailField.getText());
            try {
            	if (guest != null && GuestManager.verifySecurityAnswer(emailField.getText(), securityAnswerField.getText())) {
                    guest.hashPassword = passwordField.getText();
                    GuestManager.updatePassword(emailField.getText(), passwordField.getText());
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Reset password successfully!");
                    switchToSignIn(event);
            	}
            	else {
            		showAlert(Alert.AlertType.INFORMATION, "Error", "Invalid credentials");
            	}
                
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to reset password: " + e.getMessage());
            }
        }
    }
    
    @FXML private void handleSubmitEmail(ActionEvent event) {
        // Reset error messages
        clearErrors();
        
        // Validate inputs
        boolean isValid = true;
        
        // Email validation
        if (emailField.getText().isEmpty()) {
        	emailError.setText("Email is required");
            isValid = false;
        } else if (!EMAIL_PATTERN.matcher(emailField.getText()).matches()) {
        	emailError.setText("Invalid email format");
            isValid = false;
        }
        
        // If all valid, proceed to sign up
        if (isValid) {
            try {
            	if (previousEmail == emailField.getText()) {
            		previousEmail = emailField.getText();
            		return;
            	}
            	
            	// Get a copy of all guests
            	guest = GuestManager.getGuestByEmail(emailField.getText());
            	
            	String securityQuestion = "";
            	if (guest != null) {
            		securityQuestion = guest.securityQuestion;
            	}
            	else {
            		String[] securityQuestions = {
                            "What was your first pet's name?",
                            "What city were you born in?",
                            "What is your mother's maiden name?",
                            "What was your first school's name?",
                            "What is your favorite book?"
            		};
            		securityQuestion = securityQuestions[(int)Math.random()*5];
            	}            	
        		securityQuestionField.setText(securityQuestion);
        		securityQuestionField.setDisable(false);
        		securityQuestionField.setEditable(false);
        		securityAnswerField.setDisable(false);
        		passwordField.setDisable(false);
        		submitButton.setDisable(false);

            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to submit email: " + e.getMessage());
            }
        }
    }

    /**
     * Handle Log In link click event
     * @param event
     */
    @FXML private void switchToSignIn(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(LoaderFX.getFXML("SignIn.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1366, 720));
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load sign in page");
            e.printStackTrace();
        }
    }

    /**
     * Reset error fields to default text
     */
    private void clearErrors() {
        emailError.setText("");
        securityAnswerError.setText("");
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
