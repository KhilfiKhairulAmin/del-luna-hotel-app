package com.delluna.dellunahotel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.delluna.dellunahotel.utils.Logger;
import com.delluna.dellunahotel.models.Guest;
import com.delluna.dellunahotel.services.GuestService;
import com.delluna.dellunahotel.utils.ApiClient;
import com.delluna.dellunahotel.utils.LoaderFX;

import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;

import java.io.IOException;

public class App extends Application {
    // TODO Complete Integration Tinte
    // TODO Complete Payments
    // TODO Complete Wallet
    // TODO Complete Settings
    // TODO Complete Account
    // TODO Complete Homepage
    // TODO Add complementary feature
    // TODO Complete feature search 
    // TODO Complete feature music
    // TODO Perfectionist
	
    @Override
    public void start(Stage primaryStage) throws Exception {

        String initialPage;
        try {
            new GuestService().getCurrentGuest();
            initialPage = "Main.fxml";
        } catch (RuntimeException e) {
            initialPage = "SignIn.fxml";
        }

    	// Initialize window
    	FXMLLoader loader = new FXMLLoader(LoaderFX.getFXML(initialPage));
    	Parent root = loader.load();
    	Scene scene = new Scene(root);
        Logger logger = Logger.getInstance();
        
        // Configure logger
        try {
            logger.addOutput(new Logger.FileOutput("src/main/resources/com/delluna/dellunahotel/logs/application.log"));
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

