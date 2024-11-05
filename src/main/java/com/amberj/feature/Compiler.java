package com.amberj.feature;

import com.amberj.lib.WindowProvider;
import com.amberj.terminal.JustTerminal;

import java.io.*;

public class Compiler {

    private final JustTerminal terminal = WindowProvider.getConsole();

    public void compileAndRunCCode(String path) {
        try {
            WindowProvider.showBottomPanel();
            String executablePath = path.replace(".c", "");

            String command;
            if (isWindows()) {
                command = "gcc " + path + " -o " + executablePath + " && " + executablePath;
            } else {
                command = "gcc " + path + " -o " + executablePath + " && ./" + executablePath;
            }

            terminal.getTerminal().getTtyConnector().write(command + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}
