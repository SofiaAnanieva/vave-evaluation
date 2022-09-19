package lancs.mobilemedia.core.ui.controller;

import javax.microedition.lcdui.Command;


public interface ControllerInterface {
	public void postCommand(Command command);
	public boolean handleCommand(Command command);
}



