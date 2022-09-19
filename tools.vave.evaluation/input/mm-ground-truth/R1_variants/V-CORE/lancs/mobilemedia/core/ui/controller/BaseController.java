package lancs.mobilemedia.core.ui.controller;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import javax.microedition.rms.RecordStoreFullException;
import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.ui.screens.AddPhotoToAlbum;
import lancs.mobilemedia.core.ui.screens.AlbumListScreen;
import lancs.mobilemedia.core.ui.screens.NewAlbumScreen;
import lancs.mobilemedia.core.ui.screens.PhotoListScreen;
import lancs.mobilemedia.core.ui.screens.PhotoViewScreen;
import lancs.mobilemedia.core.util.Constants;
import lancs.mobilemedia.lib.exceptions.ImageNotFoundException;
import lancs.mobilemedia.lib.exceptions.ImagePathNotValidException;
import lancs.mobilemedia.lib.exceptions.InvalidImageDataException;
import lancs.mobilemedia.lib.exceptions.InvalidPhotoAlbumNameException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;
import lancs.mobilemedia.lib.exceptions.UnavailablePhotoAlbumException;


public class BaseController implements CommandListener,ControllerInterface {
	private MainUIMidlet midlet;
	private Display display;
	private AlbumData model;
	private BaseController nextController;
	private AlbumListScreen albumListScreen;
	private String currentScreenName;
	private String currentStoreName = "My Photo Album";
	public BaseController(MainUIMidlet midlet) {
		super();
		this.midlet = midlet;
	}
	public BaseController(MainUIMidlet midlet,AlbumData model) {
		super();
		this.midlet = midlet;
		this.model = model;
	}
	public void init(AlbumData model) {
		this.display = Display.getDisplay(midlet);
		albumListScreen = new AlbumListScreen();
		String[]albumNames = model.getAlbumNames();
		for (int i = 0;i < albumNames.;i++) {
			if (albumNames[i] != null) {
				albumListScreen.append(albumNames[i],null);
			}
		}
		albumListScreen.initMenu();
		albumListScreen.setCommandListener(this);
		setCurrentScreen(albumListScreen);
		currentScreenName = Constants.ALBUMLIST_SCREEN;
	}
	public boolean handleCommand(Command c,Displayable d) {
		String label = c.getLabel();
		System.out.println(this.getClass().getName() + "::handleCommand: " + label);
		if (label.equals("Exit")) {
			midlet.destroyApp(true);
			return true;
		}else if (label.equals("Reset")) {
			resetImageData();
			currentScreenName = Constants.ALBUMLIST_SCREEN;
			return true;
		}else if (label.equals("New Photo Album")) {
			System.out.println("Create new Photo Album here");
			currentScreenName = Constants.NEWALBUM_SCREEN;
			NewAlbumScreen canv = new NewAlbumScreen("Add new Photo Album");
			canv.setCommandListener(this);
			setCurrentScreen(canv);
			canv = null;
			return true;
		}else if (label.equals("Delete Album")) {
			System.out.println("Delete Photo Album here");
			List down = (List) display.getCurrent();
			currentScreenName = Constants.CONFIRMDELETEALBUM_SCREEN;
			currentStoreName = down.getString(down.getSelectedIndex());
			Alert deleteConfAlert = new Alert("Delete Photo Album","Would you like to remove the album " + currentStoreName,null,AlertType.CONFIRMATION);
			deleteConfAlert.setTimeout(Alert.FOREVER);
			deleteConfAlert.addCommand(new Command("Yes - Delete",Command.OK,2));
			deleteConfAlert.addCommand(new Command("No - Delete",Command.CANCEL,2));
			setCurrentScreen(deleteConfAlert,albumListScreen);
			deleteConfAlert.setCommandListener(this);
			return true;
		}else if (label.equals("Yes - Delete")) {
			try {
				model.deletePhotoAlbum(currentStoreName);
			}catch (PersistenceMechanismException e) {
				Alert alert = new Alert("Error","The mobile database can not delete this photo album",null,AlertType.ERROR);
				Display.getDisplay(midlet).setCurrent(alert,Display.getDisplay(midlet).getCurrent());
			}
			goToPreviousScreen();
			return true;
		}else if (label.equals("No - Delete")) {
			goToPreviousScreen();
			return true;
		}else if (label.equals("Save")) {
			try {
				model.createNewPhotoAlbum(((NewAlbumScreen) getCurrentScreenName()).getAlbumName());
			}catch (PersistenceMechanismException e) {
				Alert alert = null;
				if (e.getCause()instanceof RecordStoreFullException)alert = new Alert("Error","The mobile database is full",null,AlertType.ERROR);else alert = new Alert("Error","The mobile database can not add a new photo album",null,AlertType.ERROR);
				Display.getDisplay(midlet).setCurrent(alert,Display.getDisplay(midlet).getCurrent());
				return true;
			}catch (InvalidPhotoAlbumNameException e) {
				Alert alert = new Alert("Error","You have provided an invalid Photo Album name",null,AlertType.ERROR);
				Display.getDisplay(midlet).setCurrent(alert,Display.getDisplay(midlet).getCurrent());
				return true;
			}
			goToPreviousScreen();
			return true;
		}else if (label.equals("Select")) {
			List down = (List) display.getCurrent();
			currentStoreName = down.getString(down.getSelectedIndex());
			showImageList(currentStoreName);
			currentScreenName = Constants.IMAGELIST_SCREEN;
			return true;
		}else if (label.equals("View")) {
			showImage();
			currentScreenName = Constants.IMAGE_SCREEN;
			return true;
		}else if (label.equals("Add")) {
			currentScreenName = Constants.ADDPHOTOTOALBUM_SCREEN;
			AddPhotoToAlbum form = new AddPhotoToAlbum("Add new Photo to Album");
			form.setCommandListener(this);
			setCurrentScreen(form);
			return true;
		}else if (label.equals("Save Add Photo")) {
			try {
				model.addNewPhotoToAlbum(((AddPhotoToAlbum) getCurrentScreenName()).getPhotoName(),((AddPhotoToAlbum) getCurrentScreenName()).getPath(),currentStoreName);
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
			goToPreviousScreen();
			return true;
		}else if (label.equals("Delete")) {
			String selectedImageName = getSelectedImageName();
			try {
				model.deleteImage(currentStoreName,selectedImageName);
			}catch (PersistenceMechanismException e) {
				Alert alert = new Alert("Error","The mobile database can not delete this photo",null,AlertType.ERROR);
				Display.getDisplay(midlet).setCurrent(alert,Display.getDisplay(midlet).getCurrent());
				return true;
			}catch (ImageNotFoundException e) {
				Alert alert = new Alert("Error","The selected photo was not found in the mobile device",null,AlertType.ERROR);
				Display.getDisplay(midlet).setCurrent(alert,Display.getDisplay(midlet).getCurrent());
				return true;
			}
			showImageList(currentStoreName);
			currentScreenName = Constants.IMAGELIST_SCREEN;
			return true;
		}else if (label.equals("Back")) {
			goToPreviousScreen();
			return true;
		}else if (label.equals("Cancel")) {
			goToPreviousScreen();
			return true;
		}
		return false;
	}
	public void postCommand(Command c,Displayable d) {
		System.out.println("BaseController::postCommand - Current controller is: " + this.getClass().getName());
		if (handleCommand(c,d) == false) {
			BaseController next = getNextController();
			if (next != null) {
				System.out.println("Passing to next controller in chain: " + next.getClass().getName());
				next.postCommand(c,d);
			}else {
				System.out.println("BaseController::postCommand - Reached top of chain. No more handlers for command: " + c.getLabel());
			}
		}
	}
	public void commandAction(Command c,Displayable d) {
		postCommand(c,d);
	}
	private void resetImageData() {
		try {
			model.resetImageData();
		}catch (PersistenceMechanismException e) {
			Alert alert = null;
			if (e.getCause()instanceof RecordStoreFullException)alert = new Alert("Error","The mobile database is full",null,AlertType.ERROR);else alert = new Alert("Error","It is not possible to reset the database",null,AlertType.ERROR);
			Display.getDisplay(midlet).setCurrent(alert,Display.getDisplay(midlet).getCurrent());
			return;
		}
		for (int i = 0;i < albumListScreen.size();i++) {
			albumListScreen.delete(i);
		}
		String[]albumNames = model.getAlbumNames();
		for (int i = 0;i < albumNames.;i++) {
			if (albumNames[i] != null) {
				albumListScreen.append(albumNames[i],null);
			}
		}
		setCurrentScreen(albumListScreen);
	}
	public void setCurrentScreen(Displayable d) {
		Display.getDisplay(midlet).setCurrent(d);
	}
	public void setCurrentScreen(Alert a,Displayable d) {
		Display.getDisplay(midlet).setCurrent(a,d);
	}
	public Displayable getCurrentScreenName() {
		return Display.getDisplay(midlet).getCurrent();
	}
	private void goToPreviousScreen() {
		if (currentScreenName.equals(Constants.ALBUMLIST_SCREEN)) {
			System.out.println("Can\'t go back here...Should never reach this spot");
		}else if (currentScreenName.equals(Constants.IMAGE_SCREEN)) {
			showImageList(currentStoreName);
			currentScreenName = Constants.IMAGELIST_SCREEN;
		}else if (currentScreenName.equals(Constants.IMAGELIST_SCREEN)) {
			setCurrentScreen(albumListScreen);
			currentScreenName = Constants.ALBUMLIST_SCREEN;
		}else if (currentScreenName.equals(Constants.NEWALBUM_SCREEN)) {
			albumListScreen.repaintListAlbum(model.getAlbumNames());
			setCurrentScreen(albumListScreen);
			currentScreenName = Constants.ALBUMLIST_SCREEN;
		}else if (currentScreenName.equals(Constants.CONFIRMDELETEALBUM_SCREEN)) {
			albumListScreen.repaintListAlbum(model.getAlbumNames());
			setCurrentScreen(albumListScreen);
			currentScreenName = Constants.ALBUMLIST_SCREEN;
		}else if (currentScreenName.equals(Constants.ADDPHOTOTOALBUM_SCREEN)) {
			showImageList(currentStoreName);
			currentScreenName = Constants.IMAGELIST_SCREEN;
		}
	}
	public void showImage() {
		List selected = (List) display.getCurrent();
		String name = selected.getString(selected.getSelectedIndex());
		Image storedImage = null;
		try {
			storedImage = model.getImageFromRecordStore(currentStoreName,name);
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
		setCurrentScreen(canv);
	}
	public void showImageList(String recordName) {
		if (recordName == null)recordName = currentStoreName;
		PhotoListScreen imageList = new PhotoListScreen();
		imageList.initMenu();
		imageList.setCommandListener(this);
		String[]labels = null;
		try {
			labels = model.getImageNames(recordName);
		}catch (UnavailablePhotoAlbumException e) {
			Alert alert = new Alert("Error","The list of photos can not be recovered",null,AlertType.ERROR);
			Display.getDisplay(midlet).setCurrent(alert,Display.getDisplay(midlet).getCurrent());
			return;
		}
		for (int i = 0;i < labels.;i++) {
			if (labels[i] != null) {
				imageList.append(labels[i],null);
			}
		}
		setCurrentScreen(imageList);
	}
	public String getSelectedImageName() {
		if (display == null) {
			System.out.println("BaseController::getSelectedImageName: Current display is NULL! Trying to get from Midlet");
			display = Display.getDisplay(midlet);
		}
		List selected = (List) display.getCurrent();
		if (selected == null)System.out.println("Current List from display is NULL!");
		String name = selected.getString(selected.getSelectedIndex());
		return name;
	}
	public AlbumData getModel() {
		return model;
	}
	public String getCurrentStoreName() {
		return currentStoreName;
	}
	public void setCurrentScreenName(String currentScreenName) {
		this.currentScreenName = currentScreenName;
	}
	public BaseController getNextController() {
		return nextController;
	}
	public void setNextController(BaseController nextController) {
		this.nextController = nextController;
	}
}



