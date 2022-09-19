package org.argouml.persistence;

import java.util.Hashtable;


abstract class XMLTokenTableBase {
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
				error = true;
			}
		}
		tokens.put(s,i);
		if (dbg&&!error) {
		}
	}
	public boolean contains(String token) {
		return tokens.containsKey(token);
	}
	protected abstract void setupTokens();
}



