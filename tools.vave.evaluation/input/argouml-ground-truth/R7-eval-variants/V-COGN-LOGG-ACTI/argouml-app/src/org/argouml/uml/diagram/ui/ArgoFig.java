package org.argouml.uml.diagram.ui;

import java.awt.Color;
import org.argouml.kernel.Project;
import org.argouml.uml.diagram.DiagramSettings;


public interface ArgoFig {
	static final int X0 = 10;
	static final int Y0 = 10;
	public static final int ROWHEIGHT = 17;
	public static final int STEREOHEIGHT = 18;
	static final boolean DEBUG = false;
	static final int LINE_WIDTH = !DEBUG?1:20;
	static final Color LINE_COLOR = !DEBUG?Color.black:new Color(0,100,100,50);
	static final Color SOLID_FILL_COLOR = !DEBUG?LINE_COLOR:new Color(LINE_COLOR.getRed(),LINE_COLOR.getGreen(),LINE_COLOR.getBlue(),75);
	static final Color FILL_COLOR = !DEBUG?Color.white:new Color(255,255,100,100);
	static final Color INVISIBLE_LINE_COLOR = !DEBUG?FILL_COLOR:new Color(FILL_COLOR.getRed(),FILL_COLOR.getGreen(),FILL_COLOR.getBlue(),50);
	static final Color TEXT_COLOR = !DEBUG?Color.black:new Color(0,100,0,100);
	static final Color DEBUG_COLOR = new Color(255,0,255,!DEBUG?0:255);
	@Deprecated public void setProject(Project project);
	@Deprecated public Project getProject();
	public void renderingChanged();
	public DiagramSettings getSettings();
	public void setSettings(DiagramSettings settings);
	@Deprecated public void setOwner(Object owner);
}



