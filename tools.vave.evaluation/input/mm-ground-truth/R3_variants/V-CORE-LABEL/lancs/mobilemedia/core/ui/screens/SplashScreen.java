package lancs.mobilemedia.core.ui.screens;

import java.util.*;
import javax.microedition.lcdui.*;


public class SplashScreen extends Canvas {
	private Display display;
	private Displayable next;
	private Timer timer = new Timer();
	public SplashScreen(Display display,Displayable next) {
		this.display = display;
		this.next = next;
		display.setCurrent(this);
	}
	protected void keyPressed(int keyCode) {
		dismiss();
	}
	protected void paint(Graphics g) {
	}
	protected void pointerPressed(int x,int y) {
		dismiss();
	}
	protected void showNotify() {
		timer.schedule(new CountDown(),5000);
	}
	private void dismiss() {
		timer.cancel();
		display.setCurrent(next);
	}
	private class CountDown extends TimerTask {
	public void run() {
		dismiss();
	}
}
}



