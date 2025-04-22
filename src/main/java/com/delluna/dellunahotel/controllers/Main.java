package com.delluna.dellunahotel.controllers;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Load image
        Image image = new Image("file:/mnt/data/9d34a5e4-9d19-48cd-ab33-e500cdef134c.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(900);
        imageView.setPreserveRatio(true);

        // Title
        Label title = new Label("INTRODUCTION");
        title.setFont(Font.font("Arial", 20));
        title.setStyle("-fx-font-weight: bold;");
        title.setPadding(new Insets(20, 0, 10, 0));

        // Body Text
        String paragraph1 = "Hotel Del Luna Subang Jaya includes the original Del Luna residence, a beautifully preserved landmark now recognized "
                + "as a local heritage site. This elegant structure was built in the early 1940s and quickly became one of the most celebrated "
                + "establishments in Subang Jaya during its golden years. A beloved icon through decades of local growth and cultural transformation, "
                + "the hotel gained a reputation as the venue of choice for exquisite social gatherings, private events, and distinguished guest stays. "
                + "Today, Hotel Del Luna continues this legacy, offering guests a blend of timeless charm and modern comfort as a signature Classic Retreat.";

        String paragraph2 = "Hotel Del Luna Subang Jaya is now proudly part of the Autograph Collection, a portfolio of uniquely historic hotels where the "
                + "character of the past is beautifully intertwined with today’s refined luxuries and contemporary style.";

        Label body1 = new Label(paragraph1);
        body1.setWrapText(true);
        body1.setTextAlignment(TextAlignment.CENTER);
        body1.setFont(Font.font("Arial", 14));

        Label body2 = new Label(paragraph2);
        body2.setWrapText(true);
        body2.setTextAlignment(TextAlignment.CENTER);
        body2.setFont(Font.font("Arial", 14));
        body2.setPadding(new Insets(10, 0, 20, 0));

        // Layout
        VBox root = new VBox(10, imageView, title, body1, body2);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        // Scene
        Scene scene = new Scene(root, 950, 700);
        primaryStage.setTitle("Hotel Del Luna Subang Jaya");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
