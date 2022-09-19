package org.argouml.uml.diagram.ui;

import java.awt.Image;
import org.tigris.gef.presentation.FigImage;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigText;
import org.argouml.uml.diagram.ui.FigSingleLineText;


public class FigProfileIcon extends FigNode {
	private FigImage image = null;
	private FigText label = null;
	private static final int GAP = 2;
	public FigProfileIcon(Image icon,String str) {
		image = new FigImage(0,0,icon);
		label = new FigSingleLineText(0,image.getHeight() + GAP,0,0,true);
		label.setText(str);
		label.calcBounds();
		addFig(image);
		addFig(label);
		image.setResizable(false);
		image.setLocked(true);
	}
	@Override protected void setBoundsImpl(int x,int y,int w,int h) {
		int width = Math.max(image.getWidth(),label.getWidth());
		image.setLocation(x + (width - image.getWidth()) / 2,y);
		label.setLocation(x + (width - label.getWidth()) / 2,y + image.getHeight() + GAP);
		calcBounds();
		updateEdges();
	}
	public FigText getLabelFig() {
		return label;
	}
	public String getLabel() {
		return label.getText();
	}
	public void setLabel(String txt) {
		this.label.setText(txt);
		this.label.calcBounds();
		this.calcBounds();
	}
}



