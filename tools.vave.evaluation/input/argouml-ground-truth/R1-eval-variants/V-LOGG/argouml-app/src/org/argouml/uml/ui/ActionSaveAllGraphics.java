package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoStatusEvent;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramUtils;
import org.argouml.util.ArgoFrame;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.SaveGraphicsAction;
import org.tigris.gef.util.Util;
import org.argouml.uml.ui.SaveGraphicsManager;


public class ActionSaveAllGraphics extends AbstractAction {
	private static final Logger LOG = Logger.getLogger(ActionSaveAllGraphics.class);
	private boolean overwrite;
	public ActionSaveAllGraphics() {
		super(Translator.localize("action.save-all-graphics"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("action.save-all-graphics"));
	}
	public void actionPerformed(ActionEvent ae) {
		trySave(false);
	}
	public boolean trySave(boolean canOverwrite) {
		return trySave(canOverwrite,null);
	}
	public boolean trySave(boolean canOverwrite,File directory) {
		overwrite = canOverwrite;
		Project p = ProjectManager.getManager().getCurrentProject();
		TargetManager tm = TargetManager.getInstance();
		File saveDir = (directory != null)?directory:getSaveDir(p);
		if (saveDir == null) {
			return false;
		}
		boolean okSoFar = true;
		ArgoDiagram activeDiagram = DiagramUtils.getActiveDiagram();
		for (ArgoDiagram d:p.getDiagramList()) {
			tm.setTarget(d);
			okSoFar = trySaveDiagram(d,saveDir);
			if (!okSoFar) {
				break;
			}
		}
		tm.setTarget(activeDiagram);
		return okSoFar;
	}
	protected boolean trySaveDiagram(Object target,File saveDir) {
		if (target instanceof Diagram) {
			String defaultName = ((Diagram) target).getName();
			defaultName = Util.stripJunk(defaultName);
			try {
				File theFile = new File(saveDir,defaultName + "." + SaveGraphicsManager.getInstance().getDefaultSuffix());
				String name = theFile.getName();
				String path = theFile.getParent();
				SaveGraphicsAction cmd = SaveGraphicsManager.getInstance().getSaveActionBySuffix(SaveGraphicsManager.getInstance().getDefaultSuffix());
				if (cmd == null) {
					showStatus("Unknown graphics file type with extension " + SaveGraphicsManager.getInstance().getDefaultSuffix());
					return false;
				}
				showStatus("Writing " + path + name + "...");
				boolean result = saveGraphicsToFile(theFile,cmd);
				showStatus("Wrote " + path + name);
				return result;
			}catch (FileNotFoundException ignore) {
				LOG.error("got a FileNotFoundException",ignore);
			}catch (IOException ignore) {
				LOG.error("got an IOException",ignore);
			}
		}
		return false;
	}
	protected File getSaveDir(Project p) {
		JFileChooser chooser = getFileChooser(p);
		String fn = Configuration.getString(SaveGraphicsManager.KEY_SAVEALL_GRAPHICS_PATH);
		if (fn.length() > 0) {
			chooser.setSelectedFile(new File(fn));
		}
		int retval = chooser.showSaveDialog(ArgoFrame.getInstance());
		if (retval == JFileChooser.APPROVE_OPTION) {
			File theFile = chooser.getSelectedFile();
			String path = theFile.getPath();
			Configuration.setString(SaveGraphicsManager.KEY_SAVEALL_GRAPHICS_PATH,path);
			return theFile;
		}
		return null;
	}
	private boolean saveGraphicsToFile(File theFile,SaveGraphicsAction cmd)throws IOException {
		if (theFile.exists()&&!overwrite) {
			String message = Translator.messageFormat("optionpane.confirm-overwrite",new Object[] {theFile});
			String title = Translator.localize("optionpane.confirm-overwrite-title");
			Object[]options =  {Translator.localize("optionpane.confirm-overwrite.overwrite"),Translator.localize("optionpane.confirm-overwrite.overwrite-all"),Translator.localize("optionpane.confirm-overwrite.skip-this-one"),Translator.localize("optionpane.confirm-overwrite.cancel")};
			int response = JOptionPane.showOptionDialog(ArgoFrame.getInstance(),message,title,JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
			if (response == 1) {
				overwrite = true;
			}
			if (response == 2) {
				return true;
			}
			if (response == 3) {
				return false;
			}
			if (response == JOptionPane.CLOSED_OPTION) {
				return false;
			}
		}
		FileOutputStream fo = null;
		try {
			fo = new FileOutputStream(theFile);
			cmd.setStream(fo);
			cmd.setScale(Configuration.getInteger(SaveGraphicsManager.KEY_GRAPHICS_RESOLUTION,1));
			cmd.actionPerformed(null);
		}finally {
			if (fo != null) {
				fo.close();
			}
		}
		return true;
	}
	private JFileChooser getFileChooser(Project p) {
		JFileChooser chooser = null;
		try {
			if (p != null&&p.getURI() != null&&p.getURI().toURL().getFile().length() > 0) {
				chooser = new JFileChooser(p.getURI().toURL().getFile());
			}
		}catch (MalformedURLException ex) {
			LOG.error("exception in opening JFileChooser",ex);
		}
		if (chooser == null)chooser = new JFileChooser();
		chooser.setDialogTitle(Translator.localize("filechooser.save-all-graphics"));
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		return chooser;
	}
	private void showStatus(String text) {
		ArgoEventPump.fireEvent(new ArgoStatusEvent(ArgoEventTypes.STATUS_TEXT,this,text));
	}
}



