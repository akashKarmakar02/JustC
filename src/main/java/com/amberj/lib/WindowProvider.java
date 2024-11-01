package com.amberj.lib;

import com.amberj.terminal.JustTerminal;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import javax.swing.*;
import java.awt.*;

public class WindowProvider {

    private static JFrame frame;
    private static JustTerminal console;

    public static JFrame getFrame() {
        if (frame == null) {
            try {
                UIManager.setLookAndFeel(new FlatMacDarkLaf());

                UIManager.put("Component.focusWidth", 0);
                UIManager.put("TabbedPane.focusColor", Color.DARK_GRAY);

            } catch (UnsupportedLookAndFeelException e) {
                throw new RuntimeException(e);
            }

            frame = new JFrame();
            frame.setTitle("JustC");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 700);

            return frame;
        }

        return frame;
    }

    public static JustTerminal getConsole() {
        if (console == null) {
            console = new JustTerminal();
            return console;
        }
        return console;
    }

    public static void showBottomPanel() {
        console.getTerminal().setVisible(true);
        frame.add(console.getTerminal(), BorderLayout.SOUTH); // Add it to the frame
        frame.revalidate(); // Refresh the frame layout
        frame.repaint(); // Repaint the frame
    }
}
