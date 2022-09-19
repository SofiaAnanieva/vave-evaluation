package lancs.mobilemedia.core.ui;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import lancs.mobilemedia.core.ui.controller.AlbumController;
import lancs.mobilemedia.core.ui.controller.BaseController;
import lancs.mobilemedia.core.ui.controller.PhotoListController;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.ui.screens.AlbumListScreen;


public class MainUIMidlet extends MIDlet {
	private BaseController rootController;
	private AlbumData model;
	public MainUIMidlet() {
	}
	public void startApp()throws MIDletStateChangeException {
		model = new AlbumData();
		AlbumListScreen album = new AlbumListScreen();
		rootController = new BaseController(this,model,album);
		PhotoListController photoListController = new PhotoListController(this,model,album);
		photoListController.setNextController(rootController);
		AlbumController albumController = new AlbumController(this,model,album);
		albumController.setNextController(photoListController);
		album.setCommandListener(albumController);
		rootController.init(model);
	}
	public void pauseApp() {
	}
	public void destroyApp(boolean unconditional) {
		notifyDestroyed();
	}
}



