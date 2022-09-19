package org.argouml.notation.providers.uml;

import java.text.ParseException;
import java.util.Map;
import java.util.NoSuchElementException;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoHelpEvent;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.notation.NotationSettings;
import org.argouml.notation.providers.AssociationEndNameNotation;
import org.argouml.uml.StereotypeUtility;
import org.argouml.util.MyTokenizer;
import org.argouml.notation.providers.uml.NotationUtilityUml;


public class AssociationEndNameNotationUml extends AssociationEndNameNotation {
	public static final AssociationEndNameNotationUml getInstance() {
		return new AssociationEndNameNotationUml();
	}
	protected AssociationEndNameNotationUml() {
		super();
	}
	public String getParsingHelp() {
		return"parsing.help.fig-association-end-name";
	}
	public void parse(Object modelElement,String text) {
		try {
			parseAssociationEnd(modelElement,text);
		}catch (ParseException pe) {
			String msg = "statusmsg.bar.error.parsing.association-end-name";
			Object[]args =  {pe.getLocalizedMessage(),Integer.valueOf(pe.getErrorOffset())};
			ArgoEventPump.fireEvent(new ArgoHelpEvent(ArgoEventTypes.HELP_CHANGED,this,Translator.messageFormat(msg,args)));
		}
	}
	protected void parseAssociationEnd(Object role,String text)throws ParseException {
		MyTokenizer st;
		String name = null;
		StringBuilder stereotype = null;
		String token;
		try {
			st = new MyTokenizer(text,"<<,«,»,>>");
			while (st.hasMoreTokens()) {
				token = st.nextToken();
				if ("<<".equals(token)||"«".equals(token)) {
					if (stereotype != null) {
						String msg = "parsing.error.association-name.twin-stereotypes";
						throw new ParseException(Translator.localize(msg),st.getTokenIndex());
					}
					stereotype = new StringBuilder();
					while (true) {
						token = st.nextToken();
						if (">>".equals(token)||"»".equals(token)) {
							break;
						}
						stereotype.append(token);
					}
				}else {
					if (name != null) {
						String msg = "parsing.error.association-name.twin-names";
						throw new ParseException(Translator.localize(msg),st.getTokenIndex());
					}
					name = token;
				}
			}
		}catch (NoSuchElementException nsee) {
			String ms = "parsing.error.association-name.unexpected-end-element";
			throw new ParseException(Translator.localize(ms),text.length());
		}catch (ParseException pre) {
			throw pre;
		}
		if (name != null) {
			name = name.trim();
		}
		if (name != null&&name.startsWith("+")) {
			name = name.substring(1).trim();
			Model.getCoreHelper().setVisibility(role,Model.getVisibilityKind().getPublic());
		}
		if (name != null&&name.startsWith("-")) {
			name = name.substring(1).trim();
			Model.getCoreHelper().setVisibility(role,Model.getVisibilityKind().getPrivate());
		}
		if (name != null&&name.startsWith("#")) {
			name = name.substring(1).trim();
			Model.getCoreHelper().setVisibility(role,Model.getVisibilityKind().getProtected());
		}
		if (name != null&&name.startsWith("~")) {
			name = name.substring(1).trim();
			Model.getCoreHelper().setVisibility(role,Model.getVisibilityKind().getPackage());
		}
		if (name != null) {
			Model.getCoreHelper().setName(role,name);
		}
		StereotypeUtility.dealWithStereotypes(role,stereotype,true);
	}
	@SuppressWarnings("deprecation")@Deprecated@Override public String toString(Object modelElement,Map args) {
		return toString(modelElement,NotationUtilityUml.isValue("visibilityVisible",args),NotationUtilityUml.isValue("useGuillemets",args));
	}
	private String toString(Object modelElement,boolean showVisibility,boolean useGuillemets) {
		String name = Model.getFacade().getName(modelElement);
		if (name == null) {
			name = "";
		}
		String visibility = "";
		if (showVisibility) {
			visibility = NotationUtilityUml.generateVisibility2(modelElement);
			if (name.length() < 1) {
				visibility = "";
			}
		}
		String stereoString = NotationUtilityUml.generateStereotype(modelElement,useGuillemets);
		if (stereoString.length() > 0) {
			stereoString += " ";
		}
		return stereoString + visibility + name;
	}
	@Override public String toString(Object modelElement,NotationSettings settings) {
		return toString(modelElement,settings.isShowVisibilities(),settings.isUseGuillemets());
	}
}



