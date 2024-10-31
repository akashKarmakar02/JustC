package com.amberj;

import com.amberj.component.FileTree;
import com.amberj.component.MenuBar;
import com.amberj.feature.FileManager;
import com.amberj.lib.ProjectDataLib;
import com.amberj.lib.WindowProvider;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

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

        ProjectDataLib dataLib;

        try {
            dataLib = new ProjectDataLib();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JFrame frame = WindowProvider.getFrame();
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        String projectDir = dataLib.getProperty(ProjectDataLib.ProjectDataKey.PROJECT_DIRECTORY);

        if (projectDir == null) {
            JFileChooser folderChooser = new JFileChooser();
            folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int result = folderChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                projectDir = folderChooser.getSelectedFile().getAbsolutePath();
                dataLib.setProperty(ProjectDataLib.ProjectDataKey.PROJECT_DIRECTORY, projectDir);
            } else {
                JOptionPane.showMessageDialog(frame, "A project directory is required to continue.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        JMenuBar menuBar = new MenuBar(fileManager);
        frame.setJMenuBar(menuBar);

        JTabbedPane tabbedPane = new JTabbedPane();
        JTree fileTree = new FileTree(tabbedPane, projectDir, fileManager);

        JScrollPane treeScrollPane = new JScrollPane(fileTree);
        treeScrollPane.setPreferredSize(new Dimension(200, 0));
        treeScrollPane.setBorder(BorderFactory.createEmptyBorder());

        frame.add(treeScrollPane, BorderLayout.WEST);
        frame.add(tabbedPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowEditor);
    }
}
