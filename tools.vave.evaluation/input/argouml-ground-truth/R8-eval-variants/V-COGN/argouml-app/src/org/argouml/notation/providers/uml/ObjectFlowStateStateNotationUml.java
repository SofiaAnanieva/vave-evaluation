package org.argouml.notation.providers.uml;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Iterator;
import java.util.StringTokenizer;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoHelpEvent;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.notation.NotationSettings;
import org.argouml.notation.providers.ObjectFlowStateStateNotation;


public class ObjectFlowStateStateNotationUml extends ObjectFlowStateStateNotation {
	public ObjectFlowStateStateNotationUml(Object objectflowstate) {
		super(objectflowstate);
	}
	public String getParsingHelp() {
		return"parsing.help.fig-objectflowstate2";
	}
	public void parse(Object modelElement,String text) {
		try {
			parseObjectFlowState2(modelElement,text);
		}catch (ParseException pe) {
			String msg = "statusmsg.bar.error.parsing.objectflowstate";
			Object[]args =  {pe.getLocalizedMessage(),Integer.valueOf(pe.getErrorOffset())};
			ArgoEventPump.fireEvent(new ArgoHelpEvent(ArgoEventTypes.HELP_CHANGED,this,Translator.messageFormat(msg,args)));
		}
	}
	protected Object parseObjectFlowState2(Object objectFlowState,String s)throws ParseException {
		s = s.trim();
		if (s.startsWith("[")) {
			s = s.substring(1);
		}
		if (s.endsWith("]")) {
			s = s.substring(0,s.length() - 1);
		}
		s = s.trim();
		Object c = Model.getFacade().getType(objectFlowState);
		if (c != null) {
			if (Model.getFacade().isAClassifierInState(c)) {
				Object classifier = Model.getFacade().getType(c);
				if ((s == null)||"".equals(s)) {
					Model.getCoreHelper().setType(objectFlowState,classifier);
					delete(c);
					Model.getCoreHelper().setType(objectFlowState,classifier);
					return objectFlowState;
				}
				Collection states = new ArrayList(Model.getFacade().getInStates(c));
				Collection statesToBeRemoved = new ArrayList(states);
				Collection namesToBeAdded = new ArrayList();
				StringTokenizer tokenizer = new StringTokenizer(s,",");
				while (tokenizer.hasMoreTokens()) {
					String nextToken = tokenizer.nextToken().trim();
					boolean found = false;
					Iterator i = states.iterator();
					while (i.hasNext()) {
						Object state = i.next();
						if (Model.getFacade().getName(state) == nextToken) {
							found = true;
							statesToBeRemoved.remove(state);
						}
					}
					if (!found) {
						namesToBeAdded.add(nextToken);
					}
				}
				states.removeAll(statesToBeRemoved);
				Iterator i = namesToBeAdded.iterator();
				while (i.hasNext()) {
					String name = (String) i.next();
					String msg = "parsing.error.object-flow-state.state-not-found";
					Object[]args =  {s};
					throw new ParseException(Translator.localize(msg,args),0);
				}
			}else {
				Collection statesToBeAdded = new ArrayList();
				StringTokenizer tokenizer = new StringTokenizer(s,",");
				while (tokenizer.hasMoreTokens()) {
					String nextToken = tokenizer.nextToken().trim();
					String msg = "parsing.error.object-flow-state.state-not-found";
					Object[]args =  {s};
					throw new ParseException(Translator.localize(msg,args),0);
				}
			}
		}else {
			String msg = "parsing.error.object-flow-state.classifier-not-found";
			throw new ParseException(Translator.localize(msg),0);
		}
		return objectFlowState;
	}
	private void delete(Object obj) {
		if (obj != null) {
			ProjectManager.getManager().getCurrentProject().moveToTrash(obj);
		}
	}
	@SuppressWarnings("deprecation")@Deprecated public String toString(Object modelElement,Map args) {
		return toString(modelElement);
	}
	private String toString(Object modelElement) {
		StringBuilder theNewText = new StringBuilder("");
		Object cis = Model.getFacade().getType(modelElement);
		if (Model.getFacade().isAClassifierInState(cis)) {
			theNewText.append("[ ");
			theNewText.append(formatNameList(Model.getFacade().getInStates(cis)));
			theNewText.append(" ]");
		}
		return theNewText.toString();
	}
	@Override public String toString(Object modelElement,NotationSettings settings) {
		return toString(modelElement);
	}
}



