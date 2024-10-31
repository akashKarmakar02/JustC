package com.amberj.component;

import com.amberj.feature.FileManager;

import javax.swing.*;

public class MenuBar extends JMenuBar {

    public MenuBar(FileManager fileManager) {
        super();
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(new JMenuItem("New"));
        fileMenu.add(new JMenuItem("Open") {{
            addActionListener(e -> {
                fileManager.openFolder();
            });
        }});
        fileMenu.add(new JMenuItem("Save"));
        fileMenu.add(new JMenuItem("Save As"));
        add(fileMenu);

        JMenu editMenu = new JMenu("Edit");
        editMenu.add(new JMenuItem("Undo"));
        editMenu.add(new JMenuItem("Redo"));
        add(editMenu);
    }

}
