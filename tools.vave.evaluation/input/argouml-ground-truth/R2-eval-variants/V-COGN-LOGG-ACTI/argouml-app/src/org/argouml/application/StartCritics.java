package org.argouml.application;

import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.cognitive.Designer;
import org.argouml.configuration.Configuration;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.pattern.cognitive.critics.InitPatternCritics;
import org.argouml.uml.cognitive.UMLDecision;
import org.argouml.uml.cognitive.critics.ChildGenUML;
import org.argouml.uml.cognitive.critics.InitCognitiveCritics;


public class StartCritics implements Runnable {
	private static final Logger LOG = Logger.getLogger(StartCritics.class);
	public void run() {
		Designer dsgr = Designer.theDesigner();
		SubsystemUtility.initSubsystem(new InitCognitiveCritics());
		SubsystemUtility.initSubsystem(new InitPatternCritics());
		org.argouml.uml.cognitive.checklist.Init.init();
		dsgr.setClarifier(ResourceLoaderWrapper.lookupIconResource("PostItD0"));
		dsgr.setDesignerName(Configuration.getString(Argo.KEY_USER_FULLNAME));
		Configuration.addListener(Argo.KEY_USER_FULLNAME,dsgr);
		Project p = ProjectManager.getManager().getCurrentProject();
		dsgr.spawnCritiquer(p);
		dsgr.setChildGenerator(new ChildGenUML());
		for (Object model:p.getUserDefinedModelList()) {
			Model.getPump().addModelEventListener(dsgr,model);
		}
		LOG.info("spawned critiquing thread");
		dsgr.getDecisionModel().startConsidering(UMLDecision.CLASS_SELECTION);
		dsgr.getDecisionModel().startConsidering(UMLDecision.BEHAVIOR);
		dsgr.getDecisionModel().startConsidering(UMLDecision.NAMING);
		dsgr.getDecisionModel().startConsidering(UMLDecision.STORAGE);
		dsgr.getDecisionModel().startConsidering(UMLDecision.INHERITANCE);
		dsgr.getDecisionModel().startConsidering(UMLDecision.CONTAINMENT);
		dsgr.getDecisionModel().startConsidering(UMLDecision.PLANNED_EXTENSIONS);
		dsgr.getDecisionModel().startConsidering(UMLDecision.STATE_MACHINES);
		dsgr.getDecisionModel().startConsidering(UMLDecision.PATTERNS);
		dsgr.getDecisionModel().startConsidering(UMLDecision.RELATIONSHIPS);
		dsgr.getDecisionModel().startConsidering(UMLDecision.INSTANCIATION);
		dsgr.getDecisionModel().startConsidering(UMLDecision.MODULARITY);
		dsgr.getDecisionModel().startConsidering(UMLDecision.EXPECTED_USAGE);
		dsgr.getDecisionModel().startConsidering(UMLDecision.METHODS);
		dsgr.getDecisionModel().startConsidering(UMLDecision.CODE_GEN);
		dsgr.getDecisionModel().startConsidering(UMLDecision.STEREOTYPES);
		Designer.setUserWorking(true);
	}
}



