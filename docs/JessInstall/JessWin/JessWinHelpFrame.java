
// 31 July 2003

// WEW note: new help system for JessWin
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

public class JessWinHelpFrame extends JFrame {
    private JEditorPane htmlPane;
    private static boolean DEBUG = false;
    private URL helpURL;

    // Optionally play with line styles.  Possible values are
    // "Angled", "Horizontal", and "None" (the default).
    private boolean playWithLineStyle = false;
    private String lineStyle = "Angled";

    public JessWinHelpFrame() {
        super("JessWin Help");

        // Create the nodes.
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("The JessWin Menu");
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

        int scrnWidth  = 3 * screenSize.width  / 8;
        int scrnHeight = 3 * screenSize.height / 8;

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
                "jwhelp/JessWinHelp.html";

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
        category = new DefaultMutableTreeNode("File Menu");
        top.add(category);

        book = new DefaultMutableTreeNode(new BookInfo("Load CLP", "jwhelp/loadclp.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("Save Session", "jwhelp/savesession.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("Jess Editor", "jwhelp/jesseditor.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("Recent Commands", "jwhelp/recentcommands.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("Print", "jwhelp/print.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("Print Preview", "jwhelp/printpreview.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("Page Setup", "jwhelp/pagesetup.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("Exit", "jwhelp/exit.html"));
        category.add(book);

        // Execute Menu
        category = new DefaultMutableTreeNode("Execute Menu");
        top.add(category);

        book = new DefaultMutableTreeNode(new BookInfo("Start", "jwhelp/start.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("Reset", "jwhelp/reset.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("Run", "jwhelp/run.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("Step", "jwhelp/step.html"));
        category.add(book);
        // book = new DefaultMutableTreeNode(new BookInfo("View Rete Net", "jwhelp/viewretenet.html"));
        // category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("Clear", "jwhelp/clear.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("Halt", "jwhelp/halt.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("Version", "jwhelp/version.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("Strategy...", "jwhelp/strategy.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("Watch...", "jwhelp/watch.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("Exit Jess", "jwhelp/exitjess.html"));
        category.add(book);

        // Watch Menu
        category = new DefaultMutableTreeNode("Watch Menu");
        top.add(category);

        book = new DefaultMutableTreeNode(new BookInfo("Watch Facts", "jwhelp/facts.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("Watch Rules", "jwhelp/rules.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("Watch Agenda", "jwhelp/agenda.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("Watch Defglobals", "jwhelp/defglobals.html"));
        category.add(book);
        // book = new DefaultMutableTreeNode(new BookInfo("Watch Rete Net", "jwhelp/retenet.html"));
        // category.add(book);

        // Options Menu
        category = new DefaultMutableTreeNode("Options Menu");
        top.add(category);

        book = new DefaultMutableTreeNode(new BookInfo("Font", "jwhelp/jwfont.html"));
        category.add(book);

        // Help Menu
        category = new DefaultMutableTreeNode("Help Menu");
        top.add(category);

        book = new DefaultMutableTreeNode(new BookInfo("JessWin Menu Help", "jwhelp/jesswinmenu.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("Jess Commands", "jwhelp/jesscommands.html"));
        category.add(book);
        book = new DefaultMutableTreeNode(new BookInfo("About", "jwhelp/about.html"));
        category.add(book);
    }

    public void UpdateWindow() {
        setState(Frame.NORMAL);
        setVisible(true);
    }
}
