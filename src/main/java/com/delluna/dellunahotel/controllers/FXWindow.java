package com.delluna.dellunahotel.controllers;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

// FXWindow.java
public class FXWindow extends javafx.application.Application {
  private String username;

  public FXWindow(String username) {
      this.username = username;
  }

  @Override
  public void start(Stage stage) {
      Label label = new Label("Welcome " + username);
      StackPane root = new StackPane(label);
      Scene scene = new Scene(root, 300, 200);
      stage.setScene(scene);
      stage.setTitle("JavaFX Window");
      stage.show();
  }
}
