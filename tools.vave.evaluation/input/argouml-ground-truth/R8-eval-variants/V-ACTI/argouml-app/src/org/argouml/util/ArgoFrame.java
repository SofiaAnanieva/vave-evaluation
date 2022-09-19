package org.argouml.util;

import java.awt.Frame;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class ArgoFrame {
	private static JFrame topFrame;
	private ArgoFrame() {
	}
	public static JFrame getInstance() {
		if (topFrame == null) {
			Frame rootFrame = JOptionPane.getRootFrame();
			if (rootFrame instanceof JFrame) {
				topFrame = (JFrame) rootFrame;
			}else {
				Frame[]frames = Frame.getFrames();
				for (int i = 0;i < frames.;i++) {
					if (frames[i]instanceof JFrame) {
						if (topFrame != null) {
						}else {
							topFrame = (JFrame) frames[i];
						}
					}
				}
			}
			ArgoDialog.setFrame(topFrame);
		}
		return topFrame;
	}
	public static void setInstance(JFrame frame) {
		topFrame = frame;
	}
}



