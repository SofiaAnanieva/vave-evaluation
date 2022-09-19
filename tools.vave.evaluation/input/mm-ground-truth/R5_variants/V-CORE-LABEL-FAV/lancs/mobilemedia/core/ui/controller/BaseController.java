package lancs.mobilemedia.core.ui.controller;

import javax.microedition.lcdui.Command;
import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.ui.screens.AlbumListScreen;
import lancs.mobilemedia.core.util.Constants;


public class BaseController extends AbstractController {
	public BaseController(MainUIMidlet midlet,AlbumData model,AlbumListScreen albumListScreen) {
		super(midlet,model,albumListScreen);
	}
	public void init(AlbumData model) {
		String[]albumNames = model.getAlbumNames();
		for (int i = 0;i < albumNames.;i++) {
			if (albumNames[i] != null) {
				getAlbumListScreen().append(albumNames[i],null);
			}
		}
		getAlbumListScreen().initMenu();
		setCurrentScreen(getAlbumListScreen());
	}
	public boolean handleCommand(Command command) {
		String label = command.getLabel();
		System.out.println(this.getClass().getName() + "::handleCommand: " + label);
		if (label.equals("Exit")) {
			midlet.destroyApp(true);
			return true;
		}else if (label.equals("Back")) {
			return goToPreviousScreen();
		}else if (label.equals("Cancel")) {
			return goToPreviousScreen();
		}
		return false;
	}
	private boolean goToPreviousScreen() {
		System.out.println("<* AlbumController.goToPreviousScreen() *>");
		String currentScreenName = ScreenSingleton.getInstance().getCurrentScreenName();
		if ((currentScreenName.equals(Constants.IMAGELIST_SCREEN))||(currentScreenName.equals(Constants.NEWALBUM_SCREEN))||(currentScreenName.equals(Constants.CONFIRMDELETEALBUM_SCREEN))) {
			getAlbumListScreen().repaintListAlbum(getAlbumData().getAlbumNames());
			setCurrentScreen(getAlbumListScreen());
			ScreenSingleton.getInstance().setCurrentScreenName(Constants.ALBUMLIST_SCREEN);
			return true;
		}
		return false;
	}
}



