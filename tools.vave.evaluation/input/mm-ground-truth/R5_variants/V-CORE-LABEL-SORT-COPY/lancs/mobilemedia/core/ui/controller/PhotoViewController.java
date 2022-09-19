package lancs.mobilemedia.core.ui.controller;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.rms.RecordStoreFullException;
import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.ui.datamodel.ImageData;
import lancs.mobilemedia.core.ui.screens.AddPhotoToAlbum;
import lancs.mobilemedia.core.ui.screens.AlbumListScreen;
import lancs.mobilemedia.core.ui.screens.PhotoViewScreen;
import lancs.mobilemedia.core.util.Constants;
import lancs.mobilemedia.lib.exceptions.ImageNotFoundException;
import lancs.mobilemedia.lib.exceptions.ImagePathNotValidException;
import lancs.mobilemedia.lib.exceptions.InvalidImageDataException;
import lancs.mobilemedia.lib.exceptions.NullAlbumDataReference;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;


public class PhotoViewController extends AbstractController {
	String imageName = "";
	public PhotoViewController(MainUIMidlet midlet,AlbumData albumData,AlbumListScreen albumListScreen,String imageName) {
		super(midlet,albumData,albumListScreen);
		this.imageName = imageName;
	}
	public boolean handleCommand(Command c) {
		String label = c.getLabel();
		System.out.println("<* PhotoViewController.handleCommand() *> " + label);
		if (label.equals("Copy")) {
			AddPhotoToAlbum copyPhotoToAlbum = new AddPhotoToAlbum("Copy Photo to Album");
			copyPhotoToAlbum.setPhotoName(imageName);
			copyPhotoToAlbum.setLabePhotoPath("Copy to Album:");
			copyPhotoToAlbum.setCommandListener(this);
			Display.getDisplay(midlet).setCurrent(copyPhotoToAlbum);
			return true;
		}else if (label.equals("Save Photo")) {
			try {
				ImageData imageData = null;
				 {
					try {
						imageData = getAlbumData().getImageInfo(imageName);
					}catch (ImageNotFoundException e) {
						Alert alert = new Alert("Error","The selected photo was not found in the mobile device",null,AlertType.ERROR);
						Display.getDisplay(midlet).setCurrent(alert,Display.getDisplay(midlet).getCurrent());
					}catch (NullAlbumDataReference e) {
						this.setAlbumData(new AlbumData());
						Alert alert = new Alert("Error","The operation is not available. Try again later !",null,AlertType.ERROR);
						Display.getDisplay(midlet).setCurrent(alert,Display.getDisplay(midlet).getCurrent());
					}
				}
				String photoname = ((AddPhotoToAlbum) getCurrentScreen()).getPhotoName();
				String albumname = ((AddPhotoToAlbum) getCurrentScreen()).getPath();
				getAlbumData().addImageData(photoname,imageData,albumname);
			}catch (InvalidImageDataException e) {
				Alert alert = null;
				if (e instanceof ImagePathNotValidException)alert = new Alert("Error","The path is not valid",null,AlertType.ERROR);else alert = new Alert("Error","The image file format is not valid",null,AlertType.ERROR);
				Display.getDisplay(midlet).setCurrent(alert,Display.getDisplay(midlet).getCurrent());
				return true;
			}catch (PersistenceMechanismException e) {
				Alert alert = null;
				if (e.getCause()instanceof RecordStoreFullException)alert = new Alert("Error","The mobile database is full",null,AlertType.ERROR);else alert = new Alert("Error","The mobile database can not add a new photo",null,AlertType.ERROR);
				Display.getDisplay(midlet).setCurrent(alert,Display.getDisplay(midlet).getCurrent());
			}
			getAlbumListScreen().repaintListAlbum(getAlbumData().getAlbumNames());
			setCurrentScreen(getAlbumListScreen());
			ScreenSingleton.getInstance().setCurrentScreenName(Constants.ALBUMLIST_SCREEN);
			return true;
		}else if ((label.equals("Cancel"))||(label.equals("Back"))) {
			getAlbumListScreen().repaintListAlbum(getAlbumData().getAlbumNames());
			setCurrentScreen(getAlbumListScreen());
			ScreenSingleton.getInstance().setCurrentScreenName(Constants.ALBUMLIST_SCREEN);
			return true;
		}
		return false;
	}
}



