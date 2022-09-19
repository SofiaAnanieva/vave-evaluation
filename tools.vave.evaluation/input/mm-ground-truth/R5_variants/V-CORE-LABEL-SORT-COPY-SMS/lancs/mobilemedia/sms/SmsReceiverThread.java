package lancs.mobilemedia.sms;

import java.io.IOException;
import java.io.InterruptedIOException;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.wireless.messaging.Message;
import javax.wireless.messaging.MessageConnection;
import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.ui.screens.AlbumListScreen;


public class SmsReceiverThread implements Runnable {
	SmsReceiverController controller = null;
	String[]connections;
	String smsPort;
	MessageConnection smsconn;
	Message msg;
	String senderAddress;
	Command acceptPhotoCommand = new Command("Accept Photo",Command.ITEM,1);
	Command rejectPhotoCommand = new Command("Reject Photo",Command.ITEM,1);
	Command errorNotice = new Command("Ok",Command.ITEM,1);
	public SmsReceiverThread(MainUIMidlet midlet,AlbumData albumData,AlbumListScreen albumListScreen,SmsReceiverController controller) {
		this.controller = controller;
		smsPort = "1000";
	}
	public void run() {
		SmsMessaging smsMessenger = new SmsMessaging();
		while (true) {
			smsMessenger.setSmsReceivePort(smsPort);
			byte[]receivedData = null;
			try {
				receivedData = smsMessenger.receiveImage();
			}catch (InterruptedIOException e) {
				Alert alert = new Alert("Error Incoming Photo");
				alert.setString("" + "You have just received a bad fragmentated photo which was not possible to recovery.");
				alert.addCommand(errorNotice);
				System.out.println("Error interreput");
				alert.setCommandListener(controller);
				controller.setCurrentScreen(alert);
				smsMessenger.cleanUpReceiverConnections();
				continue;
			}catch (IOException e) {
				Alert alert = new Alert("Error Incoming Photo");
				alert.setString("" + "You have just received a bad fragmentated photo which was not possible to recovery.");
				alert.addCommand(errorNotice);
				System.out.println("Bad fragmentation");
				alert.setCommandListener(controller);
				controller.setCurrentScreen(alert);
				smsMessenger.cleanUpReceiverConnections();
				continue;
			}
			Alert alert = new Alert("New Incoming Photo");
			alert.setString("A MobileMedia user has sent you a Photo. Do you want to accept it?");
			alert.addCommand(acceptPhotoCommand);
			alert.addCommand(rejectPhotoCommand);
			controller.setIncommingData(receivedData);
			alert.setCommandListener(controller);
			controller.setCurrentScreen(alert);
		}
	}
}



