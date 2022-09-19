package org.argouml.persistence;

import java.awt.Component;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import org.argouml.application.api.Argo;
import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.tigris.gef.util.UnexpectedException;
import org.argouml.persistence.UmlFilePersister;


public final class PersistenceManager {
	private static final PersistenceManager INSTANCE = new PersistenceManager();
	private AbstractFilePersister defaultPersister;
	private List<AbstractFilePersister>otherPersisters = new ArrayList<AbstractFilePersister>();
	private UmlFilePersister quickViewDump;
	private XmiFilePersister xmiPersister;
	private XmiFilePersister xmlPersister;
	private UmlFilePersister umlPersister;
	private ZipFilePersister zipPersister;
	private AbstractFilePersister savePersister;
	public static final ConfigurationKey KEY_PROJECT_NAME_PATH = Configuration.makeKey("project","name","path");
	public static final ConfigurationKey KEY_OPEN_PROJECT_PATH = Configuration.makeKey("project","open","path");
	public static final ConfigurationKey KEY_IMPORT_XMI_PATH = Configuration.makeKey("xmi","import","path");
	private DiagramMemberFilePersister diagramMemberFilePersister = new DiagramMemberFilePersister();
	public static PersistenceManager getInstance() {
		return INSTANCE;
	}
	private PersistenceManager() {
		defaultPersister = new OldZargoFilePersister();
		quickViewDump = new UmlFilePersister();
		xmiPersister = new XmiFilePersister();
		otherPersisters.add(xmiPersister);
		xmlPersister = new XmlFilePersister();
		otherPersisters.add(xmlPersister);
		umlPersister = new UmlFilePersister();
		otherPersisters.add(umlPersister);
		zipPersister = new ZipFilePersister();
		otherPersisters.add(zipPersister);
	}
	public void register(AbstractFilePersister fp) {
		otherPersisters.add(fp);
	}
	public AbstractFilePersister getPersisterFromFileName(String name) {
		if (defaultPersister.isFileExtensionApplicable(name)) {
			return defaultPersister;
		}
		for (AbstractFilePersister persister:otherPersisters) {
			if (persister.isFileExtensionApplicable(name)) {
				return persister;
			}
		}
		return null;
	}
	public void setSaveFileChooserFilters(JFileChooser chooser,String fileName) {
		chooser.addChoosableFileFilter(defaultPersister);
		AbstractFilePersister defaultFileFilter = defaultPersister;
		for (AbstractFilePersister fp:otherPersisters) {
			if (fp.isSaveEnabled()&&!fp.equals(xmiPersister)&&!fp.equals(xmlPersister)) {
				chooser.addChoosableFileFilter(fp);
				if (fileName != null&&fp.isFileExtensionApplicable(fileName)) {
					defaultFileFilter = fp;
				}
			}
		}
		chooser.setFileFilter(defaultFileFilter);
	}
	public void setOpenFileChooserFilter(JFileChooser chooser) {
		MultitypeFileFilter mf = new MultitypeFileFilter();
		mf.add(defaultPersister);
		chooser.addChoosableFileFilter(mf);
		chooser.addChoosableFileFilter(defaultPersister);
		Iterator iter = otherPersisters.iterator();
		while (iter.hasNext()) {
			AbstractFilePersister ff = (AbstractFilePersister) iter.next();
			if (ff.isLoadEnabled()) {
				mf.add(ff);
				chooser.addChoosableFileFilter(ff);
			}
		}
		chooser.setFileFilter(mf);
	}
	public void setXmiFileChooserFilter(JFileChooser chooser) {
		chooser.addChoosableFileFilter(xmiPersister);
		chooser.setFileFilter(xmiPersister);
	}
	public String getDefaultExtension() {
		return defaultPersister.getExtension();
	}
	public String getXmiExtension() {
		return xmiPersister.getExtension();
	}
	public String fixExtension(String in) {
		if (getPersisterFromFileName(in) == null) {
			in += "." + getDefaultExtension();
		}
		return in;
	}
	public String fixXmiExtension(String in) {
		if (getPersisterFromFileName(in) != xmiPersister) {
			in += "." + getXmiExtension();
		}
		return in;
	}
	public URI fixUriExtension(URI in) {
		URI newUri;
		String n = in.toString();
		n = fixExtension(n);
		try {
			newUri = new URI(n);
		}catch (java.net.URISyntaxException e) {
			throw new UnexpectedException(e);
		}
		return newUri;
	}
	public String getBaseName(String n) {
		AbstractFilePersister p = getPersisterFromFileName(n);
		if (p == null) {
			return n;
		}
		int extLength = p.getExtension().length() + 1;
		return n.substring(0,n.length() - extLength);
	}
	public String getProjectBaseName(Project p) {
		URI uri = p.getUri();
		String name = Translator.localize("label.projectbrowser-title");
		if (uri != null) {
			name = new File(uri).getName();
		}
		return getBaseName(name);
	}
	public void setProjectName(final String n,Project p)throws URISyntaxException {
		String s = "";
		if (p.getURI() != null) {
			s = p.getURI().toString();
		}
		s = s.substring(0,s.lastIndexOf("/") + 1) + n;
		setProjectURI(new URI(s),p);
	}
	public void setProjectURI(URI theUri,Project p) {
		if (theUri != null) {
			theUri = fixUriExtension(theUri);
		}
		p.setUri(theUri);
	}
	public String getQuickViewDump(Project project) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			quickViewDump.writeProject(project,stream,null);
		}catch (Exception e) {
			e.printStackTrace(new PrintStream(stream));
		}
		try {
			return stream.toString(Argo.getEncoding());
		}catch (UnsupportedEncodingException e) {
			return e.toString();
		}
	}
	DiagramMemberFilePersister getDiagramMemberFilePersister() {
		return diagramMemberFilePersister;
	}
	public void setDiagramMemberFilePersister(DiagramMemberFilePersister persister) {
		diagramMemberFilePersister = persister;
	}
	public boolean confirmOverwrite(Component frame,boolean overwrite,File file) {
		if (file.exists()&&!overwrite) {
			String sConfirm = Translator.messageFormat("optionpane.confirm-overwrite",new Object[] {file});
			int nResult = JOptionPane.showConfirmDialog(frame,sConfirm,Translator.localize("optionpane.confirm-overwrite-title"),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
			if (nResult != JOptionPane.YES_OPTION) {
				return false;
			}
		}
		return true;
	}
	public void setSavePersister(AbstractFilePersister persister) {
		savePersister = persister;
	}
	public AbstractFilePersister getSavePersister() {
		return savePersister;
	}
	public void addTranslation(final String originalClassName,final String newClassName) {
		getDiagramMemberFilePersister().addTranslation(originalClassName,newClassName);
	}
}

class MultitypeFileFilter extends FileFilter {
	private ArrayList<FileFilter>filters;
	private ArrayList<String>extensions;
	private String desc;
	public MultitypeFileFilter() {
		super();
		filters = new ArrayList<FileFilter>();
		extensions = new ArrayList<String>();
	}
	public void add(AbstractFilePersister filter) {
		filters.add(filter);
		String extension = filter.getExtension();
		if (!extensions.contains(extension)) {
			extensions.add(filter.getExtension());
			desc = ((desc == null)?"":desc + ", ") + "*." + extension;
		}
	}
	public Collection<FileFilter>getAll() {
		return filters;
	}
	@Override public boolean accept(File arg0) {
		for (FileFilter ff:filters) {
			if (ff.accept(arg0)) {
				return true;
			}
		}
		return false;
	}
	@Override public String getDescription() {
		Object[]s =  {desc};
		return Translator.messageFormat("filechooser.all-types-desc",s);
	}
}



