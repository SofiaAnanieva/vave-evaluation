package org.argouml.uml;

import java.util.Collection;
import java.util.Iterator;
import org.argouml.application.api.Argo;
import org.argouml.model.Model;
import org.argouml.util.MyTokenizer;


public class DocumentationManager {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	public static String getDocs(Object o,String indent) {
		return getDocs(o,indent,"/** "," *  "," */");
	}
	public static String getDocs(Object o,String indent,String header,String prefix,String footer) {
		String sResult = defaultFor(o,indent);
		if (Model.getFacade().isAModelElement(o)) {
			Iterator iter = Model.getFacade().getTaggedValues(o);
			if (iter != null) {
				while (iter.hasNext()) {
					Object tv = iter.next();
					String tag = Model.getFacade().getTagOfTag(tv);
					if (Argo.DOCUMENTATION_TAG.equals(tag)||Argo.DOCUMENTATION_TAG_ALT.equals(tag)) {
						sResult = Model.getFacade().getValueOfTag(tv);
						if (Argo.DOCUMENTATION_TAG.equals(tag)) {
							break;
						}
					}
				}
			}
		}
		if (sResult == null)return"(No comment)";
		StringBuffer result = new StringBuffer();
		if (header != null) {
			result.append(header).append(LINE_SEPARATOR);
		}
		if (indent != null) {
			if (prefix != null) {
				prefix = indent + prefix;
			}
			if (footer != null) {
				footer = indent + footer;
			}
		}
		appendComment(result,prefix,sResult,0);
		if (footer != null) {
			result.append(footer);
		}
		return result.toString();
	}
	public static void setDocs(Object o,String s) {
		Object taggedValue = Model.getFacade().getTaggedValue(o,Argo.DOCUMENTATION_TAG);
		if (taggedValue == null) {
			taggedValue = Model.getExtensionMechanismsFactory().buildTaggedValue(Argo.DOCUMENTATION_TAG,s);
			Model.getExtensionMechanismsHelper().addTaggedValue(o,taggedValue);
		}else {
			Model.getExtensionMechanismsHelper().setValueOfTag(taggedValue,s);
		}
	}
	public static boolean hasDocs(Object o) {
		if (Model.getFacade().isAModelElement(o)) {
			Iterator i = Model.getFacade().getTaggedValues(o);
			if (i != null) {
				while (i.hasNext()) {
					Object tv = i.next();
					String tag = Model.getFacade().getTagOfTag(tv);
					String value = Model.getFacade().getValueOfTag(tv);
					if ((Argo.DOCUMENTATION_TAG.equals(tag)||Argo.DOCUMENTATION_TAG_ALT.equals(tag))&&value != null&&value.trim().length() > 0) {
						return true;
					}
				}
			}
		}
		return false;
	}
	public static String defaultFor(Object o,String indent) {
		if (Model.getFacade().isAClass(o)) {
			return" A class that represents ...\n\n" + indent + " @see OtherClasses\n" + indent + " @author your_name_here";
		}
		if (Model.getFacade().isAAttribute(o)) {
			return" An attribute that represents ...";
		}
		if (Model.getFacade().isAOperation(o)) {
			return" An operation that does...\n\n" + indent + " @param firstParam a description of this parameter";
		}
		if (Model.getFacade().isAInterface(o)) {
			return" An interface defining operations expected of ...\n\n" + indent + " @see OtherClasses\n" + indent + " @author your_name_here";
		}
		if (Model.getFacade().isAModelElement(o)) {
			return"\n";
		}
		return null;
	}
	public static String getComments(Object o) {
		return getComments(o,"/*"," * "," */");
	}
	public static String getComments(Object o,String header,String prefix,String footer) {
		StringBuffer result = new StringBuffer();
		if (header != null) {
			result.append(header).append(LINE_SEPARATOR);
		}
		if (Model.getFacade().isAUMLElement(o)) {
			Collection comments = Model.getFacade().getComments(o);
			if (!comments.isEmpty()) {
				int nlcount = 2;
				for (Iterator iter = comments.iterator();iter.hasNext();) {
					Object c = iter.next();
					String s = Model.getFacade().getName(c);
					nlcount = appendComment(result,prefix,s,nlcount > 1?0:1);
				}
			}else {
				return"";
			}
		}else {
			return"";
		}
		if (footer != null) {
			result.append(footer).append(LINE_SEPARATOR);
		}
		return result.toString();
	}
	private static int appendComment(StringBuffer sb,String prefix,String comment,int nlprefix) {
		int nlcount = 0;
		for (;nlprefix > 0;nlprefix--) {
			if (prefix != null)sb.append(prefix);
			sb.append(LINE_SEPARATOR);
			nlcount++;
		}
		if (comment == null) {
			return nlcount;
		}
		MyTokenizer tokens = new MyTokenizer(comment,"",MyTokenizer.LINE_SEPARATOR);
		while (tokens.hasMoreTokens()) {
			String s = tokens.nextToken();
			if (!s.startsWith("\r")&&!s.startsWith("\n")) {
				if (prefix != null)sb.append(prefix);
				sb.append(s);
				sb.append(LINE_SEPARATOR);
				nlcount = 0;
			}else if (nlcount > 0) {
				if (prefix != null)sb.append(prefix);
				sb.append(LINE_SEPARATOR);
				nlcount++;
			}else {
				nlcount++;
			}
		}
		return nlcount;
	}
}



