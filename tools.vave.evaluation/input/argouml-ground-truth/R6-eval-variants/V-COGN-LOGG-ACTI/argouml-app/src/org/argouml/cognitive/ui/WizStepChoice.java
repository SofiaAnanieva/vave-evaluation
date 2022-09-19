package org.argouml.cognitive.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import org.argouml.cognitive.critics.Wizard;
import org.argouml.swingext.SpacerPanel;


public class WizStepChoice extends WizStep {
	private JTextArea instructions = new JTextArea();
	private List<String>choices = new ArrayList<String>();
	private int selectedIndex = -1;
	public WizStepChoice(Wizard w,String instr,List<String>ch) {
		choices = ch;
		instructions.setText(instr);
		instructions.setWrapStyleWord(true);
		instructions.setEditable(false);
		instructions.setBorder(null);
		instructions.setBackground(getMainPanel().getBackground());
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
		c.gridheight = GridBagConstraints.REMAINDER;
		c.gridy = 0;
		c.anchor = GridBagConstraints.NORTH;
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
		c.gridx = 2;
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.WEST;
		c.gridwidth = 1;
		int size = ch.size();
		for (int i = 0;i < size;i++) {
			c.gridy = 2 + i;
			String s = ch.get(i);
			JRadioButton rb = new JRadioButton(s);
			rb.setActionCommand(s);
			rb.addActionListener(this);
			gb.setConstraints(rb,c);
			getMainPanel().add(rb);
		}
		c.gridx = 1;
		c.gridy = 3 + ch.size();
		c.weightx = 0.0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		SpacerPanel spacer2 = new SpacerPanel();
		gb.setConstraints(spacer2,c);
		getMainPanel().add(spacer2);
	}
	public int getSelectedIndex() {
		return selectedIndex;
	}
	@Override public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getSource()instanceof JRadioButton) {
			String cmd = e.getActionCommand();
			if (cmd == null) {
				selectedIndex = -1;
				return;
			}
			int size = choices.size();
			for (int i = 0;i < size;i++) {
				String s = choices.get(i);
				if (s.equals(cmd)) {
					selectedIndex = i;
				}
			}
			getWizard().doAction();
			enableButtons();
		}
	}
	private static final long serialVersionUID = 8055896491830976354l;
}



