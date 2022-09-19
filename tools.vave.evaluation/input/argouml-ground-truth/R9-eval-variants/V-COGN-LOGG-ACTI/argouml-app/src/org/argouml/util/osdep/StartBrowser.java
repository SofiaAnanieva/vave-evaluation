package org.argouml.util.osdep;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import org.apache.log4j.Logger;


public class StartBrowser {
	private static final Logger LOG = Logger.getLogger(StartBrowser.class);
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
					LOG.error(cnfe);
					LOG.info("Trying a default browser (netscape)");
					String[]commline =  {"netscape",url};
					Runtime.getRuntime().exec(commline);
				}
			}else {
				Runtime.getRuntime().exec("firefox " + url);
			}
		}catch (IOException ioe) {
			LOG.error(ioe);
		}
	}
	public static void openUrl(URL url) {
		openUrl(url.toString());
	}
}



