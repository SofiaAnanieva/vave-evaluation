package lancs.mobilemedia.core.ui.controller;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;


public interface ControllerInterface {
	public void postCommand(Command c,Displayable d);
	public boolean handleCommand(Command c,Displayable d);
}



