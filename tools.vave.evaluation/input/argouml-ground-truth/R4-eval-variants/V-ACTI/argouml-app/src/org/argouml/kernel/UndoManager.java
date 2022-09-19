package org.argouml.kernel;

import java.beans.PropertyChangeListener;


public interface UndoManager {
	public abstract void addCommand(Command command);
	public abstract Object execute(Command command);
	public abstract void setUndoMax(int max);
	public abstract void undo();
	public abstract void redo();
	public abstract void startInteraction(String label);
	public abstract void addPropertyChangeListener(PropertyChangeListener listener);
	public abstract void removePropertyChangeListener(PropertyChangeListener listener);
}



