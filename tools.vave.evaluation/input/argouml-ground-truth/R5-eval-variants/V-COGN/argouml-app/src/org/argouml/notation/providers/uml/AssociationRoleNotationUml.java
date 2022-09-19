package org.argouml.notation.providers.uml;

import java.text.ParseException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoHelpEvent;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.notation.NotationSettings;
import org.argouml.notation.providers.AssociationRoleNotation;
import org.argouml.util.MyTokenizer;


public class AssociationRoleNotationUml extends AssociationRoleNotation {
	public AssociationRoleNotationUml(Object role) {
		super(role);
	}
	public String getParsingHelp() {
		return"parsing.help.fig-association-role";
	}
	public void parse(Object modelElement,String text) {
		try {
			parseRole(modelElement,text);
		}catch (ParseException pe) {
			String msg = "statusmsg.bar.error.parsing.association-role";
			Object[]args =  {pe.getLocalizedMessage(),Integer.valueOf(pe.getErrorOffset())};
			ArgoEventPump.fireEvent(new ArgoHelpEvent(ArgoEventTypes.HELP_CHANGED,this,Translator.messageFormat(msg,args)));
		}
	}
	protected void parseRole(Object role,String text)throws ParseException {
		String token;
		boolean hasColon = false;
		boolean hasSlash = false;
		String rolestr = null;
		String basestr = null;
		MyTokenizer st = new MyTokenizer(text," ,\t,/,:");
		while (st.hasMoreTokens()) {
			token = st.nextToken();
			if (" ".equals(token)||"\t".equals(token)) {
			}else if ("/".equals(token)) {
				hasSlash = true;
				hasColon = false;
			}else if (":".equals(token)) {
				hasColon = true;
				hasSlash = false;
			}else if (hasColon) {
				if (basestr != null) {
					String msg = "parsing.error.association-role.association-extra-text";
					throw new ParseException(Translator.localize(msg),st.getTokenIndex());
				}
				basestr = token;
			}else if (hasSlash) {
				if (rolestr != null) {
					String msg = "parsing.error.association-role.association-extra-text";
					throw new ParseException(Translator.localize(msg),st.getTokenIndex());
				}
				rolestr = token;
			}else {
				String msg = "parsing.error.association-role.association-extra-text";
				throw new ParseException(Translator.localize(msg),st.getTokenIndex());
			}
		}
		if (basestr == null) {
			if (rolestr != null) {
				Model.getCoreHelper().setName(role,rolestr.trim());
			}
			return;
		}
		Object currentBase = Model.getFacade().getBase(role);
		if (currentBase != null) {
			String currentBaseStr = Model.getFacade().getName(currentBase);
			if (currentBaseStr == null) {
				currentBaseStr = "";
			}
			if (currentBaseStr.equals(basestr)) {
				if (rolestr != null) {
					Model.getCoreHelper().setName(role,rolestr.trim());
				}
				return;
			}
		}
		String msg = "parsing.error.association-role.base-not-found";
		throw new ParseException(Translator.localize(msg),0);
	}
	@SuppressWarnings("deprecation")@Deprecated public String toString(Object modelElement,Map args) {
		return toString(modelElement);
	}
	private String toString(final Object modelElement) {
		String name = Model.getFacade().getName(modelElement);
		if (name == null) {
			name = "";
		}
		if (name.length() > 0) {
			name = "/" + name;
		}
		Object assoc = Model.getFacade().getBase(modelElement);
		if (assoc != null) {
			String baseName = Model.getFacade().getName(assoc);
			if (baseName != null&&baseName.length() > 0) {
				name = name + ":" + baseName;
			}
		}
		return name;
	}
	@Override public String toString(final Object modelElement,final NotationSettings settings) {
		return toString(modelElement);
	}
}



