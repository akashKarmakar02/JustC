package com.amberj.terminal;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class TerminalEmulator {
    public static void main(String[] args) throws UnsupportedLookAndFeelException {

        configureJavaUtilLogging();

        UIManager.setLookAndFeel(new FlatMacDarkLaf());

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Terminal");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 800);

            var terminal = new JustTerminal();

            frame.add(terminal.getTerminal());

            frame.setVisible(true);

        });

    }

    private static void configureJavaUtilLogging() {
        String format = "[%1$tF %1$tT] [%4$-7s] %5$s %n";
        try {
            LogManager.getLogManager().readConfiguration(new ByteArrayInputStream(
                    ("java.util.logging.SimpleFormatter.format=" + format).getBytes(StandardCharsets.UTF_8)
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Logger rootLogger = Logger.getLogger("");
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        rootLogger.addHandler(consoleHandler);
        rootLogger.setLevel(Level.INFO);
    }
}
