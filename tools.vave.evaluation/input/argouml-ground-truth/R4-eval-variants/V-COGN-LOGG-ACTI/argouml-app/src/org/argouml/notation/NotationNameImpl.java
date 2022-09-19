package org.argouml.notation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import javax.swing.Icon;
import org.apache.log4j.Logger;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoNotationEvent;


class NotationNameImpl implements NotationName {
	private static final Logger LOG = Logger.getLogger(NotationNameImpl.class);
	private String name;
	private String version;
	private Icon icon;
	private static ArrayList<NotationName>notations = new ArrayList<NotationName>();
	protected NotationNameImpl(String theName) {
		this(theName,null,null);
	}
	protected NotationNameImpl(String theName,Icon theIcon) {
		this(theName,null,theIcon);
	}
	protected NotationNameImpl(String theName,String theVersion) {
		this(theName,theVersion,null);
	}
	protected NotationNameImpl(String myName,String myVersion,Icon myIcon) {
		name = myName;
		version = myVersion;
		icon = myIcon;
	}
	public String getName() {
		return name;
	}
	public String getVersion() {
		return version;
	}
	public String getTitle() {
		String myName = name;
		if (myName.equalsIgnoreCase("uml")) {
			myName = myName.toUpperCase();
		}
		if (version == null||version.equals("")) {
			return myName;
		}
		return myName + " " + version;
	}
	public Icon getIcon() {
		return icon;
	}
	public String getConfigurationValue() {
		return getNotationNameString(name,version);
	}
	public String toString() {
		return getTitle();
	}
	static String getNotationNameString(String k1,String k2) {
		if (k2 == null) {
			return k1;
		}
		if (k2.equals("")) {
			return k1;
		}
		return k1 + " " + k2;
	}
	private static void fireEvent(int eventType,NotationName nn) {
		ArgoEventPump.fireEvent(new ArgoNotationEvent(eventType,nn));
	}
	static NotationName makeNotation(String k1,String k2,Icon icon) {
		NotationName nn = null;
		nn = findNotation(getNotationNameString(k1,k2));
		if (nn == null) {
			nn = new NotationNameImpl(k1,k2,icon);
			notations.add(nn);
			fireEvent(ArgoEventTypes.NOTATION_ADDED,nn);
		}
		return nn;
	}
	static boolean removeNotation(NotationName theNotation) {
		return notations.remove(theNotation);
	}
	static List<NotationName>getAvailableNotations() {
		return Collections.unmodifiableList(notations);
	}
	static NotationName findNotation(String s) {
		ListIterator iterator = notations.listIterator();
		while (iterator.hasNext()) {
			try {
				NotationName nn = (NotationName) iterator.next();
				if (s.equals(nn.getConfigurationValue())) {
					return nn;
				}
			}catch (Exception e) {
				LOG.error("Unexpected exception",e);
			}
		}
		return null;
	}
	public boolean sameNotationAs(NotationName nn) {
		return this.getConfigurationValue().equals(nn.getConfigurationValue());
	}
	static NotationName getNotation(String k1) {
		return findNotation(getNotationNameString(k1,null));
	}
	static NotationName getNotation(String k1,String k2) {
		return findNotation(getNotationNameString(k1,k2));
	}
}



