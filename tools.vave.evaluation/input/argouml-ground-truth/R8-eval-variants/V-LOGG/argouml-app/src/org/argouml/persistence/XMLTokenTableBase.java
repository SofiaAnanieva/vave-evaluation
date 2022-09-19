package org.argouml.persistence;

import java.util.Hashtable;
import org.apache.log4j.Logger;


abstract class XMLTokenTableBase {
	private static final Logger LOG = Logger.getLogger(XMLTokenTableBase.class);
	private Hashtable tokens = null;
	private boolean dbg = false;
	private String[]openTags = new String[100];
	private int[]openTokens = new int[100];
	private int numOpen = 0;
	public XMLTokenTableBase(int tableSize) {
		tokens = new Hashtable(tableSize);
		setupTokens();
	}
	public final int toToken(String s,boolean push) {
		if (push)openTags[++numOpen] = s;else if (s.equals(openTags[numOpen])) {
			LOG.debug("matched: " + s);
			return openTokens[numOpen--];
		}
		Integer i = (Integer) tokens.get(s);
		if (i != null) {
			openTokens[numOpen] = i.intValue();
			return openTokens[numOpen];
		}else return-1;
	}
	public void setDbg(boolean d) {
		dbg = d;
	}
	public boolean getDbg() {
		return dbg;
	}
	protected void addToken(String s,Integer i) {
		boolean error = false;
		if (dbg) {
			if (tokens.contains(i)||tokens.containsKey(s)) {
				LOG.error("ERROR: token table already contains " + s);
				error = true;
			}
		}
		tokens.put(s,i);
		if (dbg&&!error) {
			LOG.debug("NOTE: added \'" + s + "\' to token table");
		}
	}
	public boolean contains(String token) {
		return tokens.containsKey(token);
	}
	protected abstract void setupTokens();
}



