package org.argouml.uml.generator;


public class SourceUnit {
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private Language language;
	private String name;
	private String basePath;
	private String content;
	public SourceUnit(String theName,String path,String theContent) {
		setName(theName);
		setBasePath(path);
		this.content = theContent;
	}
	public SourceUnit(String fullName,String theContent) {
		setFullName(fullName);
		content = theContent;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String theContent) {
		this.content = theContent;
	}
	public String getName() {
		return name;
	}
	public void setName(String filename) {
		int sep = filename.lastIndexOf(FILE_SEPARATOR);
		if (sep >= 0) {
			name = filename.substring(sep + FILE_SEPARATOR.length());
		}else {
			name = filename;
		}
	}
	public String getBasePath() {
		return basePath;
	}
	public void setBasePath(String path) {
		if (path.endsWith(FILE_SEPARATOR)) {
			basePath = path.substring(0,path.length() - FILE_SEPARATOR.length());
		}else {
			basePath = path;
		}
	}
	public String getFullName() {
		return basePath + System.getProperty("file.separator") + name;
	}
	public void setFullName(String path) {
		int sep = path.lastIndexOf(FILE_SEPARATOR);
		if (sep >= 0) {
			basePath = path.substring(0,sep);
			name = path.substring(sep + FILE_SEPARATOR.length());
		}else {
			basePath = "";
			name = path;
		}
	}
	public Language getLanguage() {
		return language;
	}
	public void setLanguage(Language lang) {
		this.language = lang;
	}
}



