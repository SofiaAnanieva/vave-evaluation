package lancs.mobilemedia.core.ui.screens;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.TextField;


public class NewLabelScreen extends Form {
	public static final int NEW_ALBUM = 0;
	public static final int LABEL_PHOTO = 1;
	TextField labelName = new TextField("Name","",15,TextField.ANY);
	Command ok;
	Command cancel;
	private int formType;
	public NewLabelScreen(String name,int type) {
		super(name);
		this.formType = type;
		this.append(labelName);
		ok = new Command("Save",Command.SCREEN,0);
		cancel = new Command("Cancel",Command.EXIT,1);
		this.addCommand(ok);
		this.addCommand(cancel);
	}
	public NewLabelScreen(String title,Item[]items) {
		super(title,items);
	}
	public String getLabelName() {
		return labelName.getString();
	}
	public void setFormType(int formType) {
		this.formType = formType;
	}
	public int getFormType() {
		return formType;
	}
}



