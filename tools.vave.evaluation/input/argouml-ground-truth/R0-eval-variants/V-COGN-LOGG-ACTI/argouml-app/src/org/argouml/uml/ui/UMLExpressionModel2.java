package org.argouml.uml.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.SwingUtilities;
import org.argouml.model.Model;
import org.argouml.ui.TabTarget;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.tigris.gef.presentation.Fig;
import org.argouml.uml.ui.UMLUserInterfaceContainer;


public abstract class UMLExpressionModel2 implements TargetListener,PropertyChangeListener {
	private UMLUserInterfaceContainer container;
	private String propertyName;
	private Object expression;
	private boolean mustRefresh;
	private static final String EMPTYSTRING = "";
	private Object target = null;
	public UMLExpressionModel2(UMLUserInterfaceContainer c,String name) {
		container = c;
		propertyName = name;
		mustRefresh = true;
	}
	public void targetChanged() {
		mustRefresh = true;
		expression = null;
	}
	public abstract Object getExpression();
	public abstract void setExpression(Object expr);
	public abstract Object newExpression();
	public String getLanguage() {
		if (mustRefresh) {
			expression = getExpression();
		}
		if (expression == null) {
			return EMPTYSTRING;
		}
		return Model.getDataTypesHelper().getLanguage(expression);
	}
	public String getBody() {
		if (mustRefresh) {
			expression = getExpression();
		}
		if (expression == null) {
			return EMPTYSTRING;
		}
		return Model.getDataTypesHelper().getBody(expression);
	}
	public void setLanguage(String lang) {
		boolean mustChange = true;
		if (expression != null) {
			String oldValue = Model.getDataTypesHelper().getLanguage(expression);
			if (oldValue != null&&oldValue.equals(lang)) {
				mustChange = false;
			}
		}
		if (mustChange) {
			String body = EMPTYSTRING;
			if (expression != null&&Model.getDataTypesHelper().getBody(expression) != null) {
				body = Model.getDataTypesHelper().getBody(expression);
			}
			setExpression(lang,body);
		}
	}
	public void setBody(String body) {
		boolean mustChange = true;
		if (expression != null) {
			Object oldValue = Model.getDataTypesHelper().getBody(expression);
			if (oldValue != null&&oldValue.equals(body)) {
				mustChange = false;
			}
		}
		if (mustChange) {
			String lang = null;
			if (expression != null) {
				lang = Model.getDataTypesHelper().getLanguage(expression);
			}
			if (lang == null) {
				lang = EMPTYSTRING;
			}
			setExpression(lang,body);
		}
	}
	private void setExpression(String lang,String body) {
		Object oldExpression = null;
		if (mustRefresh||expression == null) {
			oldExpression = expression;
			expression = newExpression();
		}
		expression = Model.getDataTypesHelper().setLanguage(expression,lang);
		expression = Model.getDataTypesHelper().setBody(expression,body);
		setExpression(expression);
		if (oldExpression != null) {
			Model.getUmlFactory().delete(oldExpression);
		}
	}
	protected UMLUserInterfaceContainer getContainer() {
		return container;
	}
	public void setTarget(Object theNewTarget) {
		theNewTarget = theNewTarget instanceof Fig?((Fig) theNewTarget).getOwner():theNewTarget;
		if (Model.getFacade().isAUMLElement(target)) {
			Model.getPump().removeModelEventListener(this,target,propertyName);
		}
		if (Model.getFacade().isAUMLElement(theNewTarget)) {
			target = theNewTarget;
			Model.getPump().addModelEventListener(this,target,propertyName);
			if (container instanceof TabTarget) {
				((TabTarget) container).refresh();
			}
		}else {
			target = null;
		}
	}
	public void propertyChange(PropertyChangeEvent e) {
		if (target != null&&target == e.getSource()) {
			mustRefresh = true;
			expression = null;
			if (container instanceof TabTarget) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						((TabTarget) container).refresh();
					}
				});
			}
		}
	}
	public void targetAdded(TargetEvent e) {
		setTarget(e.getNewTarget());
	}
	public void targetRemoved(TargetEvent e) {
		setTarget(e.getNewTarget());
	}
	public void targetSet(TargetEvent e) {
		setTarget(e.getNewTarget());
	}
}



