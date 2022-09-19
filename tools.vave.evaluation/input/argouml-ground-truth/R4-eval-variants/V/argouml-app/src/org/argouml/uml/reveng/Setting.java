package org.argouml.uml.reveng;

import java.util.Collections;
import java.util.List;


public class Setting {
	private String label;
	private String description;
	public Setting(String labelText) {
		super();
		label = labelText;
	}
	public Setting(String labelText,String descriptionText) {
		this(labelText);
		description = descriptionText;
	}
	public final String getLabel() {
		return label;
	}
	public String getDescription() {
		return description;
	}
}



