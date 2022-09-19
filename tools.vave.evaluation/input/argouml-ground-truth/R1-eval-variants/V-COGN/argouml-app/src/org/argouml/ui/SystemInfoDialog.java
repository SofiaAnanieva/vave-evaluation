package org.argouml.ui;

import java.awt.Frame;
import java.awt.Insets;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.argouml.application.helpers.ApplicationVersion;
import org.argouml.i18n.Translator;
import org.argouml.util.ArgoDialog;


public class SystemInfoDialog extends ArgoDialog {
	private static final long serialVersionUID = 1595302214402366939l;
	private static final int INSET_PX = 3;
	private JTextArea info = new JTextArea();
	private JButton runGCButton = new JButton();
	private JButton copyButton = new JButton();
	public SystemInfoDialog(boolean modal) {
		super(Translator.localize("dialog.title.system-information"),ArgoDialog.CLOSE_OPTION,modal);
		info.setEditable(false);
		info.setMargin(new Insets(INSET_PX,INSET_PX,INSET_PX,INSET_PX));
		runGCButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				runGCActionPerformed(e);
			}
		});
		copyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copyActionPerformed(e);
			}
		});
		nameButton(copyButton,"button.copy-to-clipboard");
		nameButton(runGCButton,"button.run-gc");
		addButton(copyButton,0);
		addButton(runGCButton,0);
		setContent(new JScrollPane(info));
		updateInfo();
		addWindowListener(new WindowAdapter() {
			public void windowActivated(WindowEvent e) {
				updateInfo();
			}
		});
		pack();
	}
	private void runGCActionPerformed(ActionEvent e) {
		assert e.getSource() == runGCButton;
		Runtime.getRuntime().gc();
		updateInfo();
	}
	private void copyActionPerformed(ActionEvent e) {
		assert e.getSource() == copyButton;
		String infoText = info.getText();
		StringSelection contents = new StringSelection(infoText);
		Clipboard clipboard = getToolkit().getSystemClipboard();
		clipboard.setContents(contents,defaultClipboardOwner);
	}
	void updateInfo() {
		info.setText(getInfo());
	}
	public static String getInfo() {
		StringBuffer s = new StringBuffer();
		s.append(Translator.localize("dialog.systeminfo.argoumlversion"));
		s.append(ApplicationVersion.getVersion() + "\n");
		s.append(Translator.localize("dialog.systeminfo.javaversion"));
		s.append(System.getProperty("java.version","") + "\n");
		s.append(Translator.localize("dialog.systeminfo.javavendor"));
		s.append(System.getProperty("java.vendor","") + "\n");
		s.append(Translator.localize("dialog.systeminfo.url-javavendor"));
		s.append(System.getProperty("java.vendor.url","") + "\n");
		s.append(Translator.localize("dialog.systeminfo.java-home-directory"));
		s.append(System.getProperty("java.home","") + "\n");
		s.append(Translator.localize("dialog.systeminfo.java-classpath"));
		s.append(System.getProperty("java.class.path","") + "\n");
		s.append(Translator.localize("dialog.systeminfo.operating-system"));
		s.append(System.getProperty("os.name",""));
		s.append(Translator.localize("dialog.systeminfo.operating-systemversion"));
		s.append(System.getProperty("os.version","") + "\n");
		s.append(Translator.localize("dialog.systeminfo.architecture"));
		s.append(System.getProperty("os.arch","") + "\n");
		s.append(Translator.localize("dialog.systeminfo.user-name"));
		s.append(System.getProperty("user.name","") + "\n");
		s.append(Translator.localize("dialog.systeminfo.user-home-directory"));
		s.append(System.getProperty("user.home","") + "\n");
		s.append(Translator.localize("dialog.systeminfo.current-directory"));
		s.append(System.getProperty("user.dir","") + "\n");
		s.append(Translator.localize("dialog.systeminfo.jvm-total-memory"));
		s.append(String.valueOf(Runtime.getRuntime().totalMemory()) + "\n");
		s.append(Translator.localize("dialog.systeminfo.jvm-free-memory"));
		s.append(String.valueOf(Runtime.getRuntime().freeMemory()) + "\n");
		return s.toString();
	}
	private static ClipboardOwner defaultClipboardOwner = new ClipboardObserver();
	static class ClipboardObserver implements ClipboardOwner {
	public void lostOwnership(Clipboard clipboard,Transferable contents) {
	}
}
}



