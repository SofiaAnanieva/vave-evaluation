package org.argouml.ui.explorer.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;


public class GoComponentToResidentModelElement extends AbstractPerspectiveRule {
	public String getRuleName() {
		return Translator.localize("misc.component.resident.modelelement");
	}
	public Collection getChildren(Object parent) {
		if (Model.getFacade().isAComponent(parent)) {
			Iterator eri = Model.getFacade().getResidentElements(parent).iterator();
			Collection result = new ArrayList();
			while (eri.hasNext()) {
				result.add(Model.getFacade().getResident(eri.next()));
			}
			return result;
		}
		return Collections.EMPTY_SET;
	}
	public Set getDependencies(Object parent) {
		return Collections.EMPTY_SET;
	}
}



