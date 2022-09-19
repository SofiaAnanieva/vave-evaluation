package org.argouml.ui;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.notation.Notation;
import org.argouml.notation.NotationProvider;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.notation.NotationSettings;
import org.argouml.notation.providers.uml.NotationUtilityUml;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.ui.UMLTreeCellRenderer;


public class DisplayTextTree extends JTree {
	private static final Logger LOG = Logger.getLogger(DisplayTextTree.class);
	private Hashtable<TreeModel,List<TreePath>>expandedPathsInModel;
	private boolean reexpanding;
	private boolean showStereotype;
	public DisplayTextTree() {
		super();
		setCellRenderer(new UMLTreeCellRenderer());
		setRootVisible(false);
		setShowsRootHandles(true);
		setToolTipText("Tree");
		setRowHeight(18);
		expandedPathsInModel = new Hashtable<TreeModel,List<TreePath>>();
		reexpanding = false;
	}
	public String convertValueToText(Object value,boolean selected,boolean expanded,boolean leaf,int row,boolean hasFocus) {
		if (Model.getFacade().isAModelElement(value)) {
			String name = null;
			try {
				if (Model.getFacade().isATransition(value)) {
					name = formatTransitionLabel(value);
				}else if (Model.getFacade().isAExtensionPoint(value)) {
					name = formatExtensionPoint(value);
				}else if (Model.getFacade().isAComment(value)) {
					name = (String) Model.getFacade().getBody(value);
				}else if (Model.getFacade().isATaggedValue(value)) {
					name = formatTaggedValueLabel(value);
				}else {
					name = getModelElementDisplayName(value);
				}
				if (name != null&&name.indexOf("\n") < 80&&name.indexOf("\n") > -1) {
					name = name.substring(0,name.indexOf("\n")) + "...";
				}else if (name != null&&name.length() > 80) {
					name = name.substring(0,80) + "...";
				}
				if (showStereotype) {
					Collection<Object>stereos = Model.getFacade().getStereotypes(value);
					name += " " + generateStereotype(stereos);
					if (name != null&&name.length() > 80) {
						name = name.substring(0,80) + "...";
					}
				}
			}catch (InvalidElementException e) {
				name = Translator.localize("misc.name.deleted");
			}
			return name;
		}
		if (Model.getFacade().isAElementImport(value)) {
			try {
				Object me = Model.getFacade().getImportedElement(value);
				String typeName = Model.getFacade().getUMLClassName(me);
				String elemName = convertValueToText(me,selected,expanded,leaf,row,hasFocus);
				String alias = Model.getFacade().getAlias(value);
				if (alias != null&&alias.length() > 0) {
					Object[]args =  {typeName,elemName,alias};
					return Translator.localize("misc.name.element-import.alias",args);
				}else {
					Object[]args =  {typeName,elemName};
					return Translator.localize("misc.name.element-import",args);
				}
			}catch (InvalidElementException e) {
				return Translator.localize("misc.name.deleted");
			}
		}
		if (Model.getFacade().isAUMLElement(value)) {
			try {
				return Model.getFacade().toString(value);
			}catch (InvalidElementException e) {
				return Translator.localize("misc.name.deleted");
			}
		}
		if (value instanceof ArgoDiagram) {
			return((ArgoDiagram) value).getName();
		}
		if (value != null) {
			return value.toString();
		}
		return"-";
	}
	private String formatExtensionPoint(Object value) {
		NotationSettings settings = getNotationSettings();
		NotationProvider notationProvider = NotationProviderFactory2.getInstance().getNotationProvider(NotationProviderFactory2.TYPE_EXTENSION_POINT,value,Notation.findNotation(settings.getNotationLanguage()));
		String name = notationProvider.toString(value,settings);
		return name;
	}
	private static NotationSettings getNotationSettings() {
		Project p = ProjectManager.getManager().getCurrentProject();
		NotationSettings settings;
		if (p != null) {
			settings = p.getProjectSettings().getNotationSettings();
		}else {
			settings = NotationSettings.getDefaultSettings();
		}
		return settings;
	}
	private String formatTaggedValueLabel(Object value) {
		String name;
		String tagName = Model.getFacade().getTag(value);
		if (tagName == null||tagName.equals("")) {
			name = MessageFormat.format(Translator.localize("misc.unnamed"),new Object[] {Model.getFacade().getUMLClassName(value)});
		}
		Collection referenceValues = Model.getFacade().getReferenceValue(value);
		Collection dataValues = Model.getFacade().getDataValue(value);
		Iterator i;
		if (referenceValues.size() > 0) {
			i = referenceValues.iterator();
		}else {
			i = dataValues.iterator();
		}
		String theValue = "";
		if (i.hasNext()) {
			theValue = i.next().toString();
		}
		if (i.hasNext()) {
			theValue += " , ...";
		}
		name = (tagName + " = " + theValue);
		return name;
	}
	private String formatTransitionLabel(Object value) {
		String name;
		name = Model.getFacade().getName(value);
		NotationProvider notationProvider = NotationProviderFactory2.getInstance().getNotationProvider(NotationProviderFactory2.TYPE_TRANSITION,value);
		String signature = notationProvider.toString(value,NotationSettings.getDefaultSettings());
		if (name != null&&name.length() > 0) {
			name += ": " + signature;
		}else {
			name = signature;
		}
		return name;
	}
	public static String generateStereotype(Collection<Object>st) {
		return NotationUtilityUml.generateStereotype(st,getNotationSettings().isUseGuillemets());
	}
	public static final String getModelElementDisplayName(Object modelElement) {
		String name = Model.getFacade().getName(modelElement);
		if (name == null||name.equals("")) {
			name = MessageFormat.format(Translator.localize("misc.unnamed"),new Object[] {Model.getFacade().getUMLClassName(modelElement)});
		}
		return name;
	}
	public void fireTreeExpanded(TreePath path) {
		super.fireTreeExpanded(path);
		LOG.debug("fireTreeExpanded");
		if (reexpanding||path == null) {
			return;
		}
		List<TreePath>expanded = getExpandedPaths();
		expanded.remove(path);
		expanded.add(path);
	}
	public void fireTreeCollapsed(TreePath path) {
		super.fireTreeCollapsed(path);
		LOG.debug("fireTreeCollapsed");
		if (path == null||expandedPathsInModel == null) {
			return;
		}
		List<TreePath>expanded = getExpandedPaths();
		expanded.remove(path);
	}
	public void setModel(TreeModel newModel) {
		LOG.debug("setModel");
		Object r = newModel.getRoot();
		if (r != null) {
			super.setModel(newModel);
		}
		reexpand();
	}
	protected List<TreePath>getExpandedPaths() {
		LOG.debug("getExpandedPaths");
		TreeModel tm = getModel();
		List<TreePath>res = expandedPathsInModel.get(tm);
		if (res == null) {
			res = new ArrayList<TreePath>();
			expandedPathsInModel.put(tm,res);
		}
		return res;
	}
	private void reexpand() {
		LOG.debug("reexpand");
		if (expandedPathsInModel == null) {
			return;
		}
		reexpanding = true;
		for (TreePath path:getExpandedPaths()) {
			expandPath(path);
		}
		reexpanding = false;
	}
	protected void setShowStereotype(boolean show) {
		this.showStereotype = show;
	}
	private static final long serialVersionUID = 949560309817566838l;
}



