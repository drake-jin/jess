
// 31 July 2003

// WEW note: new help system for the JESS commands
// faster than the JavaHelp API
// based on TreeDemo.java from the Sun Java Tutorial

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.TreeSelectionModel;
import java.net.URL;
import java.io.IOException;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JFrame;
import java.awt.*;

public class JessCommandsHelpFrame extends JFrame {
    private JEditorPane htmlPane;
    private static boolean DEBUG = false;
    private URL helpURL;

    // Optionally play with line styles.  Possible values are
    // "Angled", "Horizontal", and "None" (the default).
    private boolean playWithLineStyle = false;
    private String lineStyle = "Angled";

    public JessCommandsHelpFrame() {
        super("JESS Commands Help");

        // Create the nodes.
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("List of JESS commands.");
        createNodes(top);

        // Create a tree that allows one selection at a time.
        final JTree tree = new JTree(top);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        // Listen for when the selection changes.
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();

                if (node == null) return;

                Object nodeInfo = node.getUserObject();
                if (node.isLeaf()) {
                    BookInfo book = (BookInfo)nodeInfo;
                    displayURL(book.bookURL);
                    if (DEBUG) {
                        System.out.print(book.bookURL + ":  \n    ");
                    }
                } else {
                    displayURL(helpURL);
                }
                if (DEBUG) {
                    System.out.println(nodeInfo.toString());
                }
            }
        });

        if (playWithLineStyle) {
            tree.putClientProperty("JTree.lineStyle", lineStyle);
        }

        // Create the scroll pane and add the tree to it.
        JScrollPane treeView = new JScrollPane(tree);

        // Create the HTML viewing pane.
        htmlPane = new JEditorPane();
        htmlPane.setEditable(false);
        initHelp();
        JScrollPane htmlView = new JScrollPane(htmlPane);

        // Add the scroll panes to a split pane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(treeView);
        splitPane.setBottomComponent(htmlView);

        Dimension minimumSize = new Dimension(100, 50);
        htmlView.setMinimumSize(minimumSize);
        treeView.setMinimumSize(minimumSize);
        splitPane.setDividerLocation(100); // XXX: ignored in some releases
                                           // of Swing. bug 4101306
        // workaround for bug 4101306:
        // treeView.setPreferredSize(new Dimension(100, 100));

        splitPane.setPreferredSize(new Dimension(500, 300));

        // Add the split pane to this frame.
        getContentPane().add(splitPane, BorderLayout.CENTER);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int scrnWidth  = 3 * screenSize.width  / 4;
        int scrnHeight = 3 * screenSize.height / 4;

        setSize(scrnWidth, scrnHeight);
        setLocation((screenSize.width - scrnWidth) / 2, (screenSize.height - scrnHeight) / 2);
        show();
        setVisible(true);
    }

    private class BookInfo {
        public String bookName;
        public URL bookURL;
        public String prefix = "file:" +
                               System.getProperty("user.dir") +
                               System.getProperty("file.separator");

        public BookInfo(String book, String filename) {
            bookName = book;
            try {
                bookURL = new URL(prefix + filename);
            } catch (java.net.MalformedURLException exc) {
                System.err.println("Attempted to create a BookInfo with a bad URL: " + bookURL);
                bookURL = null;
            }
        }

        public String toString() {
            return bookName;
        }
    }

    private void initHelp() {
        String s = null;
        try {
            s = "file:" +
                System.getProperty("user.dir") +
                System.getProperty("file.separator") +
                "jcmdhelp/jesscmds.html";

            if (DEBUG) {
                System.out.println("Help URL is " + s);
            }
            helpURL = new URL(s);
            displayURL(helpURL);
        } catch (Exception e) {
            System.err.println("Couldn't create help URL: " + s);
        }
    }

    private void displayURL(URL url) {
        try {
            htmlPane.setPage(url);
        } catch (IOException e) {
            System.err.println("Attempted to read a bad URL: " + url);
        }
    }

    private void createNodes(DefaultMutableTreeNode top) {
        DefaultMutableTreeNode category = null;
        DefaultMutableTreeNode book = null;

        // build the File Menu
        category = new DefaultMutableTreeNode("Jess Functions Grouped by Usage");
        top.add(category);

        //Load the large list of functions
        book = new DefaultMutableTreeNode(new BookInfo("Miscellaneous functions", "jcmdhelp/misc.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("File I/O functions", "jcmdhelp/fileio.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("Functions for interacting with Java code", "jcmdhelp/interaction.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("Logical functions", "jcmdhelp/logical.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("Debugging functions", "jcmdhelp/debugging.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("Defqueries", "jcmdhelp/defquery.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("Control structures", "jcmdhelp/control.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("Pretty-print functions", "jcmdhelp/pprint.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("Commands for the Rules Engine", "jcmdhelp/rulescmds.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("Mathematical Functions", "jcmdhelp/math.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("Functions that query the type of Value", "jcmdhelp/value.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("String Functions", "jcmdhelp/string.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("Functions for working with multifields", "jcmdhelp/multifield.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("Functions for working with facts", "jcmdhelp/facts.html"));
        category.add(book);
    }

    public void UpdateWindow() {
        setState(Frame.NORMAL);
        setVisible(true);
    }
}

