package com.amberj.component;

import com.amberj.feature.Compiler;
import com.amberj.feature.FileManager;
import com.amberj.lib.WindowProvider;
import com.formdev.flatlaf.extras.components.FlatButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class MenuBar extends JMenuBar {

    private final JFrame frame = WindowProvider.getFrame();
    private final FileTab fileTab;
    private final Compiler compiler;

    public MenuBar(FileManager fileManager, FileTab fileTab, Compiler compiler) {
        super();
        this.fileTab = fileTab;
        this.compiler = compiler;

        // Create File Menu
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

        // Create Edit Menu
        JMenu editMenu = new JMenu("Edit");
        editMenu.add(new JMenuItem("Undo"));
        editMenu.add(new JMenuItem("Redo"));
        add(editMenu);

        JMenu tools = new JMenu("Tools");
        tools.add(new JMenuItem("Open Terminal") {{
            addActionListener(e -> {
                WindowProvider.showBottomPanel();
                frame.revalidate();
                frame.repaint();
            });
        }});

        add(tools);

        // Set the menu background color explicitly (optional)
        UIManager.put("Panel.background", UIManager.getColor("Menu.background"));
        UIManager.put("Panel.foreground", UIManager.getColor("Menu.foreground"));
        UIManager.put("Panel.background", UIManager.getColor("MenuItem.background"));
        UIManager.put("Panel.foreground", UIManager.getColor("MenuItem.foreground"));


        // Create a panel for the button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));



        FlatButton runButton = new FlatButton();
        runButton.setButtonType(FlatButton.ButtonType.roundRect);
        runButton.setText("Run");
        runButton.addActionListener(e -> {
            var path = fileTab.getSelectedTabNodePath();
            if (path != null) {
                compiler.compileAndRunCCode(path);
                System.out.println(path);
            }
        });

        runButton.setPreferredSize(new Dimension(90, 30));

        add(Box.createHorizontalGlue());
        rightPanel.add(runButton);

        // Add the panel to the menu bar
        add(rightPanel);
    }
}
