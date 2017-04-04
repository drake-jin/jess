
// 31 July 2003

// This is a modification of the FileManager.java file from
// Kim Topley's book, "Core Java Foundation Classes"
// published by Prentice Hall
// manages File operations

import java.io.*;
import java.awt.Toolkit;
import javax.swing.*;

// Helper class to manage files
public class FileManager {
    // Check whether a named file exists
    boolean fileExists(String fileName) {
        return (new File(fileName)).exists();
    }

    // Open a new file
    void newFile(String fileName) throws IOException {
        if (fileExists(fileName)) {
            Toolkit.getDefaultToolkit().beep();
            System.out.println("File <" + fileName + "> already exists.");
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "File <" + fileName + "> already exists.");
            throw new IOException("File exists");
        }
        this.canWriteFile = true;  // Assume we can write
        this.userWantsWrite = true;
        this.fileName = fileName;
    }

    // Open a file
    String openFile(String fileName) throws IOException, FileNotFoundException {
        this.fileName = fileName;
        openFile = new File(fileName);
        try {
            canWriteFile = openFile.canWrite();
            FileInputStream fs = new FileInputStream(openFile);

            long l = openFile.length();
            byte[] content = new byte[(int)l];

            fs.read(content);
            fs.close();

            return new String(content);
        } catch (FileNotFoundException e) {
            Toolkit.getDefaultToolkit().beep();
            System.out.println("Failed to open \"" + fileName + "\".");
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "Failed to open \"" + fileName + "\".");
            throw e;
        } catch (IOException e) {
            Toolkit.getDefaultToolkit().beep();
            System.out.println("Failed to read \"" + fileName + "\".");
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "Failed to read \"" + fileName + "\".");
            throw e;
        }
    }

    // Save to the file
    void saveFile(String text) throws IOException {
        if (fileName != null && userWantsWrite == true && canWriteFile == true) {
            openFile = new File(fileName);
            try {
                FileOutputStream fs = new FileOutputStream(openFile);
                byte[] content = text.getBytes();
                fs.write(content);
                fs.close();
            } catch (FileNotFoundException e) {
                Toolkit.getDefaultToolkit().beep();
                System.out.println("Failed to open \"" + fileName + "\" for writing.");
                JFrame frame = new JFrame();
                JOptionPane.showMessageDialog(frame, "Failed to open \"" + fileName + "\" for writing.");
                throw e;
            } catch (IOException e) {
                Toolkit.getDefaultToolkit().beep();
                System.out.println("Failed to write to \"" + fileName + "\".");
                JFrame frame = new JFrame();
                JOptionPane.showMessageDialog(frame, "Failed to write to \"" + fileName + "\".");
                throw e;
            }
        }
    }

    // Save with a different file name
    void saveAsFile(String fileName, String text) throws IOException {
        this.fileName = fileName;
        // set attributes to true so locked files can be saved
        // under another name
        userWantsWrite = true;
        canWriteFile = true;
        if (userWantsWrite && canWriteFile == true) {
            saveFile(text);
        }
    }

    // Close the open file, writing data if required
    void closeFile(String text) throws IOException {
        if (isFileOpen()) {
            // Write file content
            saveFile(text);
        }
        fileName = null;
        openFile = null;
    }

    // Make the file writable or not
    public void setWritable(boolean writable) {
        this.userWantsWrite = writable;
    }

    // Check whether we have an open file
    boolean isFileOpen() {
        return openFile != null;
    }

    // Check whether we can write to the file
    boolean canWrite() {
        return canWriteFile;
    }

    // Check whether we can run a program
    boolean canRunProgram() {
        return canRun;
    }

    // Get current file name
    String getFileName() {
        return fileName;
    }

    File openFile;       // File object for open file
    String fileName;     // Name of current open file
    String convertName;  // Name of converted file

    boolean userWantsWrite = true;  // User wants to be able to write
    boolean canWriteFile = true;    // File can be written to
    boolean canRun = false;
}
