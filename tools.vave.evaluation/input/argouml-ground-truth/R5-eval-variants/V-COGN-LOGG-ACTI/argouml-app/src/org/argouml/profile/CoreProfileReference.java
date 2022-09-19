package org.argouml.profile;

import java.net.MalformedURLException;
import java.net.URL;
import org.argouml.profile.ProfileReference;


public class CoreProfileReference extends ProfileReference {
	static final String PROFILES_RESOURCE_PATH = "/org/argouml/profile/profiles/uml14/";
	static final String PROFILES_BASE_URL = "http://argouml.org/profiles/uml14/";
	public CoreProfileReference(String fileName)throws MalformedURLException {
		super(PROFILES_RESOURCE_PATH + fileName,new URL(PROFILES_BASE_URL + fileName));
		assert fileName != null:"null isn\'t acceptable as the profile file name.";
		assert!"".equals(fileName):"the empty string isn\'t acceptable as the profile file name.";
	}
}



