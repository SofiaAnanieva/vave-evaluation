package org.argouml.uml.reveng;

import java.util.List;


public interface SettingsTypes {
	interface Setting {
		String getLabel();
	}
	interface Setting2 extends Setting {
		String getDescription();
	}
	interface UniqueSelection extends Setting {
		public int UNDEFINED_SELECTION = -1;
		List<String>getOptions();
		int getDefaultSelection();
		boolean setSelection(int selection);
		int getSelection();
	}
	interface UniqueSelection2 extends UniqueSelection,Setting2 {
	}
	interface UserString extends Setting {
		String getDefaultString();
		String getUserString();
		void setUserString(String userString);
	}
	interface UserString2 extends UserString,Setting2 {
	}
	interface BooleanSelection extends Setting {
		boolean getDefaultValue();
		boolean isSelected();
		void setSelected(boolean selected);
	}
	interface BooleanSelection2 extends BooleanSelection,Setting2 {
	}
	interface PathSelection extends Setting2 {
		String getDefaultPath();
		String getPath();
		void setPath(String path);
	}
	interface PathListSelection extends Setting2 {
		List<String>getDefaultPathList();
		List<String>getPathList();
		void setPathList(List<String>pathList);
	}
}



