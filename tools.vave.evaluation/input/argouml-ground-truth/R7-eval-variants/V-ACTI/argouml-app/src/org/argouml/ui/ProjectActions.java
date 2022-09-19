package org.argouml.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.UndoManager;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramUtils;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.argouml.uml.diagram.ui.ActionRemoveFromDiagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;


public final class ProjectActions implements TargetListener,PropertyChangeListener {
	private static ProjectActions theInstance;
	private ProjectActions() {
		super();
		undoAction = new ActionUndo(Translator.localize("action.undo"),ResourceLoaderWrapper.lookupIcon("Undo"));
		undoAction.setEnabled(false);
		redoAction = new ActionRedo(Translator.localize("action.redo"),ResourceLoaderWrapper.lookupIcon("Redo"));
		redoAction.setEnabled(false);
		TargetManager.getInstance().addTargetListener(this);
		ProjectManager.getManager().getCurrentProject().getUndoManager().addPropertyChangeListener(this);
	}
	private final ActionUndo undoAction;
	private final AbstractAction redoAction;
	public static synchronized ProjectActions getInstance() {
		if (theInstance == null) {
			theInstance = new ProjectActions();
		}
		return theInstance;
	}
	private final ActionRemoveFromDiagram removeFromDiagram = new ActionRemoveFromDiagram(Translator.localize("action.remove-from-diagram"));
	public AbstractAction getUndoAction() {
		return undoAction;
	}
	public AbstractAction getRedoAction() {
		return redoAction;
	}
	public AbstractAction getRemoveFromDiagramAction() {
		return removeFromDiagram;
	}
	public void targetAdded(TargetEvent e) {
		determineRemoveEnabled();
	}
	public void targetRemoved(TargetEvent e) {
		determineRemoveEnabled();
	}
	public void targetSet(TargetEvent e) {
		determineRemoveEnabled();
	}
	private void determineRemoveEnabled() {
		Editor editor = Globals.curEditor();
		Collection figs = editor.getSelectionManager().getFigs();
		boolean removeEnabled = !figs.isEmpty();
		GraphModel gm = editor.getGraphModel();
		if (gm instanceof UMLMutableGraphSupport) {
			removeEnabled = ((UMLMutableGraphSupport) gm).isRemoveFromDiagramAllowed(figs);
		}
		removeFromDiagram.setEnabled(removeEnabled);
	}
	public static void jumpToDiagramShowing(List targets) {
		if (targets == null||targets.size() == 0) {
			return;
		}
		Object first = targets.get(0);
		if (first instanceof ArgoDiagram&&targets.size() > 1) {
			setTarget(first);
			setTarget(targets.get(1));
			return;
		}
		if (first instanceof ArgoDiagram&&targets.size() == 1) {
			setTarget(first);
			return;
		}
		Project project = ProjectManager.getManager().getCurrentProject();
		if (project == null) {
			return;
		}
		List<ArgoDiagram>diagrams = project.getDiagramList();
		Object target = TargetManager.getInstance().getTarget();
		if ((target instanceof ArgoDiagram)&&((ArgoDiagram) target).countContained(targets) == targets.size()) {
			setTarget(first);
			return;
		}
		ArgoDiagram bestDiagram = null;
		int bestNumContained = 0;
		for (ArgoDiagram d:diagrams) {
			int nc = d.countContained(targets);
			if (nc > bestNumContained) {
				bestNumContained = nc;
				bestDiagram = d;
			}
			if (nc == targets.size()) {
				break;
			}
		}
		if (bestDiagram != null) {
			if (!DiagramUtils.getActiveDiagram().equals(bestDiagram)) {
				setTarget(bestDiagram);
			}
			setTarget(first);
		}
		if (project.getRoots().contains(first)) {
			setTarget(first);
		}
		Object f = TargetManager.getInstance().getFigTarget();
		if (f instanceof Fig) {
			Globals.curEditor().scrollToShow((Fig) f);
		}
	}
	private static void setTarget(Object o) {
		TargetManager.getInstance().setTarget(o);
	}
	public void propertyChange(final PropertyChangeEvent evt) {
		if (evt.getSource()instanceof UndoManager) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					if ("undoLabel".equals(evt.getPropertyName())) {
						undoAction.putValue(AbstractAction.NAME,evt.getNewValue());
					}
					if ("redoLabel".equals(evt.getPropertyName())) {
						redoAction.putValue(AbstractAction.NAME,evt.getNewValue());
					}
					if ("undoable".equals(evt.getPropertyName())) {
						undoAction.setEnabled((Boolean) evt.getNewValue());
					}
					if ("redoable".equals(evt.getPropertyName())) {
						redoAction.setEnabled((Boolean) evt.getNewValue());
					}
				}
			});
		}
	}
}



