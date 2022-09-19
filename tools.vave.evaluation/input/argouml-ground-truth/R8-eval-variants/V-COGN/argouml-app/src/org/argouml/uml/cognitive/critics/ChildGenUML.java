package org.argouml.uml.cognitive.critics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import org.argouml.kernel.Project;
import org.argouml.model.Model;
import org.argouml.util.IteratorEnumeration;
import org.argouml.util.SingleElementIterator;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.util.ChildGenerator;


public class ChildGenUML implements ChildGenerator {
	@Deprecated public Enumeration gen(Object o) {
		return new IteratorEnumeration(gen2(o));
	}
	public Iterator gen2(Object o) {
		if (o instanceof Project) {
			Project p = (Project) o;
			Collection result = new ArrayList();
			result.addAll(p.getUserDefinedModelList());
			result.addAll(p.getDiagramList());
			return result.iterator();
		}
		if (o instanceof Diagram) {
			Collection figs = ((Diagram) o).getLayer().getContents();
			if (figs != null) {
				return figs.iterator();
			}
		}
		if (Model.getFacade().isAPackage(o)) {
			Collection ownedElements = Model.getFacade().getOwnedElements(o);
			if (ownedElements != null) {
				return ownedElements.iterator();
			}
		}
		if (Model.getFacade().isAElementImport(o)) {
			Object me = Model.getFacade().getModelElement(o);
			if (me != null) {
				return new SingleElementIterator(me);
			}
		}
		if (Model.getFacade().isAClassifier(o)) {
			Collection result = new ArrayList();
			result.addAll(Model.getFacade().getFeatures(o));
			Collection sms = Model.getFacade().getBehaviors(o);
			if (sms != null) {
				result.addAll(sms);
			}
			return result.iterator();
		}
		if (Model.getFacade().isAAssociation(o)) {
			List assocEnds = (List) Model.getFacade().getConnections(o);
			if (assocEnds != null) {
				return assocEnds.iterator();
			}
		}
		if (Model.getFacade().isAStateMachine(o)) {
			Collection result = new ArrayList();
			Object top = Model.getStateMachinesHelper().getTop(o);
			if (top != null) {
				result.add(top);
			}
			result.addAll(Model.getFacade().getTransitions(o));
			return result.iterator();
		}
		if (Model.getFacade().isACompositeState(o)) {
			Collection substates = Model.getFacade().getSubvertices(o);
			if (substates != null) {
				return substates.iterator();
			}
		}
		if (Model.getFacade().isAOperation(o)) {
			Collection params = Model.getFacade().getParameters(o);
			if (params != null) {
				return params.iterator();
			}
		}
		if (Model.getFacade().isAModelElement(o)) {
			Collection behavior = Model.getFacade().getBehaviors(o);
			if (behavior != null) {
				return behavior.iterator();
			}
		}
		if (Model.getFacade().isAUMLElement(o)) {
			Collection result = Model.getFacade().getModelElementContents(o);
			return result.iterator();
		}
		return Collections.emptySet().iterator();
	}
}



