package org.argouml.notation.providers;

import java.beans.PropertyChangeListener;
import java.util.List;
import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;


public abstract class MessageNotation extends NotationProvider {
	public MessageNotation(Object message) {
		if (!Model.getFacade().isAMessage(message)) {
			throw new IllegalArgumentException("This is not an Message.");
		}
	}
	public void initialiseListener(PropertyChangeListener listener,Object umlMessage) {
		addElementListener(listener,umlMessage,new String[] {"activator","predecessor","successor","sender","receiver","action","name"});
		Object action = Model.getFacade().getAction(umlMessage);
		if (action != null) {
			addElementListener(listener,action,new String[] {"remove","recurrence","script","actualArgument","signal","operation"});
			List args = Model.getFacade().getActualArguments(action);
			for (Object argument:args) {
				addElementListener(listener,argument,new String[] {"remove","value"});
			}
			if (Model.getFacade().isACallAction(action)) {
				Object operation = Model.getFacade().getOperation(action);
				if (Model.getFacade().isAOperation(operation)) {
					addElementListener(listener,operation,new String[] {"name"});
				}
			}
			if (Model.getFacade().isASendAction(action)) {
				Object signal = Model.getFacade().getSignal(action);
				if (Model.getFacade().isASignal(signal)) {
					addElementListener(listener,signal,new String[] {"name"});
				}
			}
		}
	}
}



