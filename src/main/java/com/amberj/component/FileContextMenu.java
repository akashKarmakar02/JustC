package com.amberj.component;

import com.amberj.feature.FileManager;
import com.formdev.flatlaf.icons.FlatClearIcon;
import com.formdev.flatlaf.icons.FlatFileChooserNewFolderIcon;
import com.formdev.flatlaf.icons.FlatTreeLeafIcon;

import javax.swing.*;
import java.awt.*;

public class FileContextMenu extends JPopupMenu {
    private String path;
    private final String projectDir;

    public FileContextMenu(FileManager fileManager, String projectDir, Runnable runnable) {
        this.projectDir = projectDir;
        JMenuItem newCFile = new JMenuItem("New C File");
        newCFile.setIcon(new FlatTreeLeafIcon());
        newCFile.addActionListener(e -> {
            new CreateDialog("Enter C File", "Name", (value) -> {
                fileManager.createNewFile(value + ".c", getFileCreationPath());
                runnable.run();
            });
        });

        add(newCFile);

        JMenuItem newFolder = new JMenuItem("New Folder");
        newFolder.setIcon(new FlatFileChooserNewFolderIcon());
        newFolder.addActionListener(e -> {
            new CreateDialog("Enter Folder Name", "Enter file name...", (value) -> {
                fileManager.createFolder(value, getFileCreationPath());
                runnable.run();
            });
        });

        add(newFolder);

        JMenuItem delete = new JMenuItem("Delete");
        delete.setIcon(new FlatClearIcon());
        delete.addActionListener(e -> {});

        add(delete);
    }

    private String getFileCreationPath() {
        if (path == null)
            return projectDir;
        return path;
    }

    public void showWithPath(Component invoker, int x, int y, String path) {
        this.path = path;
        show(invoker, x, y);
    }
}
