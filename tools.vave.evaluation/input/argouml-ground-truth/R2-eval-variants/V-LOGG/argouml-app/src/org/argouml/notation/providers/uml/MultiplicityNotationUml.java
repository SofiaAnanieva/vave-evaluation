package org.argouml.notation.providers.uml;

import java.text.ParseException;
import java.util.Map;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoHelpEvent;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.notation.NotationSettings;
import org.argouml.notation.providers.MultiplicityNotation;
import org.argouml.notation.providers.uml.NotationUtilityUml;


public class MultiplicityNotationUml extends MultiplicityNotation {
	public MultiplicityNotationUml(Object multiplicityOwner) {
		super(multiplicityOwner);
	}
	@Override public String getParsingHelp() {
		return"parsing.help.fig-multiplicity";
	}
	@Override public void parse(final Object multiplicityOwner,final String text) {
		try {
			parseMultiplicity(multiplicityOwner,text);
		}catch (ParseException pe) {
			final String msg = "statusmsg.bar.error.parsing.multiplicity";
			final Object[]args =  {pe.getLocalizedMessage(),Integer.valueOf(pe.getErrorOffset())};
			ArgoEventPump.fireEvent(new ArgoHelpEvent(ArgoEventTypes.HELP_CHANGED,this,Translator.messageFormat(msg,args)));
		}
	}
	protected Object parseMultiplicity(final Object multiplicityOwner,final String s1)throws ParseException {
		String s = s1.trim();
		Object multi = null;
		try {
			multi = Model.getDataTypesFactory().createMultiplicity(s);
		}catch (IllegalArgumentException iae) {
			throw new ParseException(iae.getLocalizedMessage(),0);
		}
		Model.getCoreHelper().setMultiplicity(multiplicityOwner,multi);
		return multi;
	}
	@SuppressWarnings("deprecation")@Deprecated@Override public String toString(Object multiplicityOwner,Map args) {
		return NotationUtilityUml.generateMultiplicity(multiplicityOwner,args);
	}
	@Override public String toString(Object modelElement,NotationSettings settings) {
		return NotationUtilityUml.generateMultiplicity(modelElement,settings.isShowSingularMultiplicities());
	}
}



