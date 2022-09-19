package org.argouml.persistence;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectFactory;
import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.ProjectMember;
import org.argouml.model.Model;
import org.argouml.util.ThreadUtils;
import org.xml.sax.InputSource;


class XmiFilePersister extends AbstractFilePersister implements XmiExtensionParser {
	private static final Logger LOG = Logger.getLogger(XmiFilePersister.class);
	private List<String>pgmlStrings = new ArrayList<String>();
	private String argoString;
	public XmiFilePersister() {
	}
	public String getExtension() {
		return"xmi";
	}
	protected String getDesc() {
		return Translator.localize("combobox.filefilter.xmi");
	}
	public boolean isSaveEnabled() {
		return false;
	}
	public void doSave(Project project,File file)throws SaveException,InterruptedException {
		ProgressMgr progressMgr = new ProgressMgr();
		progressMgr.setNumberOfPhases(4);
		progressMgr.nextPhase();
		File lastArchiveFile = new File(file.getAbsolutePath() + "~");
		File tempFile = null;
		try {
			tempFile = createTempFile(file);
		}catch (FileNotFoundException e) {
			throw new SaveException("Failed to archive the previous file version",e);
		}catch (IOException e) {
			throw new SaveException("Failed to archive the previous file version",e);
		}
		OutputStream stream = null;
		try {
			stream = new FileOutputStream(file);
			writeProject(project,stream,progressMgr);
			stream.close();
			if (lastArchiveFile.exists()) {
				lastArchiveFile.delete();
			}
			if (tempFile.exists()&&!lastArchiveFile.exists()) {
				tempFile.renameTo(lastArchiveFile);
			}
			if (tempFile.exists()) {
				tempFile.delete();
			}
		}catch (InterruptedException exc) {
			try {
				stream.close();
			}catch (IOException ex) {
			}
			throw exc;
		}catch (Exception e) {
			LOG.error("Exception occured during save attempt",e);
			try {
				stream.close();
			}catch (IOException ex) {
			}
			file.delete();
			tempFile.renameTo(file);
			throw new SaveException(e);
		}
		progressMgr.nextPhase();
	}
	void writeProject(Project project,OutputStream stream,ProgressMgr progressMgr)throws SaveException,InterruptedException {
		int size = project.getMembers().size();
		for (int i = 0;i < size;i++) {
			ProjectMember projectMember = project.getMembers().get(i);
			if (LOG.isInfoEnabled()) {
				LOG.info("Saving member of type: ");
			}
			MemberFilePersister persister = new ModelMemberFilePersister();
			persister.save(projectMember,stream);
		}
		if (progressMgr != null) {
			progressMgr.nextPhase();
		}
	}
	public Project doLoad(File file)throws OpenException,InterruptedException {
		LOG.info("Loading with XMIFilePersister");
		try {
			Project p = ProjectFactory.getInstance().createProject();
			long length = file.length();
			long phaseSpace = 100000;
			int phases = (int) (length / phaseSpace);
			if (phases < 10) {
				phaseSpace = length / 10;
				phases = 10;
			}
			LOG.info("File length is " + length + " phase space is " + phaseSpace + " phases is " + phases);
			ProgressMgr progressMgr = new ProgressMgr();
			progressMgr.setNumberOfPhases(phases);
			ThreadUtils.checkIfInterrupted();
			InputSource source = new InputSource(new XmiInputStream(file.toURI().toURL().openStream(),this,phaseSpace,progressMgr));
			source.setSystemId(file.toURI().toURL().toString());
			ModelMemberFilePersister modelPersister = new ModelMemberFilePersister();
			modelPersister.readModels(source);
			Object model = modelPersister.getCurModel();
			progressMgr.nextPhase();
			Model.getUmlHelper().addListenersToModel(model);
			p.setUUIDRefs(modelPersister.getUUIDRefs());
			p.addMember(model);
			parseXmiExtensions(p);
			modelPersister.registerDiagrams(p);
			p.setRoot(model);
			p.setRoots(modelPersister.getElementsRead());
			File defaultProjectFile = new File(file.getPath() + ".zargo");
			for (int i = 0;i < 99;i++) {
				if (!defaultProjectFile.exists()) {
					break;
				}
				defaultProjectFile = new File(file.getPath() + "." + i + ".zargo");
			}
			PersistenceManager.getInstance().setProjectURI(defaultProjectFile.toURI(),p);
			progressMgr.nextPhase();
			ProjectManager.getManager().setSaveEnabled(false);
			return p;
		}catch (IOException e) {
			throw new OpenException(e);
		}
	}
	public boolean hasAnIcon() {
		return true;
	}
	public void parse(String label,String xmiExtensionString) {
		if (label.equals("pgml")) {
			pgmlStrings.add(xmiExtensionString);
		}else if (label.equals("argo")) {
			argoString = xmiExtensionString;
		}
	}
	public void parseXmiExtensions(Project project)throws OpenException {
		if (argoString != null) {
			LOG.info("Parsing argoString " + argoString.length());
			StringReader inputStream = new StringReader(argoString);
			ArgoParser parser = new ArgoParser();
			try {
				parser.readProject(project,inputStream);
			}catch (Exception e) {
				throw new OpenException("Exception caught",e);
			}
		}
		for (String pgml:pgmlStrings) {
			LOG.info("Parsing pgml " + pgml.length());
			InputStream inputStream = new ByteArrayInputStream(pgml.getBytes());
			MemberFilePersister persister = PersistenceManager.getInstance().getDiagramMemberFilePersister();
			persister.load(project,inputStream);
		}
	}
}



