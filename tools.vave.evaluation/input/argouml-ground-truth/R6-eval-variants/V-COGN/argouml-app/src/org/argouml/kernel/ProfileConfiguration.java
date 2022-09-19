package org.argouml.kernel;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoProfileEvent;
import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;
import org.argouml.model.Model;
import org.argouml.profile.DefaultTypeStrategy;
import org.argouml.profile.FigNodeStrategy;
import org.argouml.profile.FormatingStrategy;
import org.argouml.profile.Profile;
import org.argouml.profile.ProfileException;
import org.argouml.profile.ProfileFacade;
import org.argouml.profile.ProfileManager;
import org.argouml.profile.Profile;


public class ProfileConfiguration extends AbstractProjectMember {
	private FormatingStrategy formatingStrategy;
	private DefaultTypeStrategy defaultTypeStrategy;
	private List figNodeStrategies = new ArrayList();
	private List<Profile>profiles = new ArrayList<Profile>();
	private List<Object>profileModels = new ArrayList<Object>();
	public static final String EXTENSION = "profile";
	public static final ConfigurationKey KEY_DEFAULT_STEREOTYPE_VIEW = Configuration.makeKey("profiles","stereotypeView");
	public ProfileConfiguration(Project project) {
		super(EXTENSION,project);
		for (Profile p:ProfileFacade.getManager().getDefaultProfiles()) {
			addProfile(p);
		}
		updateStrategies();
	}
	public ProfileConfiguration(Project project,Collection<Profile>configuredProfiles) {
		super(EXTENSION,project);
		for (Profile profile:configuredProfiles) {
			addProfile(profile);
		}
		updateStrategies();
	}
	private void updateStrategies() {
		for (Profile profile:profiles) {
			activateFormatingStrategy(profile);
			activateDefaultTypeStrategy(profile);
		}
	}
	public FormatingStrategy getFormatingStrategy() {
		return formatingStrategy;
	}
	public DefaultTypeStrategy getDefaultTypeStrategy() {
		return defaultTypeStrategy;
	}
	public void activateDefaultTypeStrategy(Profile profile) {
		if (profile != null&&profile.getDefaultTypeStrategy() != null&&getProfiles().contains(profile)) {
			this.defaultTypeStrategy = profile.getDefaultTypeStrategy();
		}
	}
	public void activateFormatingStrategy(Profile profile) {
		if (profile != null&&profile.getFormatingStrategy() != null&&getProfiles().contains(profile)) {
			this.formatingStrategy = profile.getFormatingStrategy();
		}
	}
	public List<Profile>getProfiles() {
		return profiles;
	}
	@SuppressWarnings("unchecked")public void addProfile(Profile p) {
		if (!profiles.contains(p)) {
			profiles.add(p);
			try {
				profileModels.addAll(p.getProfilePackages());
			}catch (ProfileException e) {
			}
			FigNodeStrategy fns = p.getFigureStrategy();
			if (fns != null) {
				figNodeStrategies.add(fns);
			}
			for (Profile dependency:p.getDependencies()) {
				addProfile(dependency);
			}
			updateStrategies();
			ArgoEventPump.fireEvent(new ArgoProfileEvent(ArgoEventTypes.PROFILE_ADDED,new PropertyChangeEvent(this,"profile",null,p)));
		}
	}
	private List getProfileModels() {
		return profileModels;
	}
	public void removeProfile(Profile p) {
		profiles.remove(p);
		try {
			profileModels.removeAll(p.getProfilePackages());
		}catch (ProfileException e) {
		}
		FigNodeStrategy fns = p.getFigureStrategy();
		if (fns != null) {
			figNodeStrategies.remove(fns);
		}
		if (formatingStrategy == p.getFormatingStrategy()) {
			formatingStrategy = null;
		}
		List<Profile>markForRemoval = new ArrayList<Profile>();
		for (Profile profile:profiles) {
			if (profile.getDependencies().contains(p)) {
				markForRemoval.add(profile);
			}
		}
		for (Profile profile:markForRemoval) {
			removeProfile(profile);
		}
		updateStrategies();
		ArgoEventPump.fireEvent(new ArgoProfileEvent(ArgoEventTypes.PROFILE_REMOVED,new PropertyChangeEvent(this,"profile",p,null)));
	}
	private FigNodeStrategy compositeFigNodeStrategy = new FigNodeStrategy() {
	public Image getIconForStereotype(Object element) {
		Iterator it = figNodeStrategies.iterator();
		while (it.hasNext()) {
			FigNodeStrategy strat = (FigNodeStrategy) it.next();
			Image extra = strat.getIconForStereotype(element);
			if (extra != null) {
				return extra;
			}
		}
		return null;
	}
};
	public FigNodeStrategy getFigNodeStrategy() {
		return compositeFigNodeStrategy;
	}
	public String getType() {
		return EXTENSION;
	}
	public String repair() {
		return"";
	}
	@Override public String toString() {
		return"Profile Configuration";
	}
	public Object findStereotypeForObject(String name,Object element) {
		Iterator iter = null;
		for (Object model:profileModels) {
			iter = Model.getFacade().getOwnedElements(model).iterator();
			while (iter.hasNext()) {
				Object stereo = iter.next();
				if (!Model.getFacade().isAStereotype(stereo)||!name.equals(Model.getFacade().getName(stereo))) {
					continue;
				}
				if (Model.getExtensionMechanismsHelper().isValidStereotype(element,stereo)) {
					return stereo;
				}
			}
		}
		return null;
	}
	public Object findType(String name) {
		for (Object model:getProfileModels()) {
			Object result = findTypeInModel(name,model);
			if (result != null) {
				return result;
			}
		}
		return null;
	}
	public static Object findTypeInModel(String s,Object model) {
		if (!Model.getFacade().isANamespace(model)) {
			throw new IllegalArgumentException("Looking for the classifier " + s + " in a non-namespace object of " + model + ". A namespace was expected.");
		}
		Collection allClassifiers = Model.getModelManagementHelper().getAllModelElementsOfKind(model,Model.getMetaTypes().getClassifier());
		Object[]classifiers = allClassifiers.toArray();
		Object classifier = null;
		for (int i = 0;i < classifiers.;i++) {
			classifier = classifiers[i];
			if (Model.getFacade().getName(classifier) != null&&Model.getFacade().getName(classifier).equals(s)) {
				return classifier;
			}
		}
		return null;
	}
	@SuppressWarnings("unchecked")public Collection findByMetaType(Object metaType) {
		Set elements = new HashSet();
		Iterator it = getProfileModels().iterator();
		while (it.hasNext()) {
			Object model = it.next();
			elements.addAll(Model.getModelManagementHelper().getAllModelElementsOfKind(model,metaType));
		}
		return elements;
	}
	public Collection findAllStereotypesForModelElement(Object modelElement) {
		return Model.getExtensionMechanismsHelper().getAllPossibleStereotypes(getProfileModels(),modelElement);
	}
}



