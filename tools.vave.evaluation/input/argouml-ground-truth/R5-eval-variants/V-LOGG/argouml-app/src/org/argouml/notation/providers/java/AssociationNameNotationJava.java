package org.argouml.notation.providers.java;

import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.Map;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoHelpEvent;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.notation.NotationSettings;
import org.argouml.notation.providers.AssociationNameNotation;


public class AssociationNameNotationJava extends AssociationNameNotation {
	public AssociationNameNotationJava(Object modelElement) {
		super(modelElement);
	}
	@Override public void initialiseListener(final PropertyChangeListener listener,final Object modelElement) {
		addElementListener(listener,modelElement,new String[] {"isLeaf"});
		super.initialiseListener(listener,modelElement);
	}
	public String getParsingHelp() {
		return"parsing.help.java.fig-nodemodelelement";
	}
	public void parse(final Object modelElement,final String text) {
		try {
			ModelElementNameNotationJava.parseModelElement(modelElement,text);
		}catch (ParseException pe) {
			final String msg = "statusmsg.bar.error.parsing.node-modelelement";
			final Object[]args =  {pe.getLocalizedMessage(),Integer.valueOf(pe.getErrorOffset())};
			ArgoEventPump.fireEvent(new ArgoHelpEvent(ArgoEventTypes.HELP_CHANGED,this,Translator.messageFormat(msg,args)));
		}
	}
	@SuppressWarnings("deprecation")@Deprecated@Override public String toString(final Object modelElement,final Map args) {
		String name;
		name = Model.getFacade().getName(modelElement);
		if (name == null) {
			return"";
		}
		return NotationUtilityJava.generateLeaf(modelElement,args) + NotationUtilityJava.generateAbstract(modelElement,args) + NotationUtilityJava.generateVisibility(modelElement,args) + NotationUtilityJava.generatePath(modelElement,args) + name;
	}
	@Override public String toString(final Object modelElement,final NotationSettings settings) {
		String name;
		name = Model.getFacade().getName(modelElement);
		if (name == null) {
			return"";
		}
		String visibility = "";
		if (settings.isShowVisibilities()) {
			visibility = NotationUtilityJava.generateVisibility(modelElement);
		}
		String path = "";
		if (settings.isShowPaths()) {
			path = NotationUtilityJava.generatePath(modelElement);
		}
		return NotationUtilityJava.generateLeaf(modelElement) + NotationUtilityJava.generateAbstract(modelElement) + visibility + path + name;
	}
}



