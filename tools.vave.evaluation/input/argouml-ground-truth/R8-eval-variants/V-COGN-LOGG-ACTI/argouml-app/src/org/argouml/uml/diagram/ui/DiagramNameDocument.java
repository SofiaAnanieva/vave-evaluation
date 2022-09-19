package org.argouml.uml.diagram.ui;

import java.beans.PropertyVetoException;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import org.apache.log4j.Logger;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;


class DiagramNameDocument implements DocumentListener,TargetListener {
	private static final Logger LOG = Logger.getLogger(DiagramNameDocument.class);
	private JTextField field;
	private boolean stopEvents = false;
	private Object highlightTag = null;
	public DiagramNameDocument(JTextField theField) {
		field = theField;
		TargetManager tm = TargetManager.getInstance();
		tm.addTargetListener(this);
		setTarget(tm.getTarget());
	}
	private void setTarget(Object t) {
		if (t instanceof ArgoDiagram) {
			stopEvents = true;
			field.setText(((ArgoDiagram) t).getName());
			stopEvents = false;
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
	public void insertUpdate(DocumentEvent e) {
		update(e);
	}
	public void removeUpdate(DocumentEvent e) {
		update(e);
	}
	public void changedUpdate(DocumentEvent e) {
		update(e);
	}
	private void update(DocumentEvent e) {
		if (!stopEvents) {
			Object target = TargetManager.getInstance().getTarget();
			if (target instanceof ArgoDiagram) {
				ArgoDiagram d = (ArgoDiagram) target;
				try {
					int documentLength = e.getDocument().getLength();
					String newName = e.getDocument().getText(0,documentLength);
					String oldName = d.getName();
					if (!oldName.equals(newName)) {
						d.setName(newName);
						if (highlightTag != null) {
							field.getHighlighter().removeHighlight(highlightTag);
							highlightTag = null;
						}
					}
				}catch (PropertyVetoException pe) {
					try {
						highlightTag = field.getHighlighter().addHighlight(0,field.getText().length(),DefaultHighlighter.DefaultPainter);
					}catch (BadLocationException e1) {
						LOG.debug("Nested exception",e1);
					}
				}catch (BadLocationException ble) {
					LOG.debug(ble);
				}
			}
		}
	}
}



