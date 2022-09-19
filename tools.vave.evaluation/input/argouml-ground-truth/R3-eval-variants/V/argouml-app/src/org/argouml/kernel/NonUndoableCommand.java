package org.argouml.kernel;


public abstract class NonUndoableCommand implements Command {
	public abstract Object execute();
	public void undo() {
	}
	public boolean isUndoable() {
		return false;
	}
	public boolean isRedoable() {
		return false;
	}
}



