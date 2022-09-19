package org.argouml.model.euml;

import org.argouml.model.euml.EUMLModelImplementation;


public class CommandStackImpl {
	private EUMLModelImplementation modelImplementation;
	public CommandStackImpl(EUMLModelImplementation implementation) {
		modelImplementation = implementation;
		implementation.getEditingDomain().getCommandStack().flush();
	}
	public boolean canRedo() {
		return modelImplementation.getEditingDomain().getCommandStack().canRedo();
	}
	public boolean canUndo() {
		return modelImplementation.getEditingDomain().getCommandStack().canUndo();
	}
	public String getRedoLabel() {
		return canRedo()?modelImplementation.getEditingDomain().getCommandStack().getRedoCommand().getLabel():null;
	}
	public String getUndoLabel() {
		return canUndo()?modelImplementation.getEditingDomain().getCommandStack().getUndoCommand().getLabel():null;
	}
	public boolean isCommandStackCapabilityAvailable() {
		return true;
	}
	public void redo() {
		modelImplementation.getEditingDomain().getCommandStack().redo();
	}
	public void undo() {
		modelImplementation.getEditingDomain().getCommandStack().undo();
	}
}



