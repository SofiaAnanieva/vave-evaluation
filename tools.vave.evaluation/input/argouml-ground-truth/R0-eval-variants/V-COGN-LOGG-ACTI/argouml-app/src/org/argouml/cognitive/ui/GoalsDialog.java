package org.argouml.cognitive.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Hashtable;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.Goal;
import org.argouml.cognitive.GoalModel;
import org.argouml.cognitive.Translator;
import org.argouml.util.ArgoDialog;


public class GoalsDialog extends ArgoDialog implements ChangeListener {
	private static final int DIALOG_WIDTH = 320;
	private static final int DIALOG_HEIGHT = 400;
	private JPanel mainPanel = new JPanel();
	private Hashtable<JSlider,Goal>slidersToGoals = new Hashtable<JSlider,Goal>();
	private Hashtable<JSlider,JLabel>slidersToDigits = new Hashtable<JSlider,JLabel>();
	public GoalsDialog() {
		super(Translator.localize("dialog.title.design-goals"),false);
		initMainPanel();
		JScrollPane scroll = new JScrollPane(mainPanel);
		scroll.setPreferredSize(new Dimension(DIALOG_WIDTH,DIALOG_HEIGHT));
		setContent(scroll);
	}
	private void initMainPanel() {
		GoalModel gm = Designer.theDesigner().getGoalModel();
		List<Goal>goals = gm.getGoalList();
		GridBagLayout gb = new GridBagLayout();
		mainPanel.setLayout(gb);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.ipadx = 3;
		c.ipady = 3;
		c.gridy = 1;
		for (Goal goal:goals) {
			JLabel decLabel = new JLabel(goal.getName());
			JLabel valueLabel = new JLabel("    " + goal.getPriority());
			JSlider decSlide = new JSlider(SwingConstants.HORIZONTAL,0,5,goal.getPriority());
			decSlide.setPaintTicks(true);
			decSlide.setPaintLabels(true);
			decSlide.addChangeListener(this);
			Dimension origSize = decSlide.getPreferredSize();
			Dimension smallSize = new Dimension(origSize.width / 2,origSize.height);
			decSlide.setSize(smallSize);
			decSlide.setPreferredSize(smallSize);
			slidersToGoals.put(decSlide,goal);
			slidersToDigits.put(decSlide,valueLabel);
			c.gridx = 0;
			c.gridwidth = 1;
			c.weightx = 0.0;
			c.ipadx = 3;
			gb.setConstraints(decLabel,c);
			mainPanel.add(decLabel);
			c.gridx = 1;
			c.gridwidth = 1;
			c.weightx = 0.0;
			c.ipadx = 0;
			gb.setConstraints(valueLabel,c);
			mainPanel.add(valueLabel);
			c.gridx = 2;
			c.gridwidth = 6;
			c.weightx = 1.0;
			gb.setConstraints(decSlide,c);
			mainPanel.add(decSlide);
			c.gridy++;
		}
	}
	public void stateChanged(ChangeEvent ce) {
		JSlider srcSlider = (JSlider) ce.getSource();
		Goal goal = slidersToGoals.get(srcSlider);
		JLabel valLab = slidersToDigits.get(srcSlider);
		int pri = srcSlider.getValue();
		goal.setPriority(pri);
		if (pri == 0) {
			valLab.setText(Translator.localize("label.off"));
		}else {
			valLab.setText("    " + pri);
		}
	}
	private static final long serialVersionUID = -1871200638199122363l;
}



