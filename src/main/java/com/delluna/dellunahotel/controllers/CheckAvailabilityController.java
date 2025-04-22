package com.delluna.dellunahotel.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.fxml.Initializable; //implement Initializable
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.delluna.dellunahotel.models.Room;
import com.delluna.dellunahotel.models.RoomType;
import com.delluna.dellunahotel.services.BookingService;
import com.delluna.dellunahotel.services.BookingSingleton;
import com.delluna.dellunahotel.services.RoomService;
import com.delluna.dellunahotel.utils.AlertBox;
import com.delluna.dellunahotel.utils.LoaderFX;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Locale; //to get month names in correct locale
import java.time.DayOfWeek;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class CheckAvailabilityController implements Initializable{
	@FXML
	private ComboBox <String> monthComboBox;
	@FXML 
	private ComboBox <String> yearComboBox;
	@FXML
	private GridPane calendarPane;
	@FXML
	private Label startDateLabel;
	@FXML
	private Label endDateLabel;
	@FXML
	private Button checkButton;

	RoomType roomType;
	
	private LocalDate startDate;
	private LocalDate endDate;
	private Button startDateButton;
	private Button endDateButton;
	private Map <LocalDate, Button> dateButtonMap = new HashMap <>(); //store date to button mapping 
	
	Year currentYear = Year.now();
	String currentYearName = currentYear.toString();
	String nextYearName = currentYear.plusYears(1).toString();

	private BookingSingleton bs = BookingSingleton.getInstance();
	
	private ObservableList <String> monthList = FXCollections.observableArrayList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
	private ObservableList <String> yearList = FXCollections.observableArrayList(currentYearName, nextYearName);

	@Override
	public void initialize (URL url, ResourceBundle resourceBundle)
	{
		monthComboBox.setItems(monthList);
		yearComboBox.setItems(yearList);
		
		//get the current month using java.time
		Month currentMonth = LocalDate.now().getMonth();
		String currentMonthName = currentMonth.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault()); //getDefault() will display the month name in the language and format of user's system
		
		monthComboBox.setValue(currentMonthName);
		yearComboBox.setValue(currentYearName);
		
		monthComboBox.setOnAction(event->drawCalendar());
		yearComboBox.setOnAction(event->drawCalendar());
		
		//load CSS stylesheet
		try {

	        // Get the scene to apply the stylesheet.
	        Scene scene = calendarPane.getScene();
	        if (scene != null) {
	        	scene.getStylesheets().clear(); //clear any existing stylesheets
	          scene.getStylesheets().add(LoaderFX.getCSS("buttonStyle.css"));
	        } else {
	        	calendarPane.getParent().getStylesheets().clear();
	          calendarPane.getParent().getStylesheets().add(LoaderFX.getCSS("buttonStyle.css"));
	        }
	    } catch (Exception e) {
	        System.err.println("Error loading CSS: " + e.getMessage());
	        e.printStackTrace(); //  Print the stack trace to see the full error details
	    }
		
		drawCalendar(); //Initial calendar draw
	}

	public void setData(RoomType roomType) {
		this.roomType = roomType;
	}

	@FXML private void handleCheck() {
		String startDate = startDateLabel.getText();
		String endDate = endDateLabel.getText();

		BookingService bookingService = new BookingService();
		List<Room> availRooms = bookingService.getAvailableRoomsByTypeAndDate(roomType.typeId, startDate, endDate);
		String out = "";
		for (Room r: availRooms) {
			out += r + "\n";
		}

		bs.setDate(startDate, endDate);  // Save the date
		AlertBox.information("Available Rooms", out);


		FXMLLoader loader = new FXMLLoader(LoaderFX.getFXML("SelectRoomss.fxml"));

		try {
			Node editView = loader.load();
		
			SelectRoomsController editController = loader.getController();
			
			for (Room r: availRooms) {
				editController.addRoomCard(r.roomNum, r.floorLevel, r.roomTypeId);
			}
	
			MainController.getInstance().changeView("SelectRoomss.fxml", Sidebar.EXPLORE, editView);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void drawCalendar()
	{
		calendarPane.getChildren().clear(); //clear previous calendar
		calendarPane.getRowConstraints().clear(); //clear existing row constraints
		calendarPane.getColumnConstraints().clear();
		dateButtonMap.clear(); //clear when redrawing
		
		String selectedMonth = monthComboBox.getValue();
		int selectedYear = Integer.parseInt(yearComboBox.getValue());
		Month month = Month.valueOf(selectedMonth.toUpperCase());
		LocalDate firstDayOfMonth = LocalDate.of(selectedYear, month, 1);
		DayOfWeek firstDayOfWeek = firstDayOfMonth.getDayOfWeek();
		int daysInMonth = YearMonth.of(selectedYear, month).lengthOfMonth();
		
		int column = firstDayOfWeek.getValue() - 1; //0-based index
		int row = 0;
		
		//Set row constraints for equal heights
		for (int i = 0; i < 7; i++)
		{
			javafx.scene.layout.RowConstraints rowConstraints = new javafx.scene.layout.RowConstraints();
			rowConstraints.setPercentHeight (100.0 / 7.0); //distribute height
			rowConstraints.setVgrow(javafx.scene.layout.Priority.ALWAYS); //Allow vertical growth
			calendarPane.getRowConstraints().add(rowConstraints);
		}
		
		//Row constraints for the header
		javafx.scene.layout.RowConstraints headerRowConstraints = new javafx.scene.layout.RowConstraints();
		headerRowConstraints.setPercentHeight(100.0 / 8.0);
		calendarPane.getRowConstraints().add(0, headerRowConstraints);
		
		//Add day headers
		String[] day = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
		
		for (int i = 0; i < 7; i++)
		{
			Label dayLabel = new Label(day[i]);
			dayLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
			GridPane.setColumnIndex(dayLabel, i);
			GridPane.setRowIndex(dayLabel, row);
			calendarPane.getChildren().add(dayLabel);
		}
		row++;
		
		for (int d = 1; d <= daysInMonth; d++)
		{
			Button dayButton = new Button (String.valueOf(d));
			LocalDate currentDate = LocalDate.of(selectedYear, month, d);
			dayButton.getStyleClass().addAll("date-button");
			dayButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
			GridPane.setColumnIndex(dayButton, column);
			GridPane.setRowIndex(dayButton, row);
			calendarPane.getChildren().add(dayButton);
			
			//Make buttons fill grid cell
			dayButton.setMaxWidth(Double.MAX_VALUE);
			dayButton.setMaxHeight(Double.MAX_VALUE);
			dateButtonMap.put(currentDate, dayButton);
			
			dayButton.setOnAction(e->handleDateSelection(currentDate));
			
			if (startDate != null && currentDate.isEqual(startDate)) {
                dayButton.getStyleClass().add("selected");
                dayButton.applyCss();
                startDateButton = dayButton;
            }
            if (endDate != null && currentDate.isEqual(endDate)) {
                dayButton.getStyleClass().add("selected");
                dayButton.applyCss();
                endDateButton = dayButton;
            }
            if (startDate != null && endDate != null && currentDate.isAfter(startDate) && currentDate.isBefore(endDate)) {
                dayButton.getStyleClass().add("in-range");
                dayButton.applyCss();
            }
			
			column++;
			if (column > 6)
			{
				column = 0;
				row++;
			}
		}
	}

	@FXML private void handleBack() {
		MainController.getInstance().resetCache("checkingAvailability.fxml");
		MainController.getInstance().changeView("SelectingRooms2.fxml", Sidebar.EXPLORE);
		bs.resetBooking();
	}
	
	private void handleDateSelection(LocalDate currentDate)
	{
		Button clickedButton = dateButtonMap.get(currentDate); //get button directly

		if (startDate == null)
		{
			startDate = currentDate;
			startDateLabel.setText(currentDate.format(DateTimeFormatter.ISO_DATE));
			if (clickedButton != null)
			{
				clickedButton.getStyleClass().add("selected");
				clickedButton.applyCss(); //force style update
				startDateButton = clickedButton;
				endDate = null;
				endDateLabel.setText("");
				drawCalendar(); //redraw to clear previous range
			}
		}
		
		else if (endDate == null)
		{
			if (currentDate.isAfter(startDate) || currentDate.isEqual(startDate))
			{
				endDate = currentDate;
				endDateLabel.setText(currentDate.format(DateTimeFormatter.ISO_DATE));
				
				if (clickedButton != null)
				{
					clickedButton.getStyleClass().add("selected");
					clickedButton.applyCss();
					endDateButton = clickedButton;
			    }
				highlightRange(); //a new method to highlight range
			}
			
			else
			{
				//if the second click is before the start date, reset
				if (startDateButton != null)
				{
					startDateButton.getStyleClass().remove("selected");
				}
				startDate = currentDate;
				endDate = null;
				startDateLabel.setText(currentDate.format(DateTimeFormatter.ISO_DATE));
				endDateLabel.setText("");
				
				if (clickedButton != null)
				{
					clickedButton.getStyleClass().add("selected");
					clickedButton.applyCss();
					startDateButton = clickedButton;
					endDateButton = null;
					//clear previous highlighting
					drawCalendar();
				}
			}
		}
		
		else
		{
			//if both start and end dates are selected, reset
			if (startDateButton != null)
			{
				startDateButton.getStyleClass().remove("selected");
			}
			if (endDateButton != null)
			{
				endDateButton.getStyleClass().remove("selected");
			}
			startDate = currentDate;
			endDate = null;
			startDateLabel.setText(currentDate.format(DateTimeFormatter.ISO_DATE));
			endDateLabel.setText("");
			
			if (clickedButton != null)
			{
				clickedButton.getStyleClass().add("selected");
				clickedButton.applyCss();
				startDateButton = clickedButton;
				endDateButton = null;
				//clear previous highlighting
				drawCalendar();
			}
		}
	}
	
	private void highlightRange()
	{
		if (startDate != null && endDate != null)
		{
			LocalDate currentDate = startDate;
			
			while (!currentDate.isAfter(endDate))
			{
				Button button = dateButtonMap.get(currentDate);
				
				if (button != null && !currentDate.isEqual(startDate) && !currentDate.isEqual(endDate))
				{
					button.getStyleClass().add("in-range");
					button.applyCss();
				}
				currentDate = currentDate.plusDays(1);
			}
			//ensure start and end dates button also have "selected" class
			if (startDateButton != null)
			{
				startDateButton.getStyleClass().add("selected");
				startDateButton.applyCss();
			}
			
			if (endDateButton != null)
			{
				endDateButton.getStyleClass().add("selected");
				endDateButton.applyCss();
			}
		}
	}

}
