package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.apache.log4j.Logger;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.ui.ExceptionDialog;
import org.argouml.uml.reveng.Import;
import org.argouml.uml.reveng.ImporterManager;
import org.argouml.util.ArgoFrame;
import org.tigris.gef.undo.UndoableAction;


public class ActionImportFromSources extends UndoableAction {
	private static final Logger LOG = Logger.getLogger(ActionImportFromSources.class);
	private static final ActionImportFromSources SINGLETON = new ActionImportFromSources();
	protected ActionImportFromSources() {
		super(Translator.localize("action.import-sources"),ResourceLoaderWrapper.lookupIcon("action.import-sources"));
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("action.import-sources"));
	}
	public void actionPerformed(ActionEvent event) {
		super.actionPerformed(event);
		if (ImporterManager.getInstance().hasImporters()) {
			new Import(ArgoFrame.getInstance());
		}else {
			LOG.info("Import sources dialog not shown: no importers!");
			ExceptionDialog ed = new ExceptionDialog(ArgoFrame.getInstance(),Translator.localize("dialog.title.problem"),Translator.localize("dialog.import.no-importers.intro"),Translator.localize("dialog.import.no-importers.message"));
			ed.setModal(true);
			ed.setVisible(true);
		}
	}
	public static ActionImportFromSources getInstance() {
		return SINGLETON;
	}
}



