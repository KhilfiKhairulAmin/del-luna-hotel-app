package com.delluna.dellunahotel.controllers;

// Import necessary Swing and AWT components
import javax.swing.*;

import com.delluna.dellunahotel.utils.LoaderFX;

import javafx.application.Platform;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;
import java.util.ArrayList;

// Main class for the hotel booking service GUI
public class HotelBooking2 
{
    // GUI components and data structures
    private JFrame frame;                         // Main window frame
    private JPanel panel;                         // Panel for checkboxes
    private JLabel imageLabel, descriptionLabel;  // Labels for image and service description
    private JTextArea selectedServicesArea;       // Displays selected services
    private ArrayList<String> selectedServices;   // Stores list of selected services
    private JCheckBox[] checkBoxes;               // Stores checkboxes for services

    // Static list of services offered
    private static final String[] services = 
    {
        "Spa", "Couple Decorations", "Gymnasium", "Minibar", "Laundry", "Transportations"
    };

    // Corresponding descriptions for each service
    private static final String[] descriptions = 
    {
        "Relax with our full-body spa treatments. \nPrice: $50 per session.",
        "Romantic couple decorations for your room. \nPrice: $30 per setup.",
        "Access to our state-of-the-art gym. \nPrice: $10 per day.",
        "Enjoy a fully stocked minibar. \nPrice: $20 per night.",
        "Laundry service for your convenience. \nPrice: $15 per load.",
        "Luxury transport services. \nPrice: $40 per ride."
    };

    // Constructor to initialize the GUI
    public HotelBooking2(String roomNum) 
    {
        selectedServices = new ArrayList<>();               // Initialize selected services list
        checkBoxes = new JCheckBox[services.length];        // Allocate space for checkboxes

        // Create and configure the main window
        frame = new JFrame("Additional Services");
        frame.setSize(600, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create main panel and add to frame
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(203, 214, 248));
        mainPanel.setLayout(new BorderLayout());
        frame.add(mainPanel);

        // Panel that holds the service checkboxes
        panel = new JPanel();
        panel.setLayout(new GridLayout(2, 3));  // Arrange checkboxes in 2 rows, 3 columns
        panel.setBackground(new Color(203, 214, 248));

        // Loop to create and configure each service checkbox
        for (int i = 0; i < services.length; i++) 
        {
            JCheckBox checkBox = new JCheckBox(services[i]);       // Create checkbox
            checkBox.setFont(new Font("Times New Roman", Font.PLAIN, 18));
            checkBox.setBackground(new Color(213, 172, 209));
            checkBox.setPreferredSize(new Dimension(200, 50));
            checkBoxes[i] = checkBox;                               // Store for later reference

            int index = i;  // Needed to use inside inner class

            // Add listener for checkbox selection
            checkBox.addItemListener(new ItemListener() 
            {
                @Override
                public void itemStateChanged(ItemEvent e) 
                {
                    if (checkBox.isSelected()) 
                    {
                        addService(services[index]);                   // Add to selected list
                        displayServiceInfo(services[index], descriptions[index]); // Show info
                    } 
                    else 
                    {
                        removeService(services[index]);                // Remove from list
                    }
                }
            });
            panel.add(checkBox); // Add checkbox to panel
        }

        // Info panel to show selected service's image and description
        JPanel infoPanel = new JPanel(new BorderLayout());

        imageLabel = new JLabel("", SwingConstants.CENTER);          // Image display label
        imageLabel.setPreferredSize(new Dimension(600, 120));
        imageLabel.setBackground(new Color(203, 214, 248));
        imageLabel.setOpaque(true);

        descriptionLabel = new JLabel("Service details will appear here:", SwingConstants.CENTER);  // Description label
        descriptionLabel.setForeground(Color.BLACK);
        descriptionLabel.setFont(new Font("Arial", Font.BOLD, 12));
        descriptionLabel.setBackground(Color.WHITE);
        descriptionLabel.setOpaque(true);
        descriptionLabel.setPreferredSize(new Dimension(600, 60));

        infoPanel.add(imageLabel, BorderLayout.CENTER);             // Add image label to center
        infoPanel.add(descriptionLabel, BorderLayout.SOUTH);        // Add description below

        // Text area showing list of selected services
        selectedServicesArea = new JTextArea(4, 20);
        selectedServicesArea.setEditable(false);
        selectedServicesArea.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(selectedServicesArea); // Add scroll bar

        // Button panel for user actions
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(203, 214, 248));

        JButton updateButton = new JButton("Update");
        JButton nextButton = new JButton("Proceed Payment");
        JButton recommendButton = new JButton("Recommend Me!");

        Color buttonColor = new Color(213, 172, 209); // Soft purple

        updateButton.setBackground(buttonColor);
        nextButton.setBackground(buttonColor);
        recommendButton.setBackground(buttonColor);

        // Update button shows selected services
        updateButton.addActionListener(e -> 
        {
            updateSelectedServices();
            if (selectedServices.isEmpty()) 
            {
                JOptionPane.showMessageDialog(frame, "No services selected.");
            } 
            else 
            {
                StringBuilder message = new StringBuilder("You have selected the following services:\n");
                for (String service : selectedServices) {
                    message.append("- ").append(service).append("\n");
                }
                JOptionPane.showMessageDialog(frame, message.toString(), "Selected Services", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // "Next" button simulation
        nextButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Proceeding to Next Step...");
            // Swing Side
        String username = "user123";
        Platform.runLater(() -> {
            new FXWindow(username).start(new Stage());
        });

        });

        // Recommendation quiz
        recommendButton.addActionListener(e -> showServiceRecommendationQuiz());

        // Add buttons to panel
        buttonPanel.add(updateButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(recommendButton);

        // Add sub-panels to main panel
        mainPanel.add(panel, BorderLayout.NORTH);     // Top: checkboxes
        mainPanel.add(infoPanel, BorderLayout.CENTER);// Center: image + desc
        mainPanel.add(scrollPane, BorderLayout.SOUTH);// Bottom: selected text area
        mainPanel.add(buttonPanel, BorderLayout.PAGE_END); // Very bottom: buttons

        frame.setVisible(true); // Show window
    }

    // Add a service to the selected list if not already added
    private void addService(String service) 
    {
        if (!selectedServices.contains(service)) 
        {
            selectedServices.add(service);
            updateSelectedServices();
        }
    }

    // Remove a service from the selected list
    private void removeService(String service) 
    {
        selectedServices.remove(service);
        updateSelectedServices();
    }

    // Refresh the text area displaying selected services
    private void updateSelectedServices() 
    {
        selectedServicesArea.setText(""); // Clear text area
        for (String service : selectedServices) 
        {
            selectedServicesArea.append(service + "\n"); // Append each selected service
        }
    }

    // Displays service info and loads its image
    private void displayServiceInfo(String service, String description) 
    {
        String imagePath = "";

        // Map service to its corresponding image file path
        switch (service) 
        {
            case "Spa":
                imagePath = "spa.jpg";
                break;
            case "Couple Decorations":
                imagePath = "couple_decorations.jpg";
                break;
            case "Gymnasium":
                imagePath = "gym.jpg";
                break;
            case "Minibar":
                imagePath = "minibar.jpg";
                break;
            case "Laundry":
                imagePath = "laundry.jpg";
                break;
            case "Transportations":
                imagePath = "transportation.jpg";
                break;
            default:
                imagePath = "";
                break;
        }

        // Load and scale image if path is available
        if (!imagePath.isEmpty()) 
        {
            ImageIcon icon = new ImageIcon(getClass().getResource("/com/delluna/dellunahotel/images/services/" + imagePath));
            if (icon.getIconWidth() == -1) // -1 means image not found
            {
                imageLabel.setText("Image not found!");
                imageLabel.setIcon(null);
            }
            else 
            {
                Image img = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH); // Resize image
                imageLabel.setIcon(new ImageIcon(img));
                imageLabel.setText("");
            }
        } 
        else 
        {
            imageLabel.setText("No image available");
            imageLabel.setIcon(null);
        }

        descriptionLabel.setText(description); // Update description label
    }

    // Shows a quiz dialog and recommends services based on user answers
    private void showServiceRecommendationQuiz() 
    {
        ArrayList<String> recommended = new ArrayList<>();

        // Simple yes/no questions
        if (askYesNo("Do you want to relax?")) {
            recommended.add("Spa");
        }
        if (askYesNo("Are you here with a partner?")) {
            recommended.add("Couple Decorations");
        }
        if (askYesNo("Do you enjoy working out?")) {
            recommended.add("Gymnasium");
        }
        if (askYesNo("Do you prefer cold drinks and snacks?")) {
            recommended.add("Minibar");
        }
        if (askYesNo("Do you hate doing laundry while on a trip?")) {
            recommended.add("Laundry");
        }
        if (askYesNo("Do you plan to travel around town?")) {
            recommended.add("Transportations");
        }

        // Show recommendation result
        if (recommended.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No recommendations based on your answers.");
        } 
        else 
        {
            StringBuilder message = new StringBuilder("Based on your answers, we recommend:\n");
            for (String service : recommended) {
                message.append("- ").append(service).append("\n");
                // Automatically check recommended services
                for (int i = 0; i < services.length; i++) 
                {
                    if (services[i].equals(service)) {
                        checkBoxes[i].setSelected(true);
                    }
                }
            }
            JOptionPane.showMessageDialog(frame, message.toString());
        }
    }

    // Helper to ask yes/no questions using JOptionPane
    private boolean askYesNo(String question) 
    {
        int result = JOptionPane.showConfirmDialog(frame, question, "Service Quiz", JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }

    // Main method to start the program
    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(HotelBooking2::new); // Run GUI in event-dispatch thread
    }
}
