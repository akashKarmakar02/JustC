package com.amberj.feature;

import com.amberj.data.CompilerRepository;
import com.amberj.lib.WindowProvider;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GccManager {

    private String gccPath;
    private final JFrame frame = WindowProvider.getFrame();

    public GccManager(CompilerRepository repository) throws Exception {
        if (isGccInstalled()) {
            gccPath = "gcc";
        } else {
            var path = repository.getCompilerPath();
            if (path == null) {
                var fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    var compilerPath = fileChooser.getSelectedFile().getAbsolutePath();
                    repository.saveCompilerPath(compilerPath);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select compiler path.", "Error", JOptionPane.ERROR_MESSAGE);
                    throw new Exception("Error: No compiler path");
                }
            }
        }
    }

    public static boolean isGccInstalled() {
        try {
            Process process = new ProcessBuilder("gcc", "--version").start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line = reader.readLine();

                return line != null && line.contains("gcc");
            }
        } catch (IOException e) {
            return false;
        }
    }
}
