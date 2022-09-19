package org.argouml.notation.ui;

import java.awt.Dimension;
import java.util.ListIterator;
import javax.swing.JComboBox;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoNotationEvent;
import org.argouml.application.events.ArgoNotationEventListener;
import org.argouml.notation.Notation;
import org.argouml.notation.NotationName;


public class NotationComboBox extends JComboBox implements ArgoNotationEventListener {
	private static NotationComboBox singleton;
	public static NotationComboBox getInstance() {
		if (singleton == null) {
			singleton = new NotationComboBox();
		}
		return singleton;
	}
	public NotationComboBox() {
		super();
		setEditable(false);
		setMaximumRowCount(6);
		Dimension d = getPreferredSize();
		d.width = 200;
		setMaximumSize(d);
		ArgoEventPump.addListener(ArgoEventTypes.ANY_NOTATION_EVENT,this);
		refresh();
	}
	public void notationChanged(ArgoNotationEvent event) {
	}
	public void notationAdded(ArgoNotationEvent event) {
		refresh();
	}
	public void notationRemoved(ArgoNotationEvent event) {
	}
	public void notationProviderAdded(ArgoNotationEvent event) {
	}
	public void notationProviderRemoved(ArgoNotationEvent event) {
	}
	public void refresh() {
		removeAllItems();
		ListIterator iterator = Notation.getAvailableNotations().listIterator();
		while (iterator.hasNext()) {
			try {
				NotationName nn = (NotationName) iterator.next();
				addItem(nn);
			}catch (Exception e) {
			}
		}
		setVisible(true);
		invalidate();
	}
	private static final long serialVersionUID = 4059899784583789412l;
}



