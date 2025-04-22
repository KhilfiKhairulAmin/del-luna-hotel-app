package com.delluna.dellunahotel.utils;

import java.io.IOException;

import com.delluna.dellunahotel.controllers.MainController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Transtition {
      /**
     * Handle Sign Up link click event
     * @param event
     */
    public static void switchPage(ActionEvent event, String XFMLfilename) {
        try {
            Parent root = FXMLLoader.load(LoaderFX.getFXML(XFMLfilename));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1366, 720));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
