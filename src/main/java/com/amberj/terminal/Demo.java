package com.amberj.terminal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Demo {
    public static String runShellCommand(String command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        // Split the command to handle spaces and arguments correctly
        processBuilder.command("sh", "-c", command);

        Process process = processBuilder.start();

        // Capture the output
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }

        // Wait for the process to finish
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IOException("Command exited with code " + exitCode);
        }

        return output.toString();
    }

    public static void main(String[] args) {
        try {
            String result = runShellCommand("ls -la");
            System.out.println("Command Output:\n" + result);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
