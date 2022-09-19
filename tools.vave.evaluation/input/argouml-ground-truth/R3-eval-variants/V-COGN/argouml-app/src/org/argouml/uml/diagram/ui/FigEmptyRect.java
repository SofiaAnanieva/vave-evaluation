package org.argouml.uml.diagram.ui;

import org.tigris.gef.presentation.FigRect;


public class FigEmptyRect extends FigRect {
	public FigEmptyRect(int x,int y,int w,int h) {
		super(x,y,w,h);
		super.setFilled(false);
	}
	public void setFilled(boolean filled) {
	}
}



