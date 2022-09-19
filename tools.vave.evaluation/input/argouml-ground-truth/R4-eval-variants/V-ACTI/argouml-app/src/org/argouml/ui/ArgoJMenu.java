package org.argouml.ui;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingConstants;
import org.argouml.i18n.Translator;


public class ArgoJMenu extends JMenu {
	public ArgoJMenu(String key) {
		super();
		localize(this,key);
	}
	public static final void localize(JMenuItem menuItem,String key) {
		menuItem.setText(Translator.localize(key));
		String localMnemonic = Translator.localize(key + ".mnemonic");
		if (localMnemonic != null&&localMnemonic.length() == 1) {
			menuItem.setMnemonic(localMnemonic.charAt(0));
		}
	}
	public JCheckBoxMenuItem addCheckItem(Action a) {
		String name = (String) a.getValue(Action.NAME);
		Icon icon = (Icon) a.getValue(Action.SMALL_ICON);
		Boolean selected = (Boolean) a.getValue("SELECTED");
		JCheckBoxMenuItem mi = new JCheckBoxMenuItem(name,icon,(selected == null||selected.booleanValue()));
		mi.setHorizontalTextPosition(SwingConstants.RIGHT);
		mi.setVerticalTextPosition(SwingConstants.CENTER);
		mi.setEnabled(a.isEnabled());
		mi.addActionListener(a);
		add(mi);
		a.addPropertyChangeListener(createActionChangeListener(mi));
		return mi;
	}
	public JRadioButtonMenuItem addRadioItem(Action a) {
		String name = (String) a.getValue(Action.NAME);
		Icon icon = (Icon) a.getValue(Action.SMALL_ICON);
		Boolean selected = (Boolean) a.getValue("SELECTED");
		JRadioButtonMenuItem mi = new JRadioButtonMenuItem(name,icon,(selected == null||selected.booleanValue()));
		mi.setHorizontalTextPosition(SwingConstants.RIGHT);
		mi.setVerticalTextPosition(SwingConstants.CENTER);
		mi.setEnabled(a.isEnabled());
		mi.addActionListener(a);
		add(mi);
		a.addPropertyChangeListener(createActionChangeListener(mi));
		return mi;
	}
	private static final long serialVersionUID = 8318663502924796474l;
}



