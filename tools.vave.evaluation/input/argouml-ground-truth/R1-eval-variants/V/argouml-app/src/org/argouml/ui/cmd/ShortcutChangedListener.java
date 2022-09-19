package org.argouml.ui.cmd;

import java.util.EventListener;


public interface ShortcutChangedListener extends EventListener {
	void shortcutChange(ShortcutChangedEvent event);
}



