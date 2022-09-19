package org.argouml.ui.cmd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import org.apache.log4j.Logger;
import org.argouml.ui.ActionSettings;
import org.argouml.ui.ArgoJMenu;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.uml.ui.ActionOpenProject;


public class GenericArgoMenuBar extends JMenuBar implements TargetListener {
	private static final Logger LOG = Logger.getLogger(GenericArgoMenuBar.class);
	private static List<JMenu>moduleMenus = new ArrayList<JMenu>();
	private static List<Action>moduleCreateDiagramActions = new ArrayList<Action>();
	private Collection<Action>disableableActions = new ArrayList<Action>();
	public static final double ZOOM_FACTOR = 0.9;
	private static final String MENU = "menu.";
	private static final String MENUITEM = "menu.item.";
	private JToolBar fileToolbar;
	private JToolBar editToolbar;
	private JToolBar viewToolbar;
	private JToolBar createDiagramToolbar;
	private LastRecentlyUsedMenuList mruList;
	private JMenu edit;
	private JMenu select;
	private ArgoJMenu view;
	private JMenu createDiagramMenu;
	private JMenu tools;
	private JMenu generate;
	private ArgoJMenu arrange;
	private JMenu help;
	private Action navigateTargetForwardAction;
	private Action navigateTargetBackAction;
	private ActionSettings settingsAction;
	private ActionAboutArgoUML aboutAction;
	private ActionExit exitAction;
	private ActionOpenProject openAction;
	public GenericArgoMenuBar() {
	}
	private void initActions() {
	}
	protected static final void setMnemonic(JMenuItem item,String key) {
	}
	protected static final String menuLocalize(String key) {
		return null;
	}
	static final String menuItemLocalize(String key) {
		return null;
	}
	protected void initMenus() {
	}
	private void initModulesUI() {
	}
	private void initMenuFile() {
	}
	private void initMenuEdit() {
	}
	private void initMenuView() {
	}
	private void initMenuCreate() {
	}
	private void initMenuArrange() {
	}
	private static void initAlignMenu(JMenu align) {
	}
	private static void initDistributeMenu(JMenu distribute) {
	}
	private static void initReorderMenu(JMenu reorder) {
	}
	private void initMenuGeneration() {
	}
	private void initMenuTools() {
	}
	private void initMenuHelp() {
	}
	private void initModulesMenus() {
	}
	private void initModulesActions() {
	}
	public JToolBar getCreateDiagramToolbar() {
		return null;
	}
	public JMenu getCreateDiagramMenu() {
		return null;
	}
	public JToolBar getEditToolbar() {
		return null;
	}
	public JToolBar getFileToolbar() {
		return null;
	}
	public JToolBar getViewToolbar() {
		return null;
	}
	private static String prepareKey(String str) {
		return null;
	}
	public void addFileSaved(String filename) {
	}
	public JMenu getTools() {
		return null;
	}
	private static final long serialVersionUID = 2904074534530273119l;
	private void setTarget() {
	}
	public void targetAdded(TargetEvent e) {
	}
	public void targetRemoved(TargetEvent e) {
	}
	public void targetSet(TargetEvent e) {
	}
	public static void registerMenuItem(JMenu menu) {
	}
	public static void registerCreateDiagramAction(Action action) {
	}
	private void registerForMacEvents() {
	}
	public void macQuit() {
	}
	public void macAbout() {
	}
	public void macPreferences() {
	}
	public void macOpenFile(String filename) {
	}
}



