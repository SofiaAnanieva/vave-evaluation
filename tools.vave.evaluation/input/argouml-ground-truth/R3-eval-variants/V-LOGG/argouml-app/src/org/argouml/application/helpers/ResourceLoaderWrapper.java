package org.argouml.application.helpers;

import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.model.DataTypesHelper;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;


public final class ResourceLoaderWrapper {
	private static final Logger LOG = Logger.getLogger(ResourceLoaderWrapper.class);
	private static ImageIcon initialStateIcon;
	private static ImageIcon deepIcon;
	private static ImageIcon shallowIcon;
	private static ImageIcon forkIcon;
	private static ImageIcon joinIcon;
	private static ImageIcon branchIcon;
	private static ImageIcon junctionIcon;
	private static ImageIcon realizeIcon;
	private static ImageIcon signalIcon;
	private static ImageIcon exceptionIcon;
	private static ImageIcon commentIcon;
	private Hashtable<Class,Icon>iconCache = new Hashtable<Class,Icon>();
	private static ResourceLoaderWrapper instance = new ResourceLoaderWrapper();
	public static ResourceLoaderWrapper getInstance() {
		return instance;
	}
	private ResourceLoaderWrapper() {
		initResourceLoader();
	}
	private static String lookAndFeelPath(String classname,String element) {
		return"/org/argouml/Images/plaf/" + classname.replace('.','/') + "/toolbarButtonGraphics/" + element;
	}
	private static void initResourceLoader() {
		String lookAndFeelClassName;
		if ("true".equals(System.getProperty("force.nativelaf","false"))) {
			lookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
		}else {
			lookAndFeelClassName = "javax.swing.plaf.metal.MetalLookAndFeel";
		}
		String lookAndFeelGeneralImagePath = lookAndFeelPath(lookAndFeelClassName,"general");
		String lookAndFeelNavigationImagePath = lookAndFeelPath(lookAndFeelClassName,"navigation");
		String lookAndFeelDiagramImagePath = lookAndFeelPath(lookAndFeelClassName,"argouml/diagrams");
		String lookAndFeelElementImagePath = lookAndFeelPath(lookAndFeelClassName,"argouml/elements");
		String lookAndFeelArgoUmlImagePath = lookAndFeelPath(lookAndFeelClassName,"argouml");
		ResourceLoader.addResourceExtension("gif");
		ResourceLoader.addResourceExtension("png");
		ResourceLoader.addResourceLocation(lookAndFeelGeneralImagePath);
		ResourceLoader.addResourceLocation(lookAndFeelNavigationImagePath);
		ResourceLoader.addResourceLocation(lookAndFeelDiagramImagePath);
		ResourceLoader.addResourceLocation(lookAndFeelElementImagePath);
		ResourceLoader.addResourceLocation(lookAndFeelArgoUmlImagePath);
		ResourceLoader.addResourceLocation("/org/argouml/Images");
		ResourceLoader.addResourceLocation("/org/tigris/gef/Images");
		org.tigris.gef.util.ResourceLoader.addResourceExtension("gif");
		org.tigris.gef.util.ResourceLoader.addResourceExtension("png");
		org.tigris.gef.util.ResourceLoader.addResourceLocation(lookAndFeelGeneralImagePath);
		org.tigris.gef.util.ResourceLoader.addResourceLocation(lookAndFeelNavigationImagePath);
		org.tigris.gef.util.ResourceLoader.addResourceLocation(lookAndFeelDiagramImagePath);
		org.tigris.gef.util.ResourceLoader.addResourceLocation(lookAndFeelElementImagePath);
		org.tigris.gef.util.ResourceLoader.addResourceLocation(lookAndFeelArgoUmlImagePath);
		org.tigris.gef.util.ResourceLoader.addResourceLocation("/org/argouml/Images");
		org.tigris.gef.util.ResourceLoader.addResourceLocation("/org/tigris/gef/Images");
		initialStateIcon = ResourceLoader.lookupIconResource("Initial");
		deepIcon = ResourceLoader.lookupIconResource("DeepHistory");
		shallowIcon = ResourceLoader.lookupIconResource("ShallowHistory");
		forkIcon = ResourceLoader.lookupIconResource("Fork");
		joinIcon = ResourceLoader.lookupIconResource("Join");
		branchIcon = ResourceLoader.lookupIconResource("Choice");
		junctionIcon = ResourceLoader.lookupIconResource("Junction");
		realizeIcon = ResourceLoader.lookupIconResource("Realization");
		signalIcon = ResourceLoader.lookupIconResource("SignalSending");
		exceptionIcon = ResourceLoader.lookupIconResource("Exception");
		commentIcon = ResourceLoader.lookupIconResource("Note");
	}
	public static void addResourceLocation(String location) {
		ResourceLoader.addResourceLocation(location);
	}
	public static ImageIcon lookupIconResource(String resource) {
		return ResourceLoader.lookupIconResource(resource);
	}
	public static ImageIcon lookupIconResource(String resource,String desc) {
		return ResourceLoader.lookupIconResource(resource,desc);
	}
	public static ImageIcon lookupIcon(String key) {
		return lookupIconResource(getImageBinding(key),Translator.localize(key));
	}
	public Icon lookupIcon(Object value) {
		if (value == null) {
			throw new IllegalArgumentException("Attempted to get an icon given a null key");
		}
		if (value instanceof String) {
			return null;
		}
		Icon icon = iconCache.get(value.getClass());
		try {
			if (Model.getFacade().isAPseudostate(value)) {
				Object kind = Model.getFacade().getKind(value);
				DataTypesHelper helper = Model.getDataTypesHelper();
				if (helper.equalsINITIALKind(kind)) {
					icon = initialStateIcon;
				}
				if (helper.equalsDeepHistoryKind(kind)) {
					icon = deepIcon;
				}
				if (helper.equalsShallowHistoryKind(kind)) {
					icon = shallowIcon;
				}
				if (helper.equalsFORKKind(kind)) {
					icon = forkIcon;
				}
				if (helper.equalsJOINKind(kind)) {
					icon = joinIcon;
				}
				if (helper.equalsCHOICEKind(kind)) {
					icon = branchIcon;
				}
				if (helper.equalsJUNCTIONKind(kind)) {
					icon = junctionIcon;
				}
			}
			if (Model.getFacade().isAAbstraction(value)) {
				icon = realizeIcon;
			}
			if (Model.getFacade().isAException(value)) {
				icon = exceptionIcon;
			}else {
				if (Model.getFacade().isASignal(value)) {
					icon = signalIcon;
				}
			}
			if (Model.getFacade().isAComment(value)) {
				icon = commentIcon;
			}
			if (icon == null) {
				String cName = Model.getMetaTypes().getName(value);
				icon = lookupIconResource(cName);
				if (icon == null) {
					LOG.debug("Can\'t find icon for " + cName);
				}else {
					synchronized (iconCache) {
						iconCache.put(value.getClass(),icon);
					}
				}
			}
		}catch (InvalidElementException e) {
			LOG.debug("Attempted to get icon for deleted element");
			return null;
		}
		return icon;
	}
	private static Map<String,String>images = new HashMap<String,String>();
	static {
	images.put("action.about-argouml","AboutArgoUML");
	images.put("action.activity-diagram","Activity Diagram");
	images.put("action.class-diagram","Class Diagram");
	images.put("action.collaboration-diagram","Collaboration Diagram");
	images.put("action.deployment-diagram","Deployment Diagram");
	images.put("action.sequence-diagram","Sequence Diagram");
	images.put("action.state-diagram","State Diagram");
	images.put("action.usecase-diagram","Use Case Diagram");
}
	static {
	images.put("action.add-concurrent-region","Add Concurrent Region");
	images.put("action.add-message","Add Message");
	images.put("action.configure-perspectives","ConfigurePerspectives");
	images.put("action.copy","Copy");
	images.put("action.cut","Cut");
	images.put("action.delete-concurrent-region","DeleteConcurrentRegion");
	images.put("action.delete-from-model","DeleteFromModel");
	images.put("action.find","Find...");
	images.put("action.import-sources","Import Sources...");
	images.put("action.more-info","More Info...");
	images.put("action.navigate-back","Navigate Back");
	images.put("action.navigate-forward","Navigate Forward");
	images.put("action.new","New");
	images.put("action.new-todo-item","New To Do Item...");
	images.put("action.open-project","Open Project...");
	images.put("action.page-setup","Page Setup...");
	images.put("action.paste","Paste");
	images.put("action.print","Print...");
	images.put("action.properties","Properties");
	images.put("action.remove-from-diagram","Remove From Diagram");
	images.put("action.resolve-item","Resolve Item...");
	images.put("action.save-project","Save Project");
	images.put("action.save-project-as","Save Project As...");
	images.put("action.settings","Settings...");
	images.put("action.snooze-critic","Snooze Critic");
	images.put("action.system-information","System Information");
}
	static {
	images.put("button.broom","Broom");
	images.put("button.new-actionstate","ActionState");
	images.put("button.new-actor","Actor");
	images.put("button.new-aggregation","Aggregation");
	images.put("button.new-association","Association");
	images.put("button.new-associationclass","AssociationClass");
	images.put("button.new-association-end","AssociationEnd");
	images.put("button.new-associationrole","AssociationRole");
	images.put("button.new-attribute","New Attribute");
	images.put("button.new-callaction","CallAction");
	images.put("button.new-callstate","CallState");
	images.put("button.new-choice","Choice");
	images.put("button.new-class","Class");
	images.put("button.new-classifierrole","ClassifierRole");
	images.put("button.new-commentlink","CommentLink");
	images.put("button.new-component","Component");
	images.put("button.new-componentinstance","ComponentInstance");
	images.put("button.new-compositestate","CompositeState");
	images.put("button.new-composition","Composition");
	images.put("button.new-createaction","CreateAction");
	images.put("button.new-datatype","DataType");
	images.put("button.new-deephistory","DeepHistory");
	images.put("button.new-dependency","Dependency");
	images.put("button.new-destroyaction","DestroyAction");
	images.put("button.new-enumeration","Enumeration");
	images.put("button.new-enumeration-literal","EnumerationLiteral");
	images.put("button.new-extension-point","New Extension Point");
	images.put("button.new-extend","Extend");
	images.put("button.new-exception","Exception");
}
	static {
	images.put("button.new-finalstate","FinalState");
	images.put("button.new-fork","Fork");
	images.put("button.new-generalization","Generalization");
	images.put("button.new-include","Include");
	images.put("button.new-initial","Initial");
}
	static {
	images.put("button.new-inner-class","Inner Class");
	images.put("button.new-interface","Interface");
	images.put("button.new-join","Join");
	images.put("button.new-junction","Junction");
	images.put("button.new-link","Link");
	images.put("button.new-node","Node");
	images.put("button.new-nodeinstance","NodeInstance");
	images.put("button.new-object","Object");
	images.put("button.new-objectflowstate","ObjectFlowState");
}
	static {
	images.put("button.new-operation","New Operation");
	images.put("button.new-package","Package");
	images.put("button.new-parameter","New Parameter");
	images.put("button.new-partition","Partition");
	images.put("button.new-permission","Permission");
	images.put("button.new-raised-signal","New Raised Signal");
	images.put("button.new-reception","New Reception");
	images.put("button.new-realization","Realization");
	images.put("button.new-returnaction","ReturnAction");
	images.put("button.new-sendaction","SendAction");
	images.put("button.new-shallowhistory","ShallowHistory");
	images.put("button.new-signal","Signal");
	images.put("button.new-simplestate","SimpleState");
	images.put("button.new-stereotype","Stereotype");
	images.put("button.new-stubstate","StubState");
	images.put("button.new-subactivitystate","SubactivityState");
	images.put("button.new-submachinestate","SubmachineState");
	images.put("button.new-synchstate","SynchState");
	images.put("button.new-tagdefinition","TagDefinition");
	images.put("button.new-transition","Transition");
	images.put("button.new-uniaggregation","UniAggregation");
	images.put("button.new-uniassociation","UniAssociation");
	images.put("button.new-unicomposition","UniComposition");
	images.put("button.new-usage","Usage");
	images.put("button.new-usecase","UseCase");
}
	static {
	images.put("button.select","Select");
	images.put("button.sequence-expand","SequenceExpand");
	images.put("button.sequence-contract","SequenceContract");
}
	public static String getImageBinding(String name) {
		String found = images.get(name);
		if (found == null) {
			return name;
		}
		return found;
	}
	public static URL lookupIconUrl(String name,ClassLoader loader) {
		return ResourceLoader.lookupIconUrl(name,loader);
	}
	public static URL lookupIconUrl(String name) {
		return lookupIconUrl(name,null);
	}
}



