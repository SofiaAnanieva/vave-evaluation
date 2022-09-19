package org.argouml.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;


class TokenSep {
	private TokenSep next = null;
	private final String theString;
	private final int length;
	private int pattern;
	public TokenSep(String str) {
		theString = str;
		length = str.length();
		if (length > 32)throw new IllegalArgumentException("TokenSep " + str + " is " + length + " (> 32) chars long");
		pattern = 0;
	}
	public boolean addChar(char c) {
		int i;
		pattern <<= 1;
		pattern |= 1;
		for (i = 0;i < length;i++) {
			if (theString.charAt(i) != c) {
				pattern &= ~(1 << i);
			}
		}
		return(pattern&(1 << (length - 1))) != 0;
	}
	public void reset() {
		pattern = 0;
	}
	public int length() {
		return length;
	}
	public String getString() {
		return theString;
	}
	public void setNext(TokenSep n) {
		this.next = n;
	}
	public TokenSep getNext() {
		return next;
	}
}

class QuotedStringSeparator extends CustomSeparator {
	private final char escChr;
	private final char startChr;
	private final char stopChr;
	private boolean esced;
	private int tokLen;
	private int level;
	public QuotedStringSeparator(char q,char esc) {
		super(q);
		esced = false;
		escChr = esc;
		startChr = 0;
		stopChr = q;
		tokLen = 0;
		level = 1;
	}
	public QuotedStringSeparator(char sq,char eq,char esc) {
		super(sq);
		esced = false;
		escChr = esc;
		startChr = sq;
		stopChr = eq;
		tokLen = 0;
		level = 1;
	}
	public void reset() {
		super.reset();
		tokLen = 0;
		level = 1;
	}
	public int tokenLength() {
		return super.tokenLength() + tokLen;
	}
	public boolean hasFreePart() {
		return true;
	}
	public boolean endChar(char c) {
		tokLen++;
		if (esced) {
			esced = false;
			return false;
		}
		if (escChr != 0&&c == escChr) {
			esced = true;
			return false;
		}
		if (startChr != 0&&c == startChr)level++;
		if (c == stopChr)level--;
		return level <= 0;
	}
}

class ExprSeparatorWithStrings extends CustomSeparator {
	private boolean isSQuot;
	private boolean isDQuot;
	private boolean isEsc;
	private int tokLevel;
	private int tokLen;
	public ExprSeparatorWithStrings() {
		super('(');
		isEsc = false;
		isSQuot = false;
		isDQuot = false;
		tokLevel = 1;
		tokLen = 0;
	}
	public void reset() {
		super.reset();
		isEsc = false;
		isSQuot = false;
		isDQuot = false;
		tokLevel = 1;
		tokLen = 0;
	}
	public int tokenLength() {
		return super.tokenLength() + tokLen;
	}
	public boolean hasFreePart() {
		return true;
	}
	public boolean endChar(char c) {
		tokLen++;
		if (isSQuot) {
			if (isEsc) {
				isEsc = false;
				return false;
			}
			if (c == '\\')isEsc = true;else if (c == '\'')isSQuot = false;
			return false;
		}else if (isDQuot) {
			if (isEsc) {
				isEsc = false;
				return false;
			}
			if (c == '\\')isEsc = true;else if (c == '\"')isDQuot = false;
			return false;
		}else {
			if (c == '\'')isSQuot = true;else if (c == '\"')isDQuot = true;else if (c == '(')tokLevel++;else if (c == ')')tokLevel--;
			return tokLevel <= 0;
		}
	}
}

class LineSeparator extends CustomSeparator {
	private boolean hasCr;
	private boolean hasLf;
	private boolean hasPeeked;
	public LineSeparator() {
		hasCr = false;
		hasLf = false;
		hasPeeked = false;
	}
	public void reset() {
		super.reset();
		hasCr = false;
		hasLf = false;
		hasPeeked = false;
	}
	public int tokenLength() {
		return hasCr&&hasLf?2:1;
	}
	public int getPeekCount() {
		return hasPeeked?1:0;
	}
	public boolean hasFreePart() {
		return!hasLf;
	}
	public boolean addChar(char c) {
		if (c == '\n') {
			hasLf = true;
			return true;
		}
		if (c == '\r') {
			hasCr = true;
			return true;
		}
		return false;
	}
	public boolean endChar(char c) {
		if (c == '\n') {
			hasLf = true;
		}else {
			hasPeeked = true;
		}
		return true;
	}
}

public class MyTokenizer implements Enumeration {
	public static final CustomSeparator SINGLE_QUOTED_SEPARATOR = new QuotedStringSeparator('\'','\\');
	public static final CustomSeparator DOUBLE_QUOTED_SEPARATOR = new QuotedStringSeparator('\"','\\');
	public static final CustomSeparator PAREN_EXPR_SEPARATOR = new QuotedStringSeparator('(',')','\0');
	public static final CustomSeparator PAREN_EXPR_STRING_SEPARATOR = new ExprSeparatorWithStrings();
	public static final CustomSeparator LINE_SEPARATOR = new LineSeparator();
	private int sIdx;
	private final int eIdx;
	private int tokIdx;
	private final String source;
	private final TokenSep delims;
	private String savedToken;
	private int savedIdx;
	private List customSeps;
	private String putToken;
	public MyTokenizer(String string,String delim) {
		source = string;
		delims = parseDelimString(delim);
		sIdx = 0;
		tokIdx = 0;
		eIdx = string.length();
		savedToken = null;
		customSeps = null;
		putToken = null;
	}
	public MyTokenizer(String string,String delim,CustomSeparator sep) {
		source = string;
		delims = parseDelimString(delim);
		sIdx = 0;
		tokIdx = 0;
		eIdx = string.length();
		savedToken = null;
		customSeps = new ArrayList();
		customSeps.add(sep);
	}
	public MyTokenizer(String string,String delim,Collection seps) {
		source = string;
		delims = parseDelimString(delim);
		sIdx = 0;
		tokIdx = 0;
		eIdx = string.length();
		savedToken = null;
		customSeps = new ArrayList(seps);
	}
	public boolean hasMoreTokens() {
		return sIdx < eIdx||savedToken != null||putToken != null;
	}
	public String nextToken() {
		CustomSeparator csep;
		TokenSep sep;
		String s = null;
		int i,j;
		if (putToken != null) {
			s = putToken;
			putToken = null;
			return s;
		}
		if (savedToken != null) {
			s = savedToken;
			tokIdx = savedIdx;
			savedToken = null;
			return s;
		}
		if (sIdx >= eIdx)throw new NoSuchElementException("No more tokens available");
		for (sep = delims;sep != null;sep = sep.getNext())sep.reset();
		if (customSeps != null) {
			for (i = 0;i < customSeps.size();i++)((CustomSeparator) customSeps.get(i)).reset();
		}
		for (i = sIdx;i < eIdx;i++) {
			char c = source.charAt(i);
			for (j = 0;customSeps != null&&j < customSeps.size();j++) {
				csep = (CustomSeparator) customSeps.get(j);
				if (csep.addChar(c))break;
			}
			if (customSeps != null&&j < customSeps.size()) {
				csep = (CustomSeparator) customSeps.get(j);
				while (csep.hasFreePart()&&i + 1 < eIdx)if (csep.endChar(source.charAt(++i)))break;
				i -= Math.min(csep.getPeekCount(),i);
				int clen = Math.min(i + 1,source.length());
				if (i - sIdx + 1 > csep.tokenLength()) {
					s = source.substring(sIdx,i - csep.tokenLength() + 1);
					savedIdx = i - csep.tokenLength() + 1;
					savedToken = source.substring(savedIdx,clen);
				}else {
					s = source.substring(sIdx,clen);
				}
				tokIdx = sIdx;
				sIdx = i + 1;
				break;
			}
			for (sep = delims;sep != null;sep = sep.getNext())if (sep.addChar(c))break;
			if (sep != null) {
				if (i - sIdx + 1 > sep.length()) {
					s = source.substring(sIdx,i - sep.length() + 1);
					savedIdx = i - sep.length() + 1;
					savedToken = sep.getString();
				}else {
					s = sep.getString();
				}
				tokIdx = sIdx;
				sIdx = i + 1;
				break;
			}
		}
		if (s == null) {
			s = source.substring(sIdx);
			tokIdx = sIdx;
			sIdx = eIdx;
		}
		return s;
	}
	public Object nextElement() {
		return nextToken();
	}
	public boolean hasMoreElements() {
		return hasMoreTokens();
	}
	public int getTokenIndex() {
		return tokIdx;
	}
	public void putToken(String s) {
		if (s == null)throw new NullPointerException("Cannot put a null token");
		putToken = s;
	}
	private static TokenSep parseDelimString(String str) {
		TokenSep first = null;
		TokenSep p = null;
		int idx0,idx1,length;
		StringBuilder val = new StringBuilder();
		char c;
		length = str.length();
		for (idx0 = 0;idx0 < length;) {
			for (idx1 = idx0;idx1 < length;idx1++) {
				c = str.charAt(idx1);
				if (c == '\\') {
					idx1++;
					if (idx1 < length)val.append(str.charAt(idx1));
				}else if (c == ',') {
					break;
				}else {
					val.append(c);
				}
			}
			idx1 = Math.min(idx1,length);
			if (idx1 > idx0) {
				p = new TokenSep(val.toString());
				val = new StringBuilder();
				p.setNext(first);
				first = p;
			}
			idx0 = idx1 + 1;
		}
		return first;
	}
}



