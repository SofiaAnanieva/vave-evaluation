package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import org.argouml.i18n.Translator;
import org.argouml.util.osdep.StartBrowser;
import org.argouml.ui.SystemInfoDialog;


public class ExceptionDialog extends JDialog implements ActionListener {
	private JButton closeButton;
	private JButton copyButton;
	private JLabel northLabel;
	private JEditorPane textArea;
	public ExceptionDialog(Frame f,Throwable e) {
		this(f,Translator.localize("dialog.exception.unknown.error"),e);
	}
	public ExceptionDialog(Frame f,String message,Throwable e) {
		this(f,message,e,false);
	}
	public ExceptionDialog(Frame f,String message,Throwable e,boolean highlightCause) {
		this(f,Translator.localize("dialog.exception.title"),Translator.localize("dialog.exception.message"),formatException(message,e,highlightCause));
	}
	public static String formatException(String message,Throwable e,boolean highlightCause) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		if (highlightCause&&e.getCause() != null) {
			pw.print(message);
			pw.print("<hr>System Info:<p>" + SystemInfoDialog.getInfo());
			pw.print("<p><hr>Error occurred at : " + new Date());
			pw.print("<p>  Cause : ");
			e.getCause().printStackTrace(pw);
			pw.print("-------<p>Full exception : ");
		}
		e.printStackTrace(pw);
		return sw.toString();
	}
	public ExceptionDialog(Frame f,String title,String intro,String message) {
		super(f);
		setResizable(true);
		setModal(false);
		setTitle(title);
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		getContentPane().setLayout(new BorderLayout(0,0));
		northLabel = new JLabel(intro);
		northLabel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		getContentPane().add(northLabel,BorderLayout.NORTH);
		textArea = new JEditorPane();
		textArea.setContentType("text/html");
		textArea.setEditable(false);
		textArea.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent hle) {
				linkEvent(hle);
			}
		});
		textArea.setText(message.replaceAll("\n","<p>"));
		textArea.setCaretPosition(0);
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.add(new JScrollPane(textArea));
		centerPanel.setPreferredSize(new Dimension(500,200));
		getContentPane().add(centerPanel);
		copyButton = new JButton(Translator.localize("button.copy-to-clipboard"));
		copyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				copyActionPerformed(evt);
			}
		});
		closeButton = new JButton(Translator.localize("button.close"));
		closeButton.addActionListener(this);
		JPanel bottomPanel = new JPanel();
		bottomPanel.add(copyButton);
		bottomPanel.add(closeButton);
		getContentPane().add(bottomPanel,BorderLayout.SOUTH);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				disposeDialog();
			}
		});
		pack();
		Dimension contentPaneSize = getContentPane().getSize();
		setLocation(scrSize.width / 2 - contentPaneSize.width / 2,scrSize.height / 2 - contentPaneSize.height / 2);
	}
	public void actionPerformed(ActionEvent e) {
		disposeDialog();
	}
	private void copyActionPerformed(ActionEvent e) {
		assert e.getSource() == copyButton;
		textArea.setSelectionStart(0);
		textArea.setSelectionEnd(textArea.getText().length());
		textArea.copy();
		textArea.setSelectionEnd(0);
	}
	private void disposeDialog() {
		setVisible(false);
		dispose();
	}
	private void linkEvent(HyperlinkEvent e) {
		if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
			StartBrowser.openUrl(e.getURL());
		}
	}
	private static final long serialVersionUID = -2773182347529547418l;
}



