package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.util.ArgoDialog;


public class GotoDialog extends ArgoDialog {
	private final TabResults allDiagrams = new TabResults(false);
	public GotoDialog() {
		super(Translator.localize("dialog.gotodiagram.title"),ArgoDialog.OK_CANCEL_OPTION,false);
		Project p = ProjectManager.getManager().getCurrentProject();
		allDiagrams.setResults(p.getDiagramList(),p.getDiagramList());
		allDiagrams.setPreferredSize(new Dimension(allDiagrams.getPreferredSize().width,allDiagrams.getPreferredSize().height / 2));
		allDiagrams.selectResult(0);
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(allDiagrams,BorderLayout.CENTER);
		setContent(mainPanel);
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getOkButton()) {
			allDiagrams.doDoubleClick();
		}else {
			super.actionPerformed(e);
		}
	}
	protected void nameButtons() {
		super.nameButtons();
		nameButton(getOkButton(),"button.go-to-selection");
		nameButton(getCancelButton(),"button.close");
	}
}



