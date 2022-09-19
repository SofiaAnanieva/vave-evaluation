package org.argouml.model;


public class XmiException extends UmlException {
	private String publicId;
	private String systemId;
	private int lineNumber;
	private int columnNumber;
	public XmiException(String message) {
		super(message);
	}
	public XmiException(String message,Throwable c) {
		super(message,c);
	}
	public XmiException(Throwable c) {
		super(c);
	}
	public XmiException(String message,String publicId,String systemId,int line,int column) {
		super(message);
		init(publicId,systemId,line,column);
	}
	public XmiException(String message,String publicId,String systemId,int line,int column,Exception e) {
		super(message,e);
		init(publicId,systemId,line,column);
	}
	private void init(String publicId,String systemId,int line,int column) {
		this.publicId = publicId;
		this.systemId = systemId;
		lineNumber = line;
		columnNumber = column;
	}
	public String getPublicId() {
		return this.publicId;
	}
	public String getSystemId() {
		return this.systemId;
	}
	public int getLineNumber() {
		return this.lineNumber;
	}
	public int getColumnNumber() {
		return this.columnNumber;
	}
}



