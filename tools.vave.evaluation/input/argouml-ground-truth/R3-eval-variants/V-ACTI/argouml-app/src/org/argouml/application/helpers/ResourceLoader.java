package org.argouml.application.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;


class ResourceLoader {
	private static HashMap<String,Icon>resourceCache = new HashMap<String,Icon>();
	private static List<String>resourceLocations = new ArrayList<String>();
	private static List<String>resourceExtensions = new ArrayList<String>();
	public static ImageIcon lookupIconResource(String resource) {
		return lookupIconResource(resource,resource);
	}
	public static ImageIcon lookupIconResource(String resource,String desc) {
		return lookupIconResource(resource,desc,null);
	}
	public static ImageIcon lookupIconResource(String resource,ClassLoader loader) {
		return lookupIconResource(resource,resource,loader);
	}
	public static ImageIcon lookupIconResource(String resource,String desc,ClassLoader loader) {
		resource = toJavaIdentifier(resource);
		if (isInCache(resource)) {
			return(ImageIcon) resourceCache.get(resource);
		}
		ImageIcon res = null;
		java.net.
				URL imgURL = lookupIconUrl(resource,loader);
		if (imgURL != null) {
			res = new ImageIcon(imgURL,desc);
			synchronized (resourceCache) {
				resourceCache.put(resource,res);
			}
		}
		return res;
	}
	static java.net.URL lookupIconUrl(String resource,ClassLoader loader) {
		java.net.
				URL imgURL = null;
		for (Iterator extensions = resourceExtensions.iterator();extensions.hasNext();) {
			String tmpExt = (String) extensions.next();
			for (Iterator locations = resourceLocations.iterator();locations.hasNext();) {
				String imageName = locations.next() + "/" + resource + "." + tmpExt;
				if (loader == null) {
					imgURL = ResourceLoader.class.getResource(imageName);
				}else {
					imgURL = loader.getResource(imageName);
				}
				if (imgURL != null) {
					break;
				}
			}
			if (imgURL != null) {
				break;
			}
		}
		return imgURL;
	}
	public static void addResourceLocation(String location) {
		if (!containsLocation(location)) {
			resourceLocations.add(location);
		}
	}
	public static void addResourceExtension(String extension) {
		if (!containsExtension(extension)) {
			resourceExtensions.add(extension);
		}
	}
	public static void removeResourceLocation(String location) {
		for (Iterator iter = resourceLocations.iterator();iter.hasNext();) {
			String loc = (String) iter.next();
			if (loc.equals(location)) {
				resourceLocations.remove(loc);
				break;
			}
		}
	}
	public static void removeResourceExtension(String extension) {
		for (Iterator iter = resourceExtensions.iterator();iter.hasNext();) {
			String ext = (String) iter.next();
			if (ext.equals(extension)) {
				resourceExtensions.remove(ext);
				break;
			}
		}
	}
	public static boolean containsExtension(String extension) {
		return resourceExtensions.contains(extension);
	}
	public static boolean containsLocation(String location) {
		return resourceLocations.contains(location);
	}
	public static boolean isInCache(String resource) {
		return resourceCache.containsKey(resource);
	}
	public static final String toJavaIdentifier(String s) {
		int len = s.length();
		int pos = 0;
		for (int i = 0;i < len;i++,pos++) {
			if (!Character.isJavaIdentifierPart(s.charAt(i)))break;
		}
		if (pos == len) {
			return s;
		}
		StringBuffer buf = new StringBuffer(len);
		buf.append(s.substring(0,pos));
		for (int i = pos + 1;i < len;i++) {
			char c = s.charAt(i);
			if (Character.isJavaIdentifierPart(c)) {
				buf.append(c);
			}
		}
		return buf.toString();
	}
}



