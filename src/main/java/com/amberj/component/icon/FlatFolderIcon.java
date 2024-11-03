package com.amberj.component.icon;

import com.formdev.flatlaf.icons.FlatAbstractIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

public class FlatFolderIcon extends FlatAbstractIcon {

    private static final Color ICON_COLOR = Color.WHITE; // Change as needed

    public FlatFolderIcon() {
        super(22, 18, ICON_COLOR); // Adjusted height to 18 for a shorter icon
    }

    @Override
    protected void paintIcon(Component c, Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(ICON_COLOR);

        Path2D path = new Path2D.Double();
        path.moveTo(18, 17);           // Start at the bottom-right, shifted x by +2
        path.lineTo(18, 8);            // Move up to the top-right, shifted x by +2
        path.quadTo(18, 6.5, 16, 6.5); // Top-right corner curve, shifted x by +2
        path.lineTo(11, 6.5);          // Move left to the folder flap, shifted x by +2
        path.quadTo(9.5, 6.5, 8.8, 5.5); // Curved top-left for the folder flap, shifted x by +2
        path.lineTo(7.5, 4);           // Move to the top-left corner, shifted x by +2
        path.quadTo(7, 3.5, 5.5, 3.5); // Upper-left corner curve, shifted x by +2
        path.lineTo(4, 3.5);           // Move down the left side, shifted x by +2
        path.quadTo(2.5, 3.5, 2.5, 5.5); // Lower-left corner curve, shifted x by +2
        path.lineTo(2.5, 17);          // Move down to the bottom-left corner, shifted x by +2
        path.lineTo(18, 17);           // Draw the bottom edge, shifted x by +2
        path.lineTo(18, 15);           // Move up slightly
        path.quadTo(19, 15, 19, 17);   // Smooth transition at the bottom-right corner, shifted x by +2

        g.draw(path);
    }
}
