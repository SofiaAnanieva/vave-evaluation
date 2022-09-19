package org.argouml.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.cognitive.Designer;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectMember;
import org.argouml.ocl.OCLExpander;
import org.argouml.uml.cognitive.ProjectMemberTodoList;
import org.tigris.gef.ocl.ExpansionException;
import org.tigris.gef.ocl.TemplateReader;


class TodoListMemberFilePersister extends MemberFilePersister {
	private static final Logger LOG = Logger.getLogger(ProjectMemberTodoList.class);
	private static final String TO_DO_TEE = "/org/argouml/persistence/todo.tee";
	public void load(Project project,InputStream inputStream)throws OpenException {
		try {
			TodoParser parser = new TodoParser();
			Reader reader = new InputStreamReader(inputStream,Argo.getEncoding());
			parser.readTodoList(reader);
			ProjectMemberTodoList pm = new ProjectMemberTodoList("",project);
			project.addMember(pm);
		}catch (Exception e) {
			if (e instanceof OpenException) {
				throw(OpenException) e;
			}
			throw new OpenException(e);
		}
	}
	@Override public void load(Project project,URL url)throws OpenException {
		try {
			load(project,url.openStream());
		}catch (IOException e) {
			throw new OpenException(e);
		}
	}
	public final String getMainTag() {
		return"todo";
	}
	public void save(ProjectMember member,OutputStream outStream)throws SaveException {
		OCLExpander expander;
		try {
			expander = new OCLExpander(TemplateReader.getInstance().read(TO_DO_TEE));
		}catch (ExpansionException e) {
			throw new SaveException(e);
		}
		PrintWriter pw;
		try {
			pw = new PrintWriter(new OutputStreamWriter(outStream,"UTF-8"));
		}catch (UnsupportedEncodingException e1) {
			throw new SaveException("UTF-8 encoding not supported on platform",e1);
		}
		try {
			Designer.disableCritiquing();
			expander.expand(pw,member);
		}catch (ExpansionException e) {
			throw new SaveException(e);
		}finally {
			pw.flush();
			Designer.enableCritiquing();
		}
	}
}



