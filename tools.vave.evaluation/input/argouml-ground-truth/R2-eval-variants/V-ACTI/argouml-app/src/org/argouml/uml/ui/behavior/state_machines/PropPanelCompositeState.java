package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ui.ActionAddConcurrentRegion;


public class PropPanelCompositeState extends AbstractPropPanelState {
	private static final long serialVersionUID = 4758716706184949796l;
	private JList subverticesList = null;
	private Action addConcurrentRegion;
	public PropPanelCompositeState(final String name,final ImageIcon icon) {
		super(name,icon);
		initialize();
	}
	public PropPanelCompositeState() {
		super("label.composite-state",lookupIcon("CompositeState"));
		initialize();
		addField("label.name",getNameTextField());
		addField("label.container",getContainerScroll());
		addField("label.entry",getEntryScroll());
		addField("label.exit",getExitScroll());
		addField("label.do-activity",getDoScroll());
		addSeparator();
		addField("label.incoming",getIncomingScroll());
		addField("label.outgoing",getOutgoingScroll());
		addField("label.internal-transitions",getInternalTransitionsScroll());
		addSeparator();
		addField("label.subvertex",new JScrollPane(subverticesList));
	}
	@Override protected void addExtraButtons() {
		super.addExtraButtons();
		addConcurrentRegion = new ActionAddConcurrentRegion();
		addAction(addConcurrentRegion);
	}
	protected void updateExtraButtons() {
		addConcurrentRegion.setEnabled(addConcurrentRegion.isEnabled());
	}
	protected void initialize() {
		subverticesList = new UMLCompositeStateSubvertexList(new UMLCompositeStateSubvertexListModel());
	}
	@Override public void setTarget(final Object t) {
		super.setTarget(t);
		updateExtraButtons();
		final Object target = TargetManager.getInstance().getModelTarget();
		if (Model.getFacade().isAConcurrentRegion(target)) {
			getTitleLabel().setText(Translator.localize("label.concurrent.region"));
		}else if (Model.getFacade().isConcurrent(target)) {
			getTitleLabel().setText(Translator.localize("label.concurrent.composite.state"));
		}else if (!Model.getFacade().isASubmachineState(target)) {
			getTitleLabel().setText(Translator.localize("label.composite-state"));
		}
	}
}



