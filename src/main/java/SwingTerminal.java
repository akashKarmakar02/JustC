import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SwingTerminal extends JFrame {

    private JTextArea terminalOutput;
    private JTextField terminalInput;

    public SwingTerminal() {
        setTitle("Swing Terminal");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        terminalOutput = new JTextArea();
        terminalOutput.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(terminalOutput);

        terminalInput = new JTextField();
        terminalInput.addActionListener(e -> {
            String command = terminalInput.getText();
            processCommand(command);
            terminalInput.setText("");
        });

        add(scrollPane, BorderLayout.CENTER);
        add(terminalInput, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void processCommand(String command) {
        terminalOutput.append(command + "\n");
        // Implement command execution logic hereInP
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SwingTerminal();
            }
        });
    }
}