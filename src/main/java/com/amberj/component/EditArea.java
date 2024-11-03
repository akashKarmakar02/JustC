package com.amberj.component;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class EditArea extends RSyntaxTextArea {
    
    public EditArea(String content, Runnable saveFunc) {
        setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_C);
        setCodeFoldingEnabled(true);
        setAntiAliasingEnabled(true);
        setBracketMatchingEnabled(true);
        setCloseMarkupTags(true);
        setShowMatchedBracketPopup(true);
        setRoundedSelectionEdges(true);
        setTabsEmulated(true);
        setText(content);
        setFont(new Font("SansSerif", Font.PLAIN, 25));

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S) {
                    saveFunc.run();
                }
            }
        });
    }
    
}
