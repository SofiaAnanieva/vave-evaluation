package org.argouml.model.mdr;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jmi.model.ModelPackage;
import javax.jmi.model.MofPackage;
import javax.jmi.reflect.InvalidObjectException;
import javax.jmi.reflect.RefPackage;
import javax.jmi.xmi.MalformedXMIException;
import org.apache.log4j.Logger;
import org.argouml.model.ActivityGraphsFactory;
import org.argouml.model.ActivityGraphsHelper;
import org.argouml.model.AggregationKind;
import org.argouml.model.ChangeableKind;
import org.argouml.model.CollaborationsFactory;
import org.argouml.model.CollaborationsHelper;
import org.argouml.model.CommonBehaviorFactory;
import org.argouml.model.CommonBehaviorHelper;
import org.argouml.model.ConcurrencyKind;
import org.argouml.model.CoreFactory;
import org.argouml.model.CoreHelper;
import org.argouml.model.DataTypesFactory;
import org.argouml.model.DataTypesHelper;
import org.argouml.model.DiagramInterchangeModel;
import org.argouml.model.DirectionKind;
import org.argouml.model.ExtensionMechanismsFactory;
import org.argouml.model.ExtensionMechanismsHelper;
import org.argouml.model.Facade;
import org.argouml.model.MetaTypes;
import org.argouml.model.ModelEventPump;
import org.argouml.model.ModelImplementation;
import org.argouml.model.ModelManagementFactory;
import org.argouml.model.ModelManagementHelper;
import org.argouml.model.OrderingKind;
import org.argouml.model.PseudostateKind;
import org.argouml.model.ScopeKind;
import org.argouml.model.StateMachinesFactory;
import org.argouml.model.StateMachinesHelper;
import org.argouml.model.UUIDManager;
import org.argouml.model.UmlException;
import org.argouml.model.UmlFactory;
import org.argouml.model.UmlHelper;
import org.argouml.model.UseCasesFactory;
import org.argouml.model.UseCasesHelper;
import org.argouml.model.VisibilityKind;
import org.argouml.model.XmiReader;
import org.argouml.model.XmiWriter;
import org.netbeans.api.mdr.CreationFailedException;
import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRepository;
import org.netbeans.api.xmi.XMIReader;
import org.netbeans.api.xmi.XMIReaderFactory;
import org.omg.uml.UmlPackage;
import org.argouml.model.mdr.UndoCoreHelperDecorator;


public class MDRModelImplementation implements ModelImplementation {
	private static final Logger LOG = Logger.getLogger(MDRModelImplementation.class);
	private Facade theFacade;
	private ModelEventPumpMDRImpl theModelEventPump;
	private CopyHelper theCopyHelper;
	private ActivityGraphsHelper theActivityGraphsHelper;
	private CoreHelper theCoreHelper;
	private MetaTypes theMetaTypes = new MetaTypesMDRImpl();
	private ModelManagementFactory theModelManagementFactory;
	private ModelManagementHelper theModelManagementHelper;
	private StateMachinesHelper theStateMachinesHelper;
	private UmlFactory theUmlFactory;
	private UmlHelper theUmlHelper;
	private UseCasesFactory theUseCasesFactory;
	private UseCasesHelper theUseCasesHelper;
	private ActivityGraphsFactory theActivityGraphsFactory;
	private CollaborationsFactory theCollaborationsFactory;
	private CollaborationsHelper theCollaborationsHelper;
	private CommonBehaviorFactory theCommonBehaviorFactory;
	private CommonBehaviorHelper theCommonBehaviorHelper;
	private DataTypesFactory theDataTypesFactory;
	private DataTypesHelper theDataTypesHelper;
	private ExtensionMechanismsFactory theExtensionMechanismsFactory;
	private ExtensionMechanismsHelper theExtensionMechanismsHelper;
	private StateMachinesFactory theStateMachinesFactory;
	private CoreFactory theCoreFactory;
	private KindsMDRImpl theKindsObject;
	private MDRepository repository;
	private UmlPackage umlPackage;
	private MofPackage mofPackage;
	private ModelPackage mofExtent;
	private Map<String,XmiReference>objectToId = Collections.synchronizedMap(new HashMap<String,XmiReference>());
	private Map<String,String>public2SystemIds = Collections.synchronizedMap(new HashMap<String,String>());
	private List<String>searchDirs = new ArrayList<String>();
	private Map<UmlPackage,Boolean>extents = new HashMap<UmlPackage,Boolean>(10,(float) 0.5);
	public UmlPackage getUmlPackage() {
		synchronized (extents) {
			if (umlPackage == null) {
				LOG.debug("umlPackage is null - no current extent");
			}
			return umlPackage;
		}
	}
	RefPackage createExtent(String name,boolean readOnly) {
		try {
			synchronized (extents) {
				UmlPackage extent = (UmlPackage) getRepository().createExtent(name,getMofPackage());
				extents.put(extent,Boolean.valueOf(readOnly));
				if (!readOnly) {
					if (umlPackage != null) {
						try {
							deleteExtentUnchecked(umlPackage);
						}catch (InvalidObjectException e) {
							LOG.debug("User model extent already deleted");
						}
					}
					umlPackage = extent;
				}
				if (LOG.isDebugEnabled()) {
					LOG.debug("Created new " + (readOnly?"readonly ":"") + "extent " + umlPackage);
					LOG.debug("All registered extents = " + Arrays.toString(repository.getExtentNames()));
				}
				return extent;
			}
		}catch (CreationFailedException e) {
			LOG.error("Extent creation failed for " + name);
			return null;
		}
	}
	void deleteExtent(UmlPackage extent) {
		synchronized (extents) {
			if (umlPackage.equals(extent)) {
				createDefaultExtent();
			}else {
				deleteExtentUnchecked(extent);
			}
		}
	}
	private void deleteExtentUnchecked(UmlPackage extent) {
		synchronized (extents) {
			extents.remove(extent);
			extent.refDelete();
		}
	}
	Collection<UmlPackage>getExtents() {
		return Collections.unmodifiableSet(extents.keySet());
	}
	boolean isReadOnly(Object extent) {
		synchronized (extents) {
			Boolean result = extents.get(extent);
			if (result == null) {
				LOG.warn("Unable to find extent " + extent);
				return false;
			}
			return result.booleanValue();
		}
	}
	public MofPackage getMofPackage() {
		return mofPackage;
	}
	ModelPackage getModelPackage() {
		return mofExtent;
	}
	MDRepository getRepository() {
		return repository;
	}
	static final String MOF_EXTENT_NAME = "MOF Extent";
	static final String MODEL_EXTENT_NAME = "model extent";
	static final String METAMODEL_URL = "mof/01-02-15_Diff.xml";
	public MDRModelImplementation(MDRepository r)throws UmlException {
		repository = r;
		initializeM2();
	}
	public MDRModelImplementation()throws UmlException {
		this(getDefaultRepository());
		createDefaultExtent();
		if (umlPackage == null) {
			throw new UmlException("Could not create UML extent");
		}
		LOG.debug("MDR Init - created UML extent");
		initializeFactories(umlPackage);
	}
	private static MDRepository getDefaultRepository() {
		LOG.debug("Starting MDR system initialization");
		String storageImplementation = System.getProperty("org.netbeans.mdr.storagemodel.StorageFactoryClassName","org.netbeans.mdr.persistence.memoryimpl.StorageFactoryImpl");
		System.setProperty("org.netbeans.mdr.storagemodel.StorageFactoryClassName",storageImplementation);
		System.setProperty("MDRStorageProperty.org.netbeans.mdr.persistence.memoryimpl.id",UUIDManager.getInstance().getNewUUID());
		MDRepository defaultRepository = MDRManager.getDefault().getDefaultRepository();
		LOG.debug("MDR Init - got default repository");
		return defaultRepository;
	}
	private void initializeM2()throws UmlException {
		mofExtent = (ModelPackage) repository.getExtent(MOF_EXTENT_NAME);
		LOG.debug("MDR Init - tried to get MOF extent");
		if (mofExtent == null) {
			try {
				mofExtent = (ModelPackage) repository.createExtent(MOF_EXTENT_NAME);
			}catch (CreationFailedException e) {
				throw new UmlException(e);
			}
			LOG.debug("MDR Init - created MOF extent");
			XMIReader reader = XMIReaderFactory.getDefault().createXMIReader();
			LOG.debug("MDR Init - created XMI reader");
			String metafacade = System.getProperty("argouml.model.mdr.facade",METAMODEL_URL);
			URL resource = getClass().getResource(metafacade);
			try {
				reader.read(resource.toString(),mofExtent);
			}catch (IOException e) {
				throw new UmlException(e);
			}catch (MalformedXMIException e) {
				throw new UmlException(e);
			}
			LOG.debug("MDR Init - read UML metamodel");
		}
		mofPackage = null;
		for (MofPackage pkg:(Collection<MofPackage>) mofExtent.getMofPackage().refAllOfClass()) {
			if ("UML".equals(pkg.getName())) {
				mofPackage = pkg;
				break;
			}
		}
	}
	public void initializeFactories(UmlPackage up) {
		umlPackage = up;
		theModelEventPump = new ModelEventPumpMDRImpl(this,repository);
		theModelEventPump.startPumpingEvents();
		LOG.debug("MDR Init - event pump started");
		theDataTypesFactory = new DataTypesFactoryMDRImpl(this);
		theDataTypesHelper = new DataTypesHelperMDRImpl(this);
		theKindsObject = new KindsMDRImpl(this);
		theModelManagementFactory = new ModelManagementFactoryMDRImpl(this);
		theExtensionMechanismsHelper = new ExtensionMechanismsHelperMDRImpl(this);
		theExtensionMechanismsFactory = new ExtensionMechanismsFactoryMDRImpl(this);
		LOG.debug("MDR Init - initialized package Extension mechanism");
		theCopyHelper = new CopyHelper(this);
		theActivityGraphsHelper = new ActivityGraphsHelperMDRImpl();
		theCoreHelper = new UndoCoreHelperDecorator(new CoreHelperMDRImpl(this));
		LOG.debug("MDR Init - initialized package Core helper");
		theModelManagementHelper = new ModelManagementHelperMDRImpl(this);
		theStateMachinesHelper = new StateMachinesHelperMDRImpl(this);
		LOG.debug("MDR Init - initialized package StateMachines");
		theUseCasesFactory = new UseCasesFactoryMDRImpl(this);
		theUseCasesHelper = new UseCasesHelperMDRImpl(this);
		LOG.debug("MDR Init - initialized package Use Cases");
		theActivityGraphsFactory = new ActivityGraphsFactoryMDRImpl(this);
		LOG.debug("MDR Init - initialized package Collaborations");
		theCommonBehaviorFactory = new CommonBehaviorFactoryMDRImpl(this);
		theCommonBehaviorHelper = new CommonBehaviorHelperMDRImpl(this);
		LOG.debug("MDR Init - initialized package CommonBehavior");
		theStateMachinesFactory = new StateMachinesFactoryMDRImpl(this);
		theCoreFactory = new CoreFactoryMDRImpl(this);
		LOG.debug("MDR Init - all packages initialized");
	}
	RefPackage createDefaultExtent() {
		synchronized (extents) {
			umlPackage = (UmlPackage) repository.getExtent(MODEL_EXTENT_NAME);
			if (umlPackage != null) {
				try {
					UmlPackage oldPackage = umlPackage;
					umlPackage = null;
					deleteExtentUnchecked(oldPackage);
					LOG.debug("MDR Init - UML extent existed - " + "deleted it and all UML data");
				}catch (InvalidObjectException e) {
					LOG.debug("Got error deleting old default user extent");
				}
			}
			umlPackage = (UmlPackage) createExtent(MODEL_EXTENT_NAME,false);
			LOG.debug("Created default extent");
			return umlPackage;
		}
	}
	public void shutdown() {
		theModelEventPump.flushModelEvents();
		theModelEventPump.stopPumpingEvents();
		MDRManager.getDefault().shutdownAll();
	}
	public DiagramInterchangeModel getDiagramInterchangeModel() {
		return null;
	}
	public Facade getFacade() {
		if (theFacade == null) {
			theFacade = new FacadeMDRImpl(this);
		}
		return theFacade;
	}
	public ModelEventPump getModelEventPump() {
		return theModelEventPump;
	}
	public ActivityGraphsFactory getActivityGraphsFactory() {
		return theActivityGraphsFactory;
	}
	public ActivityGraphsHelper getActivityGraphsHelper() {
		return theActivityGraphsHelper;
	}
	public CollaborationsFactory getCollaborationsFactory() {
		if (theCollaborationsFactory == null) {
			theCollaborationsFactory = new CollaborationsFactoryMDRImpl(this);
		}
		return theCollaborationsFactory;
	}
	public CollaborationsHelper getCollaborationsHelper() {
		if (theCollaborationsHelper == null) {
			theCollaborationsHelper = new CollaborationsHelperMDRImpl(this);
		}
		return theCollaborationsHelper;
	}
	public CommonBehaviorFactory getCommonBehaviorFactory() {
		return theCommonBehaviorFactory;
	}
	public CommonBehaviorHelper getCommonBehaviorHelper() {
		return theCommonBehaviorHelper;
	}
	public org.argouml.model.CopyHelper getCopyHelper() {
		return theCopyHelper;
	}
	public CoreFactory getCoreFactory() {
		return theCoreFactory;
	}
	public CoreHelper getCoreHelper() {
		return theCoreHelper;
	}
	public DataTypesFactory getDataTypesFactory() {
		return theDataTypesFactory;
	}
	public DataTypesHelper getDataTypesHelper() {
		return theDataTypesHelper;
	}
	public ExtensionMechanismsFactory getExtensionMechanismsFactory() {
		return theExtensionMechanismsFactory;
	}
	public ExtensionMechanismsHelper getExtensionMechanismsHelper() {
		return theExtensionMechanismsHelper;
	}
	public ModelManagementFactory getModelManagementFactory() {
		return theModelManagementFactory;
	}
	public ModelManagementHelper getModelManagementHelper() {
		return theModelManagementHelper;
	}
	public StateMachinesFactory getStateMachinesFactory() {
		return theStateMachinesFactory;
	}
	public StateMachinesHelper getStateMachinesHelper() {
		return theStateMachinesHelper;
	}
	public UmlFactory getUmlFactory() {
		if (theUmlFactory == null) {
			theUmlFactory = new UmlFactoryMDRImpl(this);
		}
		return theUmlFactory;
	}
	public UmlHelper getUmlHelper() {
		if (theUmlHelper == null) {
			theUmlHelper = new UmlHelperMDRImpl(this);
		}
		return theUmlHelper;
	}
	public UseCasesFactory getUseCasesFactory() {
		return theUseCasesFactory;
	}
	public UseCasesHelper getUseCasesHelper() {
		return theUseCasesHelper;
	}
	public MetaTypes getMetaTypes() {
		return theMetaTypes;
	}
	public ChangeableKind getChangeableKind() {
		return theKindsObject;
	}
	public AggregationKind getAggregationKind() {
		return theKindsObject;
	}
	public PseudostateKind getPseudostateKind() {
		return theKindsObject;
	}
	public ScopeKind getScopeKind() {
		return theKindsObject;
	}
	public ConcurrencyKind getConcurrencyKind() {
		return theKindsObject;
	}
	public DirectionKind getDirectionKind() {
		return theKindsObject;
	}
	public OrderingKind getOrderingKind() {
		return theKindsObject;
	}
	public VisibilityKind getVisibilityKind() {
		return theKindsObject;
	}
	public XmiReader getXmiReader()throws UmlException {
		XmiReader reader = new XmiReaderImpl(this);
		return reader;
	}
	public XmiWriter getXmiWriter(Object model,OutputStream stream,String version)throws UmlException {
		return new XmiWriterMDRImpl(this,model,stream,version);
	}
	protected Map<String,XmiReference>getObjectToId() {
		return objectToId;
	}
	Map<String,String>getPublic2SystemIds() {
		return public2SystemIds;
	}
	void addSearchPath(String path) {
		searchDirs.add(path);
	}
	void removeSearchPath(String path) {
		searchDirs.remove(path);
	}
	List<String>getSearchPath() {
		return searchDirs;
	}
}



