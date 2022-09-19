package org.argouml.profile.init;

import org.argouml.profile.ProfileFacade;
import org.argouml.profile.internal.ui.ProfilePropPanelFactory;
import org.argouml.uml.ui.PropPanelFactory;
import org.argouml.uml.ui.PropPanelFactoryManager;
import org.argouml.profile.ProfileManager;
import org.argouml.profile.internal.ProfileManagerImpl;


public class InitProfileSubsystem {
	public void init() {
		ProfileFacade.setManager(new ProfileManagerImpl());
		PropPanelFactory factory = new ProfilePropPanelFactory();
		PropPanelFactoryManager.addPropPanelFactory(factory);
		new ProfileLoader().doLoad();
	}
}


