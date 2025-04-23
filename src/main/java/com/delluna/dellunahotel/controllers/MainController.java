package com.delluna.dellunahotel.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import com.delluna.dellunahotel.utils.ApiClient;
import com.delluna.dellunahotel.utils.LoaderFX;
import com.delluna.dellunahotel.utils.ViewManager;
import javafx.scene.Node;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

enum Sidebar {
    HOME,
    EXPLORE,
    BOOKINGS,
    ACCOUNT
}

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

    private final Map<Sidebar, String> sidebarCache = new HashMap<>();

    
    public void initialize() {
        sidebarCache.put(Sidebar.HOME, "LandingPage.fxml");
        sidebarCache.put(Sidebar.EXPLORE, "SelectingRooms2.fxml");
        sidebarCache.put(Sidebar.ACCOUNT, "account.fxml");

    	ViewManager.setMainPane(mainPane);
    	
        // Load the sidebar logo
        Image image = LoaderFX.getImage("icon.png");
        appLogoContainer.setFill(new ImagePattern(image));
        
        selectedPane = homeStack;
        changeView("LandingPage.fxml", Sidebar.HOME); // Load home view by default
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
            changeView(sidebarCache.get(Sidebar.HOME), Sidebar.HOME);
        } else if (clickedStack == exploreStack) {
            changeView(sidebarCache.get(Sidebar.EXPLORE), Sidebar.EXPLORE);
        } else if (clickedStack == rewardsStack) {
            // changeView("rewards.fxml");
        } else if (clickedStack == bookingsStack) {
            // changeView("bookings.fxml");
        } else if (clickedStack == accountStack) {
            changeView(sidebarCache.get(Sidebar.ACCOUNT), Sidebar.ACCOUNT);
        } else if (clickedStack == logoutStack) {
            // changeView("logout.fxml");
        }
    }

    private final Map<String, Node> viewCache = new HashMap<>();
    private Sidebar curView = Sidebar.HOME; 

    public void resetCache(String fxmlFilename) {
        viewCache.put(fxmlFilename, null);
    }

    public void changeView(String FXMLfilename, Sidebar sidebar) {
        try {
            FXMLLoader node = new FXMLLoader(LoaderFX.getFXML(FXMLfilename));
            Node view = node.load();
            Node newView = getCachedView(FXMLfilename, view);

            if (curView == sidebar) {
                changeContentView(newView);
            } else {
                changeSidebarView(newView);
            }
            curView = sidebar;
            sidebarCache.put(sidebar, FXMLfilename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeView(String FXMLfilename, Sidebar sidebar, Node ref) {
        try {
            Node newView = getCachedView(FXMLfilename, ref);

            if (curView == sidebar) {
                changeContentView(newView);
            } else {
                changeSidebarView(newView);
            }
            curView = sidebar;
            sidebarCache.put(sidebar, FXMLfilename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void changeSidebarView(Node newView) {
            // If it's already visible, skip
            if (contentPane.getChildren().contains(newView)) {
                return;
            }
    
            Node oldView = contentPane.getChildren().isEmpty() ? null : contentPane.getChildren().get(0);
    
            newView.setTranslateX(-mainPane.getWidth()); // Start off-screen
            newView.setOpacity(0);
            
            Rectangle clip = new Rectangle();
            clip.widthProperty().bind(contentPane.widthProperty());
            clip.heightProperty().bind(contentPane.heightProperty());
            newView.setClip(clip);
    
            contentPane.getChildren().add(newView);
    
            TranslateTransition slideIn = new TranslateTransition(Duration.millis(600), newView);
            slideIn.setFromX(-mainPane.getWidth());
            slideIn.setToX(0);
    
            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), newView);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
    
            if (oldView != null) {
                TranslateTransition slideOut = new TranslateTransition(Duration.millis(550), oldView);
                slideOut.setFromX(0);
                slideOut.setToX(mainPane.getWidth() * 0.3);
    
                ParallelTransition transition = new ParallelTransition(slideOut, slideIn, fadeIn);
                transition.setOnFinished(e -> {
                    contentPane.getChildren().remove(oldView);
                    oldView.setTranslateX(0);
                    newView.setClip(null);
                });
                transition.play();
            } else {
                newView.setTranslateX(0);
                newView.setOpacity(1);
            }
    }

    private void changeContentView(Node newView) {
        // If it's already visible, skip
        if (contentPane.getChildren().contains(newView)) {
            return;
        }
    
        Node oldView = contentPane.getChildren().isEmpty() ? null : contentPane.getChildren().get(0);
    
        // Prepare the new view
        newView.setOpacity(0);
        contentPane.getChildren().add(newView);
    
        // Fade in for the new view
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), newView);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
    
        if (oldView != null) {
            // Fade out the old view
            FadeTransition fadeOut = new FadeTransition(Duration.millis(400), oldView);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(e -> {
                contentPane.getChildren().remove(oldView);
            });
    
            // Play both transitions together
            ParallelTransition transition = new ParallelTransition(fadeOut, fadeIn);
            transition.play();
        } else {
            // No old view, just fade in
            fadeIn.play();
        }
    }
    
    

private Node getCachedView(String fxmlFilename, Node def) throws IOException {
    if (viewCache.containsKey(fxmlFilename) && viewCache.get(fxmlFilename) != null) {
        return viewCache.get(fxmlFilename);
    } else {
        viewCache.put(fxmlFilename, def);
        return def;
    }
}

    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public Stage getStage() {
        return stage;
    }
}