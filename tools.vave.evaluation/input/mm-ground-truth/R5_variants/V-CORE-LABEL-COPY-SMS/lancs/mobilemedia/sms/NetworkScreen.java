package lancs.mobilemedia.sms;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.TextField;


public class NetworkScreen extends Form {
	TextField recPhoneNum = new TextField("Phone #","5550001",15,TextField.ANY);
	String rPort = "1000";
	Command ok;
	Command cancel;
	public NetworkScreen(String title) {
		super(title);
		recPhoneNum.setString("5550001");
		this.append(recPhoneNum);
		ok = new Command("Send Now",Command.OK,0);
		cancel = new Command("Cancel",Command.EXIT,1);
		this.addCommand(ok);
		this.addCommand(cancel);
	}
	public NetworkScreen(String title,Item[]items) {
		super(title,items);
	}
	public String getRecPhoneNum() {
		return recPhoneNum.getString();
	}
	public String getRecPort() {
		return rPort;
	}
}



