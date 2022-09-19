package org.argouml.uml.ui;

import java.awt.Font;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;
import org.argouml.model.Model;
import org.argouml.i18n.Translator;
import org.argouml.ui.LookAndFeelMgr;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.tigris.gef.presentation.Fig;


public abstract class UMLRadioButtonPanel extends JPanel implements TargetListener,PropertyChangeListener {
	private static Font stdFont = LookAndFeelMgr.getInstance().getStandardFont();
	private Object panelTarget;
	private String propertySetName;
	private ButtonGroup buttonGroup = new ButtonGroup();
	public UMLRadioButtonPanel(boolean isDoubleBuffered,String title,List<String[]>labeltextsActioncommands,String thePropertySetName,Action setAction,boolean horizontal) {
		super(isDoubleBuffered);
		setLayout(horizontal?new GridLayout():new GridLayout(0,1));
		setDoubleBuffered(true);
		if (Translator.localize(title) != null) {
			TitledBorder border = new TitledBorder(Translator.localize(title));
			border.setTitleFont(stdFont);
			setBorder(border);
		}
		setButtons(labeltextsActioncommands,setAction);
		setPropertySetName(thePropertySetName);
	}
	public UMLRadioButtonPanel(String title,List<String[]>labeltextsActioncommands,String thePropertySetName,Action setAction,boolean horizontal) {
		this(true,title,labeltextsActioncommands,thePropertySetName,setAction,horizontal);
	}
	private static List<String[]>toList(Map<String,String>map) {
		List<String[]>list = new ArrayList<String[]>();
		for (Map.Entry<String,String>entry:map.entrySet()) {
			list.add(new String[] {entry.getKey(),entry.getValue()});
		}
		return list;
	}
	private void setButtons(List<String[]>labeltextsActioncommands,Action setAction) {
		Enumeration en = buttonGroup.getElements();
		while (en.hasMoreElements()) {
			AbstractButton button = (AbstractButton) en.nextElement();
			buttonGroup.remove(button);
		}
		removeAll();
		buttonGroup.add(new JRadioButton());
		for (String[]keyAndLabelX:labeltextsActioncommands) {
			JRadioButton button = new JRadioButton(keyAndLabelX[0]);
			button.addActionListener(setAction);
			String actionCommand = keyAndLabelX[1];
			button.setActionCommand(actionCommand);
			button.setFont(LookAndFeelMgr.getInstance().getStandardFont());
			buttonGroup.add(button);
			add(button);
		}
	}
	public void propertyChange(PropertyChangeEvent e) {
		if (e.getPropertyName().equals(propertySetName)) {
			buildModel();
		}
	}
	public Object getTarget() {
		return panelTarget;
	}
	public void setTarget(Object target) {
		target = target instanceof Fig?((Fig) target).getOwner():target;
		if (Model.getFacade().isAModelElement(panelTarget)) {
			Model.getPump().removeModelEventListener(this,panelTarget,propertySetName);
		}
		panelTarget = target;
		if (Model.getFacade().isAModelElement(panelTarget)) {
			Model.getPump().addModelEventListener(this,panelTarget,propertySetName);
		}
		if (panelTarget != null) {
			buildModel();
		}
	}
	public String getPropertySetName() {
		return propertySetName;
	}
	public void setPropertySetName(String name) {
		propertySetName = name;
	}
	public abstract void buildModel();
	public void setSelected(String actionCommand) {
		Enumeration<AbstractButton>en = buttonGroup.getElements();
		if (actionCommand == null) {
			AbstractButton ab = en.nextElement();
			ab.setSelected(true);
			return;
		}
		while (en.hasMoreElements()) {
			AbstractButton b = en.nextElement();
			if (actionCommand.equals(b.getModel().getActionCommand())) {
				b.setSelected(true);
				break;
			}
		}
	}
	public void targetAdded(TargetEvent e) {
		setTarget(e.getNewTarget());
	}
	public void targetRemoved(TargetEvent e) {
		setTarget(e.getNewTarget());
	}
	public void targetSet(TargetEvent e) {
		setTarget(e.getNewTarget());
	}
}



