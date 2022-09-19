package lancs.mobilemedia.core.ui.screens;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.util.Constants;
import lancs.mobilemedia.lib.exceptions.ImageNotFoundException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;


public class PhotoViewScreen extends Canvas {
	String imageName = "";
	Image image;
	AlbumData model = null;
	public static final Command backCommand = new Command("Back",Command.BACK,0);
	public static final Command copyCommand = new Command("Copy",Command.ITEM,1);
	public PhotoViewScreen(Image img) {
		image = img;
		this.addCommand(backCommand);
		this.addCommand(copyCommand);
	}
	public PhotoViewScreen(AlbumData mod,String name) {
		imageName = name;
		model = mod;
		try {
			loadImage();
		}catch (ImageNotFoundException e) {
			Alert alert = new Alert("Error","The selected image can not be found",null,AlertType.ERROR);
			alert.setTimeout(5000);
		}catch (PersistenceMechanismException e) {
			Alert alert = new Alert("Error","It was not possible to recovery the selected image",null,AlertType.ERROR);
			alert.setTimeout(5000);
		}
		this.addCommand(backCommand);
	}
	public void loadImage()throws ImageNotFoundException,PersistenceMechanismException {
		image = model.getImageFromRecordStore(null,imageName);
	}
	protected void paint(Graphics g) {
		g.setGrayScale(255);
		g.fillRect(0,0,Constants.SCREEN_WIDTH,Constants.SCREEN_HEIGHT);
		System.out.println("Screen size:" + Constants.SCREEN_WIDTH + ":" + Constants.SCREEN_HEIGHT);
		if (image == null)System.out.println("PhotoViewScreen::paint(): Image object was null.");
		g.drawImage(image,0,0,Graphics.TOP|Graphics.LEFT);
	}
}



