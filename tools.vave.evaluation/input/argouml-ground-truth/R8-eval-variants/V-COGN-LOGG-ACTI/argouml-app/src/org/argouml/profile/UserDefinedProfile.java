package org.argouml.profile;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import javax.swing.ImageIcon;
import org.apache.log4j.Logger;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Decision;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.Translator;
import org.argouml.model.Model;
import org.argouml.profile.internal.ocl.CrOCL;
import org.argouml.profile.internal.ocl.InvalidOclException;
import org.argouml.uml.cognitive.UMLDecision;
import org.argouml.profile.ProfileFacade;
import org.argouml.profile.ProfileException;
import org.argouml.profile.ProfileReference;
import org.argouml.profile.Profile;


public class UserDefinedProfile extends Profile {
	private static final Logger LOG = Logger.getLogger(UserDefinedProfile.class);
	private String displayName;
	private File modelFile;
	private Collection profilePackages;
	private UserDefinedFigNodeStrategy figNodeStrategy = new UserDefinedFigNodeStrategy();
	private class UserDefinedFigNodeStrategy implements FigNodeStrategy {
	private Map<String,Image>images = new HashMap<String,Image>();
	public Image getIconForStereotype(Object stereotype) {
		return images.get(Model.getFacade().getName(stereotype));
	}
	public void addDesrciptor(FigNodeDescriptor fnd) {
		images.put(fnd.stereotype,fnd.img);
	}
}
	private class FigNodeDescriptor {
	private String stereotype;
	private Image img;
	private String src;
	private int length;
	public boolean isValid() {
		return stereotype != null&&src != null&&length > 0;
	}
}
	public UserDefinedProfile(File file)throws ProfileException {
		LOG.info("load " + file);
		displayName = file.getName();
		modelFile = file;
		ProfileReference reference = null;
		try {
			reference = new UserProfileReference(file.getPath());
		}catch (MalformedURLException e) {
			throw new ProfileException("Failed to create the ProfileReference.",e);
		}
		profilePackages = new FileModelLoader().loadModel(reference);
		finishLoading();
	}
	public UserDefinedProfile(URL url)throws ProfileException {
		LOG.info("load " + url);
		ProfileReference reference = null;
		reference = new UserProfileReference(url.getPath(),url);
		profilePackages = new URLModelLoader().loadModel(reference);
		finishLoading();
	}
	public UserDefinedProfile(String dn,URL url,Set<Critic>critics,Set<String>dependencies)throws ProfileException {
		LOG.info("load " + url);
		this.displayName = dn;
		if (url != null) {
			ProfileReference reference = null;
			reference = new UserProfileReference(url.getPath(),url);
			profilePackages = new URLModelLoader().loadModel(reference);
		}else {
			profilePackages = new ArrayList(0);
		}
		this.setCritics(critics);
		for (String profileID:dependencies) {
			addProfileDependency(profileID);
		}
		finishLoading();
	}
	private void finishLoading() {
		Collection packagesInProfile = filterPackages();
		for (Object obj:packagesInProfile) {
			if (Model.getFacade().isAModelElement(obj)&&(Model.getExtensionMechanismsHelper().hasStereotype(obj,"profile")||(packagesInProfile.size() == 1))) {
				String name = Model.getFacade().getName(obj);
				if (name != null) {
					displayName = name;
				}else {
					if (displayName == null) {
						displayName = Translator.localize("misc.profile.unnamed");
					}
				}
				LOG.info("profile " + displayName);
				String dependencyListStr = Model.getFacade().getTaggedValueValue(obj,"Dependency");
				StringTokenizer st = new StringTokenizer(dependencyListStr," ,;:");
				String profile = null;
				while (st.hasMoreTokens()) {
					profile = st.nextToken();
					if (profile != null) {
						LOG.debug("AddingDependency " + profile);
						this.addProfileDependency(ProfileFacade.getManager().lookForRegisteredProfile(profile));
					}
				}
			}
		}
		Collection allStereotypes = Model.getExtensionMechanismsHelper().getStereotypes(packagesInProfile);
		for (Object stereotype:allStereotypes) {
			Collection tags = Model.getFacade().getTaggedValuesCollection(stereotype);
			for (Object tag:tags) {
				String tagName = Model.getFacade().getTag(tag);
				if (tagName == null) {
					LOG.debug("profile package with stereotype " + Model.getFacade().getName(stereotype) + " contains a null tag definition");
				}else if (tagName.toLowerCase().equals("figure")) {
					LOG.debug("AddFigNode " + Model.getFacade().getName(stereotype));
					String value = Model.getFacade().getValueOfTag(tag);
					File f = new File(value);
					FigNodeDescriptor fnd = null;
					try {
						fnd = loadImage(Model.getFacade().getName(stereotype).toString(),f);
						figNodeStrategy.addDesrciptor(fnd);
					}catch (IOException e) {
						LOG.error("Error loading FigNode",e);
					}
				}
			}
		}
		Set<Critic>myCritics = this.getCritics();
		myCritics.addAll(getAllCritiquesInModel());
		this.setCritics(myCritics);
	}
	private Collection filterPackages() {
		Collection ret = new ArrayList();
		for (Object object:profilePackages) {
			if (Model.getFacade().isAPackage(object)) {
				ret.add(object);
			}
		}
		return ret;
	}
	private CrOCL generateCriticFromComment(Object critique) {
		String ocl = "" + Model.getFacade().getBody(critique);
		String headline = null;
		String description = null;
		int priority = ToDoItem.HIGH_PRIORITY;
		List<Decision>supportedDecisions = new ArrayList<Decision>();
		List<String>knowledgeTypes = new ArrayList<String>();
		String moreInfoURL = null;
		Collection tags = Model.getFacade().getTaggedValuesCollection(critique);
		boolean i18nFound = false;
		for (Object tag:tags) {
			if (Model.getFacade().getTag(tag).toLowerCase().equals("i18n")) {
				i18nFound = true;
				String i18nSource = Model.getFacade().getValueOfTag(tag);
				headline = Translator.localize(i18nSource + "-head");
				description = Translator.localize(i18nSource + "-desc");
				moreInfoURL = Translator.localize(i18nSource + "-moreInfoURL");
			}else if (!i18nFound&&Model.getFacade().getTag(tag).toLowerCase().equals("headline")) {
				headline = Model.getFacade().getValueOfTag(tag);
			}else if (!i18nFound&&Model.getFacade().getTag(tag).toLowerCase().equals("description")) {
				description = Model.getFacade().getValueOfTag(tag);
			}else if (Model.getFacade().getTag(tag).toLowerCase().equals("priority")) {
				priority = str2Priority(Model.getFacade().getValueOfTag(tag));
			}else if (Model.getFacade().getTag(tag).toLowerCase().equals("supporteddecision")) {
				String decStr = Model.getFacade().getValueOfTag(tag);
				StringTokenizer st = new StringTokenizer(decStr,",;:");
				while (st.hasMoreTokens()) {
					Decision decision = str2Decision(st.nextToken().trim().toLowerCase());
					if (decision != null) {
						supportedDecisions.add(decision);
					}
				}
			}else if (Model.getFacade().getTag(tag).toLowerCase().equals("knowledgetype")) {
				String ktStr = Model.getFacade().getValueOfTag(tag);
				StringTokenizer st = new StringTokenizer(ktStr,",;:");
				while (st.hasMoreTokens()) {
					String knowledge = str2KnowledgeType(st.nextToken().trim().toLowerCase());
					if (knowledge != null) {
						knowledgeTypes.add(knowledge);
					}
				}
			}else if (!i18nFound&&Model.getFacade().getTag(tag).toLowerCase().equals("moreinfourl")) {
				moreInfoURL = Model.getFacade().getValueOfTag(tag);
			}
		}
		LOG.debug("OCL-Critic: " + ocl);
		try {
			return new CrOCL(ocl,headline,description,priority,supportedDecisions,knowledgeTypes,moreInfoURL);
		}catch (InvalidOclException e) {
			LOG.error("Invalid OCL in XMI!",e);
			return null;
		}
	}
	private String str2KnowledgeType(String token) {
		String knowledge = null;
		if (token.equals("completeness")) {
			knowledge = Critic.KT_COMPLETENESS;
		}
		if (token.equals("consistency")) {
			knowledge = Critic.KT_CONSISTENCY;
		}
		if (token.equals("correctness")) {
			knowledge = Critic.KT_CORRECTNESS;
		}
		if (token.equals("designers")) {
			knowledge = Critic.KT_DESIGNERS;
		}
		if (token.equals("experiencial")) {
			knowledge = Critic.KT_EXPERIENCIAL;
		}
		if (token.equals("optimization")) {
			knowledge = Critic.KT_OPTIMIZATION;
		}
		if (token.equals("organizational")) {
			knowledge = Critic.KT_ORGANIZATIONAL;
		}
		if (token.equals("presentation")) {
			knowledge = Critic.KT_PRESENTATION;
		}
		if (token.equals("semantics")) {
			knowledge = Critic.KT_SEMANTICS;
		}
		if (token.equals("syntax")) {
			knowledge = Critic.KT_SYNTAX;
		}
		if (token.equals("tool")) {
			knowledge = Critic.KT_TOOL;
		}
		return knowledge;
	}
	private int str2Priority(String prioStr) {
		int prio = ToDoItem.MED_PRIORITY;
		if (prioStr.toLowerCase().equals("high")) {
			prio = ToDoItem.HIGH_PRIORITY;
		}else if (prioStr.toLowerCase().equals("med")) {
			prio = ToDoItem.MED_PRIORITY;
		}else if (prioStr.toLowerCase().equals("low")) {
			prio = ToDoItem.LOW_PRIORITY;
		}else if (prioStr.toLowerCase().equals("interruptive")) {
			prio = ToDoItem.INTERRUPTIVE_PRIORITY;
		}
		return prio;
	}
	private Decision str2Decision(String token) {
		Decision decision = null;
		if (token.equals("behavior")) {
			decision = UMLDecision.BEHAVIOR;
		}
		if (token.equals("containment")) {
			decision = UMLDecision.CONTAINMENT;
		}
		if (token.equals("classselection")) {
			decision = UMLDecision.CLASS_SELECTION;
		}
		if (token.equals("codegen")) {
			decision = UMLDecision.CODE_GEN;
		}
		if (token.equals("expectedusage")) {
			decision = UMLDecision.EXPECTED_USAGE;
		}
		if (token.equals("inheritance")) {
			decision = UMLDecision.INHERITANCE;
		}
		if (token.equals("instantiation")) {
			decision = UMLDecision.INSTANCIATION;
		}
		if (token.equals("methods")) {
			decision = UMLDecision.METHODS;
		}
		if (token.equals("modularity")) {
			decision = UMLDecision.MODULARITY;
		}
		if (token.equals("naming")) {
			decision = UMLDecision.NAMING;
		}
		if (token.equals("patterns")) {
			decision = UMLDecision.PATTERNS;
		}
		if (token.equals("plannedextensions")) {
			decision = UMLDecision.PLANNED_EXTENSIONS;
		}
		if (token.equals("relationships")) {
			decision = UMLDecision.RELATIONSHIPS;
		}
		if (token.equals("statemachines")) {
			decision = UMLDecision.STATE_MACHINES;
		}
		if (token.equals("stereotypes")) {
			decision = UMLDecision.STEREOTYPES;
		}
		if (token.equals("storage")) {
			decision = UMLDecision.STORAGE;
		}
		return decision;
	}
	private List<CrOCL>getAllCritiquesInModel() {
		List<CrOCL>ret = new ArrayList<CrOCL>();
		Collection<Object>comments = getAllCommentsInModel(profilePackages);
		for (Object comment:comments) {
			if (Model.getExtensionMechanismsHelper().hasStereotype(comment,"Critic")) {
				CrOCL cr = generateCriticFromComment(comment);
				if (cr != null) {
					ret.add(cr);
				}
			}
		}
		return ret;
	}
	@SuppressWarnings("unchecked")private Collection<Object>getAllCommentsInModel(Collection objs) {
		Collection<Object>col = new ArrayList<Object>();
		for (Object obj:objs) {
			if (Model.getFacade().isAComment(obj)) {
				col.add(obj);
			}else if (Model.getFacade().isANamespace(obj)) {
				Collection contents = Model.getModelManagementHelper().getAllContents(obj);
				if (contents != null) {
					col.addAll(contents);
				}
			}
		}
		return col;
	}
	public String getDisplayName() {
		return displayName;
	}
	@Override public FormatingStrategy getFormatingStrategy() {
		return null;
	}
	@Override public FigNodeStrategy getFigureStrategy() {
		return figNodeStrategy;
	}
	public File getModelFile() {
		return modelFile;
	}
	@Override public String toString() {
		File str = getModelFile();
		return super.toString() + (str != null?" [" + str + "]":"");
	}
	@Override public Collection getProfilePackages() {
		return profilePackages;
	}
	private FigNodeDescriptor loadImage(String stereotype,File f)throws IOException {
		FigNodeDescriptor descriptor = new FigNodeDescriptor();
		descriptor.length = (int) f.length();
		descriptor.src = f.getPath();
		descriptor.stereotype = stereotype;
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
		byte[]buf = new byte[descriptor.length];
		try {
			bis.read(buf);
		}catch (IOException e) {
			e.printStackTrace();
		}
		descriptor.img = new ImageIcon(buf).getImage();
		return descriptor;
	}
}



