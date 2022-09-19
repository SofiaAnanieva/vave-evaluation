package org.argouml.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.application.helpers.ApplicationVersion;
import org.argouml.configuration.Configuration;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectMember;
import org.argouml.model.Facade;
import org.argouml.model.Model;
import org.argouml.model.UmlException;
import org.argouml.model.XmiException;
import org.argouml.model.XmiReader;
import org.argouml.model.XmiWriter;
import org.argouml.uml.ProjectMemberModel;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramFactory;
import org.argouml.uml.diagram.DiagramFactory.DiagramType;
import org.xml.sax.InputSource;
import org.argouml.persistence.UmlFilePersister;


class ModelMemberFilePersister extends MemberFilePersister implements XmiExtensionParser {
	private static final Logger LOG = Logger.getLogger(ModelMemberFilePersister.class);
	public void load(Project project,URL url)throws OpenException {
		load(project,new InputSource(url.toExternalForm()));
	}
	public void load(Project project,InputStream inputStream)throws OpenException {
		load(project,new InputSource(inputStream));
	}
	private void load(Project project,InputSource source)throws OpenException {
		Object mmodel = null;
		try {
			source.setEncoding(Argo.getEncoding());
			readModels(source);
			mmodel = getCurModel();
		}catch (OpenException e) {
			LOG.error("UmlException caught",e);
			throw e;
		}
		Model.getUmlHelper().addListenersToModel(mmodel);
		project.addMember(mmodel);
		project.setUUIDRefs(new HashMap<String,Object>(getUUIDRefs()));
	}
	public String getMainTag() {
		try {
			return Model.getXmiReader().getTagName();
		}catch (UmlException e) {
			throw new RuntimeException(e);
		}
	}
	public void save(ProjectMember member,OutputStream outStream)throws SaveException {
		ProjectMemberModel pmm = (ProjectMemberModel) member;
		Object model = pmm.getModel();
		try {
			XmiWriter xmiWriter = Model.getXmiWriter(model,outStream,ApplicationVersion.getVersion() + "(" + UmlFilePersister.PERSISTENCE_VERSION + ")");
			xmiWriter.write();
			outStream.flush();
		}catch (UmlException e) {
			throw new SaveException(e);
		}catch (IOException e) {
			throw new SaveException(e);
		}
	}
	public void parse(String label,String xmiExtensionString) {
		LOG.info("Parsing an extension for " + label);
	}
	private Object curModel;
	private HashMap<String,Object>uUIDRefs;
	private Collection elementsRead;
	public Object getCurModel() {
		return curModel;
	}
	public HashMap<String,Object>getUUIDRefs() {
		return uUIDRefs;
	}
	public synchronized void readModels(URL url,XmiExtensionParser xmiExtensionParser)throws OpenException {
		LOG.info("=======================================");
		LOG.info("== READING MODEL " + url);
		try {
			InputSource source = new InputSource(new XmiInputStream(url.openStream(),xmiExtensionParser,100000,null));
			source.setSystemId(url.toString());
			readModels(source);
		}catch (IOException ex) {
			throw new OpenException(ex);
		}
	}
	public synchronized void readModels(InputSource source)throws OpenException {
		XmiReader reader = null;
		try {
			reader = Model.getXmiReader();
			if (Configuration.getBoolean(Argo.KEY_XMI_STRIP_DIAGRAMS,false)) {
				reader.setIgnoredElements(new String[] {"UML:Diagram"});
			}else {
				reader.setIgnoredElements(null);
			}
			List<String>searchPath = reader.getSearchPath();
			String pathList = System.getProperty("org.argouml.model.modules_search_path");
			if (pathList != null) {
				String[]paths = pathList.split(",");
				for (String path:paths) {
					if (!searchPath.contains(path)) {
						reader.addSearchPath(path);
					}
				}
			}
			reader.addSearchPath(source.getSystemId());
			curModel = null;
			elementsRead = reader.parse(source,false);
			if (elementsRead != null&&!elementsRead.isEmpty()) {
				Facade facade = Model.getFacade();
				Object current;
				Iterator elements = elementsRead.iterator();
				while (elements.hasNext()) {
					current = elements.next();
					if (facade.isAModel(current)) {
						LOG.info("Loaded model \'" + facade.getName(current) + "\'");
						if (curModel == null) {
							curModel = current;
						}
					}
				}
			}
			uUIDRefs = new HashMap<String,Object>(reader.getXMIUUIDToObjectMap());
		}catch (XmiException ex) {
			throw new XmiFormatException(ex);
		}catch (UmlException ex) {
			throw new XmiFormatException(ex);
		}
		LOG.info("=======================================");
	}
	public void registerDiagrams(Project project) {
		registerDiagramsInternal(project,elementsRead,true);
	}
	private void registerDiagramsInternal(Project project,Collection elements,boolean atLeastOne) {
		Facade facade = Model.getFacade();
		Collection diagramsElement = new ArrayList();
		Iterator it = elements.iterator();
		while (it.hasNext()) {
			Object element = it.next();
			if (facade.isAModel(element)) {
				diagramsElement.addAll(Model.getModelManagementHelper().getAllModelElementsOfKind(element,Model.getMetaTypes().getStateMachine()));
			}else if (facade.isAStateMachine(element)) {
				diagramsElement.add(element);
			}
		}
		DiagramFactory diagramFactory = DiagramFactory.getInstance();
		it = diagramsElement.iterator();
		while (it.hasNext()) {
			Object statemachine = it.next();
			Object namespace = facade.getNamespace(statemachine);
			if (namespace == null) {
				namespace = facade.getContext(statemachine);
				Model.getCoreHelper().setNamespace(statemachine,namespace);
			}
			ArgoDiagram diagram = null;
			if (facade.isAActivityGraph(statemachine)) {
				LOG.info("Creating activity diagram for " + facade.getUMLClassName(statemachine) + "<<" + facade.getName(statemachine) + ">>");
				diagram = diagramFactory.createDiagram(DiagramType.Activity,namespace,statemachine);
			}else {
			}
			if (diagram != null) {
				project.addMember(diagram);
			}
		}
		if (atLeastOne&&project.getDiagramCount() < 1) {
			ArgoDiagram d = diagramFactory.createDiagram(DiagramType.Class,curModel,null);
			project.addMember(d);
		}
		if (project.getDiagramCount() >= 1&&project.getActiveDiagram() == null) {
		}
	}
	public Collection getElementsRead() {
		return elementsRead;
	}
	public void setElementsRead(Collection elements) {
		this.elementsRead = elements;
	}
}



