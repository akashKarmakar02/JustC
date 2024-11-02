package com.amberj.feature;

import org.fife.rsta.ac.c.CLanguageSupport;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.CompletionCellRenderer;
import org.fife.ui.autocomplete.CompletionProvider;

import javax.swing.*;

public class JustCLanguageSupport extends CLanguageSupport {

    public JustCLanguageSupport() {
        super();
        this.setAutoActivationEnabled(true);
        this.setAutoActivationDelay(0);
        this.setAutoCompleteEnabled(true);
        this.setParameterAssistanceEnabled(false);
        this.setShowDescWindow(true);
    }

    @Override
    protected ListCellRenderer<Object> createDefaultCompletionCellRenderer() {
        final var s = 5;

        return new CompletionCellRenderer();
    }

    @Override
    protected AutoCompletion createAutoCompletion(CompletionProvider p) {
        return super.createAutoCompletion(p);
    }
}
