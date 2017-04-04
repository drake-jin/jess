
// WEW note:  used to display the facts, rules, agenda or defglobals
// 1.  reformat                                  (done) 01 August 2003
// 2.  added call to new method in JessWin       (done) 01 August 2003
//         jessWin.updateWatchWindowStatus(wwTitle, false);
//     updates JessWin on the status of a window (open or closed)
// 3.  cleanup
// 4.  test extensively

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WatchWindow extends JFrame {
    private static final boolean DEBUG = false;
    private JTextArea ta = new JTextArea(200, 80);
    private JScrollPane tasp = new JScrollPane(ta);
    private JPanel panel = new JPanel();
    private String wwTitle = new String();
    private int wwLeft   = 0;
    private int wwTop    = 0;
    private int wwWidth  = 150;
    private int wwHeight = 100;
    private Font textFont  = new Font("Monospaced", Font.BOLD, 16);
	private JessWin jessWin;
	
    public WatchWindow(JessWin jw, final String title, int left, int top, int width, int height) {
    	jessWin = jw;
        wwTitle = title;
        wwLeft = left;
        wwTop = top;
        wwWidth = width;
        wwHeight = height;

        // add a Window Listener to the frame
        // later modifications to this area might include such things as saving the current location and size of the
        // watch window, just testing at this point
        addWindowListener(new WindowAdapter() {
            public void windowIconified(WindowEvent e) {
                if (DEBUG)  System.out.println("Iconifying " + title + " Window");
            }

            public void windowDeiconified(WindowEvent e) {
                if (DEBUG)  System.out.println("Deiconifying " + title + " Window");
            }

            public void windowClosing(WindowEvent e) {
                jessWin.updateWatchWindowStatus(wwTitle, false);
            }
        });

        addComponentListener(new ComponentListener() {
            public void componentHidden(ComponentEvent e) {
              if (DEBUG)  System.out.println(title + " WatchWindow is hidden");
            }

            public void componentMoved(ComponentEvent e) {
                if (DEBUG) {
                    System.out.println("WatchWindow was moved");
                    System.out.println("width: " + wwWidth + " height: " + wwHeight);
                    System.out.println("left:  " + wwLeft  + " top:    " + wwTop);
                }
                // update the parameters here later
            }

            public void componentResized(ComponentEvent e) {
                if (DEBUG)  System.out.println("WatchWindow was resized");
            }

            public void componentShown(ComponentEvent e) {
                if (DEBUG)  System.out.println("WatchWindow is shown");
            }
        });

        panel.setLayout(new BorderLayout());

        ta.setEditable(false);
        ta.setFont(textFont);
        panel.add(tasp, "Center");

        getContentPane().add(panel);

        DisplayWindow();
    }

    public void DisplayWindow() {
        if (DEBUG)  System.out.println("Frame: " + wwTitle);

        setTitle(wwTitle);
        setSize(wwWidth, wwHeight);
        setLocation(wwLeft, wwTop);
        if (DEBUG)  System.out.println("width: " + wwWidth + " height: " + wwHeight);
        if (DEBUG)  System.out.println("left:  " + wwLeft  + " top:    " + wwTop);

        show();
        setVisible(true);
    }

    public void UpdateWindow() {
        setState(Frame.NORMAL);
        setVisible(true);
    }

    public void SetText(String text) {
        ta.setText(text);
    }
}

