package org.argouml.ui;

import java.awt.Font;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;
import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.configuration.Configuration;


public final class LookAndFeelMgr {
	private static final Logger LOG = Logger.getLogger(LookAndFeelMgr.class);
	private static final LookAndFeelMgr SINGLETON = new LookAndFeelMgr();
	private static final String METAL_LAF_CLASS_NAME = "javax.swing.plaf.metal.MetalLookAndFeel";
	private static final String DEFAULT_KEY = "Default";
	private static final MetalTheme DEFAULT_THEME = new JasonsTheme();
	private static final MetalTheme BIG_THEME = new JasonsBigTheme();
	private static final MetalTheme HUGE_THEME = new JasonsHugeTheme();
	private static final MetalTheme[]THEMES =  {DEFAULT_THEME,BIG_THEME,HUGE_THEME,new DefaultMetalTheme()};
	private String defaultLafClass;
	public static LookAndFeelMgr getInstance() {
		return SINGLETON;
	}
	private LookAndFeelMgr() {
		LookAndFeel laf = UIManager.getLookAndFeel();
		if (laf != null) {
			defaultLafClass = laf.getClass().getName();
		}else {
			defaultLafClass = null;
		}
	}
	public void initializeLookAndFeel() {
		String n = getCurrentLookAndFeel();
		setLookAndFeel(n);
		if (isThemeCompatibleLookAndFeel(n)) {
			setTheme(getMetalTheme(getCurrentThemeClassName()));
		}
	}
	public String getThemeClassNameFromArg(String arg) {
		if (arg.equalsIgnoreCase("-big")) {
			return BIG_THEME.getClass().getName();
		}else if (arg.equalsIgnoreCase("-huge")) {
			return HUGE_THEME.getClass().getName();
		}
		return null;
	}
	public void printThemeArgs() {
		System.err.println("  -big            use big fonts");
		System.err.println("  -huge           use huge fonts");
	}
	public String[]getAvailableLookAndFeelNames() {
		UIManager.
				LookAndFeelInfo[]lafs = UIManager.getInstalledLookAndFeels();
		String[]names = new String[lafs. + 1];
		names[0] = DEFAULT_KEY;
		for (int i = 0;i < lafs.;++i) {
			names[i + 1] = lafs[i].getName();
		}
		return names;
	}
	public String[]getAvailableThemeNames() {
		String[]names = new String[LookAndFeelMgr.THEMES.];
		for (int i = 0;i < THEMES.;++i) {
			names[i] = THEMES[i].getName();
		}
		return names;
	}
	public String getLookAndFeelFromName(String name) {
		if (name == null||DEFAULT_KEY.equals(name)) {
			return null;
		}
		String className = null;
		UIManager.
				LookAndFeelInfo[]lafs = UIManager.getInstalledLookAndFeels();
		for (int i = 0;i < lafs.;++i) {
			if (lafs[i].getName().equals(name)) {
				className = lafs[i].getClassName();
			}
		}
		return className;
	}
	public String getThemeFromName(String name) {
		if (name == null) {
			return null;
		}
		String className = null;
		for (int i = 0;i < THEMES.;++i) {
			if (THEMES[i].getName().equals(name)) {
				className = THEMES[i].getClass().getName();
			}
		}
		return className;
	}
	public boolean isThemeCompatibleLookAndFeel(String lafClass) {
		if (lafClass == null) {
			return false;
		}
		return(lafClass.equals(METAL_LAF_CLASS_NAME));
	}
	public String getCurrentLookAndFeel() {
		String value = Configuration.getString(Argo.KEY_LOOK_AND_FEEL_CLASS,null);
		if (DEFAULT_KEY.equals(value)) {
			value = null;
		}
		return value;
	}
	public String getCurrentLookAndFeelName() {
		String currentLookAndFeel = getCurrentLookAndFeel();
		if (currentLookAndFeel == null) {
			return DEFAULT_KEY;
		}
		String name = null;
		UIManager.
				LookAndFeelInfo[]lafs = UIManager.getInstalledLookAndFeels();
		for (int i = 0;i < lafs.;++i) {
			if (lafs[i].getClassName().equals(currentLookAndFeel)) {
				name = lafs[i].getName();
			}
		}
		return name;
	}
	public void setCurrentLAFAndThemeByName(String lafName,String themeName) {
		String lafClass = getLookAndFeelFromName(lafName);
		String currentLookAndFeel = getCurrentLookAndFeel();
		if (lafClass == null&&currentLookAndFeel == null) {
			return;
		}
		if (lafClass == null) {
			lafClass = DEFAULT_KEY;
		}
		Configuration.setString(Argo.KEY_LOOK_AND_FEEL_CLASS,lafClass);
		setCurrentTheme(getThemeFromName(themeName));
	}
	public String getCurrentThemeClassName() {
		String value = Configuration.getString(Argo.KEY_THEME_CLASS,null);
		if (DEFAULT_KEY.equals(value)) {
			value = null;
		}
		return value;
	}
	public String getCurrentThemeName() {
		String currentThemeClassName = getCurrentThemeClassName();
		if (currentThemeClassName == null) {
			return THEMES[0].getName();
		}
		for (int i = 0;i < THEMES.;++i) {
			if (THEMES[i].getClass().getName().equals(currentThemeClassName)) {
				return THEMES[i].getName();
			}
		}
		return THEMES[0].getName();
	}
	public void setCurrentTheme(String themeClass) {
		MetalTheme theme = getMetalTheme(themeClass);
		if (theme.getClass().getName().equals(getCurrentThemeClassName())) {
			return;
		}
		setTheme(theme);
		String themeValue = themeClass;
		if (themeValue == null) {
			themeValue = DEFAULT_KEY;
		}
		Configuration.setString(Argo.KEY_THEME_CLASS,themeValue);
	}
	public Font getStandardFont() {
		Font font = UIManager.getDefaults().getFont("TextField.font");
		if (font == null) {
			font = (new javax.swing.JTextField()).getFont();
		}
		return font;
	}
	public Font getSmallFont() {
		Font font = getStandardFont();
		if (font.getSize2D() >= 12.0f) {
			return font.deriveFont(font.getSize2D() - 2.0f);
		}
		return font;
	}
	private void setLookAndFeel(String lafClass) {
		try {
			if (lafClass == null&&defaultLafClass != null) {
				UIManager.setLookAndFeel(defaultLafClass);
			}else {
				UIManager.setLookAndFeel(lafClass);
			}
		}catch (UnsupportedLookAndFeelException e) {
			LOG.error(e);
		}catch (ClassNotFoundException e) {
			LOG.error(e);
		}catch (InstantiationException e) {
			LOG.error(e);
		}catch (IllegalAccessException e) {
			LOG.error(e);
		}
	}
	private void setTheme(MetalTheme theme) {
		String currentLookAndFeel = getCurrentLookAndFeel();
		if ((currentLookAndFeel != null&&currentLookAndFeel.equals(METAL_LAF_CLASS_NAME))||(currentLookAndFeel == null&&defaultLafClass.equals(METAL_LAF_CLASS_NAME))) {
			try {
				MetalLookAndFeel.setCurrentTheme(theme);
				UIManager.setLookAndFeel(METAL_LAF_CLASS_NAME);
			}catch (UnsupportedLookAndFeelException e) {
				LOG.error(e);
			}catch (ClassNotFoundException e) {
				LOG.error(e);
			}catch (InstantiationException e) {
				LOG.error(e);
			}catch (IllegalAccessException e) {
				LOG.error(e);
			}
		}
	}
	private MetalTheme getMetalTheme(String themeClass) {
		MetalTheme theme = null;
		for (int i = 0;i < THEMES.;++i) {
			if (THEMES[i].getClass().getName().equals(themeClass)) {
				theme = THEMES[i];
			}
		}
		if (theme == null) {
			theme = DEFAULT_THEME;
		}
		return theme;
	}
}



