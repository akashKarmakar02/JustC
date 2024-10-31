package com.amberj;

import com.amberj.component.FileTree;
import com.amberj.component.MenuBar;
import com.amberj.feature.FileManager;
import com.amberj.lib.WindowProvider;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import javax.swing.*;
import java.awt.*;

public class Main {
    static FileManager fileManager;

    static {
        fileManager = new FileManager();
    }

    public static void createAndShowEditor() {
        try {
            UIManager.setLookAndFeel(new FlatMacDarkLaf());

            UIManager.put("Component.focusWidth", 0);
            UIManager.put("TabbedPane.focusColor", Color.DARK_GRAY);

        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        JFrame frame = WindowProvider.getFrame();

        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JMenuBar menuBar = new MenuBar(fileManager);
        frame.setJMenuBar(menuBar);

        JTabbedPane tabbedPane = new JTabbedPane();
        JTree fileTree = new FileTree(tabbedPane, "/home/akash/Desktop/c", fileManager);

        JScrollPane treeScrollPane = new JScrollPane(fileTree);

        treeScrollPane.setPreferredSize(new Dimension(200, 0));
        treeScrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove border focus highlight

        frame.add(treeScrollPane, BorderLayout.WEST);
        frame.add(tabbedPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowEditor);
    }
}
