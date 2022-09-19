package org.argouml.uml.ui;

import java.beans.PropertyChangeEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import org.apache.log4j.Logger;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.model.ModelEventPump;
import org.argouml.ui.targetmanager.TargetEvent;
import org.tigris.gef.presentation.Fig;


public abstract class UMLPlainTextDocument extends PlainDocument implements UMLDocument {
	private static final Logger LOG = Logger.getLogger(UMLPlainTextDocument.class);
	private boolean firing = true;
	@Deprecated private boolean editing = false;
	private Object panelTarget = null;
	private String eventName = null;
	public UMLPlainTextDocument(String name) {
		super();
		setEventName(name);
	}
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt instanceof AttributeChangeEvent&&eventName.equals(evt.getPropertyName())) {
			updateText((String) evt.getNewValue());
		}
	}
	public final Object getTarget() {
		return panelTarget;
	}
	public final void setTarget(Object target) {
		target = target instanceof Fig?((Fig) target).getOwner():target;
		if (Model.getFacade().isAUMLElement(target)) {
			if (target != panelTarget) {
				ModelEventPump eventPump = Model.getPump();
				if (panelTarget != null) {
					eventPump.removeModelEventListener(this,panelTarget,getEventName());
				}
				panelTarget = target;
				eventPump.addModelEventListener(this,panelTarget,getEventName());
			}
			updateText(getProperty());
		}
	}
	public void insertString(int offset,String str,AttributeSet a)throws BadLocationException {
		super.insertString(offset,str,a);
		setPropertyInternal(getText(0,getLength()));
	}
	public void remove(int offs,int len)throws BadLocationException {
		super.remove(offs,len);
		setPropertyInternal(getText(0,getLength()));
	}
	private void setPropertyInternal(String newValue) {
		if (isFiring()&&!newValue.equals(getProperty())) {
			setFiring(false);
			setProperty(newValue);
			Model.getPump().flushModelEvents();
			setFiring(true);
		}
	}
	protected abstract void setProperty(String text);
	protected abstract String getProperty();
	private final synchronized void setFiring(boolean f) {
		ModelEventPump eventPump = Model.getPump();
		if (f&&panelTarget != null) {
			eventPump.addModelEventListener(this,panelTarget,eventName);
		}else {
			eventPump.removeModelEventListener(this,panelTarget,eventName);
		}
		firing = f;
	}
	private final synchronized boolean isFiring() {
		return firing;
	}
	private final void updateText(String textValue) {
		try {
			if (textValue == null) {
				textValue = "";
			}
			String currentValue = getText(0,getLength());
			if (isFiring()&&!textValue.equals(currentValue)) {
				setFiring(false);
				super.remove(0,getLength());
				super.insertString(0,textValue,null);
			}
		}catch (BadLocationException b) {
			LOG.error("A BadLocationException happened\n" + "The string to set was: " + getProperty(),b);
		}finally {
			setFiring(true);
		}
	}
	public String getEventName() {
		return eventName;
	}
	protected void setEventName(String en) {
		eventName = en;
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



