package org.argouml.uml.ui.behavior.common_behavior;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;


public abstract class PropPanelInstance extends PropPanelModelElement {
	private JPanel stimuliSenderScroll;
	private JPanel stimuliReceiverScroll;
	private static UMLInstanceSenderStimulusListModel stimuliSenderListModel = new UMLInstanceSenderStimulusListModel();
	private static UMLInstanceReceiverStimulusListModel stimuliReceiverListModel = new UMLInstanceReceiverStimulusListModel();
	public PropPanelInstance(String name,ImageIcon icon) {
		super(name,icon);
	}
	protected JPanel getStimuliSenderScroll() {
		if (stimuliSenderScroll == null) {
			stimuliSenderScroll = getSingleRowScroll(stimuliSenderListModel);
		}
		return stimuliSenderScroll;
	}
	protected JPanel getStimuliReceiverScroll() {
		if (stimuliReceiverScroll == null) {
			stimuliReceiverScroll = getSingleRowScroll(stimuliReceiverListModel);
		}
		return stimuliReceiverScroll;
	}
}



