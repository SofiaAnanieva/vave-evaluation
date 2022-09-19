package org.argouml.uml.diagram;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ClassDiagram;
import org.argouml.model.DeploymentDiagram;
import org.argouml.model.DiDiagram;
import org.argouml.model.Model;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.graph.GraphNodeRenderer;
import org.argouml.uml.diagram.GraphChangeAdapter;
import org.argouml.uml.diagram.DiagramFactoryInterface;
import org.argouml.uml.diagram.DiagramFactoryInterface2;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.argouml.uml.diagram.ArgoDiagram;


public final class DiagramFactory {
	private final Map noStyleProperties = new HashMap();
	private static Map<DiagramType,Class>diagramClasses = new EnumMap<DiagramType,Class>(DiagramType.class);
	private static DiagramFactory diagramFactory = new DiagramFactory();
	public enum DiagramType {
	Class}
	private Map<DiagramType,Object>factories = new EnumMap<DiagramType,Object>(DiagramType.class);
	private DiagramFactory() {
		super();
		diagramClasses.put(DiagramType.Class,UMLClassDiagram.class);
	}
	public static DiagramFactory getInstance() {
		return diagramFactory;
	}
	public ArgoDiagram createDefaultDiagram(Object namespace) {
		return null;
	}
	@Deprecated public ArgoDiagram createDiagram(final DiagramType type,final Object namespace,final Object machine) {
		DiagramSettings settings = ProjectManager.getManager().getCurrentProject().getProjectSettings().getDefaultDiagramSettings();
		return createInternal(type,namespace,machine,settings);
	}
	public ArgoDiagram create(final DiagramType type,final Object owner,final DiagramSettings settings) {
		return createInternal(type,owner,null,settings);
	}
	private ArgoDiagram createInternal(final DiagramType type,final Object namespace,final Object machine,DiagramSettings settings) {
		final ArgoDiagram diagram;
		if (settings == null) {
			throw new IllegalArgumentException("DiagramSettings may not be null");
		}
		Object factory = factories.get(type);
		if (factory != null) {
			Object owner;
			if (machine != null) {
				owner = machine;
			}else {
				owner = namespace;
			}
			if (factory instanceof DiagramFactoryInterface2) {
				diagram = null;
			}else if (factory instanceof DiagramFactoryInterface) {
				diagram = null;
				diagram.setDiagramSettings(settings);
			}else {
				throw new IllegalStateException("Unknown factory type registered");
			}
		}else {
			diagram.setDiagramSettings(settings);
		}
		return diagram;
	}
	@Deprecated public ArgoDiagram createDiagram(Class type,Object namespace,Object machine) {
		ArgoDiagram diagram = null;
		Class diType = null;
		if (type == UMLClassDiagram.class) {
			diagram = new UMLClassDiagram(namespace);
			diType = ClassDiagram.class;
		}
		if (diagram == null) {
			throw new IllegalArgumentException("Unknown diagram type");
		}
		if (Model.getDiagramInterchangeModel() != null) {
			diagram.getGraphModel().addGraphEventListener(GraphChangeAdapter.getInstance());
			DiDiagram dd = null;
			((UMLMutableGraphSupport) diagram.getGraphModel()).setDiDiagram(dd);
		}
		return diagram;
	}
	public ArgoDiagram removeDiagram(ArgoDiagram diagram) {
		DiDiagram dd = ((UMLMutableGraphSupport) diagram.getGraphModel()).getDiDiagram();
		if (dd != null) {
			GraphChangeAdapter.getInstance().removeDiagram(dd);
		}
		return diagram;
	}
	@Deprecated public Object createRenderingElement(Object diagram,Object model) {
		GraphNodeRenderer rend = ((Diagram) diagram).getLayer().getGraphNodeRenderer();
		Object renderingElement = rend.getFigNodeFor(model,0,0,noStyleProperties);
		return renderingElement;
	}
	@Deprecated public void registerDiagramFactory(final DiagramType type,final DiagramFactoryInterface factory) {
		factories.put(type,factory);
	}
	public void registerDiagramFactory(final DiagramType type,final DiagramFactoryInterface2 factory) {
		factories.put(type,factory);
	}
}



