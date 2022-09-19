package org.argouml.uml.diagram.ui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.uml.diagram.AttributesCompartmentContainer;
import org.argouml.uml.diagram.ExtensionsCompartmentContainer;
import org.argouml.uml.diagram.OperationsCompartmentContainer;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.undo.UndoableAction;


public class ActionCompartmentDisplay extends UndoableAction {
	private boolean display = false;
	private int cType;
	private static final int COMPARTMENT_ATTRIBUTE = 1;
	private static final int COMPARTMENT_OPERATION = 2;
	private static final int COMPARTMENT_EXTENSIONPOINT = 4;
	private static final int COMPARTMENT_ENUMLITERAL = 8;
	private static final UndoableAction SHOW_ATTR_COMPARTMENT = new ActionCompartmentDisplay(true,"action.show-attribute-compartment",COMPARTMENT_ATTRIBUTE);
	private static final UndoableAction HIDE_ATTR_COMPARTMENT = new ActionCompartmentDisplay(false,"action.hide-attribute-compartment",COMPARTMENT_ATTRIBUTE);
	private static final UndoableAction SHOW_OPER_COMPARTMENT = new ActionCompartmentDisplay(true,"action.show-operation-compartment",COMPARTMENT_OPERATION);
	private static final UndoableAction HIDE_OPER_COMPARTMENT = new ActionCompartmentDisplay(false,"action.hide-operation-compartment",COMPARTMENT_OPERATION);
	private static final UndoableAction SHOW_EXTPOINT_COMPARTMENT = new ActionCompartmentDisplay(true,"action.show-extension-point-compartment",COMPARTMENT_EXTENSIONPOINT);
	private static final UndoableAction HIDE_EXTPOINT_COMPARTMENT = new ActionCompartmentDisplay(false,"action.hide-extension-point-compartment",COMPARTMENT_EXTENSIONPOINT);
	private static final UndoableAction SHOW_ALL_COMPARTMENTS = new ActionCompartmentDisplay(true,"action.show-all-compartments",COMPARTMENT_ATTRIBUTE|COMPARTMENT_OPERATION|COMPARTMENT_ENUMLITERAL);
	private static final UndoableAction HIDE_ALL_COMPARTMENTS = new ActionCompartmentDisplay(false,"action.hide-all-compartments",COMPARTMENT_ATTRIBUTE|COMPARTMENT_OPERATION|COMPARTMENT_ENUMLITERAL);
	private static final UndoableAction SHOW_ENUMLITERAL_COMPARTMENT = new ActionCompartmentDisplay(true,"action.show-enumeration-literal-compartment",COMPARTMENT_ENUMLITERAL);
	private static final UndoableAction HIDE_ENUMLITERAL_COMPARTMENT = new ActionCompartmentDisplay(false,"action.hide-enumeration-literal-compartment",COMPARTMENT_ENUMLITERAL);
	protected ActionCompartmentDisplay(boolean d,String c,int type) {
		super(Translator.localize(c));
		display = d;
		cType = type;
	}
	public static Collection<Action>getActions() {
		Collection<Action>actions = new ArrayList<Action>();
		Editor ce = Globals.curEditor();
		int present = 0;
		int visible = 0;
		boolean operPresent = false;
		boolean operVisible = false;
		boolean attrPresent = false;
		boolean attrVisible = false;
		boolean epPresent = false;
		boolean epVisible = false;
		boolean enumPresent = false;
		boolean enumVisible = false;
		List<Fig>figs = ce.getSelectionManager().getFigs();
		for (Fig f:figs) {
			if (f instanceof AttributesCompartmentContainer) {
				present++;
				attrPresent = true;
				attrVisible = ((AttributesCompartmentContainer) f).isAttributesVisible();
				if (attrVisible) {
					visible++;
				}
			}
			if (f instanceof OperationsCompartmentContainer) {
				present++;
				operPresent = true;
				operVisible = ((OperationsCompartmentContainer) f).isOperationsVisible();
				if (operVisible) {
					visible++;
				}
			}
			if (f instanceof ExtensionsCompartmentContainer) {
				present++;
				epPresent = true;
				epVisible = ((ExtensionsCompartmentContainer) f).isExtensionPointVisible();
				if (epVisible) {
					visible++;
				}
			}
			if (f instanceof EnumLiteralsCompartmentContainer) {
				present++;
				enumPresent = true;
				enumVisible = ((EnumLiteralsCompartmentContainer) f).isEnumLiteralsVisible();
				if (enumVisible) {
					visible++;
				}
			}
		}
		if (present > 1) {
			if (visible > 0) {
				actions.add(HIDE_ALL_COMPARTMENTS);
			}
			if (present - visible > 0) {
				actions.add(SHOW_ALL_COMPARTMENTS);
			}
		}
		if (attrPresent) {
			if (attrVisible) {
				actions.add(HIDE_ATTR_COMPARTMENT);
			}else {
				actions.add(SHOW_ATTR_COMPARTMENT);
			}
		}
		if (enumPresent) {
			if (enumVisible) {
				actions.add(HIDE_ENUMLITERAL_COMPARTMENT);
			}else {
				actions.add(SHOW_ENUMLITERAL_COMPARTMENT);
			}
		}
		if (operPresent) {
			if (operVisible) {
				actions.add(HIDE_OPER_COMPARTMENT);
			}else {
				actions.add(SHOW_OPER_COMPARTMENT);
			}
		}
		if (epPresent) {
			if (epVisible) {
				actions.add(HIDE_EXTPOINT_COMPARTMENT);
			}else {
				actions.add(SHOW_EXTPOINT_COMPARTMENT);
			}
		}
		return actions;
	}
	@Override public void actionPerformed(ActionEvent ae) {
		Iterator i = Globals.curEditor().getSelectionManager().selections().iterator();
		while (i.hasNext()) {
			Selection sel = (Selection) i.next();
			Fig f = sel.getContent();
			if ((cType&COMPARTMENT_ATTRIBUTE) != 0) {
				if (f instanceof AttributesCompartmentContainer) {
					((AttributesCompartmentContainer) f).setAttributesVisible(display);
				}
			}
			if ((cType&COMPARTMENT_OPERATION) != 0) {
				if (f instanceof OperationsCompartmentContainer) {
					((OperationsCompartmentContainer) f).setOperationsVisible(display);
				}
			}
			if ((cType&COMPARTMENT_EXTENSIONPOINT) != 0) {
			}
			if ((cType&COMPARTMENT_ENUMLITERAL) != 0) {
				if (f instanceof EnumLiteralsCompartmentContainer) {
					((EnumLiteralsCompartmentContainer) f).setEnumLiteralsVisible(display);
				}
			}
		}
	}
}



