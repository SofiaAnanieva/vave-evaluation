package org.argouml.uml.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.application.api.GUISettingsTabInterface;
import org.argouml.application.api.InitSubsystem;
import org.argouml.uml.ui.PropPanelFactory;
import org.argouml.uml.ui.PropPanelFactoryManager;
import org.argouml.uml.ui.ElementPropPanelFactory;
import org.argouml.uml.ui.UmlObjectPropPanelFactory;
import org.argouml.uml.ui.TabProps;
import org.argouml.uml.ui.TabDocumentation;
import org.argouml.uml.ui.TabStyle;
import org.argouml.uml.ui.TabSrc;
import org.argouml.uml.ui.TabConstraints;
import org.argouml.uml.ui.TabStereotype;
import org.argouml.uml.ui.TabTaggedValues;


public class InitUmlUI implements InitSubsystem {
	public void init() {
		PropPanelFactory elementFactory = new ElementPropPanelFactory();
		PropPanelFactoryManager.addPropPanelFactory(elementFactory);
		PropPanelFactory umlObjectFactory = new UmlObjectPropPanelFactory();
		PropPanelFactoryManager.addPropPanelFactory(umlObjectFactory);
	}
	public List<AbstractArgoJPanel>getDetailsTabs() {
		List<AbstractArgoJPanel>result = new ArrayList<AbstractArgoJPanel>();
		result.add(new TabProps());
		result.add(new TabDocumentation());
		result.add(new TabStyle());
		result.add(new TabSrc());
		result.add(new TabConstraints());
		result.add(new TabStereotype());
		result.add(new TabTaggedValues());
		return result;
	}
	public List<GUISettingsTabInterface>getProjectSettingsTabs() {
		return Collections.emptyList();
	}
	public List<GUISettingsTabInterface>getSettingsTabs() {
		return Collections.emptyList();
	}
}



