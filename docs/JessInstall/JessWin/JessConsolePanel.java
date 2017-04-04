
// TODO 
// 1.  reformat (done) 01 August 2003
// 2.  test extensively before the next release

// WEW notes:
// modification of Jess's ConsolePanel.java
// main changes:
// 1.  using a text area instead of a textfield for input
// 2.  minor changes to the GUI to reflect the new components
// 3.  using a new listener for the text area
// 4.  additional functionality to support input from a menu

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.swing.*;

import jess.*;
import jess.awt.TextReader;
import jess.awt.TextAreaWriter;

/**
 * A basic question-and-answer dialog GUI.
 * This class is a Panel containing input and output text areas for a Rete to use.
 * It uses the TextReader and TextAreaWriter classes to turn textual data into I/O streams.
 * The Console and ConsoleApplet classes both display an instance of this.
 *
 * <P>
 * (C) 1998 E.J. Friedman-Hill and the Sandia Corporation
 * @author E.J. Friedman-Hill
 */

public class JessConsolePanel extends Panel implements Serializable {
    private static final boolean DEBUG = false;

    // this is the TextArea that displays the Jess output
    private static final TextArea ta = new TextArea(40, 40);  // was 10, 40

    // Members used for presenting output
    private TextAreaWriter m_taw;

    // Members used for getting input
    private TextArea m_ta = new TextArea(40, 40);

    // TextReader is in the jess package
    private TextReader m_in;

    // We need the engine to fire events on...
    private static Rete m_rete;

    WatchWindow factsFrame;
    private boolean factsFrameVisible = false;

    WatchWindow rulesFrame;
    private boolean rulesFrameVisible = false;

    WatchWindow agendaFrame;
    private boolean agendaFrameVisible = false;

    // defglobals added by TODD
    WatchWindow globalsFrame;
    private boolean globalsFrameVisible = false;

    // short-cuts added by Todd
    private static final int CTRL_R = 18;      // <ctrl+R> (run)
    private static final int CTRL_T = 20;      // <ctrl+T> (run 1)    [i.e. step]
    private static final int CTRL_U = 21;      // <ctrl+U> (reset)

    private Font textFont = new Font("Monospaced", Font.BOLD, 14);

    Panel p = new Panel();

    private boolean watchReteNet = false;  // was static
    private boolean loseFocus = false;  // was static

    private int numberOfRules = 0;  // was static

    private boolean viewReteNetActive = false;  // was static

    /**
     * Lay out the Panel; also attach the Rete object to
     * the input and output text components.
     * @param r A Rete engine
     */
    public JessConsolePanel(Rete r) {
        // Remember the engine
        m_rete = r;

        // Set up the GUI elements
        ta.setEditable(false);

        // set the fonts for both TextAreas
        ta.setFont(textFont);
        m_ta.setFont(textFont);

        p.setLayout(new BorderLayout());

        // Set up I/O streams
        m_taw = new TextAreaWriter(ta);
        m_in = new TextReader(false);

        // Assemble the GUI
        setLayout(new GridLayout(2, 1));
        add(ta);

        p.add("Center", m_ta);
        add(p);

        m_ta.addKeyListener(new KeyListener() {
            public void keyReleased(KeyEvent e) {
                // TODD
                int code = (int)e.getKeyChar();
                if (code == CTRL_R) {
                    processMenuCommand("(run)");    // Ctrl-R = (run)
                    return;
                } else if (code == CTRL_T) {
                    processMenuCommand("(run 1)");  // Ctrl-T = (run 1)  [step]
                    return;
                } else if (code == CTRL_U) {
                    processMenuCommand("(reset)");  // Ctrl-U = (reset)
                    return;
                }
                // TODD
                Object o = e.getSource();
                if (o == (Object)m_ta) {
                    char c = e.getKeyChar();
                    int keyInteger = e.getKeyCode();
                    if (keyInteger < 32 && keyInteger != 10)  return;  // ignore control characters

                    // count the number of left and right parenthesis characters
                    // if they are equal and the key released was the linefeed,
                    // then send the whole string to Jess
                    int lp_count = 0;  // left parenthesis count
                    int rp_count = 0;  // right parenthesis count

                    String temp = new String(m_ta.getText());
                    for (int count = 0; count < temp.length(); count++) {
                        if (temp.charAt(count) == '(')
                            lp_count++;
                        else if (temp.charAt(count) == ')')
                            rp_count++;
                    }

                    if (keyInteger == 10 && (lp_count == rp_count)) {
                        // send the command to Jess
                        processJessCommands(m_ta.getText());
                    }
                }
            }
            public void keyPressed(KeyEvent e) { }
            public void keyTyped(KeyEvent e)   { }
        });

        // make sure the text area always has the focus 
        m_ta.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent fe) {
                // if (loseFocus  && !watchReteNet)
                if (loseFocus || watchReteNet) {
                    // prevents other windows and frames from being buried by JessWin
                    return;
                }
                setFocus();
            }
        });

        // Configure the Rete object
        PrintWriter pw = new PrintWriter(m_taw, true);
        r.addInputRouter("t", m_in, true);
        r.addOutputRouter("t", pw);
        r.addInputRouter("WSTDIN", m_in, true);
        r.addOutputRouter("WSTDOUT", pw);
        r.addOutputRouter("WSTDERR", pw);

        setFocus();
    }

    /**
     * Move focus to the input area. Helps to call this whenever a button is clicked, etc.
     */
    final public void setFocus() {
        m_ta.requestFocus();
    }

    // ---------- start of section for newer versions of Jess, >= 60a3 ----------
    private void updateFacts() {
        if (DEBUG)  System.out.println("updateFacts");

        Iterator iterator = (Iterator)m_rete.listFacts();

        int count = 0;
        String factsString = new String();
        while (iterator.hasNext()) {
            Fact f = (Fact)iterator.next();
            String ftemp = new String("f-" + f.getFactId() + "\t" + f);
            String countString = new String("" + count);
            if (count == 0)
                factsString = factsString.concat(ftemp);
            else
                factsString = factsString.concat("\n").concat(ftemp);
            count++;
        }
        if (DEBUG)  System.out.println("test facts: " + "\n" + factsString);

        if (factsFrameVisible) {
            if (DEBUG)  System.out.println("factsFrameVisible" + "\n" + factsString);
            factsFrame.SetText(factsString);
        }
    }

    private void updateRules() {
        if (DEBUG)  System.out.println("updateRules");

        Iterator iteratorRules = (Iterator)m_rete.listDefrules();

        String rulesString = new String();
        int countRules = 0;
        while (iteratorRules.hasNext()) {
            Object element = iteratorRules.next();

            String rulePrintout = new String("" + new PrettyPrinter((Defrule)element));

            // the whole rule is in rulePrintout, parse out the
            // defrule and to the end of the first line
            String delimiter = "defrule ";
            int startLocation = rulePrintout.trim().indexOf(delimiter);

            String tempRule = rulePrintout.substring(startLocation + delimiter.length());
            int spaceLocation = tempRule.indexOf(" ");  // find the first space
            int lfLocation = tempRule.indexOf('\n');    // find the end of the line

            String ruleName;
            if (spaceLocation < lfLocation)
                ruleName = new String(tempRule.substring(0, spaceLocation));
            else
                ruleName = new String(tempRule.substring(0, lfLocation));

            if (countRules == 0)
                rulesString = rulesString.concat(ruleName);
            else
                rulesString = rulesString.concat("\n").concat(ruleName);
            countRules++;
        }
        // System.out.println(rulesString);
        numberOfRules = countRules;  // count;

        if (rulesFrameVisible) {
            if (DEBUG)  System.out.println("rulesFrameVisible" + "\n" + rulesString);
            rulesFrame.SetText(rulesString);
        }
    }

    private void updateAgenda() {
        if (DEBUG)  System.out.println("updateAgenda");

        Iterator iterator = (Iterator)m_rete.listActivations();

        String agendaString = new String();
        int count = 0;
        while (iterator.hasNext()) {
            Object element = iterator.next();
            String temp = new String("" + element);
            temp = temp.trim();

            // clean up the data sent to the screen
            // remove '[Activations: ' and the trailing ']'
            temp = temp.substring(temp.indexOf(": ") + 2);
            temp = temp.substring(0, temp.length() - 1);
            if (count == 0)
                agendaString = agendaString.concat(temp);
            else
                agendaString = agendaString.concat("\n").concat(temp);
            count++;
        }

        if (agendaFrameVisible) {
            if (DEBUG)  System.out.println("agendaFrameVisible" + "\n" + agendaString);
            agendaFrame.SetText(agendaString);
        }
    }

    // something broke this section that used to work fine
    // fixed 21 aug 2001 for Jess60a8, might break for previous versions???
    private void updateGlobals() {
        if (DEBUG)  System.out.println("updateGlobals");

        Iterator iterator = (Iterator)m_rete.listDefglobals();
        Context context = m_rete.getGlobalContext();

        String globalsString = new String();
        int count = 0;
        while (iterator.hasNext()) {
            Object element = iterator.next();
            String temp = new String("" + element);
            temp = temp.trim();

            System.out.println("temp 1: " + temp);

            // clean up the data sent to the screen so it looks like global = value
            int space_loc = 0;
            int bracket_loc = 0;
            space_loc = temp.indexOf(" ") + 1;
            temp = temp.substring(space_loc);
            // System.out.println("temp 2: " + temp);
            bracket_loc = temp.indexOf("]") + 1;
            temp = temp.substring(0, bracket_loc - 1);
            // System.out.println("temp 3: " + temp);
            // wew note: bug in here someplace, replaced with the above
            // int startofdef = temp.indexOf("defglobal") + 10;
            // int endofdef = temp.indexOf(" ", startofdef + 1);
            // String name = temp.substring(startofdef, endofdef);
            // name = name.substring(1);             // bypass ? character
            try {
                // String val = context.getVariable(name).toStringWithParens();
                String val = context.getVariable(temp).toStringWithParens();
                // System.out.println("val: " + val);
                // temp = "?" + name + " = " + val;
                temp = "?" + temp + " = " + val;
            } catch (JessException e) {
                // temp = "?" + name + " = UNKNOWN - Reason " + e;
                temp = "?" + temp + " = UNKNOWN - Reason " + e;
            }

            if (count == 0)
                globalsString = globalsString.concat(temp);
            else
                globalsString = globalsString.concat("\n").concat(temp);

            iterator.next();  // this should not be necessary
            count++;
        }

        if (globalsFrameVisible) {
            if (DEBUG)  System.out.println("globalsFrameVisible" + "\n" + globalsString);
            globalsFrame.SetText(globalsString);
        }
    }

    // used to update the factsFrame GUI
    public void setFactsFrameVisibility(boolean isVisible) {
        factsFrameVisible = isVisible;
    }

    public void assignFactsFrame(WatchWindow factsFrame) {
        // remember the facts window
        this.factsFrame = factsFrame;
    }

    // used to update the rulesFrame GUI 
    public void setRulesFrameVisibility(boolean isVisible) {
        rulesFrameVisible = isVisible;
    }

    public void assignRulesFrame(WatchWindow rulesFrame) {
        // remember the rules window
        this.rulesFrame = rulesFrame;
    }

    // used to update the agendaFrame GUI
    public void setAgendaFrameVisibility(boolean isVisible) {
        agendaFrameVisible = isVisible;
    }

    public void assignAgendaFrame(WatchWindow agendaFrame) {
        // remember the agenda window
        this.agendaFrame = agendaFrame;
    }

    public void setDefglobalsFrameVisibility(boolean isVisible) {
        globalsFrameVisible = isVisible;
    }

    public void assignGlobalsFrame(WatchWindow globalsFrame) {
        // remember the globals window
        this.globalsFrame = globalsFrame;
    }

    public void processMenuCommand(String command) {
        if (DEBUG)  System.out.println("processing Jess command from the IDE's menu: " + command);

        // menu entries will not end with the linefeed
        if (command.charAt(command.length() - 1) == ')')
            command = command.concat("\n");

        processJessCommands(command);
    }

    private void processJessCommands(String command) {
        synchronized (ta) {
            try {
                m_taw.write(command);
                m_taw.flush();
            } catch (IOException ioe) {
                // Can't really happen
            }
        }
        m_in.appendText(command + "\n");
        m_ta.setText("");

        // now go and do any other processing to update the watch windows
        updateWatchWindows(command);
    }

    // might need to make changes here as testing continues
    private void updateWatchWindows(String command) {
        boolean clearFlag = false;
        // some commands require separate processing to update
        // the various watch windows
        // added options for the agenda, defglobals, watch selections
        // anything that changes the facts, rules, agenda or defglobals
        command = command.trim();
        if (DEBUG)  System.out.println("1.  command: " + command);

        if (command.indexOf("(assert")     != -1 ||
            // command.indexOf("(run")        != -1 ||    // removed for run 1 bug fix
            // command.indexOf("(run 1")        != -1 ||  // removed for run 1 bug fix
            command.indexOf("(clear)")     != -1 ||
            command.indexOf("(batch")      != -1 ||
            command.indexOf("(load-facts") != -1 ||
            command.indexOf("(reset")      != -1 ||
            command.indexOf("(retract")    != -1 ||
            command.indexOf("(defrule")    != -1 ||
            command.indexOf("(undefrule")  != -1 ||
            command.indexOf("(defglobal")  != -1 ||
            command.indexOf("(run-query")  != -1 ||
            command.indexOf("(set-reset-globals") != -1 ||
            command.indexOf("(set-salience-evaluation") != -1 ||
            (command.indexOf("(watch") != -1 && command.indexOf("all") != -1)   ||
            (command.indexOf("(watch") != -1 && command.indexOf("facts") != -1)   ||
            (command.indexOf("(watch") != -1 && command.indexOf("rules") != -1)   ||
            (command.indexOf("(unwatch") != -1 && command.indexOf("all") != -1) ||
            (command.indexOf("(unwatch") != -1 && command.indexOf("facts") != -1) ||
            (command.indexOf("(unwatch") != -1 && command.indexOf("rules") != -1) )   { // start of if block
            	
            // force the assertion of the fact into the facts table
//            try {
//                // an exception is thrown if I send a 'retract' directly to Jess
//                if (command.indexOf("(retract") == -1) {
//                    if (DEBUG)  System.out.println("2. executing command");
//                    m_rete.executeCommand(command);
//                }
//                if (command.indexOf("(clear)") != -1) {
//                    clearFlag = true;
//                }
//            } catch (JessException je) {
//                // Jess will display this in its own area
//                // also print it out in a dialog box for easier viewing
//                String errMessage = new String("Jess Exception: " + "\n" + je);
//
//                JFrame frame = new JFrame();
//                JOptionPane.showMessageDialog(frame, errMessage);
//                frame = null;
//            }
        }
        updateFacts();
        updateRules();
        updateAgenda();
        updateGlobals();

        // fix for Jess60a5 bug where the 'clear' command also unloads the
        // ViewFunctions functionality, this reloads that package
        if (clearFlag) {
            try {
                m_rete.executeCommand("(load-package jess.ViewFunctions)");
            } catch (JessException je) {
                // Jess will display this in its own area
                // also print it out in a dialog box for easier viewing
                String errMessage = new String("Jess Exception: " + "\n" + je);

                JFrame frame = new JFrame();
                JOptionPane.showMessageDialog(frame, errMessage);
                frame = null;
            }
        }

        // prevent the view Rete frame from being buried behind JessWin
        if (command.indexOf("(view") != -1) {
            System.out.println("watch ReteNet is on");
            watchReteNet = true;
        }

        // must be a valid command to make it this far
        // add it to the recent command vector
        addRecentCommand(command);  // v1.14
    }

    // also used to get the text for printing
    public String getJessHistory() {
        // returns the text area so it can be saved to a history file
        // note: ta.getText() must add extra carriage return characters
        // because the spacing is off

        byte[]  databytes = ta.getText().getBytes();
        int ivalue;
        for (int i = 0; i < databytes.length; i++) {
            ivalue = (int)databytes[i];
            if (ivalue == 13) {
                databytes[i] = (byte)' ';
            }
        }
        String history = new String(databytes);
        return history;
    }

    public String getJessInputText() {
        // returns the text area so it can be saved to a history file
        return m_ta.getText();
    }

    public String getJessVersion() {
        try {
            Value v = m_rete.executeCommand("(jess-version-string)");
            return ("" + v.stringValue(m_rete.getGlobalContext()));
        } catch (JessException je) {
            String errMessage = new String("Jess Exception: " + "\n" + je);

            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, errMessage);
            frame = null;
            return " unknown ";
        }
    }

    public void giveUpFocus(boolean focus) {
        this.loseFocus = focus;
    }

    public void setReteNetView(boolean watchReteNet) {
        this.watchReteNet = watchReteNet;
    }

    // bug fix for v1.14
    public boolean getReteNetView() {
       return watchReteNet;
    }

    public void addRecentCommand(String commandName) {
        int offset;

        // Check whether this file is already in the file list
        if ((offset = recentCommands.indexOf(commandName)) == -1) {
            // Not present - add it at the beginning
            recentCommands.insertElementAt(commandName, 0);

            if (recentCommands.size() > RECENT_COMMANDS) {
                // List was full - remove the last (oldest) item
                recentCommands.removeElementAt(RECENT_COMMANDS);
            }
        } else {
            // Already present: move this item to the front of the list
            recentCommands.removeElementAt(offset);
            recentCommands.insertElementAt(commandName, 0);
        }
    }

    public Vector getCommandVector() {
        return recentCommands;
    }

    public Font getCurrentFont() {
        // used by the FontDialog
        return textFont;
    }

    public void setCurrentFont(Font font) {
        textFont = font;
        ta.setFont(textFont);
        m_ta.setFont(textFont);

        String temp1 = new String(ta.getText());
        String temp2 = new String(m_ta.getText());

        ta.setText("");
        ta.setText(temp1);
        m_ta.setText("");
        m_ta.setText(temp2);
    }

    static final int RECENT_COMMANDS = 20;
    Vector recentCommands = new Vector(RECENT_COMMANDS);
}

