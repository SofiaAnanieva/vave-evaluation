package org.argouml.persistence;

import java.awt.Rectangle;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.AttributesCompartmentContainer;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ExtensionsCompartmentContainer;
import org.argouml.uml.diagram.OperationsCompartmentContainer;
import org.argouml.uml.diagram.PathContainer;
import org.argouml.uml.diagram.StereotypeContainer;
import org.argouml.uml.diagram.VisibilityContainer;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigEdgePort;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.persistence.pgml.Container;
import org.tigris.gef.persistence.pgml.FigEdgeHandler;
import org.tigris.gef.persistence.pgml.FigGroupHandler;
import org.tigris.gef.persistence.pgml.HandlerStack;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigNode;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


class PGMLStackParser extends org.tigris.gef.persistence.pgml.PGMLStackParser {
	private static final Logger LOG = Logger.getLogger(PGMLStackParser.class);
	private List<EdgeData>figEdges = new ArrayList<EdgeData>(50);
	private LinkedHashMap<FigEdge,Object>modelElementsByFigEdge = new LinkedHashMap<FigEdge,Object>(50);
	private DiagramSettings diagramSettings;
	@Deprecated public PGMLStackParser(Map modelElementsByUuid) {
		super(modelElementsByUuid);
		addTranslations();
	}
	private void addTranslations() {
		addTranslation("org.argouml.uml.diagram.ui.FigNote","org.argouml.uml.diagram.static_structure.ui.FigComment");
		addTranslation("org.argouml.uml.diagram.static_structure.ui.FigNote","org.argouml.uml.diagram.static_structure.ui.FigComment");
		addTranslation("org.argouml.uml.diagram.state.ui.FigState","org.argouml.uml.diagram.state.ui.FigSimpleState");
		addTranslation("org.argouml.uml.diagram.ui.FigCommentPort","org.argouml.uml.diagram.ui.FigEdgePort");
		addTranslation("org.tigris.gef.presentation.FigText","org.argouml.uml.diagram.ui.ArgoFigText");
		addTranslation("org.tigris.gef.presentation.FigLine","org.argouml.gefext.ArgoFigLine");
		addTranslation("org.tigris.gef.presentation.FigPoly","org.argouml.gefext.ArgoFigPoly");
		addTranslation("org.tigris.gef.presentation.FigCircle","org.argouml.gefext.ArgoFigCircle");
		addTranslation("org.tigris.gef.presentation.FigRect","org.argouml.gefext.ArgoFigRect");
		addTranslation("org.tigris.gef.presentation.FigRRect","org.argouml.gefext.ArgoFigRRect");
		addTranslation("org.argouml.uml.diagram.deployment.ui.FigMNodeInstance","org.argouml.uml.diagram.deployment.ui.FigNodeInstance");
		addTranslation("org.argouml.uml.diagram.ui.FigRealization","org.argouml.uml.diagram.ui.FigAbstraction");
	}
	public PGMLStackParser(Map<String,Object>modelElementsByUuid,DiagramSettings defaultSettings) {
		super(modelElementsByUuid);
		addTranslations();
		diagramSettings = new DiagramSettings(defaultSettings);
	}
	@Override public DefaultHandler getHandler(HandlerStack stack,Object container,String uri,String localname,String qname,Attributes attributes)throws SAXException {
		String href = attributes.getValue("href");
		Object owner = null;
		if (href != null) {
			owner = findOwner(href);
			if (owner == null) {
				LOG.warn("Found href of " + href + " with no matching element in model");
				return null;
			}
		}
		if (container instanceof FigGroupHandler) {
			FigGroup group = ((FigGroupHandler) container).getFigGroup();
			if (group instanceof FigNode&&!qname.equals("private")) {
				return null;
			}
		}
		if (qname.equals("private")&&(container instanceof Container)) {
			return new PrivateHandler(this,(Container) container);
		}
		DefaultHandler handler = super.getHandler(stack,container,uri,localname,qname,attributes);
		if (handler instanceof FigEdgeHandler) {
			return new org.argouml.persistence.FigEdgeHandler(this,((FigEdgeHandler) handler).getFigEdge());
		}
		return handler;
	}
	@Override protected final void setAttrs(Fig f,Attributes attrList)throws SAXException {
		if (f instanceof FigGroup) {
			FigGroup group = (FigGroup) f;
			String clsNameBounds = attrList.getValue("description");
			if (clsNameBounds != null) {
				StringTokenizer st = new StringTokenizer(clsNameBounds,",;[] ");
				if (st.hasMoreElements()) {
					st.nextToken();
				}
				if (st.hasMoreElements()) {
					st.nextToken();
				}
				if (st.hasMoreElements()) {
					st.nextToken();
				}
				if (st.hasMoreElements()) {
					st.nextToken();
				}
				if (st.hasMoreElements()) {
					st.nextToken();
				}
				Map<String,String>attributeMap = interpretStyle(st);
				setStyleAttributes(group,attributeMap);
			}
		}
		String name = attrList.getValue("name");
		if (name != null&&!name.equals("")) {
			registerFig(f,name);
		}
		setCommonAttrs(f,attrList);
		final String href = attrList.getValue("href");
		if (href != null&&!href.equals("")) {
			Object modelElement = findOwner(href);
			if (modelElement == null) {
				LOG.error("Can\'t find href of " + href);
				throw new SAXException("Found href of " + href + " with no matching element in model");
			}
			if (f.getOwner() != modelElement) {
				if (f instanceof FigEdge) {
					modelElementsByFigEdge.put((FigEdge) f,modelElement);
				}else {
					f.setOwner(modelElement);
				}
			}else {
				LOG.debug("Ignoring href on " + f.getClass().getName() + " as it\'s already set");
			}
		}
	}
	private Map<String,String>interpretStyle(StringTokenizer st) {
		Map<String,String>map = new HashMap<String,String>();
		String name;
		String value;
		while (st.hasMoreElements()) {
			String namevaluepair = st.nextToken();
			int equalsPos = namevaluepair.indexOf('=');
			if (equalsPos < 0) {
				name = namevaluepair;
				value = "true";
			}else {
				name = namevaluepair.substring(0,equalsPos);
				value = namevaluepair.substring(equalsPos + 1);
			}
			map.put(name,value);
		}
		return map;
	}
	private void setStyleAttributes(Fig fig,Map<String,String>attributeMap) {
		for (Map.Entry<String,String>entry:attributeMap.entrySet()) {
			final String name = entry.getKey();
			final String value = entry.getValue();
			if ("operationsVisible".equals(name)) {
				((OperationsCompartmentContainer) fig).setOperationsVisible(value.equalsIgnoreCase("true"));
			}else if ("attributesVisible".equals(name)) {
				((AttributesCompartmentContainer) fig).setAttributesVisible(value.equalsIgnoreCase("true"));
			}else if ("stereotypeVisible".equals(name)) {
				((StereotypeContainer) fig).setStereotypeVisible(value.equalsIgnoreCase("true"));
			}else if ("visibilityVisible".equals(name)) {
				((VisibilityContainer) fig).setVisibilityVisible(value.equalsIgnoreCase("true"));
			}else if ("pathVisible".equals(name)) {
				((PathContainer) fig).setPathVisible(value.equalsIgnoreCase("true"));
			}else if ("extensionPointVisible".equals(name)) {
				((ExtensionsCompartmentContainer) fig).setExtensionPointVisible(value.equalsIgnoreCase("true"));
			}
		}
	}
	public ArgoDiagram readArgoDiagram(InputStream is,boolean closeStream)throws SAXException {
		return(ArgoDiagram) readDiagram(is,closeStream);
	}
	@Override public Diagram readDiagram(InputStream is,boolean closeStream)throws SAXException {
		Diagram d = super.readDiagram(is,closeStream);
		attachEdges(d);
		return d;
	}
	private void attachEdges(Diagram d) {
		for (EdgeData edgeData:figEdges) {
			final FigEdge edge = edgeData.getFigEdge();
			Object modelElement = modelElementsByFigEdge.get(edge);
			if (modelElement != null) {
				if (edge.getOwner() == null) {
					edge.setOwner(modelElement);
				}
			}
		}
		for (EdgeData edgeData:figEdges) {
			final FigEdge edge = edgeData.getFigEdge();
			Fig sourcePortFig = findFig(edgeData.getSourcePortFigId());
			Fig destPortFig = findFig(edgeData.getDestPortFigId());
			final FigNode sourceFigNode = getFigNode(edgeData.getSourceFigNodeId());
			final FigNode destFigNode = getFigNode(edgeData.getDestFigNodeId());
			if (sourceFigNode instanceof FigEdgePort) {
				sourcePortFig = sourceFigNode;
			}
			if (destFigNode instanceof FigEdgePort) {
				destPortFig = destFigNode;
			}
			if (sourcePortFig == null&&sourceFigNode != null) {
				sourcePortFig = getPortFig(sourceFigNode);
			}
			if (destPortFig == null&&destFigNode != null) {
				destPortFig = getPortFig(destFigNode);
			}
			if (sourcePortFig == null||destPortFig == null||sourceFigNode == null||destFigNode == null) {
				LOG.error("Can\'t find nodes for FigEdge: " + edge.getId() + ":" + edge.toString());
				edge.removeFromDiagram();
			}else {
				edge.setSourcePortFig(sourcePortFig);
				edge.setDestPortFig(destPortFig);
				edge.setSourceFigNode(sourceFigNode);
				edge.setDestFigNode(destFigNode);
			}
		}
		for (Object edge:d.getLayer().getContentsEdgesOnly()) {
			FigEdge figEdge = (FigEdge) edge;
			figEdge.computeRouteImpl();
		}
	}
	public void addFigEdge(final FigEdge figEdge,final String sourcePortFigId,final String destPortFigId,final String sourceFigNodeId,final String destFigNodeId) {
		figEdges.add(new EdgeData(figEdge,sourcePortFigId,destPortFigId,sourceFigNodeId,destFigNodeId));
	}
	private FigNode getFigNode(String figId)throws IllegalStateException {
		if (figId.contains(".")) {
			figId = figId.substring(0,figId.indexOf('.'));
			FigEdgeModelElement edge = (FigEdgeModelElement) findFig(figId);
			if (edge == null) {
				throw new IllegalStateException("Can\'t find a FigNode with id " + figId);
			}
			edge.makeEdgePort();
			return edge.getEdgePort();
		}else {
			Fig f = findFig(figId);
			if (f instanceof FigNode) {
				return(FigNode) f;
			}else {
				LOG.error("FigID " + figId + " is not a node, edge ignored");
				return null;
			}
		}
	}
	private Fig getPortFig(FigNode figNode) {
		if (figNode instanceof FigEdgePort) {
			return figNode;
		}else {
			return(Fig) figNode.getPortFigs().get(0);
		}
	}
	private class EdgeData {
	private final FigEdge figEdge;
	private final String sourcePortFigId;
	private final String destPortFigId;
	private final String sourceFigNodeId;
	private final String destFigNodeId;
	public EdgeData(FigEdge edge,String sourcePortId,String destPortId,String sourceNodeId,String destNodeId) {
		if (sourcePortId == null||destPortId == null) {
			throw new IllegalArgumentException("source port and dest port must not be null" + " source = " + sourcePortId + " dest = " + destPortId + " figEdge = " + edge);
		}
		this.figEdge = edge;
		this.sourcePortFigId = sourcePortId;
		this.destPortFigId = destPortId;
		this.sourceFigNodeId = sourceNodeId != null?sourceNodeId:sourcePortId;
		this.destFigNodeId = destNodeId != null?destNodeId:destPortId;
	}
	public String getDestFigNodeId() {
		return destFigNodeId;
	}
	public String getDestPortFigId() {
		return destPortFigId;
	}
	public FigEdge getFigEdge() {
		return figEdge;
	}
	public String getSourceFigNodeId() {
		return sourceFigNodeId;
	}
	public String getSourcePortFigId() {
		return sourcePortFigId;
	}
}
	@Override protected Fig constructFig(String className,String href,Rectangle bounds)throws SAXException {
		Fig f = null;
		try {
			Class figClass = Class.forName(className);
			for (Constructor constructor:figClass.getConstructors()) {
				Class[]parameterTypes = constructor.getParameterTypes();
				if (parameterTypes. == 3&&parameterTypes[0].equals(Object.class)&&parameterTypes[1].equals(Rectangle.class)&&parameterTypes[2].equals(DiagramSettings.class)) {
					Object parameters[] = new Object[3];
					Object owner = null;
					if (href != null) {
						owner = findOwner(href);
					}
					parameters[0] = owner;
					parameters[1] = bounds;
					parameters[2] = ((ArgoDiagram) getDiagram()).getDiagramSettings();
					f = (Fig) constructor.newInstance(parameters);
				}
				if (parameterTypes. == 2&&parameterTypes[0].equals(Object.class)&&parameterTypes[1].equals(DiagramSettings.class)) {
					Object parameters[] = new Object[2];
					Object owner = null;
					if (href != null) {
						owner = findOwner(href);
					}
					parameters[0] = owner;
					parameters[1] = ((ArgoDiagram) getDiagram()).getDiagramSettings();
					f = (Fig) constructor.newInstance(parameters);
				}
			}
		}catch (ClassNotFoundException e) {
			throw new SAXException(e);
		}catch (IllegalAccessException e) {
			throw new SAXException(e);
		}catch (InstantiationException e) {
			throw new SAXException(e);
		}catch (InvocationTargetException e) {
			throw new SAXException(e);
		}
		if (f == null) {
			LOG.debug("No ArgoUML constructor found for " + className + " falling back to GEF\'s default constructors");
			f = super.constructFig(className,href,bounds);
		}
		return f;
	}
	@Override public void setDiagram(Diagram diagram) {
		((ArgoDiagram) diagram).setDiagramSettings(getDiagramSettings());
		super.setDiagram(diagram);
	}
	public DiagramSettings getDiagramSettings() {
		return diagramSettings;
	}
}



