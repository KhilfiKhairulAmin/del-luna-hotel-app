package com.delluna.dellunahotel.utils;

import javafx.scene.control.Alert;

public class AlertBox {
    /**
     * Show a pop up box to alert the user
     * @param alertType
     * @param title Title of the pop up window
     * @param message Message of the pop up window
     */
    private static void showAlert(Alert.AlertType alertType, String title, String message) {
      Alert alert = new Alert(alertType);
      alert.setTitle(title);
      alert.setHeaderText(null);
      alert.setContentText(message);
      alert.showAndWait();
    }

    public static void error(String title, String message) {
      showAlert(Alert.AlertType.ERROR, title, message);
    }

    public static void information(String title, String message) {
      showAlert(Alert.AlertType.INFORMATION, title, message);
    }
}
