package org.argouml.ui.cmd;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.argouml.application.api.Argo;
import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;
import org.argouml.uml.ui.ActionReopenProject;
import java.io.File;


public class LastRecentlyUsedMenuList {
	private static final int MAX_COUNT_DEFAULT = 4;
	private JMenu fileMenu;
	private int lruCount;
	private int maxCount = MAX_COUNT_DEFAULT;
	private int menuIndex = -1;
	private JMenuItem[]menuItems;
	private ConfigurationKey[]confKeys;
	private JMenuItem addEventHandler(String filename,int addAt) {
		File f = new File(filename);
		JMenuItem item = fileMenu.insert(new ActionReopenProject(filename),addAt);
		String entryName = f.getName();
		if (entryName.length() > 40) {
			entryName = entryName.substring(0,40) + "...";
		}
		item.setText(entryName);
		item.setToolTipText(filename);
		return item;
	}
	public LastRecentlyUsedMenuList(JMenu filemenu) {
		String newName;
		int i;
		fileMenu = filemenu;
		lruCount = 0;
		menuIndex = filemenu.getItemCount();
		maxCount = Configuration.getInteger(Argo.KEY_NUMBER_LAST_RECENT_USED,MAX_COUNT_DEFAULT);
		Configuration.setInteger(Argo.KEY_NUMBER_LAST_RECENT_USED,maxCount);
		confKeys = new ConfigurationKey[maxCount];
		menuItems = new JMenuItem[maxCount];
		for (i = 0;i < maxCount;i++) {
			confKeys[i] = Configuration.makeKey("project","mostrecent","filelist".concat(Integer.toString(i)));
		}
		i = 0;
		boolean readOK = true;
		while (i < maxCount&&readOK) {
			newName = Configuration.getString(confKeys[i],"");
			if (newName.length() > 0) {
				menuItems[i] = addEventHandler(newName,menuIndex + i);
				i++;
			}else {
				readOK = false;
			}
		}
		lruCount = i;
	}
	public void addEntry(String filename) {
		String[]tempNames = new String[maxCount];
		for (int i = 0;i < lruCount;i++) {
			ActionReopenProject action = (ActionReopenProject) menuItems[i].getAction();
			tempNames[i] = action.getFilename();
		}
		for (int i = 0;i < lruCount;i++) {
			fileMenu.remove(menuItems[i]);
		}
		menuItems[0] = addEventHandler(filename,menuIndex);
		int i,j;
		i = 0;
		j = 1;
		while (i < lruCount&&j < maxCount) {
			if (!(tempNames[i].equals(filename))) {
				menuItems[j] = addEventHandler(tempNames[i],menuIndex + j);
				j++;
			}
			i++;
		}
		lruCount = j;
		for (int k = 0;k < lruCount;k++) {
			ActionReopenProject action = (ActionReopenProject) menuItems[k].getAction();
			Configuration.setString(confKeys[k],action.getFilename());
		}
	}
}



