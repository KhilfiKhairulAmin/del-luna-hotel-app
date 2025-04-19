package com.delluna.dellunahotel.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import com.delluna.dellunahotel.utils.LoaderFX;

public class RoomSelectionController {
	@FXML
	private ImageView singleImage;
	@FXML
	private ImageView doubleImage;
	@FXML
	private ImageView tripleImage;
	@FXML
	private ImageView quadImage;
	@FXML
	private ImageView executiveImage;
	@FXML
	private ImageView suiteImage;
	@FXML
	private ImageView familyImage;
	@FXML
	private ImageView honeymoonImage;
	
	@FXML
	private Label singleLabel;
	@FXML
	private Label doubleLabel;
	@FXML
	private Label tripleLabel;
	@FXML
	private Label quadLabel;
	@FXML
	private Label executiveLabel;
	@FXML
	private Label suiteLabel;
	@FXML
	private Label familyLabel;
	@FXML
	private Label honeymoonLabel;

    @FXML
    private void singleRoomDescription(MouseEvent event) 
    {
    	showDescription("singleDescription.fxml", "Single Room");
    }
    @FXML
    private void doubleRoomDescription(MouseEvent event) 
    {
    	showDescription("doubleDescription.fxml", "Double Room");
    }
    @FXML
    private void tripleRoomDescription(MouseEvent event) 
    {
    	showDescription("tripleDescription.fxml", "Triple Room");
    }
    @FXML
    private void quadRoomDescription(MouseEvent event) 
    {
    	showDescription("quadDescription.fxml", "Quad Room");
    }
    @FXML
    private void executiveRoomDescription(MouseEvent event) 
    {
    	showDescription("executiveDescription.fxml", "Executive Room");
    }
    @FXML
    private void suiteDescription(MouseEvent event) 
    {
    	showDescription("suiteDescription.fxml", "Suite");
    }
    @FXML
    private void familyRoomDescription(MouseEvent event) 
    {
    	showDescription("familyDescription.fxml", "Family Room");
    }
    @FXML
    private void honeymoonSuiteDescription(MouseEvent event) 
    {
    	showDescription("honeymoonDescription.fxml", "Honeymoon Suite");
    }
    
    private void showDescription (String file, String title)
    {
    	try
    	{
    		FXMLLoader desc = new FXMLLoader (LoaderFX.getFXML(file));
        	Parent root = desc.load();
        	Stage stage = new Stage();
        	stage.setTitle(title);
        	stage.setScene(new Scene(root));
        	stage.show();
        	
    	}catch (IOException e)
    	{
    		e.printStackTrace();
    	}
    }

}
