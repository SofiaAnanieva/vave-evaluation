package org.argouml.model;


public abstract class ModelCommand {
	public abstract Object execute();
	public abstract boolean isUndoable();
	public abstract boolean isRedoable();
	public abstract void undo();
}



