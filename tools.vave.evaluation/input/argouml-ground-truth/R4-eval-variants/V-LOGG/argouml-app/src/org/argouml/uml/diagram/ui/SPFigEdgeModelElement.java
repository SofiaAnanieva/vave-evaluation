package org.argouml.uml.diagram.ui;

import java.awt.event.ItemListener;
import javax.swing.text.Document;
import org.argouml.ui.StylePanelFig;
import org.tigris.gef.ui.ColorRenderer;


public class SPFigEdgeModelElement extends StylePanelFig implements ItemListener {
	public SPFigEdgeModelElement() {
		super("Edge Appearance");
		initChoices();
		Document bboxDoc = getBBoxField().getDocument();
		bboxDoc.addDocumentListener(this);
		getLineField().addItemListener(this);
		getLineField().setRenderer(new ColorRenderer());
		getBBoxLabel().setLabelFor(getBBoxField());
		add(getBBoxLabel());
		add(getBBoxField());
		getLineLabel().setLabelFor(getLineField());
		add(getLineLabel());
		add(getLineField());
	}
}



