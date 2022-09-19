package org.argouml.profile;

import java.io.Reader;
import java.util.Collection;
import org.argouml.model.Model;
import org.argouml.model.UmlException;
import org.argouml.model.XmiReader;
import org.xml.sax.InputSource;
import org.argouml.profile.ProfileReference;
import org.argouml.profile.ProfileException;
import org.argouml.profile.ProfileModelLoader;


public class ReaderModelLoader implements ProfileModelLoader {
	private Reader reader;
	public ReaderModelLoader(Reader theReader) {
		this.reader = theReader;
	}
	public Collection loadModel(ProfileReference reference)throws ProfileException {
		if (reader == null) {
			throw new ProfileException("Profile not found!");
		}
		try {
			XmiReader xmiReader = Model.getXmiReader();
			InputSource inputSource = new InputSource(reader);
			inputSource.setSystemId(reference.getPath());
			inputSource.setPublicId(reference.getPublicReference().toString());
			Collection elements = xmiReader.parse(inputSource,true);
			return elements;
		}catch (UmlException e) {
			throw new ProfileException("Invalid XMI data!",e);
		}
	}
}



