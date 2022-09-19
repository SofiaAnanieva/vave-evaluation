package org.argouml.ui.cmd;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.persistence.PersistenceManager;
import org.argouml.util.ArgoFrame;
import org.argouml.util.UIUtils;


public class ActionShowXMLDump extends AbstractAction {
	private static final long serialVersionUID = -7942597779499060380l;
	private static final int INSET_PX = 3;
	public ActionShowXMLDump() {
		super(Translator.localize("action.show-saved"));
	}
	public void actionPerformed(ActionEvent e) {
		Project project = ProjectManager.getManager().getCurrentProject();
		String data = PersistenceManager.getInstance().getQuickViewDump(project);
		JDialog pw = new JDialog(ArgoFrame.getInstance(),Translator.localize("action.show-saved"),false);
		JTextArea a = new JTextArea(data,50,80);
		a.setEditable(false);
		a.setLineWrap(true);
		a.setWrapStyleWord(true);
		a.setMargin(new Insets(INSET_PX,INSET_PX,INSET_PX,INSET_PX));
		a.setCaretPosition(0);
		pw.getContentPane().add(new JScrollPane(a));
		pw.setSize(400,500);
		pw.setLocationRelativeTo(ArgoFrame.getInstance());
		init(pw);
		pw.setVisible(true);
	}
	private void init(JDialog pw) {
		UIUtils.loadCommonKeyMap(pw);
	}
}



