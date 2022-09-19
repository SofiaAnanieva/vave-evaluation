package org.argouml.uml.generator;

import javax.swing.Icon;


public class Language {
	private String name;
	private String title;
	private Icon icon;
	public Language(String theName,String theTitle,Icon theIcon) {
		this.name = theName;
		if (theTitle == null) {
			this.title = theName;
		}else {
			this.title = theTitle;
		}
		this.icon = theIcon;
	}
	public Language(String theName,String theTitle) {
		this(theName,theTitle,null);
	}
	public Language(String theName,Icon theIcon) {
		this(theName,theName,theIcon);
	}
	public Language(String theName) {
		this(theName,theName,null);
	}
	public Icon getIcon() {
		return icon;
	}
	public void setIcon(Icon theIcon) {
		this.icon = theIcon;
	}
	public String getName() {
		return name;
	}
	public void setName(String theName) {
		this.name = theName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String theTitle) {
		this.title = theTitle;
	}
	public String toString() {
		String tit = getTitle();
		return tit == null?"(no name)":tit;
	}
}



