package org.argouml.persistence;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.log4j.Logger;
import org.argouml.application.helpers.ApplicationVersion;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectMember;
import org.argouml.ocl.OCLExpander;
import org.argouml.util.FileConstants;
import org.tigris.gef.ocl.TemplateReader;
import org.argouml.persistence.ZargoFilePersister;
import org.argouml.persistence.AbstractFilePersister;
import org.argouml.persistence.AbstractFilePersister.ProgressMgr;


class OldZargoFilePersister extends ZargoFilePersister {
	private static final Logger LOG = Logger.getLogger(OldZargoFilePersister.class);
	private static final String ARGO_MINI_TEE = "/org/argouml/persistence/argo.tee";
	public OldZargoFilePersister() {
	}
	protected String getDesc() {
		return Translator.localize("combobox.filefilter.zargo");
	}
	public boolean isSaveEnabled() {
		return true;
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
		BufferedWriter writer = null;
		try {
			project.setFile(file);
			project.setVersion(ApplicationVersion.getVersion());
			project.setPersistenceVersion(PERSISTENCE_VERSION);
			ZipOutputStream stream = new ZipOutputStream(new FileOutputStream(file));
			writer = new BufferedWriter(new OutputStreamWriter(stream,"UTF-8"));
			ZipEntry zipEntry = new ZipEntry(PersistenceManager.getInstance().getProjectBaseName(project) + FileConstants.UNCOMPRESSED_FILE_EXT);
			stream.putNextEntry(zipEntry);
			Hashtable templates = TemplateReader.getInstance().read(ARGO_MINI_TEE);
			OCLExpander expander = new OCLExpander(templates);
			expander.expand(writer,project);
			writer.flush();
			stream.closeEntry();
			int counter = 0;
			int size = project.getMembers().size();
			Collection<String>names = new ArrayList<String>();
			for (int i = 0;i < size;i++) {
				ProjectMember projectMember = project.getMembers().get(i);
				if (!(projectMember.getType().equalsIgnoreCase("xmi"))) {
					if (LOG.isInfoEnabled()) {
						ProjectMember pm = project.getMembers().get(i);
						LOG.info("Saving member: " + pm.getZipName());
					}
					String name = projectMember.getZipName();
					String originalName = name;
					while (names.contains(name)) {
						name = ++ counter + originalName;
					}
					names.add(name);
					stream.putNextEntry(new ZipEntry(name));
					MemberFilePersister persister = getMemberFilePersister(projectMember);
					persister.save(projectMember,stream);
					stream.flush();
					stream.closeEntry();
				}
			}
			for (int i = 0;i < size;i++) {
				ProjectMember projectMember = project.getMembers().get(i);
				if (projectMember.getType().equalsIgnoreCase("xmi")) {
					if (LOG.isInfoEnabled()) {
						ProjectMember pm = project.getMembers().get(i);
						LOG.info("Saving member of type: " + pm.getType());
					}
					stream.putNextEntry(new ZipEntry(projectMember.getZipName()));
					OldModelMemberFilePersister persister = new OldModelMemberFilePersister();
					persister.save(projectMember,stream);
					stream.flush();
				}
			}
			if (lastArchiveFile.exists()) {
				lastArchiveFile.delete();
			}
			if (tempFile.exists()&&!lastArchiveFile.exists()) {
				tempFile.renameTo(lastArchiveFile);
			}
			if (tempFile.exists()) {
				tempFile.delete();
			}
			progressMgr.nextPhase();
		}catch (Exception e) {
			LOG.error("Exception occured during save attempt",e);
			try {
				writer.close();
			}catch (Exception ex) {
			}
			file.delete();
			tempFile.renameTo(file);
			throw new SaveException(e);
		}
		try {
			writer.close();
		}catch (IOException ex) {
			LOG.error("Failed to close save output writer",ex);
		}
	}
	public boolean isLoadEnabled() {
		return false;
	}
}



