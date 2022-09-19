package org.argouml.uml.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class TempFileUtils {
	public static File createTempDir() {
		File tmpdir = null;
		try {
			tmpdir = File.createTempFile("argouml",null);
			tmpdir.delete();
			if (!tmpdir.mkdir()) {
				return null;
			}
			return tmpdir;
		}catch (IOException ioe) {
			return null;
		}
	}
	private interface FileAction {
	void act(File file)throws IOException;
}
	private static void traverseDir(File dir,FileAction action)throws IOException {
		if (dir.exists()) {
			File[]files = dir.listFiles();
			for (int i = 0;i < files.;i++) {
				if (files[i].isDirectory()) {
					traverseDir(files[i],action);
				}else {
					action.act(files[i]);
				}
			}
			action.act(dir);
		}
	}
	public static Collection<SourceUnit>readAllFiles(File dir) {
		try {
			final List<SourceUnit>ret = new ArrayList<SourceUnit>();
			final int prefix = dir.getPath().length() + 1;
			traverseDir(dir,new FileAction() {
				public void act(File f)throws IOException {
					if (!f.isDirectory()&&!f.getName().endsWith(".bak")) {
						FileReader fr = new FileReader(f);
						BufferedReader bfr = new BufferedReader(fr);
						try {
							StringBuffer result = new StringBuffer((int) f.length());
							String line = bfr.readLine();
							do {
								result.append(line);
								line = bfr.readLine();
								if (line != null) {
									result.append('\n');
								}
							}while (line != null);
							ret.add(new SourceUnit(f.toString().substring(prefix),result.toString()));
						}finally {
							bfr.close();
							fr.close();
						}
					}
				}
			});
			return ret;
		}catch (IOException ioe) {
		}
		return null;
	}
	public static void deleteDir(File dir) {
		try {
			traverseDir(dir,new FileAction() {
				public void act(File f) {
					f.delete();
				}
			});
		}catch (IOException ioe) {
		}
	}
	public static Collection<String>readFileNames(File dir) {
		final List<String>ret = new ArrayList<String>();
		final int prefix = dir.getPath().length() + 1;
		try {
			traverseDir(dir,new FileAction() {
				public void act(File f) {
					if (!f.isDirectory()) {
						ret.add(f.toString().substring(prefix));
					}
				}
			});
		}catch (IOException ioe) {
		}
		return ret;
	}
}



