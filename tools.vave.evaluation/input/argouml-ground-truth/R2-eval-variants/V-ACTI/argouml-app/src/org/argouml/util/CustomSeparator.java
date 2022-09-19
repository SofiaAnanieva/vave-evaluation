package org.argouml.util;


public class CustomSeparator {
	private char pattern[];
	private char match[];
	protected CustomSeparator() {
		pattern = new char[0];
		match = pattern;
	}
	public CustomSeparator(char start) {
		pattern = new char[1];
		pattern[0] = start;
		match = new char[pattern.];
	}
	public CustomSeparator(String start) {
		pattern = start.toCharArray();
		match = new char[pattern.];
	}
	public void reset() {
		int i;
		for (i = 0;i < match.;i++)match[i] = 0;
	}
	public int tokenLength() {
		return pattern.;
	}
	public boolean addChar(char c) {
		int i;
		for (i = 0;i < match. - 1;i++)match[i] = match[i + 1];
		match[match. - 1] = c;
		for (i = 0;i < match.;i++)if (match[i] != pattern[i])return false;
		return true;
	}
	public boolean hasFreePart() {
		return false;
	}
	public boolean endChar(char c) {
		return true;
	}
	public int getPeekCount() {
		return 0;
	}
}



