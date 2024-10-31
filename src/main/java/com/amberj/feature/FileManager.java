package com.amberj.feature;

import com.amberj.lib.WindowProvider;
import com.formdev.flatlaf.ui.FlatFileChooserUI;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class FileManager {

    private File currentFile;

    JFrame frame = WindowProvider.getFrame();

    // Open a file and return its contents as a String
    public String openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            try {
                return Files.readString(currentFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // Save the current file with the provided content
    public boolean saveFile(String content) {
        if (currentFile != null) {
            try (FileWriter writer = new FileWriter(currentFile)) {
                writer.write(content);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // Save as - let the user choose a file location and save the content there
    public boolean saveFileAs(String content) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(currentFile)) {
                writer.write(content);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // Open a folder and return its Path
    public Path openFolder() {
        JFileChooser folderChooser = new JFileChooser();
        FlatFileChooserUI.createUI(folderChooser);
        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = folderChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return folderChooser.getSelectedFile().toPath();
        }
        return null;
    }

    public void createNewFile(String fileName, String projectDir) {
        if (fileName == null || fileName.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Can not create file " + fileName, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (projectDir == null || projectDir.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Project directory cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error: Project directory cannot be empty.");
            return;
        }

        File newFile = new File(projectDir, fileName);

        try {
            // Check if file already exists
            if (newFile.exists()) {
                JOptionPane.showMessageDialog(frame, "File already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Attempt to create the file
                if (!newFile.createNewFile()) {
                    JOptionPane.showMessageDialog(frame, "Could not create the file.", "Error", JOptionPane.ERROR_MESSAGE);
                    System.out.println("Error: Could not create the file.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error: An exception occurred while creating the file.");
            e.printStackTrace();
        }
    }

    public boolean deleteFileOrDirectory(File file) {
        if (file.isDirectory()) {
            for (File subFile : Objects.requireNonNull(file.listFiles())) {
                deleteFileOrDirectory(subFile);
            }
        }
        return file.delete();
    }

    public void createFolder(String folderName, String projectDir) {
        if (folderName == null || folderName.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Folder name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (projectDir == null || projectDir.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Project directory cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        File newFolder = new File(projectDir, folderName);

        if (newFolder.exists()) {
            JOptionPane.showMessageDialog(frame, "Folder already exists.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (newFolder.mkdirs()) {
            JOptionPane.showMessageDialog(frame, "Folder created successfully: " + newFolder.getPath(), "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "Could not create the folder.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
