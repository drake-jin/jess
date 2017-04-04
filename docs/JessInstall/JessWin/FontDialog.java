
// 31 July 2003

// modification of a class from:
// Beginning Java 2, JDK1.3 Edition, Ivor Horton, Wrox Press

// Class to define a dialog to choose a font

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;

class FontDialog extends JDialog implements ActionListener, ListSelectionListener { // For list box

    public FontDialog(JessEditor je) {
        // Call the base constructor to create a modal dialog
        super(je, "Font Selection", true);
        this.je = je;  // Save the window reference
        type = 1;
        buildGUI();
    }

    public FontDialog(JessWin jw) {
        // Call the base constructor to create a modal dialog
        super(jw, "Font Selection", true);
        this.jw = jw;  // Save the window reference
        type = 2;
        buildGUI();
    }

    public void buildGUI() {
        if (type == 1)
            font = je.getCurrentFont();   // Get the current font
        else
            font = jw.getCurrentFont();

        fontName = font.getFontName();
        fontStyle = font.getStyle();    // ...style
        fontSize = font.getSize();      // ...and size

        // Create the dialog button panel
        JPanel buttonPane = new JPanel();  // Create the panel to hold buttons

        // Create and add the buttons to the buttonPane
        buttonPane.add(ok = createButton("OK"));  // Add the OK button
        buttonPane.add(cancel = createButton("Cancel"));  // Add the Cancel button
        getContentPane().add(buttonPane, BorderLayout.SOUTH); // Add pane to content pane

        // Code to create the data input panel...
        JPanel dataPane = new JPanel();    // Create the data entry panel
        dataPane.setBorder(BorderFactory.createCompoundBorder(         // Create pane border
                           BorderFactory.createLineBorder(Color.black),
                           BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        GridBagLayout gbLayout = new GridBagLayout();                  // Create the layout
        dataPane.setLayout(gbLayout);                                  // Set the pane layout
        GridBagConstraints constraints = new GridBagConstraints();

        // Code to create the font choice and add it to the input panel
        JLabel label = new JLabel("Choose a Font");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        gbLayout.setConstraints(label, constraints);
        dataPane.add(label);

        // Code to set up font list choice component
        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontNames = e.getAvailableFontFamilyNames();             // Get the font names

        JLabel fontLabel = new JLabel("Font:");
        fontList = new JList(fontNames);                                // Create list of font names
        fontList.setValueIsAdjusting(true);                             // single event selection
        fontList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Choose 1 font

        fontList.setSelectedValue(font.getFamily(), true);

        fontList.addListSelectionListener(this);
        JScrollPane chooseFont = new JScrollPane(fontList);             // Scrollable list
        JPanel fontPanel = new JPanel();
        fontPanel.setLayout(new BorderLayout());
        fontPanel.add(fontLabel, "North");
        fontPanel.add(chooseFont, "Center");

        // put the styles in a small list
        JLabel styleLabel = new JLabel("Style: ");
        String[] styleNames = new String[] { "Regular", "Bold", "Italic", "Bold Italic" };
        styleList = new JList(styleNames);                                // Create list of font names
        styleList.setValueIsAdjusting(true);
        styleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        styleList.setSelectedIndex(fontStyle);
        styleList.addListSelectionListener(this);
        JScrollPane chooseStyle = new JScrollPane(styleList);             // Scrollable list
        JPanel stylePanel = new JPanel();
        stylePanel.setLayout(new BorderLayout());
        stylePanel.add(styleLabel, "North");
        stylePanel.add(chooseStyle, "Center");

        // Panel to display font sample
        JPanel display = new JPanel();
        fontDisplay = new JTextArea();
        fontDisplay.setFont(font);
        fontDisplay.setText(sampleString);
        fontDisplay.setPreferredSize(new Dimension(300,100));
        display.add(fontDisplay);

        // Set up the size choice using a combobox
        JLabel sizeLabel = new JLabel("Size: ");
        String[] sizeList = { "8", "10", "12", "14", "16", "18", "20", "22", "24"};
        chooseSize = new JList(sizeList);                // Size choice combobox
        chooseSize.setValueIsAdjusting(true);
        chooseSize.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        chooseSize.setSelectedIndex((fontSize - 8) / 2);
        chooseSize.ensureIndexIsVisible(chooseSize.getSelectedIndex());
        chooseSize.addListSelectionListener(this);       // Add size listener
        JPanel sizePanel = new JPanel();                 // Pane for size choices
        sizePanel.setLayout(new BorderLayout());
        sizePanel.add(sizeLabel, "North");
        sizePanel.add(chooseSize, "Center");

        // make a new panel for the fonts, style and size panels
        JPanel fsPanel = new JPanel();
        fsPanel.add(fontPanel);
        fsPanel.add(stylePanel);
        fsPanel.add(sizePanel);

        // Create a split pane with font choice at the top
        // and font display at the bottom
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, fsPanel, display);
        gbLayout.setConstraints(splitPane, constraints);   // Split pane constraints
        dataPane.add(splitPane);                           // Add to the data pane

        JPanel stylePane = new JPanel();                          // Create style pane
        gbLayout.setConstraints(stylePane, constraints);          // Set pane constraints
        dataPane.add(stylePane);                                  // Add the pane

        getContentPane().add(dataPane, BorderLayout.CENTER);
        pack();
        setVisible(false);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                int index = fontList.getSelectedIndex();
                fontList.setSelectedValue(fontName, true);
                fontList.ensureIndexIsVisible(index);
            }
        });
    }

    JButton createButton(String label) {
        JButton button = new JButton(label);                // Create the button
        button.setPreferredSize(new Dimension(80,20));      // Set the size
        button.addActionListener(this);                     // Listener is the dialog
        return button;                                      // Return the button
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();                // Get the source of the event
        if(source == ok) {                            // Is it the OK button?
            // Set the selected font
        if (type == 1)
            je.setCurrentFont(font);
        else
            jw.setCurrentFont(font);
              setVisible(false);                      // Hide the dialog
        } else if (source == cancel) {                // If it is the Cancel button
            setVisible(false);                        // just hide the dialog  setVisible(false);                          // just hide the dialog
        }
    }

    // List selection listener method
    public void valueChanged(ListSelectionEvent e) {
        Object source = e.getSource();                // Get the source of the event

        if (source == fontList) {
            if(!e.getValueIsAdjusting()) {
                fontName = (String)fontList.getSelectedValue();
                font = new Font(font.getFamily(), fontStyle, fontSize);
                fontDisplay.setFont(font);
                fontDisplay.setText("");
                fontDisplay.setText(sampleString);
                fontDisplay.repaint();
            }
        } else if (source == styleList) {
            if(!e.getValueIsAdjusting()) {
                fontStyle = styleList.getSelectedIndex();
                if (fontStyle == 0)
                    font = font.deriveFont(Font.PLAIN);
                else if (fontStyle == 1)
                    font = font.deriveFont(Font.BOLD);
                else if (fontStyle == 2)
                    font = font.deriveFont(Font.ITALIC);
                else if (fontStyle == 3)
                    font = new Font(font.getFamily(), Font.BOLD + Font.ITALIC, fontSize);
                fontDisplay.setFont(font);
                fontDisplay.setText("");
                fontDisplay.setText(sampleString);
                fontDisplay.repaint();
            }
        } else if (source == chooseSize) {
            if(!e.getValueIsAdjusting()) {
                fontSize = Integer.parseInt((String)chooseSize.getSelectedValue());
                font = font.deriveFont((float)fontSize);
                fontDisplay.setFont(font);
                fontDisplay.setText("");
                fontDisplay.setText(sampleString);
                fontDisplay.repaint();
            }
        }
    }

    private int type;
    private JessEditor je;  // was static
    private JessWin    jw;  // was static
    private static Font font;          // Currently selected font
    String  fontName;
    private static int fontStyle;      // Font style - Plain,Bold,Italic
    private int fontSize;              // Font point size
    private JButton ok;                // OK button
    private JButton cancel;            // Cancel button
    private JList fontList;            // Font list
    private JList styleList;           // Style list
    private JList chooseSize;
    private JTextArea fontDisplay;
    private String sampleString = new String("0123456789" + "\n" + "abcdefghijklmnopqrstuvwxyz" + "\n" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
}

