package org.argouml.kernel;


public interface Command {
	public abstract Object execute();
	abstract void undo();
	abstract boolean isUndoable();
	abstract boolean isRedoable();
}



