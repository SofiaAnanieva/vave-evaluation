package org.argouml.kernel;


public abstract class AbstractCommand implements Command {
	public abstract Object execute();
	public abstract void undo();
	public boolean isUndoable() {
		return true;
	}
	public boolean isRedoable() {
		return true;
	}
}



