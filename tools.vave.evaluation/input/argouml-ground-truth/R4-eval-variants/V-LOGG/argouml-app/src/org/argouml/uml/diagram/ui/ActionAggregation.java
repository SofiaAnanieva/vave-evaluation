package org.argouml.uml.diagram.ui;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.undo.UndoableAction;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;


public class ActionAggregation extends UndoableAction {
	private String str = "";
	private Object agg = null;
	private static UndoableAction srcAgg = new ActionAggregation(Model.getAggregationKind().getAggregate(),"src");
	private static UndoableAction destAgg = new ActionAggregation(Model.getAggregationKind().getAggregate(),"dest");
	private static UndoableAction srcAggComposite = new ActionAggregation(Model.getAggregationKind().getComposite(),"src");
	private static UndoableAction destAggComposite = new ActionAggregation(Model.getAggregationKind().getComposite(),"dest");
	private static UndoableAction srcAggNone = new ActionAggregation(Model.getAggregationKind().getNone(),"src");
	private static UndoableAction destAggNone = new ActionAggregation(Model.getAggregationKind().getNone(),"dest");
	protected ActionAggregation(Object a,String s) {
		super(Translator.localize(Model.getFacade().getName(a)),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize(Model.getFacade().getName(a)));
		str = s;
		agg = a;
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
			Model.getCoreHelper().setAggregation(ascEnd,agg);
		}
	}
	@Override public boolean isEnabled() {
		return true;
	}
	public static UndoableAction getSrcAgg() {
		return srcAgg;
	}
	public static UndoableAction getDestAgg() {
		return destAgg;
	}
	public static UndoableAction getSrcAggComposite() {
		return srcAggComposite;
	}
	public static UndoableAction getDestAggComposite() {
		return destAggComposite;
	}
	public static UndoableAction getSrcAggNone() {
		return srcAggNone;
	}
	public static UndoableAction getDestAggNone() {
		return destAggNone;
	}
}



