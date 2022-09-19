package org.argouml.notation.providers.uml;

import java.text.ParseException;
import java.util.Map;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoHelpEvent;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.notation.NotationSettings;
import org.argouml.notation.providers.AssociationNameNotation;
import org.argouml.notation.providers.uml.NotationUtilityUml;


public class AssociationNameNotationUml extends AssociationNameNotation {
	public AssociationNameNotationUml(Object association) {
		super(association);
	}
	public String getParsingHelp() {
		return"parsing.help.fig-association-name";
	}
	public void parse(Object modelElement,String text) {
		try {
			NotationUtilityUml.parseModelElement(modelElement,text);
		}catch (ParseException pe) {
			String msg = "statusmsg.bar.error.parsing.association-name";
			Object[]args =  {pe.getLocalizedMessage(),Integer.valueOf(pe.getErrorOffset())};
			ArgoEventPump.fireEvent(new ArgoHelpEvent(ArgoEventTypes.HELP_CHANGED,this,Translator.messageFormat(msg,args)));
		}
	}
	public String toString(Object modelElement,NotationSettings settings) {
		return toString(modelElement,settings.isShowAssociationNames(),settings.isFullyHandleStereotypes(),settings.isShowPaths(),settings.isShowVisibilities(),settings.isUseGuillemets());
	}
	@SuppressWarnings("deprecation")@Deprecated public String toString(Object modelElement,Map args) {
		return toString(modelElement,(Boolean) args.get("showAssociationName"),isValue("fullyHandleStereotypes",args),isValue("pathVisible",args),isValue("visibilityVisible",args),isValue("useGuillemets",args));
	}
	private String toString(Object modelElement,Boolean showAssociationName,boolean fullyHandleStereotypes,boolean showPath,boolean showVisibility,boolean useGuillemets) {
		if (showAssociationName == Boolean.FALSE) {
			return"";
		}
		String name = Model.getFacade().getName(modelElement);
		StringBuffer sb = new StringBuffer("");
		if (fullyHandleStereotypes) {
			sb.append(NotationUtilityUml.generateStereotype(modelElement,useGuillemets));
		}
		if (showVisibility) {
			sb.append(NotationUtilityUml.generateVisibility2(modelElement));
			sb.append(" ");
		}
		if (showPath) {
			sb.append(NotationUtilityUml.generatePath(modelElement));
		}
		if (name != null) {
			sb.append(name);
		}
		return sb.toString();
	}
}



