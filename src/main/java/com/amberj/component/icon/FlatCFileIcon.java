package com.amberj.component.icon;

import com.formdev.flatlaf.icons.FlatAbstractIcon;
import javax.swing.UIManager;
import java.awt.*;

public class FlatCFileIcon extends FlatAbstractIcon {

    public FlatCFileIcon() {
        super(15, 15, UIManager.getColor("Objects.Gray"));  // Set background color
    }

    @Override
    protected void paintIcon(Component c, Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(Color.WHITE);
        Font font = g.getFont().deriveFont(Font.BOLD, 11f);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();

        // Center the "C" text within the icon
        String text = "C";
        int textX = (15 - fm.stringWidth(text)) / 2;
        int textY = (15 + fm.getAscent() - fm.getDescent()) / 2;

        g.drawString(text, textX, textY);
    }
}
