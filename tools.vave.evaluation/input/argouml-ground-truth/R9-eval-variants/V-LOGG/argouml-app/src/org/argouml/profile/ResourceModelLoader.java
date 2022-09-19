package org.argouml.profile;

import java.util.Collection;
import org.apache.log4j.Logger;
import org.argouml.profile.ProfileReference;
import org.argouml.profile.ProfileException;


public class ResourceModelLoader extends URLModelLoader {
	private static final Logger LOG = Logger.getLogger(ResourceModelLoader.class);
	private Class clazz;
	public ResourceModelLoader() {
		this.clazz = this.getClass();
	}
	public ResourceModelLoader(Class c) {
		clazz = c;
	}
	public Collection loadModel(ProfileReference reference)throws ProfileException {
		LOG.info("Loading profile from resource\'" + reference.getPath() + "\'");
		return super.loadModel(clazz.getResource(reference.getPath()),reference.getPublicReference());
	}
}



