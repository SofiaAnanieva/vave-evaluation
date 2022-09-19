package org.argouml.uml.ui;

import java.awt.Component;
import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.UIManager;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;


public class UMLListCellRenderer2 extends DefaultListCellRenderer {
	private boolean showIcon;
	private boolean showPath;
	public UMLListCellRenderer2(boolean showTheIcon) {
		this(showTheIcon,true);
	}
	public UMLListCellRenderer2(boolean showTheIcon,boolean showThePath) {
		updateUI();
		setAlignmentX(LEFT_ALIGNMENT);
		showIcon = showTheIcon;
		showPath = showThePath;
	}
	public Component getListCellRendererComponent(JList list,Object value,int index,boolean isSelected,boolean cellHasFocus) {
		if (Model.getFacade().isAUMLElement(value)) {
			String text = makeText(value);
			setText(text);
			if (showIcon) {
				setComponentOrientation(list.getComponentOrientation());
				if (isSelected) {
					setForeground(list.getSelectionForeground());
					setBackground(list.getSelectionBackground());
				}else {
					setForeground(list.getForeground());
					setBackground(list.getBackground());
				}
				setEnabled(list.isEnabled());
				setFont(list.getFont());
				setBorder((cellHasFocus)?UIManager.getBorder("List.focusCellHighlightBorder"):noFocusBorder);
				setIcon(ResourceLoaderWrapper.getInstance().lookupIcon(value));
			}else {
				return super.getListCellRendererComponent(list,text,index,isSelected,cellHasFocus);
			}
		}else if (value instanceof String) {
			return super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
		}else if (value == null||value.equals("")) {
			JLabel label = new JLabel(" ");
			label.setIcon(null);
			return label;
		}
		return this;
	}
	public String makeText(Object value) {
		if (value instanceof String) {
			return(String) value;
		}
		String name = null;
		if (Model.getFacade().isAParameter(value)) {
			Object type = Model.getFacade().getType(value);
			name = getName(value);
			String typeName = null;
			if (type != null)typeName = Model.getFacade().getName(type);
			if (typeName != null||"".equals(typeName)) {
				name = Translator.localize("misc.name.withType",new Object[] {name,typeName});
			}
			return name;
		}
		if (Model.getFacade().isAUMLElement(value)) {
			try {
				name = getName(value);
				if (Model.getFacade().isAStereotype(value)) {
					String baseString = "";
					Iterator bases = Model.getFacade().getBaseClasses(value).iterator();
					if (bases.hasNext()) {
						baseString = makeText(bases.next());
						while (bases.hasNext()) {
							baseString = Translator.localize("misc.name.baseClassSeparator",new Object[] {baseString,makeText(bases.next())});
						}
					}
					name = Translator.localize("misc.name.withBaseClasses",new Object[] {name,baseString});
				}else if (showPath) {
					List pathList = Model.getModelManagementHelper().getPathList(value);
					String path;
					if (pathList.size() > 1) {
						path = (String) pathList.get(0);
						for (int i = 1;i < pathList.size() - 1;i++) {
							String n = (String) pathList.get(i);
							path = Translator.localize("misc.name.pathSeparator",new Object[] {path,n});
						}
						name = Translator.localize("misc.name.withPath",new Object[] {name,path});
					}
				}
			}catch (InvalidElementException e) {
				name = Translator.localize("misc.name.deleted");
			}
		}else if (Model.getFacade().isAMultiplicity(value)) {
			name = Model.getFacade().getName(value);
		}else {
			name = makeTypeName(value);
		}
		return name;
	}
	private String getName(Object value) {
		String name = Model.getFacade().getName(value);
		if (name == null||name.equals("")) {
			name = Translator.localize("misc.name.unnamed",new Object[] {makeTypeName(value)});
		}
		return name;
	}
	private String makeTypeName(Object elem) {
		if (Model.getFacade().isAUMLElement(elem)) {
			return Model.getFacade().getUMLClassName(elem);
		}
		return null;
	}
}



