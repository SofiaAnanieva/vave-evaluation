package org.argouml.uml.cognitive.critics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import org.argouml.cognitive.ToDoItem;
import org.argouml.ui.Clarifier;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.presentation.Fig;


public class ClClassName implements Clarifier {
	private static ClClassName theInstance = new ClClassName();
	private static final int WAVE_LENGTH = 4;
	private static final int WAVE_HEIGHT = 2;
	private Fig fig;
	public void setFig(Fig f) {
		fig = f;
	}
	public void setToDoItem(ToDoItem i) {
	}
	public void paintIcon(Component c,Graphics g,int x,int y) {
		Rectangle rect = null;
		if (fig instanceof FigNodeModelElement) {
			FigNodeModelElement fnme = (FigNodeModelElement) fig;
			rect = fnme.getNameBounds();
		}else if (fig instanceof FigEdgeModelElement) {
			FigEdgeModelElement feme = (FigEdgeModelElement) fig;
			rect = feme.getNameBounds();
		}
		if (rect != null) {
			int left = rect.x + 6;
			int height = rect.y + rect.height - 4;
			int right = rect.x + rect.width - 6;
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
		Rectangle rect = null;
		if (fig instanceof FigNodeModelElement) {
			FigNodeModelElement fnme = (FigNodeModelElement) fig;
			rect = fnme.getNameBounds();
		}else if (fig instanceof FigEdgeModelElement) {
			FigEdgeModelElement feme = (FigEdgeModelElement) fig;
			rect = feme.getNameBounds();
		}
		fig = null;
		return(rect != null)&&rect.contains(x,y);
	}
	public static ClClassName getTheInstance() {
		return theInstance;
	}
}



