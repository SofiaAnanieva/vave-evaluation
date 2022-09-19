package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import org.argouml.application.helpers.ApplicationVersion;


public class HelpBox extends JFrame implements HyperlinkListener {
	private JTabbedPane tabs = new JTabbedPane();
	private JEditorPane[]panes = null;
	private String pages[][] =  { {"Manual",ApplicationVersion.getOnlineManual(),"The ArgoUML online manual"}, {"Support",ApplicationVersion.getOnlineSupport(),"The ArgoUML support page"}};
	public HelpBox(String title) {
		super(title);
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(scrSize.width / 2 - 400,scrSize.height / 2 - 300);
		getContentPane().setLayout(new BorderLayout(0,0));
		setSize(800,600);
		panes = new JEditorPane[pages.];
		for (int i = 0;i < pages.;i++) {
			panes[i] = new JEditorPane();
			panes[i].setEditable(false);
			panes[i].setSize(780,580);
			panes[i].addHyperlinkListener(this);
			URL paneURL = null;
			try {
				paneURL = new URL(pages[i][1]);
			}catch (MalformedURLException e) {
			}
			if (paneURL != null) {
				try {
					panes[i].setPage(paneURL);
				}catch (IOException e) {
				}
			}
			JScrollPane paneScrollPane = new JScrollPane(panes[i]);
			paneScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			paneScrollPane.setPreferredSize(new Dimension(800,600));
			paneScrollPane.setMinimumSize(new Dimension(400,300));
			tabs.addTab(pages[i][0],null,paneScrollPane,pages[i][2]);
		}
		getContentPane().add(tabs,BorderLayout.CENTER);
	}
	public void hyperlinkUpdate(HyperlinkEvent event) {
		if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			JEditorPane pane = (JEditorPane) event.getSource();
			try {
				pane.setPage(event.getURL());
			}catch (IOException ioe) {
			}
		}
	}
	private static final long serialVersionUID = 0l;
}



