package com.amberj.terminal;

import com.amberj.lib.ProjectDataLib;
import com.amberj.lib.WindowProvider;
import com.jediterm.app.JediTerm;
import com.jediterm.terminal.TtyConnector;
import com.pty4j.PtyProcess;
import com.pty4j.PtyProcessBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JustTerminal extends TerminalPanel {

    @Override
    public TtyConnector createTtyConnector() {
        try {
            Map<String, String> envs = configureEnvironmentVariables();

            String[] command;
            if (isWindows()) {
                command = new String[] { "powershell.exe" };
            } else {
                String shell = envs.getOrDefault("SHELL", "/bin/bash");
                if (isMacOS()) {
                    command = new String[] { shell, "--login" };
                } else {
                    command = new String[] { shell };
                }
            }
            String projectDir = new ProjectDataLib().getProperty(ProjectDataLib.ProjectDataKey.PROJECT_DIRECTORY);

            String workingDirectory = Path.of(projectDir != null ? projectDir : ".").toAbsolutePath().normalize().toString();

            PtyProcess process = new PtyProcessBuilder()
                    .setDirectory(workingDirectory)
                    .setInitialColumns(120)
                    .setInitialRows(20)
                    .setCommand(command)
                    .setEnvironment(envs)
                    .setConsole(false)
                    .setUseWinConPty(true)
                    .start();

            return new JediTerm.LoggingPtyProcessTtyConnector(process, StandardCharsets.UTF_8, Arrays.asList(command));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

    }

    private Map<String, String> configureEnvironmentVariables() {
        Map<String, String> envs = new HashMap<>(System.getenv());
        if (isMacOS()) {
            envs.put("LC_CTYPE", StandardCharsets.UTF_8.name());
        }
        if (!isWindows()) {
            envs.put("TERM", "xterm-256color");
        }
        return envs;
    }

    boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().startsWith("windows");
    }

    boolean isMacOS() {
        return System.getProperty("os.name").toLowerCase().startsWith("mac");
    }

    public void runCommand(String command) {
        if (this.getTerminal() != null) {
            try {
                this.getTerminal().getTtyConnector().write(command + "\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            LOG.warn("Terminal is not initialized. Cannot run command: {}", command);
        }
    }

}