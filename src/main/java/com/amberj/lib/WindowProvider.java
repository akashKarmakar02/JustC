package com.amberj.lib;

import com.amberj.terminal.JustTerminal;
import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.icons.FlatWindowCloseIcon;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import javax.swing.*;
import java.awt.*;

public class WindowProvider {

    private static JFrame frame;
    private static JustTerminal console;
    private static JPanel terminalPanel;
    private static String projectDir;

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
            frame.setUndecorated(true);
            frame.setSize(1000, 700);
        }

        return frame;
    }

    public static JustTerminal getConsole() {
        if (console == null) {
            console = new JustTerminal();
        }
        return console;
    }

    public static void showBottomPanel() {
        if (terminalPanel == null) {
            JPanel headerPanel = new JPanel(new BorderLayout());
            FlatButton closeButton = new FlatButton();
            closeButton.setButtonType(FlatButton.ButtonType.borderless);
            closeButton.setIcon(new FlatWindowCloseIcon());
            closeButton.setPreferredSize(new Dimension(20, 20));
            closeButton.addActionListener(e -> terminalPanel.setVisible(false));

            headerPanel.add(closeButton, BorderLayout.EAST);

            terminalPanel = new JPanel(new BorderLayout());
            terminalPanel.add(headerPanel, BorderLayout.NORTH);
            terminalPanel.add(getConsole().getTerminal(), BorderLayout.CENTER);

            frame.add(terminalPanel, BorderLayout.SOUTH);
        }

        terminalPanel.setVisible(true);
        frame.revalidate();
        frame.repaint();
    }

    public static String getProjectDir() {
        return projectDir;
    }

    public static void setProjectDir(String projectDir) {
        WindowProvider.projectDir = projectDir;
    }
}
