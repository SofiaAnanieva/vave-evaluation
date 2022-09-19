package org.argouml.profile;

import java.util.Collection;
import org.argouml.profile.ProfileReference;
import org.argouml.profile.ProfileException;


public interface ProfileModelLoader {
	Collection loadModel(ProfileReference reference)throws ProfileException;
}



