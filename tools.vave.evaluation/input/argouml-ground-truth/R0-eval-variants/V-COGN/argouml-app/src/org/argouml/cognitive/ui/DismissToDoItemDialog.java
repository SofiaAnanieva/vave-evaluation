package org.argouml.cognitive.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoList;
import org.argouml.cognitive.Translator;
import org.argouml.cognitive.UnresolvableException;
import org.argouml.util.ArgoDialog;
import org.tigris.swidgets.Dialog;


public class DismissToDoItemDialog extends ArgoDialog {
	private JRadioButton badGoalButton;
	private JRadioButton badDecButton;
	private JRadioButton explainButton;
	private ButtonGroup actionGroup;
	private JTextArea explanation;
	private ToDoItem target;
	public DismissToDoItemDialog() {
		super(Translator.localize("dialog.title.dismiss-todo-item"),Dialog.OK_CANCEL_OPTION,true);
		JLabel instrLabel = new JLabel(Translator.localize("label.remove-item"));
		badGoalButton = new JRadioButton(Translator.localize("button.not-relevant-to-my-goals"));
		badDecButton = new JRadioButton(Translator.localize("button.not-of-concern-at-moment"));
		explainButton = new JRadioButton(Translator.localize("button.reason-given-below"));
		badGoalButton.setMnemonic(Translator.localize("button.not-relevant-to-my-goals.mnemonic").charAt(0));
		badDecButton.setMnemonic(Translator.localize("button.not-of-concern-at-moment.mnemonic").charAt(0));
		explainButton.setMnemonic(Translator.localize("button.reason-given-below.mnemonic").charAt(0));
		JPanel content = new JPanel();
		GridBagLayout gb = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.gridwidth = 2;
		content.setLayout(gb);
		explanation = new JTextArea(6,40);
		explanation.setLineWrap(true);
		explanation.setWrapStyleWord(true);
		JScrollPane explain = new JScrollPane(explanation);
		c.gridx = 0;
		c.gridy = 0;
		gb.setConstraints(instrLabel,c);
		content.add(instrLabel);
		c.gridy = 1;
		c.insets = new Insets(5,0,0,0);
		gb.setConstraints(badGoalButton,c);
		content.add(badGoalButton);
		c.gridy = 2;
		gb.setConstraints(badDecButton,c);
		content.add(badDecButton);
		c.gridy = 3;
		gb.setConstraints(explainButton,c);
		content.add(explainButton);
		c.gridy = 4;
		c.weighty = 1.0;
		gb.setConstraints(explain,c);
		content.add(explain);
		setContent(content);
		getOkButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (badGoalButton.getModel().isSelected()) {
					badGoal(e);
				}else if (badDecButton.getModel().isSelected()) {
					badDec(e);
				}else if (explainButton.getModel().isSelected()) {
					explain(e);
				}
			}
		});
		actionGroup = new ButtonGroup();
		actionGroup.add(badGoalButton);
		actionGroup.add(badDecButton);
		actionGroup.add(explainButton);
		actionGroup.setSelected(explainButton.getModel(),true);
		explanation.setText(Translator.localize("label.enter-rationale-here"));
		badGoalButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				explanation.setEnabled(false);
			}
		});
		badDecButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				explanation.setEnabled(false);
			}
		});
		explainButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				explanation.setEnabled(true);
				explanation.requestFocus();
				explanation.selectAll();
			}
		});
	}
	public void setTarget(Object t) {
		target = (ToDoItem) t;
	}
	public void setVisible(boolean b) {
		super.setVisible(b);
		if (b) {
			explanation.requestFocus();
			explanation.selectAll();
		}
	}
	private void badGoal(ActionEvent e) {
		GoalsDialog d = new GoalsDialog();
		d.setVisible(true);
	}
	private void badDec(ActionEvent e) {
		DesignIssuesDialog d = new DesignIssuesDialog();
		d.setVisible(true);
	}
	private void explain(ActionEvent e) {
		ToDoList list = Designer.theDesigner().getToDoList();
		try {
			list.explicitlyResolve(target,explanation.getText());
			Designer.firePropertyChange(Designer.MODEL_TODOITEM_DISMISSED,null,null);
		}catch (UnresolvableException ure) {
			JOptionPane.showMessageDialog(this,ure.getMessage(),Translator.localize("optionpane.dismiss-failed"),JOptionPane.ERROR_MESSAGE);
		}
	}
}



