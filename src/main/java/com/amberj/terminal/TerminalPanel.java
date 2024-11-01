package com.amberj.terminal;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.jediterm.app.JediTerm;
import com.jediterm.app.TtyConnectorWaitFor;
import com.jediterm.terminal.CursorShape;
import com.jediterm.terminal.TtyConnector;
import com.jediterm.terminal.ui.JediTermWidget;
import com.jediterm.terminal.ui.TerminalWidget;
import com.jediterm.terminal.ui.settings.SettingsProvider;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.function.IntConsumer;

public abstract class TerminalPanel {
    public static final Logger LOG = LoggerFactory.getLogger(TerminalPanel.class);

    private final JediTermWidget myWidget;

    protected void openSession(TerminalWidget terminal) {
        if (terminal.canOpenSession()) {
            openSession(terminal, createTtyConnector());
        }
    }

    public void openSession(TerminalWidget terminal, TtyConnector ttyConnector) {
        JediTermWidget session = terminal.createTerminalSession(ttyConnector);
        if (ttyConnector instanceof JediTerm.LoggingPtyProcessTtyConnector) {
            ((JediTerm.LoggingPtyProcessTtyConnector) ttyConnector).setWidget(session);
        }
        session.start();
    }

    public abstract TtyConnector createTtyConnector();

    protected TerminalPanel() {
        try {
            UIManager.setLookAndFeel(new FlatMacDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        myWidget = createTerminalWidget(new JustTerminalSettingsProvider());

        myWidget.setBackground(Color.decode("#1E1F22"));

        myWidget.getTerminalPanel().getWindowForeground();

        openSession(myWidget);
        onTermination(myWidget, exitCode -> myWidget.close());
    }

    public JediTermWidget getTerminal() {
        return this.myWidget;
    }

    private static void onTermination(@NotNull JediTermWidget widget, @NotNull IntConsumer terminationCallback) {
        new TtyConnectorWaitFor(widget.getTtyConnector(),
                widget.getExecutorServiceManager().getUnboundedExecutorService(),
                terminationCallback);
    }

    protected JediTermWidget createTerminalWidget(@NotNull SettingsProvider settingsProvider) {
        JediTermWidget widget = new JediTermWidget(settingsProvider);
        widget.getTerminalPanel().setDefaultCursorShape(CursorShape.BLINK_VERTICAL_BAR);
        return widget;
    }

}
