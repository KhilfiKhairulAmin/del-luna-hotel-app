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
import com.delluna.dellunahotel.models.GuestManager;
import com.delluna.dellunahotel.utils.Logger;
import com.delluna.dellunahotel.utils.LoaderFX;

import java.io.IOException;       // Error handling package
import java.util.List;
import java.util.regex.Pattern;


public class SignUpController {

	/* @FXML annotation is used to link UI components with attributes of the class to handle UI updates & input retrieval */
	
	// These are fields entered by the user
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> securityQuestionBox;
    @FXML private TextField securityAnswerField;
    @FXML private ComboBox<String> genderBox;
    @FXML private ComboBox<String> personalityComboBox;
    
    // These are fields to display error messages
    @FXML private Label nameError;
    @FXML private Label emailError;
    @FXML private Label phoneError;
    @FXML private Label genderError;
    @FXML private Label passwordError;
    @FXML private Label securityQuestionError;
    @FXML private Label securityAnswerError;

    @FXML private Rectangle signUpImageContainer;
    
    Logger logger = Logger.getInstance();
    

    // These are regular expressions for input validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[+]?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}$");

    @FXML public void initialize() {
        securityQuestionBox.getItems().addAll(
                "What was your first pet's name?",
                "What city were you born in?",
                "What is your mother's maiden name?",
                "What was your first school's name?",
                "What is your favorite book?"
            );
        genderBox.getItems().addAll(
        		"Male",
        		"Female"
            );

        Image image = LoaderFX.getImage("sign_up.png");
        signUpImageContainer.setFill(new ImagePattern(image));
    }
    
    /**
     * Handle Sign Up button click event
     * @param event
     */
    @FXML private void handleSignUp(ActionEvent event) {
        // Reset error messages
        clearErrors();
        
        // Validate inputs
        boolean isValid = true;
        
        // Name validation
        if (nameField.getText().isEmpty()) {
            nameError.setText("Name is required");
            isValid = false;
        } else if (nameField.getText().length() < 3) {
            nameError.setText("Name must be at least 3 characters");
            isValid = false;
        }
        
        // Email validation
        if (emailField.getText().isEmpty()) {
            emailError.setText("Email is required");
            isValid = false;
        } else if (!EMAIL_PATTERN.matcher(emailField.getText()).matches()) {
            emailError.setText("Invalid email format");
            isValid = false;
        }
        
        // Phone validation
        if (phoneField.getText().isEmpty()) {
            phoneError.setText("Phone is required");
            isValid = false;
        } else if (!PHONE_PATTERN.matcher(phoneField.getText()).matches()) {
            phoneError.setText("Invalid phone format");
            isValid = false;
        }
        
        // Security question validation
        if (genderBox.getValue() == null) {
        	genderError.setText("Gender is required");
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
        
        // Security question validation
        if (securityQuestionBox.getValue() == null) {
        	securityQuestionError.setText("Security answer is required");
            isValid = false;
        }
        
        // Security answer validation
        if (securityAnswerField.getText().isEmpty()) {
        	securityAnswerError.setText("Security answer is required");
            isValid = false;
        }
        
        // If all valid, proceed to sign up
        if (isValid) {
            try {
            	// Get a copy of all guests
            	List<Guest> guests = GuestManager.getAllGuests();
            	
            	// Checks if email is unique by removing guests that doesn't have the same email as the entered email
            	guests.removeIf(guest -> !guest.getEmail().equals(emailField.getText()));
            	
            	if (guests.size() == 0) {  // No one has the same email as the entered one
                    Guest newGuest = new Guest(
                            null, // ID will be auto-generated
                            emailField.getText(),
                            nameField.getText(),
                            phoneField.getText(),
                            genderBox.getValue(),
                            "Newcomer",
                            securityQuestionBox.getValue(),
                            null, // Password hash will be set
                            null  // Security answer hash will be set
                        );
                        
                        GuestManager.addGuest(
                            newGuest,
                            passwordField.getText(),
                            securityAnswerField.getText()
                        );
            	}
            	else {  // Someone already took the email
            		throw new Exception("Email already taken");
            	}
                
                // For now, just show success and go to home page
                showAlert(Alert.AlertType.INFORMATION, "Success", "Account created successfully!");
                loadHomePage(event);
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to create account: " + e.getMessage());
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
        nameError.setText("");
        emailError.setText("");
        phoneError.setText("");
        genderError.setText("");
        passwordError.setText("");
        securityQuestionError.setText("");
        securityAnswerError.setText("");
    }

    /**
     * Transition to Homepage
     * @param event
     * @throws IOException
     */
    private void loadHomePage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(LoaderFX.getFXML("Main.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1366, 720));
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
