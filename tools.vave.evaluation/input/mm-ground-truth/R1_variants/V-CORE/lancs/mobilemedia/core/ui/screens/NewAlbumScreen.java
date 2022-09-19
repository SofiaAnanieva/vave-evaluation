package lancs.mobilemedia.core.ui.screens;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.TextField;


public class NewAlbumScreen extends Form {
	TextField albumName = new TextField("Album Name","",15,TextField.ANY);
	Command ok;
	Command cancel;
	public NewAlbumScreen(String name) {
		super(name);
		this.append(albumName);
		ok = new Command("Save",Command.SCREEN,0);
		cancel = new Command("Cancel",Command.EXIT,1);
		this.addCommand(ok);
		this.addCommand(cancel);
	}
	public NewAlbumScreen(String title,Item[]items) {
		super(title,items);
	}
	public String getAlbumName() {
		return albumName.getString();
	}
}



