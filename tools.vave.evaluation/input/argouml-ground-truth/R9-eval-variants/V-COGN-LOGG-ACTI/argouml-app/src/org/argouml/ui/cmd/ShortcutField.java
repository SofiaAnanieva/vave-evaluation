package org.argouml.ui.cmd;

import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collections;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.EventListenerList;
import org.argouml.util.KeyEventUtils;


public class ShortcutField extends JTextField {
	private static final long serialVersionUID = -62483698420802557l;
	private EventListenerList listenerList = new EventListenerList();
	public ShortcutField(String text,int columns) {
		super(null,text,columns);
		this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,Collections.EMPTY_SET);
		this.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,Collections.EMPTY_SET);
		this.addKeyListener(new KeyListener() {
	private int currentKeyCode = 0;
	public void keyPressed(KeyEvent ke) {
		ke.consume();
		JTextField tf = (JTextField) ke.getSource();
		tf.setText(toString(ke));
	}
	public void keyReleased(KeyEvent ke) {
		ke.consume();
		JTextField tf = (JTextField) ke.getSource();
		switch (currentKeyCode) {case KeyEvent.VK_ALT:
		case KeyEvent.VK_ALT_GRAPH:
		case KeyEvent.VK_CONTROL:
		case KeyEvent.VK_SHIFT:
			tf.setText("");
			return;
		}
	}
	public void keyTyped(KeyEvent ke) {
		ke.consume();
	}
	private String toString(KeyEvent ke) {
		currentKeyCode = ke.getKeyCode();
		int keyCode = currentKeyCode;
		String modifText = KeyEventUtils.getModifiersText(ke.getModifiers());
		if ("".equals(modifText)) {
			if (KeyEventUtils.isActionEvent(ke)) {
				return KeyEventUtils.getKeyText(keyCode);
			}else {
				return"";
			}
		}else {
			switch (keyCode) {case KeyEvent.VK_ALT:
			case KeyEvent.VK_ALT_GRAPH:
			case KeyEvent.VK_CONTROL:
			case KeyEvent.VK_SHIFT:
				return modifText;
			default:
				modifText += KeyEventUtils.getKeyText(ke.getKeyCode());
				fireShortcutChangedEvent(modifText);
				return modifText;
			}
		}
	}
});
	}
	public void addShortcutChangedListener(ShortcutChangedListener listener) {
		listenerList.add(ShortcutChangedListener.class,listener);
	}
	protected void fireShortcutChangedEvent(String text) {
		ShortcutChangedEvent event = null;
		Object[]listeners = listenerList.getListenerList();
		KeyStroke keyStroke = ShortcutMgr.decodeKeyStroke(text);
		for (int i = listeners. - 2;i >= 0;i -= 2) {
			if (listeners[i] == ShortcutChangedListener.class) {
				if (event == null) {
					event = new ShortcutChangedEvent(this,keyStroke);
				}
				((ShortcutChangedListener) listeners[i + 1]).shortcutChange(event);
			}
		}
	}
}



