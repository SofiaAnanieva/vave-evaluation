package org.argouml.uml.diagram.static_structure.ui;

import java.util.Collections;
import java.util.List;
import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.application.api.GUISettingsTabInterface;
import org.argouml.application.api.InitSubsystem;
import org.argouml.uml.ui.PropPanelFactory;
import org.argouml.uml.ui.PropPanelFactoryManager;


public class InitClassDiagram implements InitSubsystem {
	public List<AbstractArgoJPanel>getDetailsTabs() {
		return Collections.emptyList();
	}
	public List<GUISettingsTabInterface>getProjectSettingsTabs() {
		return Collections.emptyList();
	}
	public List<GUISettingsTabInterface>getSettingsTabs() {
		return Collections.emptyList();
	}
	public void init() {
		PropPanelFactory diagramFactory = new ClassDiagramPropPanelFactory();
		PropPanelFactoryManager.addPropPanelFactory(diagramFactory);
	}
}



