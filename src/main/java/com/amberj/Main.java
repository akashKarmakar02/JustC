package com.amberj;

import com.amberj.component.FileTab;
import com.amberj.component.FileTree;
import com.amberj.component.MenuBar;
import com.amberj.component.icon.FlatFolderIcon;
import com.amberj.data.FilesRepository;
import com.amberj.feature.Compiler;
import com.amberj.feature.FileManager;
import com.amberj.lib.ProjectDataLib;
import com.amberj.lib.WindowProvider;
import com.amberj.util.EventEmitter;
import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.icons.FlatFileChooserHomeFolderIcon;
import com.formdev.flatlaf.icons.FlatFileChooserUpFolderIcon;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    static FileManager fileManager;
    static FilesRepository filesRepository;
    static Compiler compiler;
    static EventEmitter emitter;

    static {
        fileManager = new FileManager();
        emitter = new EventEmitter();
        compiler = new Compiler();
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
        WindowProvider.setProjectDir(projectDir);

        if (projectDir == null) {
            JFileChooser folderChooser = new JFileChooser();
            folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int result = folderChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                projectDir = folderChooser.getSelectedFile().getAbsolutePath();
                dataLib.setProperty(ProjectDataLib.ProjectDataKey.PROJECT_DIRECTORY, projectDir);
                WindowProvider.setProjectDir(projectDir);
            } else {
                JOptionPane.showMessageDialog(frame, "A project directory is required to continue.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }



        filesRepository = new FilesRepository(dataLib);

        FileTab tabbedPane = new FileTab(filesRepository, fileManager);
        JMenuBar menuBar = new MenuBar(fileManager, tabbedPane, compiler, emitter);
        AtomicReference<JTree> fileTree = new AtomicReference<>(new FileTree(tabbedPane, projectDir, fileManager, filesRepository));
        AtomicReference<Boolean> isTreeScrollPaneVisible = new AtomicReference<>(true);

        JScrollPane treeScrollPane = new JScrollPane(fileTree.get());
        treeScrollPane.setPreferredSize(new Dimension(270, 0));
        treeScrollPane.setBorder(BorderFactory.createEmptyBorder());
        treeScrollPane.setVisible(isTreeScrollPaneVisible.get());

        emitter.on(EventEmitter.EventType.PROJECT_CHANGED.value, (path) -> {
            Thread.ofVirtual().start(() -> {
                dataLib.setProperty(ProjectDataLib.ProjectDataKey.PROJECT_DIRECTORY, (String) path);
                dataLib.setProperty(ProjectDataLib.ProjectDataKey.VIEW_TAB_FILES, "");
                dataLib.setPropertyList(ProjectDataLib.ProjectDataKey.OPENED_FILES, new ArrayList<>());

                SwingUtilities.invokeLater(() -> {
                    tabbedPane.removeAll();

                    frame.remove(treeScrollPane);
                    fileTree.set(new FileTree(tabbedPane, (String) path, fileManager, filesRepository));
                    JScrollPane newTreeScrollPane = new JScrollPane(fileTree.get());
                    newTreeScrollPane.setPreferredSize(new Dimension(200, 0));
                    frame.add(newTreeScrollPane, BorderLayout.WEST);

                    frame.revalidate();
                    frame.repaint();
                });
            });
        });

        JPanel leftMenu = new JPanel();
        int leftMenuWidth = 50;

        leftMenu.setPreferredSize(new Dimension(leftMenuWidth, 0));
        leftMenu.setMinimumSize(new Dimension(leftMenuWidth, 0));
        leftMenu.setMaximumSize(new Dimension(leftMenuWidth, Integer.MAX_VALUE));

        leftMenu.add(new FlatButton(){{
            setIcon(new FlatFolderIcon());
            addActionListener(e -> {
                var value = isTreeScrollPaneVisible.get();
                treeScrollPane.setVisible(!value);
                isTreeScrollPaneVisible.set(!value);
                frame.revalidate();
                frame.repaint();
            });
        }});

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.X_AXIS));

        leftPanel.add(leftMenu);
        leftPanel.add(treeScrollPane);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, tabbedPane);
        splitPane.setDividerLocation(270);
        splitPane.setResizeWeight(0);
        splitPane.setOneTouchExpandable(false);

        frame.setJMenuBar(menuBar);
        frame.add(splitPane, BorderLayout.CENTER);


        frame.setVisible(true);
    }

    public static void setGlobalFontSize(int fontSize) {
        FontUIResource font = new FontUIResource("SansSerif", Font.PLAIN, fontSize);

        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, font);
            }
        }
    }

    public static void main(String[] args) {
        setGlobalFontSize(18);

        SwingUtilities.invokeLater(Main::createAndShowEditor);
    }
}
