package org.argouml.kernel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;
import org.argouml.i18n.Translator;


class DefaultUndoManager implements UndoManager {
	private int undoMax = 0;
	private ArrayList<PropertyChangeListener>listeners = new ArrayList<PropertyChangeListener>();
	private boolean newInteraction = true;
	private String newInteractionLabel;
	private UndoStack undoStack = new UndoStack();
	private RedoStack redoStack = new RedoStack();
	private static final UndoManager INSTANCE = new DefaultUndoManager();
	private DefaultUndoManager() {
		super();
	}
	@Deprecated public static UndoManager getInstance() {
		return INSTANCE;
	}
	public synchronized Object execute(Command command) {
		addCommand(command);
		return command.execute();
	}
	public synchronized void addCommand(Command command) {
		ProjectManager.getManager().setSaveEnabled(true);
		if (undoMax == 0) {
			return;
		}
		if (!command.isUndoable()) {
			undoStack.clear();
			newInteraction = true;
		}
		final Interaction macroCommand;
		if (newInteraction||undoStack.isEmpty()) {
			redoStack.clear();
			newInteraction = false;
			if (undoStack.size() > undoMax) {
				undoStack.remove(0);
			}
			macroCommand = new Interaction(newInteractionLabel);
			undoStack.push(macroCommand);
		}else {
			macroCommand = undoStack.peek();
		}
		macroCommand.addCommand(command);
	}
	public void setUndoMax(int max) {
		undoMax = max;
	}
	public synchronized void undo() {
		final Interaction command = undoStack.pop();
		command.undo();
		if (!command.isRedoable()) {
			redoStack.clear();
		}
		redoStack.push(command);
	}
	public synchronized void redo() {
		final Interaction command = redoStack.pop();
		command.execute();
		undoStack.push(command);
	}
	public synchronized void startInteraction(String label) {
		this.newInteractionLabel = label;
		newInteraction = true;
	}
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.add(listener);
	}
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.remove(listener);
	}
	private void fire(final String property,final Object value) {
		for (PropertyChangeListener listener:listeners) {
			listener.propertyChange(new PropertyChangeEvent(this,property,"",value));
		}
	}
	class Interaction extends AbstractCommand {
		private List<Command>commands = new ArrayList<Command>();
		private String label;
		Interaction(String lbl) {
				label = lbl;
			}
		public void undo() {
			final ListIterator<Command>it = commands.listIterator(commands.size());
			while (it.hasPrevious()) {
				Command c = it.previous();
				c.undo();
			}
		}
		public Object execute() {
			final Iterator<Command>it = commands.iterator();
			while (it.hasNext()) {
				Command c = it.next();
				c.execute();
			}
			return null;
		}
		public boolean isUndoable() {
			final Iterator<Command>it = commands.iterator();
			while (it.hasNext()) {
				final Command command = it.next();
				if (!command.isUndoable()) {
					return false;
				}
			}
			return true;
		}
		public boolean isRedoable() {
			final Iterator<Command>it = commands.iterator();
			while (it.hasNext()) {
				final Command command = it.next();
				if (!command.isRedoable()) {
					return false;
				}
			}
			return true;
		}
		private void addCommand(Command command) {
			commands.add(command);
		}
		private String getUndoLabel() {
			if (isUndoable()) {
				return"Undo " + label;
			}else {
				return"Can\'t Undo " + label;
			}
		}
		private String getRedoLabel() {
			if (isRedoable()) {
				return"Redo " + label;
			}else {
				return"Can\'t Redo " + label;
			}
		}
		List<Command>getCommands() {
			return new ArrayList<Command>(commands);
		}
	}
	private abstract class InteractionStack extends Stack<Interaction> {
	private String labelProperty;
	private String addedProperty;
	private String removedProperty;
	private String sizeProperty;
	public InteractionStack(String labelProp,String addedProp,String removedProp,String sizeProp) {
		labelProperty = labelProp;
		addedProperty = addedProp;
		removedProperty = removedProp;
		sizeProperty = sizeProp;
	}
	public Interaction push(Interaction item) {
		super.push(item);
		fireLabel();
		fire(addedProperty,item);
		fire(sizeProperty,size());
		return item;
	}
	public Interaction pop() {
		Interaction item = super.pop();
		fireLabel();
		fire(removedProperty,item);
		fire(sizeProperty,size());
		return item;
	}
	private void fireLabel() {
		fire(labelProperty,getLabel());
	}
	protected abstract String getLabel();
}
	private class UndoStack extends InteractionStack {
	public UndoStack() {
		super("undoLabel","undoAdded","undoRemoved","undoSize");
	}
	public Interaction push(Interaction item) {
		super.push(item);
		if (item.isUndoable()) {
			fire("undoable",true);
		}
		return item;
	}
	public Interaction pop() {
		Interaction item = super.pop();
		Interaction interaction = peek();
		if (size() == 0||!interaction.isUndoable()) {
			fire("undoable",false);
		}
		return item;
	}
	public void clear() {
		super.clear();
		fire("undoSize",size());
		fire("undoable",false);
	}
	protected String getLabel() {
		if (empty()) {
			return Translator.localize("action.undo");
		}else {
			Interaction interaction = peek();
			return interaction.getUndoLabel();
		}
	}
}
	private class RedoStack extends InteractionStack {
	public RedoStack() {
		super("redoLabel","redoAdded","redoRemoved","redoSize");
	}
	public Interaction push(Interaction item) {
		super.push(item);
		if (item.isRedoable()) {
			fire("redoable",true);
		}
		return item;
	}
	public Interaction pop() {
		Interaction item = super.pop();
		Interaction interaction = peek();
		if (size() == 0||!interaction.isRedoable()) {
			fire("redoable",false);
		}
		return item;
	}
	public void clear() {
		super.clear();
		fire("redoSize",size());
		fire("redoable",false);
	}
	protected String getLabel() {
		if (empty()) {
			return Translator.localize("action.redo");
		}else {
			Interaction interaction = peek();
			return interaction.getRedoLabel();
		}
	}
}
}



