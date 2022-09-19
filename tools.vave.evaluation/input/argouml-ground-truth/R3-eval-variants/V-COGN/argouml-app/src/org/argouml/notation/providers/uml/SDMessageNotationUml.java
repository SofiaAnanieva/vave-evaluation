package org.argouml.notation.providers.uml;

import java.util.Map;
import org.argouml.notation.NotationProvider;
import org.argouml.notation.NotationSettings;
import org.argouml.notation.SDNotationSettings;


public class SDMessageNotationUml extends AbstractMessageNotationUml {
	public SDMessageNotationUml(Object message) {
		super(message);
	}
	public String toString(final Object modelElement,NotationSettings settings) {
		if (settings instanceof SDNotationSettings) {
			return toString(modelElement,((SDNotationSettings) settings).isShowSequenceNumbers());
		}else {
			return toString(modelElement,true);
		}
	}
	@SuppressWarnings("deprecation")public String toString(final Object modelElement,final Map args) {
		return toString(modelElement,!NotationProvider.isValue("hideSequenceNrs",args));
	}
}



