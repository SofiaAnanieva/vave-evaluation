package org.argouml.profile.internal;

import java.util.Iterator;
import org.argouml.model.Model;
import org.argouml.profile.FormatingStrategy;


public class JavaFormatingStrategy implements FormatingStrategy {
	public String formatElement(Object element,Object namespace) {
		String value = null;
		if (element == null) {
			value = "";
		}else {
			Object elementNs = Model.getFacade().getNamespace(element);
			if (Model.getFacade().isAAssociationEnd(element)) {
				Object assoc = Model.getFacade().getAssociation(element);
				if (assoc != null) {
					elementNs = Model.getFacade().getNamespace(assoc);
				}
			}
			if (elementNs == namespace) {
				value = Model.getFacade().getName(element);
				if (value == null||value.length() == 0) {
					value = defaultName(element,namespace);
				}
			}else {
				StringBuffer buffer = new StringBuffer();
				String pathSep = getPathSeparator();
				buildPath(buffer,element,pathSep);
				value = buffer.toString();
			}
		}
		return value;
	}
	protected String defaultAssocEndName(Object assocEnd,Object namespace) {
		String name = null;
		Object type = Model.getFacade().getType(assocEnd);
		if (type != null) {
			name = formatElement(type,namespace);
		}else {
			name = "unknown type";
		}
		Object mult = Model.getFacade().getMultiplicity(assocEnd);
		if (mult != null) {
			StringBuffer buf = new StringBuffer(name);
			buf.append("[");
			buf.append(Integer.toString(Model.getFacade().getLower(mult)));
			buf.append("..");
			int upper = Model.getFacade().getUpper(mult);
			if (upper >= 0) {
				buf.append(Integer.toString(upper));
			}else {
				buf.append("*");
			}
			buf.append("]");
			name = buf.toString();
		}
		return name;
	}
	protected String defaultAssocName(Object assoc,Object ns) {
		StringBuffer buf = new StringBuffer();
		Iterator iter = Model.getFacade().getConnections(assoc).iterator();
		for (int i = 0;iter.hasNext();i++) {
			if (i != 0) {
				buf.append("-");
			}
			buf.append(defaultAssocEndName(iter.next(),ns));
		}
		return buf.toString();
	}
	protected String defaultGeneralizationName(Object gen,Object namespace) {
		Object child = Model.getFacade().getSpecific(gen);
		Object parent = Model.getFacade().getGeneral(gen);
		StringBuffer buf = new StringBuffer();
		buf.append(formatElement(child,namespace));
		buf.append(" extends ");
		buf.append(formatElement(parent,namespace));
		return buf.toString();
	}
	protected String defaultName(Object element,Object namespace) {
		String name = null;
		if (Model.getFacade().isAAssociationEnd(element)) {
			name = defaultAssocEndName(element,namespace);
		}else {
			if (Model.getFacade().isAAssociation(element)) {
				name = defaultAssocName(element,namespace);
			}
			if (Model.getFacade().isAGeneralization(element)) {
				name = defaultGeneralizationName(element,namespace);
			}
		}
		if (name == null) {
			name = "anon";
		}
		return name;
	}
	protected String getPathSeparator() {
		return".";
	}
	private void buildPath(StringBuffer buffer,Object element,String pathSep) {
		if (element != null) {
			Object parent = Model.getFacade().getNamespace(element);
			if (parent != null&&parent != element) {
				buildPath(buffer,parent,pathSep);
				buffer.append(pathSep);
			}
			String name = Model.getFacade().getName(element);
			if (name == null||name.length() == 0) {
				name = defaultName(element,null);
			}
			buffer.append(name);
		}
	}
	protected String getElementSeparator() {
		return", ";
	}
	protected String getEmptyCollection() {
		return"[empty]";
	}
	public String formatCollection(Iterator iter,Object namespace) {
		String value = null;
		if (iter.hasNext()) {
			StringBuffer buffer = new StringBuffer();
			String elementSep = getElementSeparator();
			Object obj = null;
			for (int i = 0;iter.hasNext();i++) {
				if (i > 0) {
					buffer.append(elementSep);
				}
				obj = iter.next();
				if (Model.getFacade().isAModelElement(obj)) {
					buffer.append(formatElement(obj,namespace));
				}else {
					buffer.append(obj.toString());
				}
			}
			value = buffer.toString();
		}else {
			value = getEmptyCollection();
		}
		return value;
	}
}



