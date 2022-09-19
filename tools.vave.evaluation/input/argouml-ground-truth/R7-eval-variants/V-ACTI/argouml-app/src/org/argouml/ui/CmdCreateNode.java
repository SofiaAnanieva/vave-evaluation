package org.argouml.ui;

import javax.swing.Action;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.tigris.gef.base.CreateNodeAction;


public class CmdCreateNode extends CreateNodeAction {
	private static final long serialVersionUID = 4813526025971574818l;
	public CmdCreateNode(Object nodeType,String name) {
		super(nodeType,name,ResourceLoaderWrapper.lookupIconResource(ResourceLoaderWrapper.getImageBinding(name)));
		putToolTip(name);
	}
	@Override public Object makeNode() {
		Object newNode = Model.getUmlFactory().buildNode(getArg("className"));
		return newNode;
	}
	private void putToolTip(String name) {
		putValue(Action.SHORT_DESCRIPTION,Translator.localize(name));
	}
}



