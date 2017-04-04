
// 31 July 2003

/**
 * This is a modification of other code, probably something from the Sun Tutorial.
 * Returns the names of Clip files, most popular extensions.
 * Minor formatting cleanup 31 July 2003
 *
 * @author  William E. Wheeler
 * @version 1.0
 * @since   31 July 2003
 *
 */

import java.io.File;

public class ClipsFilter extends javax.swing.filechooser.FileFilter {
    // accept directory and clp files
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        if (f.getName().endsWith(".clp") || f.getName().endsWith(".clip") || f.getName().endsWith(".clips")) {
            return true;
        } else {
            return false;
        }
    }

    // description of this filter
    public String getDescription() {
        return "Clip files only.";
    }
}

