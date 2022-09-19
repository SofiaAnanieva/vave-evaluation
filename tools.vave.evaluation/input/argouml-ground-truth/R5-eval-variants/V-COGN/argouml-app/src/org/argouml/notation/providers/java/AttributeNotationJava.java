package org.argouml.notation.providers.java;

import java.util.Map;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoHelpEvent;
import org.argouml.model.Model;
import org.argouml.notation.NotationSettings;
import org.argouml.notation.providers.AttributeNotation;


public class AttributeNotationJava extends AttributeNotation {
	protected AttributeNotationJava() {
		super();
	}
	private static final AttributeNotationJava INSTANCE = new AttributeNotationJava();
	public static final AttributeNotationJava getInstance() {
		return INSTANCE;
	}
	public String getParsingHelp() {
		return"Parsing in Java not yet supported";
	}
	public void parse(Object modelElement,String text) {
		ArgoEventPump.fireEvent(new ArgoHelpEvent(ArgoEventTypes.HELP_CHANGED,this,"Parsing in Java not yet supported"));
	}
	@SuppressWarnings("deprecation")@Deprecated public String toString(Object modelElement,Map args) {
		return toString(modelElement);
	}
	private String toString(Object modelElement) {
		StringBuffer sb = new StringBuffer(80);
		sb.append(NotationUtilityJava.generateVisibility(modelElement));
		sb.append(NotationUtilityJava.generateScope(modelElement));
		sb.append(NotationUtilityJava.generateChangeability(modelElement));
		Object type = Model.getFacade().getType(modelElement);
		Object multi = Model.getFacade().getMultiplicity(modelElement);
		if (type != null&&multi != null) {
			if (Model.getFacade().getUpper(multi) == 1) {
				sb.append(NotationUtilityJava.generateClassifierRef(type)).append(' ');
			}else if (Model.getFacade().isADataType(type)) {
				sb.append(NotationUtilityJava.generateClassifierRef(type)).append("[] ");
			}else {
				sb.append("java.util.Vector ");
			}
		}
		sb.append(Model.getFacade().getName(modelElement));
		Object init = Model.getFacade().getInitialValue(modelElement);
		if (init != null) {
			String initStr = NotationUtilityJava.generateExpression(init).trim();
			if (initStr.length() > 0) {
				sb.append(" = ").append(initStr);
			}
		}
		return sb.toString();
	}
	@Override public String toString(Object modelElement,NotationSettings settings) {
		return toString(modelElement);
	}
}



