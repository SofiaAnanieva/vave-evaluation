package org.argouml.cognitive.ui;

import javax.swing.tree.TreeModel;
import org.argouml.util.Predicate;
import org.argouml.util.PredicateTrue;


public abstract class AbstractGoList2 extends AbstractGoList implements TreeModel {
	private Predicate listPredicate = PredicateTrue.getInstance();
	public void setListPredicate(Predicate newPredicate) {
		listPredicate = newPredicate;
	}
	public Predicate getPredicate() {
		return listPredicate;
	}
	public Object getRoot() {
		throw new UnsupportedOperationException();
	}
	public void setRoot(Object r) {
	}
}



