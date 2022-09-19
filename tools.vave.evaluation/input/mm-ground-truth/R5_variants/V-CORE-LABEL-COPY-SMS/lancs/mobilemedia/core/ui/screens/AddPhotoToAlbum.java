package lancs.mobilemedia.core.ui.screens;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.TextField;


public class AddPhotoToAlbum extends Form {
	TextField labeltxt = new TextField("Photo label","",15,TextField.ANY);
	TextField photopathtxt = new TextField("Path","",20,TextField.ANY);
	Image image = null;
	Command ok;
	Command cancel;
	public AddPhotoToAlbum(String title) {
		super(title);
		this.append(labeltxt);
		this.append(photopathtxt);
		ok = new Command("Save Photo",Command.SCREEN,0);
		cancel = new Command("Cancel",Command.EXIT,1);
		this.addCommand(ok);
		this.addCommand(cancel);
	}
	public String getPhotoName() {
		return labeltxt.getString();
	}
	public void setPhotoName(String photoName) {
		labeltxt.setString(photoName);
	}
	public String getPath() {
		return photopathtxt.getString();
	}
	public void setLabePhotoPath(String label) {
		photopathtxt.setLabel(label);
	}
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
}



