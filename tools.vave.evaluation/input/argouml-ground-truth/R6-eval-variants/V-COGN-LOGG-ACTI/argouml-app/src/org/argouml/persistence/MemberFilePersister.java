package org.argouml.persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URL;
import org.argouml.application.api.Argo;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectMember;
import org.xml.sax.InputSource;


abstract class MemberFilePersister {
	public abstract void load(Project project,InputStream inputStream)throws OpenException;
	public abstract void load(Project project,URL url)throws OpenException;
	public abstract void load(Project project,InputSource inputSource)throws OpenException;
	public abstract String getMainTag();
	public abstract void save(ProjectMember member,OutputStream stream)throws SaveException;
	protected void addXmlFileToWriter(PrintWriter writer,File file)throws SaveException {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),Argo.getEncoding()));
			String line = reader.readLine();
			while (line != null&&(line.startsWith("<?xml ")||line.startsWith("<!DOCTYPE "))) {
				line = reader.readLine();
			}
			while (line != null) {
				(writer).println(line);
				line = reader.readLine();
			}
			reader.close();
		}catch (FileNotFoundException e) {
			throw new SaveException(e);
		}catch (IOException e) {
			throw new SaveException(e);
		}
	}
}



