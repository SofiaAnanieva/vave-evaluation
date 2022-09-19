package org.argouml.model;


public class DummyModelCommand extends ModelCommand {
	@Override public void undo() {
	}
	@Override public Object execute() {
		return null;
	}
	@Override public boolean isUndoable() {
		return false;
	}
	@Override public boolean isRedoable() {
		return false;
	}
}



