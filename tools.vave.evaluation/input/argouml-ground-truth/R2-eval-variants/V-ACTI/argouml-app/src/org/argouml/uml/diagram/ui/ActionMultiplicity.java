package org.argouml.uml.diagram.ui;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.Action;
import org.argouml.model.Model;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.undo.UndoableAction;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;


public class ActionMultiplicity extends UndoableAction {
	private String str = "";
	private Object mult = null;
	private static UndoableAction srcMultOne = new ActionMultiplicity("1","src");
	private static UndoableAction destMultOne = new ActionMultiplicity("1","dest");
	private static UndoableAction srcMultZeroToOne = new ActionMultiplicity("0..1","src");
	private static UndoableAction destMultZeroToOne = new ActionMultiplicity("0..1","dest");
	private static UndoableAction srcMultZeroToMany = new ActionMultiplicity("0..*","src");
	private static UndoableAction destMultZeroToMany = new ActionMultiplicity("0..*","dest");
	private static UndoableAction srcMultOneToMany = new ActionMultiplicity("1..*","src");
	private static UndoableAction destMultOneToMany = new ActionMultiplicity("1..*","dest");
	protected ActionMultiplicity(String m,String s) {
		super(m,null);
		putValue(Action.SHORT_DESCRIPTION,m);
		str = s;
		mult = m;
	}
	@Override public void actionPerformed(ActionEvent ae) {
		super.actionPerformed(ae);
		List sels = Globals.curEditor().getSelectionManager().selections();
		if (sels.size() == 1) {
			Selection sel = (Selection) sels.get(0);
			Fig f = sel.getContent();
			Object owner = ((FigEdgeModelElement) f).getOwner();
			Collection ascEnds = Model.getFacade().getConnections(owner);
			Iterator iter = ascEnds.iterator();
			Object ascEnd = null;
			if (str.equals("src")) {
				ascEnd = iter.next();
			}else {
				while (iter.hasNext()) {
					ascEnd = iter.next();
				}
			}
			if (!mult.equals(Model.getFacade().toString(Model.getFacade().getMultiplicity(ascEnd)))) {
				Model.getCoreHelper().setMultiplicity(ascEnd,Model.getDataTypesFactory().createMultiplicity((String) mult));
			}
		}
	}
	public boolean isEnabled() {
		return true;
	}
	public static UndoableAction getSrcMultOne() {
		return srcMultOne;
	}
	public static UndoableAction getDestMultOne() {
		return destMultOne;
	}
	public static UndoableAction getSrcMultZeroToOne() {
		return srcMultZeroToOne;
	}
	public static UndoableAction getDestMultZeroToOne() {
		return destMultZeroToOne;
	}
	public static UndoableAction getSrcMultZeroToMany() {
		return srcMultZeroToMany;
	}
	public static UndoableAction getDestMultZeroToMany() {
		return destMultZeroToMany;
	}
	public static UndoableAction getSrcMultOneToMany() {
		return srcMultOneToMany;
	}
	public static UndoableAction getDestMultOneToMany() {
		return destMultOneToMany;
	}
}



