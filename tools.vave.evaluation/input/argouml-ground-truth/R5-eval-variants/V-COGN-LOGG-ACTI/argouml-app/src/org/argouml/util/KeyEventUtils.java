package org.argouml.util;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import javax.swing.KeyStroke;


public class KeyEventUtils {
	public static final String MODIFIER_JOINER = " + ";
	public static final String SHIFT_MODIFIER = "SHIFT";
	public static final String CTRL_MODIFIER = "CTRL";
	public static final String META_MODIFIER = "META";
	public static final String ALT_MODIFIER = "ALT";
	public static final String ALT_GRAPH_MODIFIER = "altGraph";
	public static final boolean isActionEvent(KeyEvent evt) {
		int keyCode = evt.getKeyCode();
		switch (keyCode) {case KeyEvent.VK_BACK_SPACE:
		case KeyEvent.VK_DELETE:
		case KeyEvent.VK_CANCEL:
		case KeyEvent.VK_HOME:
		case KeyEvent.VK_END:
		case KeyEvent.VK_PAGE_UP:
		case KeyEvent.VK_PAGE_DOWN:
		case KeyEvent.VK_UP:
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_KP_LEFT:
		case KeyEvent.VK_KP_UP:
		case KeyEvent.VK_KP_RIGHT:
		case KeyEvent.VK_KP_DOWN:
		case KeyEvent.VK_F1:
		case KeyEvent.VK_F2:
		case KeyEvent.VK_F3:
		case KeyEvent.VK_F4:
		case KeyEvent.VK_F5:
		case KeyEvent.VK_F6:
		case KeyEvent.VK_F7:
		case KeyEvent.VK_F8:
		case KeyEvent.VK_F9:
		case KeyEvent.VK_F10:
		case KeyEvent.VK_F11:
		case KeyEvent.VK_F12:
		case KeyEvent.VK_F13:
		case KeyEvent.VK_F14:
		case KeyEvent.VK_F15:
		case KeyEvent.VK_F16:
		case KeyEvent.VK_F17:
		case KeyEvent.VK_F18:
		case KeyEvent.VK_F19:
		case KeyEvent.VK_F20:
		case KeyEvent.VK_F21:
		case KeyEvent.VK_F22:
		case KeyEvent.VK_F23:
		case KeyEvent.VK_F24:
		case KeyEvent.VK_PRINTSCREEN:
		case KeyEvent.VK_SCROLL_LOCK:
		case KeyEvent.VK_CAPS_LOCK:
		case KeyEvent.VK_NUM_LOCK:
		case KeyEvent.VK_PAUSE:
		case KeyEvent.VK_INSERT:
		case KeyEvent.VK_FINAL:
		case KeyEvent.VK_CONVERT:
		case KeyEvent.VK_NONCONVERT:
		case KeyEvent.VK_ACCEPT:
		case KeyEvent.VK_MODECHANGE:
		case KeyEvent.VK_KANA:
		case KeyEvent.VK_KANJI:
		case KeyEvent.VK_ALPHANUMERIC:
		case KeyEvent.VK_KATAKANA:
		case KeyEvent.VK_HIRAGANA:
		case KeyEvent.VK_FULL_WIDTH:
		case KeyEvent.VK_HALF_WIDTH:
		case KeyEvent.VK_ROMAN_CHARACTERS:
		case KeyEvent.VK_ALL_CANDIDATES:
		case KeyEvent.VK_PREVIOUS_CANDIDATE:
		case KeyEvent.VK_CODE_INPUT:
		case KeyEvent.VK_JAPANESE_KATAKANA:
		case KeyEvent.VK_JAPANESE_HIRAGANA:
		case KeyEvent.VK_JAPANESE_ROMAN:
		case KeyEvent.VK_KANA_LOCK:
		case KeyEvent.VK_INPUT_METHOD_ON_OFF:
		case KeyEvent.VK_AGAIN:
		case KeyEvent.VK_UNDO:
		case KeyEvent.VK_COPY:
		case KeyEvent.VK_PASTE:
		case KeyEvent.VK_CUT:
		case KeyEvent.VK_FIND:
		case KeyEvent.VK_PROPS:
		case KeyEvent.VK_STOP:
		case KeyEvent.VK_HELP:
			return true;
		}
		return false;
	}
	public static String getKeyText(int keyCode) {
		int expectedModifiers = (Modifier.PUBLIC|Modifier.STATIC|Modifier.FINAL);
		Field[]fields = KeyEvent.class.getDeclaredFields();
		for (int i = 0;i < fields.;i++) {
			try {
				if (fields[i].getModifiers() == expectedModifiers&&fields[i].getType() == Integer.TYPE&&fields[i].getName().startsWith("VK_")&&fields[i].getInt(KeyEvent.class) == keyCode) {
					return fields[i].getName().substring(3);
				}
			}catch (IllegalAccessException e) {
			}
		}
		return"UNKNOWN";
	}
	public static String getModifiersText(int modifiers) {
		StringBuffer buf = new StringBuffer();
		if ((modifiers&InputEvent.SHIFT_MASK) != 0) {
			buf.append(SHIFT_MODIFIER).append(MODIFIER_JOINER);
		}
		if ((modifiers&InputEvent.CTRL_MASK) != 0) {
			buf.append(CTRL_MODIFIER).append(MODIFIER_JOINER);
		}
		if ((modifiers&InputEvent.META_MASK) != 0) {
			buf.append(META_MODIFIER).append(MODIFIER_JOINER);
		}
		if ((modifiers&InputEvent.ALT_MASK) != 0) {
			buf.append(ALT_MODIFIER).append(MODIFIER_JOINER);
		}
		if ((modifiers&InputEvent.ALT_GRAPH_MASK) != 0) {
			buf.append(ALT_GRAPH_MODIFIER).append(MODIFIER_JOINER);
		}
		return buf.toString();
	}
	public static String formatKeyStroke(KeyStroke keyStroke) {
		if (keyStroke != null) {
			return getModifiersText(keyStroke.getModifiers()) + KeyEventUtils.getKeyText(keyStroke.getKeyCode());
		}else {
			return"";
		}
	}
}



