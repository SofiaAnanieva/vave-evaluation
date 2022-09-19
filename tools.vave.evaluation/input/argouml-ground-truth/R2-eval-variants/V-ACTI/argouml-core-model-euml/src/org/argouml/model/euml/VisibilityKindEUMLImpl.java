package org.argouml.model.euml;

import org.argouml.model.VisibilityKind;


class VisibilityKindEUMLImpl implements VisibilityKind {
	public Object getPackage() {
		return org.eclipse.uml2.uml.VisibilityKind.PACKAGE_LITERAL;
	}
	public Object getPrivate() {
		return org.eclipse.uml2.uml.VisibilityKind.PRIVATE_LITERAL;
	}
	public Object getProtected() {
		return org.eclipse.uml2.uml.VisibilityKind.PROTECTED_LITERAL;
	}
	public Object getPublic() {
		return org.eclipse.uml2.uml.VisibilityKind.PUBLIC_LITERAL;
	}
}



