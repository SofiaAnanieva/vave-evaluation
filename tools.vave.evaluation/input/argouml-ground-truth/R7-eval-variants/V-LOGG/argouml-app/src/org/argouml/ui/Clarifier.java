package org.argouml.ui;

import javax.swing.Icon;
import org.tigris.gef.presentation.Fig;


public interface Clarifier extends Icon {
	public void setFig(Fig f);
	public boolean hit(int x,int y);
}



