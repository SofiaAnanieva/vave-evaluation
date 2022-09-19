package org.argouml.language.ui;

import java.awt.Dimension;
import java.util.Iterator;
import javax.swing.JComboBox;
import org.apache.log4j.Logger;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoGeneratorEvent;
import org.argouml.application.events.ArgoGeneratorEventListener;
import org.argouml.uml.generator.GeneratorManager;
import org.argouml.uml.generator.Language;


public class LanguageComboBox extends JComboBox implements ArgoGeneratorEventListener {
	private static final Logger LOG = Logger.getLogger(LanguageComboBox.class);
	public LanguageComboBox() {
		super();
		setEditable(false);
		setMaximumRowCount(6);
		Dimension d = getPreferredSize();
		d.width = 200;
		setMaximumSize(d);
		ArgoEventPump.addListener(ArgoEventTypes.ANY_GENERATOR_EVENT,this);
		refresh();
	}
	protected void finalize() {
		ArgoEventPump.removeListener(this);
	}
	public void refresh() {
		removeAllItems();
		Iterator iterator = GeneratorManager.getInstance().getLanguages().iterator();
		while (iterator.hasNext()) {
			try {
				Language ll = (Language) iterator.next();
				addItem(ll);
			}catch (Exception e) {
				LOG.error("Unexpected exception",e);
			}
		}
		setVisible(true);
		invalidate();
	}
	public void generatorChanged(ArgoGeneratorEvent e) {
		refresh();
	}
	public void generatorAdded(ArgoGeneratorEvent e) {
		refresh();
	}
	public void generatorRemoved(ArgoGeneratorEvent e) {
		refresh();
	}
}



