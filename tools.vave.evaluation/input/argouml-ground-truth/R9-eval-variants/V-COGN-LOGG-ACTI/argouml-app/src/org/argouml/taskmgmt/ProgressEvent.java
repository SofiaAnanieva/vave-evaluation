package org.argouml.taskmgmt;

import java.util.EventObject;


public class ProgressEvent extends EventObject {
	private long length;
	private long position;
	public ProgressEvent(Object source,long thePosition,long theLength) {
		super(source);
		this.length = theLength;
		this.position = thePosition;
	}
	public long getPosition() {
		return position;
	}
	public long getLength() {
		return length;
	}
	private static final long serialVersionUID = -440923505939663713l;
}



