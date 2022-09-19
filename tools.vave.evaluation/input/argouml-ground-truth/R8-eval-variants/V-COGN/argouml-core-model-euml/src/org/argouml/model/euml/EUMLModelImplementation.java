package org.argouml.model.euml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.argouml.model.DiagramInterchangeModel;
import org.argouml.model.ModelImplementation;
import org.argouml.model.UmlException;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.uml2.common.edit.domain.UML2AdapterFactoryEditingDomain;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.edit.providers.UMLItemProviderAdapterFactory;
import org.eclipse.uml2.uml.edit.providers.UMLReflectiveItemProviderAdapterFactory;
import org.eclipse.uml2.uml.edit.providers.UMLResourceItemProviderAdapterFactory;
import org.eclipse.uml2.uml.resource.UML22UMLExtendedMetaData;
import org.eclipse.uml2.uml.resource.UML22UMLResource;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resource.XMI2UMLExtendedMetaData;
import org.eclipse.uml2.uml.resource.XMI2UMLResource;
import org.argouml.model.euml.ModelEventPumpEUMLImpl;


public class EUMLModelImplementation implements ModelImplementation {
	private ActivityGraphsFactoryEUMLlImpl theActivityGraphsFactory;
	private ActivityGraphsHelperEUMLImpl theActivityGraphsHelper;
	private AggregationKindEUMLImpl theAggregationKind;
	@SuppressWarnings("deprecation")private ChangeableKindEUMLImpl theChangeableKind;
	private CollaborationsFactoryEUMLImpl theCollaborationsFactory;
	private CollaborationsHelperEUMLImpl theCollaborationsHelper;
	private CommonBehaviorFactoryEUMLImpl theCommonBehaviorFactory;
	private CommonBehaviorHelperEUMLImpl theCommonBehaviorHelper;
	private ConcurrencyKindEUMLImpl theConcurrencyKind;
	private CopyHelperEUMLImpl theCopyHelper;
	private CoreFactoryEUMLImpl theCoreFactory;
	private CoreHelperEUMLImpl theCoreHelper;
	private DataTypesFactoryEUMLImpl theDataTypesFactory;
	private DataTypesHelperEUMLImpl theDataTypesHelper;
	private DirectionKindEUMLImpl theDirectionKind;
	private ExtensionMechanismsFactoryEUMLImpl theExtensionMechanismsFactory;
	private ExtensionMechanismsHelperEUMLImpl theExtensionMechanismsHelper;
	private FacadeEUMLImpl theFacade;
	private MetaTypesEUMLImpl theMetaTypes;
	private ModelEventPumpEUMLImpl theModelEventPump;
	private ModelManagementFactoryEUMLImpl theModelManagementFactory;
	private ModelManagementHelperEUMLImpl theModelManagementHelper;
	private OrderingKindEUMLImpl theOrderingKind;
	private PseudostateKindEUMLImpl thePseudostateKind;
	@SuppressWarnings("deprecation")private ScopeKindEUMLImpl theScopeKind;
	private StateMachinesFactoryEUMLImpl theStateMachinesFactory;
	private StateMachinesHelperEUMLImpl theStateMachinesHelper;
	private UmlFactoryEUMLImpl theUmlFactory;
	private UmlHelperEUMLImpl theUmlHelper;
	private UseCasesFactoryEUMLImpl theUseCasesFactory;
	private UseCasesHelperEUMLImpl theUseCasesHelper;
	private VisibilityKindEUMLImpl theVisibilityKind;
	private CommandStackImpl theCommandStack;
	private AdapterFactoryEditingDomain editingDomain;
	private Map<Resource,Boolean>readOnlyMap = new HashMap<Resource,Boolean>();
	public EUMLModelImplementation() {
		initializeEditingDomain();
	}
	private void initializeEditingDomain() {
		String path = System.getProperty("eUML.resources");
		BasicCommandStack commandStack = new BasicCommandStack() {
	@Override protected void handleError(Exception exception) {
		super.handleError(exception);
		throw new RuntimeException(exception);
	}
};
		List<AdapterFactory>factories = new ArrayList<AdapterFactory>();
		factories.add(new UMLResourceItemProviderAdapterFactory());
		factories.add(new UMLItemProviderAdapterFactory());
		factories.add(new EcoreItemProviderAdapterFactory());
		factories.add(new UMLReflectiveItemProviderAdapterFactory());
		ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(factories);
		editingDomain = new UML2AdapterFactoryEditingDomain(adapterFactory,commandStack,readOnlyMap);
		ResourceSet resourceSet = editingDomain.getResourceSet();
		Map<String,Object>extensionToFactoryMap = resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap();
		Map<URI,URI>uriMap = resourceSet.getURIConverter().getURIMap();
		if (path != null) {
			try {
				FileInputStream in = new FileInputStream(path);
				in.close();
			}catch (IOException e) {
				throw(new RuntimeException(e));
			}
			path = path.replace('\\','/');
			if (Character.isLetter(path.charAt(0))) {
				path = '/' + path;
			}
			URI uri = URI.createURI("jar:file:" + path + "!/");
			resourceSet.getPackageRegistry().put(UMLPackage.eNS_URI,UMLPackage.eINSTANCE);
			resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI,EcorePackage.eINSTANCE);
			extensionToFactoryMap.put(UMLResource.FILE_EXTENSION,UMLResource.Factory.INSTANCE);
			uriMap.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP),uri.appendSegment("libraries").appendSegment(""));
			uriMap.put(URI.createURI(UMLResource.METAMODELS_PATHMAP),uri.appendSegment("metamodels").appendSegment(""));
			uriMap.put(URI.createURI(UMLResource.PROFILES_PATHMAP),uri.appendSegment("profiles").appendSegment(""));
		}
		extensionToFactoryMap.put(UML22UMLResource.FILE_EXTENSION,UML22UMLResource.Factory.INSTANCE);
		extensionToFactoryMap.put(XMI2UMLResource.FILE_EXTENSION,XMI2UMLResource.Factory.INSTANCE);
		uriMap.putAll(UML22UMLExtendedMetaData.getURIMap());
		uriMap.putAll(XMI2UMLExtendedMetaData.getURIMap());
	}
	public EditingDomain getEditingDomain() {
		return editingDomain;
	}
	public Map<Resource,Boolean>getReadOnlyMap() {
		return readOnlyMap;
	}
	public ActivityGraphsFactoryEUMLlImpl getActivityGraphsFactory() {
		if (theActivityGraphsFactory == null) {
			theActivityGraphsFactory = new ActivityGraphsFactoryEUMLlImpl(this);
		}
		return theActivityGraphsFactory;
	}
	public ActivityGraphsHelperEUMLImpl getActivityGraphsHelper() {
		if (theActivityGraphsHelper == null) {
			theActivityGraphsHelper = new ActivityGraphsHelperEUMLImpl(this);
		}
		return theActivityGraphsHelper;
	}
	public AggregationKindEUMLImpl getAggregationKind() {
		if (theAggregationKind == null) {
			theAggregationKind = new AggregationKindEUMLImpl();
		}
		return theAggregationKind;
	}
	@SuppressWarnings("deprecation")public ChangeableKindEUMLImpl getChangeableKind() {
		if (theChangeableKind == null) {
			theChangeableKind = new ChangeableKindEUMLImpl();
		}
		return theChangeableKind;
	}
	public CollaborationsFactoryEUMLImpl getCollaborationsFactory() {
		if (theCollaborationsFactory == null) {
			theCollaborationsFactory = new CollaborationsFactoryEUMLImpl(this);
		}
		return theCollaborationsFactory;
	}
	public CollaborationsHelperEUMLImpl getCollaborationsHelper() {
		if (theCollaborationsHelper == null) {
			theCollaborationsHelper = new CollaborationsHelperEUMLImpl(this);
		}
		return theCollaborationsHelper;
	}
	public CommonBehaviorFactoryEUMLImpl getCommonBehaviorFactory() {
		if (theCommonBehaviorFactory == null) {
			theCommonBehaviorFactory = new CommonBehaviorFactoryEUMLImpl(this);
		}
		return theCommonBehaviorFactory;
	}
	public CommonBehaviorHelperEUMLImpl getCommonBehaviorHelper() {
		if (theCommonBehaviorHelper == null) {
			theCommonBehaviorHelper = new CommonBehaviorHelperEUMLImpl(this);
		}
		return theCommonBehaviorHelper;
	}
	public ConcurrencyKindEUMLImpl getConcurrencyKind() {
		if (theConcurrencyKind == null) {
			theConcurrencyKind = new ConcurrencyKindEUMLImpl();
		}
		return theConcurrencyKind;
	}
	public CopyHelperEUMLImpl getCopyHelper() {
		if (theCopyHelper == null) {
			theCopyHelper = new CopyHelperEUMLImpl(this);
		}
		return theCopyHelper;
	}
	public CoreFactoryEUMLImpl getCoreFactory() {
		if (theCoreFactory == null) {
			theCoreFactory = new CoreFactoryEUMLImpl(this);
		}
		return theCoreFactory;
	}
	public CoreHelperEUMLImpl getCoreHelper() {
		if (theCoreHelper == null) {
			theCoreHelper = new CoreHelperEUMLImpl(this);
		}
		return theCoreHelper;
	}
	public DataTypesFactoryEUMLImpl getDataTypesFactory() {
		if (theDataTypesFactory == null) {
			theDataTypesFactory = new DataTypesFactoryEUMLImpl(this);
		}
		return theDataTypesFactory;
	}
	public DataTypesHelperEUMLImpl getDataTypesHelper() {
		if (theDataTypesHelper == null) {
			theDataTypesHelper = new DataTypesHelperEUMLImpl(this);
		}
		return theDataTypesHelper;
	}
	public DirectionKindEUMLImpl getDirectionKind() {
		if (theDirectionKind == null) {
			theDirectionKind = new DirectionKindEUMLImpl();
		}
		return theDirectionKind;
	}
	public ExtensionMechanismsFactoryEUMLImpl getExtensionMechanismsFactory() {
		if (theExtensionMechanismsFactory == null) {
			theExtensionMechanismsFactory = new ExtensionMechanismsFactoryEUMLImpl(this);
		}
		return theExtensionMechanismsFactory;
	}
	public ExtensionMechanismsHelperEUMLImpl getExtensionMechanismsHelper() {
		if (theExtensionMechanismsHelper == null) {
			theExtensionMechanismsHelper = new ExtensionMechanismsHelperEUMLImpl(this);
		}
		return theExtensionMechanismsHelper;
	}
	public FacadeEUMLImpl getFacade() {
		if (theFacade == null) {
			theFacade = new FacadeEUMLImpl(this);
		}
		return theFacade;
	}
	public MetaTypesEUMLImpl getMetaTypes() {
		if (theMetaTypes == null) {
			theMetaTypes = new MetaTypesEUMLImpl(this);
		}
		return theMetaTypes;
	}
	public ModelEventPumpEUMLImpl getModelEventPump() {
		if (theModelEventPump == null) {
			theModelEventPump = new ModelEventPumpEUMLImpl(this);
		}
		return theModelEventPump;
	}
	public ModelManagementFactoryEUMLImpl getModelManagementFactory() {
		if (theModelManagementFactory == null) {
			theModelManagementFactory = new ModelManagementFactoryEUMLImpl(this);
		}
		return theModelManagementFactory;
	}
	public ModelManagementHelperEUMLImpl getModelManagementHelper() {
		if (theModelManagementHelper == null) {
			theModelManagementHelper = new ModelManagementHelperEUMLImpl(this);
		}
		return theModelManagementHelper;
	}
	public OrderingKindEUMLImpl getOrderingKind() {
		if (theOrderingKind == null) {
			theOrderingKind = new OrderingKindEUMLImpl();
		}
		return theOrderingKind;
	}
	public PseudostateKindEUMLImpl getPseudostateKind() {
		if (thePseudostateKind == null) {
			thePseudostateKind = new PseudostateKindEUMLImpl();
		}
		return thePseudostateKind;
	}
	@SuppressWarnings("deprecation")public ScopeKindEUMLImpl getScopeKind() {
		if (theScopeKind == null) {
			theScopeKind = new ScopeKindEUMLImpl();
		}
		return theScopeKind;
	}
	public StateMachinesFactoryEUMLImpl getStateMachinesFactory() {
		if (theStateMachinesFactory == null) {
			theStateMachinesFactory = new StateMachinesFactoryEUMLImpl(this);
		}
		return theStateMachinesFactory;
	}
	public StateMachinesHelperEUMLImpl getStateMachinesHelper() {
		if (theStateMachinesHelper == null) {
			theStateMachinesHelper = new StateMachinesHelperEUMLImpl(this);
		}
		return theStateMachinesHelper;
	}
	public UmlFactoryEUMLImpl getUmlFactory() {
		if (theUmlFactory == null) {
			theUmlFactory = new UmlFactoryEUMLImpl(this);
		}
		return theUmlFactory;
	}
	public UmlHelperEUMLImpl getUmlHelper() {
		if (theUmlHelper == null) {
			theUmlHelper = new UmlHelperEUMLImpl(this);
		}
		return theUmlHelper;
	}
	public UseCasesFactoryEUMLImpl getUseCasesFactory() {
		if (theUseCasesFactory == null) {
			theUseCasesFactory = new UseCasesFactoryEUMLImpl(this);
		}
		return theUseCasesFactory;
	}
	public UseCasesHelperEUMLImpl getUseCasesHelper() {
		if (theUseCasesHelper == null) {
			theUseCasesHelper = new UseCasesHelperEUMLImpl(this);
		}
		return theUseCasesHelper;
	}
	public VisibilityKindEUMLImpl getVisibilityKind() {
		if (theVisibilityKind == null) {
			theVisibilityKind = new VisibilityKindEUMLImpl();
		}
		return theVisibilityKind;
	}
	public XmiReaderEUMLImpl getXmiReader()throws UmlException {
		return new XmiReaderEUMLImpl(this);
	}
	public XmiWriterEUMLImpl getXmiWriter(Object model,OutputStream stream,String version)throws UmlException {
		return new XmiWriterEUMLImpl(this,model,stream,version);
	}
	public DiagramInterchangeModel getDiagramInterchangeModel() {
		return null;
	}
	private CommandStackImpl getCommandStack() {
		if (theCommandStack == null) {
			theCommandStack = new CommandStackImpl(this);
		}
		return theCommandStack;
	}
	void clearEditingDomain() {
		for (Resource resource:editingDomain.getResourceSet().getResources()) {
			unloadResource(resource);
		}
	}
	void unloadResource(Resource resource) {
		resource.unload();
		readOnlyMap.remove(resource);
	}
}



