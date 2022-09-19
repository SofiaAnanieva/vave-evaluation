package lancs.mobilemedia.core.ui.screens;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;


public class PhotoListScreen extends List {
	public static final Command viewCommand = new Command("View",Command.ITEM,1);
	public static final Command addCommand = new Command("Add",Command.ITEM,1);
	public static final Command deleteCommand = new Command("Delete",Command.ITEM,1);
	public static final Command backCommand = new Command("Back",Command.BACK,0);
	public PhotoListScreen() {
		super("Choose Items",Choice.IMPLICIT);
	}
	public PhotoListScreen(String arg0,int arg1) {
		super(arg0,arg1);
	}
	public PhotoListScreen(String arg0,int arg1,String[]arg2,Image[]arg3) {
		super(arg0,arg1,arg2,arg3);
	}
	public void initMenu() {
		this.addCommand(viewCommand);
		this.addCommand(addCommand);
		this.addCommand(deleteCommand);
		this.addCommand(backCommand);
	}
}



