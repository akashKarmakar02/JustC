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

    public CreateDialog(String title, String hint, Consumer<String> onEnter) {
        JFrame frame = WindowProvider.getFrame();

        JDialog dialog = new JDialog((Frame) null, true);
        dialog.setUndecorated(true);
        dialog.setLayout(new BorderLayout());

        JLabel headingLabel = new JLabel(title);
        headingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headingLabel.setBorder(new EmptyBorder(10, 0, 0, 0));

        FlatTextField textField = new FlatTextField();
        textField.setColumns(20);
        textField.setPlaceholderText(hint);

        JPanel textFieldPanel = new JPanel();
        textFieldPanel.setLayout(new BorderLayout());
        textFieldPanel.setBorder(new EmptyBorder(20, 15, 15, 10));
        textFieldPanel.add(textField, BorderLayout.CENTER);

        textField.addActionListener(e -> {
            String inputText = textField.getText();
            if (!inputText.equals(hint) && !inputText.isEmpty()) {
                onEnter.accept(inputText);
                dialog.dispose();
            }
        });

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

        dialog.add(headingLabel, BorderLayout.NORTH);
        dialog.add(textFieldPanel, BorderLayout.CENTER);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }
}
