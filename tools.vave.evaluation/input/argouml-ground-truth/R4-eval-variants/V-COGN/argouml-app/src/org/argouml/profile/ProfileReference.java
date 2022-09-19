package org.argouml.profile;

import java.io.File;
import java.net.URL;


public class ProfileReference {
	private String path;
	private URL url;
	public ProfileReference(String thePath,URL publicReference) {
		File file = new File(thePath);
		File fileFromPublicReference = new File(publicReference.getPath());
		assert file.getName().equals(fileFromPublicReference.getName()):"File name in path and in publicReference are different.";
		path = thePath;
		url = publicReference;
	}
	public String getPath() {
		return path;
	}
	public URL getPublicReference() {
		return url;
	}
}



