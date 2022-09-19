package org.argouml.uml.reveng;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.argouml.taskmgmt.ProgressMonitor;
import org.argouml.util.SuffixFilter;


public class FileImportUtils {
	public static List<File>getList(File file,boolean recurse,SuffixFilter[]filters,ProgressMonitor monitor) {
		if (file == null) {
			return Collections.emptyList();
		}
		List<File>results = new ArrayList<File>();
		List<File>toDoDirectories = new LinkedList<File>();
		Set<File>seenDirectories = new HashSet<File>();
		toDoDirectories.add(file);
		while (!toDoDirectories.isEmpty()) {
			if (monitor != null&&monitor.isCanceled()) {
				return Collections.emptyList();
			}
			File curDir = toDoDirectories.remove(0);
			if (!curDir.isDirectory()) {
				results.add(curDir);
				continue;
			}
			File[]files = curDir.listFiles();
			if (files != null) {
				for (File curFile:curDir.listFiles()) {
					if (curFile.isDirectory()) {
						if (recurse&&!seenDirectories.contains(curFile)) {
							toDoDirectories.add(curFile);
							seenDirectories.add(curFile);
						}
					}else {
						if (matchesSuffix(curFile,filters)) {
							results.add(curFile);
						}
					}
				}
			}
		}
		return results;
	}
	public static boolean matchesSuffix(Object file,SuffixFilter[]filters) {
		if (!(file instanceof File)) {
			return false;
		}
		if (filters != null) {
			for (int i = 0;i < filters.length;i++) {
				if (filters[i].accept((File) file)) {
					return true;
				}
			}
		}
		return false;
	}
}



