package lancs.mobilemedia.core.ui.controller;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.ui.screens.AlbumListScreen;


public abstract class AbstractController implements CommandListener,ControllerInterface {
	protected MainUIMidlet midlet;
	private ControllerInterface nextController;
	private AlbumData albumData;
	private AlbumListScreen albumListScreen;
	public AbstractController(MainUIMidlet midlet,AlbumData albumData,AlbumListScreen albumListScreen) {
		this.midlet = midlet;
		this.albumData = albumData;
		this.albumListScreen = albumListScreen;
	}
	public void postCommand(Command command) {
		System.out.println("AbstractController::postCommand - Current controller is: " + this.getClass().getName());
		if (handleCommand(command) == false) {
			ControllerInterface next = getNextController();
			if (next != null) {
				System.out.println("Passing to next controller in chain: " + next.getClass().getName());
				next.postCommand(command);
			}else {
				System.out.println("AbstractController::postCommand - Reached top of chain. No more handlers for command: " + command);
			}
		}
	}
	public void commandAction(Command c,Displayable d) {
		postCommand(c);
	}
	public void setAlbumListAsCurrentScreen(Alert a) {
		setCurrentScreen(a,albumListScreen);
	}
	public void setCurrentScreen(Alert a,Displayable d) {
		Display.getDisplay(midlet).setCurrent(a,d);
	}
	public Displayable getCurrentScreen() {
		return Display.getDisplay(midlet).getCurrent();
	}
	public void setCurrentScreen(Displayable d) {
		Display.getDisplay(midlet).setCurrent(d);
	}
	public AlbumData getAlbumData() {
		return albumData;
	}
	public void setAlbumData(AlbumData albumData) {
		this.albumData = albumData;
	}
	public ControllerInterface getNextController() {
		return nextController;
	}
	public void setNextController(ControllerInterface nextController) {
		this.nextController = nextController;
	}
	public String getCurrentStoreName() {
		return ScreenSingleton.getInstance().getCurrentStoreName();
	}
	public AlbumListScreen getAlbumListScreen() {
		return albumListScreen;
	}
}



