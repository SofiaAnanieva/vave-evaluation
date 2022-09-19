package org.argouml.uml.ui;

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import org.argouml.application.api.Argo;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.swingext.UpArrowIcon;
import org.tigris.gef.presentation.Fig;
import org.tigris.swidgets.Horizontal;
import org.tigris.swidgets.Vertical;
import org.argouml.uml.ui.PropPanel;
import org.argouml.uml.ui.UMLTextArea2;
import org.argouml.uml.ui.UMLTextField2;


public class TabDocumentation extends PropPanel {
	private static String orientation = Configuration.getString(Configuration.makeKey("layout","tabdocumentation"));
	public TabDocumentation() {
		super(Translator.localize("tab.documentation"),(ImageIcon) null);
		setOrientation((orientation.equals("West")||orientation.equals("East"))?Vertical.getInstance():Horizontal.getInstance());
		setIcon(new UpArrowIcon());
		addField(Translator.localize("label.author"),new UMLTextField2(new UMLModelElementTaggedValueDocument(Argo.AUTHOR_TAG)));
		addField(Translator.localize("label.version"),new UMLTextField2(new UMLModelElementTaggedValueDocument(Argo.VERSION_TAG)));
		addField(Translator.localize("label.since"),new UMLTextField2(new UMLModelElementTaggedValueDocument(Argo.SINCE_TAG)));
		addField(Translator.localize("label.deprecated"),new UMLDeprecatedCheckBox());
		UMLTextArea2 see = new UMLTextArea2(new UMLModelElementTaggedValueDocument(Argo.SEE_TAG));
		see.setRows(2);
		see.setLineWrap(true);
		see.setWrapStyleWord(true);
		JScrollPane spSee = new JScrollPane();
		spSee.getViewport().add(see);
		addField(Translator.localize("label.see"),spSee);
		add(LabelledLayout.getSeparator());
		UMLTextArea2 doc = new UMLTextArea2(new UMLModelElementTaggedValueDocument(Argo.DOCUMENTATION_TAG));
		doc.setRows(2);
		doc.setLineWrap(true);
		doc.setWrapStyleWord(true);
		JScrollPane spDocs = new JScrollPane();
		spDocs.getViewport().add(doc);
		addField(Translator.localize("label.documentation"),spDocs);
		UMLTextArea2 comment = new UMLTextArea2(new UMLModelElementCommentDocument(false));
		disableTextArea(comment);
		JScrollPane spComment = new JScrollPane();
		spComment.getViewport().add(comment);
		addField(Translator.localize("label.comment.name"),spComment);
		UMLTextArea2 commentBody = new UMLTextArea2(new UMLModelElementCommentDocument(true));
		disableTextArea(commentBody);
		JScrollPane spCommentBody = new JScrollPane();
		spCommentBody.getViewport().add(commentBody);
		addField(Translator.localize("label.comment.body"),spCommentBody);
		setButtonPanelSize(18);
	}
	private void disableTextArea(final JTextArea textArea) {
		textArea.setRows(2);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEnabled(false);
		textArea.setDisabledTextColor(textArea.getForeground());
		final Color inactiveColor = UIManager.getColor("TextField.inactiveBackground");
		if (inactiveColor != null) {
			textArea.setBackground(new Color(inactiveColor.getRGB()));
		}
	}
	public boolean shouldBeEnabled() {
		Object target = getTarget();
		return shouldBeEnabled(target);
	}
	@Override public boolean shouldBeEnabled(Object target) {
		target = (target instanceof Fig)?((Fig) target).getOwner():target;
		return Model.getFacade().isAModelElement(target);
	}
}



