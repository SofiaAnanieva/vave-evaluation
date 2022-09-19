package org.argouml.cognitive.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import org.argouml.cognitive.critics.Wizard;
import org.argouml.swingext.SpacerPanel;


public class WizStepConfirm extends WizStep {
	private JTextArea instructions = new JTextArea();
	private WizStepConfirm() {
		instructions.setEditable(false);
		instructions.setBorder(null);
		instructions.setBackground(getMainPanel().getBackground());
		instructions.setWrapStyleWord(true);
		getMainPanel().setBorder(new EtchedBorder());
		GridBagLayout gb = new GridBagLayout();
		getMainPanel().setLayout(gb);
		GridBagConstraints c = new GridBagConstraints();
		c.ipadx = 3;
		c.ipady = 3;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.anchor = GridBagConstraints.EAST;
		JLabel image = new JLabel("");
		image.setIcon(getWizardIcon());
		image.setBorder(null);
		c.gridx = 0;
		c.gridheight = 4;
		c.gridy = 0;
		gb.setConstraints(image,c);
		getMainPanel().add(image);
		c.weightx = 1.0;
		c.gridx = 2;
		c.gridheight = 1;
		c.gridwidth = 3;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		gb.setConstraints(instructions,c);
		getMainPanel().add(instructions);
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 0.0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		SpacerPanel spacer = new SpacerPanel();
		gb.setConstraints(spacer,c);
		getMainPanel().add(spacer);
	}
	public WizStepConfirm(Wizard w,String instr) {
		this();
		instructions.setText(instr);
	}
	private static final long serialVersionUID = 9145817515169354813l;
}



