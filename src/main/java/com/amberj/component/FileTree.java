package com.amberj.component;

import com.amberj.common.FileData;
import com.amberj.common.FileType;
import com.amberj.component.icon.FlatCFileIcon;
import com.amberj.data.FilesRepository;
import com.amberj.feature.FileManager;
import com.amberj.lib.WindowProvider;
import com.formdev.flatlaf.icons.FlatFileViewFileIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FileTree extends JTree {

    private final String projectDir;
    private final DefaultMutableTreeNode root;
    private final JFrame frame = WindowProvider.getFrame();
    private final FilesRepository filesRepository;

    public FileTree(FileTab tabbedPane, String projectDir, FileManager fileManager, FilesRepository filesRepository) {
        super(new DefaultMutableTreeNode(Arrays.stream(projectDir.split("/")).toList().getLast()));
        this.projectDir = projectDir;
        this.filesRepository = filesRepository;

        this.root = (DefaultMutableTreeNode) this.getModel().getRoot();

        createFileNodes(new File(projectDir), root);
        setBorder(new EmptyBorder(5, 5, 0, 0));

        tabbedPane.openLastOpenedFiles(tabbedPane);

        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        setCellRenderer(new DefaultTreeCellRenderer() {
            private final Icon folderIcon = UIManager.getIcon("FileView.directoryIcon");

            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                          boolean selected, boolean expanded,
                                                          boolean leaf, int row, boolean hasFocus) {
                Component c = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

                Object userObject = node.getUserObject();
                if (node == root) {
                    setIcon(folderIcon);
                } else if (userObject instanceof FileData fileData) {
                    if (fileData.type() == FileType.Folder) {
                        setIcon(folderIcon);
                    } else if (fileData.type() == FileType.File && fileData.name().endsWith(".c")){
                        setIcon(new FlatCFileIcon());
                    } else {
                        setIcon(new FlatFileViewFileIcon());
                    }
                }

                return c;
            }
        });

        tabbedPane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S) {
                    tabbedPane.saveCurrentFile(tabbedPane);
                }
            }
        });

        tabbedPane.addChangeListener(e -> saveLastViewedFile(tabbedPane));

        FileContextMenu popupMenu = new FileContextMenu(fileManager, projectDir, this::reloadTree);

        addTreeSelectionListener(e -> openSelectedFile(tabbedPane));
        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    int row = getClosestRowForLocation(e.getX(), e.getY());
                    setSelectionRow(row);
                    TreePath path = getPathForRow(row);
                    if (path != null) {
                        popupMenu.showWithPath(e.getComponent(), e.getX(), e.getY(), getSelectedNodePath());
                    }
                }
            }
        });

        // Add key listener for DELETE key
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    deleteSelectedNode();
                }
            }
        });

        ExecutorService watcherService = Executors.newVirtualThreadPerTaskExecutor();
        watcherService.submit(this::watchDirectoryChanges);
        reloadTree();
    }

    public String getSelectedNodePath() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) getLastSelectedPathComponent();
        if (selectedNode == null || selectedNode == root) return null;

        StringBuilder filePath = new StringBuilder(projectDir);
        for (int i = 1; i < selectedNode.getPath().length; i++) {
            filePath.append("/").append(selectedNode.getPath()[i]);
        }

        return filePath.toString();
    }

    private void deleteSelectedNode() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) getLastSelectedPathComponent();
        if (selectedNode == null || selectedNode == root) return;

        int confirm = JOptionPane.showConfirmDialog(frame,
            "Are you sure you want to delete " + selectedNode + "?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            StringBuilder filePath = new StringBuilder(projectDir);
            for (int i = 1; i < selectedNode.getPath().length; i++) {
                filePath.append("/").append(selectedNode.getPath()[i]);
            }

            File fileToDelete = new File(filePath.toString());
            if (deleteFileOrDirectory(fileToDelete)) {
                ((DefaultTreeModel) getModel()).removeNodeFromParent(selectedNode);
                reloadTree();
            } else {
                JOptionPane.showMessageDialog(frame, "Could not delete " + fileToDelete.getName(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean deleteFileOrDirectory(File file) {
        if (file.isDirectory()) {
            for (File subFile : Objects.requireNonNull(file.listFiles())) {
                deleteFileOrDirectory(subFile);
            }
        }
        return file.delete();
    }

    private void openSelectedFile(FileTab tabbedPane) {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) getLastSelectedPathComponent();
        if (selectedNode == null || !selectedNode.isLeaf()) return;

        StringBuilder filePath = new StringBuilder(projectDir);
        for (int i = 1; i < selectedNode.getPath().length; i++) {
            filePath.append("/").append(selectedNode.getPath()[i]);
        }

        File selectedFile = new File(filePath.toString());
        if (selectedFile.isFile()) {
            tabbedPane.openFileInNewTab(selectedFile, tabbedPane);
        }
    }

    private void saveLastViewedFile(FileTab tabbedPane) {
        int selectedIndex = tabbedPane.getSelectedIndex();
        if (selectedIndex >= 0) {
            String selectedFilePath = tabbedPane.tabFilePathMap.get(selectedIndex);
            if (selectedFilePath != null) {
                filesRepository.saveLastViewedFile(selectedFilePath);
            }
        }
    }

    private void createFileNodes(File directory, DefaultMutableTreeNode node) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                var fileData = new FileData(file.getName(), file.isDirectory() ? FileType.Folder : FileType.File);
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(fileData);
                node.add(childNode);
                if (file.isDirectory()) {
                    createFileNodes(file, childNode);
                }
            }
        }
    }

    private void reloadTree() {
        SwingUtilities.invokeLater(() -> {
            root.removeAllChildren();
            createFileNodes(new File(projectDir), root);
            ((DefaultTreeModel) getModel()).reload();
        });
    }

    private void watchDirectoryChanges() {
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            Path path = Paths.get(projectDir);
            registerAll(path, watchService);

            while (true) {
                WatchKey key = watchService.poll(300, TimeUnit.MILLISECONDS);
                if (key != null) {
                    boolean updated = false;
                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();
                        if (kind == StandardWatchEventKinds.ENTRY_CREATE ||
                            kind == StandardWatchEventKinds.ENTRY_DELETE ||
                            kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                            updated = true;
                        }
                    }
                    key.reset();

                    if (updated) {
                        reloadTree();
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void registerAll(Path start, WatchService watchService) throws IOException {
        try (var stream = Files.walk(start)) {
            stream.filter(Files::isDirectory).forEach(dir -> {
                try {
                    dir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
