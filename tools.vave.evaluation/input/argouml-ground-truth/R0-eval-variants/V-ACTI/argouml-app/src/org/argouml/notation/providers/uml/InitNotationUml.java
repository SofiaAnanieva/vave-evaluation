package org.argouml.notation.providers.uml;

import java.util.Collections;
import java.util.List;
import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.application.api.GUISettingsTabInterface;
import org.argouml.application.api.InitSubsystem;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.notation.Notation;
import org.argouml.notation.NotationName;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.notation.providers.uml.NotationUtilityUml;


public class InitNotationUml implements InitSubsystem {
	public void init() {
		NotationProviderFactory2 npf = NotationProviderFactory2.getInstance();
		NotationName name = Notation.makeNotation("UML","1.4",ResourceLoaderWrapper.lookupIconResource("UmlNotation"));
		npf.addNotationProvider(NotationProviderFactory2.TYPE_NAME,name,ModelElementNameNotationUml.class);
		npf.addNotationProvider(NotationProviderFactory2.TYPE_TRANSITION,name,TransitionNotationUml.class);
		npf.addNotationProvider(NotationProviderFactory2.TYPE_STATEBODY,name,StateBodyNotationUml.class);
		npf.addNotationProvider(NotationProviderFactory2.TYPE_ACTIONSTATE,name,ActionStateNotationUml.class);
		npf.addNotationProvider(NotationProviderFactory2.TYPE_OBJECT,name,ObjectNotationUml.class);
		npf.addNotationProvider(NotationProviderFactory2.TYPE_COMPONENTINSTANCE,name,ComponentInstanceNotationUml.class);
		npf.addNotationProvider(NotationProviderFactory2.TYPE_NODEINSTANCE,name,NodeInstanceNotationUml.class);
		npf.addNotationProvider(NotationProviderFactory2.TYPE_OBJECTFLOWSTATE_TYPE,name,ObjectFlowStateTypeNotationUml.class);
		npf.addNotationProvider(NotationProviderFactory2.TYPE_OBJECTFLOWSTATE_STATE,name,ObjectFlowStateStateNotationUml.class);
		npf.addNotationProvider(NotationProviderFactory2.TYPE_CALLSTATE,name,CallStateNotationUml.class);
		npf.addNotationProvider(NotationProviderFactory2.TYPE_CLASSIFIERROLE,name,ClassifierRoleNotationUml.class);
		npf.addNotationProvider(NotationProviderFactory2.TYPE_MESSAGE,name,MessageNotationUml.class);
		npf.addNotationProvider(NotationProviderFactory2.TYPE_ATTRIBUTE,name,AttributeNotationUml.class);
		npf.addNotationProvider(NotationProviderFactory2.TYPE_OPERATION,name,OperationNotationUml.class);
		npf.addNotationProvider(NotationProviderFactory2.TYPE_EXTENSION_POINT,name,ExtensionPointNotationUml.class);
		npf.addNotationProvider(NotationProviderFactory2.TYPE_ASSOCIATION_END_NAME,name,AssociationEndNameNotationUml.class);
		npf.addNotationProvider(NotationProviderFactory2.TYPE_ASSOCIATION_ROLE,name,AssociationRoleNotationUml.class);
		npf.addNotationProvider(NotationProviderFactory2.TYPE_ASSOCIATION_NAME,name,AssociationNameNotationUml.class);
		npf.addNotationProvider(NotationProviderFactory2.TYPE_MULTIPLICITY,name,MultiplicityNotationUml.class);
		npf.addNotationProvider(NotationProviderFactory2.TYPE_ENUMERATION_LITERAL,name,EnumerationLiteralNotationUml.class);
		NotationProviderFactory2.getInstance().setDefaultNotation(name);
		(new NotationUtilityUml()).init();
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



