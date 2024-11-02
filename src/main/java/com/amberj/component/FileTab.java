package com.amberj.component;

import com.amberj.data.FilesRepository;
import com.amberj.feature.FileManager;
import com.amberj.feature.JustCLanguageSupport;
import com.amberj.lib.WindowProvider;
import org.fife.rsta.ac.LanguageSupportFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileTab extends JTabbedPane {
    private final FilesRepository filesRepository;
    private final JFrame frame = WindowProvider.getFrame();
    public final Map<Integer, String> tabFilePathMap = new HashMap<>();
    private final FileManager fileManager;


    public FileTab(FilesRepository filesRepository, FileManager fileManager) {
        this.filesRepository = filesRepository;
        this.fileManager = fileManager;
    }

    public void openLastOpenedFiles(JTabbedPane tabbedPane) {
        List<String> lastOpenedFiles = filesRepository.getLastOpenedFiles();
        String lastViewedFile = filesRepository.getLastViewFile();

        for (String filePath : lastOpenedFiles) {
            File file = new File(filePath);
            if (file.exists()) {
                openFileInNewTab(file, tabbedPane);
            }
        }


        if (lastViewedFile != null && !lastViewedFile.isEmpty()) {
            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                String tabFilePath = tabFilePathMap.get(i);
                if (tabFilePath != null && tabFilePath.equals(lastViewedFile)) {
                    tabbedPane.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    public void openFileInNewTab(File file, JTabbedPane tabbedPane) {
        String filePath = file.getAbsolutePath();

        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            String tabFilePath = tabFilePathMap.get(i);
            if (tabFilePath != null && tabFilePath.equals(filePath)) {
                tabbedPane.setSelectedIndex(i);
                return;
            }
        }

        try {
            String content = new String(Files.readAllBytes(file.toPath()));

            RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
            textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_C);
            textArea.setCodeFoldingEnabled(true);
            textArea.setAntiAliasingEnabled(true);
            textArea.setText(content);
            textArea.setFont(new Font("SansSerif", Font.PLAIN, 25));

            textArea.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S) {
                        saveCurrentFile(tabbedPane);
                    }
                }
            });

            var lsp = LanguageSupportFactory.get();
            lsp.addLanguageSupport("text/c", "com.amberj.feature.JustCLanguageSupport");

            lsp.register(textArea);

            try (InputStream in = FileTree.class.getClassLoader().getResourceAsStream("dark.xml")) {
                Theme theme = Theme.load(in);
                theme.apply(textArea);
            }

            RTextScrollPane scrollPane = new RTextScrollPane(textArea);
            tabbedPane.addTab(file.getName(), scrollPane);

            // Store the path for the tab
            tabFilePathMap.put(tabbedPane.getTabCount() - 1, filePath);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Could not open file: " + file.getName(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        saveOpenedFiles(tabbedPane);
    }

    public void saveOpenedFiles(JTabbedPane tabbedPane) {
        List<String> openedFiles = new ArrayList<>();
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            String path = tabFilePathMap.get(i);
            if (path != null) {
                openedFiles.add(path);
            }
        }
        filesRepository.saveLastOpenedFiles(openedFiles);
    }

    public String getSelectedTabNodePath() {
        int selectedIndex = getSelectedIndex();
        if (selectedIndex >= 0) {
            return tabFilePathMap.get(selectedIndex);
        }

        return null;
    }

    public void saveCurrentFile(JTabbedPane tabbedPane) {
        int selectedIndex = tabbedPane.getSelectedIndex();
        if (selectedIndex >= 0) {
            Component component = tabbedPane.getComponentAt(selectedIndex);
            if (component instanceof RTextScrollPane scrollPane) {
                RSyntaxTextArea textArea = (RSyntaxTextArea) scrollPane.getViewport().getView();
                String filePath = tabFilePathMap.get(tabbedPane.getSelectedIndex());
                if (filePath != null) {
                    fileManager.saveFile(new File(filePath), textArea.getText());
                } else {
                    JOptionPane.showMessageDialog(frame, "No file is currently selected.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

}
