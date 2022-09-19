package org.argouml.uml.diagram.ui;

import java.util.List;
import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;
import org.argouml.uml.diagram.ui.ArgoFig;


public abstract class ArgoFigGroup extends FigGroup implements ArgoFig {
	private static final Logger LOG = Logger.getLogger(ArgoFigGroup.class);
	private DiagramSettings settings;
	@Deprecated public ArgoFigGroup() {
		super();
	}
	public ArgoFigGroup(Object owner,DiagramSettings renderSettings) {
		super();
		super.setOwner(owner);
		settings = renderSettings;
	}
	@Deprecated public ArgoFigGroup(List<ArgoFig>arg0) {
		super(arg0);
	}
	@SuppressWarnings("deprecation")@Deprecated public void setProject(Project project) {
		throw new UnsupportedOperationException();
	}
	@SuppressWarnings("deprecation")@Deprecated public Project getProject() {
		return ArgoFigUtil.getProject(this);
	}
	public void renderingChanged() {
		for (Fig fig:(List<Fig>) getFigs()) {
			if (fig instanceof ArgoFig) {
				((ArgoFig) fig).renderingChanged();
			}else {
				LOG.debug("Found non-Argo fig nested");
			}
		}
	}
	public DiagramSettings getSettings() {
		if (settings == null) {
			Project p = getProject();
			if (p != null) {
				return p.getProjectSettings().getDefaultDiagramSettings();
			}
		}
		return settings;
	}
	public void setSettings(DiagramSettings renderSettings) {
		settings = renderSettings;
		renderingChanged();
	}
	@Deprecated public void setOwner(Object owner) {
		super.setOwner(owner);
	}
}



