package com.delluna.dellunahotel.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class UI {
      /**
     * Show a pop up box to alert the user
     * @param alertType
     * @param title Title of the pop up window
     * @param message Message of the pop up window
     */
    public static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Clear error message
     */
    public static void clearErrors(Label... fields) {
      for (Label l: fields) {
        l.setText("");
      }
    }
}
