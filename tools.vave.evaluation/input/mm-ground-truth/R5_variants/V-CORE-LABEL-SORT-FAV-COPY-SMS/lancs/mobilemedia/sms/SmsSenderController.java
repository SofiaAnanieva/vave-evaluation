package lancs.mobilemedia.sms;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.core.ui.controller.AbstractController;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.ui.datamodel.ImageAccessor;
import lancs.mobilemedia.core.ui.datamodel.ImageData;
import lancs.mobilemedia.core.ui.screens.AlbumListScreen;
import lancs.mobilemedia.lib.exceptions.ImageNotFoundException;
import lancs.mobilemedia.lib.exceptions.NullAlbumDataReference;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;


public class SmsSenderController extends AbstractController {
	String selectedImageName = "null";
	String imageName = "";
	NetworkScreen networkScreen;
	public SmsSenderController(MainUIMidlet midlet,AlbumData albumData,AlbumListScreen albumListScreen,String imageName) {
		super(midlet,albumData,albumListScreen);
		this.imageName = imageName;
	}
	public boolean handleCommand(Command c) {
		String label = c.getLabel();
		System.out.println("SmsSenderController::handleCommand: " + label);
		if (label.equals("Send Photo by SMS")) {
			networkScreen = new NetworkScreen("Reciever Details");
			selectedImageName = imageName;
			networkScreen.setCommandListener(this);
			this.setCurrentScreen(networkScreen);
			return true;
		}else if (label.equals("Send Now")) {
			ImageData ii = null;
			ImageAccessor imageAccessor = null;
			byte[]imageBytes = null;
			try {
				ii = getAlbumData().getImageInfo(selectedImageName);
				imageBytes = imageAccessor.loadImageBytesFromRMS(ii.getParentAlbumName(),ii.getImageLabel(),ii.getForeignRecordId());
			}catch (ImageNotFoundException e) {
				Alert alert = new Alert("Error","The selected image can not be found",null,AlertType.ERROR);
				alert.setTimeout(5000);
			}catch (NullAlbumDataReference e) {
				this.setAlbumData(new AlbumData());
				Alert alert = new Alert("Error","The operation is not available. Try again later !",null,AlertType.ERROR);
				Display.getDisplay(midlet).setCurrent(alert,Display.getDisplay(midlet).getCurrent());
			}catch (PersistenceMechanismException e) {
				Alert alert = new Alert("Error","It was not possible to recovery the selected image",null,AlertType.ERROR);
				alert.setTimeout(5000);
			}
			System.out.println("SmsController::handleCommand - Sending bytes for image " + ii.getImageLabel() + " with length: " + imageBytes.);
			String smsPort = "1000";
			String destinationAddress = "5550001";
			String messageText = "Binary Message (No Text)";
			smsPort = networkScreen.getRecPort();
			destinationAddress = networkScreen.getRecPhoneNum();
			System.out.println("SmsController:handleCommand() - Info from Network Screen is: " + smsPort + " and " + destinationAddress);
			SmsSenderThread smsS = new SmsSenderThread(smsPort,destinationAddress,messageText);
			smsS.setBinaryData(imageBytes);
			new Thread(smsS).start();
			return true;
		}else if (label.equals("Cancel Send")) {
			System.out.println("Cancel sending of SMS message");
			return true;
		}
		return false;
	}
}



