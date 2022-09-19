package org.argouml.profile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;


public class UserDefinedProfileHelper {
	public static JFileChooser createUserDefinedProfileFileChooser() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setFileFilter(new FileFilter() {
			public boolean accept(File file) {
				String s = file.getName().toLowerCase();
				return file.isDirectory()||(file.isFile()&&(s.endsWith(".xmi")||s.endsWith(".xml")||s.endsWith(".xmi.zip")||s.endsWith(".xml.zip")));
			}
			public String getDescription() {
				return"*.xmi *.xml *.xmi.zip *.xml.zip";
			}
		});
		return fileChooser;
	}
	public static List<File>getFileList(File[]fileArray) {
		List<File>files = new ArrayList<File>();
		for (int i = 0;i < fileArray.;i++) {
			File file = fileArray[i];
			files.addAll(getList(file));
		}
		return files;
	}
	private static List<File>getList(File file) {
		List<File>results = new ArrayList<File>();
		List<File>toDoDirectories = new LinkedList<File>();
		Set<File>seenDirectories = new HashSet<File>();
		toDoDirectories.add(file);
		while (!toDoDirectories.isEmpty()) {
			File curDir = toDoDirectories.remove(0);
			if (!curDir.isDirectory()) {
				results.add(curDir);
				continue;
			}
			File[]files = curDir.listFiles();
			if (files != null) {
				for (File curFile:curDir.listFiles()) {
					if (curFile.isDirectory()) {
						if (!seenDirectories.contains(curFile)) {
							toDoDirectories.add(curFile);
							seenDirectories.add(curFile);
						}
					}else {
						String s = curFile.getName().toLowerCase();
						if (s.endsWith(".xmi")||s.endsWith(".xml")||s.endsWith(".xmi.zip")||s.endsWith(".xml.zip")) {
							results.add(curFile);
						}
					}
				}
			}
		}
		return results;
	}
}



