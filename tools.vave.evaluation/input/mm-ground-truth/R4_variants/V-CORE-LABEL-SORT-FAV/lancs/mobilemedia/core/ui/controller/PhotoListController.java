package lancs.mobilemedia.core.ui.controller;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.List;
import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.ui.datamodel.ImageData;
import lancs.mobilemedia.core.ui.screens.AlbumListScreen;
import lancs.mobilemedia.core.ui.screens.PhotoListScreen;
import lancs.mobilemedia.core.util.Constants;
import lancs.mobilemedia.lib.exceptions.UnavailablePhotoAlbumException;


public class PhotoListController extends AbstractController {
	public PhotoListController(MainUIMidlet midlet,AlbumData albumData,AlbumListScreen albumListScreen) {
		super(midlet,albumData,albumListScreen);
	}
	public boolean handleCommand(Command command) {
		String label = command.getLabel();
		if (label.equals("Select")) {
			List down = (List) Display.getDisplay(midlet).getCurrent();
			ScreenSingleton.getInstance().setCurrentStoreName(down.getString(down.getSelectedIndex()));
			showImageList(getCurrentStoreName(),false,false);
			ScreenSingleton.getInstance().setCurrentScreenName(Constants.IMAGELIST_SCREEN);
			return true;
		}
		return false;
	}
	public void showImageList(String recordName,boolean sort,boolean favorite) {
		if (recordName == null)recordName = getCurrentStoreName();
		PhotoController photoController = new PhotoController(midlet,getAlbumData(),getAlbumListScreen());
		photoController.setNextController(this);
		PhotoListScreen imageList = new PhotoListScreen();
		imageList.setCommandListener(photoController);
		imageList.initMenu();
		ImageData[]images = null;
		try {
			images = getAlbumData().getImages(recordName);
		}catch (UnavailablePhotoAlbumException e) {
			Alert alert = new Alert("Error","The list of photos can not be recovered",null,AlertType.ERROR);
			Display.getDisplay(midlet).setCurrent(alert,Display.getDisplay(midlet).getCurrent());
			return;
		}
		if (images == null)return;
		if (sort) {
			bubbleSort(images);
		}
		for (int i = 0;i < images.length;i++) {
			if (images[i] != null) {
				if (favorite) {
					if (images[i].isFavorite())imageList.append(images[i].getImageLabel(),null);
				}else imageList.append(images[i].getImageLabel(),null);
			}
		}
		setCurrentScreen(imageList);
	}
	private void exchange(ImageData[]images,int pos1,int pos2) {
		ImageData tmp = images[pos1];
		images[pos1] = images[pos2];
		images[pos2] = tmp;
	}
	public void bubbleSort(ImageData[]images) {
		System.out.print("Sorting by BubbleSort...");
		for (int end = images.length;end > 1;end--) {
			for (int current = 0;current < end - 1;current++) {
				if (images[current].getNumberOfViews() > images[current + 1].getNumberOfViews()) {
					exchange(images,current,current + 1);
				}
			}
		}
		System.out.println("done.");
	}
}



