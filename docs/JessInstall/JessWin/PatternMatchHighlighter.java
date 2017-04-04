
// 31 July 2003

// modification of a program from Kim Topley's book:
// Core Swing: Advanced Programming

import javax.swing.text.*;
import java.awt.*;

public class PatternMatchHighlighter extends DefaultHighlighter {
    public PatternMatchHighlighter(Color c) {
        painter = (c == null ? sharedPainter : new PatternMatchHighlightPainter(c));
    }

    // Convenience method to add a highlight with
    // the default painter.
    public Object addHighlight(int p0, int p1) throws BadLocationException {
        return addHighlight(p0, p1, painter);
    }

    public void setDrawsLayeredHighlights(boolean newValue) {
        // Illegal if false - we only support layered highlights
        if (newValue == false) {
            throw new IllegalArgumentException("PatternMatchHighlighter only draws layered highlights");
        }
        super.setDrawsLayeredHighlights(true);
    }

    // Painter for the highlights
    public static class PatternMatchHighlightPainter extends LayeredHighlighter.LayerPainter {
        public PatternMatchHighlightPainter(Color c) {
            color = c;
        }

        public void paint(Graphics g, int offs0, int offs1, Shape bounds, JTextComponent c) {
            // Do nothing: this method will never be called
        }

        public Shape paintLayer(Graphics g, int offs0, int offs1, Shape bounds, JTextComponent c, View view) {
            g.setColor(color == null ? c.getSelectionColor() : color);

            Rectangle alloc = null;
            if (offs0 == view.getStartOffset() && offs1 == view.getEndOffset()) {
                if (bounds instanceof Rectangle) {
                    alloc = (Rectangle)bounds;
                } else {
                    alloc = bounds.getBounds();
                }
            } else {
                try {
                    Shape shape = view.modelToView(offs0, Position.Bias.Forward,
                                                   offs1, Position.Bias.Backward,
                                                   bounds);
                    alloc = (shape instanceof Rectangle) ? (Rectangle)shape : shape.getBounds();
                } catch (BadLocationException e) {
                    return null;
                }
            }

            FontMetrics fm = c.getFontMetrics(c.getFont());
            int baseline = alloc.y + alloc.height - fm.getDescent() + 1;

            g.fillRect(alloc.x, alloc.y, alloc.x + alloc.width - 5, baseline);
            return alloc;
        }

        protected Color color; // The color for the underline
    }

    // Shared painter used for default highlighting
    protected static final Highlighter.HighlightPainter sharedPainter = new PatternMatchHighlightPainter(null);

    // Painter used for this highlighter
    protected Highlighter.HighlightPainter painter;
}
