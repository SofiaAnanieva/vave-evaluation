package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import org.argouml.i18n.Translator;
import org.argouml.kernel.UmlModelMutator;
import org.argouml.util.ArgoFrame;
import org.tigris.gef.undo.UndoableAction;
import org.argouml.uml.ui.UMLAddDialog;


@UmlModelMutator public abstract class AbstractActionAddModelElement2 extends UndoableAction {
	private Object target;
	private boolean multiSelect = true;
	private boolean exclusive = true;
	protected AbstractActionAddModelElement2() {
		super(Translator.localize("menu.popup.add-modelelement"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("menu.popup.add-modelelement"));
	}
	public AbstractActionAddModelElement2(String name) {
		super(name);
	}
	public AbstractActionAddModelElement2(String name,Icon icon) {
		super(name,icon);
	}
	@Override public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		UMLAddDialog dialog = new UMLAddDialog(getChoices(),getSelected(),getDialogTitle(),isMultiSelect(),isExclusive());
		int result = dialog.showDialog(ArgoFrame.getInstance());
		if (result == JOptionPane.OK_OPTION) {
			doIt(dialog.getSelected());
		}
	}
	protected abstract List getChoices();
	protected abstract List getSelected();
	protected abstract void doIt(Collection selected);
	@Override public boolean isEnabled() {
		return!getChoices().isEmpty();
	}
	protected Object getTarget() {
		return target;
	}
	public void setTarget(Object theTarget) {
		target = theTarget;
	}
	protected abstract String getDialogTitle();
	public boolean isExclusive() {
		return exclusive;
	}
	public boolean isMultiSelect() {
		return multiSelect;
	}
	public void setExclusive(boolean theExclusive) {
		exclusive = theExclusive;
	}
	public void setMultiSelect(boolean theMultiSelect) {
		multiSelect = theMultiSelect;
	}
}



