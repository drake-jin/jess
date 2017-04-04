

// TODO
// version incremented to 1.18
// 1.  reformat                    (done) 01 August 2003
// 2.  fix watch windows and menus (done) 02 August 2003
// 3.  add more hot keys
// 4.  cleanup
// 5.  test extensively

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.awt.print.*;
import jess.*;

public class JessWin extends JFrame implements Serializable {
    private static boolean DEBUG = false;
    private static String versionNumber = new String("Version 1.18");

    // two separate printjobs and pageformats
    private PrinterJob pjJessDisplay;
    private PageFormat mpfJessDisplay;

    private PrinterJob pjJessInput;
    private PageFormat mpfJessInput;

    private static boolean reteNetViewOn = false;

    // note: this variable was supposed to help solve the problem
    // of JessWin killing the JessEditor.  However, must be from
    // the JESS code since killing the engine kills everything else.
    // private static boolean runFromJessEditor = false;

    public JessWin() {
        super();
        buildGUI();
        // start Jess automatically
        // this fixes a problem on older, slower machines (threading problem)
        // it isn't pretty but it works
        Runnable startJessThread = new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1000);
                    startJessEngine();
                    jcp.setFocus();
                    jessRunning = true;
                } catch (Exception e) {
                    System.out.println("Exception starting Jess:" + "\n" + e);
                }
            }
        };
        SwingUtilities.invokeLater(startJessThread);
    }

    public JessWin(String[] args) {
        super();
        jessArgs = args;
        buildGUI();
        // start Jess automatically
        // this fixes a problem on older, slower machines (threading problem)
        // it isn't pretty but it works
        Runnable startJessThread = new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1000);
                    startJessEngine();
                    jcp.setFocus();
                    jcp.giveUpFocus(true);
                    jessRunning = true;
                } catch (Exception e) {
                    System.out.println("Exception starting Jess:" + "\n" + e);
                }
            }
        };
        SwingUtilities.invokeLater(startJessThread);
        if (jcp != null)  jcp.giveUpFocus(true);
    }

    private void buildGUI() {
        this.setTitle("JessWin");

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        // make a menubar
        JMenuBar mb = new JMenuBar();
        this.setJMenuBar(mb);

        // mixing light and heavy-weight components
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);

        // the File menu starts here
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        mb.add(fileMenu);

        loadClpItem = new JMenuItem("Load Clip", 'L');
        fileMenu.add(loadClpItem);
        saveSessionItem = new JMenuItem("Save Session", 'S');
        fileMenu.add(saveSessionItem);
        jessEditorItem = new JMenuItem("Jess Editor", 'J');
        fileMenu.add(jessEditorItem);
        fileMenu.addSeparator();
        commandListMenu = new JMenu("Recent Commands");
        commandListMenu.setMnemonic('R');
        fileMenu.add(commandListMenu);
        emptyListItem = new JMenuItem("Empty");
        emptyListItem.setEnabled(false);
        commandListMenu.add(emptyListItem);
        fileMenu.addSeparator();

        // printer functionality
        printMenu = new JMenu("Print");
        printPreviewerMenu = new JMenu("Print Preview");
        pageSetupMenu = new JMenu("Page Setup");
        printJessDisplayItem = new JMenuItem("Jess Display (top)");
        printJessInputItem = new JMenuItem("Jess Input (bottom)");
        printMenu.add(printJessDisplayItem);
        printMenu.add(printJessInputItem);
        printPreviewerJessDisplayItem = new JMenuItem("Jess Display Preview (top)");
        printPreviewerJessInputItem = new JMenuItem("Jess Input Preview (bottom)");
        printPreviewerMenu.add(printPreviewerJessDisplayItem);
        printPreviewerMenu.add(printPreviewerJessInputItem);
        pageSetupJessDisplayItem = new JMenuItem("Jess Display Setup (top)");
        pageSetupJessInputItem = new JMenuItem("Jess Input Setup (bottom)");
        pageSetupMenu.add(pageSetupJessDisplayItem);
        pageSetupMenu.add(pageSetupJessInputItem);

        fileMenu.add(printMenu);
        fileMenu.add(printPreviewerMenu);
        fileMenu.add(pageSetupMenu);

        exitItem = new JMenuItem("Exit", 'x');

        // allow the exit only if running standalone
        // if (!runFromJessEditor) {
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        // }

        // the Execute menu starts here
        executeMenu = new JMenu("Execute");
        executeMenu.setMnemonic('X');
        mb.add(executeMenu);

        startJessItem = new JMenuItem("Start", 'S');
        executeMenu.add(startJessItem);
        resetJessItem = new JMenuItem("Reset", 'T');
        executeMenu.add(resetJessItem);
        runJessItem = new JMenuItem("Run", 'R');
        executeMenu.add(runJessItem);
        stepJessItem = new JMenuItem("Step", 'P'); // (run 1) TODD
        executeMenu.add(stepJessItem);                      // (run 1) TODD
        clearJessItem = new JMenuItem("Clear", 'L');
        executeMenu.add(clearJessItem);
        haltJessItem = new JMenuItem("Halt", 'H');
        executeMenu.add(haltJessItem);
        versionJessItem = new JMenuItem("Version", 'V');
        executeMenu.add(versionJessItem);

        // add new strategy menu
        strategyMenu = new JMenu("Strategy...");
        depthStrategyMenuItem = new JCheckBoxMenuItem("depth (LIFO)", true);
        breadthStrategyMenuItem = new JCheckBoxMenuItem("breadth (FIFO)", false);
        strategyMenu.add(depthStrategyMenuItem);
        strategyMenu.add(breadthStrategyMenuItem);
        executeMenu.add(strategyMenu);

        // Watch subMenu on the Execute Menu
        executeWatchMenu = new JMenu("Watch...");
        executeWatchAllMenuItem = new JCheckBoxMenuItem("watch all");
        executeWatchFactsMenuItem = new JCheckBoxMenuItem("watch facts");
        executeWatchRulesMenuItem = new JCheckBoxMenuItem("watch rules");
        executeWatchAgendaMenuItem = new JCheckBoxMenuItem("watch activations");
        executeWatchDefglobalsMenuItem = new JCheckBoxMenuItem("watch compilations");
        // build the submenu
        executeWatchMenu.add(executeWatchAllMenuItem);
        executeWatchMenu.addSeparator();
        executeWatchMenu.add(executeWatchFactsMenuItem);
        executeWatchMenu.add(executeWatchRulesMenuItem);
        executeWatchMenu.add(executeWatchAgendaMenuItem);
        executeWatchMenu.add(executeWatchDefglobalsMenuItem);
        executeMenu.add(executeWatchMenu);
        exitJessItem = new JMenuItem("Exit Jess", 'J');
        // do not allow the exit
        // if (!runFromJessEditor) {
        executeMenu.addSeparator();
        executeMenu.add(exitJessItem);
        // }

        startJessItem.setEnabled(true);
        resetJessItem.setEnabled(false);
        runJessItem.setEnabled(false);
        stepJessItem.setEnabled(false);     // (run 1) TODD
        clearJessItem.setEnabled(false);
        haltJessItem.setEnabled(false);  // (halt)
        versionJessItem.setEnabled(false);
        exitJessItem.setEnabled(false);  // (exit) from Jess
        strategyMenu.setEnabled(false);

        // the Watch menu starts here
        watchWindowMenu = new JMenu("Watch");
        watchWindowMenu.setMnemonic('W');
        watchWindowFactsItem = new JCheckBoxMenuItem("Watch Facts");
        watchWindowMenu.add(watchWindowFactsItem);
        watchWindowRulesItem = new JCheckBoxMenuItem("Watch Rules");
        watchWindowMenu.add(watchWindowRulesItem);
        watchWindowAgendaItem = new JCheckBoxMenuItem("Watch Agenda");
        watchWindowMenu.add(watchWindowAgendaItem);
        // defglobals added by TODD
        watchWindowDefglobalsItem = new JCheckBoxMenuItem("Watch Defglobals");
        watchWindowMenu.add(watchWindowDefglobalsItem);
        mb.add(watchWindowMenu);

        // the options menu starts here
        optionsMenu = new JMenu("Options");
        optionsMenu.setMnemonic('O');
        mb.add(optionsMenu);
        selectFontItem = new JMenuItem("Font", 'F');
        optionsMenu.add(selectFontItem);

        // the Help menu starts here
        helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');
        mb.add(helpMenu);
        jessWinMenuHelpItem = new JMenuItem("JessWin Menu Help");
        jessCommandsHelpItem = new JMenuItem("Jess Commands Help");
        aboutItem = new JMenuItem("About");
        helpMenu.add(jessWinMenuHelpItem);
        helpMenu.add(jessCommandsHelpItem);
        helpMenu.addSeparator();
        helpMenu.add(aboutItem);

        getContentPane().add(jcp);

        // for the top textarea (Jess Display)
        pjJessDisplay = PrinterJob.getPrinterJob();
        mpfJessDisplay = pjJessDisplay.defaultPage();
        // for the bottom textarea (Jess Input)
        pjJessInput = PrinterJob.getPrinterJob();
        mpfJessInput = pjJessInput.defaultPage();

        // add the reference to the new FontDialog class
        fontDlg = new FontDialog(this);

        // add keyboard accelerators (update later)
        resetJessItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_MASK, false));
        runJessItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK, false));
        stepJessItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK, false));

        // listeners for the menu items start here
        selectFontItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                // Set the dialog window position
                Rectangle bounds = getBounds();
                fontDlg.setLocation(bounds.x + bounds.width/3, bounds.y + bounds.height/3);
                fontDlg.setVisible(true);            // Show the dialog
            }
        });

        // printer code for the Jess Display (top)
        printJessDisplayItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        jcp.giveUpFocus(true);
                        JTextArea jcpHistory = new JTextArea();
                        jcpHistory.setText(jcp.getJessHistory());
                        JTextAreaPrinter jtap = new JTextAreaPrinter(jcpHistory, mpfJessDisplay);

                        PrintMonitor pm = new PrintMonitor(jtap);
                        try {
                            pm.performPrint(true);
                        } catch (PrinterException pe) {
                            JOptionPane.showMessageDialog(JessWin.this, "Printing error:" + pe.getMessage());
                        }
                    }
                });
                t.start();
            }
        });

        printPreviewerJessDisplayItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        JTextArea jcpHistory = new JTextArea();
                        jcpHistory.setText(jcp.getJessHistory());
                        JTextAreaPrinter jtap = new JTextAreaPrinter(jcpHistory, mpfJessDisplay);

                        PrintPreviewer pp = new PrintPreviewer(jtap, 0);
                        JDialog dlg = new JDialog(JessWin.this, "Print Preview");
                        dlg.getContentPane().add(pp);
                        dlg.setSize(400, 300);
                        dlg.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
                        dlg.setVisible(true);
                    }
                });
                t.start();
            }
        });

        // later on add code for jcp.giveupFocus(false);
        pageSetupJessDisplayItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        jcp.giveUpFocus(true);
                        pjJessDisplay = PrinterJob.getPrinterJob();
                        mpfJessDisplay = pjJessDisplay.pageDialog(mpfJessDisplay);
                    }
                });
                t.start();
            }
        });

        // printer code for the Jess Input (bottom)
        printJessInputItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        jcp.giveUpFocus(true);
                        JTextArea jcpHistory = new JTextArea();
                        jcpHistory.setText(jcp.getJessInputText());
                        JTextAreaPrinter jtap = new JTextAreaPrinter(jcpHistory, mpfJessInput);

                        PrintMonitor pm = new PrintMonitor(jtap);
                        try {
                            pm.performPrint(true);
                        } catch (PrinterException pe) {
                            JOptionPane.showMessageDialog(JessWin.this, "Printing error:" + pe.getMessage());
                        }
                    }
                });
                t.start();
            }
        });

        printPreviewerJessInputItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        JTextArea jcpHistory = new JTextArea();
                        jcpHistory.setText(jcp.getJessInputText());
                        JTextAreaPrinter jtap = new JTextAreaPrinter(jcpHistory, mpfJessInput);

                        PrintPreviewer pp = new PrintPreviewer(jtap, 0);
                        JDialog dlg = new JDialog(JessWin.this, "Print Preview");
                        dlg.getContentPane().add(pp);
                        dlg.setSize(400, 300);
                        dlg.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
                        dlg.setVisible(true);
                    }
                });
                t.start();
            }
        });

        // later on add code for jcp.giveupFocus(false);
        pageSetupJessInputItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        jcp.giveUpFocus(true);
                        pjJessInput = PrinterJob.getPrinterJob();
                        mpfJessInput = pjJessInput.pageDialog(mpfJessInput);
                    }
                });
                t.start();
            }
        });

        // update the Watch submenu for the Execute menu (fixed)
		executeWatchMenu.addMenuListener(new MenuListener() {
			public void menuSelected(MenuEvent evt) {
				// if all the items are checked, check the watch all menu
				executeWatchAllMenuItem.setState(watchFacts && watchRules && watchAgenda && watchDefglobals);
 				executeWatchFactsMenuItem.setState(watchFacts);
				executeWatchRulesMenuItem.setState(watchRules);
				executeWatchAgendaMenuItem.setState(watchAgenda);
				executeWatchDefglobalsMenuItem.setState(watchDefglobals);
			}
			public void menuDeselected(MenuEvent evt) {
				// Not used
			}
			public void menuCanceled(MenuEvent evt) {
				// Not used
			}
		});  // end of menu listeners

		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				closeWindow();
			}
		});

        // update the Watch Menu for the watch windows here (fixed)
        watchWindowMenu.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent evt) {
				watchWindowFactsItem.setState(watchFacts);
				watchWindowRulesItem.setState(watchRules);
				watchWindowAgendaItem.setState(watchAgenda);
				watchWindowDefglobalsItem.setState(watchDefglobals);
            }
            public void menuDeselected(MenuEvent evt) {
                // Not used
            }
            public void menuCanceled(MenuEvent evt) {
                // Not used
            }
        });  // end of menu listeners

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                closeWindow();
            }
        });

        // new Help System, better than the old
        // the JavaHelp API is overkill for the simple JessEditor
        jessWinMenuHelpItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jcp.giveUpFocus(true);
                if (jessWinHelpFrame == null) {
                    jessWinHelpFrame = new JessWinHelpFrame();
                } else {
                    jessWinHelpFrame.UpdateWindow();
                }
            }
        });

        jessCommandsHelpItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jcp.giveUpFocus(true);
                if (jessCommandsHelpFrame == null) {
                    jessCommandsHelpFrame = new JessCommandsHelpFrame();
                } else {
                    jessCommandsHelpFrame.UpdateWindow();
                }
            }
        });

        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String aboutText = new String(versionNumber + "\n" + "William E. Wheeler" +
                                                              "\n" + "physicsguyaz@comcast.net");
               JOptionPane.showMessageDialog(JessWin.this, aboutText);
            }
        });

        // make a Watch Window for the facts
        watchWindowFactsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	System.out.println("factsItem");
                // jcp.giveUpFocus(true);
                if (watchWindowFactsItem.getState()) {
                    if (factsFrame == null) {
                        Dimension scrnSize = Toolkit.getDefaultToolkit().getScreenSize();
                        int fWidth  = 1 * scrnSize.width  / 3;
                        int fHeight = 1 * scrnHeight / 3;

                        // open the factsFrame at the top right 1/4
                        // of the screen and half its height
                        factsFrame = new WatchWindow(JessWin.this, "Facts", scrnWidth, 0, fWidth, fHeight);
                        jcp.assignFactsFrame(factsFrame);
                        watchFacts = true;
                    } else {
                        factsFrame.UpdateWindow();
                    }
                    jcp.setFactsFrameVisibility(true);
                    updateJess("(facts)");
                } else {
                	// shutting down the facts window
					jcp.setFactsFrameVisibility(false);
                	factsFrame.setVisible(false);
                	factsFrame = null;
                	watchFacts = false;
                }
            }
        });

        // add a Watch Window for the rules
        watchWindowRulesItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                // jcp.giveUpFocus(true);
                if (watchWindowRulesItem.getState()) {
                    if (rulesFrame == null) {
                        Dimension scrnSize = Toolkit.getDefaultToolkit().getScreenSize();
                        int fWidth  = 1 * scrnSize.width  / 3;
                        int fHeight = 1 * scrnHeight / 3;

                        // open the rulesFrame at the middle right 1/4
                        // of the screen and half its height
                        rulesFrame = new WatchWindow(JessWin.this, "Rules", scrnWidth, scrnHeight / 3, fWidth, fHeight);
                        jcp.assignRulesFrame(rulesFrame);
                        watchRules = true;
                    } else {
                        rulesFrame.UpdateWindow();
                    }
                    jcp.setRulesFrameVisibility(true);
                    updateJess("(rules)");
                } else {
                	// shutting down the rules window
                	jcp.setRulesFrameVisibility(false);
                	rulesFrame.setVisible(false);
                	rulesFrame = null;
                	watchRules = false;
                }
			}
        });

        // make a Watch Window for the agenda
        watchWindowAgendaItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                // jcp.giveUpFocus(true);
                if (watchWindowAgendaItem.getState()) {
                    if (agendaFrame == null) {
                        Dimension scrnSize = Toolkit.getDefaultToolkit().getScreenSize();
                        int fWidth  = 1 * scrnSize.width  / 3;
                        int fHeight = 1 * scrnHeight / 3;

                        // open the factsFrame at the top right 1/4
                        // of the screen and half its height
                        agendaFrame = new WatchWindow(JessWin.this, "Agenda", scrnWidth, 2 * scrnHeight / 3, fWidth, fHeight);
                        jcp.assignAgendaFrame(agendaFrame);
                        watchAgenda = true;
                    } else {
                        agendaFrame.UpdateWindow();
                    }
                    jcp.setAgendaFrameVisibility(true);
                    updateJess("(agenda)");
                } else {
                	// shutting down the agenda window
                	jcp.setAgendaFrameVisibility(false);
                	agendaFrame.setVisible(false);
                	agendaFrame = null;
                	watchAgenda = false;
                }
            }
        });

        // make a Watch Window for the defglobals by TODD
        watchWindowDefglobalsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                // jcp.giveUpFocus(true);
                if (watchWindowDefglobalsItem.getState()) {
                    if (defglobalsFrame == null) {
                        Dimension scrnSize = Toolkit.getDefaultToolkit().getScreenSize();
                        int fWidth  = 1 * scrnSize.width  / 3;
                        int fHeight = 1 * scrnHeight / 3;

                        // open the factsFrame at the top right 1/4
                        // of the screen and half its height
                        defglobalsFrame = new WatchWindow(JessWin.this, "Defglobals", scrnWidth, scrnHeight / 3, fWidth, fHeight);
                        jcp.assignGlobalsFrame(defglobalsFrame);
                        watchDefglobals = true;
                    } else {
                        defglobalsFrame.UpdateWindow();
                    }
                    jcp.setDefglobalsFrameVisibility(true);
                    updateJess("(defglobal)");
                } else {
                	// shutting down the defglobals window
                	jcp.setDefglobalsFrameVisibility(false);
                	defglobalsFrame.setVisible(false);
                	defglobalsFrame = null;
                	watchDefglobals = false;
                }
            }
        });

        // new selections for the Watch submenu on the Execute Menu
        executeWatchAllMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (DEBUG)  System.out.println("watch all");

                if (executeWatchAllMenuItem.getState()) {
                    watchAll = true;
                    watchFacts = true;
                    watchRules = true;
                    watchAgenda = true;
                    watchDefglobals = true;

                    executeWatchFactsMenuItem.setState(true);
                    executeWatchRulesMenuItem.setState(true);
                    executeWatchAgendaMenuItem.setState(true);
                    executeWatchDefglobalsMenuItem.setState(true);

                    Thread t = new Thread(new Runnable() {
                        public void run() {
                            jcp.processMenuCommand("(watch all)");
                        }
                    });
                    t.start();
                } else if (watchAll && !executeWatchAllMenuItem.getState()) {
                    // unwatch all
                    watchAll = false;
                    watchFacts = false;
                    watchRules = false;
                    watchAgenda = false;
                    watchDefglobals = false;

                    executeWatchFactsMenuItem.setState(false);
                    executeWatchRulesMenuItem.setState(false);
                    executeWatchAgendaMenuItem.setState(false);
                    executeWatchDefglobalsMenuItem.setState(false);

                    Thread t = new Thread(new Runnable() {
                        public void run() {
                            jcp.processMenuCommand("(unwatch all)");
                        }
                    });
                    t.start();
                }
            }
        });

		// part of the watch submenu on the execute menu
        executeWatchFactsMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
				if (DEBUG)  System.out.println("watch facts");

                if (executeWatchFactsMenuItem.getState()) {
                	watchFacts = true;
                    Thread t = new Thread(new Runnable() {
                        public void run() {
                            jcp.processMenuCommand("(watch facts)");
                        }
                    });
                    t.start();
                } else if (!watchFacts) { // && !executeWatchFactsMenuItem.getState()) {
                    // unwatch the facts
                    watchFacts = false;
                    Thread t = new Thread(new Runnable() {
                        public void run() {
                            jcp.processMenuCommand("(unwatch facts)");
                        }
                    });
                    t.start();
					// destroy the WatchWindow object for the Facts
					factsFrame.setVisible(false);
				    factsFrame = null;
				    executeWatchFactsMenuItem.setState(false);
                }
                // if all the items are checked, check the watch all menu
                executeWatchAllMenuItem.setState(watchFacts && watchRules && watchAgenda && watchDefglobals);
            }
        });

		// part of the watch submenu on the execute menu
        executeWatchRulesMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (DEBUG)  System.out.println("watch rules");

                if (executeWatchRulesMenuItem.getState()) {
                    watchRules = true;
                    Thread t = new Thread(new Runnable() {
                        public void run() {
                            jcp.processMenuCommand("(watch rules)");
                        }
                    });
                    t.start();
                } else if (watchRules && !executeWatchRulesMenuItem.getState()) {
                    // unwatch the rules
                    watchRules = false;
                    Thread t = new Thread(new Runnable() {
                        public void run() {
                            jcp.processMenuCommand("(unwatch rules)");
                        }
                    });
                    t.start();
					// destroy the WatchWindow object for the Rules
					rulesFrame.setVisible(false);
					rulesFrame = null;
					executeWatchRulesMenuItem.setState(false);
                }
                // if all the items are checked, check the watch all menu
                executeWatchAllMenuItem.setState(watchFacts && watchRules && watchAgenda && watchDefglobals);
            }
        });

		// part of the watch submenu on the execute menu
        executeWatchAgendaMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (DEBUG)  System.out.println("watch agenda");

                if (executeWatchAgendaMenuItem.getState()) {
                    watchAgenda = true;
                    Thread t = new Thread(new Runnable() {
                        public void run() {
                            jcp.processMenuCommand("(watch activations)");
                        }
                    });
                    t.start();
                } else if (watchAgenda && !executeWatchAgendaMenuItem.getState()) {
                    // unwatch the activations
                    watchAgenda = false;
                    Thread t = new Thread(new Runnable() {
                        public void run() {
                            jcp.processMenuCommand("(unwatch activations)");
                        }
                    });
                    t.start();
					// destroy the WatchWindow object for the Agenda
					agendaFrame.setVisible(false);
					agendaFrame = null;
					executeWatchAgendaMenuItem.setState(false);
                }
                // if all the items are checked, check the watch all menu
                executeWatchAllMenuItem.setState(watchFacts && watchRules && watchAgenda && watchDefglobals);
            }
        });

		// part of the watch submenu on the execute menu
        executeWatchDefglobalsMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (DEBUG)  System.out.println("watch defglobals");

                if (executeWatchDefglobalsMenuItem.getState()) {
                    watchDefglobals = true;
                    Thread t = new Thread(new Runnable() {
                        public void run() {
                            jcp.processMenuCommand("(watch compilations)");
                        }
                    });
                    t.start();
                } else if (watchDefglobals && !executeWatchDefglobalsMenuItem.getState()) {
                    // unwatch the compilations
                    watchDefglobals = false;
                    Thread t = new Thread(new Runnable() {
                        public void run() {
                            jcp.processMenuCommand("(unwatch compilations)");
                        }
                    });
                    t.start();
					// destroy the WatchWindow object for the Defglobals
					defglobalsFrame.setVisible(false);
					defglobalsFrame = null;
					executeWatchDefglobalsMenuItem.setState(false);
                }
                // if all the items are checked, check the watch all menu
                executeWatchAllMenuItem.setState(watchFacts && watchRules && watchAgenda && watchDefglobals);
            }
        });

        // Create the handler for the recent file list
        menuListener = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JMenuItem mi = (JMenuItem)(evt.getSource());
                final String tempCommand = mi.getActionCommand();

                Thread t = new Thread(new Runnable() {
                    public void run() {
                        jcp.processMenuCommand(tempCommand);
                    }
                });
                t.start();
            }
        };
        // end of menu listeners

        // might change this later, mostly for testing
        addComponentListener(new ComponentListener() {
            public void componentHidden(ComponentEvent e) {
                if (DEBUG)  System.out.println("IDE is hidden");
            }
            public void componentMoved(ComponentEvent e) {
                if (DEBUG)  System.out.println("IDE was moved");
            }
            public void componentResized(ComponentEvent e) {
                if (DEBUG)  System.out.println("IDE was resized");
            }
            public void componentShown(ComponentEvent e) {
                 if (DEBUG)  System.out.println("IDE is shown");
            }
        });

        // make a JessEditor in a separate Frame
        jessEditorItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jcp.giveUpFocus(true);
                if (jessEditorFrame == null) {
                    jessEditorFrame = new JessEditor(false, JessWin.this);  // open as a child process
                } else {
                    jessEditorFrame.DisplayWindow();
                }
            }
        });

        fileManager = new FileManager();

        // Processing for the "File" menu
        fileMenu.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent evt) {
                boolean fileOpen = fileManager.isFileOpen();
                boolean canSave = fileManager.canWrite();
                boolean canRun = (fileOpen && fileManager.canRunProgram());

                buildRecentCommandMenu();
                if (jessRunning) {
                    loadClpItem.setEnabled(true);
                    saveSessionItem.setEnabled(true);
                } else {
                    loadClpItem.setEnabled(false);
                    saveSessionItem.setEnabled(false);
                }
                // if there isn't anything to print in the
                // bottom of the screen, disable the menuItem
                if (jcp.getJessInputText().length() > 1) {
                    printJessInputItem.setEnabled(true);
                    printPreviewerJessInputItem.setEnabled(true);
                    pageSetupJessInputItem.setEnabled(true);
                } else {
                    printJessInputItem.setEnabled(false);
                    printPreviewerJessInputItem.setEnabled(false);
                    pageSetupJessInputItem.setEnabled(false);
                }
            }

            public void menuDeselected(MenuEvent evt) {
                // Not used
            }
            public void menuCanceled(MenuEvent evt) {
                // Not used
            }
        });  // end of the file menu listener

        // update the execute Menu items
        executeMenu.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent evt) {
                if (jessRunning == false) {
                    startJessItem.setEnabled(true);
                } else {
                    startJessItem.setEnabled(false);
                    resetJessItem.setEnabled(true);
                    runJessItem.setEnabled(true);
                    stepJessItem.setEnabled(true);    // (run 1) TODD
                    clearJessItem.setEnabled(true);
                    haltJessItem.setEnabled(true);    // (halt)
                    versionJessItem.setEnabled(true);
                    exitJessItem.setEnabled(true);    // (exit) from Jess
                    strategyMenu.setEnabled(true);
                }
            }

            public void menuDeselected(MenuEvent evt) {
                // Not used
            }
            public void menuCanceled(MenuEvent evt) {
                // Not used
            }
        });  // end of edit menu listeners

        // update the Watch Menu items here
        watchWindowMenu.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent evt) {
            	// Not used
            	System.out.println("watchMenu selected");
            }
            public void menuDeselected(MenuEvent evt) {
                // Not used
            }
            public void menuCanceled(MenuEvent evt) {
                // Not used
            }
            public void itemStateChanged(MenuEvent evt) {
                // experimental
                System.out.println("watchMenu, new section");
            }
        });  // end of edit menu listeners

        // load a CLP file and send it to Jess as a batch command
        loadClpItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JFileChooser fc = new JFileChooser();
                fc.addChoosableFileFilter(new ClipsFilter());

                int returnVal = fc.showDialog(JessWin.this, "Load Clips File");

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    filename = new String(file.getAbsolutePath());

                    if (filename.length() >= 1) {
                        try {
                            String text = fileManager.openFile(filename);
                            StringBuffer sbCommand = new StringBuffer("(batch " + filename + ")");

                            // replace the backslash with a forward slash
                            for (int loc = 0; loc < sbCommand.length(); loc++)
                                if (sbCommand.charAt(loc) == '\\')
                                    sbCommand.setCharAt(loc, '/');
                            // if (DEBUG)  System.out.println("batch: " + sbCommand);
                            jcp.processMenuCommand(sbCommand.toString());
                        } catch (IOException e) {
                            // Do nothing - message already printed
                        }
                    }
                }
            }
        });

        saveSessionItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (filename.length() <= 1) {
                    JFileChooser fc = new JFileChooser();
                    int returnVal = fc.showDialog(JessWin.this, "Save As");

                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        filename = new String(file.getAbsolutePath());

                        try {
                            fileManager.newFile(filename);
                            fileManager.saveFile(jcp.getJessHistory());
                        } catch (IOException e) {
                            // Do nothing - message already printed
                        }
                    }
                } else {
                    // Close any open file
                    JFileChooser fc = new JFileChooser();
                    int returnVal = fc.showDialog(JessWin.this, "Save As");

                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        filename = new String(file.getAbsolutePath());

                        try {
                            fileManager.newFile(filename);
                            fileManager.saveFile(jcp.getJessHistory());
                        } catch (IOException e) {
                            // Do nothing - message already printed
                        }
                    }
                }
                // textPane.setEditable(true);  // Always writable
                // fileManager.setWritable(true); // Allow writes to file
                // saveItem.setEnabled(true);  // enable saving to a file
                // saveAsItem.setEnabled(true);
                // addRecentFile(filename);
            }
        });

        depthStrategyMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                // set the state of the breadthStrategyMenuItem to the opposite value
                breadthStrategyMenuItem.setState(!depthStrategyMenuItem.getState());
                jcp.processMenuCommand("(set-strategy depth)");
            }
        });

        breadthStrategyMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                // set the state of the depthStrategyMenuItem to the opposite value
                depthStrategyMenuItem.setState(!breadthStrategyMenuItem.getState());
                jcp.processMenuCommand("(set-strategy breadth)");
            }
        });
        // end of new menu listeners

        // Jess menu items
        // as per Todd, starting Jess automatically when the JessWin GUI comes up
        // retained for posterity and later reference
        startJessItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (DEBUG)  System.out.println("Starting Jess Engine");

                Thread t = new Thread(new Runnable() {
                    public void run() {
                        jessExecute(jessArgs);
                    }
                });
                t.start();
                jcp.setFocus();
                jessRunning = true;
            }
        });

        resetJessItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
               jcp.processMenuCommand("(reset)");
            }
        });

        runJessItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jcp.processMenuCommand("(run)");
            }
        });

        // (run 1) TODD
        stepJessItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jcp.processMenuCommand("(run 1)");
            }
        });

        // (run 1) TODD
        clearJessItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jcp.processMenuCommand("(clear)");
            }
        });

        haltJessItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jcp.processMenuCommand("(halt)");
                jessRunning = false;
            }
        });

        // this is only used when JessWin is standalone
        exitJessItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                // might want to popup a dialog box asking the
                // user if they really want to do this
                // since the 'exit' will kill the JessWin program
                jcp.processMenuCommand("(exit)");
            }
        });

        versionJessItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jcp.processMenuCommand("(jess-version-string)");
            }
        });

        // finally, build the frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // this main screen originally took up the
        // inner 3/4's of the screen, change to the following:
        // set the main window to be 2/3's of the screen with
        // the other 1/3 used for the other watch windows
        scrnWidth  = 2 * screenSize.width  / 3;
        scrnHeight = screenSize.height - 35;  // just above the Windows taskbar

        setSize(scrnWidth, scrnHeight);
        setLocation(0, 0);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // EXIT_ON_CLOSE);  Windows ok, not on Solaris 8

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                JessWin frame = (JessWin)(evt.getSource());
                frame.closeWindow();
            }
        });

        show();
        setVisible(true);
    }

    private void startJessEngine() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                jessExecute(jessArgs);
            }
        });
        t.start();
    }

    public void updateJess(final String command) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                jcp.processMenuCommand(command);
            }
        });
        t.start();
    }

    // Build the recent file menu
    // get the vector of recent commands from the JessConsolePanel
    // and build the vector here
    // add a listener for the commandListMenu so it can update the list
    // and send the selected menu item to the Jess Engine
    public void buildRecentCommandMenu() {
        // get the jcp vectors instead
        recentCommands = jcp.getCommandVector();
        int size = recentCommands.size();

        commandListMenu.removeAll();
        for (int i = 1; i <= size; i++) {
            String commandName = (String)(recentCommands.elementAt(i - 1));
            JMenuItem mi = new JMenuItem();

            mi.setActionCommand(commandName);
            mi.setText(i + " " + commandName);
            if (i >= 1 && i <= 9)
                mi.setMnemonic('0' + i);
            else
                mi.setMnemonic(i);

            commandListMenu.add(mi);
            mi.addActionListener(menuListener);
        }
    }

    // Take any action needed to close the application
    public void closeWindow() {
        m_rete = null;
        System.exit(0);
    }

    public void jessExecute(String [] argv) {
        final Main m = new Main();
        m.initialize(argv, m_rete);

        while (true)
            m.execute(true);
    }

    public Font getCurrentFont() {
        // used by the FontDialog
        return jcp.getCurrentFont();
    }

    public void setCurrentFont(Font font) {
        jcp.setCurrentFont(font);
    }

    public boolean getJessStatus() {
        return jessRunning;
    }

	// gets the WatchWindow status from the WatchWindow closing event
	public void updateWatchWindowStatus(String watchWindowTitle, boolean windowIsOpen) {
		// update the status of the watch window
		// Facts, Rules, Agenda, Defglobals
		if (watchWindowTitle.equals("Facts")) {
			watchFacts = windowIsOpen;
			if (watchFacts) {
				// window is already open, do nothing
			} else {
				// close the window
				factsFrame.setVisible(false);
				factsFrame = null;
			}
		} else if (watchWindowTitle.equals("Rules")) {
			watchRules = windowIsOpen;
			if (watchRules) {
				// window is already open, do nothing
			} else {
				// close the window
				rulesFrame.setVisible(false);
				rulesFrame = null;
			}
		} else if (watchWindowTitle.equals("Agenda")) {
			watchAgenda = windowIsOpen;
			if (watchAgenda) {
				// window is already open, do nothing
			} else {
				// close the window
				agendaFrame.setVisible(false);
				agendaFrame = null;
			}
		} else if (watchWindowTitle.equals("Defglobals")) {
			watchDefglobals = windowIsOpen;
			if (watchDefglobals) {
				// window is already open, do nothing
			} else {
				// close the window
				defglobalsFrame.setVisible(false);
				defglobalsFrame = null;
			}
		} else {
			// should never get here
			System.out.println("Unknown WatchWindow....");
		}
	}

    public static void main(String[] args) {
        JessWin jwin = new JessWin();

        jessArgs = args;  // use for the Jess engine
    }

    static int scrnWidth;
    static int scrnHeight;

    // The organization of the menu bar and all of the items is given below
    // instead of being scattered throughout the first part of this file.

    // File Menu
    JMenu fileMenu;
    JMenuItem loadClpItem;
    JMenuItem saveSessionItem;  // save session as a text file
    JMenuItem jessEditorItem;
    JMenuItem emptyListItem;

    JMenu printMenu;
    JMenu printPreviewerMenu;
    JMenu pageSetupMenu;
    JMenuItem printJessDisplayItem;
    JMenuItem printJessInputItem;
    JMenuItem pageSetupJessDisplayItem;
    JMenuItem pageSetupJessInputItem;
    JMenuItem printPreviewerJessDisplayItem;
    JMenuItem printPreviewerJessInputItem;
    JMenuItem exitItem;
    JMenu commandListMenu;

    // components for the Execute menu
    JMenu executeMenu;
    JMenuItem startJessItem;
    JMenuItem resetJessItem;
    JMenuItem runJessItem;
    JMenuItem stepJessItem;   // (run 1) TODD
    JMenuItem clearJessItem;
    JMenuItem haltJessItem;   // (halt)
    JMenuItem versionJessItem;
    JMenuItem exitJessItem;   // (exit) from Jess

    // added 22 Mar 2001 as per discussions with TODD
    JMenu strategyMenu;
    JCheckBoxMenuItem depthStrategyMenuItem;
    JCheckBoxMenuItem breadthStrategyMenuItem;

    // added 11 Apr 2001, Watch submenu located in the Execute menu
    JMenu executeWatchMenu;
    JCheckBoxMenuItem executeWatchAllMenuItem;
    JCheckBoxMenuItem executeWatchFactsMenuItem;
    JCheckBoxMenuItem executeWatchRulesMenuItem;
    JCheckBoxMenuItem executeWatchAgendaMenuItem;
    JCheckBoxMenuItem executeWatchDefglobalsMenuItem;

    // components for the Watch menu
    JMenu watchWindowMenu;
    JCheckBoxMenuItem watchWindowFactsItem;
    JCheckBoxMenuItem watchWindowRulesItem;
    JCheckBoxMenuItem watchWindowAgendaItem;
    JCheckBoxMenuItem watchWindowDefglobalsItem;

    // new Options menu
    JMenu optionsMenu;
    JMenuItem selectFontItem;

    // components for the Help menu
    JMenu helpMenu;
    JMenuItem jessWinMenuHelpItem;
    JMenuItem jessCommandsHelpItem;

    // item on the About menu
    JMenuItem aboutItem;

    // flags for the watch menu selections
    // used by the menus on the execute, watch submenu
    // and the watch menu
    private boolean watchAll = false;
    private boolean watchFacts = false;
    private boolean watchRules = false;
    private boolean watchAgenda = false;
    private boolean watchDefglobals = false;

    FileManager fileManager;
    String filename = new String();
    String directory = "";  // Current directory
    ActionListener menuListener;

    // Recently-used command list
    static final int RECENT_COMMANDS = 20;
    Vector recentCommands = new Vector(RECENT_COMMANDS);

    static JessEditor  jessEditorFrame;
    static WatchWindow factsFrame;
    static WatchWindow rulesFrame;
    static WatchWindow agendaFrame;
    static WatchWindow defglobalsFrame;

    static JessWinHelpFrame jessWinHelpFrame;
    static JessCommandsHelpFrame jessCommandsHelpFrame;
    private static Rete m_rete = new Rete();
    private static JessConsolePanel jcp = new JessConsolePanel(m_rete);

    // Jess variables
    boolean jessRunning = false;
    static String [] jessArgs;
    private FontDialog fontDlg; // new font dialog box
}

