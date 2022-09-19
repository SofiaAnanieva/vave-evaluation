package org.argouml.uml.diagram;

import java.beans.PropertyChangeListener;
import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.tigris.gef.undo.Memento;
import org.tigris.gef.undo.UndoManager;


public class DiagramUndoManager extends UndoManager {
	private static final Logger LOG = Logger.getLogger(UndoManager.class);
	private boolean startChain;
	@Override public void startChain() {
		startChain = true;
	}
	@Override public boolean isGenerateMementos() {
		Project p = ProjectManager.getManager().getCurrentProject();
		return super.isGenerateMementos()&&p != null&&p.getUndoManager() != null;
	}
	@Override public void addMemento(final Memento memento) {
		Project p = ProjectManager.getManager().getCurrentProject();
		if (p != null) {
			org.argouml.kernel.
				UndoManager undo = p.getUndoManager();
			if (undo != null) {
				if (startChain) {
					undo.startInteraction("Diagram Interaction");
				}
				undo.addCommand(new DiagramCommand(memento));
				startChain = false;
			}
		}
	}
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		LOG.info("Adding property listener " + listener);
		super.addPropertyChangeListener(listener);
	}
	private class DiagramCommand extends org.argouml.kernel.AbstractCommand {
	private final Memento memento;
	DiagramCommand(final Memento theMemento) {
			this.memento = theMemento;
		}
	@Override public Object execute() {
		memento.redo();
		return null;
	}
	@Override public void undo() {
		memento.undo();
	}
	@Override public String toString() {
		return memento.toString();
	}
}
}



