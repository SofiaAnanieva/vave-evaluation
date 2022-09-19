package org.argouml.model.mdr;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jmi.reflect.RefObject;
import javax.jmi.reflect.RefPackage;
import org.netbeans.api.xmi.XMIInputConfig;
import org.netbeans.lib.jmi.util.DebugException;
import org.netbeans.lib.jmi.xmi.XmiContext;
import org.omg.uml.foundation.core.ModelElement;
import org.argouml.model.mdr.XmiReferenceException;


class XmiReferenceResolverImpl extends XmiContext {
	private Map<String,Object>idToObjects = Collections.synchronizedMap(new HashMap<String,Object>());
	private Map<String,XmiReference>objectsToId;
	private String topSystemId;
	private URI baseUri;
	private List<String>modulesPath = new ArrayList<String>();
	private Map<String,URL>urlMap = new HashMap<String,URL>();
	private Map<String,String>reverseUrlMap = new HashMap<String,String>();
	private boolean profile;
	private Map<String,String>public2SystemIds;
	private String modelPublicId;
	XmiReferenceResolverImpl(RefPackage[]extents,XMIInputConfig config,Map<String,XmiReference>objectToIdMap,Map<String,String>publicIds,List<String>searchDirs,boolean isProfile,String publicId,String systemId) {
			super(extents,config);
			objectsToId = objectToIdMap;
			modulesPath = searchDirs;
			profile = isProfile;
			public2SystemIds = publicIds;
			modelPublicId = publicId;
			if (isProfile) {
				if (public2SystemIds.containsKey(modelPublicId)) {
				}
				public2SystemIds.put(publicId,systemId);
			}
		}
	@Override public void register(final String systemId,final String xmiId,final RefObject object) {
		if (topSystemId == null) {
			topSystemId = systemId;
			try {
				baseUri = new URI(systemId.substring(0,systemId.lastIndexOf('/') + 1));
			}catch (URISyntaxException e) {
				baseUri = null;
			}
		}
		String resolvedSystemId = systemId;
		if (profile&&systemId.equals(topSystemId)) {
			resolvedSystemId = modelPublicId;
		}else if (systemId.equals(topSystemId)) {
			resolvedSystemId = null;
		}else if (reverseUrlMap.get(systemId) != null) {
			resolvedSystemId = reverseUrlMap.get(systemId);
		}
		String key;
		if (resolvedSystemId == null||"".equals(resolvedSystemId)) {
			key = xmiId;
		}else {
			key = resolvedSystemId + "#" + xmiId;
		}
		if (!idToObjects.containsKey(key)&&!objectsToId.containsKey(object.refMofId())) {
			super.register(resolvedSystemId,xmiId,object);
			idToObjects.put(key,object);
			objectsToId.put(object.refMofId(),new XmiReference(resolvedSystemId,xmiId));
		}else {
			if (idToObjects.containsKey(key)&&idToObjects.get(key) != object) {
				((ModelElement) idToObjects.get(key)).getName();
				throw new IllegalStateException("Multiple elements with same xmi.id");
			}
			if (objectsToId.containsKey(object.refMofId())) {
				XmiReference ref = objectsToId.get(object.refMofId());
			}
		}
	}
	public Map<String,Object>getIdToObjectMap() {
		return idToObjects;
	}
	public void clearIdMaps() {
		idToObjects.clear();
		objectsToId.clear();
	}
	@Override public URL toURL(String systemId) {
		final String suffix = getSuffix(systemId);
		String exts = "\\.jar|\\.zip";
		String suffixWithExt = suffix.replaceAll(exts,"");
		URL modelUrl = urlMap.get(suffixWithExt);
		if (modelUrl == null) {
			if (public2SystemIds.containsKey(systemId)) {
				modelUrl = getValidURL(public2SystemIds.get(systemId));
			}
			if (modelUrl == null) {
				modelUrl = getValidURL(fixupURL(systemId));
			}
			if (modelUrl == null) {
				String modelUrlAsString = findModuleURL(suffix);
				if (!(modelUrlAsString == null||"".equals(modelUrlAsString))) {
					modelUrl = getValidURL(modelUrlAsString);
				}
				if (modelUrl == null) {
					modelUrl = findModelUrlOnClasspath(systemId);
				}
				if (modelUrl == null) {
					modelUrl = super.toURL(systemId);
				}
			}
			if (modelUrl != null) {
				urlMap.put(suffixWithExt,modelUrl);
				String relativeUri = systemId;
				try {
					if (baseUri != null) {
						relativeUri = baseUri.relativize(new URI(systemId)).toString();
					}else {
						relativeUri = systemId;
					}
				}catch (URISyntaxException e) {
					relativeUri = systemId;
				}
				reverseUrlMap.put(modelUrl.toString(),relativeUri);
				reverseUrlMap.put(systemId,relativeUri);
			}else {
			}
		}
		return modelUrl;
	}
	private String findModuleURL(String moduleName) {
		if (modulesPath == null) {
			return null;
		}
		for (String moduleDirectory:modulesPath) {
			File candidate = new File(moduleDirectory,moduleName);
			if (candidate.exists()) {
				String urlString;
				try {
					urlString = candidate.toURI().toURL().toExternalForm();
				}catch (MalformedURLException e) {
					return null;
				}
				return fixupURL(urlString);
			}
		}
		if (public2SystemIds.containsKey(moduleName)) {
			return moduleName;
		}
		return null;
	}
	private String getSuffix(String systemId) {
		int lastSlash = systemId.lastIndexOf("/");
		if (lastSlash > 0) {
			String suffix = systemId.substring(lastSlash + 1);
			return suffix;
		}
		return systemId;
	}
	protected static final String[]CLASSPATH_MODEL_SUFFIXES = new String[] {"xml","xmi"};
	private URL findModelUrlOnClasspath(String systemId) {
		final String dot = ".";
		String modelName = systemId;
		if (public2SystemIds.containsKey(systemId)) {
			modelName = public2SystemIds.get(systemId);
		}else {
			int filenameIndex = systemId.lastIndexOf("/");
			if (filenameIndex > 0) {
				modelName = systemId.substring(filenameIndex + 1,systemId.length());
			}
			if (modelName.lastIndexOf(dot) > 0) {
				modelName = modelName.substring(0,modelName.lastIndexOf(dot));
			}
		}
		URL modelUrl = Thread.currentThread().getContextClassLoader().getResource(modelName);
		if (modelUrl == null) {
			modelUrl = this.getClass().getResource(modelName);
		}
		if (modelUrl == null) {
			if (CLASSPATH_MODEL_SUFFIXES != null&&CLASSPATH_MODEL_SUFFIXES. > 0) {
				for (String suffix:CLASSPATH_MODEL_SUFFIXES) {
					modelUrl = Thread.currentThread().getContextClassLoader().getResource(modelName + dot + suffix);
					if (modelUrl != null) {
						break;
					}
					modelUrl = this.getClass().getResource(modelName);
					if (modelUrl != null) {
						break;
					}
				}
			}
		}
		return modelUrl;
	}
	private URL getValidURL(String systemId) {
		InputStream stream = null;
		URL url = null;
		try {
			url = new URL(systemId);
			stream = url.openStream();
			stream.close();
		}catch (MalformedURLException e) {
			url = null;
		}catch (IOException e) {
			url = null;
		}finally {
			stream = null;
		}
		return url;
	}
	private String fixupURL(String url) {
		final String suffix = getSuffix(url);
		if (suffix.endsWith(".zargo")) {
			url = "jar:" + url + "!/" + suffix.substring(0,suffix.length() - 6) + ".xmi";
		}else if (suffix.endsWith(".zip")||suffix.endsWith(".jar")) {
			url = "jar:" + url + "!/" + suffix.substring(0,suffix.length() - 4);
		}
		return url;
	}
	@Override public void readExternalDocument(String arg0) {
		try {
			super.readExternalDocument(arg0);
		}catch (DebugException e) {
			throw new XmiReferenceException(arg0,e);
		}
	}
}



