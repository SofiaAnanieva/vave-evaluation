package org.argouml.ui.cmd;

import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramUtils;
import org.tigris.gef.base.PrintAction;


public class PrintManager {
	private final PrintAction printCmd = new PrintAction();
	private static final PrintManager INSTANCE = new PrintManager();
	public static PrintManager getInstance() {
		return INSTANCE;
	}
	private PrintManager() {
	}
	public void print() {
		Object target = DiagramUtils.getActiveDiagram();
		if (target instanceof ArgoDiagram) {
			printCmd.actionPerformed(null);
		}
	}
	public void showPageSetupDialog() {
		printCmd.doPageSetup();
	}
}



