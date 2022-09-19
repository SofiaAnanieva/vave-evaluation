package org.argouml.uml.ui;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.JTextComponent;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.tigris.gef.base.Globals;


public class ActionPaste extends AbstractAction implements CaretListener,FocusListener {
	private static ActionPaste instance = new ActionPaste();
	private static final String LOCALIZE_KEY = "action.paste";
	public ActionPaste() {
		super(Translator.localize(LOCALIZE_KEY));
		Icon icon = ResourceLoaderWrapper.lookupIcon(LOCALIZE_KEY);
		if (icon != null) {
			putValue(Action.SMALL_ICON,icon);
		}
		putValue(Action.SHORT_DESCRIPTION,Translator.localize(LOCALIZE_KEY) + " ");
		setEnabled(false);
	}
	public static ActionPaste getInstance() {
		return instance;
	}
	private JTextComponent textSource;
	public void actionPerformed(ActionEvent ae) {
		if (Globals.clipBoard != null&&!Globals.clipBoard.isEmpty()) {
		}else {
			if (!isSystemClipBoardEmpty()&&textSource != null) {
				textSource.paste();
			}
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
	public void focusLost(FocusEvent e) {
		if (e.getSource() == textSource) {
			textSource = null;
		}
	}
	public void caretUpdate(CaretEvent e) {
		textSource = (JTextComponent) e.getSource();
	}
	public void focusGained(FocusEvent e) {
		textSource = (JTextComponent) e.getSource();
	}
}



