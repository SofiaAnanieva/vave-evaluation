package org.argouml.persistence;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;
import org.argouml.persistence.AbstractFilePersister.ProgressMgr;


class XmiInputStream extends BufferedInputStream {
	private String tagName;
	private String endTagName;
	private String attributes;
	private boolean extensionFound;
	private boolean endFound;
	private boolean parsingExtension;
	private boolean readingName;
	private XmiExtensionParser xmiExtensionParser;
	private StringBuffer stringBuffer;
	private String type;
	private long eventSpacing;
	private long readCount;
	private ProgressMgr progressMgr;
	public XmiInputStream(InputStream inputStream,XmiExtensionParser extParser,long spacing,ProgressMgr prgrssMgr) {
		super(inputStream);
		eventSpacing = spacing;
		xmiExtensionParser = extParser;
		progressMgr = prgrssMgr;
	}
	@Override public synchronized int read()throws IOException {
		if (endFound) {
			extensionFound = false;
			parsingExtension = false;
			endFound = false;
			readingName = false;
			tagName = null;
			endTagName = null;
		}
		int ch = super.read();
		if (parsingExtension) {
			stringBuffer.append((char) ch);
		}
		++
		readCount;
		if (progressMgr != null&&readCount == eventSpacing) {
			try {
				readCount = 0;
				progressMgr.nextPhase();
			}catch (InterruptedException e) {
				throw new InterruptedIOException(e);
			}
		}
		if (xmiExtensionParser != null) {
			if (readingName) {
				if (isNameTerminator((char) ch)) {
					readingName = false;
					if (parsingExtension&&endTagName == null) {
						endTagName = "/" + tagName;
					}else if (tagName.equals("XMI.extension")) {
						extensionFound = true;
					}else if (tagName.equals(endTagName)) {
						endFound = true;
						xmiExtensionParser.parse(type,stringBuffer.toString());
						stringBuffer.delete(0,stringBuffer.length());
					}
				}else {
					tagName += (char) ch;
				}
			}
			if (extensionFound) {
				if (ch == '>') {
					extensionFound = false;
					callExtensionParser();
				}else {
					attributes += (char) ch;
				}
			}
			if (ch == '<') {
				readingName = true;
				tagName = "";
			}
		}
		return ch;
	}
	private void callExtensionParser() {
		String label = null;
		String extender = null;
		for (StringTokenizer st = new StringTokenizer(attributes," =");st.hasMoreTokens();) {
			String attributeType = st.nextToken();
			if (attributeType.equals("xmi.extender")) {
				extender = st.nextToken();
				extender = extender.substring(1,extender.length() - 1);
			}
			if (attributeType.equals("xmi.label")) {
				label = st.nextToken();
				label = label.substring(1,label.length() - 1);
			}
		}
		if ("ArgoUML".equals(extender)) {
			type = label;
			stringBuffer = new StringBuffer();
			parsingExtension = true;
			endTagName = null;
		}
	}
	@Override public synchronized int read(byte[]b,int off,int len)throws IOException {
		int cnt;
		for (cnt = 0;cnt < len;++cnt) {
			int read = read();
			if (read == -1) {
				break;
			}
			b[cnt + off] = (byte) read;
		}
		if (cnt > 0) {
			return cnt;
		}
		return-1;
	}
	private boolean isNameTerminator(char ch) {
		return(ch == '>'||Character.isWhitespace(ch));
	}
	@Override public void close()throws IOException {
	}
	public void realClose()throws IOException {
		super.close();
	}
}

class InterruptedIOException extends IOException {
	private static final long serialVersionUID = 5654808047803205851l;
	private InterruptedException cause;
	public InterruptedIOException(InterruptedException theCause) {
		cause = theCause;
	}
	public InterruptedException getInterruptedException() {
		return cause;
	}
}



