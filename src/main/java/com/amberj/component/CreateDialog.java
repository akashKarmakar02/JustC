package com.amberj.component;

import com.amberj.lib.WindowProvider;
import com.formdev.flatlaf.extras.components.FlatTextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

public class CreateDialog {

    public CreateDialog(String hint, Consumer<String> onEnter) {
        JFrame frame = WindowProvider.getFrame();

        JDialog dialog = new JDialog((Frame) null, true);
        dialog.setUndecorated(true);
        dialog.setLayout(new FlowLayout());

        FlatTextField textField = new FlatTextField();
        textField.setColumns(20);
        textField.setPlaceholderText(hint);

        // Wrap the FlatTextField in a JPanel with padding
        JPanel textFieldPanel = new JPanel();
        textFieldPanel.setLayout(new BorderLayout());
        textFieldPanel.setBorder(new EmptyBorder(10, 3, 3, 4)); // Add padding here
        textFieldPanel.add(textField, BorderLayout.CENTER);

        // Add ActionListener to handle Enter key in the text field
        textField.addActionListener(e -> {
            String inputText = textField.getText();
            if (!inputText.equals(hint) && !inputText.isEmpty()) {
                onEnter.accept(inputText);
                dialog.dispose();
            }
        });

        // Add KeyListener to handle Enter and Escape keys
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                    onEnter.accept(textField.getText());
                    dialog.dispose();
                } else if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dialog.dispose();
                }
            }
        });

        // Add the panel with padding to the dialog
        dialog.add(textFieldPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true); // Show the dialog
    }
}
