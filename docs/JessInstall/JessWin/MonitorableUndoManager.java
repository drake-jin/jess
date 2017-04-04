
// 31 July 2003

// Note, this file is from Topley's book on Advanced Swing, an excellent text
// used here for the undo/redo functionality for the JessEditor

// package AdvancedSwing.Chapter9;

import javax.swing.event.*;
import javax.swing.undo.*;

public class MonitorableUndoManager extends UndoManager {

    // List of listeners for events from this object
    protected EventListenerList listenerList = new EventListenerList();

    // A ChangeEvent dedicated to a single MonitorableUndoManager
    protected ChangeEvent changeEvent;

    // Super class overrides
    public synchronized void setLimit(int l) {
        super.setLimit(l);
        fireChangeEvent();
    }

    public synchronized void discardAllEdits() {
        super.discardAllEdits();
        fireChangeEvent();
    }

    public synchronized void undo() throws CannotUndoException {
        super.undo();
        fireChangeEvent();
    }

    public synchronized void redo() throws CannotRedoException {
        super.redo();
        fireChangeEvent();
    }

    public synchronized boolean addEdit(UndoableEdit anEdit) {
        boolean retval = super.addEdit(anEdit);
        fireChangeEvent();
        return retval;
    }

    // Support for ChangeListeners
    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    protected void fireChangeEvent() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener)listeners[i+1]).stateChanged(changeEvent);
            }
        }
    }
}
