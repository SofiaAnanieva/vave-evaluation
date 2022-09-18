package tools.vave.eval.mm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diff.DefaultDiffEngine;
import org.eclipse.emf.compare.diff.DiffBuilder;
import org.eclipse.emf.compare.diff.FeatureFilter;
import org.eclipse.emf.compare.diff.IDiffEngine;
import org.eclipse.emf.compare.diff.IDiffProcessor;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.match.impl.MatchEngineFactoryImpl;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IBatchMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.utils.EqualityHelper;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.emftext.language.java.JavaClasspath;
import org.emftext.language.java.JavaClasspath.Initializer;
import org.emftext.language.java.JavaPackage;
import org.emftext.language.java.containers.CompilationUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.google.common.cache.CacheBuilder;

import edu.kit.ipd.sdq.commons.util.org.eclipse.emf.ecore.resource.ResourceSetUtil;
import tools.vave.java.HierarchicalMatchEngineFactory;
import tools.vave.java.SimilarityChecker;
import tools.vitruv.applications.umljava.JavaToUmlChangePropagationSpecification;
import tools.vitruv.domains.java.JavaDomainProvider;
import tools.vitruv.domains.uml.UmlDomainProvider;
import tools.vitruv.framework.change.description.TransactionalChange;
import tools.vitruv.framework.change.description.VitruviusChange;
import tools.vitruv.framework.change.description.impl.TransactionalChangeImpl;
import tools.vitruv.framework.change.echange.EChange;
import tools.vitruv.framework.change.echange.feature.reference.InsertEReference;
import tools.vitruv.framework.change.echange.feature.reference.ReplaceSingleValuedEReference;
import tools.vitruv.framework.change.interaction.FreeTextUserInteraction;
import tools.vitruv.framework.change.interaction.UserInteractionBase;
import tools.vitruv.framework.change.interaction.impl.InteractionFactoryImpl;
import tools.vitruv.framework.change.recording.ChangeRecorder;
import tools.vitruv.framework.domains.VitruvDomain;
import tools.vitruv.framework.propagation.ChangePropagationSpecification;
import tools.vitruv.framework.userinteraction.PredefinedInteractionResultProvider;
import tools.vitruv.framework.userinteraction.UserInteractionFactory;
import tools.vitruv.testutils.RegisterMetamodelsInStandalone;
import tools.vitruv.testutils.TestLogging;
import tools.vitruv.testutils.TestProjectManager;
import tools.vitruv.variability.vave.VirtualProductModel;
import tools.vitruv.variability.vave.VirtualProductModelInitializer;
import tools.vitruv.variability.vave.VirtualVaVeModel;
import tools.vitruv.variability.vave.consistency.AffectedConfigurations;
import tools.vitruv.variability.vave.consistency.ConsistencyRule;
import tools.vitruv.variability.vave.consistency.DependencyLifting;
import tools.vitruv.variability.vave.consistency.HintComputation;
import tools.vitruv.variability.vave.impl.VirtualVaVeModelImpl;
import tools.vitruv.variability.vave.model.expression.Conjunction;
import tools.vitruv.variability.vave.model.expression.Expression;
import tools.vitruv.variability.vave.model.expression.ExpressionFactory;
import tools.vitruv.variability.vave.model.expression.Variable;
import tools.vitruv.variability.vave.model.featuremodel.FeatureModel;
import tools.vitruv.variability.vave.model.featuremodel.FeaturemodelFactory;
import tools.vitruv.variability.vave.model.featuremodel.ViewCrossTreeConstraint;
import tools.vitruv.variability.vave.model.featuremodel.ViewFeature;
import tools.vitruv.variability.vave.model.featuremodel.ViewTreeConstraint;
import tools.vitruv.variability.vave.model.vave.Configuration;
import tools.vitruv.variability.vave.model.vave.Feature;
import tools.vitruv.variability.vave.model.vave.FeatureOption;
import tools.vitruv.variability.vave.model.vave.FeatureRevision;
import tools.vitruv.variability.vave.model.vave.SystemRevision;
import tools.vitruv.variability.vave.model.vave.VaveFactory;
import tools.vitruv.variability.vave.util.ExpressionUtil;
import tools.vitruv.variability.vave.util.FeatureModelUtil;

@ExtendWith({ TestProjectManager.class, TestLogging.class, RegisterMetamodelsInStandalone.class })
public class MMEvalTest {

	protected VirtualVaVeModel vave = null;

	protected int productNumber = 0;

	protected final Path projectFolder = Paths.get("C:\\FZI\\vave-project-folder");
	protected final Path vaveResourceLocation = Paths.get("C:\\FZI\\vave-resource-location\\temp");
	protected final Path vaveProjectMarker = vaveResourceLocation.resolve("test_project.marker_vitruv");

	@BeforeEach
	public void setUp() throws Exception {
		System.out.println("SET UP VAVE AND JAMOPP");

		// create vave instance
		Set<VitruvDomain> domains = new HashSet<>();
		domains.add(new JavaDomainProvider().getDomain());
		domains.add(new UmlDomainProvider().getDomain());

		Set<ChangePropagationSpecification> changePropagationSpecifications = new HashSet<>();
		JavaToUmlChangePropagationSpecification javaumlcps = new JavaToUmlChangePropagationSpecification();
		changePropagationSpecifications.add(javaumlcps);

		PredefinedInteractionResultProvider irp = UserInteractionFactory.instance.createPredefinedInteractionResultProvider(null);
		FreeTextUserInteraction ftui = new InteractionFactoryImpl().createFreeTextUserInteraction();
		ftui.setText("umloutput");
		for (int i = 0; i < 100; i++)
			irp.addUserInteractions(new UserInteractionBase[] { ftui, ftui });

		this.vave = new VirtualVaVeModelImpl(domains, changePropagationSpecifications, irp, projectFolder, new VirtualProductModelInitializer() {

			@Override
			public void initialize(VirtualProductModel vpm) {
				ResourceSet resourceSet = vpm.getResourceSet();
				resourceSet.getLoadOptions().put("DISABLE_LAYOUT_INFORMATION_RECORDING", Boolean.TRUE);
				resourceSet.getLoadOptions().put("DISABLE_LOCATION_MAP", Boolean.TRUE);
				resourceSet.getLoadOptions().put(JavaClasspath.OPTION_USE_LOCAL_CLASSPATH, Boolean.TRUE);
				resourceSet.getLoadOptions().put(JavaClasspath.OPTION_REGISTER_STD_LIB, Boolean.FALSE);

				// register jar files
				System.out.println("REGISTERING JAR FILES");
				JavaClasspath classPath = JavaClasspath.get(resourceSet, JavaClasspath.getInitializers());
				classPath.registerClassifierJar(URI.createFileURI(Paths.get("resources\\jamopp\\rt.jar").toAbsolutePath().toString()));
				classPath.registerClassifierJar(URI.createFileURI(Paths.get("resources\\mm\\microemu-javase-applet-2.0.4.jar").toAbsolutePath().toString()));
				classPath.registerClassifierJar(URI.createFileURI(Paths.get("resources\\mm\\microemu-jsr-120-2.0.4.jar").toAbsolutePath().toString()));
				Path[] libraryFolders = new Path[] {};

				try {
					for (Path libraryFolder : libraryFolders) {
						Files.walk(libraryFolder).forEach(f -> {
							if (Files.isRegularFile(f) && f.getFileName().toString().endsWith(".jar")) {
								classPath.registerClassifierJar(URI.createFileURI(f.toString()));
								System.out.println("ADDED JAR FILE: " + f);
							}
						});
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}, Arrays.asList(new ConsistencyRule[] { new DependencyLifting(), new HintComputation(), new AffectedConfigurations() }));

		// set up jamopp
		JavaClasspath.getInitializers().clear();
		JavaClasspath.getInitializers().add(new Initializer() {
			@Override
			public void initialize(Resource resource) {
			}

			@Override
			public boolean requiresLocalClasspath() {
				return true;
			}

			@Override
			public boolean requiresStdLib() {
				return false;
			}
		});
		EPackage.Registry.INSTANCE.put("http://www.emftext.org/java", JavaPackage.eINSTANCE);

		this.productNumber = 0;
	}

	protected Collection<Resource> parse(Path location) throws Exception {
		long timeStart = System.currentTimeMillis();

		ResourceSet resourceSet = ResourceSetUtil.withGlobalFactories(new ResourceSetImpl());
		resourceSet.getLoadOptions().put("DISABLE_LAYOUT_INFORMATION_RECORDING", Boolean.TRUE);
		resourceSet.getLoadOptions().put("DISABLE_LOCATION_MAP", Boolean.TRUE);
		resourceSet.getLoadOptions().put(JavaClasspath.OPTION_USE_LOCAL_CLASSPATH, Boolean.TRUE);
		resourceSet.getLoadOptions().put(JavaClasspath.OPTION_REGISTER_STD_LIB, Boolean.FALSE);

		Path vitruv_project_folder = location.getParent().resolve("test_project.marker_vitruv");
		if (!Files.exists(vitruv_project_folder))
			Files.createDirectories(vitruv_project_folder);

		// register jar files
		System.out.println("REGISTERING JAR FILES");
		JavaClasspath cp = JavaClasspath.get(resourceSet);
		cp.registerClassifierJar(URI.createFileURI(Paths.get("resources\\jamopp\\rt.jar").toAbsolutePath().toString()));
		cp.registerClassifierJar(URI.createFileURI(Paths.get("resources\\mm\\microemu-javase-applet-2.0.4.jar").toAbsolutePath().toString()));
		cp.registerClassifierJar(URI.createFileURI(Paths.get("resources\\mm\\microemu-jsr-120-2.0.4.jar").toAbsolutePath().toString()));
		List<Path> jarFiles = new ArrayList<>();
		Path[] libraryFolders = new Path[] { location };
		for (Path libraryFolder : libraryFolders) {
			Files.walk(libraryFolder).forEach(f -> {
				if (Files.isRegularFile(f) && f.getFileName().toString().endsWith(".jar")) {
					jarFiles.add(f);
					System.out.println("ADDED JAR FILE: " + f);
				}
			});
		}
		for (Path jarFile : jarFiles) {
			cp.registerClassifierJar(URI.createFileURI(jarFile.toString()));
		}

		// collect files to parse
		List<Path> javaFiles = new ArrayList<>();
		// parse constants first
		Path[] sourceFolders = new Path[] { location };
		for (Path sourceFolder : sourceFolders) {
			Files.walk(sourceFolder).forEach(f -> {
				if (Files.isDirectory(f) && !f.equals(sourceFolder) && !f.getFileName().toString().startsWith(".") && !f.getFileName().toString().equals("META-INF") && !f.getFileName().toString().equals("test_project.marker_vitruv") && !f.getFileName().toString().equals("umloutput") && !f.getFileName().toString().contains("-") && !f.getFileName().toString().startsWith("build-eclipse")
						&& !f.getFileName().toString().startsWith("bin") && !f.getFileName().toString().startsWith("template")) {
				} else if (Files.isRegularFile(f) && f.getFileName().toString().endsWith(".java") && !f.getFileName().toString().equals("package-info.java")) {
					if (!javaFiles.contains(f)) {
						javaFiles.add(f);
						System.out.println("ADDED JAVA FILE: " + f);
					} else {
						System.out.println("ALREADY CONTAINED JAVA FILE: " + f);
					}

					// prefill the uriconverter
					Path relPath = sourceFolder.relativize(f);
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < relPath.getNameCount(); i++) {
						sb.append(relPath.getName(i));
						if (i < relPath.getNameCount() - 1)
							sb.append(".");
					}
					resourceSet.getURIConverter().getURIMap().put(URI.createURI("pathmap:/javaclass/" + sb.toString()), URI.createFileURI(f.toString()));
				}
			});
		}

		// parse files
		System.out.println("PARSING JAVA FILES");
		List<Object[]> runtimemap = new ArrayList<>();
		List<Resource> resources = new ArrayList<>();
		for (Path javaFile : javaFiles) {
			System.out.println("FILE: " + javaFile);

			long localTimeStart = System.currentTimeMillis();
			Resource resource = resourceSet.getResource(URI.createFileURI(javaFile.toString()), true);
			long localTimeDiff = System.currentTimeMillis() - localTimeStart;
			runtimemap.add(new Object[] { localTimeDiff, javaFile.toString() });

			resources.add(resource);
		}

		System.out.println("NUM RESOURCES PARSED: " + resources.size());

		System.out.println("NUM RESOURCES IN RS: " + resourceSet.getResources().size());

		// resolve proxies
		System.out.println("RESOLVING PROXIES");
		resolveAllProxies(resourceSet);

		// convert pathmap uris to filesystem uris
		for (Resource resource : resources) {
			if (resource.getURI().toString().contains("pathmap:/javaclass/")) {
				if (resource.getURI().toString().contains("$")) {
					// change name of compilation unit
					if (resource.getContents().size() == 1 && resource.getContents().get(0) instanceof CompilationUnit) {
						CompilationUnit cu = (CompilationUnit) resource.getContents().get(0);
						if (cu.getName().contains("$")) {
							cu.setName(cu.getName().substring(0, cu.getName().lastIndexOf("$")) + ".java");
						}
					} else {
						System.out.println("FFF: " + resource.getURI());
					}
				}
				if (resourceSet.getURIConverter().getURIMap().get(resource.getURI()) != null) {
					resource.setURI(resourceSet.getURIConverter().getURIMap().get(resource.getURI()));
				} else {
					System.out.println("GGG: " + resource.getURI());
				}
			}
		}

		// change uri of resources
		for (Resource resource : resources) {
			Path relativeResourcePath = null;
			String fileString = resource.getURI().toFileString();
			if (fileString != null) {
				Path resourcePath = Paths.get(fileString);
				if (resourcePath.startsWith(location))
					relativeResourcePath = location.relativize(resourcePath);
				URI vaveURI = URI.createFileURI(vaveResourceLocation.resolve(relativeResourcePath).toString());
				System.out.println("URI: " + vaveURI);
				resource.setURI(vaveURI);
			} else {
				System.out.println("!!!!!!: " + resource.getURI());
			}
		}

		System.out.println("NUM RESOURCES IN RS AFTER PROXY RESOLUTIONS: " + resourceSet.getResources().size());

		List<Object[]> sortedRuntimeList = runtimemap.stream().sorted((o1, o2) -> Long.valueOf(((Long) o1[0]) - ((Long) o2[0])).intValue()).collect(Collectors.toList());
		for (Object[] entry : sortedRuntimeList) {
			System.out.println("T: " + entry[0] + " - " + entry[1]);
		}

		long timeDiff = System.currentTimeMillis() - timeStart;
		System.out.println("TOTAL TIME PARSING: " + timeDiff);

		return resources;
	}

	protected VirtualProductModel externalize(Configuration configuration, Path projectFolder) throws Exception {
		if (!Files.exists(vaveProjectMarker))
			Files.createDirectories(vaveProjectMarker);

		long timeStart = System.currentTimeMillis();

		// externalize product
		System.out.println("EXTERNALIZING PRODUCT");
		final VirtualProductModel vmp = vave.externalizeProduct(projectFolder.resolve("vsum" + (productNumber++)), configuration).getResult();

		long timeDiff = System.currentTimeMillis() - timeStart;
		System.out.println("TOTAL TIME EXTERNALIZATION: " + timeDiff);

		return vmp;
	}

	protected FeatureModel internalize(VirtualProductModel vmp, VirtualProductModel vmp2, Collection<Resource> resources, Expression<FeatureOption> e) throws Exception {
		if (!Files.exists(vaveProjectMarker))
			Files.createDirectories(vaveProjectMarker);

		long timeStart = System.currentTimeMillis();

		final MatchEngineFactoryImpl matchEngineFactory = new HierarchicalMatchEngineFactory(new EqualityHelper(EqualityHelper.createDefaultCache(CacheBuilder.newBuilder())), new SimilarityChecker());
		matchEngineFactory.setRanking(20);

		IMatchEngine.Factory.Registry registry = EMFCompareRCPPlugin.getDefault().getMatchEngineFactoryRegistry();
		registry.add(matchEngineFactory);

		IDiffProcessor diffProcessor = new DiffBuilder();
		IDiffEngine diffEngine = new DefaultDiffEngine(diffProcessor) {
			@Override
			protected FeatureFilter createFeatureFilter() {
				return new FeatureFilter() {
					@Override
					protected boolean isIgnoredReference(Match match, EReference reference) {
						return super.isIgnoredReference(match, reference);
					}

					@Override
					public boolean isIgnoredAttribute(EAttribute attribute) {
						return super.isIgnoredAttribute(attribute);
					}

					@Override
					public boolean checkForOrderingChanges(EStructuralFeature feature) {
						return super.checkForOrderingChanges(feature);
					}
				};
			}
		};

		// diff changes
		System.out.println("DIFFING");
		ResourceSet referenceResourceSet = vmp.getResourceSet();

		referenceResourceSet.getLoadOptions().put("DISABLE_LAYOUT_INFORMATION_RECORDING", Boolean.TRUE);
		referenceResourceSet.getLoadOptions().put("DISABLE_LOCATION_MAP", Boolean.TRUE);
		referenceResourceSet.getLoadOptions().put(JavaClasspath.OPTION_USE_LOCAL_CLASSPATH, Boolean.TRUE);
		referenceResourceSet.getLoadOptions().put(JavaClasspath.OPTION_REGISTER_STD_LIB, Boolean.FALSE);
		JavaClasspath refRSCP = JavaClasspath.get(referenceResourceSet, JavaClasspath.getInitializers());

		// register jar files
		System.out.println("REGISTERING JAR FILES");
		refRSCP.registerClassifierJar(URI.createFileURI(Paths.get("resources\\jamopp\\rt.jar").toAbsolutePath().toString()));
		refRSCP.registerClassifierJar(URI.createFileURI(Paths.get("resources\\mm\\microemu-javase-applet-2.0.4.jar").toAbsolutePath().toString()));
		refRSCP.registerClassifierJar(URI.createFileURI(Paths.get("resources\\mm\\microemu-jsr-120-2.0.4.jar").toAbsolutePath().toString()));
		List<Path> jarFiles = new ArrayList<>();
		Path[] libraryFolders = new Path[] {};
		for (Path libraryFolder : libraryFolders) {
			Files.walk(libraryFolder).forEach(f -> {
				if (Files.isRegularFile(f) && f.getFileName().toString().endsWith(".jar")) {
					jarFiles.add(f);
					System.out.println("ADDED JAR FILE: " + f);
				}
			});
		}
		for (Path jarFile : jarFiles) {
			refRSCP.registerClassifierJar(URI.createFileURI(jarFile.toString()));
		}

		final ChangeRecorder changeRecorder = new ChangeRecorder(referenceResourceSet);
		changeRecorder.addToRecording(referenceResourceSet);
		changeRecorder.beginRecording();

		for (Resource resource : resources) {
			// EMFCompare comparator = EMFCompare.builder().build();
			EMFCompare comparator = EMFCompare.builder().setMatchEngineFactoryRegistry(registry).setDiffEngine(diffEngine).build();

			Resource referenceResource;

			referenceResource = referenceResourceSet.getResource(resource.getURI(), false);
			if (referenceResource == null) {
				System.out.println("NEW RESOURCE DETECTED: " + resource.getURI());
				referenceResource = referenceResourceSet.createResource(resource.getURI());
			}

			IComparisonScope scope = new DefaultComparisonScope(resource, referenceResource, null);
			Comparison comparison = comparator.compare(scope);
			List<Diff> differences = comparison.getDifferences();

			System.out.println("NUM DIFFS: " + differences.size());
			System.out.println("RESOURCE: " + resource.getURI() + " - " + referenceResource.getURI());

			IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();
			IBatchMerger merger = new BatchMerger(mergerRegistry);
			merger.copyAllLeftToRight(differences, new BasicMonitor());
		}

		// check for deleted resources
		Collection<Resource> deletedResources = referenceResourceSet.getResources().stream().filter(r -> r.getURI().toString().contains("mobilemedia") && resources.stream().filter(r2 -> r2.getURI().equals(r.getURI())).findAny().isEmpty()).collect(Collectors.toList());
		for (Resource deletedResource : deletedResources) {
			EMFCompare comparator = EMFCompare.builder().setMatchEngineFactoryRegistry(registry).setDiffEngine(diffEngine).build();

			System.out.println("DELETED RESOURCE DETECTED: " + deletedResource.getURI());

			IComparisonScope scope = new DefaultComparisonScope(new ResourceSetImpl().createResource(deletedResource.getURI()), deletedResource, null);
			Comparison comparison = comparator.compare(scope);
			List<Diff> differences = comparison.getDifferences();

			System.out.println("NUM DIFFS: " + differences.size());
			System.out.println("RESOURCE: " + deletedResource.getURI());

			IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();
			IBatchMerger merger = new BatchMerger(mergerRegistry);
			merger.copyAllLeftToRight(differences, new BasicMonitor());
		}

		final TransactionalChange recordedChange = changeRecorder.endRecording();
		changeRecorder.close();

		System.out.println("NUM RECORDED CHANGES: " + recordedChange.getEChanges().size());

		// order recorded changes
		System.out.println("ORDERING CHANGES");
		ArrayList<EChange> newEChanges = new ArrayList<>();
		ArrayList<EChange> toAppend = new ArrayList<>();
		ArrayList<EChange> toAppend2 = new ArrayList<>();
		for (EChange change : recordedChange.getEChanges()) {
			if ((change instanceof ReplaceSingleValuedEReference) && ((ReplaceSingleValuedEReference) change).getNewValueID() != null && ((ReplaceSingleValuedEReference) change).getNewValueID().contains("pathmap") && ((ReplaceSingleValuedEReference) change).getNewValueID().contains(".java#/1")) {
				// this is the workaround for the the problem vitruvius has with the ".length" field of arrays of all types outside of the actually parsed source code (e.g., java.lang.Object or java.lang.Byte).
				// System.out.println("IGNORE: " + change);
			} else if ((change instanceof ReplaceSingleValuedEReference) && ((ReplaceSingleValuedEReference) change).getNewValueID() != null && ((ReplaceSingleValuedEReference) change).getAffectedEObject() != null && !((EObject) ((ReplaceSingleValuedEReference) change).getNewValue()).eResource().getURI().equals(((ReplaceSingleValuedEReference) change).getAffectedEObject().eResource().getURI())) {
				toAppend2.add(change);
				// System.out.println("moved change to back: " + change);
			} else if ((change instanceof InsertEReference) && ((InsertEReference) change).getNewValue() != null && ((InsertEReference) change).getAffectedEObject() != null && !((EObject) ((InsertEReference) change).getNewValue()).eResource().getURI().equals(((InsertEReference) change).getAffectedEObject().eResource().getURI())) {
				toAppend.add(change);
				// System.out.println("moved change to back: " + change);
			} else {
				newEChanges.add(change);
			}
		}
		ArrayList<EChange> orderedChanges = new ArrayList<>();
		orderedChanges.addAll(newEChanges);
		orderedChanges.addAll(toAppend);
		orderedChanges.addAll(toAppend2);
		TransactionalChange orderedChange = new TransactionalChangeImpl(orderedChanges);

		System.out.println("NUM CHANGES: " + orderedChange.getEChanges().size());

		// unresolve change
		VitruviusChange unresolvedChange = orderedChange.unresolve();

		// propagate changes into product
		System.out.println("PROPAGATING CHANGES INTO PRODUCT");
		// vmp2.propagateChange(recordedChange);
		// vmp2.propagateChange(orderedChange);
		vmp2.propagateChange(unresolvedChange);

		// internalize changes in product into system
		System.out.println("INTERNALIZING CHANGES IN PRODUCT INTO SYSTEM");
		FeatureModel repairedFM = ((DependencyLifting.Result) vave.internalizeChanges(vmp2, e).getConsistencyResult(DependencyLifting.class)).getRepairedFeatureModel();
		this.outputEvalFeatureModel(repairedFM);
		// this.vave.internalizeDomain(repairedFM);

		long timeDiff = System.currentTimeMillis() - timeStart;
		System.out.println("TOTAL TIME INTERNALIZATION: " + timeDiff);

		return repairedFM;
	}

	protected static void resolveAllProxies(ResourceSet rs) {
		if (!resolveAllProxiesRecursive(rs, 0)) {
			System.err.println("Resolution of some Proxies failed...");
			Iterator<Notifier> it = rs.getAllContents();
			while (it.hasNext()) {
				Notifier next = it.next();
				if (next instanceof EObject) {
					EObject o = (EObject) next;
					if (o.eIsProxy()) {
						try {
							it.remove();
						} catch (UnsupportedOperationException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	protected static boolean resolveAllProxiesRecursive(ResourceSet rs, int resourcesProcessedBefore) {
		boolean failure = false;
		List<EObject> eobjects = new LinkedList<EObject>();
		for (Iterator<Notifier> i = rs.getAllContents(); i.hasNext();) {
			Notifier next = i.next();
			if (next instanceof EObject) {
				eobjects.add((EObject) next);
			}
		}
		int resourcesProcessed = rs.getResources().size();
		if (resourcesProcessed == resourcesProcessedBefore) {
			return true;
		}

		int resolved = 0;
		int notResolved = 0;
		int eobjectCnt = 0;
		for (EObject next : eobjects) {
			eobjectCnt++;

			InternalEObject nextElement = (InternalEObject) next;
			nextElement = (InternalEObject) EcoreUtil.resolve(nextElement, rs);
			for (EObject crElement : nextElement.eCrossReferences()) {
				crElement = EcoreUtil.resolve(crElement, rs);
				if (crElement.eIsProxy()) {
					failure = true;
					notResolved++;
					System.out.println("Can not find referenced element in classpath: " + ((InternalEObject) crElement).eProxyURI());
				} else {
					resolved++;
				}
			}
		}

		// call this method again, because the resolving might have triggered loading of additional resources that may also contain references that need to be resolved.
		return !failure && resolveAllProxiesRecursive(rs, resourcesProcessed);
	}

	private void printHints(String revision, Collection<Feature[]> hints) throws IOException {
		String type2string = hints.stream().map(fi -> Arrays.asList(fi).stream().map(f -> f.getName()).collect(Collectors.joining("/"))).collect(Collectors.joining(System.lineSeparator()));
		System.out.println(type2string);
		Files.writeString(vaveResourceLocation.getParent().resolve("R" + revision + "-eval-type2.txt"), "TYPE 2 EVAL " + revision + ":" + System.lineSeparator() + "NEW INTERACTIONS:" + System.lineSeparator() + type2string, StandardOpenOption.CREATE);
	}

	/**
	 * This test starts from a product with no (or only core) features and adds individual features.
	 * 
	 * @throws Exception
	 */
	@Test
	public void EvalTestAdditive() throws Exception {
		// initialize vave system
		// done in @Before

		Path variantsLocation = Paths.get("C:\\FZI\\git\\mm-spl-revisions-variants");

		Feature Fcore;
		Feature Flabel;
		Feature Fsortcount;
		Feature Ffav;
		Feature Fcopy;
		Feature Fsms;

		{ // # REVISION 1
			Path revisionVariantsLocation = variantsLocation.resolve("R1_variants");
			System.out.println("START REV 1");

			// internalize domain with just the feature and without any constraints
			FeatureModel fm = FeaturemodelFactory.eINSTANCE.createFeatureModel();
			ViewFeature viewFcore = FeaturemodelFactory.eINSTANCE.createViewFeature();
			viewFcore.setName("Core");
//			fm.getRootFeatures().add(viewFcore);
			fm.getFeatureOptions().add(viewFcore);
			Collection<Feature[]> hints10 = ((HintComputation.Result) this.vave.internalizeDomain(fm).getConsistencyResult(HintComputation.class)).getNewHints();
			this.printHints("1-beginning", hints10);
			Fcore = this.vave.getSystem().getFeatures().stream().filter(f -> f.getName().equals("Core")).findAny().get();

			FeatureModel repairedFM = null;

			{ // CORE
				long timeStart = System.currentTimeMillis();

				System.out.println("START REV 1 PROD 1");
				// externalize empty product with expression TRUE
				Configuration configuration = VaveFactory.eINSTANCE.createConfiguration();
				configuration.getOptions().add(vave.getSystem().getSystemRevisions().get(vave.getSystem().getSystemRevisions().size() - 1));
				VirtualProductModel vmp = this.externalize(configuration, vaveResourceLocation.getParent().resolve("R1-V-CORE-empty-ext-vsum\\src\\"));
				Files.move(vaveResourceLocation, vaveResourceLocation.getParent().resolve("R1-V-CORE-empty-ext"));

				VirtualProductModel vmp2 = this.externalize(configuration, vaveResourceLocation.getParent().resolve("R1-V-CORE-empty-ext-int-vsum\\src\\"));

				Collection<Resource> resources = this.parse(revisionVariantsLocation.resolve("V-CORE\\src\\"));
				Variable<FeatureOption> expression = ExpressionFactory.eINSTANCE.createVariable();
				expression.setValue(Fcore);
				repairedFM = this.internalize(vmp, vmp2, resources, expression);
				Files.move(vaveResourceLocation, vaveResourceLocation.getParent().resolve("R0-V-CORE-int"));

				long timeDiff = System.currentTimeMillis() - timeStart;
				System.out.println("TOTAL TIME: " + timeDiff);
			}

			// EVALUATION
			// type 6
			Path evalVariantsLocation = vaveResourceLocation.getParent().resolve("R1-eval-variants");
			Files.createDirectory(evalVariantsLocation);
			this.createEvalVariants(evalVariantsLocation);
			// type 5
			// TODO: check if the automatically added constraints match the actual constraints in the ground truth feature model of MM revision
			System.out.println("TYPE 5 EVAL 1:");
			String type5eval1string = this.outputEvalFeatureModel(repairedFM);
			Files.writeString(vaveResourceLocation.getParent().resolve("R1-eval-type5.txt"), "TYPE 5 EVAL 1:" + System.lineSeparator() + type5eval1string, StandardOpenOption.CREATE);
			if (repairedFM != null) {
				Collection<Feature[]> hints1 = ((HintComputation.Result) this.vave.internalizeDomain(repairedFM).getConsistencyResult(HintComputation.class)).getNewHints();
				this.printHints("1-end", hints1);
			}
		}

		{ // # REVISION 2
			Path revisionVariantsLocation = variantsLocation.resolve("R2_variants");
			System.out.println("START REV 2");

			// internalize domain with just the features and without any constraints
			FeatureModel fm = this.vave.externalizeDomain(this.vave.getSystem().getSystemRevisions().get(this.vave.getSystem().getSystemRevisions().size() - 1)).getResult();
			ViewFeature viewFlabel = FeaturemodelFactory.eINSTANCE.createViewFeature();
			viewFlabel.setName("Label");
			fm.getFeatureOptions().add(viewFlabel);
			ViewFeature viewFsortcount = FeaturemodelFactory.eINSTANCE.createViewFeature();
			viewFsortcount.setName("Sort");
			fm.getFeatureOptions().add(viewFsortcount);
			Collection<Feature[]> hints2before = ((HintComputation.Result) this.vave.internalizeDomain(fm).getConsistencyResult(HintComputation.class)).getNewHints();
			this.printHints("2-beginning", hints2before);
			Flabel = this.vave.getSystem().getFeatures().stream().filter(f -> f.getName().equals("Label")).findAny().get();
			Fsortcount = this.vave.getSystem().getFeatures().stream().filter(f -> f.getName().equals("Sort")).findAny().get();

			FeatureModel repairedFM = null;

			{
				long timeStart = System.currentTimeMillis();

				System.out.println("START REV 2 PROD 1");
				Configuration configuration = VaveFactory.eINSTANCE.createConfiguration();
				configuration.getOptions().add(Fcore.getFeatureRevisions().get(Fcore.getFeatureRevisions().size() - 1));
				configuration.getOptions().add(vave.getSystem().getSystemRevisions().get(vave.getSystem().getSystemRevisions().size() - 1));
				VirtualProductModel vmp = this.externalize(configuration, vaveResourceLocation.getParent().resolve("R2-V-CORE-ext-vsum\\src\\"));
				Files.move(vaveResourceLocation, vaveResourceLocation.getParent().resolve("R2-V-CORE-ext"));

				VirtualProductModel vmp2 = this.externalize(configuration, vaveResourceLocation.getParent().resolve("R2-V-CORE-ext-int-vsum\\src\\"));

				Collection<Resource> resources = this.parse(revisionVariantsLocation.resolve("V-CORE-LABEL\\src\\"));
				Variable<FeatureOption> variable = ExpressionFactory.eINSTANCE.createVariable();
				variable.setValue(Flabel);
				this.internalize(vmp, vmp2, resources, variable);
				Files.move(vaveResourceLocation, vaveResourceLocation.getParent().resolve("R2-V-CORE-LABEL-int"));

				long timeDiff = System.currentTimeMillis() - timeStart;
				System.out.println("TOTAL TIME: " + timeDiff);
			}

			{
				long timeStart = System.currentTimeMillis();

				System.out.println("START REV 2 PROD 2");
				Configuration configuration = VaveFactory.eINSTANCE.createConfiguration();
				configuration.getOptions().add(Fcore.getFeatureRevisions().get(Fcore.getFeatureRevisions().size() - 1));
				configuration.getOptions().add(Flabel.getFeatureRevisions().get(Flabel.getFeatureRevisions().size() - 1));
				configuration.getOptions().add(vave.getSystem().getSystemRevisions().get(vave.getSystem().getSystemRevisions().size() - 1));
				VirtualProductModel vmp = this.externalize(configuration, vaveResourceLocation.getParent().resolve("R2-V-CORE-LABEL-ext-vsum\\src\\"));
				Files.move(vaveResourceLocation, vaveResourceLocation.getParent().resolve("R2-V-CORE-LABEL-ext"));

				VirtualProductModel vmp2 = this.externalize(configuration, vaveResourceLocation.getParent().resolve("R2-V-CORE-LABEL-ext-int-vsum\\src\\"));

				Collection<Resource> resources = this.parse(revisionVariantsLocation.resolve("V-CORE-LABEL-SORT\\src\\"));
				Variable<FeatureOption> variable = ExpressionFactory.eINSTANCE.createVariable();
				variable.setValue(Fsortcount);
				repairedFM = this.internalize(vmp, vmp2, resources, variable);
				Files.move(vaveResourceLocation, vaveResourceLocation.getParent().resolve("R2-V-CORE-LABEL-SORT-int"));

				long timeDiff = System.currentTimeMillis() - timeStart;
				System.out.println("TOTAL TIME: " + timeDiff);
			}

			// EVALUATION
			// type 6
			Path evalVariantsLocation = vaveResourceLocation.getParent().resolve("R2-eval-variants");
			Files.createDirectory(evalVariantsLocation);
			this.createEvalVariants(evalVariantsLocation);
			// type 5
			// TODO: check if the automatically added constraints match the actual constraints in the ground truth feature model of MM revision
			System.out.println("TYPE 5 EVAL 2:");
			String type5eval2string = this.outputEvalFeatureModel(repairedFM);
			Files.writeString(vaveResourceLocation.getParent().resolve("R2-eval-type5.txt"), "TYPE 5 EVAL 2:" + System.lineSeparator() + type5eval2string, StandardOpenOption.CREATE);
			if (repairedFM != null) {
				Collection<Feature[]> hints2 = ((HintComputation.Result) this.vave.internalizeDomain(repairedFM).getConsistencyResult(HintComputation.class)).getNewHints();
				this.printHints("2-end", hints2);
			}
		}

		{ // # REVISION 3
			Path revisionVariantsLocation = variantsLocation.resolve("R3_variants");
			System.out.println("START REV 3");

			// internalize domain with just the features and without any constraints
			FeatureModel fm = this.vave.externalizeDomain(this.vave.getSystem().getSystemRevisions().get(this.vave.getSystem().getSystemRevisions().size() - 1)).getResult();
			ViewFeature viewFfav = FeaturemodelFactory.eINSTANCE.createViewFeature();
			viewFfav.setName("Fav");
			fm.getFeatureOptions().add(viewFfav);
			Collection<Feature[]> hints3before = ((HintComputation.Result) this.vave.internalizeDomain(fm).getConsistencyResult(HintComputation.class)).getNewHints();
			this.printHints("3-beginning", hints3before);
			Ffav = this.vave.getSystem().getFeatures().stream().filter(f -> f.getName().equals("Fav")).findAny().get();

			FeatureModel repairedFM = null;

			{
				long timeStart = System.currentTimeMillis();

				System.out.println("START REV 3 PROD 1");
				Configuration configuration = VaveFactory.eINSTANCE.createConfiguration();
				configuration.getOptions().add(Fcore.getFeatureRevisions().get(Fcore.getFeatureRevisions().size() - 1));
				configuration.getOptions().add(Flabel.getFeatureRevisions().get(Flabel.getFeatureRevisions().size() - 1));
				configuration.getOptions().add(vave.getSystem().getSystemRevisions().get(vave.getSystem().getSystemRevisions().size() - 1));
				VirtualProductModel vmp = this.externalize(configuration, vaveResourceLocation.getParent().resolve("R3-V-CORE-LABEL-ext-vsum\\src\\"));
				Files.move(vaveResourceLocation, vaveResourceLocation.getParent().resolve("R3-V-CORE-LABEL-ext"));

				VirtualProductModel vmp2 = this.externalize(configuration, vaveResourceLocation.getParent().resolve("R3-V-CORE-LABEL-ext-int-vsum\\src\\"));

				Collection<Resource> resources = this.parse(revisionVariantsLocation.resolve("V-CORE-LABEL\\src\\"));
				Variable<FeatureOption> variable = ExpressionFactory.eINSTANCE.createVariable();
				variable.setValue(Fcore);
				this.internalize(vmp, vmp2, resources, variable);
				Files.move(vaveResourceLocation, vaveResourceLocation.getParent().resolve("R3-V-CORE-LABEL-int"));

				long timeDiff = System.currentTimeMillis() - timeStart;
				System.out.println("TOTAL TIME: " + timeDiff);
			}

			{
				long timeStart = System.currentTimeMillis();

				System.out.println("START REV 3 PROD 2");
				Configuration configuration = VaveFactory.eINSTANCE.createConfiguration();
				configuration.getOptions().add(Fcore.getFeatureRevisions().get(Fcore.getFeatureRevisions().size() - 1));
				configuration.getOptions().add(Flabel.getFeatureRevisions().get(Flabel.getFeatureRevisions().size() - 1));
				configuration.getOptions().add(vave.getSystem().getSystemRevisions().get(vave.getSystem().getSystemRevisions().size() - 1));
				VirtualProductModel vmp = this.externalize(configuration, vaveResourceLocation.getParent().resolve("R3-V-CORE-LABEL-ext-vsum2\\src\\"));
				Files.move(vaveResourceLocation, vaveResourceLocation.getParent().resolve("R3-V-CORE-LABEL-ext2"));

				VirtualProductModel vmp2 = this.externalize(configuration, vaveResourceLocation.getParent().resolve("R3-V-CORE-LABEL-ext-int-vsum2\\src\\"));

				Collection<Resource> resources = this.parse(revisionVariantsLocation.resolve("V-CORE-LABEL-FAV\\src\\"));
				Variable<FeatureOption> variable = ExpressionFactory.eINSTANCE.createVariable();
				variable.setValue(Ffav);
				repairedFM = this.internalize(vmp, vmp2, resources, variable);
				Files.move(vaveResourceLocation, vaveResourceLocation.getParent().resolve("R3-V-CORE-LABEL-FAV-int"));

				long timeDiff = System.currentTimeMillis() - timeStart;
				System.out.println("TOTAL TIME: " + timeDiff);
			}

			// EVALUATION
			// type 6
			Path evalVariantsLocation = vaveResourceLocation.getParent().resolve("R3-eval-variants");
			Files.createDirectory(evalVariantsLocation);
			this.createEvalVariants(evalVariantsLocation);
			// type 5
			// TODO: check if the automatically added constraints match the actual constraints in the ground truth feature model of MM revision
			System.out.println("TYPE 5 EVAL 3:");
			String type5eval3string = this.outputEvalFeatureModel(repairedFM);
			Files.writeString(vaveResourceLocation.getParent().resolve("R3-eval-type5.txt"), "TYPE 5 EVAL 3:" + System.lineSeparator() + type5eval3string, StandardOpenOption.CREATE);
			if (repairedFM != null) {
				Collection<Feature[]> hints3 = ((HintComputation.Result) this.vave.internalizeDomain(repairedFM).getConsistencyResult(HintComputation.class)).getNewHints();
				this.printHints("3-end", hints3);
			}
		}

		{ // # REVISION 4
			Path revisionVariantsLocation = variantsLocation.resolve("R4_variants");
			System.out.println("START REV 4");

			FeatureModel fm = this.vave.externalizeDomain(this.vave.getSystem().getSystemRevisions().get(this.vave.getSystem().getSystemRevisions().size() - 1)).getResult();
			ViewFeature viewFcopy = FeaturemodelFactory.eINSTANCE.createViewFeature();
			viewFcopy.setName("Copy");
			fm.getFeatureOptions().add(viewFcopy);
			Collection<Feature[]> hints4before = ((HintComputation.Result) this.vave.internalizeDomain(fm).getConsistencyResult(HintComputation.class)).getNewHints();
			this.printHints("4-beginning", hints4before);
			Fcopy = this.vave.getSystem().getFeatures().stream().filter(f -> f.getName().equals("Copy")).findAny().get();

			FeatureModel repairedFM = null;

			{ // core
				long timeStart = System.currentTimeMillis();

				System.out.println("START REV 4 PROD 1");
				Configuration configuration = VaveFactory.eINSTANCE.createConfiguration();
				configuration.getOptions().add(Fcore.getFeatureRevisions().get(Fcore.getFeatureRevisions().size() - 1));
				configuration.getOptions().add(Flabel.getFeatureRevisions().get(Flabel.getFeatureRevisions().size() - 1));
				configuration.getOptions().add(vave.getSystem().getSystemRevisions().get(vave.getSystem().getSystemRevisions().size() - 1));
				VirtualProductModel vmp = this.externalize(configuration, vaveResourceLocation.getParent().resolve("R4-V-CORE-LABEL-ext-vsum\\src\\"));
				Files.move(vaveResourceLocation, vaveResourceLocation.getParent().resolve("R4-V-CORE-LABEL-ext"));

				VirtualProductModel vmp2 = this.externalize(configuration, vaveResourceLocation.getParent().resolve("R4-V-CORE-LABEL-ext-int-vsum\\src\\"));

				Collection<Resource> resources = this.parse(revisionVariantsLocation.resolve("V-CORE-LABEL\\src\\"));
				Variable<FeatureOption> variable = ExpressionFactory.eINSTANCE.createVariable();
				variable.setValue(Fcore);
				this.internalize(vmp, vmp2, resources, variable);
				Files.move(vaveResourceLocation, vaveResourceLocation.getParent().resolve("R4-V-CORE-LABEL-int"));

				long timeDiff = System.currentTimeMillis() - timeStart;
				System.out.println("TOTAL TIME: " + timeDiff);
			}

			{ // sort
				long timeStart = System.currentTimeMillis();

				System.out.println("START REV 4 PROD 2");
				Configuration configuration = VaveFactory.eINSTANCE.createConfiguration();
				configuration.getOptions().add(Fcore.getFeatureRevisions().get(Fcore.getFeatureRevisions().size() - 1));
				configuration.getOptions().add(Flabel.getFeatureRevisions().get(Flabel.getFeatureRevisions().size() - 1));
				configuration.getOptions().add(Fsortcount.getFeatureRevisions().get(Fsortcount.getFeatureRevisions().size() - 1));
				configuration.getOptions().add(vave.getSystem().getSystemRevisions().get(vave.getSystem().getSystemRevisions().size() - 1));
				VirtualProductModel vmp = this.externalize(configuration, vaveResourceLocation.getParent().resolve("R4-V-CORE-LABEL-ext-vsum2\\src\\"));
				Files.move(vaveResourceLocation, vaveResourceLocation.getParent().resolve("R4-V-CORE-LABEL-ext2"));

				VirtualProductModel vmp2 = this.externalize(configuration, vaveResourceLocation.getParent().resolve("R4-V-CORE-LABEL-ext-int-vsum2\\src\\"));

				Collection<Resource> resources = this.parse(revisionVariantsLocation.resolve("V-CORE-LABEL-SORT\\src\\"));
				Variable<FeatureOption> variable = ExpressionFactory.eINSTANCE.createVariable();
				variable.setValue(Fsortcount);
				this.internalize(vmp, vmp2, resources, variable);
				Files.move(vaveResourceLocation, vaveResourceLocation.getParent().resolve("R4-V-CORE-LABEL-SORT-int"));

				long timeDiff = System.currentTimeMillis() - timeStart;
				System.out.println("TOTAL TIME: " + timeDiff);
			}

			{ // fav
				long timeStart = System.currentTimeMillis();

				System.out.println("START REV 4 PROD 3");
				Configuration configuration = VaveFactory.eINSTANCE.createConfiguration();
				configuration.getOptions().add(Fcore.getFeatureRevisions().get(Fcore.getFeatureRevisions().size() - 1));
				configuration.getOptions().add(Flabel.getFeatureRevisions().get(Flabel.getFeatureRevisions().size() - 1));
				configuration.getOptions().add(Ffav.getFeatureRevisions().get(Ffav.getFeatureRevisions().size() - 1));
				configuration.getOptions().add(vave.getSystem().getSystemRevisions().get(vave.getSystem().getSystemRevisions().size() - 1));
				VirtualProductModel vmp = this.externalize(configuration, vaveResourceLocation.getParent().resolve("R4-V-CORE-LABEL-ext-vsum3\\src\\"));
				Files.move(vaveResourceLocation, vaveResourceLocation.getParent().resolve("R4-V-CORE-LABEL-ext3"));

				VirtualProductModel vmp2 = this.externalize(configuration, vaveResourceLocation.getParent().resolve("R4-V-CORE-LABEL-ext-int-vsum3\\src\\"));

				Collection<Resource> resources = this.parse(revisionVariantsLocation.resolve("V-CORE-LABEL-FAV\\src\\"));
				Variable<FeatureOption> variable = ExpressionFactory.eINSTANCE.createVariable();
				variable.setValue(Ffav);
				this.internalize(vmp, vmp2, resources, variable);
				Files.move(vaveResourceLocation, vaveResourceLocation.getParent().resolve("R4-V-CORE-LABEL-FAV-int"));

				long timeDiff = System.currentTimeMillis() - timeStart;
				System.out.println("TOTAL TIME: " + timeDiff);
			}

			{ // sort && fav
				long timeStart = System.currentTimeMillis();

				System.out.println("START REV 4 PROD 4");
				Configuration configuration = VaveFactory.eINSTANCE.createConfiguration();
				configuration.getOptions().add(Fcore.getFeatureRevisions().get(Fcore.getFeatureRevisions().size() - 1));
				configuration.getOptions().add(Flabel.getFeatureRevisions().get(Flabel.getFeatureRevisions().size() - 1));
				configuration.getOptions().add(Fsortcount.getFeatureRevisions().get(Fsortcount.getFeatureRevisions().size() - 1));
				configuration.getOptions().add(Ffav.getFeatureRevisions().get(Ffav.getFeatureRevisions().size() - 1));
				configuration.getOptions().add(vave.getSystem().getSystemRevisions().get(vave.getSystem().getSystemRevisions().size() - 1));
				VirtualProductModel vmp = this.externalize(configuration, vaveResourceLocation.getParent().resolve("R4-V-CORE-LABEL-SORT-FAV-ext-vsum\\src\\"));
				Files.move(vaveResourceLocation, vaveResourceLocation.getParent().resolve("R4-V-CORE-LABEL-SORT-FAV-ext"));

				VirtualProductModel vmp2 = this.externalize(configuration, vaveResourceLocation.getParent().resolve("R4-V-CORE-LABEL-SORT-FAV-ext-int-vsum\\src\\"));

				Collection<Resource> resources = this.parse(revisionVariantsLocation.resolve("V-CORE-LABEL-SORT-FAV\\src\\"));
				Conjunction<FeatureOption> conjunction = ExpressionFactory.eINSTANCE.createConjunction();
				Variable<FeatureOption> variable1 = ExpressionFactory.eINSTANCE.createVariable();
				variable1.setValue(Fsortcount);
				Variable<FeatureOption> variable2 = ExpressionFactory.eINSTANCE.createVariable();
				variable2.setValue(Ffav);
				conjunction.getExpressions().add(variable1);
				conjunction.getExpressions().add(variable2);
				this.internalize(vmp, vmp2, resources, conjunction);
				Files.move(vaveResourceLocation, vaveResourceLocation.getParent().resolve("R4-V-CORE-LABEL-SORT-FAV-int"));

				long timeDiff = System.currentTimeMillis() - timeStart;
				System.out.println("TOTAL TIME: " + timeDiff);
			}

			{ // copy
				long timeStart = System.currentTimeMillis();

				System.out.println("START REV 4 PROD 5");
				Configuration configuration = VaveFactory.eINSTANCE.createConfiguration();
				configuration.getOptions().add(Fcore.getFeatureRevisions().get(Fcore.getFeatureRevisions().size() - 1));
				configuration.getOptions().add(Flabel.getFeatureRevisions().get(Flabel.getFeatureRevisions().size() - 1));
				configuration.getOptions().add(vave.getSystem().getSystemRevisions().get(vave.getSystem().getSystemRevisions().size() - 1));
				VirtualProductModel vmp = this.externalize(configuration, vaveResourceLocation.getParent().resolve("R4-V-CORE-LABEL-ext-vsum4\\src\\"));
				Files.move(vaveResourceLocation, vaveResourceLocation.getParent().resolve("R4-V-CORE-LABEL-ext4"));

				VirtualProductModel vmp2 = this.externalize(configuration, vaveResourceLocation.getParent().resolve("R4-V-CORE-LABEL-ext-int-vsum4\\src\\"));

				Collection<Resource> resources = this.parse(revisionVariantsLocation.resolve("V-CORE-LABEL-COPY\\src\\"));
				Variable<FeatureOption> variable = ExpressionFactory.eINSTANCE.createVariable();
				variable.setValue(Fcopy);
				repairedFM = this.internalize(vmp, vmp2, resources, variable);
				Files.move(vaveResourceLocation, vaveResourceLocation.getParent().resolve("R4-V-CORE-LABEL-COPY-int"));

				long timeDiff = System.currentTimeMillis() - timeStart;
				System.out.println("TOTAL TIME: " + timeDiff);
			}

			// EVALUATION
			// type 6
			Path evalVariantsLocation = vaveResourceLocation.getParent().resolve("R4-eval-variants");
			Files.createDirectory(evalVariantsLocation);
			this.createEvalVariants(evalVariantsLocation);
			// type 5
			// TODO: check if the automatically added constraints match the actual constraints in the ground truth feature model of MM revision
			System.out.println("TYPE 5 EVAL 4:");
			String type5eval4string = this.outputEvalFeatureModel(repairedFM);
			Files.writeString(vaveResourceLocation.getParent().resolve("R4-eval-type5.txt"), "TYPE 5 EVAL 4:" + System.lineSeparator() + type5eval4string, StandardOpenOption.CREATE);
			if (repairedFM != null) {
				Collection<Feature[]> hints4 = ((HintComputation.Result) this.vave.internalizeDomain(repairedFM).getConsistencyResult(HintComputation.class)).getNewHints();
				this.printHints("4-end", hints4);
			}
		}

		{ // # REVISION 5
			Path revisionVariantsLocation = variantsLocation.resolve("R5_variants");
			System.out.println("START REV 5");

			FeatureModel fm = this.vave.externalizeDomain(this.vave.getSystem().getSystemRevisions().get(this.vave.getSystem().getSystemRevisions().size() - 1)).getResult();
			ViewFeature viewFsms = FeaturemodelFactory.eINSTANCE.createViewFeature();
			viewFsms.setName("SMS");
			fm.getFeatureOptions().add(viewFsms);
			Collection<Feature[]> hints5before = ((HintComputation.Result) this.vave.internalizeDomain(fm).getConsistencyResult(HintComputation.class)).getNewHints();
			this.printHints("5-beginning", hints5before);
			Fsms = this.vave.getSystem().getFeatures().stream().filter(f -> f.getName().equals("SMS")).findAny().get();

			FeatureModel repairedFM = null;

			{ // core
				long timeStart = System.currentTimeMillis();

				System.out.println("START REV 5 PROD 1");
				Configuration configuration = VaveFactory.eINSTANCE.createConfiguration();
				configuration.getOptions().add(Fcore.getFeatureRevisions().get(Fcore.getFeatureRevisions().size() - 1));
				configuration.getOptions().add(Flabel.getFeatureRevisions().get(Flabel.getFeatureRevisions().size() - 1));
				configuration.getOptions().add(vave.getSystem().getSystemRevisions().get(vave.getSystem().getSystemRevisions().size() - 1));
				VirtualProductModel vmp = this.externalize(configuration, vaveResourceLocation.getParent().resolve("R5-V-CORE-LABEL-ext-vsum\\src\\"));
				Files.move(vaveResourceLocation, vaveResourceLocation.getParent().resolve("R5-V-CORE-LABEL-ext"));

				VirtualProductModel vmp2 = this.externalize(configuration, vaveResourceLocation.getParent().resolve("R5-V-CORE-LABEL-ext-int-vsum\\src\\"));

				Collection<Resource> resources = this.parse(revisionVariantsLocation.resolve("V-CORE-LABEL\\src\\"));
				Variable<FeatureOption> variable = ExpressionFactory.eINSTANCE.createVariable();
				variable.setValue(Fcore);
				this.internalize(vmp, vmp2, resources, variable);
				Files.move(vaveResourceLocation, vaveResourceLocation.getParent().resolve("R5-V-CORE-LABEL-int"));

				long timeDiff = System.currentTimeMillis() - timeStart;
				System.out.println("TOTAL TIME: " + timeDiff);
			}

			{ // copy
				long timeStart = System.currentTimeMillis();

				System.out.println("START REV 5 PROD 2");
				Configuration configuration = VaveFactory.eINSTANCE.createConfiguration();
				configuration.getOptions().add(Fcore.getFeatureRevisions().get(Fcore.getFeatureRevisions().size() - 1));
				configuration.getOptions().add(Flabel.getFeatureRevisions().get(Flabel.getFeatureRevisions().size() - 1));
				configuration.getOptions().add(Fcopy.getFeatureRevisions().get(Fcopy.getFeatureRevisions().size() - 1));
				configuration.getOptions().add(vave.getSystem().getSystemRevisions().get(vave.getSystem().getSystemRevisions().size() - 1));
				VirtualProductModel vmp = this.externalize(configuration, vaveResourceLocation.getParent().resolve("R5-V-CORE-LABEL-COPY-ext-vsum\\src\\"));
				Files.move(vaveResourceLocation, vaveResourceLocation.getParent().resolve("R5-V-CORE-LABEL-COPY-ext"));

				VirtualProductModel vmp2 = this.externalize(configuration, vaveResourceLocation.getParent().resolve("R5-V-CORE-LABEL-COPY-ext-int-vsum\\src\\"));

				Collection<Resource> resources = this.parse(revisionVariantsLocation.resolve("V-CORE-LABEL-COPY\\src\\"));
				Variable<FeatureOption> variable = ExpressionFactory.eINSTANCE.createVariable();
				variable.setValue(Fcopy);
				this.internalize(vmp, vmp2, resources, variable);
				Files.move(vaveResourceLocation, vaveResourceLocation.getParent().resolve("R5-V-CORE-LABEL-COPY-int"));

				long timeDiff = System.currentTimeMillis() - timeStart;
				System.out.println("TOTAL TIME: " + timeDiff);
			}

			{ // sms
				long timeStart = System.currentTimeMillis();

				System.out.println("START REV 5 PROD 3");
				Configuration configuration = VaveFactory.eINSTANCE.createConfiguration();
				configuration.getOptions().add(Fcore.getFeatureRevisions().get(Fcore.getFeatureRevisions().size() - 1));
				configuration.getOptions().add(Flabel.getFeatureRevisions().get(Flabel.getFeatureRevisions().size() - 1));
				configuration.getOptions().add(Fcopy.getFeatureRevisions().get(Fcopy.getFeatureRevisions().size() - 1));
				configuration.getOptions().add(vave.getSystem().getSystemRevisions().get(vave.getSystem().getSystemRevisions().size() - 1));
				VirtualProductModel vmp = this.externalize(configuration, vaveResourceLocation.getParent().resolve("R5-V-CORE-LABEL-COPY-ext-vsum2\\src\\"));
				Files.move(vaveResourceLocation, vaveResourceLocation.getParent().resolve("R5-V-CORE-LABEL-COPY-ext2"));

				VirtualProductModel vmp2 = this.externalize(configuration, vaveResourceLocation.getParent().resolve("R5-V-CORE-LABEL-COPY-ext-int-vsum2\\src\\"));

				Collection<Resource> resources = this.parse(revisionVariantsLocation.resolve("V-CORE-LABEL-COPY-SMS\\src\\"));
				Variable<FeatureOption> variable = ExpressionFactory.eINSTANCE.createVariable();
				variable.setValue(Fsms);
				repairedFM = this.internalize(vmp, vmp2, resources, variable);
				Files.move(vaveResourceLocation, vaveResourceLocation.getParent().resolve("R5-V-CORE-LABEL-COPY-SMS-int"));

				long timeDiff = System.currentTimeMillis() - timeStart;
				System.out.println("TOTAL TIME: " + timeDiff);
			}

			// EVALUATION
			// type 6
			Path evalVariantsLocation = vaveResourceLocation.getParent().resolve("R5-eval-variants");
			Files.createDirectory(evalVariantsLocation);
			this.createEvalVariants(evalVariantsLocation);
			// type 5
			// TODO: check if the automatically added constraints match the actual constraints in the ground truth feature model of MM revision
			System.out.println("TYPE 5 EVAL 5:");
			String type5eval5string = this.outputEvalFeatureModel(repairedFM);
			Files.writeString(vaveResourceLocation.getParent().resolve("R5-eval-type5.txt"), "TYPE 5 EVAL 5:" + System.lineSeparator() + type5eval5string, StandardOpenOption.CREATE);
			if (repairedFM != null) {
				Collection<Feature[]> hints5 = ((HintComputation.Result) this.vave.internalizeDomain(repairedFM).getConsistencyResult(HintComputation.class)).getNewHints();
				this.printHints("5-end", hints5);
			}
		}

	}

	private String outputEvalFeatureModel(FeatureModel repairedFM) {
		StringBuffer sb = new StringBuffer();
		if (repairedFM == null)
			sb.append("FEATURE MODEL: NULL" + System.lineSeparator());
		else {
			sb.append("FEATURE MODEL:" + System.lineSeparator());
			sb.append("ROOT FEATURE: " + repairedFM.getRootFeatures().get(0) + System.lineSeparator());
			sb.append("TREE CONSTRAINTS:" + System.lineSeparator());
			for (ViewTreeConstraint tc : FeatureModelUtil.collectTreeConstraints(repairedFM)) {
				sb.append(tc.getParentFeature().getOriginalFeature().getName() + " -> " + tc.getType() + " -> {" + tc.getChildFeatures().stream().map(f -> f.getName()).collect(Collectors.joining(", ")) + "}" + System.lineSeparator());
			}
			sb.append("CROSS-TREE CONSTRAINTS:" + System.lineSeparator());
			for (ViewCrossTreeConstraint ctc : repairedFM.getCrossTreeConstraints()) {
				sb.append(ExpressionUtil.toString(ctc.getExpression()) + System.lineSeparator());
			}
		}
		String s = sb.toString();
		System.out.println(s);
		return s;
	}

	private void createEvalVariants(Path targetLocation) throws Exception {
		System.out.println("CREATING EVAL VARIANTS");

		SystemRevision latestSysRev = this.vave.getSystem().getSystemRevisions().get(this.vave.getSystem().getSystemRevisions().size() - 1);
		// optional features
		List<Feature> optionalFeatures = this.vave.getSystem().getFeatures().stream().filter(f -> !f.getName().equals("Core") && !f.getName().equals("Label")).collect(Collectors.toList());
		// core features
		Feature Fcore = this.vave.getSystem().getFeatures().stream().filter(f -> f.getName().equals("Core")).findFirst().get();
		FeatureRevision coreFeatureRev = Fcore.getFeatureRevisions().get(Fcore.getFeatureRevisions().size() - 1);
		Feature Flabel = null;
		FeatureRevision labelFeatureRev = null;
		Optional<Feature> FlabelOpt = this.vave.getSystem().getFeatures().stream().filter(f -> f.getName().equals("Label")).findFirst();
		if (FlabelOpt.isPresent()) {
			Flabel = FlabelOpt.get();
			labelFeatureRev = Flabel.getFeatureRevisions().get(Flabel.getFeatureRevisions().size() - 1);
		}

		// only core
		try {
			Configuration config = VaveFactory.eINSTANCE.createConfiguration();
			config.getOptions().add(latestSysRev);
			config.getOptions().add(coreFeatureRev);
			String fileName = "V-CORE";
			if (Flabel != null) {
				config.getOptions().add(labelFeatureRev);
				fileName += "-LABEL";
			}
			this.externalize(config, targetLocation.resolve(fileName + "-vsum"));
			Files.move(vaveResourceLocation, targetLocation.resolve(fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// single features
		if (optionalFeatures.size() >= 1) {
			for (Feature optionalFeature : optionalFeatures) {
				try {
					if (!optionalFeature.getName().equals("SMS")) {
						Configuration config = VaveFactory.eINSTANCE.createConfiguration();
						config.getOptions().add(latestSysRev);
						config.getOptions().add(coreFeatureRev);
						if (Flabel != null)
							config.getOptions().add(labelFeatureRev);
						config.getOptions().add(optionalFeature.getFeatureRevisions().get(optionalFeature.getFeatureRevisions().size() - 1));
						this.externalize(config, targetLocation.resolve("V-CORE-LABEL-" + optionalFeature.getName().toUpperCase() + "-vsum"));
						Files.move(vaveResourceLocation, targetLocation.resolve("V-CORE-LABEL-" + optionalFeature.getName().toUpperCase()));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		// pair-wise feature interactions
		if (optionalFeatures.size() >= 2) {
			for (int i = 0; i < optionalFeatures.size(); i++) {
				for (int j = i + 1; j < optionalFeatures.size(); j++) {
					try {
						if (!(optionalFeatures.get(i).getName().equals("SMS") && !optionalFeatures.get(j).getName().equals("Copy") || optionalFeatures.get(j).getName().equals("SMS") && !optionalFeatures.get(i).getName().equals("Copy"))) {
							Configuration config = VaveFactory.eINSTANCE.createConfiguration();
							config.getOptions().add(latestSysRev);
							config.getOptions().add(coreFeatureRev);
							if (Flabel != null)
								config.getOptions().add(labelFeatureRev);
							config.getOptions().add(optionalFeatures.get(i).getFeatureRevisions().get(optionalFeatures.get(i).getFeatureRevisions().size() - 1));
							config.getOptions().add(optionalFeatures.get(j).getFeatureRevisions().get(optionalFeatures.get(j).getFeatureRevisions().size() - 1));
							this.externalize(config, targetLocation.resolve("V-CORE-LABEL-" + optionalFeatures.get(i).getName().toUpperCase() + "-" + optionalFeatures.get(j).getName().toUpperCase() + "-vsum"));
							Files.move(vaveResourceLocation, targetLocation.resolve("V-CORE-LABEL-" + optionalFeatures.get(i).getName().toUpperCase() + "-" + optionalFeatures.get(j).getName().toUpperCase()));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		// three-wise feature interactions
		if (optionalFeatures.size() >= 3) {
			for (int i = 0; i < optionalFeatures.size(); i++) {
				for (int j = i + 1; j < optionalFeatures.size(); j++) {
					for (int k = j + 1; k < optionalFeatures.size(); k++) {
						try {
							List<String> optionalFeaturesList = Arrays.asList(optionalFeatures.get(i).getName(), optionalFeatures.get(j).getName(), optionalFeatures.get(k).getName());
							if (!(optionalFeaturesList.contains("SMS") && !optionalFeaturesList.contains("Copy"))) {
								Configuration config = VaveFactory.eINSTANCE.createConfiguration();
								config.getOptions().add(latestSysRev);
								config.getOptions().add(coreFeatureRev);
								if (Flabel != null)
									config.getOptions().add(labelFeatureRev);
								config.getOptions().add(optionalFeatures.get(i).getFeatureRevisions().get(optionalFeatures.get(i).getFeatureRevisions().size() - 1));
								config.getOptions().add(optionalFeatures.get(j).getFeatureRevisions().get(optionalFeatures.get(j).getFeatureRevisions().size() - 1));
								config.getOptions().add(optionalFeatures.get(k).getFeatureRevisions().get(optionalFeatures.get(k).getFeatureRevisions().size() - 1));
								this.externalize(config, targetLocation.resolve("V-CORE-LABEL-" + optionalFeatures.get(i).getName().toUpperCase() + "-" + optionalFeatures.get(j).getName().toUpperCase() + "-" + optionalFeatures.get(k).getName().toUpperCase() + "-vsum"));
								Files.move(vaveResourceLocation, targetLocation.resolve("V-CORE-LABEL-" + optionalFeatures.get(i).getName().toUpperCase() + "-" + optionalFeatures.get(j).getName().toUpperCase() + "-" + optionalFeatures.get(k).getName().toUpperCase()));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		// all features
		if (optionalFeatures.size() >= 4) {
			try {
				Configuration config = VaveFactory.eINSTANCE.createConfiguration();
				config.getOptions().add(latestSysRev);
				config.getOptions().add(coreFeatureRev);
				if (Flabel != null)
					config.getOptions().add(labelFeatureRev);
				config.getOptions().addAll(optionalFeatures.stream().map(f -> {
					return f.getFeatureRevisions().get(f.getFeatureRevisions().size() - 1);
				}).collect(Collectors.toList()));
				this.externalize(config, targetLocation.resolve("V-CORE-LABEL-" + optionalFeatures.stream().map(f -> f.getName().toUpperCase()).collect(Collectors.joining("-")) + "-vsum"));
				Files.move(vaveResourceLocation, targetLocation.resolve("V-CORE-LABEL-" + optionalFeatures.stream().map(f -> f.getName().toUpperCase()).collect(Collectors.joining("-"))));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
