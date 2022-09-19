package org.argouml.model;


public abstract class ModelMemento {
	public abstract void undo();
	public abstract void redo();
	public void dispose() {
	}
}



