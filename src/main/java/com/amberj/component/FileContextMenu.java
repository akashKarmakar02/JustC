package com.amberj.component;

import com.amberj.feature.FileManager;
import com.formdev.flatlaf.icons.FlatClearIcon;
import com.formdev.flatlaf.icons.FlatFileChooserNewFolderIcon;
import com.formdev.flatlaf.icons.FlatTreeLeafIcon;

import javax.swing.*;

public class FileContextMenu extends JPopupMenu {

    public FileContextMenu(FileManager fileManager, String projectDir, Runnable runnable) {

        JMenuItem newCFile = new JMenuItem("New C File");
        newCFile.setIcon(new FlatTreeLeafIcon());
        newCFile.addActionListener(e -> {
            new CreateDialog("Enter file name...", (value) -> {
                fileManager.createNewFile(value + ".c", projectDir);
                runnable.run();
            });
        });

        add(newCFile);

        JMenuItem newFolder = new JMenuItem("New Folder");
        newFolder.setIcon(new FlatFileChooserNewFolderIcon());
        newFolder.addActionListener(e -> {
            new CreateDialog("Enter file name...", (value) -> {
                fileManager.createNewFile(value + ".c", projectDir);
                runnable.run();
            });
        });

        add(newFolder);

        JMenuItem delete = new JMenuItem("New C File");
        delete.setIcon(new FlatClearIcon());
        delete.addActionListener(e -> {
            new CreateDialog("Enter file name...", (value) -> {
                fileManager.createNewFile(value + ".c", projectDir);
                runnable.run();
            });
        });

        add(delete);
    }

}
