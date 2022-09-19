package org.argouml.uml.cognitive.critics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import org.argouml.cognitive.ToDoItem;
import org.argouml.ui.Clarifier;
import org.argouml.uml.diagram.OperationsCompartmentContainer;
import org.tigris.gef.presentation.Fig;


public class ClOperationCompartment implements Clarifier {
	private static ClOperationCompartment theInstance = new ClOperationCompartment();
	private static final int WAVE_LENGTH = 4;
	private static final int WAVE_HEIGHT = 2;
	private Fig fig;
	public static ClOperationCompartment getTheInstance() {
		return theInstance;
	}
	public void setFig(Fig f) {
		fig = f;
	}
	public void setToDoItem(ToDoItem i) {
	}
	public void paintIcon(Component c,Graphics g,int x,int y) {
		if (fig instanceof OperationsCompartmentContainer) {
			OperationsCompartmentContainer fc = (OperationsCompartmentContainer) fig;
			if (!fc.isOperationsVisible()) {
				fig = null;
				return;
			}
			Rectangle fr = fc.getOperationsBounds();
			int left = fr.x + 10;
			int height = fr.y + fr.height - 7;
			int right = fr.x + fr.width - 10;
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
		if (!(fig instanceof OperationsCompartmentContainer))return false;
		OperationsCompartmentContainer fc = (OperationsCompartmentContainer) fig;
		Rectangle fr = fc.getOperationsBounds();
		boolean res = fr.contains(x,y);
		fig = null;
		return res;
	}
}



