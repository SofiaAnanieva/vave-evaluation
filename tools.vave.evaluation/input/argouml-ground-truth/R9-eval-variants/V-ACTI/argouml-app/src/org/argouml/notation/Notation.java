package org.argouml.notation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.Icon;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoNotationEvent;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;


public final class Notation implements PropertyChangeListener {
	private static final String DEFAULT_NOTATION_NAME = "UML";
	private static final String DEFAULT_NOTATION_VERSION = "1.4";
	public static final String DEFAULT_NOTATION = DEFAULT_NOTATION_NAME + " " + DEFAULT_NOTATION_VERSION;
	private static NotationName notationArgo = makeNotation(DEFAULT_NOTATION_NAME,DEFAULT_NOTATION_VERSION,ResourceLoaderWrapper.lookupIconResource("UmlNotation"));
	public static final ConfigurationKey KEY_DEFAULT_NOTATION = Configuration.makeKey("notation","default");
	public static final ConfigurationKey KEY_SHOW_STEREOTYPES = Configuration.makeKey("notation","navigation","show","stereotypes");
	public static final ConfigurationKey KEY_SHOW_SINGULAR_MULTIPLICITIES = Configuration.makeKey("notation","show","singularmultiplicities");
	public static final ConfigurationKey KEY_SHOW_BOLD_NAMES = Configuration.makeKey("notation","show","bold","names");
	public static final ConfigurationKey KEY_USE_GUILLEMOTS = Configuration.makeKey("notation","guillemots");
	public static final ConfigurationKey KEY_SHOW_ASSOCIATION_NAMES = Configuration.makeKey("notation","show","associationnames");
	public static final ConfigurationKey KEY_SHOW_VISIBILITY = Configuration.makeKey("notation","show","visibility");
	public static final ConfigurationKey KEY_SHOW_MULTIPLICITY = Configuration.makeKey("notation","show","multiplicity");
	public static final ConfigurationKey KEY_SHOW_INITIAL_VALUE = Configuration.makeKey("notation","show","initialvalue");
	public static final ConfigurationKey KEY_SHOW_PROPERTIES = Configuration.makeKey("notation","show","properties");
	public static final ConfigurationKey KEY_SHOW_TYPES = Configuration.makeKey("notation","show","types");
	public static final ConfigurationKey KEY_DEFAULT_SHADOW_WIDTH = Configuration.makeKey("notation","default","shadow-width");
	public static final ConfigurationKey KEY_HIDE_BIDIRECTIONAL_ARROWS = Configuration.makeKey("notation","hide","bidirectional-arrows");
	private static final Notation SINGLETON = new Notation();
	private Notation() {
		Configuration.addListener(KEY_SHOW_BOLD_NAMES,this);
		Configuration.addListener(KEY_USE_GUILLEMOTS,this);
		Configuration.addListener(KEY_DEFAULT_NOTATION,this);
		Configuration.addListener(KEY_SHOW_TYPES,this);
		Configuration.addListener(KEY_SHOW_MULTIPLICITY,this);
		Configuration.addListener(KEY_SHOW_PROPERTIES,this);
		Configuration.addListener(KEY_SHOW_ASSOCIATION_NAMES,this);
		Configuration.addListener(KEY_SHOW_VISIBILITY,this);
		Configuration.addListener(KEY_SHOW_INITIAL_VALUE,this);
		Configuration.addListener(KEY_HIDE_BIDIRECTIONAL_ARROWS,this);
	}
	public static void setDefaultNotation(NotationName n) {
		Configuration.setString(KEY_DEFAULT_NOTATION,n.getConfigurationValue());
	}
	public static NotationName findNotation(String s) {
		return NotationNameImpl.findNotation(s);
	}
	public static NotationName getConfiguredNotation() {
		NotationName n = NotationNameImpl.findNotation(Configuration.getString(KEY_DEFAULT_NOTATION,notationArgo.getConfigurationValue()));
		if (n == null) {
			n = NotationNameImpl.findNotation(DEFAULT_NOTATION);
		}
		return n;
	}
	public static Notation getInstance() {
		return SINGLETON;
	}
	public void propertyChange(PropertyChangeEvent pce) {
		ArgoEventPump.fireEvent(new ArgoNotationEvent(ArgoEventTypes.NOTATION_CHANGED,pce));
	}
	public static List<NotationName>getAvailableNotations() {
		return NotationNameImpl.getAvailableNotations();
	}
	public static boolean removeNotation(NotationName theNotation) {
		return NotationNameImpl.removeNotation(theNotation);
	}
	public static NotationName makeNotation(String k1,String k2,Icon icon) {
		NotationName nn = NotationNameImpl.makeNotation(k1,k2,icon);
		return nn;
	}
}



