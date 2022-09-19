package org.argouml.notation.providers.uml;

import java.util.Map;
import org.apache.log4j.Logger;
import org.argouml.notation.NotationSettings;


public class MessageNotationUml extends AbstractMessageNotationUml {
	static final Logger LOG = Logger.getLogger(MessageNotationUml.class);
	public MessageNotationUml(Object message) {
		super(message);
	}
	@Override public String toString(Object modelElement,NotationSettings settings) {
		return toString(modelElement,true);
	}
	@SuppressWarnings("deprecation")@Deprecated public String toString(final Object modelElement,final Map args) {
		return toString(modelElement,true);
	}
}



