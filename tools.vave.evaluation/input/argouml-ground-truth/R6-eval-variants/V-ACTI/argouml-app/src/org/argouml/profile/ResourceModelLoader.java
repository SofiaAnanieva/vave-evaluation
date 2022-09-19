package org.argouml.profile;

import java.util.Collection;
import org.argouml.profile.ProfileReference;
import org.argouml.profile.ProfileException;


public class ResourceModelLoader extends URLModelLoader {
	private Class clazz;
	public ResourceModelLoader() {
		this.clazz = this.getClass();
	}
	public ResourceModelLoader(Class c) {
		clazz = c;
	}
	public Collection loadModel(ProfileReference reference)throws ProfileException {
		return super.loadModel(clazz.getResource(reference.getPath()),reference.getPublicReference());
	}
}



