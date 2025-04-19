package com.delluna.dellunahotel.utils;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public class ViewManager {
    private static BorderPane mainPane;
    
    public static void setMainPane(BorderPane pane) {
        mainPane = pane;
    }
    
    public static void switchView(Node node) {
        mainPane.setCenter(node);
    }
}