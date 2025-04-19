package com.delluna.dellunahotel.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import com.delluna.dellunahotel.utils.LoaderFX;
import com.delluna.dellunahotel.utils.ViewManager;
import javafx.scene.Node;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

public class MainController {
    private Stage stage;
    
    @FXML
    private BorderPane mainPane;
    
    @FXML
    private Circle appLogoContainer;
    
    @FXML
    private VBox stackPaneContainer;
    
    @FXML
    private StackPane homeStack;
    
    @FXML
    private StackPane exploreStack;
    
    @FXML
    private StackPane rewardsStack;

    @FXML
    private StackPane bookingsStack;
    
    @FXML
    private StackPane accountStack;

    @FXML
    private StackPane logoutStack;

    @FXML
    private StackPane contentPane; // This will hold our dynamic content
    
    private StackPane selectedPane;
    
    private static MainController instance;
    
    public MainController() {
        instance = this;
    }
    
    public static MainController getInstance() {
        return instance;
    }
    
    public void setContent(Node node) {
        contentPane.getChildren().setAll(node);
    }
    
    public void initialize() {
    	ViewManager.setMainPane(mainPane);
    	
        // Load the sidebar logo
        Image image = LoaderFX.getImage("icon.jpg");
        appLogoContainer.setFill(new ImagePattern(image));
        
        selectedPane = homeStack;
        loadView("home.fxml"); // Load home view by default
        selectedPane.setStyle("-fx-background-color: #d5acd1;");
        
        int count = 0, except1 = 1, except2 = 6;
        for (var node : stackPaneContainer.getChildren()) {
            count++;
            if (count == except1 || count == except2) {
                continue;
            }
            if (node instanceof StackPane stackPane) {
                stackPane.setOnMouseClicked(this::handleStackClick);
            }
        }
    }
    
    private void handleStackClick(MouseEvent event) {
        StackPane clickedPane = (StackPane) event.getSource();
        
        if (selectedPane == clickedPane) {
            return;
        }
        if (selectedPane != null) {
            selectedPane.setStyle(""); // Reset previous
        }

        clickedPane.setStyle("-fx-background-color: #d5acd1;"); // Highlight selected
        selectedPane = clickedPane;
        
        handleSidebarClick(event);
    }
    
    @FXML
    private void handleSidebarClick(MouseEvent event) {
        StackPane clickedStack = (StackPane) event.getSource();
        if (clickedStack == homeStack) {
            loadView("home.fxml");
        } else if (clickedStack == exploreStack) {
            loadView("SelectingRooms2.fxml");
        } else if (clickedStack == rewardsStack) {
            loadView("rewards.fxml");
        } else if (clickedStack == bookingsStack) {
            loadView("bookings.fxml");
        } else if (clickedStack == accountStack) {
            loadView("account.fxml");
        } else if (clickedStack == logoutStack) {
            loadView("logout.fxml");
        }
    }
    
    private Map<String, Node> viewCache = new HashMap<>();

    private void loadView(String fxmlFilename) {
        try {
            Node view;
            if (viewCache.containsKey(fxmlFilename)) {
                view = viewCache.get(fxmlFilename);
            } else {
                FXMLLoader loader = new FXMLLoader(LoaderFX.getFXML(fxmlFilename));
                view = loader.load();
                viewCache.put(fxmlFilename, view);
            }
            
            contentPane.getChildren().clear();
            contentPane.getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error
        }
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public Stage getStage() {
        return stage;
    }
}