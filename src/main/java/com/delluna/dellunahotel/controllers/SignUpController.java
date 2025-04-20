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
import com.delluna.dellunahotel.models.Guest2;
import com.delluna.dellunahotel.models.GuestManager;
import com.delluna.dellunahotel.models.ResponseBody;
import com.delluna.dellunahotel.utils.Logger;
import com.delluna.dellunahotel.utils.Transtition;
import com.delluna.dellunahotel.utils.UI;
import com.delluna.dellunahotel.utils.ApiClient;
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
    @FXML private ComboBox<String> genderBox;
    
    // These are fields to display error messages
    @FXML private Label nameError;
    @FXML private Label emailError;
    @FXML private Label phoneError;
    @FXML private Label genderError;
    @FXML private Label passwordError;

    @FXML private Rectangle signUpImageContainer;
    
    Logger logger = Logger.getInstance();

    @FXML public void initialize() {
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
        UI.clearErrors(nameError, emailError, phoneError, genderError, passwordError);

        Guest2 g = new Guest2();
        g.email = emailField.getText();
        g.passwordHash = passwordField.getText();
        g.fullName = nameField.getText();
        g.gender = genderBox.getValue() == null ? "" : genderBox.getValue();
        g.phone = phoneField.getText();

        ApiClient.<Guest2, ResponseBody>loginService("http://localhost:4567/guest/sign_up", g, ResponseBody.class)
            .valueProperty()
            .addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    
                    if (newVal.isSuccess) {
                        UI.showAlert(Alert.AlertType.INFORMATION, "Success", "Logged in successfully!");
                        loadHomePage(event);
                    } else {
                        String prop;

                        if (newVal.properties.size() == 0) {
                            UI.showAlert(Alert.AlertType.ERROR, "Error", "Email is already taken");
                            return;
                        }

                        // Check email
                        prop = newVal.getProperty("email");
                        if (prop != null) {
                            emailError.setText(prop);
                        }

                        // Check password
                        prop = newVal.getProperty("password");
                        if (prop != null) {
                            passwordError.setText(prop);
                        }

                        // Check password
                        prop = newVal.getProperty("fullName");
                        if (prop != null) {
                            nameError.setText(prop);
                        }

                        // Check password
                        prop = newVal.getProperty("gender");
                        if (prop != null) {
                            genderError.setText(prop);
                        }

                        // Check password
                        prop = newVal.getProperty("phone");
                        if (prop != null) {
                            phoneError.setText(prop);
                        }
                    }
                }
            });
    }

    /**
     * Handle Log In link click event
     * @param event
     */
    @FXML private void switchToSignIn(ActionEvent event) {
        Transtition.switchPage(event, "SignIn.fxml");
    }

    private void loadHomePage(ActionEvent event) {
        Transtition.switchPage(event, "Main.fxml");
    }
}
