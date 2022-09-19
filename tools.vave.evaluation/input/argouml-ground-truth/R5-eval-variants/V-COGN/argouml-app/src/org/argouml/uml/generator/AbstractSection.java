package org.argouml.uml.generator;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public abstract class AbstractSection {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private Map<String,String>mAry;
	public AbstractSection() {
		mAry = new HashMap<String,String>();
	}
	public static String generate(String id,String indent) {
		return"";
	}
	public void write(String filename,String indent,boolean outputLostSections) {
		try {
			FileReader f = new FileReader(filename);
			BufferedReader fr = new BufferedReader(f);
			FileWriter fw = new FileWriter(filename + ".out");
			String line = "";
			line = fr.readLine();
			while (line != null) {
				String sectionId = getSectId(line);
				if (sectionId != null) {
					String content = mAry.get(sectionId);
					if (content != null) {
						fw.write(line + LINE_SEPARATOR);
						fw.write(content);
						String endSectionId = null;
						do {
							line = fr.readLine();
							if (line == null) {
								throw new EOFException("Reached end of file while looking " + "for the end of section with ID = \"" + sectionId + "\"!");
							}
							endSectionId = getSectId(line);
						}while (endSectionId == null);
					}
					mAry.remove(sectionId);
				}
				fw.write(line);
				line = fr.readLine();
				if (line != null) {
					fw.write(LINE_SEPARATOR);
				}
			}
			if ((!mAry.isEmpty())&&(outputLostSections)) {
				fw.write("/* lost code following: " + LINE_SEPARATOR);
				Set mapEntries = mAry.entrySet();
				Iterator itr = mapEntries.iterator();
				while (itr.hasNext()) {
					Map.
						Entry entry = (Map.Entry) itr.next();
					fw.write(indent + "// section " + entry.getKey() + " begin" + LINE_SEPARATOR);
					fw.write((String) entry.getValue());
					fw.write(indent + "// section " + entry.getKey() + " end" + LINE_SEPARATOR);
				}
				fw.write("*/");
			}
			fr.close();
			fw.close();
		}catch (IOException e) {
		}
	}
	public void read(String filename) {
		try {
			FileReader f = new FileReader(filename);
			BufferedReader fr = new BufferedReader(f);
			String line = "";
			StringBuilder content = new StringBuilder();
			boolean inSection = false;
			while (line != null) {
				line = fr.readLine();
				if (line != null) {
					if (inSection) {
						String sectionId = getSectId(line);
						if (sectionId != null) {
							inSection = false;
							mAry.put(sectionId,content.toString());
							content = new StringBuilder();
						}else {
							content.append(line + LINE_SEPARATOR);
						}
					}else {
						String sectionId = getSectId(line);
						if (sectionId != null) {
							inSection = true;
						}
					}
				}
			}
			fr.close();
		}catch (IOException e) {
		}
	}
	public static String getSectId(String line) {
		final String begin = "// section ";
		final String end1 = " begin";
		final String end2 = " end";
		int first = line.indexOf(begin);
		int second = line.indexOf(end1);
		if (second < 0) {
			second = line.indexOf(end2);
		}
		String s = null;
		if ((first >= 0)&&(second >= 0)) {
			first = first + begin.length();
			s = line.substring(first,second);
		}
		return s;
	}
}



