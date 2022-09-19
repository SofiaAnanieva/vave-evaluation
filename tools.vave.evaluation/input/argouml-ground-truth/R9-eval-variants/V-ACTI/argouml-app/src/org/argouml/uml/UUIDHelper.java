package org.argouml.uml;

import org.argouml.model.Model;
import org.tigris.gef.presentation.Fig;


public final class UUIDHelper {
	private UUIDHelper() {
	}
	public static String getUUID(Object base) {
		if (base instanceof Fig) {
			base = ((Fig) base).getOwner();
		}
		if (base == null)return null;
		if (base instanceof CommentEdge) {
			return(String) ((CommentEdge) base).getUUID();
		}
		return Model.getFacade().getUUID(base);
	}
	public static String getNewUUID() {
		return org.argouml.model.UUIDManager.getInstance().getNewUUID();
	}
}



