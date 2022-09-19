package org.argouml.uml.ui;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.JTextComponent;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.tigris.gef.base.CmdCopy;
import org.tigris.gef.base.Globals;


public class ActionCopy extends AbstractAction implements CaretListener {
	private static ActionCopy instance = new ActionCopy();
	private static final String LOCALIZE_KEY = "action.copy";
	public ActionCopy() {
		super(Translator.localize(LOCALIZE_KEY));
		Icon icon = ResourceLoaderWrapper.lookupIcon(LOCALIZE_KEY);
		if (icon != null) {
			putValue(Action.SMALL_ICON,icon);
		}
		putValue(Action.SHORT_DESCRIPTION,Translator.localize(LOCALIZE_KEY) + " ");
	}
	public static ActionCopy getInstance() {
		return instance;
	}
	private JTextComponent textSource;
	public void actionPerformed(ActionEvent ae) {
		if (textSource != null) {
			textSource.copy();
			Globals.clipBoard = null;
		}else {
			CmdCopy cmd = new CmdCopy();
			cmd.doIt();
		}
		if (isSystemClipBoardEmpty()&&(Globals.clipBoard == null||Globals.clipBoard.isEmpty())) {
			ActionPaste.getInstance().setEnabled(false);
		}else {
			ActionPaste.getInstance().setEnabled(true);
		}
	}
	public void caretUpdate(CaretEvent e) {
		if (e.getMark() != e.getDot()) {
			setEnabled(true);
			textSource = (JTextComponent) e.getSource();
		}else {
			setEnabled(false);
			textSource = null;
		}
	}
	private boolean isSystemClipBoardEmpty() {
		try {
			Object text = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null).getTransferData(DataFlavor.stringFlavor);
			return text == null;
		}catch (IOException ignorable) {
		}catch (UnsupportedFlavorException ignorable) {
		}
		return true;
	}
}



