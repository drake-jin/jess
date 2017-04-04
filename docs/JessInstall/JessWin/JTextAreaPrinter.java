
// 31 July 2003

// this is a modification of 'TablePrinter.java' from
// Brett Spells's book, "Professional Java Programming"
// published by Wrox Press

import java.awt.*;
import java.awt.print.*;
import javax.swing.*;

public class JTextAreaPrinter extends JComponent implements Printable, Pageable {
    protected JTextArea jta;
    protected PageFormat pageFormat;
    int rowHeight = 0;
    int rowsPerPage;
    int lfcount = 0;
    int positionX = 0;
    int positionY = 0;

    public JTextAreaPrinter(JTextArea jTextArea, PageFormat pf) {
        jta = jTextArea;
        pageFormat = pf;
    }

    /**
     * printing is done here
     */
    public int print(Graphics g, PageFormat pf, int index) {
        Dimension size = null;
        //  Get the textarea's preferred size
        if ((jta.getWidth() == 0) || (jta.getHeight() == 0)) {
            jta.setSize(jta.getPreferredSize());
        }
        int jtaWidth = jta.getWidth();
        int jtaHeight = jta.getHeight();

        // adjust jtaHeight to only use pages with text
        // add the '1' so that single rows are printed
        jtaHeight = (lfcount + 1) * rowHeight;

        //  Loop until we have printed the entire text area
        int pageIndex = 0;
        while (positionY < jtaHeight) {
            positionX = 0;
            positionY = index * rowsPerPage * rowHeight;
            size = getPrintSize(positionX, positionY);
            paintTextArea(g, positionX, positionY, size);
            pageIndex++;
            return Printable.PAGE_EXISTS;
        }
        return Printable.NO_SUCH_PAGE;
    }

    /**
     * Calculate how much of the text area will fit on a page without
     * causing a row or column to be split across two pages
     */
    protected Dimension getPrintSize(int positionX, int positionY) {
        int printWidth;
        int printHeight;

        // always return a certain dimension object
        printWidth = jta.getWidth();
        printHeight = rowsPerPage * rowHeight;
        return new Dimension(printWidth, printHeight);
    }

    /**
     * paint or print part of the text area
     */
    protected void paintTextArea(Graphics g, int positionX, int positionY, Dimension size) {
        int offsetX = (int)(pageFormat.getImageableX());
        int offsetY = (int)(pageFormat.getImageableY());

        g.translate(offsetX - positionX, offsetY - positionY);
        g.clipRect(positionX, positionY, size.width, size.height);
        jta.paint(g);
    }

    /**
     * Calculate the number of pages it will take to print the entire text area
     */
    public int getNumberOfPages() {
        FontMetrics fm = getFontMetrics(jta.getFont());
        rowHeight = fm.getHeight();
        int maxWidth = (int)(pageFormat.getImageableWidth());
        int maxHeight = (int)(pageFormat.getImageableHeight());

        rowsPerPage = maxHeight / rowHeight;

        // calculate the number of linefeeds in the text area, not the number of rows
        byte[]  databytes = jta.getText().getBytes();

        lfcount = 0;
        int ivalue;
        for (int i = 0; i < databytes.length; i++) {
            ivalue = (int)databytes[i];
            if (ivalue == 10)
                lfcount++;
        }

        Dimension size = null;
        int jtaWidth = jta.getWidth();
        int jtaHeight = jta.getHeight();
        int local_positionX = 0;
        int local_positionY = 0;

        int pageIndex = 0;

        // adjust jtaHeight to only use pages with text
        jtaHeight = (lfcount + 1) * rowHeight;

        while (local_positionY < jtaHeight) {
            local_positionX = 0;
            size = getPrintSize(local_positionX, local_positionY);
            pageIndex++;
            local_positionY += rowsPerPage * rowHeight;
        }
        return pageIndex;
    }

    public Printable getPrintable(int index) {
        return this;
    }

    public PageFormat getPageFormat(int index) {
        return pageFormat;
    }
}
