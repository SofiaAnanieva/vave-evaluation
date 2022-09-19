package org.argouml.persistence;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;


class XmlInputStream extends BufferedInputStream {
	private boolean xmlStarted;
	private boolean inTag;
	private StringBuffer currentTag = new StringBuffer();
	private boolean endStream;
	private String tagName;
	private String endTagName;
	private Map attributes;
	private boolean childOnly;
	private int instanceCount;
	private static final Logger LOG = Logger.getLogger(XmlInputStream.class);
	public XmlInputStream(InputStream inStream,String theTag,long theLength,long theEventSpacing) {
		super(inStream);
		tagName = theTag;
		endTagName = '/' + theTag;
		attributes = null;
		childOnly = false;
	}
	public synchronized void reopen(String theTag,Map attribs,boolean child) {
		endStream = false;
		xmlStarted = false;
		inTag = false;
		tagName = theTag;
		endTagName = '/' + theTag;
		attributes = attribs;
		childOnly = child;
	}
	public synchronized void reopen(String theTag) {
		endStream = false;
		xmlStarted = false;
		inTag = false;
		tagName = theTag;
		endTagName = '/' + theTag;
		attributes = null;
		childOnly = false;
	}
	public synchronized int read()throws IOException {
		if (!xmlStarted) {
			skipToTag();
			xmlStarted = true;
		}
		if (endStream) {
			return-1;
		}
		int ch = super.read();
		endStream = isLastTag(ch);
		return ch;
	}
	public synchronized int read(byte[]b,int off,int len)throws IOException {
		if (!xmlStarted) {
			skipToTag();
			xmlStarted = true;
		}
		if (endStream) {
			return-1;
		}
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
	private boolean isLastTag(int ch) {
		if (ch == '<') {
			inTag = true;
			currentTag.setLength(0);
		}else if (ch == '>') {
			inTag = false;
			String tag = currentTag.toString();
			if (tag.equals(endTagName)||(currentTag.charAt(currentTag.length() - 1) == '/'&&tag.startsWith(tagName)&&tag.indexOf(' ') == tagName.indexOf(' '))) {
				return true;
			}
		}else if (inTag) {
			currentTag.append((char) ch);
		}
		return false;
	}
	private void skipToTag()throws IOException {
		char[]searchChars = tagName.toCharArray();
		int i;
		boolean found;
		while (true) {
			if (!childOnly) {
				mark(1000);
			}
			while (realRead() != '<') {
				if (!childOnly) {
					mark(1000);
				}
			}
			found = true;
			for (i = 0;i < tagName.length();++i) {
				int c = realRead();
				if (c != searchChars[i]) {
					found = false;
					break;
				}
			}
			int terminator = realRead();
			if (found&&!isNameTerminator((char) terminator)) {
				found = false;
			}
			if (found) {
				if (attributes != null) {
					Map attributesFound = new HashMap();
					if (terminator != '>') {
						attributesFound = readAttributes();
					}
					Iterator it = attributes.entrySet().iterator();
					while (found&&it.hasNext()) {
						Map.
							Entry pair = (Map.Entry) it.next();
						if (!pair.getValue().equals(attributesFound.get(pair.getKey()))) {
							found = false;
						}
					}
				}
			}
			if (found) {
				if (instanceCount < 0) {
					found = false;
					++
					instanceCount;
				}
			}
			if (found) {
				if (childOnly) {
					mark(1000);
					while (realRead() != '<') {
					}
					tagName = "";
					char ch = (char) realRead();
					while (!isNameTerminator(ch)) {
						tagName += ch;
						ch = (char) realRead();
					}
					endTagName = "/" + tagName;
					LOG.info("Start tag = " + tagName);
					LOG.info("End tag = " + endTagName);
				}
				reset();
				return;
			}
		}
	}
	private boolean isNameTerminator(char ch) {
		return(ch == '>'||Character.isWhitespace(ch));
	}
	private Map readAttributes()throws IOException {
		Map attributesFound = new HashMap();
		int character;
		while ((character = realRead()) != '>') {
			if (!Character.isWhitespace((char) character)) {
				StringBuffer attributeName = new StringBuffer();
				attributeName.append((char) character);
				while ((character = realRead()) != '='&&!Character.isWhitespace((char) character)) {
					attributeName.append((char) character);
				}
				while (Character.isWhitespace((char) character)) {
					character = realRead();
				}
				if (character != '=') {
					throw new IOException("Expected = sign after attribute " + attributeName);
				}
				int quoteSymbol = realRead();
				while (Character.isWhitespace((char) quoteSymbol)) {
					quoteSymbol = realRead();
				}
				if (quoteSymbol != '\"'&&quoteSymbol != '\'') {
					throw new IOException("Expected \" or \' around attribute value after " + "attribute " + attributeName);
				}
				StringBuffer attributeValue = new StringBuffer();
				while ((character = realRead()) != quoteSymbol) {
					attributeValue.append((char) character);
				}
				attributesFound.put(attributeName.toString(),attributeValue.toString());
			}
		}
		return attributesFound;
	}
	public void close()throws IOException {
	}
	public void realClose()throws IOException {
		super.close();
	}
	private int realRead()throws IOException {
		int read = super.read();
		if (read == -1) {
			throw new IOException("Tag " + tagName + " not found");
		}
		return read;
	}
}



