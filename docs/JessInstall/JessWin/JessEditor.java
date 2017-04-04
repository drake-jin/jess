
// modification of an old editor based on code from Topley's book "Core Java Foundation Classes"

// TODO 
// 1.  reformat                 (done) 01 August 2003  
// 2.  do minor code cleanup    (done) 02 August 2003
// 3.  add more hot keys
// 4.  test the permissions for file read and write on Linux

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.awt.print.*;
import javax.swing.text.*;
import javax.swing.undo.*;

public class JessEditor extends JFrame {
    private boolean DEBUG = false;
    private boolean isStandAlone = true;  // was static
    private static String versionNumber = new String("Version 1.06");

    private PrinterJob pj;
    private PageFormat mPageFormat;

    Highlighter h;

    private JessWin jw;  // was static

    private static int lastlength = 0;

    // clean up the constructors later
    public JessEditor() {
        this.jw = null;

        buildGUI();
    }

    public JessEditor(boolean isStandAlone) {
        this.isStandAlone = isStandAlone;
        buildGUI();
    }

    public JessEditor(boolean isStandAlone, final JessWin jw) {
        this.isStandAlone = isStandAlone;
        this.jw = jw;

        buildGUI();
    }

    public void buildGUI() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        // make a menubar
        JMenuBar mb = new JMenuBar();

        // the File menu starts here
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        mb.add(fileMenu);

        loadClpItem = new JMenuItem("Load CLIP", 'L');
        newItem = new JMenuItem("New...", 'N');
        openItem = new JMenuItem("Open...", 'O');

        closeItem = new JMenuItem("Close", 'L');
        clearItem = new JMenuItem("Clear", 'C');
        fileMenu.add(loadClpItem);
        fileMenu.addSeparator();
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(clearItem);

        saveItem = new JMenuItem("Save", 'S');
        saveAsItem = new JMenuItem("Save As...", 'A');
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();

        fileListMenu = new JMenu("Recent Files");
        fileListMenu.setMnemonic('R');
        fileMenu.add(fileListMenu);
        emptyListItem = new JMenuItem("Empty");
        emptyListItem.setEnabled(false);
        fileListMenu.add(emptyListItem);
        fileMenu.addSeparator();

        printItem = new JMenuItem("Print");
        printPreviewerItem = new JMenuItem("Print Preview");
        pageSetupItem = new JMenuItem("Page Setup");
        printItem.setMnemonic('P');
        printPreviewerItem.setMnemonic('R');
        pageSetupItem.setMnemonic('U');
        pageSetupItem.setEnabled(true);
        fileMenu.add(printItem);
        fileMenu.add(printPreviewerItem);
        fileMenu.add(pageSetupItem);
        fileMenu.addSeparator();

        exitItem = new JMenuItem("Exit", 'x');
        fileMenu.add(exitItem);

        // the Edit menu starts here
        editMenu = new JMenu("Edit");
        editMenu.setMnemonic('E');
        mb.add(editMenu);

        // undo/redo feature
        undoItem = new JMenuItem("Undo", 'Z');
        redoItem = new JMenuItem("Redo", 'Y');
        undoItem.setEnabled(false);
        redoItem.setEnabled(false);
        editMenu.add(undoItem);
        editMenu.add(redoItem);
        editMenu.addSeparator();

        // nothing to cut or copy when first coming up
        // so disable the menu (leave paste enabled)
        // enable them if some text has been selected (later....)
        cutItem = new JMenuItem("Cut", 'T');
        cutItem.setEnabled(false);
        editMenu.add(cutItem);
        copyItem = new JMenuItem("Copy", 'C');

        copyItem.setEnabled(false);
        editMenu.add(copyItem);

        pasteItem = new JMenuItem("Paste", 'P');
        editMenu.add(pasteItem);

        // new Jess menu starts here
        jessMenu = new JMenu("JessWin");
        jessMenu.setMnemonic('J');
        mb.add(jessMenu);
        startJessWinItem = new JMenuItem("Start JessWin", 'S');
        jessItem = new JMenuItem("Send to JessWin", 'W');
        startJessWinItem.setEnabled(true);
        jessItem.setEnabled(false);
        jessMenu.add(startJessWinItem);
        jessMenu.add(jessItem);

        // the options menu starts here
        optionsMenu = new JMenu("Options");
        optionsMenu.setMnemonic('O');
        mb.add(optionsMenu);
        selectFontItem = new JMenuItem("Font", 'F');
        optionsMenu.add(selectFontItem);
        optionsMenu.addSeparator();

        // added the new Parentheses Matching functionality
        // patternMatchParenthesesItem = new JCheckBoxMenuItem("Match Parentheses");
        // patternMatchFillTextItem = new JCheckBoxMenuItem("Fill Text");
        patternMatchNoneItem = new JRadioButtonMenuItem("Matching Off");
        patternMatchParenthesesItem = new JRadioButtonMenuItem("Match Parentheses");
        patternMatchFillTextItem = new JRadioButtonMenuItem("Fill Text");
        ButtonGroup group = new ButtonGroup();

        group.add(patternMatchNoneItem);
        group.add(patternMatchParenthesesItem);
        group.add(patternMatchFillTextItem);
        patternMatchNoneItem.setSelected(true);  // default is 'no matching'
        optionsMenu.add(patternMatchNoneItem);
        optionsMenu.add(patternMatchParenthesesItem);
        optionsMenu.add(patternMatchFillTextItem);
        optionsMenu.addSeparator();
        colorCommentsItem = new JCheckBoxMenuItem("Highlight Comments");
        colorKeywordsItem = new JCheckBoxMenuItem("Highlight Keywords");
        optionsMenu.add(colorCommentsItem);
        optionsMenu.add(colorKeywordsItem);

        // the Help menu starts here
        helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');
        mb.add(helpMenu);
        contentsItem = new JMenuItem("Contents", 'C');
        aboutItem = new JMenuItem("About", 'A');
        helpMenu.add(contentsItem);
        helpMenu.addSeparator();
        helpMenu.add(aboutItem);

        textArea.setLineWrap(true);
        textArea.setTabSize(2);
        textFont = new Font("Lucida Sans", Font.PLAIN, 16);

        textArea.setEditable(true);
        textArea.setFont(textFont);
        textArea.setText("");
        textAreaScrollPane = new JScrollPane(textArea);

        int firstrow = textArea.getCaretPosition() + 1;
        int firstcol = 1;
        statusLabel.setText(" Row: " + firstrow + "  Col: " + firstcol);

        setTitle("Jess Editor");
        setJMenuBar(mb);
        getContentPane().add(textAreaScrollPane, "Center");
        getContentPane().add(statusLabel, "South");

        pj = PrinterJob.getPrinterJob();
        mPageFormat = pj.defaultPage();

        // added Highlighter
        h = textArea.getHighlighter();

        fontDlg = new FontDialog(this);

        // Create the undo manager and actions
        MonitorableUndoManager manager = new MonitorableUndoManager();
        textArea.getDocument().addUndoableEditListener(manager);

        Action undoAction = new UndoAction(manager);
        Action redoAction = new RedoAction(manager);

        undoItem.addActionListener(undoAction);
        redoItem.addActionListener(redoAction);

        // add keyboard accelerators (from old code, rework these later)
        printItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK));
        pageSetupItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,Event.CTRL_MASK | Event.SHIFT_MASK));
        cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK, false));
        copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK, false));
        pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK, false));

        // Assign the actions to keys for the undo/redo feature
        textArea.registerKeyboardAction(undoAction, KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK), JComponent.WHEN_FOCUSED);
        textArea.registerKeyboardAction(redoAction, KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK), JComponent.WHEN_FOCUSED);

        // listeners for the menu items start here
        // Handle events from the MonitorableUndoManager
        manager.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                MonitorableUndoManager m = (MonitorableUndoManager)evt.getSource();
                boolean canUndo = m.canUndo();
                boolean canRedo = m.canRedo();

                undoItem.setEnabled(canUndo);
                redoItem.setEnabled(canRedo);
            }
        });

        selectFontItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                // Set the dialog window position
                Rectangle bounds = getBounds();
                fontDlg.setLocation(bounds.x + bounds.width/3, bounds.y + bounds.height/3);
                fontDlg.setVisible(true);            // Show the dialog
            }
        });

        startJessWinItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (jw == null) {
                    if (DEBUG)  System.out.println("building JessWin.");

                    Runnable startJessWinThread = new Runnable() {
                        public void run() {
                            try {
                                Thread.sleep(1000);
                                buildJessWinGUI();
                            } catch (Exception e) {
                                System.out.println("Exception starting JessWin:" + "\n" + e);
                            }
                        }
                    };
                    SwingUtilities.invokeLater(startJessWinThread);
                }
            }
        });

        jessItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (jw != null && jw.getJessStatus()) {
                    if (DEBUG)  System.out.println("updating JessWin");
                    Thread t = new Thread(new Runnable() {
                        public void run() {
                            jw.updateJess(textArea.getText());
                        }
                    });
                    t.start();
                }
            }
        });

        printItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        JTextAreaPrinter jtap = new JTextAreaPrinter(textArea, mPageFormat);
                        PrintMonitor pm = new PrintMonitor(jtap);
                        try {
                            pm.performPrint(true);
                        } catch (PrinterException pe) {
                            JOptionPane.showMessageDialog(JessEditor.this, "Printing error:" + pe.getMessage());
                        }
                    }
                });
                t.start();
            }
        });

        printPreviewerItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        JTextAreaPrinter jtap = new JTextAreaPrinter(textArea, mPageFormat);
                        PrintPreviewer pp = new PrintPreviewer(jtap, 0);
                        JDialog dlg = new JDialog(JessEditor.this, "Print Preview");
                        dlg.getContentPane().add(pp);
                        dlg.setSize(400, 300);
                        dlg.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
                        dlg.setVisible(true);
                    }
                });
                t.start();
            }
        });

        pageSetupItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        pj = PrinterJob.getPrinterJob();
                        mPageFormat = pj.pageDialog(mPageFormat);
                    }
                });
                t.start();
            }
        });

        clearItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                textArea.setText("");
                setTitle("JessEditor");
                filename = null;
            }
        });

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                closeWindow();
            }
        });

        // new JavaHelp system
        contentsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (jessEditorHelpFrame == null) {
                    jessEditorHelpFrame = new JessEditorHelpFrame();
                } else {
                    jessEditorHelpFrame.UpdateWindow();
                }
            }
        });

        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String aboutText = new String(versionNumber + "\n" + "William E. Wheeler" + 
                                                              "\n" + "physicsguyaz@comcast.net");
                JOptionPane.showMessageDialog(JessEditor.this, aboutText);
            }
        });

        fileManager = new FileManager();

        // Processing for the "File" menu
        fileMenu.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent evt) {
                boolean fileOpen = true;
                boolean canSave = fileManager.canWrite();
                boolean canRun = (fileOpen && fileManager.canRunProgram());
 
                if (textArea.getText().length() > 0) {
                    saveItem.setEnabled(true);
                    saveAsItem.setEnabled(true);
                    printItem.setEnabled(true);
                    printPreviewerItem.setEnabled(true);
                } else {
                    saveItem.setEnabled(canSave);
                    saveAsItem.setEnabled(fileOpen);
                    printItem.setEnabled(false);
                    printPreviewerItem.setEnabled(false);
                }
            }

            public void menuDeselected(MenuEvent evt) {
      	        // Not used
            }

            public void menuCanceled(MenuEvent evt) {
                // Not used
            }
        });  // end of the file menu listener

        editMenu.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent evt) {
                boolean fileOpen = fileManager.isFileOpen();

                cutItem.setEnabled(!textArea.getText().equals(""));
                copyItem.setEnabled(!textArea.getText().equals(""));
                boolean canPaste = (fileManager.canWrite());
                pasteItem.setEnabled(canPaste);
            }

            public void menuDeselected(MenuEvent evt) {
                // Not used
            }

            public void menuCanceled(MenuEvent evt) {
                // Not used
            }
        });  // end of edit menu listeners

        jessMenu.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent evt) {
                // if there is nothing to send to Jess, disable the menu
                if (jw != null && jw.getJessStatus()) {
                    startJessWinItem.setEnabled(false);
                }
                if (textArea.getText().length() > 0) {
                    jessItem.setEnabled(!textArea.getText().equals(""));
                }
            }

            public void menuDeselected(MenuEvent evt) {
                // Not used
            }

            public void menuCanceled(MenuEvent evt) {
                // Not used
            }
        });  // end of edit menu listeners

        optionsMenu.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent evt) {
                if (hm != null) {
                    colorKeywordsItem.setEnabled(true);
                } else {
                    // no highlight manager to read the file, set to false
                    hm = new HighlightManager(textArea);
                    colorKeywordsItem.setEnabled(hm.getFileStatus());
                }
            }

            public void menuDeselected(MenuEvent evt) {
                // Not used
            }

            public void menuCanceled(MenuEvent evt) {
                // Not used
            }
        });  // end of edit menu listeners

        // Processing for "New..." menu item
        newItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JFileChooser fc = new JFileChooser();

                int returnVal = fc.showDialog(JessEditor.this, "New File");

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    filename = new String(file.getAbsolutePath());

                    if (filename.length() >= 1) {
                        if (fileManager.isFileOpen()) {
                            // Close any open file
                            try {
                                fileManager.closeFile(textArea.getText());
                            } catch (Throwable e) {
                                // Ignore any exception
                            }
                        }
                    }
                }
                try {
                    fileManager.newFile(filename);
                    textArea.setText("");
                    textArea.setEditable(true);    // Always writable
                    fileManager.setWritable(true); // Allow writes to file

                    saveItem.setEnabled(true);     // enable saving to a file
                    saveAsItem.setEnabled(true);
                    addRecentFile(filename);
                    setTitle("JessEditor --- " + filename);
                } catch (IOException e) {
                    // Do nothing - message already printed
                }
            }
        });  // end of new menu listeners

        // Processing for "Save..." menu item
        saveItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (filename.length() <= 1) {
                    JFileChooser fc = new JFileChooser();

                    int returnVal = fc.showDialog(JessEditor.this, "Save File");

                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        filename = new String(file.getAbsolutePath());

                        try {
                            fileManager.newFile(filename);
                            fileManager.saveFile(textArea.getText());
                        } catch (IOException e) {
                            // Do nothing - message already printed
                        }
                    }
                } else {
                    // Close any open file
                    try {
                        fileManager.saveFile(textArea.getText());
                    } catch (Throwable e) {
                        // Ignore any exception
                    }
                }
                textArea.setEditable(true);  // Always writable
                fileManager.setWritable(true); // Allow writes to file

                saveItem.setEnabled(true);  // enable saving to a file
                saveAsItem.setEnabled(true);
                addRecentFile(filename);
            }
        });  // end of new menu listeners

        // Processing for "Open..." menu item
        openItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JFileChooser fc = new JFileChooser();

                int returnVal = fc.showDialog(JessEditor.this, "Open File");

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    filename = new String(file.getAbsolutePath());

                    if (filename.length() >= 1) {
                        try {
                            String text = fileManager.openFile(filename);
                            textArea.setText(text);

                            boolean canWrite = fileManager.canWrite();
                            textArea.setEditable(canWrite);
                            fileManager.setWritable(canWrite);

                            textArea.setCaretPosition(0);  // set the caret to the beginning of the file
                            addRecentFile(filename);
                            setTitle("JessEditor --- " + filename);
                        } catch (IOException e) {
                            // Do nothing - message already printed
                        }
                    }
                }
            }
        });

        // Processing for "Load Clp" menu item (same as old open...)
        // load a CLP file into the Editor's text area
        loadClpItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JFileChooser fc = new JFileChooser();
                fc.addChoosableFileFilter(new ClipsFilter());

                int returnVal = fc.showDialog(JessEditor.this, "Load Clips File");

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    filename = new String(file.getAbsolutePath());

                    if (filename.length() >= 1) {
                        try {
                            String text = fileManager.openFile(filename);
                            textArea.setText(text);

                            boolean canWrite = fileManager.canWrite();
                            textArea.setEditable(canWrite);
                            fileManager.setWritable(canWrite);

                            textArea.setCaretPosition(0);  // set the caret to the beginning of the file
                            addRecentFile(filename);
                            setTitle("JessEditor --- " + filename);
                        } catch (IOException e) {
                            // Do nothing - message already printed
                        }
                    }
                }
            }
        });

        // Processing for "Close..." menu item (add this later)
        closeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    fileManager.closeFile(textArea.getText());
                    textArea.setText("");
                    setTitle("JessEditor");
                    filename = null;
                } catch (IOException e) {
                    // Do nothing - message already printed
                }
            }
        });

        // Processing for "Save As..." menu item
        saveAsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (filename.length() <= 1) {
                    JFileChooser fc = new JFileChooser();

                    int returnVal = fc.showDialog(JessEditor.this, "Save As");

                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        filename = new String(file.getAbsolutePath());

                        try {
                            fileManager.newFile(filename);
                            fileManager.saveFile(textArea.getText());
                            addRecentFile(filename);
                            setTitle("JessEditor --- " + filename);
                        } catch (IOException e) {
                            // Do nothing - message already printed
                        }
                    }
                } else {
                    // Close any open file
                    JFileChooser fc = new JFileChooser();

                    int returnVal = fc.showDialog(JessEditor.this, "Save As");

                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        filename = new String(file.getAbsolutePath());

                        try {
                            fileManager.newFile(filename);
                            fileManager.saveAsFile(filename, textArea.getText());
                            addRecentFile(filename);
                            setTitle("JessEditor --- " + filename);
                        } catch (IOException e) {
                            // Do nothing - message already printed
                        }
                    }
                }
                textArea.setEditable(true);    // Always writable
                fileManager.setWritable(true); // Allow writes to file

                saveItem.setEnabled(true);     // enable saving to a file
                saveAsItem.setEnabled(true);
            }
        });

        // copy, cut and paste from the Menu
        copyItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                textArea.copy();
            }
        });

        cutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                textArea.cut();
            }
        });

        pasteItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                textArea.paste();
            }
        });

        // new functionality for pattern matching 
        patternMatchNoneItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (DEBUG)  System.out.println("Pattern Matching Off");

                patternMatchParentheses = false;
                patternMatchFillText = false;

                // clear the highlights
                if (hm != null) {
                    hm.clearPatternMatchingHighlights();
                }
            }
        });

        patternMatchParenthesesItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (DEBUG)  System.out.println("Match the Parentheses");

                patternMatchParentheses = true;
                patternMatchFillText = false;

                int pos = textArea.getCaretPosition();

                if (hm != null) {
                    hm.searchMatchingParentheses(pos);
                } else {
                    hm = new HighlightManager(textArea);
                    hm.searchMatchingParentheses(pos);
                }
            }
        });

        patternMatchFillTextItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (DEBUG)  System.out.println("Fill Text");

                patternMatchFillText = true;
                patternMatchParentheses = false;

                int pos = textArea.getCaretPosition();

                if (hm != null) {
                    hm.searchFillText(pos);
                } else {
                    hm = new HighlightManager(textArea);
                    hm.searchFillText(pos);
                }
            }
        });

        colorCommentsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (DEBUG)  System.out.println("Coloring Comments.");

                colorComments = colorCommentsItem.getState();

                if (colorComments) {
                    if (hm != null) {
                        hm.searchComments();
                    } else {
                        hm = new HighlightManager(textArea);
                        hm.searchComments();
                    }
                } else {
                    if (hm != null) {
                        // clear the comment highlights
                        hm.clearCommentHighlights();
                    }
                }
            }
        });

        colorKeywordsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (DEBUG)  System.out.println("Coloring Keywords.");

                colorKeywords = colorKeywordsItem.getState();

                if (colorKeywords) {
                    if (hm != null) {
                        if (DEBUG)  System.out.println("1. keyword search");
                        // hm.createKeywordList();
                        hm.searchKeywords();
                    } else {
                        if (DEBUG)  System.out.println("2. keyword search");
                        hm = new HighlightManager(textArea);
                        hm.searchKeywords();
                    }
                } else {
                    // make sure the highlight manager knows that
                    // keyword searching has been turned off
                    // this allows the keyword file to be changed and
                    // reread if coloring keywords is selected again
                    if (hm != null) {
                        hm.clearKeywordHighlights();
                    }
                }
            }
        });

        // Create the handler for the recent file list
        menuListener = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JMenuItem mi = (JMenuItem)(evt.getSource());
                String fileName = mi.getActionCommand();
                try {
                    String text = fileManager.openFile(fileName);
                    textArea.setText(text);
                    addRecentFile(fileName);
                    textArea.setEditable(fileManager.canWrite() );

                    fileManager.setWritable(fileManager.canWrite());
                } catch (IOException e) {
                    // Do nothing - message already printed
                }
            }
        };

        textArea.addCaretListener( new CaretListener() {
            public void caretUpdate(CaretEvent e) {
                if (DEBUG)  System.out.println("textPane.CaretListener");

                // update the menu display
                Object o = e.getSource();
                if (o == (Object)textArea) {
                    // just moving the caret, check pattern matching only
                    int pos = textArea.getCaretPosition();
                    updateStatusLabel(pos);

                    if (patternMatchParentheses) {
                        if (DEBUG)  System.out.println("textPane.addCaretListener, match");

                        if (hm!= null) {
                            hm.searchMatchingParentheses(pos);
                        } else {
                            hm = new HighlightManager(textArea);
                            hm.searchMatchingParentheses(pos);
                        }
                    }

                    if (patternMatchFillText) {
                        if (hm != null) {
                            hm.searchFillText(pos);
                        } else {
                            hm = new HighlightManager(textArea);
                            hm.searchFillText(pos);
                        }
                    }
                }
            }
        });

        textArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                if (DEBUG)  System.out.println("documentListener.insertUpdate");

                if (colorComments) {
                    if (hm != null) {
                        hm.searchComments();
                    } else {
                        hm = new HighlightManager(textArea);
                        hm.searchComments();
                    }
                }

                if (colorKeywords) {
                    if (hm != null) {
                        if (DEBUG)  System.out.println("3. keyword search");
                        hm.searchKeywords();
                    } else {
                        hm = new HighlightManager(textArea);
                        if (DEBUG)  System.out.println("4. keyword search");
                        hm.searchKeywords();
                    }
                }
            }

            public void removeUpdate(DocumentEvent e) {
                if (DEBUG)  System.out.println("documentListener.removeUpdate");
            }

            public void changedUpdate(DocumentEvent e) {
                if (DEBUG)  System.out.println("documentListener.changedUpdate");
            }
        });

        // make sure the text area always has the focus
        textArea.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent fe) {
                textArea.requestFocus();
            }
        });

        // fill the center 3/4's of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int scrnWidth  = 7 * screenSize.width  / 8;
        int scrnHeight = 7 * screenSize.height / 8;

        setSize(scrnWidth, scrnHeight);
        setLocation(screenSize.width  / 16, screenSize.height / 16);

        if (isStandAlone)
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // EXIT_ON_CLOSE);  Windows ok, not on Solaris 8
        else
            setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                JessEditor temp = (JessEditor)(evt.getSource());
                temp.closeWindow();
            }
        });

        show();
        setVisible(true);
    }  // end of class

    // updates the status line for the current row and column
    private void updateStatusLabel(int pos) {
        byte[]  databytes = textArea.getText().getBytes();
        int lfcount = 0;
        int prevlfpos = 0;  // before the caret position
        int nextlfpos = 0;  // after the caret position
        int ivalue;

        for (int i = 0; i < pos; i++) {
            ivalue = (int)databytes[i];
            if (ivalue == 10)  // linefeed
              lfcount++;
        }
        int row = lfcount + 1;
        int col = 0;

        // find the column
        if (row == 1) {
            col = pos + 1;
        } else {
            // find the previous linefeed position
            for (int i = 0; i < pos; i++) {
                ivalue = (int)databytes[i];
                if (ivalue == 10) {
                    prevlfpos = i;
                }
            }
            col = pos - prevlfpos;
        }
        statusLabel.setText(" Row: " + row + "  Col: " + col);
    }

    // Add a name to the recent file list
    public void addRecentFile(String fileName) {
        int offset;

        // Check whether this file is already in the file list
        if ((offset = recentFiles.indexOf(fileName)) == -1) {
            // Not present - add it at the beginning
            recentFiles.insertElementAt(fileName, 0);

            if (recentFiles.size() > RECENT_FILES) {
                // List was full - remove the last (oldest) item
                recentFiles.removeElementAt(RECENT_FILES);
            }
        } else {
            // Already present: move this item to the front of the list
            recentFiles.removeElementAt(offset);
            recentFiles.insertElementAt(fileName, 0);
        }
        // Rebuild the file menu
        buildRecentFileMenu();
    }

    // Build the recent file menu
    public void buildRecentFileMenu() {
        int size = recentFiles.size();

        fileListMenu.removeAll();
        for (int i = 1; i <= size; i++) {
            String fileName = (String)(recentFiles.elementAt(i - 1));
            JMenuItem mi = new JMenuItem();

            mi.setActionCommand(fileName);
            mi.setText(i + " " + fileName);
            mi.setMnemonic('0' + i);
            fileListMenu.add(mi);
            mi.addActionListener(menuListener);
        }
    }

    // Get a file name
    public String getFileName(String title, int mode) {
        FileDialog fd = new FileDialog(this, title, mode);
        fd.setFile("*.*");
        fd.setDirectory(directory);
        fd.setVisible(true);
        String dir = fd.getDirectory();
        String file = fd.getFile();
         
        if (dir == null || file == null) {
            return null;
        }
        directory = dir;

        String separator = System.getProperty("file.separator");
        if (dir.endsWith(separator)) {
            return dir + file;
        } else {
            return dir + System.getProperty("file.separator") + file;
        }
    }

    public Font getCurrentFont() {
        // used by the FontDialog
        return textFont;
    }

    public void setCurrentFont(Font font) {
        textFont = font;
        textArea.setFont(textFont);
        String temp = new String(textArea.getText());
        textArea.setText("");
        textArea.setText(temp);
    }

    // Take any action needed to close the application
    public void closeWindow() {
        try {
            fileManager.closeFile(textArea.getText());
        } catch (IOException e) {
            System.out.println("Failed to write data on close.");
        }
        if (isStandAlone) {
            System.exit(0);
        } else {
            setVisible(false);
        }
    }

    private void buildJessWinGUI() {
        if (jw == null) {
            // start JessWin from the editor
            Runnable startJessWinThread = new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(1000);
                        jw = new JessWin(jessWinArgs);
                    } catch (Exception e) {
                        System.out.println("Exception starting JessWin:" + "\n" + e);
                    }
                }
            };
            SwingUtilities.invokeLater(startJessWinThread);
        }
        textArea.requestFocus();
    }

    public void DisplayWindow() {
        setState(Frame.NORMAL);
        setVisible(true);
    }

    // The Undo action
    public class UndoAction extends AbstractAction {
        public UndoAction(UndoManager manager) {
            this.manager = manager;
        }
        public void actionPerformed(ActionEvent evt) {
            try {
                manager.undo();
            } catch (CannotUndoException e) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
        private UndoManager manager;
    }

    // The Redo action
    public class RedoAction extends AbstractAction {
        public RedoAction(UndoManager manager) {
            this.manager = manager;
        }
        public void actionPerformed(ActionEvent evt) {
            try {
                manager.redo();
            } catch (CannotRedoException e) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
        private UndoManager manager;
    }

    public static void main(String[] args) {
        JessEditor je = new JessEditor(true);

        jessWinArgs = args;  // use for the Jess engine
    }

    // The organization of the menu bar and all of the items is given below
    // instead of being scattered throughout the first part of this file.

    // File Menu
    JMenu fileMenu;

    JMenuItem loadClpItem;

    JMenuItem newItem;
    JMenuItem openItem;
    JMenuItem saveItem;
    JMenuItem saveAsItem;
    JMenuItem closeItem;
    JMenuItem clearItem;
    JMenuItem emptyListItem;
    JMenuItem printItem;
    JMenuItem pageSetupItem;
    JMenuItem printPreviewerItem;
    JMenuItem exitItem;
    JMenu fileListMenu;

    // items on the Edit menu
    JMenu editMenu;
    JMenuItem undoItem;
    JMenuItem redoItem;
    JMenuItem cutItem;
    JMenuItem copyItem;
    JMenuItem pasteItem;

    // new menu item
    JMenu jessMenu;
    JMenuItem jessItem;
    JMenuItem startJessWinItem;

    // new Options menu
    JMenu optionsMenu;
    JMenuItem selectFontItem;
    JRadioButtonMenuItem patternMatchNoneItem;
    JRadioButtonMenuItem patternMatchParenthesesItem;
    JRadioButtonMenuItem patternMatchFillTextItem;

    JCheckBoxMenuItem colorCommentsItem;
    JCheckBoxMenuItem colorKeywordsItem;

    // items on the Help menu
    JMenu helpMenu;
    JMenuItem contentsItem;

    // item on the About menu
    JMenuItem aboutItem;

    // changing from JTextArea to JTextPane
    JTextArea   textArea   = new JTextArea(2000, 100);
    JScrollPane textAreaScrollPane;
    Font textFont; 

    FileManager fileManager;

    String directory = "";  // Current directory

    ActionListener menuListener;

    String filename = new String();

    // Recently-used file list
    static final int RECENT_FILES = 10;

    Vector recentFiles = new Vector(RECENT_FILES);
    JLabel statusLabel = new JLabel();

    static JessEditorHelpFrame jessEditorHelpFrame;

    private boolean patternMatchParentheses = false;
    private boolean patternMatchFillText = false;
    private boolean colorComments = false;
    private boolean colorKeywords = false;

    private FontDialog fontDlg; // new font dialog box

    // new HighlightManager class
    static HighlightManager hm;

    // for the JessWin Rete engine
    static String [] jessWinArgs;
}
