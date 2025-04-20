package com.delluna.dellunahotel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.delluna.dellunahotel.utils.Logger;
import com.delluna.dellunahotel.utils.ApiClient;
import com.delluna.dellunahotel.utils.LoaderFX;

import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;

import java.io.IOException;

public class App extends Application {

    // We only have tonight (5 hours), saturday (10 hours)
    // sunday (10 hours), monday (4 hours), tuesday (4 hours), wednesday (6 hours)

    // TONIGHT: Target 6 hours (ETA 6am)
    // TODO Room Listings (Data + Looping)
    // TODO Change Sidebar to reflect current changes
	
    @Override
    public void start(Stage primaryStage) throws Exception {

        String page = "SignIn.fxml";
        String token = ApiClient.getAuthToken(); // Your method to read from auth_token.txt
        if (token != null && !token.isEmpty()) {
            page = "Main.fxml";
        }

    	// Initialize window
    	FXMLLoader loader = new FXMLLoader(LoaderFX.getFXML(page));
    	Parent root = loader.load();
    	Scene scene = new Scene(root);
        Logger logger = Logger.getInstance();
        
        // Configure logger
        try {
            logger.addOutput(new Logger.FileOutput("logs/application.log"));
        } catch (IOException e) {
            logger.error("Failed to create file logger", e);
        }
        
        // Importing stylesheets
        scene.getStylesheets().add(LoaderFX.getCSS("styles.css"));
        
        // Get window resolution (excluding task bar)
        Screen screen = Screen.getPrimary();
        Rectangle2D visualBounds = screen.getVisualBounds();
        
        // Configure windows resolution
        primaryStage.setWidth(visualBounds.getMaxX());
        primaryStage.setHeight(visualBounds.getMaxY());
        logger.info("Resolution: " + (int)visualBounds.getMaxX() + " x " + (int)visualBounds.getMaxY() + " px");
        
        // Set window title
        primaryStage.setResizable(true);
        primaryStage.setTitle("LunaWallet Dashboard");
        
        // Set window to visible
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

