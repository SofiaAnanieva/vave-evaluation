package org.argouml.model;


public interface DiagramInterchangeModel {
	DiDiagram createDiagram(Class type,Object owner);
	void deleteDiagram(DiDiagram diagram);
	DiElement createElement(DiDiagram diagram,Object modelElement);
	void deleteElement(DiElement diagram);
	void nodeAdded(Object source,Object arg);
	void edgeAdded(Object source,Object arg);
	void nodeRemoved(Object source,Object arg);
	void edgeRemoved(Object source,Object arg);
	void graphChanged(Object source,Object arg);
}



