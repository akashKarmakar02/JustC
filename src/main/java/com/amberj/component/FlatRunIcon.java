package com.amberj.component;

import com.formdev.flatlaf.icons.FlatAbstractIcon;
import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import javax.swing.UIManager;

public class FlatRunIcon extends FlatAbstractIcon {
    private Path2D path;

    public FlatRunIcon() {
        super(16, 16, UIManager.getColor("Objects.Green")); // Change color to green for "run"
    }

    protected void paintIcon(Component c, Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.setStroke(new BasicStroke(1.5F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        if (this.path == null) {
            this.path = new Path2D.Float();
            this.path.moveTo(4, 3);  // Top left of the triangle
            this.path.lineTo(4, 13); // Bottom left of the triangle
            this.path.lineTo(12, 8); // Right point of the triangle
            this.path.closePath();   // Close the triangle path
        }

        g.setColor(UIManager.getColor("Objects.Green"));  // Use the icon's color
        g.fill(this.path);        // Fill the triangle for a solid play icon
    }
}
