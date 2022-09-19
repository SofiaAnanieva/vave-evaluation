package org.argouml.ui;

import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import javax.swing.AbstractAction;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.IllegalModelElementConnectionException;
import org.argouml.model.Model;
import org.argouml.ui.explorer.ExplorerPopup;


public class ActionCreateEdgeModelElement extends AbstractAction {
	private final Object metaType;
	private final Object source;
	private final Object dest;
	public ActionCreateEdgeModelElement(final Object theMetaType,final Object theSource,final Object theDestination,final String relationshipDescr) {
		super(MessageFormat.format(relationshipDescr,new Object[] {DisplayTextTree.getModelElementDisplayName(theSource),DisplayTextTree.getModelElementDisplayName(theDestination)}));
		this.metaType = theMetaType;
		this.source = theSource;
		this.dest = theDestination;
	}
	public void actionPerformed(ActionEvent e) {
		Object rootModel = ProjectManager.getManager().getCurrentProject().getModel();
		try {
			Model.getUmlFactory().buildConnection(metaType,source,null,dest,null,null,rootModel);
		}catch (IllegalModelElementConnectionException e1) {
		}
	}
}



