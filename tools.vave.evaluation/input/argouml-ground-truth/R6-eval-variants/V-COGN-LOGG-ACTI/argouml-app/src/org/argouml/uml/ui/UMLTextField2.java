package org.argouml.uml.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JTextField;
import org.argouml.ui.LookAndFeelMgr;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargettableModelView;


public class UMLTextField2 extends JTextField implements PropertyChangeListener,TargettableModelView {
	private static final long serialVersionUID = -5740838103900828073l;
	public UMLTextField2(UMLDocument doc) {
		super(doc,null,0);
		setFont(LookAndFeelMgr.getInstance().getStandardFont());
		addCaretListener(ActionCopy.getInstance());
		addCaretListener(ActionCut.getInstance());
		addCaretListener(ActionPaste.getInstance());
		addFocusListener(ActionPaste.getInstance());
	}
	public void propertyChange(PropertyChangeEvent evt) {
		((UMLDocument) getDocument()).propertyChange(evt);
	}
	public TargetListener getTargettableModel() {
		return(UMLDocument) getDocument();
	}
}



