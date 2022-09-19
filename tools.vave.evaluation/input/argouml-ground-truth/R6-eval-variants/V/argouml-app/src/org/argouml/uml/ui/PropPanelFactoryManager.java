package org.argouml.uml.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.argouml.uml.ui.PropPanelFactory;


public class PropPanelFactoryManager {
	private static List<PropPanelFactory>ppfactories = new ArrayList<PropPanelFactory>();
	public static void addPropPanelFactory(PropPanelFactory factory) {
		ppfactories.add(0,factory);
	}
	public static void removePropPanelFactory(PropPanelFactory factory) {
		ppfactories.remove(factory);
	}
	static Collection<PropPanelFactory>getFactories() {
		return ppfactories;
	}
}



