package lancs.mobilemedia.core.ui;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import lancs.mobilemedia.core.ui.controller.BaseController;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;


public class MainUIMidlet extends MIDlet {
	private BaseController rootController;
	private AlbumData model;
	public MainUIMidlet() {
	}
	public void startApp()throws MIDletStateChangeException {
		model = new AlbumData();
		rootController = new BaseController(this,model);
		rootController.init(model);
	}
	public void pauseApp() {
	}
	public void destroyApp(boolean unconditional) {
		notifyDestroyed();
	}
}



