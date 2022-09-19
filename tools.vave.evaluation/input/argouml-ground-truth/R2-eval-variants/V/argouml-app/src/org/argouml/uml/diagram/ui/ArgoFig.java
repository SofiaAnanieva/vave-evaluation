package org.argouml.uml.diagram.ui;

import java.awt.Color;
import org.argouml.kernel.Project;
import org.argouml.uml.diagram.DiagramSettings;


public interface ArgoFig {
	static final int X0 = 10;
	static final int Y0 = 10;
	public static final int ROWHEIGHT = 17;
	public static final int STEREOHEIGHT = 18;
	static final int LINE_WIDTH = 1;
	static final Color LINE_COLOR = Color.black;
	static final Color SOLID_FILL_COLOR = LINE_COLOR;
	static final Color FILL_COLOR = Color.white;
	static final Color INVISIBLE_LINE_COLOR = FILL_COLOR;
	static final Color TEXT_COLOR = Color.black;
	static final Color DEBUG_COLOR = Color.cyan;
	@Deprecated public void setProject(Project project);
	@Deprecated public Project getProject();
	public void renderingChanged();
	public DiagramSettings getSettings();
	public void setSettings(DiagramSettings settings);
	@Deprecated public void setOwner(Object owner);
}



