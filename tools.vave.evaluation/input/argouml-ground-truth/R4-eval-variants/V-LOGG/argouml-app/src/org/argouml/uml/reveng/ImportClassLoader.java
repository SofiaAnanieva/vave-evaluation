package org.argouml.uml.reveng;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.net.URLClassLoader;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.File;
import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.configuration.Configuration;


public final class ImportClassLoader extends URLClassLoader {
	private static final Logger LOG = Logger.getLogger(ImportClassLoader.class);
	private static ImportClassLoader instance;
	private ImportClassLoader(URL[]urls) {
		super(urls);
	}
	public static ImportClassLoader getInstance()throws MalformedURLException {
		if (instance == null) {
			String path = Configuration.getString(Argo.KEY_USER_IMPORT_CLASSPATH,System.getProperty("user.dir"));
			return getInstance(getURLs(path));
		}else {
			return instance;
		}
	}
	public static ImportClassLoader getInstance(URL[]urls)throws MalformedURLException {
		instance = new ImportClassLoader(urls);
		return instance;
	}
	public void addFile(File f)throws MalformedURLException {
		addURL(f.toURI().toURL());
	}
	public void removeFile(File f) {
		URL url = null;
		try {
			url = f.toURI().toURL();
		}catch (MalformedURLException e) {
			LOG.warn("could not remove file ",e);
			return;
		}
		List<URL>urls = new ArrayList<URL>();
		for (URL u:getURLs()) {
			if (!url.equals(u)) {
				urls.add(u);
			}
		}
		if (urls.size() == 0) {
			return;
		}
		instance = new ImportClassLoader((URL[]) urls.toArray());
	}
	public void setPath(String path) {
		StringTokenizer st = new StringTokenizer(path,";");
		st.countTokens();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			try {
				this.addFile(new File(token));
			}catch (MalformedURLException e) {
				LOG.warn("could not set path ",e);
			}
		}
	}
	public static URL[]getURLs(String path) {
		java.util.
				List<URL>urlList = new ArrayList<URL>();
		StringTokenizer st = new StringTokenizer(path,";");
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			try {
				urlList.add(new File(token).toURI().toURL());
			}catch (MalformedURLException e) {
				LOG.error(e);
			}
		}
		URL[]urls = new URL[urlList.size()];
		for (int i = 0;i < urls.;i++) {
			urls[i] = urlList.get(i);
		}
		return urls;
	}
	public void setPath(Object[]paths) {
		for (int i = 0;i < paths.;i++) {
			try {
				this.addFile(new File(paths[i].toString()));
			}catch (Exception e) {
				LOG.warn("could not set path ",e);
			}
		}
	}
	public void loadUserPath() {
		setPath(Configuration.getString(Argo.KEY_USER_IMPORT_CLASSPATH,""));
	}
	public void saveUserPath() {
		Configuration.setString(Argo.KEY_USER_IMPORT_CLASSPATH,this.toString());
	}
	@Override public String toString() {
		URL[]urls = this.getURLs();
		StringBuilder path = new StringBuilder();
		for (int i = 0;i < urls.;i++) {
			path.append(urls[i].getFile());
			if (i < urls. - 1) {
				path.append(";");
			}
		}
		return path.toString();
	}
}



