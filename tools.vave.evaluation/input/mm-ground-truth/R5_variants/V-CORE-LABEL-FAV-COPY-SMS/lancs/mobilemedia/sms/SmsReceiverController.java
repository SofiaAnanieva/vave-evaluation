package lancs.mobilemedia.sms;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Image;
import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.core.ui.controller.AbstractController;
import lancs.mobilemedia.core.ui.controller.PhotoViewController;
import lancs.mobilemedia.core.ui.controller.ScreenSingleton;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.ui.screens.AlbumListScreen;
import lancs.mobilemedia.core.ui.screens.PhotoViewScreen;
import lancs.mobilemedia.core.util.Constants;


public class SmsReceiverController extends AbstractController {
	byte[]incomingImageData;
	public SmsReceiverController(MainUIMidlet midlet,AlbumData albumData,AlbumListScreen albumListScreen) {
		super(midlet,albumData,albumListScreen);
	}
	public boolean handleCommand(Command c) {
		String label = c.getLabel();
		System.out.println("SmsReceiverController::handleCommand: " + label);
		if (label.equals("Accept Photo")) {
			Image image = Image.createImage(incomingImageData,0,incomingImageData.);
			Image copy = Image.createImage(image.getWidth(),image.getHeight());
			PhotoViewScreen canv = new PhotoViewScreen(copy);
			canv.setFromSMS(true);
			canv.setCommandListener(new PhotoViewController(this.midlet,getAlbumData(),getAlbumListScreen(),"NoName"));
			this.setCurrentScreen(canv);
			return true;
		}else if (label.equals("Reject Photo")) {
			System.out.println("Reject Photo command");
			getAlbumListScreen().repaintListAlbum(getAlbumData().getAlbumNames());
			setCurrentScreen(getAlbumListScreen());
			ScreenSingleton.getInstance().setCurrentScreenName(Constants.ALBUMLIST_SCREEN);
			return true;
		}else if (label.equals("Ok")) {
			getAlbumListScreen().repaintListAlbum(getAlbumData().getAlbumNames());
			setCurrentScreen(getAlbumListScreen());
			ScreenSingleton.getInstance().setCurrentScreenName(Constants.ALBUMLIST_SCREEN);
			return true;
		}
		return false;
	}
	public void setIncommingData(byte[]incomingImageData) {
		this.incomingImageData = incomingImageData;
	}
}



