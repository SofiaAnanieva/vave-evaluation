package org.argouml.profile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.log4j.Logger;
import org.argouml.profile.ProfileException;
import org.argouml.profile.ProfileReference;


public class ZipModelLoader extends StreamModelLoader {
	private static final Logger LOG = Logger.getLogger(ZipModelLoader.class);
	public Collection loadModel(ProfileReference reference)throws ProfileException {
		LOG.info("Loading profile from ZIP \'" + reference.getPath() + "\'");
		if (!reference.getPath().endsWith("zip")) {
			throw new ProfileException("Profile could not be loaded!");
		}
		InputStream is = null;
		File modelFile = new File(reference.getPath());
		String filename = modelFile.getName();
		String extension = filename.substring(filename.indexOf('.'),filename.lastIndexOf('.'));
		String path = modelFile.getParent();
		if (path != null) {
			System.setProperty("org.argouml.model.modules_search_path",path);
		}
		try {
			is = openZipStreamAt(modelFile.toURI().toURL(),extension);
		}catch (MalformedURLException e) {
			LOG.error("Exception while loading profile \'" + reference.getPath() + "\'",e);
			throw new ProfileException(e);
		}catch (IOException e) {
			LOG.error("Exception while loading profile \'" + reference.getPath() + "\'",e);
			throw new ProfileException(e);
		}
		if (is == null) {
			throw new ProfileException("Profile could not be loaded!");
		}
		return super.loadModel(is,reference.getPublicReference());
	}
	private ZipInputStream openZipStreamAt(URL url,String ext)throws IOException {
		ZipInputStream zis = new ZipInputStream(url.openStream());
		ZipEntry entry = zis.getNextEntry();
		while (entry != null&&!entry.getName().endsWith(ext)) {
			entry = zis.getNextEntry();
		}
		return zis;
	}
}



