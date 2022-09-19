package org.argouml.notation.providers.java;

import java.util.Map;
import java.util.Stack;
import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;


public class NotationUtilityJava {
	NotationUtilityJava() {
		}
	static String generateVisibility(Object o) {
		if (Model.getFacade().isAFeature(o)) {
			Object tv = Model.getFacade().getTaggedValue(o,"src_visibility");
			if (tv != null) {
				Object tvValue = Model.getFacade().getValue(tv);
				if (tvValue instanceof String) {
					String tagged = (String) tvValue;
					if (tagged != null) {
						if (tagged.trim().equals("")||tagged.trim().toLowerCase().equals("package")||tagged.trim().toLowerCase().equals("default")) {
							return"";
						}
						return tagged + " ";
					}
				}
			}
		}
		if (Model.getFacade().isAModelElement(o)) {
			if (Model.getFacade().isPublic(o)) {
				return"public ";
			}
			if (Model.getFacade().isPrivate(o)) {
				return"private ";
			}
			if (Model.getFacade().isProtected(o)) {
				return"protected ";
			}
			if (Model.getFacade().isPackage(o)) {
				return"";
			}
		}
		if (Model.getFacade().isAVisibilityKind(o)) {
			if (Model.getVisibilityKind().getPublic().equals(o)) {
				return"public ";
			}
			if (Model.getVisibilityKind().getPrivate().equals(o)) {
				return"private ";
			}
			if (Model.getVisibilityKind().getProtected().equals(o)) {
				return"protected ";
			}
			if (Model.getVisibilityKind().getPackage().equals(o)) {
				return"";
			}
		}
		return"";
	}
	static String generateScope(Object f) {
		if (Model.getFacade().isStatic(f)) {
			return"static ";
		}
		return"";
	}
	static String generateChangeability(Object obj) {
		if (Model.getFacade().isAAttribute(obj)) {
			if (Model.getFacade().isReadOnly(obj)) {
				return"final ";
			}
		}else {
			if (Model.getFacade().isAOperation(obj)) {
				if (Model.getFacade().isLeaf(obj)) {
					return"final ";
				}
			}
		}
		return"";
	}
	static String generateClassifierRef(Object cls) {
		if (cls == null) {
			return"";
		}
		return Model.getFacade().getName(cls);
	}
	static String generateExpression(Object expr) {
		if (Model.getFacade().isAExpression(expr)) {
			return generateUninterpreted((String) Model.getFacade().getBody(expr));
		}else if (Model.getFacade().isAConstraint(expr)) {
			return generateExpression(Model.getFacade().getBody(expr));
		}
		return"";
	}
	static String generateUninterpreted(String un) {
		if (un == null) {
			return"";
		}
		return un;
	}
	static String generateParameter(Object parameter) {
		StringBuffer sb = new StringBuffer(20);
		sb.append(generateClassifierRef(Model.getFacade().getType(parameter)));
		sb.append(' ');
		sb.append(Model.getFacade().getName(parameter));
		return sb.toString();
	}
	@Deprecated static String generateAbstract(Object modelElement,@SuppressWarnings("unused")Map args) {
		return generateAbstract(modelElement);
	}
	static String generateAbstract(Object modelElement) {
		if (Model.getFacade().isAbstract(modelElement)) {
			return"abstract ";
		}
		return"";
	}
	@Deprecated static String generateLeaf(Object modelElement,@SuppressWarnings("unused")Map args) {
		return generateLeaf(modelElement);
	}
	static String generateLeaf(Object modelElement) {
		if (Model.getFacade().isLeaf(modelElement)) {
			return"final ";
		}
		return"";
	}
	static String generatePath(Object modelElement,Map args) {
		if (NotationProvider.isValue("pathVisible",args)) {
			return generatePath(modelElement);
		}else {
			return"";
		}
	}
	static String generatePath(Object modelElement) {
		StringBuilder s = new StringBuilder();
		Stack<String>stack = new Stack<String>();
		Object ns = Model.getFacade().getNamespace(modelElement);
		while (ns != null&&!Model.getFacade().isAModel(ns)) {
			stack.push(Model.getFacade().getName(ns));
			ns = Model.getFacade().getNamespace(ns);
		}
		while (!stack.isEmpty()) {
			s.append(stack.pop()).append(".");
		}
		if (s.length() > 0&&!(s.lastIndexOf(".") == s.length() - 1)) {
			s.append(".");
		}
		return s.toString();
	}
	@Deprecated static String generateVisibility(Object modelElement,Map args) {
		String s = "";
		if (NotationProvider.isValue("visibilityVisible",args)) {
			s = NotationUtilityJava.generateVisibility(modelElement);
		}
		return s;
	}
}



