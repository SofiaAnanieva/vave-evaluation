package org.argouml.ui.cmd;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.KeyStroke;
import org.argouml.application.api.Argo;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerGrid;


public class ActionAdjustGrid extends AbstractAction {
	private final Map<String,Comparable>myMap;
	private static final String DEFAULT_ID = "03";
	private static ButtonGroup myGroup;
	private static final int DEFAULT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
	private ActionAdjustGrid(final Map<String,Comparable>map,final String name) {
		super();
		myMap = map;
		putValue(Action.NAME,name);
	}
	public void actionPerformed(final ActionEvent e) {
		final Editor editor = Globals.curEditor();
		if (editor != null) {
			final Layer grid = editor.getLayerManager().findLayerNamed("Grid");
			if (grid instanceof LayerGrid) {
				if (myMap != null) {
					if (myMap instanceof HashMap) {
						grid.adjust((HashMap<String,Comparable>) myMap);
					}else {
						grid.adjust(new HashMap<String,Comparable>(myMap));
					}
					Configuration.setString(Argo.KEY_GRID,(String) getValue("ID"));
				}
			}
		}
	}
	static void setGroup(final ButtonGroup group) {
		myGroup = group;
	}
	static void init() {
		String id = Configuration.getString(Argo.KEY_GRID,DEFAULT_ID);
		List<Action>actions = createAdjustGridActions(false);
		for (Action a:actions) {
			if (a.getValue("ID").equals(id)) {
				a.actionPerformed(null);
				if (myGroup != null) {
					for (Enumeration e = myGroup.getElements();e.hasMoreElements();) {
						AbstractButton ab = (AbstractButton) e.nextElement();
						Action action = ab.getAction();
						if (action instanceof ActionAdjustGrid) {
							String currentID = (String) action.getValue("ID");
							if (id.equals(currentID)) {
								myGroup.setSelected(ab.getModel(),true);
								return;
							}
						}
					}
				}
				return;
			}
		}
	}
	static List<Action>createAdjustGridActions(final boolean longStrings) {
		List<Action>result = new ArrayList<Action>();
		result.add(buildGridAction(longStrings?"action.adjust-grid.lines-16":"menu.item.lines-16",16,true,true,"01",KeyEvent.VK_1));
		result.add(buildGridAction(longStrings?"action.adjust-grid.lines-8":"menu.item.lines-8",8,true,true,"02",KeyEvent.VK_2));
		result.add(buildGridAction(longStrings?"action.adjust-grid.dots-16":"menu.item.dots-16",16,false,true,"03",KeyEvent.VK_3));
		result.add(buildGridAction(longStrings?"action.adjust-grid.dots-32":"menu.item.dots-32",32,false,true,"04",KeyEvent.VK_4));
		result.add(buildGridAction(longStrings?"action.adjust-grid.none":"menu.item.none",16,false,false,"05",KeyEvent.VK_5));
		return result;
	}
	public static Action buildGridAction(final String property,final int spacing,final boolean paintLines,final boolean paintDots,final String id,final int key) {
		String name = Translator.localize(property);
		HashMap<String,Comparable>map1 = new HashMap<String,Comparable>(4);
		map1.put("spacing",Integer.valueOf(spacing));
		map1.put("paintLines",Boolean.valueOf(paintLines));
		map1.put("paintDots",Boolean.valueOf(paintDots));
		Action action = new ActionAdjustGrid(map1,name);
		action.putValue("ID",id);
		action.putValue("shortcut",KeyStroke.getKeyStroke(key,DEFAULT_MASK));
		return action;
	}
}



