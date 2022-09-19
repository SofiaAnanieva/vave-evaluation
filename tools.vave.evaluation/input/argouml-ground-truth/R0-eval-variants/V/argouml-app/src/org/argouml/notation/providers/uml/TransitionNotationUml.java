package org.argouml.notation.providers.uml;

import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoHelpEvent;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.notation.NotationSettings;
import org.argouml.notation.providers.TransitionNotation;
import org.argouml.notation.providers.uml.NotationUtilityUml;


public class TransitionNotationUml extends TransitionNotation {
	public TransitionNotationUml(Object transition) {
		super(transition);
	}
	public void parse(Object modelElement,String text) {
		try {
			parseTransition(modelElement,text);
		}catch (ParseException pe) {
			String msg = "statusmsg.bar.error.parsing.transition";
			Object[]args =  {pe.getLocalizedMessage(),Integer.valueOf(pe.getErrorOffset())};
			ArgoEventPump.fireEvent(new ArgoHelpEvent(ArgoEventTypes.HELP_CHANGED,this,Translator.messageFormat(msg,args)));
		}
	}
	protected Object parseTransition(Object trans,String s)throws ParseException {
		s = s.trim();
		int a = s.indexOf("[");
		int b = s.indexOf("]");
		int c = s.indexOf("/");
		if (((a < 0)&&(b >= 0))||((b < 0)&&(a >= 0))||(b < a)) {
			String msg = "parsing.error.transition.no-matching-square-brackets";
			throw new ParseException(Translator.localize(msg),0);
		}
		if ((c >= 0)&&(c < b)) {
			String msg = "parsing.error.transition.found-bracket-instead-slash";
			throw new ParseException(Translator.localize(msg),0);
		}
		StringTokenizer tokenizer = new StringTokenizer(s,"[/");
		String eventSignature = null;
		String guardCondition = null;
		String actionExpression = null;
		while (tokenizer.hasMoreTokens()) {
			String nextToken = tokenizer.nextToken().trim();
			if (nextToken.endsWith("]")) {
				guardCondition = nextToken.substring(0,nextToken.length() - 1);
			}else {
				if (s.startsWith(nextToken)) {
					eventSignature = nextToken;
				}else {
					if (s.endsWith(nextToken)) {
						actionExpression = nextToken;
					}
				}
			}
		}
		if (eventSignature != null) {
			parseTrigger(trans,eventSignature);
		}
		if (guardCondition != null) {
			parseGuard(trans,guardCondition.substring(guardCondition.indexOf('[') + 1));
		}
		if (actionExpression != null) {
			parseEffect(trans,actionExpression.trim());
		}
		return trans;
	}
	private void parseTrigger(Object trans,String trigger)throws ParseException {
		String s = "";
		boolean timeEvent = false;
		boolean changeEvent = false;
		boolean callEvent = false;
		boolean signalEvent = false;
		trigger = trigger.trim();
		StringTokenizer tokenizer = new StringTokenizer(trigger,"()");
		String name = tokenizer.nextToken().trim();
		if (name.equalsIgnoreCase("after")) {
			timeEvent = true;
		}else if (name.equalsIgnoreCase("when")) {
			changeEvent = true;
		}else {
			if (tokenizer.hasMoreTokens()||(trigger.indexOf("(") > 0)||(trigger.indexOf(")") > 1)) {
				callEvent = true;
				if (!trigger.endsWith(")")||!(trigger.indexOf("(") > 0)) {
					String msg = "parsing.error.transition.no-matching-brackets";
					throw new ParseException(Translator.localize(msg),0);
				}
			}else {
				signalEvent = true;
			}
		}
		if (timeEvent||changeEvent||callEvent) {
			if (tokenizer.hasMoreTokens()) {
				s = tokenizer.nextToken().trim();
			}
		}
		Object evt = Model.getFacade().getTrigger(trans);
		if (evt == null) {
		}else {
			delete(evt);
		}
	}
	private void parseGuard(Object trans,String guard) {
		Object g = Model.getFacade().getGuard(trans);
		if (guard.length() > 0) {
			if (g == null) {
			}else {
				Object expr = Model.getFacade().getExpression(g);
				String language = "";
				if (expr != null) {
					language = Model.getDataTypesHelper().getLanguage(expr);
				}
			}
		}else {
			if (g == null) {
			}else {
				delete(g);
			}
		}
	}
	private void parseEffect(Object trans,String actions) {
		Object effect = Model.getFacade().getEffect(trans);
		if (actions.length() > 0) {
			if (effect == null) {
				effect = Model.getCommonBehaviorFactory().createCallAction();
				Model.getCommonBehaviorHelper().setScript(effect,Model.getDataTypesFactory().createActionExpression("",actions));
				Model.getCoreHelper().setName(effect,"anon");
			}else {
				Object script = Model.getFacade().getScript(effect);
				String language = (script == null)?null:Model.getDataTypesHelper().getLanguage(script);
				Model.getCommonBehaviorHelper().setScript(effect,Model.getDataTypesFactory().createActionExpression(language,actions));
			}
		}else {
			if (effect == null) {
			}else {
				delete(effect);
			}
		}
	}
	private void delete(Object obj) {
		if (obj != null) {
			Model.getUmlFactory().delete(obj);
		}
	}
	public String getParsingHelp() {
		return"parsing.help.fig-transition";
	}
	@Override public String toString(Object modelElement,NotationSettings settings) {
		return toString(modelElement);
	}
	@SuppressWarnings("deprecation")@Deprecated public String toString(Object modelElement,Map args) {
		return toString(modelElement);
	}
	private String toString(Object modelElement) {
		Object trigger = Model.getFacade().getTrigger(modelElement);
		Object guard = Model.getFacade().getGuard(modelElement);
		Object effect = Model.getFacade().getEffect(modelElement);
		String t = generateEvent(trigger);
		String g = generateGuard(guard);
		String e = NotationUtilityUml.generateActionSequence(effect);
		if (g.length() > 0) {
			t += " [" + g + "]";
		}
		if (e.length() > 0) {
			t += " / " + e;
		}
		return t;
	}
	private String generateEvent(Object m) {
		if (m == null) {
			return"";
		}
		StringBuffer event = new StringBuffer();
		if (Model.getFacade().isAChangeEvent(m)) {
			event.append("when(");
			event.append(generateExpression(Model.getFacade().getExpression(m)));
			event.append(")");
		}else if (Model.getFacade().isATimeEvent(m)) {
			event.append("after(");
			event.append(generateExpression(Model.getFacade().getExpression(m)));
			event.append(")");
		}else if (Model.getFacade().isASignalEvent(m)) {
			event.append(Model.getFacade().getName(m));
		}else if (Model.getFacade().isACallEvent(m)) {
			event.append(Model.getFacade().getName(m));
			event.append(generateParameterList(m));
		}
		return event.toString();
	}
	private String generateGuard(Object m) {
		if (m != null) {
			if (Model.getFacade().getExpression(m) != null) {
				return generateExpression(Model.getFacade().getExpression(m));
			}
		}
		return"";
	}
	private String generateParameterList(Object parameterListOwner) {
		Iterator it = Model.getFacade().getParameters(parameterListOwner).iterator();
		StringBuffer list = new StringBuffer();
		list.append("(");
		if (it.hasNext()) {
			while (it.hasNext()) {
				Object param = it.next();
				list.append(generateParameter(param));
				if (it.hasNext()) {
					list.append(", ");
				}
			}
		}
		list.append(")");
		return list.toString();
	}
	private String generateExpression(Object expr) {
		if (Model.getFacade().isAExpression(expr)) {
			Object body = Model.getFacade().getBody(expr);
			if (body != null) {
				return(String) body;
			}
		}
		return"";
	}
	public String generateParameter(Object parameter) {
		StringBuffer s = new StringBuffer();
		s.append(generateKind(Model.getFacade().getKind(parameter)));
		if (s.length() > 0) {
			s.append(" ");
		}
		s.append(Model.getFacade().getName(parameter));
		String classRef = generateClassifierRef(Model.getFacade().getType(parameter));
		if (classRef.length() > 0) {
			s.append(" : ");
			s.append(classRef);
		}
		String defaultValue = generateExpression(Model.getFacade().getDefaultValue(parameter));
		if (defaultValue.length() > 0) {
			s.append(" = ");
			s.append(defaultValue);
		}
		return s.toString();
	}
	private String generateKind(Object kind) {
		StringBuffer s = new StringBuffer();
		if (kind == null||kind == Model.getDirectionKind().getInParameter()) {
			s.append("");
		}else if (kind == Model.getDirectionKind().getInOutParameter()) {
			s.append("inout");
		}else if (kind == Model.getDirectionKind().getReturnParameter()) {
		}else if (kind == Model.getDirectionKind().getOutParameter()) {
			s.append("out");
		}
		return s.toString();
	}
	private String generateClassifierRef(Object cls) {
		if (cls == null) {
			return"";
		}
		return Model.getFacade().getName(cls);
	}
	public void initialiseListener(PropertyChangeListener listener,Object modelElement) {
		addListenersForTransition(listener,modelElement);
	}
	private void addListenersForAction(PropertyChangeListener listener,Object action) {
		if (action != null) {
			addElementListener(listener,action,new String[] {"script","actualArgument","action"});
			Collection args = Model.getFacade().getActualArguments(action);
			Iterator i = args.iterator();
			while (i.hasNext()) {
				Object argument = i.next();
				addElementListener(listener,argument,"value");
			}
			if (Model.getFacade().isAActionSequence(action)) {
				Collection subactions = Model.getFacade().getActions(action);
				i = subactions.iterator();
				while (i.hasNext()) {
					Object a = i.next();
					addListenersForAction(listener,a);
				}
			}
		}
	}
	private void addListenersForEvent(PropertyChangeListener listener,Object event) {
		if (event != null) {
			if (Model.getFacade().isAEvent(event)) {
				addElementListener(listener,event,new String[] {"parameter","name"});
			}
			if (Model.getFacade().isATimeEvent(event)) {
				addElementListener(listener,event,new String[] {"when"});
			}
			if (Model.getFacade().isAChangeEvent(event)) {
				addElementListener(listener,event,new String[] {"changeExpression"});
			}
			Collection prms = Model.getFacade().getParameters(event);
			Iterator i = prms.iterator();
			while (i.hasNext()) {
				Object parameter = i.next();
				addElementListener(listener,parameter);
			}
		}
	}
	private void addListenersForTransition(PropertyChangeListener listener,Object transition) {
		addElementListener(listener,transition,new String[] {"guard","trigger","effect"});
		Object guard = Model.getFacade().getGuard(transition);
		if (guard != null) {
			addElementListener(listener,guard,"expression");
		}
		Object trigger = Model.getFacade().getTrigger(transition);
		addListenersForEvent(listener,trigger);
		Object effect = Model.getFacade().getEffect(transition);
		addListenersForAction(listener,effect);
	}
}



