/*
 * Lancaster University
 * Computing Department
 * 
 * Created by Eduardo Figueiredo
 * Date: 22 Jul 2007
 * 
 */
package lancs.mobilemedia.core.ui.controller;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.rms.RecordStoreFullException;

import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.ui.datamodel.ImageAccessor;
import lancs.mobilemedia.core.ui.datamodel.ImageData;
import lancs.mobilemedia.core.ui.screens.AddPhotoToAlbum;
import lancs.mobilemedia.core.ui.screens.AlbumListScreen;
import lancs.mobilemedia.core.util.Constants;
import lancs.mobilemedia.lib.exceptions.ImageNotFoundException;
import lancs.mobilemedia.lib.exceptions.ImagePathNotValidException;
import lancs.mobilemedia.lib.exceptions.InvalidImageDataException;
import lancs.mobilemedia.lib.exceptions.NullAlbumDataReference;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;

/**
 * @author Eduardo Figueiredo
 * [EF] Added in Scenario 05
 */
//#ifdef includeCopyPhoto
public class PhotoViewController extends AbstractController {

	String imageName = "";
	
	/**
	 * @param midlet
	 * @param nextController
	 * @param albumData
	 * @param albumListScreen
	 * @param currentScreenName
	 */
	public PhotoViewController(MainUIMidlet midlet, AlbumData albumData, AlbumListScreen albumListScreen, String imageName) {
		super(midlet, albumData, albumListScreen);
		this.imageName = imageName;
	}

	/* (non-Javadoc)
	 * @see ubc.midp.mobilephoto.core.ui.controller.ControllerInterface#handleCommand(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public boolean handleCommand(Command c) {
		String label = c.getLabel();
		System.out.println( "<* PhotoViewController.handleCommand() *> " + label);

		/** Case: Copy photo to a different album */
		if (label.equals("Copy")) {
			AddPhotoToAlbum copyPhotoToAlbum = new AddPhotoToAlbum("Copy Photo to Album");
			copyPhotoToAlbum.setPhotoName(imageName);
			copyPhotoToAlbum.setLabePhotoPath("Copy to Album:");
			copyPhotoToAlbum.setCommandListener(this);
	        Display.getDisplay(midlet).setCurrent(copyPhotoToAlbum);
			
			return true;
		}
		
		/** Case: Save a copy in a new album */
		else if (label.equals("Save Photo")) {
			try {
				ImageData imageData = null;
				
				try {
					imageData = getAlbumData().getImageInfo(imageName);
				} catch (ImageNotFoundException e) {
					e.printStackTrace();
				} catch (NullAlbumDataReference e) {
					e.printStackTrace();
				}
				String photoname = ((AddPhotoToAlbum) getCurrentScreen()).getPhotoName();
				String albumname = ((AddPhotoToAlbum) getCurrentScreen()).getPath();
				getAlbumData().addImageData(photoname, imageData, albumname);
				
			} catch (InvalidImageDataException e) {
				Alert alert = null;
				if (e instanceof ImagePathNotValidException)
					alert = new Alert("Error", "The path is not valid", null, AlertType.ERROR);
				else
					alert = new Alert("Error", "The image file format is not valid", null, AlertType.ERROR);
				Display.getDisplay(midlet).setCurrent(alert, Display.getDisplay(midlet).getCurrent());
				return true;
				// alert.setTimeout(5000);
			} catch (PersistenceMechanismException e) {
				Alert alert = null;
				if (e.getCause() instanceof RecordStoreFullException)
					alert = new Alert("Error", "The mobile database is full", null, AlertType.ERROR);
				else
					alert = new Alert("Error", "The mobile database can not add a new photo", null, AlertType.ERROR);
				Display.getDisplay(midlet).setCurrent(alert, Display.getDisplay(midlet).getCurrent());
			}
			((PhotoController)this.getNextController()).showImageList(ScreenSingleton.getInstance().getCurrentStoreName(), false, false);
		    ScreenSingleton.getInstance().setCurrentScreenName(Constants.IMAGELIST_SCREEN);

			return true;
		}
		
		return false;
	}
}
//#endif
