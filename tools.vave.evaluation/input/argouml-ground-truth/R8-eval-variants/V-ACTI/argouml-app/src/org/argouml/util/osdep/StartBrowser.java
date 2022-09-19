package org.argouml.util.osdep;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;


public class StartBrowser {
	public static void openUrl(String url) {
		try {
			if (OsUtil.isWin32()) {
				Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
			}else if (OsUtil.isMac()) {
				try {
					ClassLoader cl = ClassLoader.getSystemClassLoader();
					Class c = cl.loadClass("com.apple.mrj.MRJFileUtils");
					Class[]argtypes =  {String.class};
					Method m = c.getMethod("openURL",argtypes);
					Object[]args =  {url};
					m.invoke(c.newInstance(),args);
				}catch (Exception cnfe) {
					String[]commline =  {"netscape",url};
					Runtime.getRuntime().exec(commline);
				}
			}else {
				Runtime.getRuntime().exec("firefox " + url);
			}
		}catch (IOException ioe) {
		}
	}
	public static void openUrl(URL url) {
		openUrl(url.toString());
	}
}



