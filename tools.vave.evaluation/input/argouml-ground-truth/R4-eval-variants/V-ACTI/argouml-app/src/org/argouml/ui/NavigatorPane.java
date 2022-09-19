package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import org.argouml.i18n.Translator;
import org.argouml.ui.explorer.ActionPerspectiveConfig;
import org.argouml.ui.explorer.DnDExplorerTree;
import org.argouml.ui.explorer.ExplorerTree;
import org.argouml.ui.explorer.ExplorerTreeModel;
import org.argouml.ui.explorer.NameOrder;
import org.argouml.ui.explorer.PerspectiveComboBox;
import org.argouml.ui.explorer.PerspectiveManager;
import org.argouml.ui.explorer.TypeThenNameOrder;
import org.tigris.toolbar.ToolBarFactory;


class NavigatorPane extends JPanel {
	public NavigatorPane(SplashScreen splash) {
		JComboBox perspectiveCombo = new PerspectiveComboBox();
		JComboBox orderByCombo = new JComboBox();
		ExplorerTree tree = new DnDExplorerTree();
		Collection<Object>toolbarTools = new ArrayList<Object>();
		toolbarTools.add(new ActionPerspectiveConfig());
		toolbarTools.add(perspectiveCombo);
		JToolBar toolbar = (new ToolBarFactory(toolbarTools)).createToolBar();
		toolbar.setFloatable(false);
		orderByCombo.addItem(new TypeThenNameOrder());
		orderByCombo.addItem(new NameOrder());
		Collection<Object>toolbarTools2 = new ArrayList<Object>();
		toolbarTools2.add(orderByCombo);
		JToolBar toolbar2 = (new ToolBarFactory(toolbarTools2)).createToolBar();
		toolbar2.setFloatable(false);
		JPanel toolbarpanel = new JPanel();
		toolbarpanel.setLayout(new BorderLayout());
		toolbarpanel.add(toolbar,BorderLayout.NORTH);
		toolbarpanel.add(toolbar2,BorderLayout.SOUTH);
		setLayout(new BorderLayout());
		add(toolbarpanel,BorderLayout.NORTH);
		add(new JScrollPane(tree),BorderLayout.CENTER);
		if (splash != null) {
			splash.getStatusBar().showStatus(Translator.localize("statusmsg.bar.making-navigator-pane-perspectives"));
			splash.getStatusBar().showProgress(25);
		}
		perspectiveCombo.addItemListener((ExplorerTreeModel) tree.getModel());
		orderByCombo.addItemListener((ExplorerTreeModel) tree.getModel());
		PerspectiveManager.getInstance().loadUserPerspectives();
	}
	public Dimension getMinimumSize() {
		return new Dimension(120,100);
	}
	private static final long serialVersionUID = 8403903607517813289l;
}



