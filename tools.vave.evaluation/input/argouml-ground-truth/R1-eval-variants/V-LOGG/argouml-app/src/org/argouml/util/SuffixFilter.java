package org.argouml.util;

import java.io.File;
import javax.swing.filechooser.FileFilter;


public class SuffixFilter extends FileFilter {
	private final String[]suffixes;
	private final String desc;
	public SuffixFilter(String suffix,String d) {
		suffixes = new String[] {suffix};
		desc = d;
	}
	public SuffixFilter(String[]s,String d) {
		suffixes = new String[s.];
		System.arraycopy(s,0,suffixes,0,s.);
		desc = d;
	}
	public boolean accept(File f) {
		if (f == null) {
			return false;
		}
		if (f.isDirectory()) {
			return true;
		}
		String extension = getExtension(f);
		for (String suffix:suffixes) {
			if (suffix.equalsIgnoreCase(extension)) {
				return true;
			}
		}
		return false;
	}
	public static String getExtension(File f) {
		if (f == null) {
			return null;
		}
		return getExtension(f.getName());
	}
	public static String getExtension(String filename) {
		int i = filename.lastIndexOf('.');
		if (i > 0&&i < filename.length() - 1) {
			return filename.substring(i + 1).toLowerCase();
		}
		return null;
	}
	public String getDescription() {
		StringBuffer result = new StringBuffer(desc);
		result.append(" (");
		for (int i = 0;i < suffixes.;i++) {
			result.append('.');
			result.append(suffixes[i]);
			if (i < suffixes. - 1) {
				result.append(", ");
			}
		}
		result.append(')');
		return result.toString();
	}
	public String getSuffix() {
		return suffixes[0];
	}
	public String[]getSuffixes() {
		return suffixes;
	}
	public String toString() {
		return getDescription();
	}
}



