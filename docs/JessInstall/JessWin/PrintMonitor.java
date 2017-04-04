
// 31 July 2003

// this is from Brett Spells's book, "Professional Java Programming"
// published by Wrox Press

import java.awt.print.*;
import javax.swing.*;

/**
 * Creating an instance of this class and printing it allows it to
 * add a status dialog during printing. The print requests are
 * simply delegated to the Pageable that actually contains the
 * data to be printed, but by intercepting those calls, we can update
 * the page number displayed in our dialog so that it indicates
 * which page is currently being displayed.
 */
public class PrintMonitor implements Pageable {
    protected PrinterJob printerJob;
    protected Pageable pageable;
    protected JOptionPane optionPane;
    protected JDialog statusDialog;

    public PrintMonitor(Pageable p) {
        pageable = p;
        printerJob = PrinterJob.getPrinterJob();
        String[] options = {"Cancel"};
        optionPane = new JOptionPane("",
            JOptionPane.INFORMATION_MESSAGE,
            JOptionPane.CANCEL_OPTION,
            null, options);
        statusDialog = optionPane.createDialog(null, "Printer Job Status");
    }

    /**
     * Create a new thread and have it call the print() method.
     * This ensures that the AWT event thread will be able to handle
     * the Cancel button if it is pressed, and can cancel the print job.
     */
    public void performPrint(boolean showDialog) throws PrinterException {
        printerJob.setPageable(this);
        if (showDialog) {
            boolean isOk = printerJob.printDialog();
            if (!isOk) return;
        }
        optionPane.setMessage("Initiating printer job...");
        Thread t = new Thread(new Runnable() {
            public void run() {
                statusDialog.setVisible(true);
                if (optionPane.getValue() != JOptionPane.UNINITIALIZED_VALUE) {
                    printerJob.cancel();
                }
            }
        });
        t.start();
        printerJob.print();
        statusDialog.setVisible(false);
    }

    public int getNumberOfPages() {
        return pageable.getNumberOfPages();
    }

    /*
     * Update our dialog message and delegate the getPrintable() call
     */
    public Printable getPrintable(int index) {
        optionPane.setMessage("Printing page " + (index + 1));
        return pageable.getPrintable(index);
    }

    public PageFormat getPageFormat(int index) {
        return pageable.getPageFormat(index);
    }
}
