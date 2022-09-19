package org.argouml.uml.cognitive.critics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import org.argouml.cognitive.ToDoItem;
import org.argouml.ui.Clarifier;
import org.argouml.uml.diagram.AttributesCompartmentContainer;
import org.tigris.gef.presentation.Fig;


public class ClAttributeCompartment implements Clarifier {
	private static ClAttributeCompartment theInstance = new ClAttributeCompartment();
	private static final int WAVE_LENGTH = 4;
	private static final int WAVE_HEIGHT = 2;
	private Fig fig;
	public void setFig(Fig f) {
		fig = f;
	}
	public void setToDoItem(ToDoItem i) {
	}
	public void paintIcon(Component c,Graphics g,int x,int y) {
		if (fig instanceof AttributesCompartmentContainer) {
			AttributesCompartmentContainer fc = (AttributesCompartmentContainer) fig;
			if (!fc.isAttributesVisible()) {
				fig = null;
				return;
			}
			Rectangle fr = fc.getAttributesBounds();
			int left = fr.x + 6;
			int height = fr.y + fr.height - 5;
			int right = fr.x + fr.width - 6;
			g.setColor(Color.red);
			int i = left;
			while (true) {
				g.drawLine(i,height,i + WAVE_LENGTH,height + WAVE_HEIGHT);
				i += WAVE_LENGTH;
				if (i >= right)break;
				g.drawLine(i,height + WAVE_HEIGHT,i + WAVE_LENGTH,height);
				i += WAVE_LENGTH;
				if (i >= right)break;
				g.drawLine(i,height,i + WAVE_LENGTH,height + WAVE_HEIGHT / 2);
				i += WAVE_LENGTH;
				if (i >= right)break;
				g.drawLine(i,height + WAVE_HEIGHT / 2,i + WAVE_LENGTH,height);
				i += WAVE_LENGTH;
				if (i >= right)break;
			}
			fig = null;
		}
	}
	public int getIconWidth() {
		return 0;
	}
	public int getIconHeight() {
		return 0;
	}
	public boolean hit(int x,int y) {
		if (!(fig instanceof AttributesCompartmentContainer)) {
			return false;
		}
		AttributesCompartmentContainer fc = (AttributesCompartmentContainer) fig;
		Rectangle fr = fc.getAttributesBounds();
		boolean res = fr.contains(x,y);
		fig = null;
		return res;
	}
	public static ClAttributeCompartment getTheInstance() {
		return theInstance;
	}
}



