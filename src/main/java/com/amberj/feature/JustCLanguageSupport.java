package com.amberj.feature;

import com.formdev.flatlaf.extras.components.FlatList;
import com.formdev.flatlaf.extras.components.FlatPopupMenu;
import org.fife.rsta.ac.c.CCompletionProvider;
import org.fife.rsta.ac.c.CLanguageSupport;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.CompletionCellRenderer;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.VariableCompletion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class JustCLanguageSupport extends CLanguageSupport {

    public JustCLanguageSupport() {
        super();
        System.out.println("iniy");
        this.setAutoActivationEnabled(true);
        this.setAutoActivationDelay(0);
        this.setAutoCompleteEnabled(true);
        this.setParameterAssistanceEnabled(true);
        this.setShowDescWindow(true);
    }

    @Override
    protected ListCellRenderer<Object> createDefaultCompletionCellRenderer() {
        return null;
    }

    @Override
    protected AutoCompletion createAutoCompletion(CompletionProvider p) {
        return super.createAutoCompletion(p);
    }
}
