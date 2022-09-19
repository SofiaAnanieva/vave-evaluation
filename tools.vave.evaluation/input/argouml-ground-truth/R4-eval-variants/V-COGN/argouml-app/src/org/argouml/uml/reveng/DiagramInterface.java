package org.argouml.uml.reveng;

import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.List;
import org.argouml.kernel.Project;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramFactory;
import org.argouml.uml.diagram.static_structure.ClassDiagramGraphModel;
import org.argouml.uml.diagram.static_structure.ui.FigClass;
import org.argouml.uml.diagram.static_structure.ui.FigClassifierBox;
import org.argouml.uml.diagram.static_structure.ui.FigInterface;
import org.argouml.uml.diagram.static_structure.ui.FigPackage;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.presentation.Fig;


public class DiagramInterface {
	private static final char DIAGRAM_NAME_SEPARATOR = '_';
	private static final String DIAGRAM_NAME_SUFFIX = "classes";
	private Editor currentEditor;
	private List<ArgoDiagram>modifiedDiagrams = new ArrayList<ArgoDiagram>();
	private ClassDiagramGraphModel currentGM;
	private LayerPerspective currentLayer;
	private ArgoDiagram currentDiagram;
	private Project currentProject;
	public DiagramInterface(Editor editor) {
		currentEditor = editor;
		LayerPerspective layer = (LayerPerspective) editor.getLayerManager().getActiveLayer();
		currentProject = ((ArgoDiagram) layer.getDiagram()).getProject();
	}
	public DiagramInterface(Editor editor,Project project) {
		currentEditor = editor;
	}
	Editor getEditor() {
		return currentEditor;
	}
	void markDiagramAsModified(ArgoDiagram diagram) {
		if (!modifiedDiagrams.contains(diagram)) {
			modifiedDiagrams.add(diagram);
		}
	}
	public List<ArgoDiagram>getModifiedDiagramList() {
		return modifiedDiagrams;
	}
	void resetModifiedDiagrams() {
		modifiedDiagrams = new ArrayList<ArgoDiagram>();
	}
	public void addPackage(Object newPackage) {
		if (!isInDiagram(newPackage)) {
			if (currentGM.canAddNode(newPackage)) {
				FigPackage newPackageFig = new FigPackage(newPackage,new Rectangle(0,0,0,0),currentDiagram.getDiagramSettings());
				currentLayer.add(newPackageFig);
				currentGM.addNode(newPackage);
				currentLayer.putInPosition(newPackageFig);
			}
		}
	}
	public boolean isInDiagram(Object p) {
		if (currentDiagram == null) {
			return false;
		}else {
			return currentDiagram.getNodes().contains(p);
		}
	}
	public boolean isDiagramInProject(String name) {
		if (currentProject == null) {
			throw new RuntimeException("current project not set yet");
		}
		return currentProject.getDiagram(getDiagramName(name)) != null;
	}
	private String getDiagramName(String packageName) {
		return packageName.replace('.',DIAGRAM_NAME_SEPARATOR) + DIAGRAM_NAME_SEPARATOR + DIAGRAM_NAME_SUFFIX;
	}
	public void selectClassDiagram(Object p,String name) {
		if (currentProject == null) {
			throw new RuntimeException("current project not set yet");
		}
		ArgoDiagram m = currentProject.getDiagram(getDiagramName(name));
		if (m != null) {
			setCurrentDiagram(m);
		}else {
			addClassDiagram(p,name);
		}
	}
	public void addClassDiagram(Object ns,String name) {
		if (currentProject == null) {
			throw new RuntimeException("current project not set yet");
		}
		ArgoDiagram d = DiagramFactory.getInstance().createDiagram(DiagramFactory.DiagramType.Class,ns == null?currentProject.getRoot():ns,null);
		try {
			d.setName(getDiagramName(name));
		}catch (PropertyVetoException pve) {
		}
		currentProject.addMember(d);
		setCurrentDiagram(d);
	}
	public void addClass(Object newClass,boolean minimise) {
		addClassifier(newClass,minimise);
	}
	private void addClassifier(Object classifier,boolean minimise) {
		if (currentGM.canAddNode(classifier)) {
			FigClassifierBox newFig;
			if (Model.getFacade().isAClass(classifier)) {
				newFig = new FigClass(classifier,new Rectangle(0,0,0,0),currentDiagram.getDiagramSettings());
			}else if (Model.getFacade().isAInterface(classifier)) {
				newFig = new FigInterface(classifier,new Rectangle(0,0,0,0),currentDiagram.getDiagramSettings());
			}else {
				return;
			}
			currentLayer.add(newFig);
			currentGM.addNode(classifier);
			currentLayer.putInPosition(newFig);
			newFig.setOperationsVisible(!minimise);
			if (Model.getFacade().isAClass(classifier)) {
				((FigClass) newFig).setAttributesVisible(!minimise);
			}
			newFig.renderingChanged();
		}else {
			FigClassifierBox existingFig = null;
			List figs = currentLayer.getContentsNoEdges();
			for (int i = 0;i < figs.size();i++) {
				Fig fig = (Fig) figs.get(i);
				if (classifier == fig.getOwner()) {
					existingFig = (FigClassifierBox) fig;
				}
			}
			existingFig.renderingChanged();
		}
		currentGM.addNodeRelatedEdges(classifier);
	}
	public void addInterface(Object newInterface,boolean minimise) {
		addClassifier(newInterface,minimise);
	}
	public void createRootClassDiagram() {
		selectClassDiagram(null,"");
	}
	public void setCurrentDiagram(ArgoDiagram diagram) {
		if (diagram == null) {
			throw new RuntimeException("you can\'t select a null diagram");
		}
		currentGM = (ClassDiagramGraphModel) diagram.getGraphModel();
		currentLayer = diagram.getLayer();
		currentDiagram = diagram;
		currentProject = diagram.getProject();
		markDiagramAsModified(diagram);
	}
}



