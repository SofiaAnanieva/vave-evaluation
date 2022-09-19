package org.argouml.uml.ui;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;


class SourcePathTableModel extends DefaultTableModel {
	static final int MODEL_ELEMENT_COLUMN = 0;
	static final int NAME_COLUMN = 1;
	static final int TYPE_COLUMN = 2;
	static final int SOURCE_PATH_COLUMN = 3;
	private static final Logger LOG = Logger.getLogger(SourcePathTableModel.class);
	public SourcePathTableModel(SourcePathController srcPathCtrl) {
		super(new Object[][] {},new String[] {" ",Translator.localize("misc.name"),Translator.localize("misc.type"),Translator.localize("misc.source-path")});
		String strModel = Translator.localize("misc.model");
		String strPackage = Translator.localize("misc.package");
		String strClass = Translator.localize("misc.class");
		String strInterface = Translator.localize("misc.interface");
		Collection elems = srcPathCtrl.getAllModelElementsWithSourcePath();
		Iterator iter = elems.iterator();
		while (iter.hasNext()) {
			Object me = iter.next();
			File path = srcPathCtrl.getSourcePath(me);
			if (path != null) {
				String type = "";
				String name = Model.getFacade().getName(me);
				if (Model.getFacade().isAModel(me)) {
					type = strModel;
				}else if (Model.getFacade().isAPackage(me)) {
					type = strPackage;
					Object parent = Model.getFacade().getNamespace(me);
					while (parent != null) {
						if (Model.getFacade().getNamespace(parent) != null) {
							name = Model.getFacade().getName(parent) + "." + name;
						}
						parent = Model.getFacade().getNamespace(parent);
					}
				}else if (Model.getFacade().isAClass(me)) {
					type = strClass;
				}else if (Model.getFacade().isAInterface(me)) {
					type = strInterface;
				}else {
					LOG.warn("Can\'t assign a type to this model element: " + me);
				}
				addRow(new Object[] {me,name,type,path.toString()});
			}else {
				LOG.warn("Unexpected: the source path for " + me + " is null!");
			}
		}
	}
	public boolean isCellEditable(int rowIndex,int columnIndex) {
		return columnIndex == SOURCE_PATH_COLUMN;
	}
	public Object getModelElement(int rowIndex) {
		return getValueAt(rowIndex,MODEL_ELEMENT_COLUMN);
	}
	public String getMEName(int rowIndex) {
		return(String) getValueAt(rowIndex,NAME_COLUMN);
	}
	public String getMEType(int rowIndex) {
		return(String) getValueAt(rowIndex,TYPE_COLUMN);
	}
	public String getMESourcePath(int rowIndex) {
		return(String) getValueAt(rowIndex,SOURCE_PATH_COLUMN);
	}
}



