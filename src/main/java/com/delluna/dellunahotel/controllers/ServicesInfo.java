package com.delluna.dellunahotel.controllers;

import javax.swing.*; // for JFrame, JPanel, JButton, JLabel

import com.delluna.dellunahotel.services.BookingSingleton;

import java.awt.*; // for layouts, colours, fonts

public class ServicesInfo 
{
    
    public ServicesInfo()
    {
    	// create main application window (frame)
        JFrame frame = new JFrame("Services Provided:");
        frame.setSize(750, 700); 								// set window size
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // close the program when window is closed
        frame.setLocationRelativeTo(null);						// centre the window

        // main panel with BorderLayout (North, Center, South parts)
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(203, 214, 248)); // light purple background

        // title label at the top
        JLabel titleLabel = new JLabel("Service Descriptions:", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.PLAIN, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));  // padding around the title
        titleLabel.setBackground(new Color(213, 172, 209)); 					// pink background
        titleLabel.setOpaque(true);												// needed to make background color visible
        mainPanel.add(titleLabel, BorderLayout.NORTH);							// add title at the top (North)

        // panel for listing all the service cards
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(3, 2, 20, 20)); 	// 3 rows, 2 columns, gaps
        contentPanel.setBackground(new Color(203, 214, 248)); 	// same light purple background
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // outer margin

        String[] serviceNames = {
                "Spa", "Couple Decoration", "Gym", "Minibar", "Laundry", "Transportation"
        };

        String[] serviceDescriptions = {
                "• Duration Options:\n 30, 60, or 90-minute sessions\n\n"
                        + "• Expert Therapists:\n Certified professionals for optimal care\n\n"
                        + "• Ambience:\n Calming music, soothing scents, dim lighting ",

                "• Customizable Themes:\n Beach, Floral, Romantic, Rustic\n\n"
                        + "• Special Occasions:\n Ideal for anniversaries, birthdays, engagements\n\n"
                        + "• Personal Touches:\n Add personalized messages, photos ",

                "• State-of-the-art Equipment:\n Treadmills, Weights, Yoga Mats\n\n"
                        + "• Group Classes:\n Yoga, Pilates, Spinning, Zumba\n\n"
                        + "• Trained Coaches:\n Personalized workout guidance ",

                "• Premium Selection:\n Artisanal snacks, gourmet chocolates\n\n"
                        + "• Local Flavors:\n Discover regional snacks and beverages\n\n"
                        + "• Dietary Options:\n Vegan, gluten-free, and healthy snacks ",

                "• Eco-friendly Detergents:\n Hypoallergenic and organic choices\n\n"
                        + "• Ironing & Folding:\n We take care of everything\n\n"
                        + "• Dry Cleaning:\n Suits, delicate fabrics, and more",

                "• Luxury Rides:\n Sedans, SUVs, Limousines available\n\n"
                        + "• City Tours:\n Popular attractions included in local tours\n\n"
                        + "• Airport Assistance:\n Pick-up, drop-off, and expedited check-ins\n",
        };

        // define fonts for name and description
        Font nameFont = new Font("Times New Roman", Font.BOLD, 16);
        Font descFont = new Font("Times New Roman", Font.PLAIN, 13);

        // loop through each service to create its "card"
        for (int i = 0; i < serviceNames.length; i++) 
        {
            JPanel serviceCard = new JPanel();
            serviceCard.setLayout(new BoxLayout(serviceCard, BoxLayout.Y_AXIS)); // vertical layout
            serviceCard.setBackground(new Color(213, 172, 209)); 				 // pink background
            serviceCard.setBorder(BorderFactory.createLineBorder(new Color(110, 98, 170), 1)); // border color

            // service name label
            JLabel nameLabel = new JLabel(serviceNames[i], SwingConstants.CENTER);
            nameLabel.setFont(nameFont);
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            nameLabel.setForeground(new Color(85, 85, 85)); // dark text color

            // Service description in a text area (multiline)
            JTextArea descLabel = new JTextArea(serviceDescriptions[i]);
            descLabel.setFont(descFont);
            descLabel.setOpaque(false); // Transparent background
            descLabel.setEditable(false); // User cannot edit
            descLabel.setFocusable(false); // User cannot select
            descLabel.setForeground(new Color(60, 60, 60));
            descLabel.setLineWrap(true); // Wrap long lines
            descLabel.setWrapStyleWord(true); // Wrap by word

            // Put text area into scroll pane (but hide horizontal scroll)
            JScrollPane scrollPane = new JScrollPane(descLabel);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());    // No border
            scrollPane.setPreferredSize(new Dimension(300, 120)); 		// size of the scrollable area
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

            // add name and description to the service card
            serviceCard.add(nameLabel);
            serviceCard.add(Box.createVerticalStrut(10));  // small space between name and description
            serviceCard.add(scrollPane);

            // add the service card into the content panel
            contentPanel.add(serviceCard);
        }

        // add all service cards to the center of main panel
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Panel at the bottom for "Next" button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        // Create "Next" button
        JButton nextBtn = new JButton("Next");
        nextBtn.setBackground(new Color(213, 172, 209)); // Same pink color
        nextBtn.setFocusPainted(false); 				 // No ugly border when clicked

        buttonPanel.add(nextBtn);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the complete main panel to frame
        frame.add(mainPanel);
        frame.setVisible(true); // Show everything

        // When "Next" is clicked, show the booking page
        nextBtn.addActionListener(e -> showBookingPage(frame));
    }

    // This function shows the second page (after Next button is clicked)
    public static void showBookingPage(JFrame frame) 
    {
        JPanel bookingPanel = new JPanel();
        bookingPanel.setLayout(new BoxLayout(bookingPanel, BoxLayout.Y_AXIS)); // Vertical layout
        bookingPanel.setBackground(new Color(203, 214, 248)); // Light purple again
        bookingPanel.setBorder(BorderFactory.createEmptyBorder(100, 30, 100, 30)); // Padding

        // Label saying preview is complete
        JLabel doneLabel = new JLabel("All services previewed!", SwingConstants.CENTER);
        doneLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        doneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        doneLabel.setForeground(new Color(85, 85, 85));

        // Button to go to booking (HotelBooking2)
        JButton bookBtn = new JButton("Let's book your services!");
        bookBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bookBtn.setBackground(new Color(213, 172, 209));
        bookBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookBtn.setFocusPainted(false);
        bookBtn.setMaximumSize(new Dimension(250, 40)); // Width x Height

        // Button to skip booking
        JButton skipBtn = new JButton("Skip");
        skipBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        skipBtn.setBackground(Color.WHITE);
        skipBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        skipBtn.setMaximumSize(new Dimension(250, 35));

        // Add the components into the booking panel
        bookingPanel.add(doneLabel);
        bookingPanel.add(Box.createVerticalStrut(30)); // Space between label and button
        bookingPanel.add(bookBtn);
        bookingPanel.add(Box.createVerticalStrut(15)); // Space between buttons
        bookingPanel.add(skipBtn);

        // Remove the old content and show the new booking panel
        frame.getContentPane().removeAll();
        frame.add(bookingPanel);
        frame.revalidate();
        frame.repaint();

        // When "Let's book your services!" is clicked
        bookBtn.addActionListener(e -> 
        {
            frame.dispose(); // Close the current frame
            SwingUtilities.invokeLater(() -> new HotelBooking2()); // Open the HotelBooking2 window
        });

        // When "Skip" is clicked
        skipBtn.addActionListener(e -> {
            BookingSingleton bs = BookingSingleton.getInstance();
            String[] emptyArray = new String[0]; // Empty array for no services booked
            bs.setServiceIds(emptyArray);
            frame.dispose();
        });
    }
}







// PAGE ADA EXPLANATION TERUS 6 IN 1 PAGE

/*package ProjectPartTinte;
import javax.swing.*;
import java.awt.*;

public class ServicesInfo {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Services Provided:");
        frame.setSize(750, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(203, 214, 248)); // Light purple background

        // Title
        JLabel titleLabel = new JLabel("Service Descriptions:", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.PLAIN, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        titleLabel.setBackground(new Color(213, 172, 209)); // Pink background
        titleLabel.setOpaque(true);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Main content panel to hold all services
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(3, 2, 20, 20)); // 3 rows, 2 columns
        contentPanel.setBackground(new Color(203, 214, 248));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        String[] serviceNames = {
                "Spa", "Couple Decoration", "Gym", "Minibar", "Laundry", "Transportation"
        };

        String[] serviceDescriptions = {
                "• Duration Options:\n 30, 60, or 90-minute sessions\n\n"
                        + "• Expert Therapists:\n Certified professionals for optimal care\n\n"
                        + "• Ambience:\n Calming music, soothing scents, dim lighting ",

                "• Customizable Themes:\n Beach, Floral, Romantic, Rustic\n\n"
                        + "• Special Occasions:\n Ideal for anniversaries, birthdays, engagements\n\n"
                        + "• Personal Touches:\n Add personalized messages, photos ",

                "• State-of-the-art Equipment:\n Treadmills, Weights, Yoga Mats\n\n"
                        + "• Group Classes:\n Yoga, Pilates, Spinning, Zumba\n\n"
                        + "• Trained Coaches:\n Personalized workout guidance ",

                "• Premium Selection:\n Artisanal snacks, gourmet chocolates\n\n"
                        + "• Local Flavors:\n Discover regional snacks and beverages\n\n"
                        + "• Dietary Options:\n Vegan, gluten-free, and healthy snacks ",

                "• Eco-friendly Detergents:\n Hypoallergenic and organic choices\n\n"
                        + "• Ironing & Folding:\n We take care of everything\n\n"
                        + "• Dry Cleaning:\n Suits, delicate fabrics, and more",

                "• Luxury Rides:\n Sedans, SUVs, Limousines available\n\n"
                        + "• City Tours:\n Popular attractions included in local tours\n\n"
                        + "• Airport Assistance:\n Pick-up, drop-off, and expedited check-ins\n",
        };

        Font nameFont = new Font("Times New Roman", Font.BOLD, 16);
        Font descFont = new Font("Times New Roman", Font.PLAIN, 13);

        for (int i = 0; i < serviceNames.length; i++) {
            JPanel serviceCard = new JPanel();
            serviceCard.setLayout(new BoxLayout(serviceCard, BoxLayout.Y_AXIS));
            serviceCard.setBackground(new Color(213, 172, 209));
            serviceCard.setBorder(BorderFactory.createLineBorder(new Color(110, 98, 170), 1));

            JLabel nameLabel = new JLabel(serviceNames[i], SwingConstants.CENTER);
            nameLabel.setFont(nameFont);
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            nameLabel.setForeground(new Color(85, 85, 85));

            JTextArea descLabel = new JTextArea(serviceDescriptions[i]);
            descLabel.setFont(descFont);
            descLabel.setOpaque(false);
            descLabel.setEditable(false);
            descLabel.setFocusable(false);
            descLabel.setForeground(new Color(60, 60, 60));
            descLabel.setLineWrap(true);
            descLabel.setWrapStyleWord(true);

            JScrollPane scrollPane = new JScrollPane(descLabel);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setPreferredSize(new Dimension(300, 120));
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

            serviceCard.add(nameLabel);
            serviceCard.add(Box.createVerticalStrut(10));
            serviceCard.add(scrollPane);
            contentPanel.add(serviceCard);
        }

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Next Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        JButton nextBtn = new JButton("Next");
        nextBtn.setBackground(new Color(213, 172, 209)); // Pink background
        nextBtn.setFocusPainted(false);

        buttonPanel.add(nextBtn);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);

        nextBtn.addActionListener(e -> showBookingPage(frame));
    }

    public static void showBookingPage(JFrame frame) {
        JPanel bookingPanel = new JPanel();
        bookingPanel.setLayout(new BoxLayout(bookingPanel, BoxLayout.Y_AXIS));
        bookingPanel.setBackground(new Color(203, 214, 248));
        bookingPanel.setBorder(BorderFactory.createEmptyBorder(100, 30, 100, 30));

        JLabel doneLabel = new JLabel("All services previewed!", SwingConstants.CENTER);
        doneLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        doneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        doneLabel.setForeground(new Color(85, 85, 85));

        JButton bookBtn = new JButton("Let's book your services!");
        bookBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bookBtn.setBackground(new Color(213, 172, 209));
        bookBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookBtn.setFocusPainted(false);
        bookBtn.setMaximumSize(new Dimension(250, 40));
        bookBtn.addActionListener(e -> JOptionPane.showMessageDialog(null, "Redirecting to booking..."));

        JButton skipBtn = new JButton("Skip");
        skipBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        skipBtn.setBackground(Color.WHITE);
        skipBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        skipBtn.setMaximumSize(new Dimension(250, 35));
        skipBtn.addActionListener(e -> JOptionPane.showMessageDialog(null, "Skipped service booking."));

        bookingPanel.add(doneLabel);
        bookingPanel.add(Box.createVerticalStrut(30));
        bookingPanel.add(bookBtn);
        bookingPanel.add(Box.createVerticalStrut(15));
        bookingPanel.add(skipBtn);

        frame.getContentPane().removeAll();
        frame.add(bookingPanel);
        frame.revalidate();
        frame.repaint();
    }
}*/




// PAGE SWITCHING CARDS
/* package ProjectPartTinte;

import javax.swing.*;
import java.awt.*;

//(213, 172, 209));//pink
//203, 214, 248)); // light purple
//new Color(110, 98, 170) dark purple

public class ServicesInfo 
{
    public static void main(String[] args) {
        JFrame frame = new JFrame("Services Provided:");
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(203, 214, 248)); // Light purple background for the main panel

        // Title
        JLabel titleLabel = new JLabel("Service Descriptions:", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.PLAIN, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        titleLabel.setBackground(new Color(213, 172, 209)); // Pink background for title label
        titleLabel.setOpaque(true);  // Ensure the background color is visible
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Card layout to switch between services
        JPanel cardPanel = new JPanel(new CardLayout());
        cardPanel.setBackground(new Color(203, 214, 248)); // Light purple background for the card panel

        // Sample services
        String[] serviceNames = {
                "Spa", "Couple Decoration", "Gym", "Minibar", "Laundry", "Transportation"
        };

        String[] serviceDescriptions = {
                "• Duration Options:\n 30, 60, or 90-minute sessions\n\n"
                + "• Expert Therapists:\n Certified professionals for optimal care\n\n"
                + "• Ambience:\n Calming music, soothing scents, dim lighting ",
                
                "• Customizable Themes:\n Beach, Floral, Romantic, Rustic\n\n" 
                + "• Special Occasions:\n Ideal for anniversaries, birthdays, engagements\n\n" 
                + "• Personal Touches:\n Add personalized messages, photos ",
                
                "• State-of-the-art Equipment:\n Treadmills, Weights, Yoga Mats\n\n" +
                "• Group Classes:\n Yoga, Pilates, Spinning, Zumba\n\n" +
                "• Trained Coaches:\n Personalized workout guidance ",
                
                "• Premium Selection:\n Artisanal snacks, gourmet chocolates\n\n" +
                "• Local Flavors:\n Discover regional snacks and beverages\n\n" +
                "• Dietary Options:\n Vegan, gluten-free, and healthy snacks ",
                
                "• Eco-friendly Detergents:\n Hypoallergenic and organic choices\n\n" +
                "• Ironing & Folding:\n We take care of everything\n\n" +
                "• Dry Cleaning:\n Suits, delicate fabrics, and more",

                "• Luxury Rides:\n Sedans, SUVs, Limousines available\n\n" +
                "• City Tours:\n Popular attractions included in local tours\n\n" +
                "• Airport Assistance:\n Pick-up, drop-off, and expedited check-ins\n",
        };

        Font nameFont = new Font("Times New Roman", Font.BOLD, 18);
        Font descFont = new Font("Times New Roman", Font.PLAIN, 15);

        for (int i = 0; i < serviceNames.length; i++) {
            JPanel serviceCard = new JPanel();
            serviceCard.setLayout(new BoxLayout(serviceCard, BoxLayout.Y_AXIS));
            serviceCard.setBackground(new Color(203, 214, 248)); // Light purple background for each service card
            serviceCard.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

            JLabel nameLabel = new JLabel(serviceNames[i], SwingConstants.CENTER);
            nameLabel.setFont(nameFont);
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            nameLabel.setForeground(new Color(85, 85, 85));

            JTextArea descLabel = new JTextArea(serviceDescriptions[i]);
            descLabel.setFont(descFont);
            descLabel.setOpaque(true); // Ensure background color applies
            descLabel.setEditable(false);
            descLabel.setFocusable(false);
            descLabel.setForeground(new Color(60, 60, 60));
            descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            descLabel.setMaximumSize(new Dimension(300, 150));
            descLabel.setLineWrap(true);
            descLabel.setWrapStyleWord(true);
            descLabel.setBackground(new Color(203, 214, 248)); // Light purple background for description

            serviceCard.add(nameLabel);
            serviceCard.add(Box.createVerticalStrut(20));
            serviceCard.add(descLabel);

            cardPanel.add(serviceCard, serviceNames[i]);
        }

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE); // Keep buttons panel white

        JButton prevBtn = new JButton("Previous");
        JButton nextBtn = new JButton("Next");

        prevBtn.setBackground(new Color(213, 172, 209)); // Pink background for the Previous button
        nextBtn.setBackground(new Color(213, 172, 209)); // Pink background for the Next button
        prevBtn.setFocusPainted(false);
        nextBtn.setFocusPainted(false);

        buttonPanel.add(prevBtn);
        buttonPanel.add(nextBtn);

        // Add cards and button panel
        mainPanel.add(cardPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Track current card index
        final int[] currentIndex = {0};
        CardLayout cardLayout = (CardLayout) cardPanel.getLayout();

        // Disable previous at start
        prevBtn.setEnabled(false);

        // Next Button
        nextBtn.addActionListener(e -> {
            if (currentIndex[0] < serviceNames.length - 1) {
                currentIndex[0]++;
                cardLayout.next(cardPanel);
                prevBtn.setEnabled(true);
                if (currentIndex[0] == serviceNames.length - 1) {
                    nextBtn.setText("Finish");
                }
            } else {
                showBookingButton(cardPanel, mainPanel);
                prevBtn.setEnabled(false);
                nextBtn.setVisible(false);
            }
        });

        // Previous Button
        prevBtn.addActionListener(e -> {
            if (currentIndex[0] > 0) {
                currentIndex[0]--;
                cardLayout.previous(cardPanel);
                nextBtn.setText("Next");
                if (currentIndex[0] == 0) {
                    prevBtn.setEnabled(false);
                }
            }
        });

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    public static void showBookingButton(JPanel cardPanel, JPanel mainPanel) {
        JPanel finalCard = new JPanel();
        finalCard.setBackground(new Color(203, 214, 248)); // light purple
        finalCard.setLayout(new BoxLayout(finalCard, BoxLayout.Y_AXIS));
        finalCard.setBorder(BorderFactory.createEmptyBorder(50, 30, 50, 30));

        JLabel doneLabel = new JLabel("All services previewed!", SwingConstants.CENTER);
        doneLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        doneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        doneLabel.setForeground(new Color(85, 85, 85));

        JButton bookBtn = new JButton("Let's book your services!");
        bookBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bookBtn.setBackground(new Color(213, 172, 209));//pink
        bookBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookBtn.setFocusPainted(false);
        bookBtn.setPreferredSize(new Dimension(200, 40));
        bookBtn.setMaximumSize(new Dimension(200, 40));
        bookBtn.addActionListener(e -> JOptionPane.showMessageDialog(null, "Redirecting to booking..."));

        finalCard.add(doneLabel);
        finalCard.add(Box.createVerticalStrut(30));
        finalCard.add(bookBtn);

        cardPanel.add(finalCard, "final");
        CardLayout layout = (CardLayout) cardPanel.getLayout();
        layout.show(cardPanel, "final");
    }
} */