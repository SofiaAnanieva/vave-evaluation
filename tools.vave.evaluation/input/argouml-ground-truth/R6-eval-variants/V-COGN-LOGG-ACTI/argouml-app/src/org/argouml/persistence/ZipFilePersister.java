package org.argouml.persistence;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectFactory;
import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.ProjectMember;
import org.argouml.model.Model;
import org.xml.sax.InputSource;


class ZipFilePersister extends XmiFilePersister {
	private static final Logger LOG = Logger.getLogger(ZipFilePersister.class);
	public ZipFilePersister() {
	}
	public String getExtension() {
		return"zip";
	}
	protected String getDesc() {
		return Translator.localize("combobox.filefilter.zip");
	}
	public boolean isSaveEnabled() {
		return true;
	}
	public void doSave(Project project,File file)throws SaveException {
		LOG.info("Receiving file \'" + file.getName() + "\'");
		File lastArchiveFile = new File(file.getAbsolutePath() + "~");
		File tempFile = null;
		try {
			tempFile = createTempFile(file);
		}catch (FileNotFoundException e) {
			throw new SaveException("Failed to archive the previous file version",e);
		}catch (IOException e) {
			throw new SaveException("Failed to archive the previous file version",e);
		}
		OutputStream bufferedStream = null;
		try {
			ZipOutputStream stream = new ZipOutputStream(new FileOutputStream(file));
			String fileName = file.getName();
			ZipEntry xmiEntry = new ZipEntry(fileName.substring(0,fileName.lastIndexOf(".")));
			stream.putNextEntry(xmiEntry);
			bufferedStream = new BufferedOutputStream(stream);
			int size = project.getMembers().size();
			for (int i = 0;i < size;i++) {
				ProjectMember projectMember = project.getMembers().get(i);
				if (projectMember.getType().equalsIgnoreCase("xmi")) {
					if (LOG.isInfoEnabled()) {
						ProjectMember pm = (project.getMembers().get(i));
						LOG.info("Saving member of type: " + pm.getType());
					}
					MemberFilePersister persister = new ModelMemberFilePersister();
					persister.save(projectMember,bufferedStream);
				}
			}
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
		}catch (Exception e) {
			LOG.error("Exception occured during save attempt",e);
			try {
				bufferedStream.close();
			}catch (IOException ex) {
			}
			file.delete();
			tempFile.renameTo(file);
			throw new SaveException(e);
		}
		try {
			bufferedStream.close();
		}catch (IOException ex) {
			LOG.error("Failed to close save output writer",ex);
		}
	}
	public Project doLoad(File file)throws OpenException {
		LOG.info("Receiving file \'" + file.getName() + "\'");
		try {
			Project p = ProjectFactory.getInstance().createProject();
			String fileName = file.getName();
			String extension = fileName.substring(fileName.indexOf('.'),fileName.lastIndexOf('.'));
			InputStream stream = openZipStreamAt(file.toURI().toURL(),extension);
			InputSource is = new InputSource(new XmiInputStream(stream,this,100000,null));
			is.setSystemId(file.toURI().toURL().toExternalForm());
			ModelMemberFilePersister modelPersister = new ModelMemberFilePersister();
			modelPersister.readModels(is);
			Object model = modelPersister.getCurModel();
			Model.getUmlHelper().addListenersToModel(model);
			p.setUUIDRefs(modelPersister.getUUIDRefs());
			p.addMember(model);
			parseXmiExtensions(p);
			modelPersister.registerDiagrams(p);
			p.setRoot(model);
			p.setRoots(modelPersister.getElementsRead());
			ProjectManager.getManager().setSaveEnabled(false);
			return p;
		}catch (IOException e) {
			throw new OpenException(e);
		}
	}
	private ZipInputStream openZipStreamAt(URL url,String ext)throws IOException {
		ZipInputStream zis = new ZipInputStream(url.openStream());
		ZipEntry entry = zis.getNextEntry();
		while (entry != null&&!entry.getName().endsWith(ext)) {
			entry = zis.getNextEntry();
		}
		return zis;
	}
	@Override public boolean hasAnIcon() {
		return false;
	}
}



