package com.amberj.terminal;

import com.jediterm.terminal.TerminalColor;
import com.jediterm.terminal.TextStyle;
import com.jediterm.terminal.ui.settings.DefaultSettingsProvider;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class JustTerminalSettingsProvider extends DefaultSettingsProvider {

    @Override
    public @NotNull TerminalColor getDefaultBackground() {
        return new TerminalColor(30, 31, 32);
    }

    @Override
    public @NotNull TerminalColor getDefaultForeground() {
        return new TerminalColor(200, 200, 200);
    }

    @Override
    public Font getTerminalFont() {
        return new Font("Monospaced", Font.PLAIN, 16);
    }

    @Override
    public @NotNull TextStyle getFoundPatternColor() {
        return new TextStyle(TerminalColor.WHITE, TerminalColor.rgb(255, 255, 0));
    }
}
