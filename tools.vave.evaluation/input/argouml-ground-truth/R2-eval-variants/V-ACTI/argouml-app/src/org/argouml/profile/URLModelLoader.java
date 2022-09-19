package org.argouml.profile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.argouml.model.Model;
import org.argouml.model.UmlException;
import org.argouml.model.XmiReader;
import org.xml.sax.InputSource;
import org.argouml.profile.ProfileModelLoader;
import org.argouml.profile.ProfileException;
import org.argouml.profile.ProfileReference;


public class URLModelLoader implements ProfileModelLoader {
	public Collection loadModel(URL url,URL publicId)throws ProfileException {
		if (url == null) {
			throw new ProfileException("Null profile URL");
		}
		ZipInputStream zis = null;
		try {
			Collection elements = null;
			XmiReader xmiReader = Model.getXmiReader();
			if (url.getPath().toLowerCase().endsWith(".zip")) {
				zis = new ZipInputStream(url.openStream());
				ZipEntry entry = zis.getNextEntry();
				if (entry != null) {
					url = makeZipEntryUrl(url,entry.getName());
				}
				zis.close();
			}
			InputSource inputSource = new InputSource(url.toExternalForm());
			inputSource.setPublicId(publicId.toString());
			elements = xmiReader.parse(inputSource,true);
			return elements;
		}catch (UmlException e) {
			throw new ProfileException("Error loading profile XMI file ",e);
		}catch (IOException e) {
			throw new ProfileException("I/O error loading profile XMI ",e);
		}
	}
	public Collection loadModel(final ProfileReference reference)throws ProfileException {
		return loadModel(reference.getPublicReference(),reference.getPublicReference());
	}
	private URL makeZipEntryUrl(URL url,String entryName)throws MalformedURLException {
		String entryURL = "jar:" + url + "!/" + entryName;
		return new URL(entryURL);
	}
}



