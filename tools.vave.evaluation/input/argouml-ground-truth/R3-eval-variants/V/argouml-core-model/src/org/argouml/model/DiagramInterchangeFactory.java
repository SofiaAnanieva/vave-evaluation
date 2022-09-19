package org.argouml.model;

import java.util.Collection;
import java.util.List;


public interface DiagramInterchangeFactory {
	List getModelDiagram();
	void setModelDiagram(List diagrams);
	boolean isDiagramOwner(Object modelElement,Object diagram);
	Object getDiagramElementOwner(Object diagram);
	Object createDiagram();
	Object createDiagramLink();
	Object createEllipse();
	Object createGraphConnector();
	Object createGraphEdge();
	Object createGraphNode();
	Object createImage();
	Object createPolyline();
	Object createProperty();
	Object createReference();
	Object createSimpleSemanticModelElement();
	Object createTextElement();
	Object createUml1SemanticModelBridge();
	Object createBezierPoint(Object base,Object control1,Object control2);
	Object createDimension(double width,double height);
	Object createPoint(double x,double y);
	Object buildUml1SemanticModelBridge(Object modelElement,Object diagramOrGraphElement);
	Object buildSimpleSemanticModelElement(Object grafElement,Object diagram,String presentation,String typeInfo);
	Object buildDiagram(Object model);
	Object buildGraphNode(Object parentGraphElement,Object modelElement);
	Object buildProperty(String key,String value);
	boolean addProperty(Object arg0,Object arg1);
	boolean existsProperty(Object arg0,Object arg1);
	Collection getProperties(Object arg0);
	boolean removeProperty(Object arg0,Object arg1);
	boolean hasProperty(Object diagramElement,String propertyName);
	void setProperty(Object diagramElement,String key,String value);
	String getProperty(Object diagramElement,String key);
}



