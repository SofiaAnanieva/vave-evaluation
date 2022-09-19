package org.argouml.profile;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import org.argouml.profile.ProfileReference;


public class UserProfileReference extends ProfileReference {
	static final String DEFAULT_USER_PROFILE_BASE_URL = "http://argouml.org/user-profiles/";
	public UserProfileReference(String thePath,URL publicReference) {
		super(thePath,publicReference);
	}
	public UserProfileReference(String path)throws MalformedURLException {
		super(path,new URL(DEFAULT_USER_PROFILE_BASE_URL + new File(path).getName()));
	}
}



