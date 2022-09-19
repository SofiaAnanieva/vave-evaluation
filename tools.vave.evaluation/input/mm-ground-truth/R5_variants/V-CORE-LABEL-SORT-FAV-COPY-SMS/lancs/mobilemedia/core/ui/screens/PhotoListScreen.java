package lancs.mobilemedia.core.ui.screens;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.List;


public class PhotoListScreen extends List {
	public static final Command viewCommand = new Command("View",Command.ITEM,1);
	public static final Command addCommand = new Command("Add",Command.ITEM,1);
	public static final Command deleteCommand = new Command("Delete",Command.ITEM,1);
	public static final Command backCommand = new Command("Back",Command.BACK,0);
	public static final Command editLabelCommand = new Command("Edit Label",Command.ITEM,1);
	public static final Command sortCommand = new Command("Sort by Views",Command.ITEM,1);
	public static final Command favoriteCommand = new Command("Set Favorite",Command.ITEM,1);
	public static final Command viewFavoritesCommand = new Command("View Favorites",Command.ITEM,1);
	public PhotoListScreen() {
		super("Choose Items",Choice.IMPLICIT);
	}
	public void initMenu() {
		this.addCommand(viewCommand);
		this.addCommand(addCommand);
		this.addCommand(deleteCommand);
		this.addCommand(editLabelCommand);
		this.addCommand(sortCommand);
		this.addCommand(favoriteCommand);
		this.addCommand(viewFavoritesCommand);
		this.addCommand(backCommand);
	}
}



