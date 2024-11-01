package com.amberj.feature;

import com.amberj.lib.WindowProvider;
import com.amberj.terminal.JustTerminal;

import java.io.*;

public class Compiler {

    private final JustTerminal terminal = WindowProvider.getConsole();

    public void compileAndRunCCode(String path) {
        try {
            WindowProvider.showBottomPanel();
            terminal.getTerminal().getTtyConnector().write("gcc " + path + " -o " + path.replace(".c", "") + " && " + path.replace(".c", "") + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
