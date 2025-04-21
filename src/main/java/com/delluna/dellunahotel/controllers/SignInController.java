package com.delluna.dellunahotel.controllers;

import javafx.event.ActionEvent;  // Event handling package

import javafx.fxml.FXML; 		  // FXML handling packages
import javafx.fxml.FXMLLoader;

import javafx.scene.control.*;	  // UI components package

import javafx.scene.Node;		  // Scene management packages
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.delluna.dellunahotel.models.Guest2;
import com.delluna.dellunahotel.models.ResponseBody;
import com.delluna.dellunahotel.services.GuestService;
import com.delluna.dellunahotel.models.GuestManager;
import com.delluna.dellunahotel.utils.AlertBox;
import com.delluna.dellunahotel.utils.ApiClient;
import com.delluna.dellunahotel.utils.LoaderFX;
import com.delluna.dellunahotel.utils.Transtition;
import com.delluna.dellunahotel.utils.UI;

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

    @FXML Button signInButton;

    /**
     * Handle Sign In button click event
     * @param event
     */
    @FXML private void handleSignIn(ActionEvent event) {

        UI.clearErrors(emailError, passwordError);

        GuestService guestService = new GuestService();

        try {
            guestService.authenticate(emailField.getText(), passwordField.getText());
        } catch (RuntimeException e) {
            AlertBox.error("Error", "Invalid credentials");
        }
        loadHomePage(event);

        // ApiClient.<Guest2, ResponseBody>loginService("http://localhost:4567/guest/sign_in", g, ResponseBody.class)
        //     .valueProperty()
        //     .addListener((obs, oldVal, newVal) -> {
        //         if (newVal != null) {
        //             System.out.println(newVal.isSuccess);
        //             if (newVal.isSuccess) {
        //                 UI.showAlert(Alert.AlertType.INFORMATION, "Success", "Logged in successfully!");
        //                 loadHomePage(event);
        //             } else {
        //                 String prop;

        //                 if (newVal.properties.size() == 0) {
        //                     UI.showAlert(Alert.AlertType.ERROR, "Error", "Invalid credentials");
        //                     return;
        //                 }

        //                 // Check email
        //                 prop = newVal.getProperty("email");
        //                 if (prop != null) {
        //                     emailError.setText(prop);
        //                 }

        //                 // Check password
        //                 prop = newVal.getProperty("password");
        //                 if (prop != null) {
        //                     passwordError.setText(prop);
        //                 }
        //             }
        //         }
        //     });
    }

    @FXML private void switchToSignUp(ActionEvent event) {
        Transtition.switchPage(event, "SignUp.fxml");
    }

    @FXML private void switchToForgotPassword(ActionEvent event) {
        Transtition.switchPage(event, "ForgotPassword.fxml");
    }

    private void loadHomePage(ActionEvent event) {
        Transtition.switchPage(event, "Main.fxml");
    }
}
