package org.argouml.notation.providers.java;

import java.util.Map;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoHelpEvent;
import org.argouml.model.Model;
import org.argouml.notation.NotationSettings;
import org.argouml.notation.providers.AssociationEndNameNotation;
import org.argouml.notation.providers.uml.NotationUtilityUml;


public class AssociationEndNameNotationJava extends AssociationEndNameNotation {
	private static final AssociationEndNameNotationJava INSTANCE = new AssociationEndNameNotationJava();
	public static final AssociationEndNameNotationJava getInstance() {
		return INSTANCE;
	}
	protected AssociationEndNameNotationJava() {
		super();
	}
	public String getParsingHelp() {
		return"Parsing in Java not yet supported";
	}
	public void parse(Object modelElement,String text) {
		ArgoEventPump.fireEvent(new ArgoHelpEvent(ArgoEventTypes.HELP_CHANGED,this,"Parsing in Java not yet supported"));
	}
	@SuppressWarnings("deprecation")@Deprecated@Override public String toString(Object modelElement,Map args) {
		return toString(modelElement,NotationUtilityUml.isValue("useGuillemets",args));
	}
	private String toString(Object modelElement,boolean useGuillemets) {
		String name = Model.getFacade().getName(modelElement);
		if (name == null) {
			name = "";
		}
		Object visi = Model.getFacade().getVisibility(modelElement);
		String visibility = "";
		if (visi != null) {
			visibility = NotationUtilityJava.generateVisibility(visi);
		}
		if (name.length() < 1) {
			visibility = "";
		}
		String stereoString = NotationUtilityUml.generateStereotype(modelElement,useGuillemets);
		return stereoString + visibility + name;
	}
	@Override public String toString(Object modelElement,NotationSettings settings) {
		return toString(modelElement,settings.isUseGuillemets());
	}
}



