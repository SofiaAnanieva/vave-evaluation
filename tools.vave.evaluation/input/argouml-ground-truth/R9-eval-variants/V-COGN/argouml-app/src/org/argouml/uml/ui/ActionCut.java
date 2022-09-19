package org.argouml.uml.ui;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Collection;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.JTextComponent;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.tigris.gef.base.CutAction;
import org.tigris.gef.base.Globals;


public class ActionCut extends AbstractAction implements CaretListener {
	private static ActionCut instance = new ActionCut();
	private static final String LOCALIZE_KEY = "action.cut";
	public ActionCut() {
		super(Translator.localize(LOCALIZE_KEY));
		Icon icon = ResourceLoaderWrapper.lookupIcon(LOCALIZE_KEY);
		if (icon != null) {
			putValue(Action.SMALL_ICON,icon);
		}
		putValue(Action.SHORT_DESCRIPTION,Translator.localize(LOCALIZE_KEY) + " ");
	}
	public static ActionCut getInstance() {
		return instance;
	}
	private JTextComponent textSource;
	public void actionPerformed(ActionEvent ae) {
		if (textSource == null) {
			if (removeFromDiagramAllowed()) {
				CutAction cmd = new CutAction(Translator.localize("action.cut"));
				cmd.actionPerformed(ae);
			}
		}else {
			textSource.cut();
		}
		if (isSystemClipBoardEmpty()&&Globals.clipBoard == null||Globals.clipBoard.isEmpty()) {
			ActionPaste.getInstance().setEnabled(false);
		}else {
			ActionPaste.getInstance().setEnabled(true);
		}
	}
	private boolean removeFromDiagramAllowed() {
		return false;
	}
	public void caretUpdate(CaretEvent e) {
		if (e.getMark() != e.getDot()) {
			setEnabled(true);
			textSource = (JTextComponent) e.getSource();
		}else {
			Collection figSelection = Globals.curEditor().getSelectionManager().selections();
			if (figSelection == null||figSelection.isEmpty()) {
				setEnabled(false);
			}else {
				setEnabled(true);
			}
			textSource = null;
		}
	}
	private boolean isSystemClipBoardEmpty() {
		boolean hasContents = false;
		Transferable content = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
		DataFlavor[]flavors = content.getTransferDataFlavors();
		try {
			for (int i = 0;i < flavors.;i++) {
				if (content.getTransferData(flavors[i]) != null) {
					hasContents = true;
					break;
				}
			}
		}catch (UnsupportedFlavorException ignorable) {
		}catch (IOException ignorable) {
		}
		return!hasContents;
	}
}



