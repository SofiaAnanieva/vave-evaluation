package org.argouml.profile.internal;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.argouml.model.Model;
import org.argouml.profile.CoreProfileReference;
import org.argouml.profile.DefaultTypeStrategy;
import org.argouml.profile.FormatingStrategy;
import org.argouml.profile.Profile;
import org.argouml.profile.ProfileException;
import org.argouml.profile.ProfileModelLoader;
import org.argouml.profile.ProfileReference;
import org.argouml.profile.ResourceModelLoader;
import org.argouml.profile.internal.ocl.InvalidOclException;


public class ProfileUML extends Profile {
	private static final String PROFILE_FILE = "default-uml14.xmi";
	static final String NAME = "UML 1.4";
	private FormatingStrategy formatingStrategy;
	private ProfileModelLoader profileModelLoader;
	private Collection model;
	@SuppressWarnings("unchecked")ProfileUML()throws ProfileException {
		formatingStrategy = new JavaFormatingStrategy();
		profileModelLoader = new ResourceModelLoader();
		ProfileReference profileReference = null;
		try {
			profileReference = new CoreProfileReference(PROFILE_FILE);
		}catch (MalformedURLException e) {
			throw new ProfileException("Exception while creating profile reference.",e);
		}
		model = profileModelLoader.loadModel(profileReference);
		if (model == null) {
			model = new ArrayList();
			model.add(Model.getModelManagementFactory().createModel());
		}
	}
	@Override public FormatingStrategy getFormatingStrategy() {
		return formatingStrategy;
	}
	@Override public String getDisplayName() {
		return NAME;
	}
	@Override public Collection getProfilePackages() {
		return model;
	}
	@Override public DefaultTypeStrategy getDefaultTypeStrategy() {
		return new DefaultTypeStrategy() {
			public Object getDefaultAttributeType() {
				return ModelUtils.findTypeInModel("Integer",model.iterator().next());
			}
			public Object getDefaultParameterType() {
				return ModelUtils.findTypeInModel("Integer",model.iterator().next());
			}
			public Object getDefaultReturnType() {
				return null;
			}
		};
	}
}



