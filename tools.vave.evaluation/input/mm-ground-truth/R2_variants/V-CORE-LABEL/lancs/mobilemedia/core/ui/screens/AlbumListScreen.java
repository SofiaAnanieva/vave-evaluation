package lancs.mobilemedia.core.ui.screens;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;


public class AlbumListScreen extends List {
	public static final Command exitCommand = new Command("Exit",Command.STOP,2);
	public static final Command selectCommand = new Command("Select",Command.ITEM,1);
	public static final Command createAlbumCommand = new Command("New Photo Album",Command.ITEM,1);
	public static final Command deleteAlbumCommand = new Command("Delete Album",Command.ITEM,1);
	public static final Command resetCommand = new Command("Reset",Command.ITEM,1);
	public AlbumListScreen() {
		super("Select Album",Choice.IMPLICIT);
	}
	public AlbumListScreen(String arg0,int arg1) {
		super(arg0,arg1);
	}
	public AlbumListScreen(String arg0,int arg1,String[]arg2,Image[]arg3) {
		super(arg0,arg1,arg2,arg3);
	}
	public void initMenu() {
		this.addCommand(exitCommand);
		this.addCommand(selectCommand);
		this.addCommand(createAlbumCommand);
		this.addCommand(deleteAlbumCommand);
		this.addCommand(resetCommand);
	}
	public void deleteAll() {
		for (int i = 0;i < this.size();i++) {
			this.delete(i);
		}
	}
	public void repaintListAlbum(String[]names) {
		String[]albumNames = names;
		this.deleteAll();
		for (int i = 0;i < albumNames.;i++) {
			if (albumNames[i] != null) {
				this.append(albumNames[i],null);
			}
		}
	}
}



