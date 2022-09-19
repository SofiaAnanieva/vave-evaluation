package org.argouml.uml.diagram.ui;

import java.util.Hashtable;
import java.util.Properties;
import javax.swing.Action;
import javax.swing.ImageIcon;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.tigris.gef.base.SetModeAction;


public class ActionSetMode extends SetModeAction {
	public ActionSetMode(Properties args) {
		super(args);
	}
	public ActionSetMode(Class modeClass) {
		super(modeClass);
	}
	public ActionSetMode(Class modeClass,String name) {
		super(modeClass);
		putToolTip(name);
		putIcon(name);
	}
	public ActionSetMode(Class modeClass,String name,String tooltipkey) {
		super(modeClass,name);
		putToolTip(tooltipkey);
		putIcon(name);
	}
	public ActionSetMode(Class modeClass,boolean sticky) {
		super(modeClass,sticky);
	}
	public ActionSetMode(Class modeClass,Hashtable modeArgs) {
		super(modeClass,modeArgs);
	}
	public ActionSetMode(Class modeClass,Hashtable modeArgs,String name) {
		super(modeClass);
		this.modeArgs = modeArgs;
		putToolTip(name);
		putIcon(name);
	}
	public ActionSetMode(Class modeClass,String arg,Object value) {
		super(modeClass,arg,value);
	}
	public ActionSetMode(Class modeClass,String arg,Object value,String name) {
		super(modeClass,arg,value);
		putToolTip(name);
		putIcon(name);
	}
	public ActionSetMode(Class modeClass,String arg,Object value,String name,ImageIcon icon) {
		super(modeClass,arg,value,name,icon);
		putToolTip(name);
	}
	private void putToolTip(String key) {
		putValue(Action.SHORT_DESCRIPTION,Translator.localize(key));
	}
	private void putIcon(String key) {
		ImageIcon icon = ResourceLoaderWrapper.lookupIcon(key);
		if (icon != null) {
			putValue(Action.SMALL_ICON,icon);
		}
	}
}



