package org.argouml.ui.cmd;

import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import org.argouml.configuration.Configuration;
import org.argouml.ui.ActionExportXMI;
import org.argouml.ui.ActionImportXMI;
import org.argouml.ui.ActionProjectSettings;
import org.argouml.ui.ActionSettings;
import org.argouml.ui.ProjectActions;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.explorer.ActionPerspectiveConfig;
import org.argouml.uml.ui.ActionClassDiagram;
import org.argouml.uml.ui.ActionDeleteModelElements;
import org.argouml.uml.ui.ActionGenerateAll;
import org.argouml.uml.ui.ActionGenerateOne;
import org.argouml.uml.ui.ActionGenerateProjectCode;
import org.argouml.uml.ui.ActionGenerationSettings;
import org.argouml.uml.ui.ActionImportFromSources;
import org.argouml.uml.ui.ActionOpenProject;
import org.argouml.uml.ui.ActionRevertToSaved;
import org.argouml.uml.ui.ActionSaveAllGraphics;
import org.argouml.uml.ui.ActionSaveGraphics;
import org.argouml.uml.ui.ActionSaveProjectAs;
import org.argouml.util.KeyEventUtils;
import org.tigris.gef.base.AlignAction;
import org.tigris.gef.base.DistributeAction;
import org.tigris.gef.base.ReorderAction;
import org.tigris.gef.base.ZoomAction;
import org.argouml.ui.cmd.GenericArgoMenuBar;


public class ShortcutMgr {
	public static final String ACTION_NEW_PROJECT = "newProject";
	public static final String ACTION_OPEN_PROJECT = "openProject";
	public static final String ACTION_SAVE_PROJECT = "saveProject";
	public static final String ACTION_SAVE_PROJECT_AS = "saveProjectAs";
	public static final String ACTION_PRINT = "print";
	public static final String ACTION_SELECT_ALL = "selectAll";
	public static final String ACTION_UNDO = "undo";
	public static final String ACTION_REDO = "redo";
	public static final String ACTION_REMOVE_FROM_DIAGRAM = "removeFromDiagram";
	public static final String ACTION_DELETE_MODEL_ELEMENTS = "deleteModelElements";
	public static final String ACTION_ZOOM_OUT = "zoomOut";
	public static final String ACTION_ZOOM_IN = "zoomIn";
	public static final String ACTION_FIND = "find";
	public static final String ACTION_GENERATE_ALL_CLASSES = "generateAllClasses";
	public static final String ACTION_ALIGN_RIGHTS = "alignRights";
	public static final String ACTION_ALIGN_LEFTS = "alignLefts";
	public static final String ACTION_REVERT_TO_SAVED = "revertToSaved";
	public static final String ACTION_IMPORT_XMI = "importXmi";
	public static final String ACTION_EXPORT_XMI = "exportXmi";
	public static final String ACTION_IMPORT_FROM_SOURCES = "importFromSources";
	public static final String ACTION_PROJECT_SETTINGS = "projectSettings";
	public static final String ACTION_PAGE_SETUP = "pageSetup";
	public static final String ACTION_SAVE_GRAPHICS = "saveGraphics";
	public static final String ACTION_SAVE_ALL_GRAPHICS = "saveAllGraphics";
	public static final String ACTION_NAVIGATE_FORWARD = "navigateTargetForward";
	public static final String ACTION_NAVIGATE_BACK = "navigateTargetBack";
	public static final String ACTION_SELECT_INVERT = "selectInvert";
	public static final String ACTION_PERSPECTIVE_CONFIG = "perspectiveConfig";
	public static final String ACTION_SETTINGS = "settings";
	public static final String ACTION_NOTATION = "notation";
	public static final String ACTION_GO_TO_DIAGRAM = "goToDiagram";
	public static final String ACTION_ZOOM_RESET = "zoomReset";
	public static final String ACTION_ADJUST_GRID = "adjustGrid";
	public static final String ACTION_ADJUST_GUIDE = "adjustGuide";
	public static final String ACTION_ADJUST_PAGE_BREAKS = "adjustPageBreaks";
	public static final String ACTION_SHOW_XML_DUMP = "showXmlDump";
	public static final String ACTION_CLASS_DIAGRAM = "classDiagrams";
	public static final String ACTION_GENERATE_ONE = "generateOne";
	public static final String ACTION_GENERATE_PROJECT_CODE = "generateProjectCode";
	public static final String ACTION_GENERATION_SETTINGS = "generationSettings";
	public static final String ACTION_PREFERRED_SIZE = "preferredSize";
	public static final String ACTION_AUTO_CRITIQUE = "autoCritique";
	public static final String ACTION_OPEN_DECISIONS = "openDecisions";
	public static final String ACTION_OPEN_GOALS = "openGoals";
	public static final String ACTION_HELP = "help";
	public static final String ACTION_SYSTEM_INFORMATION = "systemInfo";
	public static final String ACTION_ABOUT_ARGOUML = "aboutArgoUml";
	public static final String ACTION_ALIGN_TOPS = "alignTops";
	public static final String ACTION_ALIGN_BOTTOMS = "alignBottoms";
	public static final String ACTION_ALIGN_H_CENTERS = "alignHCenters";
	public static final String ACTION_ALIGN_V_CENTERS = "alignVCenters";
	public static final String ACTION_ALIGN_TO_GRID = "alignToGrid";
	public static final String ACTION_DISTRIBUTE_H_SPACING = "distributeHSpacing";
	public static final String ACTION_DISTRIBUTE_H_CENTERS = "distributeHCenters";
	public static final String ACTION_DISTRIBUTE_V_SPACING = "distributeVSpacing";
	public static final String ACTION_DISTRIBUTE_V_CENTERS = "distributeVCenters";
	public static final String ACTION_REORDER_FORWARD = "reorderForward";
	public static final String ACTION_REORDER_BACKWARD = "reorderBackward";
	public static final String ACTION_REORDER_TO_FRONT = "reorderToFront";
	public static final String ACTION_REORDER_TO_BACK = "reorderToBack";
	private static final int DEFAULT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
	private static final int SHIFTED_DEFAULT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()|KeyEvent.SHIFT_DOWN_MASK;
	private static HashMap<String,ActionWrapper>shortcutHash = new HashMap<String,ActionWrapper>(90);
	private static HashMap<KeyStroke,KeyStroke>duplicate = new HashMap<KeyStroke,KeyStroke>(10);
	static ActionWrapper[]getShortcuts() {
		ActionWrapper[]actions = shortcutHash.values().toArray(new ActionWrapper[shortcutHash.size()]);
		Arrays.sort(actions,new Comparator<ActionWrapper>() {
			public int compare(ActionWrapper o1,ActionWrapper o2) {
				String name1 = o1.getActionName();
				if (name1 == null) {
					name1 = "";
				}
				String name2 = o2.getActionName();
				if (name2 == null) {
					name2 = "";
				}
				return name1.compareTo(name2);
			}
		});
		return actions;
	}
	public static void assignAccelerator(JMenuItem menuItem,String shortcutKey) {
		ActionWrapper shortcut = shortcutHash.get(shortcutKey);
		if (shortcut != null) {
			KeyStroke keyStroke = shortcut.getCurrentShortcut();
			if (keyStroke != null) {
				menuItem.setAccelerator(keyStroke);
			}
			KeyStroke alternativeKeyStroke = duplicate.get(keyStroke);
			if (alternativeKeyStroke != null) {
				String actionName = (String) menuItem.getAction().getValue(AbstractAction.NAME);
				menuItem.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(alternativeKeyStroke,actionName);
				menuItem.getActionMap().put(actionName,menuItem.getAction());
			}
		}
	}
	public static void assignAccelerator(JPanel panel,String shortcutKey) {
		ActionWrapper shortcut = shortcutHash.get(shortcutKey);
		if (shortcut != null) {
			KeyStroke keyStroke = shortcut.getCurrentShortcut();
			if (keyStroke != null) {
				panel.registerKeyboardAction(shortcut.getActionInstance(),keyStroke,JComponent.WHEN_FOCUSED);
			}
			KeyStroke alternativeKeyStroke = duplicate.get(keyStroke);
			if (alternativeKeyStroke != null) {
				String actionName = (String) shortcut.getActionInstance().getValue(AbstractAction.NAME);
				panel.getInputMap(JComponent.WHEN_FOCUSED).put(alternativeKeyStroke,actionName);
				panel.getActionMap().put(actionName,shortcut.getActionInstance());
			}
		}
	}
	static KeyStroke getDuplicate(KeyStroke keyStroke) {
		return duplicate.get(keyStroke);
	}
	public static ActionWrapper getShortcut(String actionId) {
		return shortcutHash.get(actionId);
	}
	private static void putDefaultShortcut(String shortcutKey,KeyStroke defaultKeyStroke,AbstractAction action) {
		putDefaultShortcut(shortcutKey,defaultKeyStroke,action,getActionDefaultName(action));
	}
	private static void putDefaultShortcut(String shortcutKey,KeyStroke defaultKeyStroke,AbstractAction action,String actionName) {
		String confCurrentShortcut = Configuration.getString(Configuration.makeKey(shortcutKey),null);
		KeyStroke currentKeyStroke = null;
		if (confCurrentShortcut == null) {
			currentKeyStroke = defaultKeyStroke;
		}else if (confCurrentShortcut.compareTo("") != 0) {
			currentKeyStroke = decodeKeyStroke(confCurrentShortcut);
		}
		ActionWrapper currentShortcut = new ActionWrapper(shortcutKey,currentKeyStroke,defaultKeyStroke,action,actionName);
		shortcutHash.put(shortcutKey,currentShortcut);
	}
	public static KeyStroke decodeKeyStroke(String strKeyStroke) {
		assert(strKeyStroke != null);
		StringTokenizer tokenizer = new StringTokenizer(strKeyStroke,KeyEventUtils.MODIFIER_JOINER);
		int modifiers = 0;
		while (tokenizer.hasMoreElements()) {
			String nextElement = (String) tokenizer.nextElement();
			if (tokenizer.hasMoreTokens()) {
				modifiers |= decodeModifier(nextElement);
			}else {
				try {
					Field f = KeyEvent.class.getField("VK_" + nextElement);
					return KeyStroke.getKeyStroke(f.getInt(null),modifiers);
				}catch (Exception exc) {
				}
			}
		}
		return null;
	}
	static void saveShortcuts(ActionWrapper[]newActions) {
		for (int i = 0;i < newActions.length;i++) {
			ActionWrapper oldAction = shortcutHash.get(newActions[i].getKey());
			if (newActions[i].getCurrentShortcut() == null&&newActions[i].getDefaultShortcut() != null) {
				Configuration.setString(Configuration.makeKey(oldAction.getKey()),"");
			}else if (newActions[i].getCurrentShortcut() != null&&!newActions[i].getCurrentShortcut().equals(newActions[i].getDefaultShortcut())) {
				Configuration.setString(Configuration.makeKey(oldAction.getKey()),KeyEventUtils.formatKeyStroke(newActions[i].getCurrentShortcut()));
			}else {
				Configuration.removeKey(Configuration.makeKey(oldAction.getKey()));
			}
		}
	}
	private static int decodeModifier(String modifier) {
		if (modifier == null||modifier.length() == 0) {
			return 0;
		}else if (modifier.equals(KeyEventUtils.CTRL_MODIFIER)) {
			return InputEvent.CTRL_DOWN_MASK;
		}else if (modifier.equals(KeyEventUtils.ALT_MODIFIER)) {
			return InputEvent.ALT_DOWN_MASK;
		}else if (modifier.equals(KeyEventUtils.ALT_GRAPH_MODIFIER)) {
			return InputEvent.ALT_GRAPH_DOWN_MASK;
		}else if (modifier.equals(KeyEventUtils.META_MODIFIER)) {
			return InputEvent.META_DOWN_MASK;
		}else if (modifier.equals(KeyEventUtils.SHIFT_MODIFIER)) {
			return InputEvent.SHIFT_DOWN_MASK;
		}else {
			return 0;
		}
	}
	private static String getActionDefaultName(AbstractAction action) {
		return(String) action.getValue(AbstractAction.NAME);
	}
	static {
	duplicate.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS,DEFAULT_MASK),KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT,DEFAULT_MASK));
	duplicate.put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT,DEFAULT_MASK),KeyStroke.getKeyStroke(KeyEvent.VK_MINUS,DEFAULT_MASK));
	duplicate.put(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS,DEFAULT_MASK),KeyStroke.getKeyStroke(KeyEvent.VK_ADD,DEFAULT_MASK));
	duplicate.put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD,DEFAULT_MASK),KeyStroke.getKeyStroke(KeyEvent.VK_PLUS,DEFAULT_MASK));
	putDefaultShortcut(ACTION_NEW_PROJECT,KeyStroke.getKeyStroke(KeyEvent.VK_N,DEFAULT_MASK),new ActionNew());
	putDefaultShortcut(ACTION_OPEN_PROJECT,KeyStroke.getKeyStroke(KeyEvent.VK_O,DEFAULT_MASK),new ActionOpenProject());
	putDefaultShortcut(ACTION_SAVE_PROJECT,KeyStroke.getKeyStroke(KeyEvent.VK_S,DEFAULT_MASK),ProjectBrowser.getInstance().getSaveAction());
	putDefaultShortcut(ACTION_SAVE_PROJECT_AS,null,new ActionSaveProjectAs());
	putDefaultShortcut(ACTION_REVERT_TO_SAVED,null,new ActionRevertToSaved());
	putDefaultShortcut(ACTION_IMPORT_XMI,null,new ActionImportXMI());
	putDefaultShortcut(ACTION_EXPORT_XMI,null,new ActionExportXMI());
	putDefaultShortcut(ACTION_IMPORT_FROM_SOURCES,null,ActionImportFromSources.getInstance());
	putDefaultShortcut(ACTION_PROJECT_SETTINGS,null,new ActionProjectSettings());
	putDefaultShortcut(ACTION_PAGE_SETUP,null,new ActionPageSetup());
	putDefaultShortcut(ACTION_SAVE_GRAPHICS,null,new ActionSaveGraphics());
	putDefaultShortcut(ACTION_SAVE_ALL_GRAPHICS,null,new ActionSaveAllGraphics());
	putDefaultShortcut(ACTION_NOTATION,null,new ActionNotation());
	putDefaultShortcut(ACTION_PRINT,KeyStroke.getKeyStroke(KeyEvent.VK_P,DEFAULT_MASK),new ActionPrint());
	putDefaultShortcut(ACTION_SELECT_ALL,KeyStroke.getKeyStroke(KeyEvent.VK_A,DEFAULT_MASK),new ActionSelectAll());
	putDefaultShortcut(ACTION_REDO,KeyStroke.getKeyStroke(KeyEvent.VK_Y,DEFAULT_MASK),ProjectActions.getInstance().getRedoAction());
	putDefaultShortcut(ACTION_UNDO,KeyStroke.getKeyStroke(KeyEvent.VK_Z,DEFAULT_MASK),ProjectActions.getInstance().getUndoAction());
	putDefaultShortcut(ACTION_NAVIGATE_FORWARD,null,new NavigateTargetForwardAction());
	putDefaultShortcut(ACTION_NAVIGATE_BACK,null,new NavigateTargetBackAction());
	putDefaultShortcut(ACTION_SELECT_INVERT,null,new ActionSelectInvert());
	putDefaultShortcut(ACTION_PERSPECTIVE_CONFIG,null,new ActionPerspectiveConfig());
	putDefaultShortcut(ACTION_SETTINGS,null,new ActionSettings());
	putDefaultShortcut(ACTION_REMOVE_FROM_DIAGRAM,KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0),ProjectActions.getInstance().getRemoveFromDiagramAction());
	putDefaultShortcut(ACTION_DELETE_MODEL_ELEMENTS,KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,DEFAULT_MASK),ActionDeleteModelElements.getTargetFollower());
	putDefaultShortcut(ACTION_GO_TO_DIAGRAM,null,new ActionGotoDiagram());
	putDefaultShortcut(ACTION_ZOOM_RESET,null,new ZoomAction(0.0));
	List gridActions = ActionAdjustGrid.createAdjustGridActions(true);
	Iterator i = gridActions.iterator();
	while (i.hasNext()) {
		AbstractAction cmdAG = (AbstractAction) i.next();
		putDefaultShortcut(ACTION_ADJUST_GRID + cmdAG.getValue("ID"),(KeyStroke) cmdAG.getValue("shortcut"),cmdAG);
	}
	List snapActions = ActionAdjustSnap.createAdjustSnapActions();
	i = snapActions.iterator();
	while (i.hasNext()) {
		AbstractAction cmdAS = (AbstractAction) i.next();
		putDefaultShortcut(ACTION_ADJUST_GUIDE + cmdAS.getValue("ID"),(KeyStroke) cmdAS.getValue("shortcut"),cmdAS);
	}
	putDefaultShortcut(ACTION_ADJUST_PAGE_BREAKS,null,new ActionAdjustPageBreaks());
	putDefaultShortcut(ACTION_SHOW_XML_DUMP,null,new ActionShowXMLDump());
	putDefaultShortcut(ACTION_ZOOM_IN,KeyStroke.getKeyStroke(KeyEvent.VK_PLUS,DEFAULT_MASK),new ZoomActionProxy((1.0) / (GenericArgoMenuBar.ZOOM_FACTOR)));
	putDefaultShortcut(ACTION_ZOOM_OUT,KeyStroke.getKeyStroke(KeyEvent.VK_MINUS,DEFAULT_MASK),new ZoomActionProxy(GenericArgoMenuBar.ZOOM_FACTOR));
	putDefaultShortcut(ACTION_FIND,KeyStroke.getKeyStroke(KeyEvent.VK_F3,0),new ActionFind());
	putDefaultShortcut(ACTION_CLASS_DIAGRAM,null,new ActionClassDiagram());
	putDefaultShortcut(ACTION_GENERATE_ONE,null,new ActionGenerateOne());
	putDefaultShortcut(ACTION_GENERATE_PROJECT_CODE,null,new ActionGenerateProjectCode());
	putDefaultShortcut(ACTION_GENERATION_SETTINGS,null,new ActionGenerationSettings());
	putDefaultShortcut(ACTION_GENERATE_ALL_CLASSES,KeyStroke.getKeyStroke(KeyEvent.VK_F7,0),new ActionGenerateAll());
	putDefaultShortcut(ACTION_SYSTEM_INFORMATION,null,new ActionSystemInfo());
	putDefaultShortcut(ACTION_ABOUT_ARGOUML,null,new ActionAboutArgoUML());
	putDefaultShortcut(ACTION_PREFERRED_SIZE,null,new CmdSetPreferredSize());
	putDefaultShortcut(ACTION_ALIGN_TOPS,null,new AlignAction(AlignAction.ALIGN_TOPS));
	putDefaultShortcut(ACTION_ALIGN_BOTTOMS,null,new AlignAction(AlignAction.ALIGN_BOTTOMS));
	putDefaultShortcut(ACTION_ALIGN_RIGHTS,KeyStroke.getKeyStroke(KeyEvent.VK_R,DEFAULT_MASK),new AlignAction(AlignAction.ALIGN_RIGHTS));
	putDefaultShortcut(ACTION_ALIGN_LEFTS,KeyStroke.getKeyStroke(KeyEvent.VK_L,DEFAULT_MASK),new AlignAction(AlignAction.ALIGN_LEFTS));
	putDefaultShortcut(ACTION_ALIGN_H_CENTERS,null,new AlignAction(AlignAction.ALIGN_H_CENTERS));
	putDefaultShortcut(ACTION_ALIGN_V_CENTERS,null,new AlignAction(AlignAction.ALIGN_V_CENTERS));
	putDefaultShortcut(ACTION_ALIGN_TO_GRID,null,new AlignAction(AlignAction.ALIGN_TO_GRID));
	putDefaultShortcut(ACTION_DISTRIBUTE_H_SPACING,null,new DistributeAction(DistributeAction.H_SPACING));
	putDefaultShortcut(ACTION_DISTRIBUTE_H_CENTERS,null,new DistributeAction(DistributeAction.H_CENTERS));
	putDefaultShortcut(ACTION_DISTRIBUTE_V_SPACING,null,new DistributeAction(DistributeAction.V_SPACING));
	putDefaultShortcut(ACTION_DISTRIBUTE_V_CENTERS,null,new DistributeAction(DistributeAction.V_CENTERS));
	putDefaultShortcut(ACTION_REORDER_FORWARD,KeyStroke.getKeyStroke(KeyEvent.VK_F,DEFAULT_MASK),new ReorderAction("Forward",ReorderAction.BRING_FORWARD));
	putDefaultShortcut(ACTION_REORDER_BACKWARD,KeyStroke.getKeyStroke(KeyEvent.VK_B,DEFAULT_MASK),new ReorderAction("Backward",ReorderAction.SEND_BACKWARD));
	putDefaultShortcut(ACTION_REORDER_TO_FRONT,KeyStroke.getKeyStroke(KeyEvent.VK_F,SHIFTED_DEFAULT_MASK),new ReorderAction("ToFront",ReorderAction.BRING_TO_FRONT));
	putDefaultShortcut(ACTION_REORDER_TO_BACK,KeyStroke.getKeyStroke(KeyEvent.VK_B,SHIFTED_DEFAULT_MASK),new ReorderAction("ToBack",ReorderAction.SEND_TO_BACK));
	putDefaultShortcut(ACTION_HELP,KeyStroke.getKeyStroke(KeyEvent.VK_F1,0),new ActionHelp());
}
}



