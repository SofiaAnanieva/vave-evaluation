package org.argouml.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.argouml.application.api.Argo;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectMember;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ProjectMemberDiagram;
import org.tigris.gef.ocl.ExpansionException;
import org.tigris.gef.ocl.OCLExpander;
import org.tigris.gef.ocl.TemplateReader;
import org.xml.sax.InputSource;
import org.argouml.persistence.PGMLStackParser;


class DiagramMemberFilePersister extends MemberFilePersister {
	private static final String PGML_TEE = "/org/argouml/persistence/PGML.tee";
	private static final Map<String,String>CLASS_TRANSLATIONS = new HashMap<String,String>();
	@Override public void load(Project project,InputStream inputStream)throws OpenException {
		load(project,new InputSource(inputStream));
		try {
			inputStream.close();
		}catch (IOException e) {
			throw new OpenException("I/O error on stream close",e);
		}
	}
	@Override public void load(Project project,InputSource inputSource)throws OpenException {
		try {
			DiagramSettings defaultSettings = project.getProjectSettings().getDefaultDiagramSettings();
			PGMLStackParser parser = new PGMLStackParser(project.getUUIDRefs(),defaultSettings);
			for (Map.Entry<String,String>translation:CLASS_TRANSLATIONS.entrySet()) {
				parser.addTranslation(translation.getKey(),translation.getValue());
			}
			ArgoDiagram d = parser.readArgoDiagram(inputSource,false);
			project.addMember(d);
		}catch (Exception e) {
			if (e instanceof OpenException) {
				throw(OpenException) e;
			}
			throw new OpenException(e);
		}
	}
	@Override public void load(Project project,URL url)throws OpenException {
		load(project,new InputSource(url.toExternalForm()));
	}
	@Override public String getMainTag() {
		return"pgml";
	}
	@Override public void save(ProjectMember member,OutputStream outStream)throws SaveException {
		ProjectMemberDiagram diagramMember = (ProjectMemberDiagram) member;
		OCLExpander expander;
		try {
			expander = new OCLExpander(TemplateReader.getInstance().read(PGML_TEE));
		}catch (ExpansionException e) {
			throw new SaveException(e);
		}
		OutputStreamWriter outputWriter;
		try {
			outputWriter = new OutputStreamWriter(outStream,Argo.getEncoding());
		}catch (UnsupportedEncodingException e1) {
			throw new SaveException("Bad encoding",e1);
		}
		try {
			expander.expand(outputWriter,diagramMember.getDiagram());
		}catch (ExpansionException e) {
			throw new SaveException(e);
		}finally {
			try {
				outputWriter.flush();
			}catch (IOException e) {
				throw new SaveException(e);
			}
		}
	}
	public void addTranslation(final String originalClassName,final String newClassName) {
		CLASS_TRANSLATIONS.put(originalClassName,newClassName);
	}
}



