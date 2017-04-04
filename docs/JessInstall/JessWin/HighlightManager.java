
// 31 July 2003
//
// removed validateText method

// rework section that reads the keyword.txt file
// catch the exception if the file is missing

// contains the code for all of the highlighters
// might change this later in the next release

// patterned after a custom highlighter from Kim Topley's book,
// "Core Swing, Advanced Programming

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.*;

public class HighlightManager {
    private static boolean DEBUG = false;

    public HighlightManager(JTextComponent comp) {
        this.comp = comp;

        if (!keywordsCreated) {
            createKeywordList();
        }
    }

    public void searchComments() {
        if (DEBUG)  System.out.println("searchComments");

        if (cpainter == null) {
            cpainter = new CommentHighlighter.CommentHighlightPainter(Color.green);
        }

        Highlighter highlighter = comp.getHighlighter();

        // remove the old comment highlights
        Highlighter.Highlight[] chighlights = highlighter.getHighlights();

        for (int i = 0; i < chighlights.length; i++) {
            Highlighter.Highlight ch = chighlights[i];

            if (ch.getPainter() instanceof CommentHighlighter.CommentHighlightPainter) {
                highlighter.removeHighlight(ch);
            }
        }

        // scan the document for semicolons
        String content = null;

        try {
            Document doc = comp.getDocument();

            // find the length of the document in case there aren't any
            // carriage returns or linefeeds yet

            int doclength = doc.getLength();

            if (doclength < 1)
               return;

            byte[]  databytes = doc.getText(0, doclength).getBytes();

            // find the semicolons (ASCII dec 59)
            // read from the beginning
            int loc = 0;
            int endloc = 0;
            int crlf = 0;

            boolean endfound;

            while (loc < databytes.length) {
                if ((int)databytes[loc] == 59) {
                    endfound = false;

                    // find the next carriage return or linefeed
                    for (crlf = loc; crlf < databytes.length; crlf++) {
                        if ((int)databytes[crlf] == 10 || (int)databytes[crlf] == 13) {
                            endloc = crlf;
                            endfound = true;
                            break;
                        }
                    }
                    if (endfound) {
                        endloc = crlf;
                    } else {
                        endloc = doclength;
                    }

                    try {
                        highlighter.addHighlight(loc, endloc, cpainter);
                    } catch (BadLocationException ble1) {
                        highlighter.removeAllHighlights();
                        System.err.println("\n1.  BadLocationException\n" + ble1);
                    }
                }   // end of if
                loc++;
            }    // end of while
        } catch (BadLocationException ble2) {
            System.err.println("\n2.  BadLocationException\n" + ble2);
        }
    }

    public void searchKeywords() {
        if (DEBUG)  System.out.println("searchKeywords");

        if (!keywordsCreated) { // || updateKeywords)
            createKeywordList();
        }

        if (!keywordsCreated)
            return;

        if (DEBUG)  System.out.println("made it to here");

        if (kpainter == null) {
            kpainter = new KeywordHighlighter.KeywordHighlightPainter(Color.red);
        }

        Highlighter highlighter = comp.getHighlighter();

        // remove the old highlights
        Highlighter.Highlight[] chighlights = highlighter.getHighlights();

        for (int i = 0; i < chighlights.length; i++) {
            Highlighter.Highlight kh = chighlights[i];

            if (kh.getPainter() instanceof KeywordHighlighter.KeywordHighlightPainter) {
                highlighter.removeHighlight(kh);
            }
        }

        String content = null;
        try {
            Document doc = comp.getDocument();
            content = doc.getText(0, doc.getLength());

            if (doc.getLength() < 1)
              return;

            // scan for each word in the document
            for (int wordnumber = 0; wordnumber < keywords.length; wordnumber++) {
                int firstOffset = -1;  // word not found
                int lastIndex = 0;
                int wordSize = keywords[wordnumber].length();

                while ((lastIndex = content.indexOf(keywords[wordnumber], lastIndex)) != -1) {
                    int endIndex = lastIndex + wordSize;

                    try {
                        highlighter.addHighlight(lastIndex, endIndex, kpainter);
                    } catch (BadLocationException ble1) {
                        highlighter.removeAllHighlights();
                        System.err.println("\n1.  BadLocationException\n" + ble1);
                    }
                    if (firstOffset == -1) {
                        firstOffset = lastIndex;
                    }
                    lastIndex = endIndex;
                }
            }
        } catch (BadLocationException ble2) {
            System.err.println("\n2.  BadLocationException\n" + ble2);
        }
    }

    public void searchMatchingParentheses(int position) {
        if (DEBUG)  System.out.println("searchMatchingParentheses");

        if (pmpainter == null) {
            pmpainter = new DefaultHighlighter.DefaultHighlightPainter(Color.lightGray);
        }

        Highlighter highlighter = comp.getHighlighter();

        // remove the old highlights
        Highlighter.Highlight[] chighlights = highlighter.getHighlights();

        for (int i = 0; i < chighlights.length; i++) {
            Highlighter.Highlight ch = chighlights[i];

            if (ch.getPainter() instanceof DefaultHighlighter.DefaultHighlightPainter) {
                highlighter.removeHighlight(ch);
            }
        }

        String content = null;

        int pos = position;

        try {
            Document doc = comp.getDocument();

            if (doc.getLength() < 1)
                return;

            StringBuffer sb = new StringBuffer(doc.getText(0, doc.getLength()));

            char cvalue;
            if (pos < doc.getLength()) {
                cvalue = sb.charAt(pos);
            } else {
                return;
            }

            int lpcount = 0;
            int rpcount = 0;
            boolean matchfound = false;

            // forward match: if a ( search for the matching )
            if (cvalue == '(') {
                // highlight the left parentheses character only
                try {
                    highlighter.addHighlight(pos, pos + 1 , pmpainter);
                } catch (BadLocationException blelp) {  // left parentheses
                    highlighter.removeAllHighlights();
                }
                // find the matching right parentheses by
                // scanning the rest of the file

                // count the left and right parentheses characters
                // in the rest of the text
                for (int i = pos; i < sb.length(); i++) {
                    cvalue = sb.charAt(i);

                    if (cvalue == '(') {
                        lpcount++;
                    }

                    if (cvalue == ')') {
                        rpcount++;

                        if (lpcount == rpcount) {
                            try {
                                highlighter.addHighlight(i, i + 1 , pmpainter);
                                matchfound = true;
                            } catch (BadLocationException blerp) {
                                highlighter.removeAllHighlights();
                            }
                            break;
                        }
                    }
                }  // end of for
                // if no match found, just don't highlight anything
                // this dialog box was too annoying to keep
                /*
                if (!matchfound) {
                    displayMessageFrame("No Match Found.");
                } */
            }  // end of if

            // reverse lookup for the matching (
            lpcount = 0;
            rpcount = 0;

            pos = position;
            if (pos < doc.getLength()) {
                cvalue = sb.charAt(pos);
            } else {
                return;
            }

            if (cvalue == ')') {
                // highlight the right parentheses character only
                try {
                    highlighter.addHighlight(pos, pos + 1 , pmpainter);
                } catch (BadLocationException blelp) { // left parentheses
                    highlighter.removeAllHighlights();
                }
                // find the matching right parentheses by
                // scanning the rest of the file

                // count the left and right parentheses characters
                // in the rest of the text
                for (int i = pos; i >= 0; i-- ) {
                    cvalue = sb.charAt(i);

                    if (cvalue == ')') {
                        rpcount++;
                    }

                    if (cvalue == '(') {
                        lpcount++;
 
                        if (lpcount == rpcount) {
                            try {
                                highlighter.addHighlight(i, i + 1 , pmpainter);
                            } catch (BadLocationException blerp) {
                                highlighter.removeAllHighlights();
                            }
                            break;
                        }
                    }
                }
            }  // end of if
        } catch (BadLocationException ble1) {
            System.err.println("1.  BadLocationException\n" + ble1);
        }
    }

    public void searchFillText(int position) {
        if (DEBUG)  System.out.println("searchFillText");

        if (pmpainter == null) {
            pmpainter = new DefaultHighlighter.DefaultHighlightPainter(Color.lightGray);
        }

        Highlighter highlighter = comp.getHighlighter();

        // remove the old highlights
        Highlighter.Highlight[] chighlights = highlighter.getHighlights();

        for (int i = 0; i < chighlights.length; i++) {
            Highlighter.Highlight ch = chighlights[i];

            if (ch.getPainter() instanceof DefaultHighlighter.DefaultHighlightPainter) {
                highlighter.removeHighlight(ch);
            }
        }

        String content = null;

        int pos = position;

        try {
            Document doc = comp.getDocument();

            if (doc.getLength() < 1)
                return;

            StringBuffer sb = new StringBuffer(doc.getText(0, doc.getLength()));

            char cvalue;
            if (pos < doc.getLength()) {
                cvalue = sb.charAt(pos);
            } else {
                return;
            }

            int lpcount = 0;
            int rpcount = 0;

            // forward match: if a ( search for the matching )
            if (cvalue == '(') {
                // find the matching right parentheses
                // scan the rest of the file

                // count the left and right parentheses characters
                // in the rest of the text
                for (int i = pos; i < sb.length(); i++) {
                    cvalue = sb.charAt(i);

                    if ( cvalue == '(') {
                        lpcount++;
                    }

                    if ( cvalue == ')') {
                        rpcount++;

                        if (lpcount == rpcount) {
                            try {
                                highlighter.addHighlight(pos, i + 1 , pmpainter);
                            } catch (BadLocationException ble2) {
                                highlighter.removeAllHighlights();
                            }
                            break;
                        }
                    }
                }
            } // end of if '('

            // reverse lookup for the matching (
            lpcount = 0;
            rpcount = 0;

            pos = position;
            if (pos < doc.getLength()) {
                cvalue = sb.charAt(pos);
            } else {
                return;
            }

            if (cvalue == ')') {
                // find the matching left parentheses
                // scan the rest of the file

                // count the left and right parentheses characters
                // in the rest of the text
                for (int i = pos; i >= 0; i--) {
                    cvalue = sb.charAt(i);

                    if ( cvalue == ')') {
                        rpcount++;
                    }

                    if ( cvalue == '(') {
                        lpcount++;

                        if (lpcount == rpcount) {
                            try {
                                highlighter.addHighlight(i, pos + 1 , pmpainter);
                            } catch (BadLocationException ble2) {
                                highlighter.removeAllHighlights();
                            }
                            break;
                        }
                    }
                }
            }
        }
        catch (BadLocationException ble2) {
            System.err.println("1.  BadLocationException\n" + ble2);
        }
    }

    // not used at this time
    public void updateHighlights() {
        if (DEBUG)  System.out.println("updateHighlights");
    }

    // not used at this time
    public void setCommentSearch(boolean condition) {
        commentSearch = condition;
        if (DEBUG)  System.out.println("setCommentSearch: " + commentSearch);
    }

    // used
    public void setKeywordSearch(boolean condition) {
        keywordSearch   = condition;
        keywordsCreated = condition;

        if (DEBUG)  System.out.println("setKeywordSearch: " + keywordSearch);
    }

    // not used at this time
    public void setMatchingParenSearch(boolean condition) {
        matchingParenSearch = condition;
        if (DEBUG)  System.out.println("setMatchingParenSearch: " + matchingParenSearch);
    }

    // not used at this time
    public void setFillTextSearch(boolean condition) {
        fillTextSearch = condition;
        if (DEBUG)  System.out.println("setFillTextSearch: " + fillTextSearch);
    }

	// The keywords.txt file is read twice
	// 1. get the number of lines
	// 2. build the word list
	// several other ways to do this but this is fast and flexible
    private void createKeywordList() {
        // read the file "keywords.txt" in the local directory
        int linecount = 0;

        try {
            File file = new File("keywords.txt");
            FileInputStream  fis  = new FileInputStream(file);
            BufferedReader bin  = new BufferedReader(new InputStreamReader(fis));
            String theLine = new String();

            // read the file once to find out the number of words in the list
            while (true) {
                if (theLine == null)  break;  // end of stream
                theLine = bin.readLine();
                linecount++;
            }
            fis.close();
            bin.close();
        } catch (FileNotFoundException fnfe) {
            System.err.println("keywords file not found");
        } catch (IOException ioe) {
            System.err.println("error opening and/or reading the keywords file");
        }

        keywords = new String[linecount - 1];

        try {
            File file2 = new File("keywords.txt");
            FileInputStream  fis2  = new FileInputStream(file2);
            BufferedReader bin2  = new BufferedReader(new InputStreamReader(fis2));
            String theLine = new String();

            // add the words to the array
            int wordcount = 0;
            while (true) {
                // if (theLine == null)  break;  // end of stream
                theLine = bin2.readLine();
                if (theLine != null) {
                    keywords[wordcount] = theLine;
                    if (DEBUG)  System.out.println("2. - " + wordcount + " word: " + keywords[wordcount]);
                    wordcount++;
                    theLine = null;
                } else {
                    break;
                }
            }

            fis2.close();
            bin2.close();

            keywordsCreated = true;
        } catch (FileNotFoundException fnfe) {
            System.err.println("keywords file not found");
        } catch (IOException ioe) {
            System.err.println("error opening and/or reading the keywords file");
        }
    }

    // not used at this time
    /*
    public void updateWordList() {
        if (DEBUG)  System.out.println("updateWordList");
        createKeywordList();
        //updateKeywords = true;
    } */

    public void clearCommentHighlights() {
        Highlighter highlighter = comp.getHighlighter();

        // remove the old comments highlights
        Highlighter.Highlight[] chighlights = highlighter.getHighlights();

        for (int i = 0; i < chighlights.length; i++) {
            Highlighter.Highlight kh = chighlights[i];

            if (kh.getPainter() instanceof CommentHighlighter.CommentHighlightPainter) {
                highlighter.removeHighlight(kh);
            }
        }
    }

    public void clearPatternMatchingHighlights() {
        Highlighter highlighter = comp.getHighlighter();

        // remove the old highlights
        Highlighter.Highlight[] chighlights = highlighter.getHighlights();

        for (int i = 0; i < chighlights.length; i++) {
            Highlighter.Highlight ch = chighlights[i];

            if (ch.getPainter() instanceof DefaultHighlighter.DefaultHighlightPainter) {
                highlighter.removeHighlight(ch);
            }
        }
    }

    public void clearKeywordHighlights() {
        Highlighter highlighter = comp.getHighlighter();

        // remove the old keyword highlights
        Highlighter.Highlight[] chighlights = highlighter.getHighlights();

        for (int i = 0; i < chighlights.length; i++) {
            Highlighter.Highlight kh = chighlights[i];

            if (kh.getPainter() instanceof KeywordHighlighter.KeywordHighlightPainter) {
                highlighter.removeHighlight(kh);
            }
        }
        keywordsCreated = false;
    }

    public void displayMessageFrame(String message) {
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame, message);
    }

    public boolean getFileStatus() {
        return keywordsCreated;
    }

    private boolean keywordsCreated = false;
    // private boolean updateKeywords  = false;

    // list of keywords that will be highlighted
    // reading the keywords from a file
    private static String[] keywords;

    protected JTextComponent comp;
    private   CommentHighlighter.CommentHighlightPainter cpainter;
    private   KeywordHighlighter.KeywordHighlightPainter kpainter;

    // pattern matching uses the same highlighter.painter
    private   DefaultHighlighter.DefaultHighlightPainter pmpainter;
    private   ValidateTextHighlighter.ValidateTextHighlightPainter vtpainter;

    private static boolean commentSearch = false;
    private static boolean keywordSearch = false;
    private static boolean matchingParenSearch = false;
    private static boolean fillTextSearch = false;
}

