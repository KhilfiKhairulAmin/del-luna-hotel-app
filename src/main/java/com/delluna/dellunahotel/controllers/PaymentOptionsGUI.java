package com.delluna.dellunahotel.controllers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaymentOptionsGUI extends JFrame {
    private final Runnable onClose;

    public PaymentOptionsGUI(Runnable onClose) {
        this.onClose = onClose;
        setTitle("Select Payment Method");
        // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new FlowLayout());

        JButton paypalButton = createImageButton("paypal.png", "PayPal");
        JButton creditCardButton = createImageButton("credit.png", "Credit Card");
        JButton debitCardButton = createImageButton("icon2.png", "Luna Wallet");

        paypalButton.addActionListener(new PaymentActionListener("PayPal"));
        creditCardButton.addActionListener(new PaymentActionListener("Credit Card"));
        debitCardButton.addActionListener(new PaymentActionListener("Luna Wallet"));

        panel.add(paypalButton);
        panel.add(creditCardButton);
        panel.add(debitCardButton);

        add(panel);
        setVisible(true);
    }

    private JButton createImageButton(String imagePath, String altText) {
        ImageIcon icon = new ImageIcon("src/main/resources/com/delluna/dellunahotel/images/" + imagePath);
        JButton button = new JButton(icon);
        button.setToolTipText(altText);
        return button;
    }

    private class PaymentActionListener implements ActionListener {
        private final String paymentMethod;

        public PaymentActionListener(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String input = JOptionPane.showInputDialog(
                    null,
                    "Enter your 16-digit card number for " + paymentMethod + ":",
                    "Enter Card Details",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (input != null && input.matches("\\d{16}")) {
                JOptionPane.showMessageDialog(
                        null,
                        "Payment made successfully via " + paymentMethod + "!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                SwingUtilities.getWindowAncestor((Component) e.getSource()).dispose();

                // ✅ Run the callback after closing
                if (onClose != null) {
                    onClose.run();
                }
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Invalid input. Please enter a 16-digit number.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}