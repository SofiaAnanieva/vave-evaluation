package lancs.mobilemedia.core.ui.controller;


public class ScreenSingleton {
	private static ScreenSingleton instance;
	private String currentScreenName;
	private String currentStoreName = "My Photo Album";
	private ScreenSingleton() {
	}
	public static ScreenSingleton getInstance() {
		if (instance == null)instance = new ScreenSingleton();
		return instance;
	}
	public void setCurrentScreenName(String currentScreenName) {
		this.currentScreenName = currentScreenName;
	}
	public String getCurrentScreenName() {
		return currentScreenName;
	}
	public void setCurrentStoreName(String currentStoreName) {
		this.currentStoreName = currentStoreName;
	}
	public String getCurrentStoreName() {
		return currentStoreName;
	}
}



