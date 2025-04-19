/**
 * File: LunaWallet.java
 * Author: Khilfi
 * Description:
 * The Luna Wallet is a component of Del Luna Hotel app.
 * The purpose of the e-wallet is for transactions.
 * User can top-up money inside the e-wallet and obtain points for future use.
 */
package com.delluna.dellunahotel.utils;
import javax.swing.*;

import com.delluna.dellunahotel.models.LunaWalletDB;

import java.awt.*;
import java.awt.event.*;

/**
 * Handles user interface for LunaWallet
 */
public class LunaWallet {
	private LunaWalletDB wallet;
	private JFrame frame;
	private JPanel mainPanel;
	private CardLayout cardLayout;
	private enum Page { DASHBOARD, TOP_UP, VIEW_TRANSACTIONS, SETTINGS, UPDATE_PIN, UPDATE_SECURITY_QUESTION, DEACTIVATE_WALLET, EXIT };
	
	public static void main(String[] args) {
		new LunaWallet("1");
	}
	
	LunaWallet(String UID) {
		
		try (LunaWalletDB temp = new LunaWalletDB(UID)) {
			wallet = temp;
			
		} catch (LunaWalletDB.UidNotFound e) {
			// Ask user to activate LunaWallet
		}
		
		frame = new JFrame("Del Luna Hotel App");
		frame.setSize(500, 400);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		// Set CardLayout
		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);
		
		// Frame Listener
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// Call these functions before closing
				wallet.save();
				
				// Ask for confirmation before exit
				int choice = JOptionPane.showConfirmDialog(frame, "Do you really want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				
				if (choice == JOptionPane.YES_OPTION) {
					frame.dispose();
					System.exit(0);
				}
				
			}
		});
		
		// Add different page
		mainPanel.add(createDashboardPage(), Page.DASHBOARD.toString());
		mainPanel.add(createTopUpPage(), Page.TOP_UP.toString());
		mainPanel.add(createSettingsPage(), Page.SETTINGS.toString());
		mainPanel.add(createUpdatePINPage(), Page.UPDATE_PIN.toString());
		mainPanel.add(createUpdateSecurityQuestionPage(), Page.UPDATE_SECURITY_QUESTION.toString());
		
		frame.add(mainPanel);
		frame.setVisible(true);

	}
	
	private JPanel createDashboardPage() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		JLabel label = new JLabel("Welcome to Del Luna Hotel!", JLabel.CENTER);
		
		JPanel buttonPanel = new JPanel();
		JButton topUpButton = new JButton("Top Up");
		JButton viewTransactionsButton = new JButton("View Transactions");
		JButton settingsButton = new JButton("Settings");
		
		topUpButton.addActionListener(e -> cardLayout.show(mainPanel, Page.TOP_UP.toString()));
		viewTransactionsButton.addActionListener(e -> cardLayout.show(mainPanel, Page.VIEW_TRANSACTIONS.toString()));
		settingsButton.addActionListener(e -> cardLayout.show(mainPanel, Page.SETTINGS.toString()));
		
		panel.add(label, BorderLayout.CENTER);
		buttonPanel.add(topUpButton);
		buttonPanel.add(viewTransactionsButton);
		buttonPanel.add(settingsButton);
		panel.add(buttonPanel, BorderLayout.SOUTH);
		
		return panel;
	}
	
	private JPanel createTopUpPage() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		double curBalance = wallet.getBalance();
		
		JLabel label = new JLabel(Double.toString(curBalance), JLabel.CENTER);
		
		JButton rm10Button = new JButton("10");
		JButton rm20Button = new JButton("20");
		JButton rm50Button = new JButton("50");
		JButton rm100Button = new JButton("100");
		JButton customAmount = new JButton("Custom");
		JButton backButton = new JButton("Back");
		
		rm10Button.addActionListener(e -> {
			try {
				wallet.topUpBalance(10);
				label.setText(Double.toString(wallet.getBalance()));
			} catch (LunaWalletDB.BalanceLimitExceeded e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		rm20Button.addActionListener(e -> {
			try {
				wallet.topUpBalance(20);
				label.setText(Double.toString(wallet.getBalance()));
			} catch (LunaWalletDB.BalanceLimitExceeded e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		rm50Button.addActionListener(e -> {
			try {
				wallet.topUpBalance(50);
				label.setText(Double.toString(wallet.getBalance()));
			} catch (LunaWalletDB.BalanceLimitExceeded e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		rm100Button.addActionListener(e -> {
			try {
				wallet.topUpBalance(100);
				label.setText(Double.toString(wallet.getBalance()));
			} catch (LunaWalletDB.BalanceLimitExceeded e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		customAmount.addActionListener(e -> {
			try {
				wallet.topUpBalance(1_000_000);
				label.setText(Double.toString(wallet.getBalance()));
			} catch (LunaWalletDB.BalanceLimitExceeded e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		backButton.addActionListener(e -> cardLayout.show(mainPanel, Page.DASHBOARD.toString()));
		
		JPanel buttonPanel1 = new JPanel();
		buttonPanel1.add(rm10Button);
		buttonPanel1.add(rm20Button);
		buttonPanel1.add(rm50Button);
		buttonPanel1.add(rm100Button);
		buttonPanel1.add(customAmount);
		
		JPanel buttonPanel2 = new JPanel();
		buttonPanel2.add(backButton);
		
		panel.add(label, BorderLayout.CENTER);
		panel.add(buttonPanel1, BorderLayout.SOUTH);
		panel.add(buttonPanel2, BorderLayout.EAST);
		
		return panel;
	}
	
	void ViewTransactions() {
		
	}
	
	private JPanel createSettingsPage() {
		JPanel panel = new JPanel();
		
		JButton changePinButton = new JButton("Change PIN");
		JButton securityQuestionsButton = new JButton("Change Security Question");
		JButton backButton = new JButton("Back");
		
		changePinButton.addActionListener(e -> cardLayout.show(mainPanel, Page.UPDATE_PIN.toString()));
		securityQuestionsButton.addActionListener(e -> cardLayout.show(mainPanel, Page.UPDATE_SECURITY_QUESTION.toString()));
		backButton.addActionListener(e -> cardLayout.show(mainPanel, Page.DASHBOARD.toString()));
		
		panel.add(changePinButton);
		panel.add(securityQuestionsButton);
		panel.add(backButton);
		
		return panel;
	}
	
	JPanel createUpdatePINPage() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(6, 1));
		
		JLabel promptPinLabel = new JLabel("Please enter current PIN");
		JPasswordField pinField = new JPasswordField();
		
		JLabel promptNewPinLabel = new JLabel("Please enter new PIN");
		JPasswordField newPinField = new JPasswordField();
		
		JButton submitButton = new JButton("Submit");
		
		panel.add(promptPinLabel);
		panel.add(pinField);
		panel.add(promptNewPinLabel);
		panel.add(newPinField);
		panel.add(submitButton);
		
		submitButton.addActionListener(e -> {
			String oldPin = new String(pinField.getPassword());
			String newPin = new String(newPinField.getPassword());
			
			if (oldPin.equals(wallet.getPinHash())) {
				wallet.setPin(newPin);
				JOptionPane.showMessageDialog(null, "PIN changed successfully!");
			}
			else {
				JOptionPane.showMessageDialog(null, "Wrong PIN entered!");
			}
			
			cardLayout.show(mainPanel, Page.SETTINGS.toString());
			pinField.setText("");
			newPinField.setText("");
		});
		
		return panel;
	}
	
	JPanel createUpdateSecurityQuestionPage() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(5, 1));
		
		JLabel prompt = new JLabel("Please choose a question & answer");
		
		String[] securityQuestions = {
				"What was the name of your first pet?",
				"What is the name of the city where you were born?",
				"What is your favorite teacher’s name?",
				"What was the make and model of your first car?",
				"What is your mother’s maiden name?"
				};
        JComboBox<String> securityQuestionsBox = new JComboBox<>(securityQuestions);
        
        JTextField answerField = new JTextField();
        
        JButton submitButton = new JButton("Submit");
        JButton backButton = new JButton("Back");
        
        backButton.addActionListener(e -> cardLayout.show(mainPanel, Page.SETTINGS.toString()));
        submitButton.addActionListener(e -> {
        	String selected = (String) securityQuestionsBox.getSelectedItem();
        	String answer = answerField.getText();
        	
        	if (!answer.equals("")) {
        		wallet.setSecurityQuestion(selected, answer);
        		JOptionPane.showMessageDialog(null, "Successfully updated your Security Question");
        		cardLayout.show(mainPanel, Page.SETTINGS.toString());
        	} else {
        		JOptionPane.showMessageDialog(null, "Please provide an Answer");
        	}
        });
        
        panel.add(prompt);
        panel.add(securityQuestionsBox);
        panel.add(answerField);
        panel.add(submitButton);
        panel.add(backButton);
        
        return panel;
	}
}

