package com.amberj.component;

import com.amberj.feature.Compiler;
import com.amberj.feature.FileManager;
import com.amberj.lib.WindowProvider;
import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.icons.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuBar extends JMenuBar {

    private final JFrame frame = WindowProvider.getFrame();

    public MenuBar(FileManager fileManager, FileTab fileTab, Compiler compiler) {
        super();

        // Create File Menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(new JMenuItem("New"));
        fileMenu.add(new JMenuItem("Open") {{
            addActionListener(e -> fileManager.openFolder());
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


        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        FlatButton runButton = createWindowControlButton(new FlatRunIcon(), e -> {
            var path = fileTab.getSelectedTabNodePath();
            if (path != null) {
                compiler.compileAndRunCCode(path);
                System.out.println(path);
            }
        });
        FlatButton minimizeButton = createWindowControlButton(new FlatWindowIconifyIcon(), e -> frame.setState(Frame.ICONIFIED));
        FlatButton maximizeButton = createWindowControlButton(new FlatWindowMaximizeIcon(), e -> frame.setExtendedState(frame.getExtendedState() ^ Frame.MAXIMIZED_BOTH));
        FlatButton closeButton = createWindowControlButton(new FlatWindowCloseIcon(), e -> System.exit(0));


        setTitleBarButtonDimension(runButton);

        add(Box.createHorizontalGlue());
        rightPanel.add(runButton);
        rightPanel.add(minimizeButton);
        rightPanel.add(maximizeButton);
        rightPanel.add(closeButton);

        add(rightPanel);

        addDragFunctionality();
    }


    private void setTitleBarButtonDimension(FlatButton button) {
        button.setPreferredSize(new Dimension(45, 37));
    }

    private FlatButton createWindowControlButton(Icon icon, ActionListener action) {
        FlatButton button = new FlatButton();
        button.setButtonType(FlatButton.ButtonType.toolBarButton);
        button.setIcon(icon);
        button.setPreferredSize(new Dimension(45, 37));
        button.addActionListener(action);
        return button;
    }

    private void addDragFunctionality() {
        final Point[] startPoint = {null};

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startPoint[0] = e.getPoint();
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point windowPoint = frame.getLocation();
                frame.setLocation(
                    windowPoint.x + e.getX() - startPoint[0].x,
                    windowPoint.y + e.getY() - startPoint[0].y
                );
            }
        });
    }
}
