package org.argouml.uml.ui;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.List;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import org.apache.log4j.Logger;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.UmlModelMutator;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.CommentEdge;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.util.ArgoFrame;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigTextEditor;
import org.tigris.gef.undo.UndoableAction;


@UmlModelMutator public class ActionDeleteModelElements extends UndoableAction {
	private static final long serialVersionUID = -5728400220151823726l;
	private static ActionDeleteModelElements targetFollower;
	public static ActionDeleteModelElements getTargetFollower() {
		if (targetFollower == null) {
			targetFollower = new ActionDeleteModelElements();
			TargetManager.getInstance().addTargetListener(new TargetListener() {
				public void targetAdded(TargetEvent e) {
					setTarget();
				}
				public void targetRemoved(TargetEvent e) {
					setTarget();
				}
				public void targetSet(TargetEvent e) {
					setTarget();
				}
				private void setTarget() {
					targetFollower.setEnabled(targetFollower.shouldBeEnabled());
				}
			});
			targetFollower.setEnabled(targetFollower.shouldBeEnabled());
		}
		return targetFollower;
	}
	private static final Logger LOG = Logger.getLogger(ActionDeleteModelElements.class);
	public ActionDeleteModelElements() {
		super(Translator.localize("action.delete-from-model"),ResourceLoaderWrapper.lookupIcon("action.delete-from-model"));
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("action.delete-from-model"));
		putValue(Action.SMALL_ICON,ResourceLoaderWrapper.lookupIcon("Delete"));
	}
	public void actionPerformed(ActionEvent ae) {
		super.actionPerformed(ae);
		KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		Component focusOwner = focusManager.getFocusOwner();
		if (focusOwner instanceof FigTextEditor) {
			((FigTextEditor) focusOwner).endEditing();
		}else if (focusOwner instanceof JTable) {
			JTable table = (JTable) focusOwner;
			if (table.isEditing()) {
				TableCellEditor ce = table.getCellEditor();
				if (ce != null) {
					ce.cancelCellEditing();
				}
			}
		}
		Project p = ProjectManager.getManager().getCurrentProject();
		Object[]targets = TargetManager.getInstance().getTargets().toArray();
		TargetManager.getInstance().setTarget(null);
		Object target = null;
		for (int i = targets. - 1;i >= 0;i--) {
			target = targets[i];
			try {
				if (sureRemove(target)) {
					if (target instanceof Fig) {
						Object owner = ((Fig) target).getOwner();
						if (owner != null) {
							target = owner;
						}
					}
					p.moveToTrash(target);
				}
			}catch (InvalidElementException e) {
				LOG.debug("Model element deleted twice - ignoring 2nd delete");
			}
		}
	}
	public static boolean sureRemove(Object target) {
		boolean sure = false;
		if (Model.getFacade().isAModelElement(target)) {
			sure = sureRemoveModelElement(target);
		}else if (Model.getFacade().isAUMLElement(target)) {
			sure = true;
		}else if (target instanceof ArgoDiagram) {
			ArgoDiagram diagram = (ArgoDiagram) target;
			if (diagram.getNodes().size() + diagram.getEdges().size() != 0) {
				String confirmStr = MessageFormat.format(Translator.localize("optionpane.remove-from-model-confirm-delete"),new Object[] {diagram.getName(),""});
				String text = Translator.localize("optionpane.remove-from-model-confirm-delete-title");
				int response = JOptionPane.showConfirmDialog(ArgoFrame.getInstance(),confirmStr,text,JOptionPane.YES_NO_OPTION);
				sure = (response == JOptionPane.YES_OPTION);
			}else {
				sure = true;
			}
		}else if (target instanceof Fig) {
			if (Model.getFacade().isAModelElement(((Fig) target).getOwner())) {
				sure = sureRemoveModelElement(((Fig) target).getOwner());
			}else {
				sure = true;
			}
		}else if (target instanceof CommentEdge) {
			sure = true;
		}
		return sure;
	}
	protected static boolean sureRemoveModelElement(Object me) {
		Project p = ProjectManager.getManager().getCurrentProject();
		int count = p.getPresentationCountFor(me);
		boolean doAsk = false;
		String confirmStr = "";
		if (count > 1) {
			confirmStr += Translator.localize("optionpane.remove-from-model-will-remove-from-diagrams");
			doAsk = true;
		}
		if (!doAsk) {
			return true;
		}
		String name = Model.getFacade().getName(me);
		if (name == null||name.equals("")) {
			name = Translator.localize("optionpane.remove-from-model-anon-element-name");
		}
		confirmStr = MessageFormat.format(Translator.localize("optionpane.remove-from-model-confirm-delete"),new Object[] {name,confirmStr});
		int response = JOptionPane.showConfirmDialog(ArgoFrame.getInstance(),confirmStr,Translator.localize("optionpane.remove-from-model-confirm-delete-title"),JOptionPane.YES_NO_OPTION);
		return(response == JOptionPane.YES_OPTION);
	}
	public boolean shouldBeEnabled() {
		List targets = TargetManager.getInstance().getTargets();
		for (Object target:targets) {
			if (Model.getFacade().isAModelElement(target)&&Model.getModelManagementHelper().isReadOnly(target)) {
				return false;
			}
		}
		int size = 0;
		try {
			Editor ce = Globals.curEditor();
			List<Fig>figs = ce.getSelectionManager().getFigs();
			size = figs.size();
		}catch (Exception e) {
		}
		if (size > 0) {
			return true;
		}
		Object target = TargetManager.getInstance().getTarget();
		if (target instanceof ArgoDiagram) {
			return(ProjectManager.getManager().getCurrentProject().getDiagramList().size() > 1);
		}
		if (Model.getFacade().isAModel(target)&&target.equals(ProjectManager.getManager().getCurrentProject().getModel())) {
			return false;
		}
		if (Model.getFacade().isAAssociationEnd(target)) {
			return Model.getFacade().getOtherAssociationEnds(target).size() > 1;
		}
		return target != null;
	}
}



