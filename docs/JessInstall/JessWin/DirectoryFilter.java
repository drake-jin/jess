
// 31 July 2003

// WEW note: returns the names of directories

import java.io.File;

public class DirectoryFilter extends javax.swing.filechooser.FileFilter {
    // accept directories
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        } else {
            return false;
        }
    }

    // description of this filter
    public String getDescription() {
        return "Directory only.";
    }
}

