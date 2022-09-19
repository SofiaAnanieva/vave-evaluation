package org.argouml.cognitive.ui;

import java.awt.BorderLayout;
import java.text.MessageFormat;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.apache.log4j.Logger;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Decision;
import org.argouml.cognitive.Goal;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.Translator;
import org.argouml.model.Model;


public class WizDescription extends WizStep {
	private static final Logger LOG = Logger.getLogger(WizDescription.class);
	private JTextArea description = new JTextArea();
	public WizDescription() {
		super();
		LOG.info("making WizDescription");
		description.setLineWrap(true);
		description.setWrapStyleWord(true);
		getMainPanel().setLayout(new BorderLayout());
		getMainPanel().add(new JScrollPane(description),BorderLayout.CENTER);
	}
	public void setTarget(Object item) {
		String message = "";
		super.setTarget(item);
		Object target = item;
		if (target == null) {
			description.setEditable(false);
			description.setText(Translator.localize("message.item.no-item-selected"));
		}else if (target instanceof ToDoItem) {
			ToDoItem tdi = (ToDoItem) target;
			description.setEditable(false);
			description.setEnabled(true);
			description.setText(tdi.getDescription());
			description.setCaretPosition(0);
		}else if (target instanceof PriorityNode) {
			message = MessageFormat.format(Translator.localize("message.item.branch-priority"),new Object[] {target.toString()});
			description.setEditable(false);
			description.setText(message);
			return;
		}else if (target instanceof Critic) {
			message = MessageFormat.format(Translator.localize("message.item.branch-critic"),new Object[] {target.toString()});
			description.setEditable(false);
			description.setText(message);
			return;
		}else if (Model.getFacade().isAUMLElement(target)) {
			message = MessageFormat.format(Translator.localize("message.item.branch-model"),new Object[] {Model.getFacade().toString(target)});
			description.setEditable(false);
			description.setText(message);
			return;
		}else if (target instanceof Decision) {
			message = MessageFormat.format(Translator.localize("message.item.branch-decision"),new Object[] {Model.getFacade().toString(target)});
			description.setText(message);
			return;
		}else if (target instanceof Goal) {
			message = MessageFormat.format(Translator.localize("message.item.branch-goal"),new Object[] {Model.getFacade().toString(target)});
			description.setText(message);
			return;
		}else if (target instanceof KnowledgeTypeNode) {
			message = MessageFormat.format(Translator.localize("message.item.branch-knowledge"),new Object[] {Model.getFacade().toString(target)});
			description.setText(message);
			return;
		}else {
			description.setText("");
			return;
		}
	}
	private static final long serialVersionUID = 2545592446694112088l;
}



