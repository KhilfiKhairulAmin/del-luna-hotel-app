package com.delluna.dellunahotel.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import com.delluna.dellunahotel.services.GuestService;
import com.delluna.dellunahotel.utils.LoaderFX;
import com.delluna.dellunahotel.utils.ViewManager;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

enum Sidebar {
    HOME,
    EXPLORE,
    BOOKINGS,
    ACCOUNT,
    GUIDES
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
    private StackPane bookingStack;

    @FXML private StackPane guideStack;
    
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
        sidebarCache.put(Sidebar.BOOKINGS, "BookingList.fxml");
        sidebarCache.put(Sidebar.GUIDES, null);
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
        } else if (clickedStack == bookingStack) {
            changeView(sidebarCache.get(Sidebar.BOOKINGS), Sidebar.BOOKINGS);
        } else if (clickedStack == guideStack) {
            curView = Sidebar.GUIDES;
            downloadFile(stage);
        } else if (clickedStack == accountStack) {
            changeView(sidebarCache.get(Sidebar.ACCOUNT), Sidebar.ACCOUNT);
        } else if (clickedStack == logoutStack) {
            logout();
        }
    }

    private void logout() {
        // Perform logout actions
        new GuestService().logout();

        try {
            // Load SignIn.fxml
            FXMLLoader loader = new FXMLLoader(LoaderFX.getFXML("SignIn.fxml"));
            Parent root = loader.load();

            // Create a new stage and show SignIn
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Sign In");
            stage.show();

            // Close the current window
            Stage currentStage = (Stage) mainPane.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void downloadFile(Stage stage) {
        changeView("BookingList.fxml", Sidebar.BOOKINGS);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF File");
        fileChooser.setInitialFileName("Rules and Regulations of Del Luna Hotel.pdf");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        
        File file = fileChooser.showSaveDialog(stage);
        
        if (file != null) {
            try (InputStream in = new FileInputStream("src/main/resources/com/delluna/dellunahotel/document/Rules & Regulations.pdf");
                 FileOutputStream out = new FileOutputStream(file)) {
                
                byte[] buffer = new byte[1024];
                int bytesRead;
                
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                
                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Download Complete");
                alert.setHeaderText(null);
                alert.setContentText("File saved successfully to:\n" + file.getAbsolutePath());
                alert.showAndWait();
                
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error downloading file: " + ex.getMessage());
                alert.showAndWait();
                ex.printStackTrace();
            }
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