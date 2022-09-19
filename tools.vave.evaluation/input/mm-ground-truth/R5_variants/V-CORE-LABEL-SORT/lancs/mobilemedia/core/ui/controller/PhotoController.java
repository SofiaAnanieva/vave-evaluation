package lancs.mobilemedia.core.ui.controller;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import javax.microedition.rms.RecordStoreFullException;
import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.ui.datamodel.ImageData;
import lancs.mobilemedia.core.ui.screens.AddPhotoToAlbum;
import lancs.mobilemedia.core.ui.screens.AlbumListScreen;
import lancs.mobilemedia.core.ui.screens.NewLabelScreen;
import lancs.mobilemedia.core.ui.screens.PhotoViewScreen;
import lancs.mobilemedia.core.util.Constants;
import lancs.mobilemedia.lib.exceptions.ImageNotFoundException;
import lancs.mobilemedia.lib.exceptions.ImagePathNotValidException;
import lancs.mobilemedia.lib.exceptions.InvalidImageDataException;
import lancs.mobilemedia.lib.exceptions.NullAlbumDataReference;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;


public class PhotoController extends PhotoListController {
	private ImageData image;
	private NewLabelScreen screen;
	public PhotoController(MainUIMidlet midlet,AlbumData albumData,AlbumListScreen albumListScreen) {
		super(midlet,albumData,albumListScreen);
	}
	public boolean handleCommand(Command command) {
		String label = command.getLabel();
		System.out.println("<* PhotoController.handleCommand() *> " + label);
		if (label.equals("View")) {
			String selectedImageName = getSelectedImageName();
			showImage(selectedImageName);
			try {
				ImageData image = getAlbumData().getImageInfo(selectedImageName);
				image.increaseNumberOfViews();
				updateImage(image);
				System.out.println("<* BaseController.handleCommand() *> Image = " + selectedImageName + "; # views = " + image.getNumberOfViews());
			}catch (ImageNotFoundException e) {
				Alert alert = new Alert("Error","The selected photo was not found in the mobile device",null,AlertType.ERROR);
				Display.getDisplay(midlet).setCurrent(alert,Display.getDisplay(midlet).getCurrent());
			}catch (NullAlbumDataReference e) {
				this.setAlbumData(new AlbumData());
				Alert alert = new Alert("Error","The operation is not available. Try again later !",null,AlertType.ERROR);
				Display.getDisplay(midlet).setCurrent(alert,Display.getDisplay(midlet).getCurrent());
			}catch (InvalidImageDataException e) {
				Alert alert = new Alert("Error","The image data is not valid",null,AlertType.ERROR);
				alert.setTimeout(5000);
			}catch (PersistenceMechanismException e) {
				Alert alert = new Alert("Error","It was not possible to recovery the selected image",null,AlertType.ERROR);
				alert.setTimeout(5000);
			}
			ScreenSingleton.getInstance().setCurrentScreenName(Constants.IMAGE_SCREEN);
			return true;
		}else if (label.equals("Add")) {
			ScreenSingleton.getInstance().setCurrentScreenName(Constants.ADDPHOTOTOALBUM_SCREEN);
			AddPhotoToAlbum form = new AddPhotoToAlbum("Add new Photo to Album");
			form.setCommandListener(this);
			setCurrentScreen(form);
			return true;
		}else if (label.equals("Save Photo")) {
			try {
				getAlbumData().addNewPhotoToAlbum(((AddPhotoToAlbum) getCurrentScreen()).getPhotoName(),((AddPhotoToAlbum) getCurrentScreen()).getPath(),getCurrentStoreName());
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
			return goToPreviousScreen();
		}else if (label.equals("Delete")) {
			String selectedImageName = getSelectedImageName();
			try {
				getAlbumData().deleteImage(getCurrentStoreName(),selectedImageName);
			}catch (PersistenceMechanismException e) {
				Alert alert = new Alert("Error","The mobile database can not delete this photo",null,AlertType.ERROR);
				Display.getDisplay(midlet).setCurrent(alert,Display.getDisplay(midlet).getCurrent());
				return true;
			}catch (ImageNotFoundException e) {
				Alert alert = new Alert("Error","The selected photo was not found in the mobile device",null,AlertType.ERROR);
				Display.getDisplay(midlet).setCurrent(alert,Display.getDisplay(midlet).getCurrent());
				return true;
			}
			showImageList(getCurrentStoreName(),false,false);
			ScreenSingleton.getInstance().setCurrentScreenName(Constants.IMAGELIST_SCREEN);
			return true;
		}else if (label.equals("Edit Label")) {
			String selectedImageName = getSelectedImageName();
			try {
				image = getAlbumData().getImageInfo(selectedImageName);
				NewLabelScreen formScreen = new NewLabelScreen("Edit Label Photo",NewLabelScreen.LABEL_PHOTO);
				formScreen.setCommandListener(this);
				this.setScreen(formScreen);
				setCurrentScreen(formScreen);
				formScreen = null;
			}catch (ImageNotFoundException e) {
				Alert alert = new Alert("Error","The selected photo was not found in the mobile device",null,AlertType.ERROR);
				Display.getDisplay(midlet).setCurrent(alert,Display.getDisplay(midlet).getCurrent());
			}catch (NullAlbumDataReference e) {
				this.setAlbumData(new AlbumData());
				Alert alert = new Alert("Error","The operation is not available. Try again later !",null,AlertType.ERROR);
				Display.getDisplay(midlet).setCurrent(alert,Display.getDisplay(midlet).getCurrent());
			}
			return true;
		}else if (label.equals("Sort by Views")) {
			showImageList(getCurrentStoreName(),true,false);
			ScreenSingleton.getInstance().setCurrentScreenName(Constants.IMAGELIST_SCREEN);
			return true;
		}else if (label.equals("Save")) {
			System.out.println("<* PhotoController.handleCommand() *> Save Photo Label = " + this.screen.getLabelName());
			this.getImage().setImageLabel(this.screen.getLabelName());
			try {
				updateImage(image);
			}catch (InvalidImageDataException e) {
				Alert alert = null;
				if (e instanceof ImagePathNotValidException)alert = new Alert("Error","The path is not valid",null,AlertType.ERROR);else alert = new Alert("Error","The image file format is not valid",null,AlertType.ERROR);
				Display.getDisplay(midlet).setCurrent(alert,Display.getDisplay(midlet).getCurrent());
			}catch (PersistenceMechanismException e) {
				Alert alert = new Alert("Error","The mobile database can not update this photo",null,AlertType.ERROR);
				Display.getDisplay(midlet).setCurrent(alert,Display.getDisplay(midlet).getCurrent());
			}
			return goToPreviousScreen();
		}else if (label.equals("Back")) {
			return goToPreviousScreen();
		}else if (label.equals("Cancel")) {
			return goToPreviousScreen();
		}
		return false;
	}
	void updateImage(ImageData image)throws InvalidImageDataException,PersistenceMechanismException {
		getAlbumData().updateImageInfo(image,image);
	}
	public String getSelectedImageName() {
		List selected = (List) Display.getDisplay(midlet).getCurrent();
		if (selected == null)System.out.println("Current List from display is NULL!");
		String name = selected.getString(selected.getSelectedIndex());
		return name;
	}
	public void showImage(String name) {
		Image storedImage = null;
		try {
			storedImage = getAlbumData().getImageFromRecordStore(getCurrentStoreName(),name);
		}catch (ImageNotFoundException e) {
			Alert alert = new Alert("Error","The selected photo was not found in the mobile device",null,AlertType.ERROR);
			Display.getDisplay(midlet).setCurrent(alert,Display.getDisplay(midlet).getCurrent());
			return;
		}catch (PersistenceMechanismException e) {
			Alert alert = new Alert("Error","The mobile database can open this photo",null,AlertType.ERROR);
			Display.getDisplay(midlet).setCurrent(alert,Display.getDisplay(midlet).getCurrent());
			return;
		}
		PhotoViewScreen canv = new PhotoViewScreen(storedImage);
		canv.setCommandListener(this);
		AbstractController nextcontroller = this;
		setCurrentScreen(canv);
	}
	private boolean goToPreviousScreen() {
		System.out.println("<* PhotoController.goToPreviousScreen() *>");
		String currentScreenName = ScreenSingleton.getInstance().getCurrentScreenName();
		if (currentScreenName.equals(Constants.ALBUMLIST_SCREEN)) {
			System.out.println("Can\'t go back here...Should never reach this spot");
		}else if (currentScreenName.equals(Constants.IMAGE_SCREEN)) {
			showImageList(getCurrentStoreName(),false,false);
			ScreenSingleton.getInstance().setCurrentScreenName(Constants.IMAGELIST_SCREEN);
			return true;
		}else if (currentScreenName.equals(Constants.ADDPHOTOTOALBUM_SCREEN)) {
			showImageList(getCurrentStoreName(),false,false);
			ScreenSingleton.getInstance().setCurrentScreenName(Constants.IMAGELIST_SCREEN);
			return true;
		}
		return false;
	}
	public void setImage(ImageData image) {
		this.image = image;
	}
	public ImageData getImage() {
		return image;
	}
	public void setScreen(NewLabelScreen screen) {
		this.screen = screen;
	}
	public NewLabelScreen getScreen() {
		return screen;
	}
}



