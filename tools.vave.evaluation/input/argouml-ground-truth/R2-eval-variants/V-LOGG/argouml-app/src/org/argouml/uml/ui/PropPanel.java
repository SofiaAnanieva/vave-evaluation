package org.argouml.uml.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.EventListenerList;
import org.apache.log4j.Logger;
import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProfileConfiguration;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.ActionCreateContainedModelElement;
import org.argouml.ui.LookAndFeelMgr;
import org.argouml.ui.TabModelTarget;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargettableModelView;
import org.tigris.gef.presentation.Fig;
import org.tigris.swidgets.GridLayout2;
import org.tigris.swidgets.Orientation;
import org.tigris.toolbar.ToolBarFactory;
import org.argouml.uml.ui.UMLChangeDispatch;
import org.argouml.uml.ui.UMLUserInterfaceContainer;
import org.argouml.uml.ui.UMLSingleRowSelector;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLModelElementListModel2;


public abstract class PropPanel extends AbstractArgoJPanel implements TabModelTarget,UMLUserInterfaceContainer,ComponentListener {
	private static final Logger LOG = Logger.getLogger(PropPanel.class);
	private Object target;
	private Object modelElement;
	private EventListenerList listenerList;
	private JPanel buttonPanel = new JPanel(new GridLayout());
	private JLabel titleLabel;
	private List actions = new ArrayList();
	private static Font stdFont = LookAndFeelMgr.getInstance().getStandardFont();
	public PropPanel(String label,ImageIcon icon) {
		super(Translator.localize(label));
		LabelledLayout layout = new LabelledLayout();
		layout.setHgap(5);
		setLayout(layout);
		if (icon != null) {
			setTitleLabel(new JLabel(Translator.localize(label),icon,SwingConstants.LEFT));
		}else {
			setTitleLabel(new JLabel(Translator.localize(label)));
		}
		titleLabel.setLabelFor(buttonPanel);
		add(titleLabel);
		add(buttonPanel);
		addComponentListener(this);
	}
	@Override public void setOrientation(Orientation orientation) {
		super.setOrientation(orientation);
	}
	protected void addAction(Action action) {
		actions.add(action);
	}
	protected void addAction(Action action,String tooltip) {
		JButton button = new TargettableButton(action);
		if (tooltip != null) {
			button.setToolTipText(tooltip);
		}
		button.setText("");
		button.setFocusable(false);
		actions.add(button);
	}
	protected void addAction(Object[]actionArray) {
		actions.add(actionArray);
	}
	public void buildToolbar() {
		LOG.debug("Building toolbar");
		ToolBarFactory factory = new ToolBarFactory(getActions());
		factory.setRollover(true);
		factory.setFloatable(false);
		JToolBar toolBar = factory.createToolBar();
		toolBar.setName("misc.toolbar.properties");
		buttonPanel.removeAll();
		buttonPanel.add(BorderLayout.WEST,toolBar);
		buttonPanel.putClientProperty("ToolBar.toolTipSelectTool",Translator.localize("action.select"));
	}
	protected List getActions() {
		return actions;
	}
	private static class TargettableButton extends JButton implements TargettableModelView {
	public TargettableButton(Action action) {
		super(action);
	}
	public TargetListener getTargettableModel() {
		if (getAction()instanceof TargetListener) {
			return(TargetListener) getAction();
		}
		return null;
	}
}
	public JLabel addField(String label,Component component) {
		JLabel jlabel = createLabelFor(label,component);
		component.setFont(stdFont);
		add(jlabel);
		add(component);
		if (component instanceof UMLLinkedList) {
			UMLModelElementListModel2 list = (UMLModelElementListModel2) ((UMLLinkedList) component).getModel();
			ActionCreateContainedModelElement newAction = new ActionCreateContainedModelElement(list.getMetaType(),list.getTarget(),"New...");
		}
		return jlabel;
	}
	private JLabel createLabelFor(String label,Component comp) {
		JLabel jlabel = new JLabel(Translator.localize(label));
		jlabel.setToolTipText(Translator.localize(label));
		jlabel.setFont(stdFont);
		jlabel.setLabelFor(comp);
		return jlabel;
	}
	public JLabel addFieldAfter(String label,Component component,Component afterComponent) {
		int nComponent = getComponentCount();
		for (int i = 0;i < nComponent;++i) {
			if (getComponent(i) == afterComponent) {
				JLabel jlabel = createLabelFor(label,component);
				component.setFont(stdFont);
				add(jlabel,++i);
				add(component,++i);
				return jlabel;
			}
		}
		throw new IllegalArgumentException("Component not found");
	}
	public JLabel addFieldBefore(String label,Component component,Component beforeComponent) {
		int nComponent = getComponentCount();
		for (int i = 0;i < nComponent;++i) {
			if (getComponent(i) == beforeComponent) {
				JLabel jlabel = createLabelFor(label,component);
				component.setFont(stdFont);
				add(jlabel,i - 1);
				add(component,i++);
				return jlabel;
			}
		}
		throw new IllegalArgumentException("Component not found");
	}
	protected final void addSeparator() {
		add(LabelledLayout.getSeparator());
	}
	public void setTarget(Object t) {
		LOG.debug("setTarget called with " + t + " as parameter (not target!)");
		t = (t instanceof Fig)?((Fig) t).getOwner():t;
		Runnable dispatch = null;
		if (t != target) {
			target = t;
			modelElement = null;
			if (listenerList == null) {
				listenerList = collectTargetListeners(this);
			}
			if (Model.getFacade().isAUMLElement(target)) {
				modelElement = target;
			}
			dispatch = new UMLChangeDispatch(this,UMLChangeDispatch.TARGET_CHANGED_ADD);
			buildToolbar();
		}else {
			dispatch = new UMLChangeDispatch(this,UMLChangeDispatch.TARGET_REASSERTED);
		}
		SwingUtilities.invokeLater(dispatch);
		if (titleLabel != null) {
			Icon icon = null;
			if (t != null) {
				icon = ResourceLoaderWrapper.getInstance().lookupIcon(t);
			}
			if (icon != null) {
				titleLabel.setIcon(icon);
			}
		}
	}
	private EventListenerList collectTargetListeners(Container container) {
		Component[]components = container.getComponents();
		EventListenerList list = new EventListenerList();
		for (int i = 0;i < components.;i++) {
			if (components[i]instanceof TargetListener) {
				list.add(TargetListener.class,(TargetListener) components[i]);
			}
			if (components[i]instanceof TargettableModelView) {
				list.add(TargetListener.class,((TargettableModelView) components[i]).getTargettableModel());
			}
			if (components[i]instanceof Container) {
				EventListenerList list2 = collectTargetListeners((Container) components[i]);
				Object[]objects = list2.getListenerList();
				for (int j = 1;j < objects.;j += 2) {
					list.add(TargetListener.class,(TargetListener) objects[j]);
				}
			}
		}
		if (container instanceof PropPanel) {
			for (TargetListener action:collectTargetListenerActions()) {
				list.add(TargetListener.class,action);
			}
		}
		return list;
	}
	private Collection<TargetListener>collectTargetListenerActions() {
		Collection<TargetListener>set = new HashSet<TargetListener>();
		for (Object obj:actions) {
			if (obj instanceof TargetListener) {
				set.add((TargetListener) obj);
			}
		}
		return set;
	}
	public final Object getTarget() {
		return target;
	}
	public void refresh() {
		SwingUtilities.invokeLater(new UMLChangeDispatch(this,0));
	}
	public boolean shouldBeEnabled(Object t) {
		t = (t instanceof Fig)?((Fig) t).getOwner():t;
		return Model.getFacade().isAUMLElement(t);
	}
	protected Object getDisplayNamespace() {
		Object ns = null;
		Object theTarget = getTarget();
		if (Model.getFacade().isAModelElement(theTarget)) {
			ns = Model.getFacade().getNamespace(theTarget);
		}
		return ns;
	}
	public ProfileConfiguration getProfile() {
		return ProjectManager.getManager().getCurrentProject().getProfileConfiguration();
	}
	public final Object getModelElement() {
		return modelElement;
	}
	public String formatElement(Object element) {
		return getProfile().getFormatingStrategy().formatElement(element,getDisplayNamespace());
	}
	public String formatNamespace(Object namespace) {
		return getProfile().getFormatingStrategy().formatElement(namespace,null);
	}
	public String formatCollection(Iterator iter) {
		Object namespace = getDisplayNamespace();
		return getProfile().getFormatingStrategy().formatCollection(iter,namespace);
	}
	protected final Action getDeleteAction() {
		return ActionDeleteModelElements.getTargetFollower();
	}
	public boolean isRemovableElement() {
		return((getTarget() != null)&&(getTarget() != (ProjectManager.getManager().getCurrentProject().getModel())));
	}
	public void targetAdded(TargetEvent e) {
		if (listenerList == null) {
			listenerList = collectTargetListeners(this);
		}
		setTarget(e.getNewTarget());
		if (isVisible()) {
			fireTargetAdded(e);
		}
	}
	public void targetRemoved(TargetEvent e) {
		setTarget(e.getNewTarget());
		if (isVisible()) {
			fireTargetRemoved(e);
		}
	}
	public void targetSet(TargetEvent e) {
		setTarget(e.getNewTarget());
		if (isVisible()) {
			fireTargetSet(e);
		}
	}
	private void fireTargetSet(TargetEvent targetEvent) {
		if (listenerList == null) {
			listenerList = collectTargetListeners(this);
		}
		Object[]listeners = listenerList.getListenerList();
		for (int i = listeners. - 2;i >= 0;i -= 2) {
			if (listeners[i] == TargetListener.class) {
				((TargetListener) listeners[i + 1]).targetSet(targetEvent);
			}
		}
	}
	private void fireTargetAdded(TargetEvent targetEvent) {
		if (listenerList == null) {
			listenerList = collectTargetListeners(this);
		}
		Object[]listeners = listenerList.getListenerList();
		for (int i = listeners. - 2;i >= 0;i -= 2) {
			if (listeners[i] == TargetListener.class) {
				((TargetListener) listeners[i + 1]).targetAdded(targetEvent);
			}
		}
	}
	private void fireTargetRemoved(TargetEvent targetEvent) {
		if (listenerList == null) {
			listenerList = collectTargetListeners(this);
		}
		Object[]listeners = listenerList.getListenerList();
		for (int i = listeners. - 2;i >= 0;i -= 2) {
			if (listeners[i] == TargetListener.class) {
				((TargetListener) listeners[i + 1]).targetRemoved(targetEvent);
			}
		}
	}
	protected void setTitleLabel(JLabel theTitleLabel) {
		titleLabel = theTitleLabel;
		titleLabel.setFont(stdFont);
	}
	protected JLabel getTitleLabel() {
		return titleLabel;
	}
	protected final JPanel createBorderPanel(String title) {
		return new GroupPanel(Translator.localize(title));
	}
	private class GroupPanel extends JPanel {
	public GroupPanel(String title) {
		super(new GridLayout2());
		TitledBorder border = new TitledBorder(Translator.localize(title));
		border.setTitleFont(stdFont);
		setBorder(border);
	}
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		for (final Component component:getComponents()) {
			component.setEnabled(enabled);
		}
	}
}
	protected void setButtonPanelSize(int height) {
		buttonPanel.setMinimumSize(new Dimension(0,height));
		buttonPanel.setPreferredSize(new Dimension(0,height));
	}
	protected static ImageIcon lookupIcon(String name) {
		return ResourceLoaderWrapper.lookupIconResource(name);
	}
	public void componentHidden(ComponentEvent e) {
	}
	public void componentShown(ComponentEvent e) {
		fireTargetSet(new TargetEvent(this,TargetEvent.TARGET_SET,null,new Object[] {target}));
	}
	public void componentMoved(ComponentEvent e) {
	}
	public void componentResized(ComponentEvent e) {
	}
	protected UMLSingleRowSelector getSingleRowScroll(ListModel model) {
		UMLSingleRowSelector pane = new UMLSingleRowSelector(model);
		return pane;
	}
}



