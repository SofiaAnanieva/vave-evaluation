package org.argouml.model;


public interface UmlFactory {
	Object buildConnection(Object connectionType,Object fromElement,Object fromStyle,Object toElement,Object toStyle,Object unidirectional,Object namespace)throws IllegalModelElementConnectionException;
	Object buildNode(Object elementType);
	Object buildNode(Object elementType,Object container);
	boolean isConnectionType(Object connectionType);
	boolean isConnectionValid(Object connectionType,Object fromElement,Object toElement,boolean checkWFR);
	boolean isContainmentValid(Object metaType,Object container);
	void delete(Object elem);
	void deleteExtent(Object element);
	boolean isRemoved(Object o);
}



