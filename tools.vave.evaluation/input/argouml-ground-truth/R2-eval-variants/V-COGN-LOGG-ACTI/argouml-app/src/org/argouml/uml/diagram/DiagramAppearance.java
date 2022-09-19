package org.argouml.uml.diagram;

import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.UIManager;
import org.apache.log4j.Logger;
import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;


public final class DiagramAppearance implements PropertyChangeListener {
	private static final Logger LOG = Logger.getLogger(DiagramAppearance.class);
	public static final ConfigurationKey KEY_FONT_NAME = Configuration.makeKey("diagramappearance","fontname");
	public static final ConfigurationKey KEY_FONT_SIZE = Configuration.makeKey("diagramappearance","fontsize");
	private static final DiagramAppearance SINGLETON = new DiagramAppearance();
	public static final int STEREOTYPE_VIEW_TEXTUAL = 0;
	public static final int STEREOTYPE_VIEW_BIG_ICON = 1;
	public static final int STEREOTYPE_VIEW_SMALL_ICON = 2;
	private DiagramAppearance() {
		Configuration.addListener(DiagramAppearance.KEY_FONT_NAME,this);
		Configuration.addListener(DiagramAppearance.KEY_FONT_SIZE,this);
	}
	public static DiagramAppearance getInstance() {
		return SINGLETON;
	}
	public void propertyChange(PropertyChangeEvent pce) {
		LOG.info("Diagram appearance change:" + pce.getOldValue() + " to " + pce.getNewValue());
	}
	public String getConfiguredFontName() {
		String fontName = Configuration.getString(DiagramAppearance.KEY_FONT_NAME);
		if (fontName.equals("")) {
			Font f = getStandardFont();
			fontName = f.getName();
			Configuration.setString(DiagramAppearance.KEY_FONT_NAME,f.getName());
			Configuration.setInteger(DiagramAppearance.KEY_FONT_SIZE,f.getSize());
		}
		return fontName;
	}
	private Font getStandardFont() {
		Font font = UIManager.getDefaults().getFont("TextField.font");
		if (font == null) {
			font = (new javax.swing.JTextField()).getFont();
		}
		return font;
	}
}



