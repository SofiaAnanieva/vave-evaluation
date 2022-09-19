package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.Collections;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.apache.log4j.Logger;
import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.swingext.UpArrowIcon;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetManager;
import org.tigris.toolbar.ToolBarFactory;


public class TabText extends AbstractArgoJPanel implements TabModelTarget,DocumentListener {
	private Object target;
	private JTextArea textArea = new JTextArea();
	private boolean parseChanges = true;
	private boolean enabled;
	private JToolBar toolbar;
	private static final Logger LOG = Logger.getLogger(TabText.class);
	public TabText(String title) {
		this(title,false);
	}
	public TabText(String title,boolean withToolbar) {
		super(title);
		setIcon(new UpArrowIcon());
		setLayout(new BorderLayout());
		textArea.setFont(new Font("Monospaced",Font.PLAIN,12));
		textArea.setTabSize(4);
		add(new JScrollPane(textArea),BorderLayout.CENTER);
		textArea.getDocument().addDocumentListener(this);
		if (withToolbar) {
			toolbar = (new ToolBarFactory(Collections.EMPTY_LIST)).createToolBar();
			toolbar.setOrientation(SwingConstants.HORIZONTAL);
			toolbar.setFloatable(false);
			toolbar.setName(getTitle());
			add(toolbar,BorderLayout.NORTH);
		}
	}
	private void doGenerateText() {
		parseChanges = false;
		if (getTarget() == null) {
			textArea.setEnabled(false);
			textArea.setText("Nothing selected");
			enabled = false;
		}else {
			textArea.setEnabled(true);
			if (isVisible()) {
				String generatedText = genText(getTarget());
				if (generatedText != null) {
					textArea.setText(generatedText);
					enabled = true;
					textArea.setCaretPosition(0);
				}else {
					textArea.setEnabled(false);
					textArea.setText("N/A");
					enabled = false;
				}
			}
		}
		parseChanges = true;
	}
	public void setTarget(Object t) {
		target = t;
		if (isVisible()) {
			doGenerateText();
		}
	}
	public Object getTarget() {
		return target;
	}
	public void refresh() {
		Object t = TargetManager.getInstance().getTarget();
		setTarget(t);
	}
	public boolean shouldBeEnabled(Object t) {
		return(t != null);
	}
	protected String genText(Object t) {
		return t == null?"Nothing selected":t.toString();
	}
	protected void parseText(String s) {
		if (s == null) {
			s = "(null)";
		}
		LOG.debug("parsing text:" + s);
	}
	public void insertUpdate(DocumentEvent e) {
		if (parseChanges) {
			parseText(textArea.getText());
		}
	}
	public void removeUpdate(DocumentEvent e) {
		if (parseChanges) {
			parseText(textArea.getText());
		}
	}
	public void changedUpdate(DocumentEvent e) {
		if (parseChanges) {
			parseText(textArea.getText());
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
	protected JToolBar getToolbar() {
		return toolbar;
	}
	protected void setShouldBeEnabled(boolean s) {
		this.enabled = s;
	}
	protected boolean shouldBeEnabled() {
		return enabled;
	}
	public void setEditable(boolean editable) {
		textArea.setEditable(editable);
	}
	@Override public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			doGenerateText();
		}
	}
	private static final long serialVersionUID = -1484647093166393888l;
}



