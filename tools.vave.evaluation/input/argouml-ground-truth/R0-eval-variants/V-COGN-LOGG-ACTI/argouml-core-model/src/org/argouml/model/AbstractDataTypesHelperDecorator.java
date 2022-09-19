package org.argouml.model;


public abstract class AbstractDataTypesHelperDecorator implements DataTypesHelper {
	private DataTypesHelper impl;
	protected AbstractDataTypesHelperDecorator(DataTypesHelper component) {
		impl = component;
	}
	protected DataTypesHelper getComponent() {
		return impl;
	}
	public boolean equalsINITIALKind(Object kind) {
		return impl.equalsINITIALKind(kind);
	}
	public boolean equalsDeepHistoryKind(Object kind) {
		return impl.equalsDeepHistoryKind(kind);
	}
	public boolean equalsShallowHistoryKind(Object kind) {
		return impl.equalsShallowHistoryKind(kind);
	}
	public boolean equalsFORKKind(Object kind) {
		return impl.equalsFORKKind(kind);
	}
	public boolean equalsJOINKind(Object kind) {
		return impl.equalsJOINKind(kind);
	}
	public boolean equalsCHOICEKind(Object kind) {
		return impl.equalsCHOICEKind(kind);
	}
	public boolean equalsJUNCTIONKind(Object kind) {
		return impl.equalsJUNCTIONKind(kind);
	}
	public String multiplicityToString(Object multiplicity) {
		return impl.multiplicityToString(multiplicity);
	}
	public Object setBody(Object handle,String body) {
		return impl.setBody(handle,body);
	}
	public String getBody(Object handle) {
		return impl.getBody(handle);
	}
	public Object setLanguage(Object handle,String language) {
		return impl.setLanguage(handle,language);
	}
	public String getLanguage(Object handle) {
		return impl.getLanguage(handle);
	}
}



