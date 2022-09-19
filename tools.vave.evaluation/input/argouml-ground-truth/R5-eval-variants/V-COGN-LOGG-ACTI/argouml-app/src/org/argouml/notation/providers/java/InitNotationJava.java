package org.argouml.notation.providers.java;

import java.util.Collections;
import java.util.List;
import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.application.api.GUISettingsTabInterface;
import org.argouml.application.api.InitSubsystem;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.notation.Notation;
import org.argouml.notation.NotationName;
import org.argouml.notation.NotationProviderFactory2;


public class InitNotationJava implements InitSubsystem {
	public void init() {
		NotationProviderFactory2 npf = NotationProviderFactory2.getInstance();
		NotationName name = Notation.makeNotation("Java",null,ResourceLoaderWrapper.lookupIconResource("JavaNotation"));
		npf.addNotationProvider(NotationProviderFactory2.TYPE_NAME,name,ModelElementNameNotationJava.class);
		npf.addNotationProvider(NotationProviderFactory2.TYPE_ATTRIBUTE,name,AttributeNotationJava.class);
		npf.addNotationProvider(NotationProviderFactory2.TYPE_OPERATION,name,OperationNotationJava.class);
		npf.addNotationProvider(NotationProviderFactory2.TYPE_ASSOCIATION_END_NAME,name,AssociationEndNameNotationJava.class);
		npf.addNotationProvider(NotationProviderFactory2.TYPE_ASSOCIATION_NAME,name,AssociationNameNotationJava.class);
	}
	public List<GUISettingsTabInterface>getProjectSettingsTabs() {
		return Collections.emptyList();
	}
	public List<GUISettingsTabInterface>getSettingsTabs() {
		return Collections.emptyList();
	}
	public List<AbstractArgoJPanel>getDetailsTabs() {
		return Collections.emptyList();
	}
}



