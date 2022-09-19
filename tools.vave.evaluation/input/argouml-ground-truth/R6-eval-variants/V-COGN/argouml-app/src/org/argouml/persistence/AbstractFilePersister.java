package org.argouml.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.event.EventListenerList;
import javax.swing.filechooser.FileFilter;
import org.argouml.kernel.ProfileConfiguration;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectMember;
import org.argouml.taskmgmt.ProgressEvent;
import org.argouml.taskmgmt.ProgressListener;
import org.argouml.uml.ProjectMemberModel;
import org.argouml.uml.cognitive.ProjectMemberTodoList;
import org.argouml.uml.diagram.ProjectMemberDiagram;
import org.argouml.util.ThreadUtils;


public abstract class AbstractFilePersister extends FileFilter implements ProjectFilePersister {
	private static Map<Class,Class<?extends MemberFilePersister>>persistersByClass = new HashMap<Class,Class<?extends MemberFilePersister>>();
	private static Map<String,Class<?extends MemberFilePersister>>persistersByTag = new HashMap<String,Class<?extends MemberFilePersister>>();
	static {
	registerPersister(ProjectMemberDiagram.class,"pgml",DiagramMemberFilePersister.class);
	registerPersister(ProfileConfiguration.class,"profile",ProfileConfigurationFilePersister.class);
	registerPersister(ProjectMemberTodoList.class,"todo",TodoListMemberFilePersister.class);
	registerPersister(ProjectMemberModel.class,"xmi",ModelMemberFilePersister.class);
}
	private EventListenerList listenerList = new EventListenerList();
	private static boolean registerPersister(Class target,String tag,Class<?extends MemberFilePersister>persister) {
		persistersByClass.put(target,persister);
		persistersByTag.put(tag,persister);
		return true;
	}
	protected File createTempFile(File file)throws FileNotFoundException,IOException {
		File tempFile = new File(file.getAbsolutePath() + "#");
		if (tempFile.exists()) {
			tempFile.delete();
		}
		if (file.exists()) {
			copyFile(file,tempFile);
		}
		return tempFile;
	}
	protected File copyFile(File src,File dest)throws FileNotFoundException,IOException {
		FileInputStream fis = new FileInputStream(src);
		FileOutputStream fos = new FileOutputStream(dest);
		byte[]buf = new byte[1024];
		int i = 0;
		while ((i = fis.read(buf)) != -1) {
			fos.write(buf,0,i);
		}
		fis.close();
		fos.close();
		dest.setLastModified(src.lastModified());
		return dest;
	}
	public boolean accept(File f) {
		if (f == null) {
			return false;
		}
		if (f.isDirectory()) {
			return true;
		}
		String s = getExtension(f);
		if (s != null) {
			if (s.equalsIgnoreCase(getExtension())) {
				return true;
			}
		}
		return false;
	}
	public abstract String getExtension();
	protected abstract String getDesc();
	private static String getExtension(File f) {
		if (f == null) {
			return null;
		}
		return getExtension(f.getName());
	}
	private static String getExtension(String filename) {
		int i = filename.lastIndexOf('.');
		if (i > 0&&i < filename.length() - 1) {
			return filename.substring(i + 1).toLowerCase();
		}
		return null;
	}
	public boolean isFileExtensionApplicable(String filename) {
		return filename.toLowerCase().endsWith("." + getExtension());
	}
	public String getDescription() {
		return getDesc() + " (*." + getExtension() + ")";
	}
	public final void save(Project project,File file)throws SaveException,InterruptedException {
		preSave(project,file);
		doSave(project,file);
		postSave(project,file);
	}
	private void preSave(Project project,File file)throws SaveException {
		if (project == null&&file == null) {
			throw new SaveException("No project nor file given");
		}
	}
	private void postSave(Project project,File file)throws SaveException {
		if (project == null&&file == null) {
			throw new SaveException("No project nor file given");
		}
	}
	protected abstract void doSave(Project project,File file)throws SaveException,InterruptedException;
	public boolean isSaveEnabled() {
		return true;
	}
	public boolean isLoadEnabled() {
		return true;
	}
	public abstract Project doLoad(File file)throws OpenException,InterruptedException;
	public void addProgressListener(ProgressListener listener) {
		listenerList.add(ProgressListener.class,listener);
	}
	public void removeProgressListener(ProgressListener listener) {
		listenerList.remove(ProgressListener.class,listener);
	}
	public abstract boolean hasAnIcon();
	protected MemberFilePersister getMemberFilePersister(ProjectMember pm) {
		Class<?extends MemberFilePersister>persister = null;
		if (persistersByClass.containsKey(pm)) {
			persister = persistersByClass.get(pm);
		}else {
			for (Class clazz:persistersByClass.keySet()) {
				if (clazz.isAssignableFrom(pm.getClass())) {
					persister = persistersByClass.get(clazz);
					break;
				}
			}
		}
		if (persister != null) {
			return newPersister(persister);
		}
		return null;
	}
	protected MemberFilePersister getMemberFilePersister(String tag) {
		Class<?extends MemberFilePersister>persister = persistersByTag.get(tag);
		if (persister != null) {
			return newPersister(persister);
		}
		return null;
	}
	private static MemberFilePersister newPersister(Class<?extends MemberFilePersister>clazz) {
		try {
			return clazz.newInstance();
		}catch (InstantiationException e) {
			return null;
		}catch (IllegalAccessException e) {
			return null;
		}
	}
	class ProgressMgr implements ProgressListener {
		private int percentPhasesComplete;
		private int phasesCompleted;
		private int numberOfPhases;
		public void setPercentPhasesComplete(int aPercentPhasesComplete) {
			this.percentPhasesComplete = aPercentPhasesComplete;
		}
		public void setPhasesCompleted(int aPhasesCompleted) {
			this.phasesCompleted = aPhasesCompleted;
		}
		public void setNumberOfPhases(int aNumberOfPhases) {
			this.numberOfPhases = aNumberOfPhases;
		}
		public int getNumberOfPhases() {
			return this.numberOfPhases;
		}
		protected void nextPhase()throws InterruptedException {
			ThreadUtils.checkIfInterrupted();
			++
			phasesCompleted;
			percentPhasesComplete = (phasesCompleted * 100) / numberOfPhases;
			fireProgressEvent(percentPhasesComplete);
		}
		public void progress(ProgressEvent event)throws InterruptedException {
			ThreadUtils.checkIfInterrupted();
			int percentPhasesLeft = 100 - percentPhasesComplete;
			long position = event.getPosition();
			long length = event.getLength();
			long proportion = (position * percentPhasesLeft) / length;
			fireProgressEvent(percentPhasesComplete + proportion);
		}
		protected void fireProgressEvent(long percent)throws InterruptedException {
			ProgressEvent event = null;
			Object[]listeners = listenerList.getListenerList();
			for (int i = listeners. - 2;i >= 0;i -= 2) {
				if (listeners[i] == ProgressListener.class) {
					if (event == null) {
						event = new ProgressEvent(this,percent,100);
					}
					((ProgressListener) listeners[i + 1]).progress(event);
				}
			}
		}
	}
}



